package com.logandhillon.typeofwar;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.game.MainMenuScene;
import com.logandhillon.typeofwar.game.TypeOfWarScene;
import com.logandhillon.typeofwar.networking.GameClient;
import com.logandhillon.typeofwar.networking.GameServer;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;
import java.net.ConnectException;

/**
 * This is the main entrypoint for Type of War, handling low-level game engine code and GameScene management.
 *
 * @author Logan Dhillon
 * @see TypeOfWarScene
 */
public class TypeOfWar extends Application implements GameSceneManager {
    public static final  String GAME_NAME = "Type of War";
    private static final Logger LOG       = LoggerContext.getContext().getLogger(TypeOfWar.class);

    private Stage     stage;
    private GameScene activeScene;

    private static GameServer             server;
    private static GameClient             client;
    public static  ReadOnlyDoubleProperty WINDOW_WIDTH;
    public static  ReadOnlyDoubleProperty WINDOW_HEIGHT;

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

        setScene(new MainMenuScene(this));
        stage.show();
    }

    /**
     * Handles bootstrap and launching the framework + engine.
     *
     * @param args command-line arguments to the Java program.
     *
     * @see TypeOfWar#start(Stage)
     */
    public static void main(String[] args) throws IOException {
        launch();

        // this runs AFTER the javafx window closes
        LOG.info("Program terminated, exiting cleanly");
        if (server != null) server.stop();
        if (client != null) client.close();
    }

    /**
     * Discards the currently active scene and replaces it with the provided one.
     *
     * @param scene the GameScene to switch
     */
    public void setScene(GameScene scene) {
        LOG.info("Switching scene to {}", scene);
        if (activeScene != null) activeScene.discard(stage.sceneProperty().get());
        stage.setScene(scene.build(stage));
        activeScene = scene;
    }

    @Override
    public void goToMainMenu() {
        this.setScene(new MainMenuScene(this));
    }

    public static void startServer() {
        if (server != null) throw new IllegalStateException("Server already exists, cannot establish connection");

        try {
            server = new GameServer();
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void connectClient(String addr) {
        if (client != null) throw new IllegalStateException("Client already exists, cannot establish connection");

        try {
            client = new GameClient(addr, 20670);
            client.connect();
        } catch (ConnectException e) {
            LOG.warn("Connection failed: {}", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}