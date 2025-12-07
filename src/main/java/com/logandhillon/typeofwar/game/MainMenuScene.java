package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.ButtonEntity;
import com.logandhillon.typeofwar.entity.ui.DynamicButtonEntity;
import com.logandhillon.typeofwar.resource.Colors;
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
    private static final ButtonEntity.Style BUTTON_STYLE_DEFAULT = new ButtonEntity.Style(
            Color.WHITE, Colors.DEFAULT, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.DM_MONO_MEDIUM, 20));
    private static final ButtonEntity.Style BUTTON_STYLE_ACTIVE  = new ButtonEntity.Style(
            Color.WHITE, Colors.PRIMARY, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.DM_MONO_MEDIUM, 21));

    public MainMenuScene() {
        addEntity(new DynamicButtonEntity(
                "Host Game", (WINDOW_HEIGHT.floatValue() + 256) / 2, 300, 256, 48,
                this::play, BUTTON_STYLE_DEFAULT, BUTTON_STYLE_ACTIVE));

        addEntity(new DynamicButtonEntity(
                "Join Game", (WINDOW_HEIGHT.floatValue() + 256) / 2, 350, 256, 48,
                this::play, BUTTON_STYLE_DEFAULT, BUTTON_STYLE_ACTIVE));

        addEntity(new DynamicButtonEntity(
                "Settings", (WINDOW_HEIGHT.floatValue() + 256) / 2, 400, 256, 48,
                this::play, BUTTON_STYLE_DEFAULT, BUTTON_STYLE_ACTIVE));

        addEntity(new DynamicButtonEntity(
                "Quit", (WINDOW_HEIGHT.floatValue() + 256) / 2, 450, 256, 48,
                this::play, BUTTON_STYLE_DEFAULT, BUTTON_STYLE_ACTIVE));
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
