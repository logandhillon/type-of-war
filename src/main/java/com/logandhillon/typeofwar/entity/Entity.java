package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;

/**
 * An entity is the most basic paradigm that can be handled by the game engine. It provides methods for
 * {@link Entity#onUpdate()} and {@link Entity#onRender(GraphicsContext, double, double)}, which must be implemented by
 * your subclass.
 *
 * @author Logan Dhillon
 * @see com.logandhillon.typeofwar.engine.GameScene
 */
public abstract class Entity extends GameObject {
    protected double x;
    protected double y;

    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public Entity(double x, double y) {
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
     * Immediately sets the absolute position of this entity to the new position.
     *
     * @param x new x-position
     * @param y new y-position
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
