package com.logandhillon.typeofwar.networking;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Logan Dhillon
 */
public class GameServer implements Runnable {
    private static final Logger LOG  = LoggerContext.getContext().getLogger(GameServer.class);
    private static final int    PORT = 20670;

    private volatile boolean      running;
    private          ServerSocket socket;

    public GameServer() {
    }

    public void start() throws IOException {
        LOG.info("Starting server on port {}...", PORT);
        socket = new ServerSocket(PORT);
        running = true;
        new Thread(this, "ServerAcceptor").start();
    }

    public void stop() throws IOException {
        running = false;
        socket.close();
    }

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

    private void handleClient(Socket client) {
        new Thread(() -> {
            LOG.info("Incoming client connection at {}", client.getInetAddress().getHostAddress());
            try (client) {
                var in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) parseRequest(GamePacket.deserialize(line));
            } catch (IOException e) {
                LOG.error(e);
            }
        }, "ClientHandler-" + client.getInetAddress()).start();
    }

    private void parseRequest(GamePacket packet) {
        System.out.println("FROM CLIENT: " + packet);
    }
}