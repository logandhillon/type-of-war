package com.logandhillon.typeofwar.networking;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * A packet to send between the server or client that contains game-related networking data.
 *
 * @param type    the {@link Type} of packet to send
 * @param payload the raw content of the packet
 *
 * @author Logan Dhillon
 */
public record GamePacket(Type type, String payload) {
    private static final Logger LOG   = LoggerContext.getContext().getLogger(GamePacket.class);
    private static final char   US    = (char)31; // unit separator byte
    private static final Type[] TYPES = Type.values();

    /**
     * Creates a packet with a null payload.
     *
     * @param type the type of packet to send.
     */
    public GamePacket(Type type) {
        this(type, "");
    }

    /**
     * The type of packet to send. SRV are server packets, CLT are client packets.
     */
    public enum Type {
        // server-side types
        SRV_ALLOW_CONN, SRV_DENY_CONN__FULL, SRV_DENY_CONN__USERNAME_TAKEN, // used for managing connections
        SRV_UNEXPECTED, // generic error for if the server wasn't expecting something (e.g. not ready for a request)

        // client-side types
        CLT_REQ_CONN // used to request registration upon joining a server
    }

    /**
     * Turns this packet into a string that can be sent.
     *
     * @return the serialized string.
     */
    public String serialize() {
        return String.valueOf(type.ordinal()) + US + payload;
    }

    /**
     * Deserializes a raw string to a {@link GamePacket}.
     *
     * @param packet the raw packet content as a string
     *
     * @return the deserialized game packet
     */
    public static GamePacket deserialize(String packet) {
        String[] parts = packet.split(String.valueOf(US), 2);
        int type = Integer.parseInt(parts[0]);
        if (type >= TYPES.length) {
            LOG.warn("Failed to deserialize packet, TYPE {} does not exist", type);
            return null;
        }
        return new GamePacket(TYPES[type], parts[1]);
    }
}
