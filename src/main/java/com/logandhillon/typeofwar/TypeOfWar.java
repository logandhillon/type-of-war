package com.logandhillon.typeofwar;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.game.TypeOfWarScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class TypeOfWar extends Application {
    public static final String GAME_NAME = "Type of War";

    private Stage stage;
    private GameScene activeScene;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle(GAME_NAME);
        setScene(new TypeOfWarScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Discards the currently active scene and replaces it with the provided one.
     * @param scene the GameScene to switch
     */
    public void setScene(GameScene scene) {
        if (activeScene != null) activeScene.discard();
        stage.setScene(scene.build());
        activeScene = scene;
    }
}