package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.engine.GameScene;
import javafx.scene.canvas.GraphicsContext;

/**
 * An entity is the most basic paradigm that can be handled by the game engine. It provides methods for
 * {@link Entity#onUpdate(float)} and {@link Entity#onRender(GraphicsContext, float, float)}, which must be implemented
 * by your subclass.
 *
 * @param <T> the type of GameScene that this entity may attach to.
 * @author Logan Dhillon
 * @see com.logandhillon.typeofwar.engine.GameScene
 */
public abstract class Entity extends GameObject {
    protected GameScene parent;
    protected float     x;
    protected float     y;

    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Called every tick to render the entity.
     *
     * @param g the graphical context to render to.
     *
     * @implNote do not implement this method to change how the entity is rendered.
     * @see Entity#onRender
     */
    public void render(GraphicsContext g) {
        this.onRender(g, this.x, this.y);
    }

    /**
     * Runs when this object is attached to a parent.
     *
     * @param parent the parent that this object is now attached to.
     *
     * @see GameScene
     */
    public void onAttach(GameScene parent) {
        this.parent = parent;
    }

    /**
     * Immediately sets the absolute position of this entity to the new position.
     *
     * @param x new x-position
     * @param y new y-position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
