package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.logandhillon.typeofwar.engine.GameScene.WINDOW_WIDTH;

/**
 * The rope is the part of the main game loop that visualizes who's winning, the players, etc.
 *
 * @author Logan Dhillon
 * @see com.logandhillon.typeofwar.game.TypeOfWarScene
 */
public class RopeEntity extends Entity {
	private static final double HEIGHT = 16;

	public RopeEntity(double x, double y) {
		super(x, y);
	}

	@Override
	public void onUpdate() {

	}

	@Override
	public void onRender(GraphicsContext g, double x, double y) {
		g.setFill(Color.WHITE);
		g.fillRect(x, (y + HEIGHT) / 2, WINDOW_WIDTH - (x*2), HEIGHT);
	}
}
