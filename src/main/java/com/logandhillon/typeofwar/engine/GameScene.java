package com.logandhillon.typeofwar.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public abstract class GameScene {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    private AnimationTimer lifecycle;

    /**
     * Do not instantiate this class.
     * @see GameScene#build()
     */
    protected GameScene(){}

    /**
     * Called every tick for non-graphics-related updates (Entity lifecycle, etc.)
     */
    protected abstract void onUpdate();

    /**
     * Called every tick to render the scene.
     * @param g the graphical context to render to.
     */
    protected abstract void render(GraphicsContext g);

    /**
     * Creates a new JavaFX Scene for this GameScene.
     * This should only be called once, as this method creates a new Scene every time.
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

        return new Scene(new StackPane(canvas), WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /**
     * Called to discard this scene (i.e., stop its lifecycle, etc.)
     */
    public void discard() {
        lifecycle.stop();
    }
}
