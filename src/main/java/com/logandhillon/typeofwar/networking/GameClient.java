package com.logandhillon.typeofwar.networking;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.game.LobbyGameScene;
import com.logandhillon.typeofwar.game.TypeOfWarScene;
import com.logandhillon.typeofwar.networking.proto.EndGameProto;
import com.logandhillon.typeofwar.networking.proto.GameInitProto;
import com.logandhillon.typeofwar.networking.proto.PlayerProto;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

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
    private final int       team;

    private Socket          socket;
    private DataInputStream in;
    private PacketWriter    out;

    private List<PlayerProto.PlayerData> team1;
    private List<PlayerProto.PlayerData> team2;

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
    public GameClient(String host, int port, TypeOfWar game, int team) {
        this.host = host;
        this.port = port;
        this.game = game;
        this.team = team;

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
        out = new PacketWriter(socket.getOutputStream());

        // ask to connect
        out.send(new GamePacket(
                GamePacket.Type.CLT_REQ_CONN,
                PlayerProto.PlayerData.newBuilder()
                                      .setName(System.getProperty("user.name"))
                                      .setTeam(team)
                                      .setR(255).setG(255).setB(255)
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
                    LOG.warn("Failed to read length byte from server packet");
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

        LOG.debug("Received {} from SERVER", packet.type());

        switch (packet.type()) {
            case SRV_ALLOW_CONN -> {
                isRegistered = true;
                LOG.info("Successfully registered with remote server");

                var data = PlayerProto.Lobby.parseFrom(packet.payload());
                this.team1 = data.getTeam1List();
                this.team2 = data.getTeam2List();

                var lobby = new LobbyGameScene(game, data.getName(), false);
                for (var p: data.getTeam1List())
                    lobby.addPlayer(p.getName(), Color.rgb(p.getR(), p.getG(), p.getB()), 1);
                for (var p: data.getTeam2List())
                    lobby.addPlayer(p.getName(), Color.rgb(p.getR(), p.getG(), p.getB()), 2);

                game.setInMenu(true);

                // run setScene on the FX thread
                Platform.runLater(() -> game.setScene(lobby));
            }
            case SRV_DENY_CONN__USERNAME_TAKEN, SRV_DENY_CONN__FULL -> {
                LOG.error("Failed to join: {}", packet.type());
                Platform.runLater(() -> game.showAlert(
                        "Failed to join server",
                        "Could not " + host + ": " + packet.type().name()));
                this.close();
            }
            case SRV_GAME_STARTING -> {
                GameInitProto.GameData gd = GameInitProto.GameData.parseFrom(packet.payload());
                game.startGame(gd.getSentence(), gd.getMultiplier());
            }
            case SRV_KEY_PRESS -> {
                TypeOfWarScene scene = game.getActiveScene(TypeOfWarScene.class);
                if (scene == null) {
                    LOG.warn("Got a key press signal, but was not in TypeOfWarScene. Ignoring");
                    return;
                }

                // team1 = if the team (that is the first-byte of the payload) is one
                scene.moveRope((packet.payload()[0] & 0xFF) == 1);
            }
            case SRV_REQ_END_GAME_STATS -> {
                TypeOfWarScene scene = game.getActiveScene(TypeOfWarScene.class);
                if (scene == null) {
                    LOG.warn("Was requested end game stats, but was not in TypeOfWarScene. Ignoring");
                    return;
                }
                var stats = scene.getStats();

                // send the stats as a protobuf :)
                sendServer(new GamePacket(
                        GamePacket.Type.CLT_END_GAME_STATS,
                        EndGameProto.PlayerStats.newBuilder()
                                                .setPlayerName(System.getProperty(
                                                        "user.name")) // TODO: populate w/ real values
                                                .setTeam(team) // TODO: populate w/ real values
                                                .setR(255).setG(255).setB(255) // TODO: populate w/ real values
                                                .setWpm(stats.getWpm())
                                                .setAccuracy(stats.getAccuracy())
                                                .setWords(stats.getCorrectWords())
                                                .build()));
            }

            case SRV_END_GAME -> {
                var stats = EndGameProto.AllStats.parseFrom(packet.payload());
                Platform.runLater(() -> game.showEndGameScreen(stats));
            }
        }
    }

    /**
     * Sends a packet to the connected server.
     *
     * @param pkt the packet to send
     *
     * @throws IllegalStateException if the {@link PacketWriter} is null (i.e. client not connected)
     */
    public void sendServer(GamePacket pkt) {
        if (out == null)
            throw new IllegalStateException("Cannot send packets from null PacketWriter; is the client connected?");
        out.send(pkt);
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

    /**
     * Gets the players that the server has told this client about.
     *
     * @param team the team (1 or 2)
     *
     * @return all players on said team
     *
     * @throws IllegalArgumentException if you tried to get an invalid team number
     */
    public List<PlayerProto.PlayerData> getTeam(int team) {
        if (team == 1) return team1;
        if (team == 2) return team2;

        throw new IllegalArgumentException("Can only get team for 1 or 2.");
    }
}