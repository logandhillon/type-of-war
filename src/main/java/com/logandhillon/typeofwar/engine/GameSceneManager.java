package com.logandhillon.typeofwar.engine;

/**
 * Provides the interface for any implementation of the core game engine mechanics: switching game scenes.
 *
 * @author Logan Dhillon
 * @apiNote Your "main" class should usually implement this.
 */
public interface GameSceneManager {
    /**
     * Builds and stage a new scene, setting it as the active one and discarding the previously active one.
     *
     * @param scene the scene to switch to.
     */
    void setScene(GameScene scene);

    /**
     * Sets the current scene to the main menu.
     */
    void goToMainMenu();
}
