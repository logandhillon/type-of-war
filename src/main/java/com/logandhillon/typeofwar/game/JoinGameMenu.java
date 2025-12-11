package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * @author Jack Ross
 */
public class JoinGameMenu extends UIScene {
    private String serverAddress;

    public JoinGameMenu(GameSceneManager mgr) {
        addEntity(new LabeledModalEntity(359, 110, 562, 464, "JOIN A GAME", mgr));
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
}
