package com.logandhillon.typeofwar.networking;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.game.JoinGameScene;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Logan Dhillon
 */
public class ServerDiscoverer {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ServerDiscoverer.class);

    private final List<String> discoveredServers = new ArrayList<>();
    private final TypeOfWar    game;

    private boolean listening = false;

    public ServerDiscoverer(TypeOfWar game) {
        this.game = game;
    }

    /**
     * Starts the server discoverer
     */
    public void start() {
        LOG.info("Starting UDP server discovery thread");
        listening = true;

        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(GameServer.ADVERTISE_PORT)) {
                byte[] buffer = new byte[256];

                while (listening) {
                    if (game.isInGame()) continue; // only listen in lobby

                    // dump all discovered servers— if they don't advertise again, they are probably gone
                    discoveredServers.clear();

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String pkt = new String(packet.getData(), 0, packet.getLength());
                    if (!pkt.startsWith("TypeOfWarServer")) return; // ignore other services

                    String ip = packet.getAddress().getHostAddress();

                    if (!discoveredServers.contains(ip)) {
                        discoveredServers.add(ip);
                        LOG.info("Discovered server at {}:{}", ip, pkt);
                    }

                    updateJoinGameScene();
                }
            } catch (SocketException e) {
                LOG.info("UDP listener socket closed");
            } catch (IOException e) {
                LOG.error("Exception while receiving server discovery packet", e);
            }
        }, "UDP-ServerDiscovery").start();
    }

    /**
     * Stops the server discoverer
     */
    public void stop() {
        LOG.info("Stopping UDP server discovery thread");
        listening = false;
    }

    private void updateJoinGameScene() {
        var scene = game.getActiveScene(JoinGameScene.class);
        if (scene == null) return;
        LOG.debug("Updating join game scene");
        scene.setDiscoveredServers(discoveredServers.stream()
                                                    .map(addr -> new JoinGameScene.ServerEntry("TODO", addr, 0))
                                                    .toList());
    }
}
