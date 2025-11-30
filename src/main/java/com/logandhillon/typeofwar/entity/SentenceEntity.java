package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SentenceEntity extends Entity {
	private static final int CHAR_WIDTH = 18;
	private static final Font FONT = Font.font("monospace", 32); // TODO: use a custom font instead

	private String text;
	private StringBuilder rawInput;
	private StringBuilder correctInput;
	private int currentCharIdx;

	public SentenceEntity(double x, double y) {
		super(x, y);
	}

	@Override
	public void onUpdate() {

	}

	// FIXME: this stops working once the user makes ONE error; probably have to parse by-word? idk yet
	private boolean isCharCorrect(int i) {
		return text.length() > i && text.charAt(i) == rawInput.charAt(i);
	}

	@Override
	public void onRender(GraphicsContext g, double x, double y) {
		g.setFont(FONT);

		double dx = x - (CHAR_WIDTH * Math.max(text.length(), rawInput.length())) / 2.0;

		// draw the user's input and validate it (show white or red depending on correctness)
		for (int i = 0; i < rawInput.length(); i++) {
			g.setFill(isCharCorrect(i) ? Color.WHITE : Color.RED);
			g.fillText(String.valueOf(rawInput.charAt(i)), dx + i * CHAR_WIDTH, y);
		}

		// draw the remaining characters from the sentence
		for (int i = correctInput.length(); i < text.length(); i++) {
			g.setFill(Color.hsb(0, 0, 0.5));
			g.fillText(String.valueOf(text.charAt(i)), dx + (i - correctInput.length() + rawInput.length()) * CHAR_WIDTH, y);
		}
	}

	public void setText(String text) {
		this.text = text;
		currentCharIdx = 0;

		// reset user input
		correctInput = new StringBuilder();
		rawInput = new StringBuilder();
	}

	public void onKeyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.BACK_SPACE) {
			if (rawInput.length() == correctInput.length())
				correctInput.deleteCharAt(rawInput.length() - 1);
			rawInput.deleteCharAt(rawInput.length() - 1);
		}
	}

	public void onKeyTyped(KeyEvent e) {
		String c = e.getCharacter();

		// ignore blank/control characters
		if (c.isEmpty() || Character.isISOControl(c.charAt(0))) return;

		rawInput.append(c);
		if (String.valueOf(text.charAt(currentCharIdx)).equals(c)) {
			correctInput.append(c);
			currentCharIdx++;
		}
	}
}
