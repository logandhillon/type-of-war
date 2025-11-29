package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.logandhillon.typeofwar.engine.GameScene.WINDOW_WIDTH;

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
