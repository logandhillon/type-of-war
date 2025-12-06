package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.ButtonEntity;
import com.logandhillon.typeofwar.entity.ui.ButtonStyle;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    private static final ButtonStyle BUTTON_STYLE = new ButtonStyle(ButtonEntity.Variant.ROUNDED_SOLID)
            .setColors(Color.DARKSLATEBLUE, Color.WHITE)
            .setFont(Font.font(Fonts.DM_MONO_MEDIUM, 24))
            .withSize(256, 64);

    public MainMenuScene() {
        addEntity(BUTTON_STYLE.build("Host Game", (WINDOW_HEIGHT.floatValue() + 256) / 2, 300, this::play));
        addEntity(BUTTON_STYLE.build("Join Game", (WINDOW_HEIGHT.floatValue() + 256) / 2, 300, this::play));
        addEntity(BUTTON_STYLE.build("Settings", (WINDOW_HEIGHT.floatValue() + 256) / 2, 400, this::play));
        addEntity(BUTTON_STYLE.build("Quit", (WINDOW_HEIGHT.floatValue() + 256) / 2, 500, this::play));
    }

    private void play(MouseEvent e) {

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
