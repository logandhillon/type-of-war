package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * A GameScene is the lowest-level of the engine; controlling the game's lifecycle, rendering, and creating a game loop.
 * It represents a "scene" of the game, that being a section of the game that is related (e.g. a game level, the main
 * menu, etc.) GameScenes are rendered with {@link GameScene#build(Stage)}, which prepares the engine code for JavaFX,
 * allowing it to be executed and ran.
 *
 * @author Logan Dhillon
 */
public abstract class GameScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(GameScene.class);

    private final ArrayList<Entity> entities = new ArrayList<>();

    private AnimationTimer         lifecycle;
    private Stage                  stage;
    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    /**
     * Do not instantiate this class.
     *
     * @see GameScene#build(Stage)
     */
    protected GameScene() {}

    /**
     * Called every tick for non-graphics-related updates (Entity lifecycle, etc.) This implementation updates all
     * entities.
     */
    protected void onUpdate(float dt) {
        for (Entity e: entities)
            e.onUpdate(dt);
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
     * @param scene the JavaFX scene (NOT GameScene!) from {@link GameScene#build(Stage)}
     */
    protected void onBuild(Scene scene) {}

    /**
     * Creates a new JavaFX Scene for this GameScene. This should only be called once, as this method creates a new
     * Scene every time.
     *
     * @return Scene containing the GameScene's GUI elements
     */
    public Scene build(Stage stage) {
        LOG.debug("Building game scene {} to stage", this);
        this.stage = stage;

        Canvas canvas = new Canvas(WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());
        GraphicsContext g = canvas.getGraphicsContext2D();

        // use one-element array as address cannot change once anonymously passed to lifecycle
        final long[] lastTime = { 0 };

        lifecycle = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime[0] == 0) lastTime[0] = now; // set initial value to now
                float dt = (now - lastTime[0]) / 1_000_000_000f; // nanoseconds to seconds
                lastTime[0] = now;

                onUpdate(dt);
                render(g);
            }
        };
        lifecycle.start();

        Scene scene = new Scene(new StackPane(canvas), WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // resize canvas when window changes
        widthListener = (obs, oldV, newV) -> canvas.setWidth(newV.doubleValue());
        heightListener = (obs, oldV, newV) -> canvas.setHeight(newV.doubleValue());
        WINDOW_WIDTH.addListener(widthListener);
        WINDOW_HEIGHT.addListener(heightListener);

        for (Entity e: entities) e.onBuild(scene);
        onBuild(scene);
        return scene;
    }

    /**
     * Called to discard this scene (i.e., stop its lifecycle, etc.)
     *
     * @param scene the JavaFX scene that is this scene is being removed from. use this for detaching events, etc.
     */
    public void discard(Scene scene) {
        LOG.debug("Discarding scene {}", this);

        // schedule all entities for destruction
        for (Entity e: entities) e.onDestroy();
        entities.clear();

        lifecycle.stop();

        // remove window resize listeners from stage
        stage.widthProperty().removeListener(widthListener);
        stage.heightProperty().removeListener(heightListener);
    }

    /**
     * Adds a new entity to the scene, that will be rendered/updated every tick.
     *
     * @param e the entity to append.
     */
    public void addEntity(Entity e) {
        entities.add(e);
        e.onAttach(this);
    }
}
