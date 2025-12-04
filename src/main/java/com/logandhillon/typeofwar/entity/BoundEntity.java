package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.engine.GameScene;

/**
 * A BoundEntity is a type of {@link Entity} that can only be attached to a specified type of {@link GameScene}.
 *
 * @param <T> the type of {@link GameScene} that this Entity requires to be attached to.
 *
 * @author Logan Dhillon
 * @apiNote the attached parent is not enforced at runtime. ensure you are only attaching this entity to the right type
 * of {@link GameScene}.
 * @see GameScene
 */
public abstract class BoundEntity<T extends GameScene> extends Entity {
    protected T parent;

    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public BoundEntity(float x, float y) {
        super(x, y);
    }

    @Override
    public void onAttach(GameScene parent) {
        try {
            this.parent = (T)parent;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    "This entity cannot attach to a GameScene that does not match the generic type.");
        }
    }
}
