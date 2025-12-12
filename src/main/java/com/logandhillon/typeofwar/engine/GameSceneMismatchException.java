package com.logandhillon.typeofwar.engine;

/**
 * This exception should be thrown if a certain type of {@link GameScene} was expected, but got another. Should only be
 * thrown from the engine itself (game scene manager).
 *
 * @author Logan Dhillon
 */
public class GameSceneMismatchException extends IllegalStateException {
    public GameSceneMismatchException(Class<? extends GameScene> active, Class<? extends GameScene> expected) {
        super("Active scene is " + active.getName() + " but expected " + expected.getName());
    }
}
