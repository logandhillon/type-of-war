package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;

/**
 * An entity is the most basic paradigm that can be handled by the game engine.
 * It provides methods for {@link Entity#onUpdate()} and {@link Entity#onRender(GraphicsContext, double, double)}, which
 * must be implemented by your subclass.
 *
 * @author Logan Dhillon
 * @see com.logandhillon.typeofwar.engine.GameScene
 */
public abstract class Entity {
	protected double x;
	protected double y;

	/**
	 * Creates an entity at the specified position.
	 * @param x x-position (from left)
	 * @param y y-position (from top)
	 */
	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Called every tick for non-graphics-related updates (Entity lifecycle, etc.)
	 */
	public abstract void onUpdate();

	/**
	 * Called every tick to render the entity.
	 *
	 * @param g the graphical context to render to.
	 *
	 * @implNote implement this method to change the render behaviour of your entity.
	 */
	public void render(GraphicsContext g) {
		this.onRender(g, x, y);
	}

	/**
	 * Called every tick to render the entity.
	 *
	 * @param g the graphical context to render to.
	 * @param x the x position to render the entity at
	 * @param y the y position to render the entity at
	 *
	 * @apiNote Do not call this to render the entity.
	 * @see Entity#render(GraphicsContext)
	 */
	protected abstract void onRender(GraphicsContext g, double x, double y);
}
