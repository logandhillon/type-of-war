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

    private final List<JoinGameScene.ServerEntry> discoveredServers = new ArrayList<>();
    private final TypeOfWar                       game;

    private volatile boolean listening        = false;
    private volatile long    lastUpdateMillis = System.currentTimeMillis();

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
                    if (!pkt.startsWith("TypeOfWarServer")) {
                        return;
                    } // ignore other services
                    String[] parts = pkt.split(":", 3); // {service name:lobby name:port}
                    if (parts.length < 3) {
                        LOG.warn(
                                "Received malformed server advertisement, there is likely a bad server on the network");
                        return;
                    }

                    String ip = packet.getAddress().getHostAddress();
                    discoveredServers.add(new JoinGameScene.ServerEntry(parts[1], ip, 0));
                    LOG.info("Discovered server at {}:{}", ip, pkt);

                    updateJoinGameScene();
                    lastUpdateMillis = System.currentTimeMillis();
                }
            } catch (SocketException e) {
                LOG.info("UDP listener socket closed");
            } catch (IOException e) {
                LOG.error("Exception while receiving server discovery packet", e);
            }
        }, "UDP-ServerDiscovery").start();

        // updates the server list if it becomes stale
        new Thread(() -> {
            while (listening) {
                // only update 4s after the last update
                if (lastUpdateMillis + 4000 > System.currentTimeMillis()) continue;

                updateJoinGameScene();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOG.warn("Thread interrupted, this is normal");
                }
            }
        }, "Discoverer/ServerListUpdater").start();
    }

    /**
     * Stops the server discoverer
     */
    public void stop() {
        LOG.info("Asking discovery threads to stop");
        listening = false;
    }

    private void updateJoinGameScene() {
        var scene = game.getActiveScene(JoinGameScene.class);
        if (scene == null) return;
        LOG.debug("Updating join game scene with {} servers", discoveredServers.size());
        scene.setDiscoveredServers(discoveredServers);
    }
}
