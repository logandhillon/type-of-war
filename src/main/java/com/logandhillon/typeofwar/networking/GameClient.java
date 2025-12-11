package com.logandhillon.typeofwar.networking;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Logan Dhillon
 */
public class GameClient {
    private static final Logger LOG = LoggerContext.getContext().getLogger(GameClient.class);

    private final String host;
    private final int    port;

    private Socket         socket;
    private BufferedReader in;
    private PrintWriter    out;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

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

    private void readLoop() {
        try {
            String line;
            // try reading and parsing lines forever, until we get null (disconnected)
            while ((line = in.readLine()) != null) parseResponse(GamePacket.deserialize(line));
        } catch (IOException ignored) {
        }
    }

    private void parseResponse(GamePacket packet) {
        // TODO: temp code to see if it workls :)
        System.out.println("FROM SERVER: " + packet);
    }

    public void send(GamePacket packet) {
        if (out != null) out.println(packet.serialize());
    }

    public void close() throws IOException {
        if (socket != null) {
            LOG.info("Closing connection to server");
            socket.close();
        }
    }
}