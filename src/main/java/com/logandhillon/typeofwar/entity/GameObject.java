package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.engine.GameScene;
import javafx.scene.canvas.GraphicsContext;

/**
 * A GameObject is very the layer below {@link Entity}, which is an independent game object that can be added to scenes.
 * Unlike its independent counterpart, GameObject cannot be rendered on its own (it cannot be added to a
 * {@link com.logandhillon.typeofwar.engine.GameScene}).
 * <p>
 * A GameObject may be used over an (independent) Entity for objects that do not need to be handled by the GameScene,
 * rather, a parent Entity. For instance, {@link PlayerObject} is a child of {@link RopeEntity}.
 * <p>
 * Finally, a GameObject can only be rendered by other objects within the {@link com.logandhillon.typeofwar.entity}
 * package.
 *
 * @author Logan Dhillon
 * @see Entity
 */
public abstract class GameObject {
    /**
     * Called every tick for non-graphics-related updates (Entity lifecycle, etc.)
     *
     * @param dt the delta time: change in time (seconds) since the last frame
     */
    public abstract void onUpdate(float dt);

    /**
     * Called every tick to render the entity; responsible for rendering this entity to the provided graphics context.
     *
     * @param g the graphical context to render to.
     * @param x the x position to render the entity at
     * @param y the y position to render the entity at
     *
     * @apiNote Do not call this to render the entity (unless you are rendering a {@link GameObject}).
     * @implNote implement this method to change the render behaviour of your entity.
     * @see Entity#render(GraphicsContext)
     */
    protected abstract void onRender(GraphicsContext g, float x, float y);

    /**
     * Called when this entity is scheduled to be destroyed.
     *
     * @implNote This method should clean up the entity, so it's ready for destruction.
     */
    public abstract void onDestroy();
}
