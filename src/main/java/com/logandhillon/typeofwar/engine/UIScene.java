package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.Clickable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * A UI scene is a type of {@link GameScene} that listens to the cursor and registers events handlers for
 * {@link UIScene#onMouseClicked(MouseEvent)}.
 * <p>
 * UI scenes are used with {@link Clickable} entities. Clickables can only be used in UI scenes.
 *
 * @author Logan Dhillon
 * @see Clickable
 */
public abstract class UIScene extends GameScene {
    private static final ArrayList<Clickable> CLICKABLES = new ArrayList<>();

    /**
     * Binds the {@link Scene} mouse click handler to the game scene.
     *
     * @param scene the JavaFX scene (NOT GameScene!) from {@link GameScene#build(Stage)}
     *
     * @see UIScene#onUpdate(float)
     */
    @Override
    protected void onBuild(Scene scene) {
        super.onBuild(scene);
        scene.setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * Works the same as the regular {@link GameScene}, but also appends {@link Clickable} entities to a separate list,
     * that will get special treatment in the scene lifecycle to trigger mouse events.
     *
     * @param e the entity or clickable to append.
     */
    @Override
    protected void addEntity(Entity e) {
        super.addEntity(e);
        if (e instanceof Clickable) CLICKABLES.add((Clickable)e);
    }

    /**
     * Runs when the mouse is clicked on the JavaFX {@link Scene}.
     * <p>
     * This method goes through all attached clickables and, if it is within the clickable's hitbox, runs the
     * {@link Clickable#onClick(MouseEvent)} event handler.
     *
     * @param e details about the mouse click event. this can be used to get the mouse button pressed, x/y position,
     *          etc.
     *
     * @see MouseEvent
     */
    protected void onMouseClicked(MouseEvent e) {
        float x = (float)e.getX();
        float y = (float)e.getY();

        for (Clickable c: CLICKABLES) {
            // if the mouse is within the hitbox of the clickable, trigger it's onClick event.
            if (x >= c.getX() && x <= c.getX() + c.getWidth() &&
                y >= c.getY() && y <= c.getY() + c.getHeight()) {
                c.onClick(e);
            }
        }
    }
}
