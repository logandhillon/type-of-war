package com.logandhillon.typeofwar;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.game.TypeOfWarScene;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.stage.Stage;

/**
 * This is the main entrypoint for Type of War, handling low-level game engine code and GameScene management.
 *
 * @author Logan Dhillon
 * @see TypeOfWarScene
 */
public class TypeOfWar extends Application {
    public static final String GAME_NAME = "Type of War";

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

        setScene(new TypeOfWarScene());
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
        if (activeScene != null) activeScene.discard();
        stage.setScene(scene.build(stage));
        activeScene = scene;
    }
}