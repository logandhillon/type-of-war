package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.UIScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    public MainMenuScene() {

    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }
}
