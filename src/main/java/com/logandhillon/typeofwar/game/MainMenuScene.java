package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.MenuButton;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    public MainMenuScene(Runnable onPlay) {
        int offsetY = 205;
        int gapY = 48 + 16;

        addEntity(new MenuButton(
                "Host Game", (WINDOW_HEIGHT.floatValue() + 256) / 2, offsetY, 256, 48, e -> onPlay.run()));

        addEntity(new MenuButton(
                "Join Game", (WINDOW_HEIGHT.floatValue() + 256) / 2, offsetY + gapY, 256, 48, e -> onPlay.run()));

        addEntity(new MenuButton(
                "Settings", (WINDOW_HEIGHT.floatValue() + 256) / 2, offsetY + 2 * gapY, 256, 48, this::doNothing));

        addEntity(new MenuButton(
                "Credits", (WINDOW_HEIGHT.floatValue() + 256) / 2, offsetY + 3 * gapY, 256, 48, this::doNothing));

        addEntity(new MenuButton(
                "Quit", (WINDOW_HEIGHT.floatValue() + 256) / 2, offsetY + 4 * gapY, 256, 48, e -> System.exit(0)));
    }

    private void doNothing(MouseEvent e) {
        // temp code :)
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
