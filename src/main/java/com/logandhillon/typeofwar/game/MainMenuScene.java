package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.ButtonBuilder;
import com.logandhillon.typeofwar.entity.ui.ButtonEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    public MainMenuScene() {
        addEntity(new ButtonBuilder()
                          .setLabel("Test button :)")
                          .setColors(Color.BLUE)
                          .setStyle(ButtonEntity.Style.OUTLINE)
                          .at(100, 100)
                          .setAction(() -> System.out.println("Pressed 1"))
                          .setFontSize(24)
                          .build());

        addEntity(new ButtonBuilder()
                          .setLabel("Test button :)")
                          .setColors(Color.BLUE, Color.WHITE)
                          .at(100, 300)
                          .setAction(() -> System.out.println("Pressed 2"))
                          .setFontSize(24)
                          .build());
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
