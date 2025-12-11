package com.logandhillon.typeofwar.networking;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    private final String host;
    private final int    port;

    private Socket         socket;
    private BufferedReader in;
    private PrintWriter    out;

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
    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;

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
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // ask to connect
        send(new GamePacket(GamePacket.Type.CLT_REQ_CONN, System.getProperty("user.name")));

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
            String line;
            // try reading and parsing lines forever, until we get null (disconnected)
            while ((line = in.readLine()) != null) parseResponse(GamePacket.deserialize(line));
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

        switch (packet.type()) {
            case SRV_ALLOW_CONN -> {
                isRegistered = true;
                LOG.info("Successfully registered with remote server");
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
        if (out != null) out.println(packet.serialize());
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