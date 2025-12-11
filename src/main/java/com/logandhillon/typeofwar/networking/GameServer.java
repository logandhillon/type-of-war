package com.logandhillon.typeofwar.networking;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

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
    private static final Logger LOG  = LoggerContext.getContext().getLogger(GameServer.class);
    private static final int    PORT = 20670;

    private volatile boolean running;

    private ServerSocket socket;

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
            try (client) {
                var in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) parseRequest(client, GamePacket.deserialize(line));
            } catch (IOException e) {
                LOG.error(e);
            }
        }, "ClientHandler-" + client.getInetAddress()).start();
    }

    /**
     * Parses a request from a client
     *
     * @param client the client socket that this packet is from
     * @param packet the packet itself, from the client
     */
    private void parseRequest(Socket client, GamePacket packet) {
        System.out.println("FROM CLIENT: " + packet);
    }
}