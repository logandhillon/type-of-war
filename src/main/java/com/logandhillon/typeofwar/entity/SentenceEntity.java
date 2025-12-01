package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

public class SentenceEntity extends Entity {
	private static final int CHAR_WIDTH = 18;
	private static final Font FONT = Font.font("monospace", 32); // TODO: use a custom font instead

	private String[] text;
	private StringBuilder[] input;
	private int currentWord;

    public SentenceEntity(double x, double y) {
		super(x, y);
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onRender(GraphicsContext g, double x, double y) {
		g.setFont(FONT);

		double dx = 64; // left-margin of text

		// for each word
		for (int i = 0; i < text.length; i++) {
			// for each letter in each word
			for (int j = 0; j < Math.max(text[i].length(), input[i].length()); j++) {
				// if input is long enough
				if (j < input[i].length()) {
					// show WHITE if text is long enough and char is valid, otherwise red.
					g.setFill(j < text[i].length() && text[i].charAt(j) == input[i].charAt(j) ? Color.WHITE : Color.RED);
					g.fillText(String.valueOf(input[i].charAt(j)), dx, y);

				// if text is long enough
				} else {
					g.setFill(Color.GRAY);
					g.fillText(String.valueOf(text[i].charAt(j)), dx, y);
				}

				dx += CHAR_WIDTH;
			}

			dx += CHAR_WIDTH;
		}
	}

	public void setText(String text) {
		this.text = text.split(" ");

		// reset user input
		input = new StringBuilder[this.text.length];
		Arrays.setAll(input, i -> new StringBuilder());

		currentWord = 0;
    }

	public void onKeyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.BACK_SPACE) {
			if (input[currentWord].isEmpty()) currentWord--;
			input[currentWord].deleteCharAt(input[currentWord].length() - 1);
        }
	}

	public void onKeyTyped(KeyEvent e) {
		String c = e.getCharacter();

		// ignore blank/control characters
        if (c.isEmpty() || Character.isISOControl(c.charAt(0))) return;

		// handle spaces (new words); increment word counter only if current word isn't blank
        if (c.equals(" ")) {
			if (!input[currentWord].isEmpty()) currentWord++;
			return;
		}

		input[currentWord].append(c);
    }
}
