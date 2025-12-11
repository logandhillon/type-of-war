package com.logandhillon.typeofwar.networking;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A packet writer internally uses a {@link PrintWriter}, but instead of using Strings to communicate, it uses a
 * {@link GamePacket}.
 * <p>
 * Such packet is then serialized and written to the output stream {using {@link PacketWriter#send(GamePacket)}}
 * <p>
 * This class it {@link AutoCloseable}, and should be used with a try-with-resources statement.
 *
 * @author Logan Dhillon
 */
public class PacketWriter implements AutoCloseable {
    private final PrintWriter writer;

    /**
     * Creates the internal print writer from an output stream
     *
     * @param out the output stream to write to
     */
    public PacketWriter(OutputStream out) {
        this.writer = new PrintWriter(new OutputStreamWriter(out));
    }

    /**
     * Serializes and writes a packet to the output stream, effectively "sending" it.
     *
     * @param packet the {@link GamePacket} to send.
     */
    public void send(GamePacket packet) {
        writer.println(packet.serialize());
        writer.flush();
    }

    /**
     * Builds a new NULL packet using the specified type and sends it.
     *
     * @param packetType the packet type to send
     */
    public void send(GamePacket.Type packetType) {
        send(new GamePacket(packetType));
    }

    /**
     * Closes the internal {@link PrintWriter}.
     */
    @Override
    public void close() {
        writer.close();
    }
}
