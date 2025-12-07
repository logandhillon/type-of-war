package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.Clickable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashMap;

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
    // list of all clickables and if they're currently active or not; where (true = is hovering)
    private static final HashMap<Clickable, Boolean> CLICKABLES = new HashMap<>();

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
        scene.setOnMouseMoved(this::onMouseMoved);
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
        if (e instanceof Clickable) CLICKABLES.put((Clickable)e, false);
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
     * @see Clickable#onClick(MouseEvent)
     */
    protected void onMouseClicked(MouseEvent e) {
        float x = (float)e.getX();
        float y = (float)e.getY();

        for (Clickable c: CLICKABLES.keySet()) {
            // if the mouse is within the hitbox of the clickable, trigger it's onClick event.
            if (x >= c.getX() && x <= c.getX() + c.getWidth() &&
                y >= c.getY() && y <= c.getY() + c.getHeight()) {
                c.onClick(e);
            }
        }
    }

    /**
     * Runs when the mouse is moved within the JavaFX {@link Scene}.
     * <p>
     * This method goes through all attached clickables and, if it is within the clickable's hitbox, runs the
     * {@link Clickable#onMouseEnter(MouseEvent)} or {@link Clickable#onMouseLeave(MouseEvent)} event handler, depending
     * on what just happened relative to said Clickable.
     *
     * @param e details about the mouse click event. this can be used to get the mouse button pressed, x/y position,
     *          etc.
     *
     * @see MouseEvent
     * @see Clickable#onMouseEnter(MouseEvent)
     * @see Clickable#onMouseLeave(MouseEvent)
     */
    protected void onMouseMoved(MouseEvent e) {
        float x = (float)e.getX();
        float y = (float)e.getY();

        for (Clickable c: CLICKABLES.keySet()) {
            // if the mouse is within the hitbox of the clickable
            if (x >= c.getX() && x <= c.getX() + c.getWidth() &&
                y >= c.getY() && y <= c.getY() + c.getHeight() &&
                !CLICKABLES.get(c) /* and clickable is not currently active */) {
                c.onMouseEnter(e);
                CLICKABLES.put(c, true); // mark as active
            }

            // if mouse is outside clickable hitbox
            else if ((x < c.getX() || x > c.getX() + c.getWidth()) &&
                     (y < c.getY() || y > c.getY() + c.getHeight()) &&
                     CLICKABLES.get(c) /* and clickable is currently active */) {
                c.onMouseLeave(e);
                CLICKABLES.put(c, false); // mark as not active
            }
        }
    }
}
