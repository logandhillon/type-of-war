package com.logandhillon.typeofwar;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.game.JoinGameMenu;
import com.logandhillon.typeofwar.game.MainMenuScene;
import com.logandhillon.typeofwar.game.TypeOfWarScene;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * This is the main entrypoint for Type of War, handling low-level game engine code and GameScene management.
 *
 * @author Logan Dhillon
 * @see TypeOfWarScene
 */
public class TypeOfWar extends Application implements GameSceneManager {
    public static final  String GAME_NAME = "Type of War";
    private static final Logger LOG       = LoggerContext.getContext().getLogger(TypeOfWar.class);

    private final MainMenuScene mainMenu = new MainMenuScene(this);

    private Stage     stage;
    private GameScene activeScene;

    public static ReadOnlyDoubleProperty WINDOW_WIDTH;
    public static ReadOnlyDoubleProperty WINDOW_HEIGHT;

    /**
     * Handles communication with JavaFX when this program is signalled to start.
     *
     * @param stage the primary stage for this application, provided by the JavaFX framework.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle(GAME_NAME);
        stage.setWidth(1280);
        stage.setHeight(720);

        WINDOW_WIDTH = stage.widthProperty();
        WINDOW_HEIGHT = stage.heightProperty();

//        MainMenuScene menu = new MainMenuScene(this);
        setScene(new JoinGameMenu(this));
        stage.show();
    }

    /**
     * Handles bootstrap and launching the framework + engine.
     *
     * @param args command-line arguments to the Java program.
     *
     * @see TypeOfWar#start(Stage)
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Discards the currently active scene and replaces it with the provided one.
     *
     * @param scene the GameScene to switch
     */
    public void setScene(GameScene scene) {
        LOG.info("Switching scene to {}", scene);
        if (activeScene != null) activeScene.discard();
        stage.setScene(scene.build(stage));
        activeScene = scene;
    }

    @Override
    public void goToMainMenu() {
        this.setScene(mainMenu);
    }
}