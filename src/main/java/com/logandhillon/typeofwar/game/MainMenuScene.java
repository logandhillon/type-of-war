package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.MenuController;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.InputBoxEntity;
import com.logandhillon.typeofwar.entity.ui.MenuButton;
import com.logandhillon.typeofwar.entity.ui.ModalEntity;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    /**
     * Creates a new main menu
     *
     * @param mgr the {@link GameSceneManager} responsible for switching active scenes.
     */
    public MainMenuScene(GameSceneManager mgr) {
        float x = 314;
        int y = 205;
        int dy = 48 + 16; // ∆y per button height

        MenuController controller = new MenuController(
                new MenuButton("Host Game", x, y, 256, 48, () -> mgr.setScene(new HostGameScene(mgr))),
                new MenuButton("Join Game", x, y + dy, 256, 48, () -> mgr.setScene(new JoinGameMenu(mgr))),
                new MenuButton("Settings", x, y + 2 * dy, 256, 48, () -> {}),
                new MenuButton("Credits", x, y + 3 * dy, 256, 48, () -> {}),
                new MenuButton("Quit", x, y + 4 * dy, 256, 48, () -> System.exit(0))
        );
        addEntity(controller);

        InputBoxEntity userInput = new InputBoxEntity(16, 47, 316, "YOUR NAME", "YOUR NAME", 20);
        addEntity(new ModalEntity(618, y, 348, 310,
                                  userInput
        ));
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
