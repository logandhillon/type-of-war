package com.logandhillon.typeofwar.networking;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.game.LobbyGameScene;
import com.logandhillon.typeofwar.networking.proto.PlayerProto;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A game client handles all outgoing communications to the {@link GameServer} via a valid network connection.
 * <p>
 * The client connects to a server using an {@link java.net.InetAddress} and communicates using {@link GamePacket}s.
 *
 * @author Logan Dhillon
 * @see GameServer
 */
public class GameClient {
    private static final Logger LOG = LoggerContext.getContext().getLogger(GameClient.class);

    private final String    host;
    private final int       port;
    private final TypeOfWar game;

    private Socket           socket;
    private DataInputStream  in;
    private DataOutputStream out;

    /** if this client is registered with a remote server */
    private boolean isRegistered;

    /**
     * Sets up a new client, does not connect to the server.
     *
     * @param host the FQDN or IP address of the server to connect to
     * @param port the port (default 20670)
     *
     * @see GameClient#connect()
     */
    public GameClient(String host, int port, TypeOfWar game) {
        this.host = host;
        this.port = port;
        this.game = game;

        isRegistered = false;
    }

    /**
     * Connects to the pre-defined server, sends a REQ_CONN packet, then starts the listener thread.
     *
     * @throws IOException if the socket throws an exception during creation
     * @see GameClient#readLoop()
     */
    public void connect() throws IOException {
        LOG.info("Connecting to server at {}:{}...", host, port);
        socket = new Socket(host, port);

        // setup remote IO
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        // ask to connect
        send(new GamePacket(
                GamePacket.Type.CLT_REQ_CONN,
                PlayerProto.PlayerData.newBuilder()
                                      .setName(System.getProperty("user.name"))
                                      .setR(1).setG(1).setB(1)
                                      .build()));

        new Thread(this::readLoop, "Client-ReadLoop").start();
    }

    /**
     * The listener thread of the client, handles incoming communication from the server, deserializes it, and sends it
     * to {@link GameClient#parseResponse(GamePacket)}.
     *
     * @apiNote This should be run in a separate thread, as it is a blocking action.
     */
    private void readLoop() {
        try {
            while (true) {
                int length;
                try {
                    length = in.readInt();
                } catch (IOException e) {
                    break; // client disconnected or stream closed
                }
                if (length <= 0) {
                    LOG.warn("Received invalid packet length {} from SERVER", length);
                    break;
                }
                byte[] data = new byte[length];
                in.readFully(data);
                parseResponse(GamePacket.deserialize(data));
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Handles an incoming packet
     *
     * @param packet the deserialized packet from the server
     */
    private void parseResponse(GamePacket packet) throws IOException {
        if (packet == null) return;

        // FIXME: client never gets this!!! hahahahahhahHhahahahhahAHHAHAHHAHAHAHAAAHA WHY DOESNT IT WORK ?????? KJSHKJDHALKSJHAKJSGKUSYDI@GIALKJALSJKHSDLKJSLKFDJALKSJ
        LOG.debug("Received {} from SERVER", packet.type());

        switch (packet.type()) {
            case SRV_ALLOW_CONN -> {
                isRegistered = true;
                LOG.info("Successfully registered with remote server");

                var data = PlayerProto.Lobby.parseFrom(packet.payload());

                var lobby = new LobbyGameScene(game, data.getName(), false);
                for (var p: data.getTeam1List()) lobby.addPlayer(p.getName(), Color.rgb(p.getR(), p.getG(), p.getB()), 1);
                for (var p: data.getTeam2List()) lobby.addPlayer(p.getName(), Color.rgb(p.getR(), p.getG(), p.getB()), 2);

                game.setScene(lobby);

            }
            case SRV_DENY_CONN__USERNAME_TAKEN, SRV_DENY_CONN__FULL -> {
                LOG.error("Failed to join: {}", packet.type());
                this.close();
            }
        }
    }

    /**
     * Sends a packet to the connected server
     *
     * @param packet the packet to serialize and send
     */
    public void send(GamePacket packet) {
        if (out != null) {
            try {
                byte[] data = packet.serialize();
                out.writeInt(data.length);
                out.write(data);
                out.flush();
            } catch (IOException e) {
                LOG.error("Failed to send packet", e);
            }
        }
    }

    /**
     * Terminates the active connection with the server
     *
     * @throws IOException if the socket fails to close
     */
    public void close() throws IOException {
        if (socket != null) {
            LOG.info("Closing connection to server");
            socket.close();
        }
    }
}