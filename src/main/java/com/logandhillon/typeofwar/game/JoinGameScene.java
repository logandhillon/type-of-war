package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.DarkMenuButton;
import com.logandhillon.typeofwar.entity.ui.InputBoxEntity;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.entity.ui.ServerEntryEntity;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The join game menu allows users to join existing servers through manual IP Address searching or local server
 * discovery. When the user has joined a game, they will be transported to the {@link LobbyGameScene}
 *
 * @author Jack Ross
 */
public class JoinGameScene extends UIScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(JoinGameScene.class);

    private static final Font LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 18);
    private static final int  ENTITY_GAP = 53;

    private final ServerEntryEntity[] serverButtons = new ServerEntryEntity[4];
    private final LabeledModalEntity  joinModal;

    private int scrollServerIndex;
    private int currentServerIndex;
    private int rawCurrentServerIndex;
    private String selectedServerAddr; // the addr of the selected server in Discovery

    // TODO: temp code, this will be filled in with the actual server list later.
    private final ArrayList<ServerEntry> serverList = new ArrayList<>();

    /**
     * @param mgr the {@link GameSceneManager} responsible for switching active scenes.
     */
    public JoinGameScene(GameSceneManager mgr, JoinGameHandler onJoin) {
        // rect in background for server list
        Entity serverListRect = new Entity(16, 152) {
            @Override
            protected void onRender(GraphicsContext g, float x, float y) {
                g.setFill(Colors.DEFAULT_DARKER);
                g.fillRect(x, y, 530, 228);
            }

            @Override
            public void onUpdate(float dt) {}

            @Override
            public void onDestroy() {}
        };

        // label for server list
        Entity serverListLabel = new Entity(16, 121) {
            @Override
            protected void onRender(GraphicsContext g, float x, float y) {
                g.setTextAlign(TextAlignment.LEFT);
                g.setTextBaseline(VPos.TOP);
                g.setFont(LABEL_FONT);
                g.setFill(Color.WHITE);

                // render label
                g.fillText("FIND A LOCAL SERVER", x, y);
            }

            @Override
            public void onUpdate(float dt) {

            }

            @Override
            public void onDestroy() {

            }
        };

        // join server input field
        InputBoxEntity joinServer = new InputBoxEntity(16, 47, 379, "ex. 192.168.0.1", "JOIN A SERVER DIRECTLY", 39);

        // join button (direct)
        DarkMenuButton joinDirectButton = new DarkMenuButton("JOIN", 407, 47, 139, 50, () -> {
            LOG.info("Attempting to join {} via manual input", joinServer.getInput());
            onJoin.handleJoin(joinServer.getInput());
        });

        // join button (discovery)
        DarkMenuButton joinDiscoverButton = new DarkMenuButton("JOIN", 16, 396, 530, 48, () -> {
            if (selectedServerAddr == null) {
                LOG.warn("Tried to join discovered server, but no server was selected. Ignoring");
                return;
            }
            LOG.info("Attempting to join {} via discovery", joinServer.getInput());
            onJoin.handleJoin(selectedServerAddr);
        });

        joinModal = new LabeledModalEntity(
                359, 99, 562, 523, "JOIN A GAME", mgr, serverListRect, serverListLabel, joinServer, joinDirectButton,
                joinDiscoverButton);
        addEntity(joinModal);

        // create event handler that uses the event and the array of buttons
        this.addHandler(KeyEvent.KEY_PRESSED, e -> onKeyPressed(e, serverButtons));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }

    /**
     * Clears the UI discovered server list and repopulates it with the values of {@link JoinGameScene#serverList}
     */
    private void updateServerList() {
        AtomicInteger currentServer = new AtomicInteger();

        // clear the list
        for (ServerEntryEntity listItem: serverButtons) joinModal.removeEntity(listItem, true);

        // repopulate items and add to list
        for (int i = 0; i < Math.min(serverButtons.length, serverList.size()); i++) {
            int finalI = i;
            // set new server button with available information
            serverButtons[i] = new ServerEntryEntity(
                    32, 231 + (ENTITY_GAP * i), 498, 37,
                    serverList.get(i).name, serverList.get(i).address, serverList.get(i).ping,
                    () -> {
                        // runnable (runs on click)

                        // highlight button
                        serverButtons[finalI].setActive(true, true);
                        currentServer.set(finalI);

                        currentServerIndex = finalI;
                        rawCurrentServerIndex = finalI;
                        selectedServerAddr = serverList.get(finalI).address;

                        // reset button highlight for non-clicked buttons
                        for (int j = 0; j < serverButtons.length; j++) {
                            if (currentServer.get() != j) {
                                serverButtons[j].setActive(false, false);
                            }
                        }
                    });
            joinModal.addEntity(serverButtons[i]);
        }
    }

    /**
     * Adds a server to the server list, then updates the list.
     *
     * @param server the server to add to the list
     */
    public void addDiscoveredServer(ServerEntry server) {
        serverList.add(server);
        updateServerList();
    }

    /**
     * Removes all servers from the server list, then updates the list.
     */
    public void clearDiscoveredServers() {
        serverList.clear();
        updateServerList();
    }

    /**
     * An entry in the server list of the join game screen.
     *
     * @param name    name of the server/room
     * @param address FQDN or IP address of server
     * @param ping    how many milliseconds of latency to the server
     */
    public record ServerEntry(String name, String address, int ping) {}

    /**
     * @param e     any key event registered by javafx
     * @param array list of buttons on screen
     */
    private void onKeyPressed(KeyEvent e, ServerEntryEntity[] array) {

        if (e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN) return;

        // increment/decrement the 4 shown servers

        if (e.getCode() == KeyCode.UP) {
            if (scrollServerIndex > 0) {
                rawCurrentServerIndex++;
                // un-highlight all buttons
                for (int i = 0; i < array.length; i++) {
                    array[i].setActive(false, false);
                }
                if (currentServerIndex < array.length - 1 && rawCurrentServerIndex > 0) {
                    currentServerIndex++;
                    // re-highlight button if it isn't still off-screen
                    array[currentServerIndex].setActive(true, true);
                }

                if (currentServerIndex == 0) {
                    if (rawCurrentServerIndex < -1) {
                        // un-highlight all buttons if the selected button is not in the array
                        for (int i = 0; i < array.length; i++) {
                            array[i].setActive(false, false);
                        }
                    }
                    // if the button was put back in the array by moving up, put it at the start
                    if (rawCurrentServerIndex > -1) {
                        currentServerIndex = 0;
                        array[0].setActive(true, true);
                    }
                }
                // increments entire list of shown servers
                scrollServerIndex--;
            }
        }
        if (e.getCode() == KeyCode.DOWN) {
            if (scrollServerIndex < serverList.toArray().length - array.length) {
                // opposite to KeyCode.UP, the index of the current button must decrease when down arrow is pressed
                rawCurrentServerIndex--;
                for (int i = 0; i < array.length; i++) {
                    array[i].setActive(false, false);
                }

                if (currentServerIndex > 0 && rawCurrentServerIndex < array.length - 1) {
                    currentServerIndex--;

                    array[currentServerIndex].setActive(true, true);
                }
                if (currentServerIndex == array.length - 1) {
                    if (rawCurrentServerIndex > array.length) {
                        for (int i = 0; i < array.length; i++) {
                            array[i].setActive(false, false);
                        }
                    }
                    if (rawCurrentServerIndex < array.length) {
                        currentServerIndex = array.length - 1;
                        array[array.length - 1].setActive(true, true);
                    }
                }
                // decrements entire list of shown servers
                scrollServerIndex++;
            }
        }

        // now that index has changed, re-populate the server list
        for (int i = 0; i < array.length; i++) {
            array[i].setData(serverList.get(i + scrollServerIndex));
        }
    }

    public interface JoinGameHandler {
        void handleJoin(String serverAddress);
    }
}
