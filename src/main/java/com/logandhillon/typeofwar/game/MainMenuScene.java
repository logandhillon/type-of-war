package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.MenuController;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.MenuButton;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    private final MenuController controller;

    public MainMenuScene(Runnable onPlay) {
        float x = (WINDOW_HEIGHT.floatValue() + 256) / 2;
        int y = 205;
        int dy = 48 + 16; // ∆y per button height

        controller = new MenuController(
                new MenuButton("Host Game", x, y, 256, 48, onPlay),
                new MenuButton("Join Game", x, y + dy, 256, 48, onPlay),
                new MenuButton("Settings", x, y + 2 * dy, 256, 48, this::doNothing),
                new MenuButton("Credits", x, y + 3 * dy, 256, 48, this::doNothing),
                new MenuButton("Quit", x, y + 4 * dy, 256, 48, () -> System.exit(0))
        );

        for (var e: controller.getButtons()) addEntity(e);
    }

    private void doNothing() {
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

    @Override
    protected void onBuild(Scene scene) {
        super.onBuild(scene);
        scene.setOnKeyPressed(controller::onKeyPressed);
    }
}
