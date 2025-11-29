package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SentenceEntity extends Entity {
	private static final int CHAR_WIDTH = 18;
	private static final int FONT_SIZE = 32;

	private String text;
	private int currentCharIdx;

	public SentenceEntity(double x, double y) {
		super(x, y);
	}

	@Override
	public void onUpdate() {

	}

	@Override
	public void onRender(GraphicsContext g, double x, double y) {
		Font font = Font.font("Consolas", FONT_SIZE);
		g.setFont(font);

		double totalWidth = CHAR_WIDTH * text.length();
		double dx = x - totalWidth / 2.0;

		for (int i = 0; i < text.length(); i++) {
			g.setFill(Color.hsb(0, 0, currentCharIdx > i ? 1 : 0.5));
			g.fillText(String.valueOf(text.charAt(i)), dx + i * CHAR_WIDTH, y);
		}
	}

	public void setText(String text) {
		this.text = text;
		currentCharIdx = 0;
	}

	public void onKeyTyped(KeyEvent e) {
		if (String.valueOf(text.charAt(currentCharIdx)).equals(e.getCharacter())) {
			currentCharIdx++;
		}
	}
}
