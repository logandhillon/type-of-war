package com.logandhillon.typeofwar.networking;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.GameSceneMismatchException;
import com.logandhillon.typeofwar.game.LobbyGameScene;
import com.logandhillon.typeofwar.networking.proto.PlayerProto;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A game server handles all outgoing communications to {@link GameClient}s via a valid network connection.
 * <p>
 * The server allows multiple clients to connect (using multi-threading to handle each connection independently) and
 * communicates using {@link GamePacket}s.
 *
 * @author Logan Dhillon
 * @see GameClient
 */
public class GameServer implements Runnable {
    private static final Logger LOG             = LoggerContext.getContext().getLogger(GameServer.class);
    private static final int    PORT            = 20670;
    private static final int    MAX_CONNECTIONS = 8;

    private volatile boolean      running;
    private final    TypeOfWar    game;
    private          ServerSocket socket;

    /** the list of ALL active client connections, including unregistered ones. */
    private final Set<Socket> clients = Collections.synchronizedSet(new HashSet<>());

    /** list of all REGISTERED client connections; maps socket to name */
    private final HashMap<Socket, ConnectionDetails> registeredClients = new HashMap<>();

    private record ConnectionDetails(String name, Color color, int team, PacketWriter out) {}

    public GameServer(TypeOfWar game) {
        this.game = game;
    }

    /**
     * Starts the server socket and the acceptor thread, which listens for incoming connections and handles them
     * accordingly.
     *
     * @throws IOException if the server socket fails to start.
     */
    public void start() throws IOException {
        LOG.info("Starting server on port {}...", PORT);
        socket = new ServerSocket(PORT);
        running = true;
        new Thread(this, "ServerAcceptor").start();
    }

    /**
     * Tries to stop the server gracefully, ending the acceptor and closing the thread.
     *
     * @throws IOException if the socket fails to close.
     */
    public void stop() throws IOException {
        LOG.info("Stopping server gracefully");
        running = false;
        if (socket != null) {
            LOG.info("Closing server socket now");
            socket.close();
        }
        LOG.info("Closing {} client connection(s)", clients.size());
        synchronized (clients) {
            for (Socket c: clients) {
                try {
                    c.close();
                } catch (IOException ignored) {
                }
            }
            clients.clear();
        }
    }

    /**
     * The method that will run when this server is started in the thread.
     *
     * @see GameServer#start()
     */
    @Override
    public void run() {
        while (running) {
            try {
                Socket client = socket.accept();
                handleClient(client);
            } catch (IOException e) {
                if (running) LOG.error(e);
            }
        }
    }

    /**
     * Runs when a new client connects; this starts a dedicated thread to handle this client.
     *
     * @param client the incoming client socket
     */
    private void handleClient(Socket client) {
        new Thread(() -> {
            LOG.info("Incoming client connection at {}", client.getInetAddress().getHostAddress());
            clients.add(client);

            try (client;
                 var out = new PacketWriter(client.getOutputStream());
                 DataInputStream dataInputStream = new DataInputStream(client.getInputStream())
            ) {
                while (true) {
                    int length;
                    try {
                        length = dataInputStream.readInt();
                    } catch (IOException e) {
                        break; // client disconnected or stream closed
                    }
                    if (length <= 0) {
                        LOG.warn("Received invalid packet length {} from {}", length, client.getInetAddress());
                        break;
                    }
                    byte[] data = new byte[length];
                    dataInputStream.readFully(data);
                    parseRequest(client, GamePacket.deserialize(data), out);
                }
            } catch (IOException e) {
                LOG.error(e);
            } finally {
                clients.remove(client);
            }
        }, "ClientHandler-" + client.getInetAddress()).start();
    }

    /**
     * Parses a request from a client
     *
     * @param client the client socket that this packet is from
     * @param packet the packet itself, from the client
     * @param out    the output stream used to respond to the client.
     */
    private void parseRequest(Socket client, GamePacket packet, PacketWriter out) {
        if (packet == null) return;

        LOG.debug("Received {} from {}", packet.type(), client.getInetAddress());

        try {
            switch (packet.type()) {
                case CLT_REQ_CONN -> handleClientRegistration(client, packet, out);
            }
        } catch (IOException e) {
            LOG.error("Failed to close client at {}", client.getInetAddress(), e);
        }
    }

    /**
     * Handles a new client request and tries to register it or defer it. Should be called when receiving a CLT_REQ_CONN
     * packet.
     */
    private void handleClientRegistration(Socket client, GamePacket packet, PacketWriter out) throws IOException {
        try {
            if (registeredClients.size() < MAX_CONNECTIONS) {
                PlayerProto.PlayerData pkt = PlayerProto.PlayerData.parseFrom(packet.payload());

                // check if name is already used
                if (registeredClients.values().stream().anyMatch(p -> p.name.equals(pkt.getName()))) {
                    LOG.info(
                            "Denying connection from {} (name '{}' in use)", client.getInetAddress(),
                            packet.payload());
                    out.send(GamePacket.Type.SRV_DENY_CONN__USERNAME_TAKEN);
                    client.close();
                    return;
                }

                // get the lobby in advance, so it can throw GameSceneMismatchException if we shouldn't do this
                var lobby = game.getActiveScene(LobbyGameScene.class);

                // all good now! register the client
                Color color = Color.rgb(pkt.getR(), pkt.getG(), pkt.getB());
                lobby.addPlayer(pkt.getName(), color, 1); // TODO: send player color in join packet
                registeredClients.put(client, new ConnectionDetails(pkt.getName(), color, 1, out));

                // get the players on each team...

                // ... and send them to the client
                out.send(new GamePacket(
                        GamePacket.Type.SRV_ALLOW_CONN,
                        PlayerProto.Lobby.newBuilder()
                                         .setName(lobby.getRoomName())
                                         .addAllTeam1(getTeam(1).toList())
                                         .addAllTeam2(getTeam(2).toList())
                                         .build()));

                LOG.info("Registered new client '{}' at {}!", pkt.getName(), client.getInetAddress());
            }

            // check if srv is full
            else {
                LOG.info("Denying connection from {} (server full)", client.getInetAddress());
                out.send(GamePacket.Type.SRV_DENY_CONN__FULL);
                client.close();
            }
        } catch (GameSceneMismatchException e) {
            LOG.warn(
                    "Server got a registration request, but was not ready for it. Closing client at {}.",
                    client.getInetAddress());
            out.send(GamePacket.Type.SRV_UNEXPECTED);
            client.close();
        }
    }

    /**
     * Without checking who, this broadcasts the same packet to all registered clients.
     *
     * @param pkt the packet to broadcast
     */
    public void broadcast(GamePacket pkt) {
        for (ConnectionDetails conn: registeredClients.values()) {
            conn.out.send(pkt);
        }
    }

    public Stream<PlayerProto.PlayerData> getTeam(int team) {
        // stream registered clients into playerdata, filtering only those that match the team
        var list = registeredClients.values().stream()
                                    .filter(d -> d.team == team)
                                    .map(d -> PlayerProto.PlayerData.newBuilder()
                                                                    .setName(d.name)
                                                                    .setR((int)d.color.getRed() * 255)
                                                                    .setG((int)d.color.getRed() * 255)
                                                                    .setB((int)d.color.getRed() * 255)
                                                                    .build()
                                    );

        // add the host to team 1
        if (team == 1) {
            list = Stream.concat(
                    Stream.of(PlayerProto.PlayerData.newBuilder().setName("You").setR(255).setG(0).setB(0).build()),
                    list);
        }

        return list;
    }
}