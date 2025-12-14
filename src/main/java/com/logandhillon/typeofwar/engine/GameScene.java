package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;
import java.util.List;

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

    private final ArrayList<Entity>   entities = new ArrayList<>();
    private final List<HandlerRef<?>> handlers = new ArrayList<>();

    private AnimationTimer         lifecycle;
    private Stage                  stage;
    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    private record HandlerRef<T extends Event>(EventType<T> type, EventHandler<? super T> handler) {}

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
    protected void onUpdate(float dt) { //FIXME(Logan Dhillon): Please fix this!
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

        // register all event handlers
        for (HandlerRef<?> h: handlers) {
            @SuppressWarnings("unchecked") EventType<Event> t = (EventType<Event>)h.type();
            @SuppressWarnings("unchecked") EventHandler<Event> eh = (EventHandler<Event>)h.handler();

            scene.addEventHandler(t, eh);
        }

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

        // remove all stored event handlers
        for (HandlerRef<?> h: handlers) {
            @SuppressWarnings("unchecked") EventType<Event> t = (EventType<Event>)h.type();
            @SuppressWarnings("unchecked") EventHandler<Event> eh = (EventHandler<Event>)h.handler();

            scene.removeEventHandler(t, eh);
        }
        handlers.clear();
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

    /**
     * Removes an entity from this scene
     *
     * @param e       the entity to remove
     * @param discard if the entity should also be discarded (and trigger {@link Entity#onDestroy()}
     *
     * @throws IllegalArgumentException if the specified entity is not part of this {@link GameScene}
     */
    public void removeEntity(Entity e, boolean discard) {
        if (!entities.contains(e))
            throw new IllegalArgumentException(
                    "Cannot remove entity " + e + " from scene: entity is not attached to this scene.");

        entities.remove(e);
        if (discard) e.onDestroy(); // discard entity if specified
    }

    /**
     * Registers an event handler that will be attached to the scene when it is built.
     *
     * @param type    the type of event to fire on
     * @param handler the event handler itself (the method that will run)
     */
    public <T extends Event> void addHandler(EventType<T> type, EventHandler<? super T> handler) {
        handlers.add(new HandlerRef<>(type, handler));
    }
}
