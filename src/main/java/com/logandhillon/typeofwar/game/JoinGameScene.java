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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The join game menu allows users to join existing servers through manual IP Address searching or local server
 * discovery.
 * When the user has joined a game, they will be transported to the {@link LobbyGameScene}
 *
 * @author Jack Ross
 */
public class JoinGameScene extends UIScene {
    private static final Font LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 18);
    private String serverAddress;
    private static final int ENTITY_GAP = 53;
    private ServerEntryEntity[] serverButtons = new ServerEntryEntity[4];

    // TODO: temp code, this will be filled in with the actual server list later.
    private final ArrayList<ServerEntry> serverList = new ArrayList<>();

    /**
     *
     * @param mgr the {@link GameSceneManager} responsible for switching active scenes.
     */
    public JoinGameScene(GameSceneManager mgr) {
        // TODO: temp code, this just adds in placeholder servers to render.
        //       this will be filled in by the networking engine LATER.
        serverList.add(new ServerEntry("Room 100", "127.0.0.1", 0));
        serverList.add(new ServerEntry("Room 101", "127.0.0.1", 0));
        serverList.add(new ServerEntry("Room 102", "127.0.0.1", 0));
        serverList.add(new ServerEntry("Room 103", "127.0.0.1", 0));
        serverList.add(new ServerEntry("Room 104", "127.0.0.1", 0));
        serverList.add(new ServerEntry("Room 105", "127.0.0.1", 0));


        // rect in background for server list
        Entity serverListRect = new Entity(16, 152){
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

        // join button
        DarkMenuButton joinDirectButton = new DarkMenuButton("JOIN",407, 47,139, 50, ()->{
            //TODO #6: Make this join server
        });

        // join
        DarkMenuButton joinDiscoverButton = new DarkMenuButton("JOIN", 16, 396, 530, 48, ()-> {
           // TODO #6: Make this join server
        });

        LabeledModalEntity joinModal = new LabeledModalEntity(359, 99, 562, 523, "JOIN A GAME", mgr, serverListRect, serverListLabel, joinServer, joinDirectButton, joinDiscoverButton);
        addEntity(joinModal);

        AtomicInteger currentServer = new AtomicInteger();

        for(int i = 0; i < serverButtons.length; i++) {
            int finalI = i;
            serverButtons[i] = new ServerEntryEntity(32, 231 + (ENTITY_GAP * i), 498, 37, serverList.get(i).name, serverList.get(i).address,serverList.get(i).ping, ()-> {
                serverButtons[finalI].setActive(true, true);
                currentServer.set(finalI);
                for (int j = 0; j < serverButtons.length; j++) {
                    if (currentServer.get() != j) {
                        serverButtons[j].setActive(false, false);
                    }
                }
            });
            joinModal.addEntity(serverButtons[i]);
        }
        this.addHandler(KeyEvent.KEY_PRESSED, e->onKeyPressed(e, serverButtons, serverList));
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
     * Gets the server address in the "direct connection" input box as a string
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * An entry in the server list of the join game screen.
     *
     * @param name name of the server/room
     * @param address FQDN or IP address of server
     * @param ping how many milliseconds of latency to the server
     */
    public record ServerEntry(String name, String address, int ping) {}

    private void onKeyPressed(KeyEvent e, ServerEntryEntity[] array, ArrayList<ServerEntry> list){
        if(e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN) return;

        if(e.getCode() == KeyCode.UP) {
            for(int i = 0; i < array.length - 1; i++){ //TODO #32: Make the up/down arrows work
                array[i + 1] = array[i];
            }
        }

        if(e.getCode() == KeyCode.DOWN){
            for(int i = array.length - 1; i > 0; i--){
                array[i] = array[i - 1];
            }
        }
    }
}
