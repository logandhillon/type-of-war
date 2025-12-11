package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.Clickable;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

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
    private static final Logger LOG = LoggerContext.getContext().getLogger(UIScene.class);

    private final HashMap<Clickable, ClickableFlags> clickables       = new HashMap<>();
    private       Clickable[]                        cachedClickables = new Clickable[0];
    private       EventHandler<MouseEvent>           mouseClickedHandler;
    private       EventHandler<MouseEvent>           mouseMovedHandler;

    private static final class ClickableFlags {
        private boolean isHovering = false;
        private boolean isActive   = false;
    }

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

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseClickedHandler = this::onMouseClicked));
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (mouseMovedHandler = this::onMouseMoved));
    }

    @Override
    public void discard(Scene scene) {
        super.discard(scene);

        // clear event handlers
        if (mouseClickedHandler != null) {
            scene.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
            mouseClickedHandler = null;
        }

        if (mouseMovedHandler != null) {
            scene.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
            mouseMovedHandler = null;
        }

        // clear stored clickables to avoid memory leaks / stale state
        for (Clickable c: clickables.keySet()) c.onDestroy();
        clickables.clear();
        cachedClickables = new Clickable[0];
    }

    /**
     * Works the same as the regular {@link GameScene}, but also appends {@link Clickable} entities to a separate list,
     * that will get special treatment in the scene lifecycle to trigger mouse events.
     *
     * @param e the entity or clickable to append.
     */
    @Override
    public void addEntity(Entity e) {
        super.addEntity(e);
        if (e instanceof Clickable) clickables.put((Clickable)e, new ClickableFlags());
        if (e instanceof Clickable) {
            cachedClickables = clickables.keySet().toArray(new Clickable[0]);
        }
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

        LOG.debug("{}: Handling mouse click event at ({}, {})", this.getClass().getSimpleName(), x, y);

        for (Clickable c: cachedClickables) {
            ClickableFlags flags = clickables.get(c);
            // if the mouse is within the hitbox of the clickable, trigger it's onClick event.
            if (x >= c.getX() && x <= c.getX() + c.getWidth() &&
                y >= c.getY() && y <= c.getY() + c.getHeight()) {
                LOG.debug("Click event sent to {} (Clickable)", c.toString());
                c.onClick(e);
                flags.isActive = true;
            }
            // if outside the hitbox and the clickable is "active"
            else if (flags.isActive) {
                c.onBlur(e);
                flags.isActive = false;
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

        for (Clickable c: cachedClickables) {
            ClickableFlags flags = clickables.get(c);

            // if the mouse is within the hitbox of the clickable
            if (x >= c.getX() && x <= c.getX() + c.getWidth() &&
                y >= c.getY() && y <= c.getY() + c.getHeight() &&
                !flags.isHovering /* and clickable is not currently active */) {
                c.onMouseEnter(e);
                flags.isHovering = true; // mark as active
            }

            // if mouse is outside clickable hitbox
            else if ((x < c.getX() || x > c.getX() + c.getWidth() ||
                      y < c.getY() || y > c.getY() + c.getHeight()
                     ) && flags.isHovering /* and clickable is currently active */) {
                c.onMouseLeave(e);
                flags.isHovering = false; // mark as not active
            }
        }
    }
}
