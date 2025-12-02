package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * A GameScene is the lowest-level of the engine; controlling the game's lifecycle, rendering, and creating a game loop.
 * It represents a "scene" of the game, that being a section of the game that is related (e.g. a game level, the main
 * menu, etc.) GameScenes are rendered with {@link GameScene#build()}, which prepares the engine code for JavaFX,
 * allowing it to be executed and ran.
 *
 * @author Logan Dhillon
 */
public abstract class GameScene {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    private final ArrayList<Entity> entities = new ArrayList<>();

    private AnimationTimer lifecycle;

    /**
     * Do not instantiate this class.
     *
     * @see GameScene#build()
     */
    protected GameScene() {}

    /**
     * Called every tick for non-graphics-related updates (Entity lifecycle, etc.) This implementation updates all
     * entities.
     */
    protected void onUpdate() {
        for (Entity e: entities)
            e.onUpdate();
    }

    /**
     * Called every tick to render the scene. This implementation renders all entities.
     *
     * @param g the graphical context to render to.
     */
    protected void render(GraphicsContext g) {
        for (Entity e: entities)
            e.render(g);
    }

    /**
     * Called when this GameScene is built to a scene with a lifecycle. Should be used to attach events to the scene.
     *
     * @param scene the JavaFX scene (NOT GameScene!) from {@link GameScene#build()}
     */
    protected void onBuild(Scene scene) {}

    /**
     * Creates a new JavaFX Scene for this GameScene. This should only be called once, as this method creates a new
     * Scene every time.
     *
     * @return Scene containing the GameScene's GUI elements
     */
    public Scene build() {
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext g = canvas.getGraphicsContext2D();

        lifecycle = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
                render(g);
            }
        };
        lifecycle.start();

        Scene scene = new Scene(new StackPane(canvas), WINDOW_WIDTH, WINDOW_HEIGHT);
        onBuild(scene);
        return scene;
    }

    /**
     * Called to discard this scene (i.e., stop its lifecycle, etc.)
     */
    public void discard() {
        entities.clear();
        lifecycle.stop();
    }

    /**
     * Adds a new entity to the scene, that will be rendered/updated every tick.
     *
     * @param e the entity to append.
     */
    protected void addEntity(Entity e) {
        entities.add(e);
    }
}
