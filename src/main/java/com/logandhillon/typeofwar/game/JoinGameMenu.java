package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.InputBoxEntity;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * @author Logan Dhillon
 */
public class JoinGameMenu extends UIScene {
    public JoinGameMenu(GameSceneManager mgr) {
        int dy = 48 + 16; // ∆y per button height

        InputBoxEntity userInput = new InputBoxEntity(16, 47, 316, "YOUR NAME", "YOUR NAME", 20);
        addEntity(new LabeledModalEntity(359, 110, 562, 464, "JOIN A GAME", mgr, userInput));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }
}
