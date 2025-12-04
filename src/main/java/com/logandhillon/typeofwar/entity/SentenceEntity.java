package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

/**
 * A SentenceEntity is the entity responsible for displaying the sentence and user input.
 * Handles keyboard input and text validation.
 *
 * @author Logan Dhillon
 * @see SentenceEntity#onKeyTyped(KeyEvent)
 * @see SentenceEntity#onKeyPressed(KeyEvent)
 */
public class SentenceEntity extends Entity {
	private static final int CHAR_WIDTH = 18;
	private static final int LINE_HEIGHT = 28;
	private static final Font FONT = Font.font(Fonts.DM_MONO, 32);

	private String[] text;
	private StringBuilder[] input;
	private int currentWord;

	/**
	 * Creates a new SentenceEntity at the provided coordinates.
	 * @apiNote {@link SentenceEntity#setText(String)} must be called before using this entity.
	 * @param x distance from left
	 * @param y distance from top
	 */
    public SentenceEntity(double x, double y) {
		super(x, y);
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void onRender(GraphicsContext g, double x, double y) {
		g.setFont(FONT);

		double dx = 64; // left-margin of text
		double cursorX = dx-2*CHAR_WIDTH;

		// for each word
		for (int i = 0; i < text.length; i++) {
			// for each letter in each word
			for (int j = 0; j < Math.max(text[i].length(), input[i].length()); j++) {
				// if input is long enough
				if (j < input[i].length()) {
					// show WHITE if text is long enough and char is valid, otherwise red.
					g.setFill(j < text[i].length() && text[i].charAt(j) == input[i].charAt(j) ? Color.WHITE : Color.RED);
					g.fillText(String.valueOf(input[i].charAt(j)), dx, y);
					cursorX = dx;

				// if text is long enough
				} else {
					// show dark red if word current word is ahead of this word (thus word incomplete) otherwise gray
					g.setFill(i >= currentWord ? Color.GRAY : Color.DARKRED);
					g.fillText(String.valueOf(text[i].charAt(j)), dx, y);
				}

				dx += CHAR_WIDTH;
			}

			dx += CHAR_WIDTH;
		}

		// handle moving across words (independent of char)
		if (input[currentWord].isEmpty()) {
			if (currentWord == 0) cursorX += CHAR_WIDTH;
			else cursorX += (text[currentWord-1].length() - input[currentWord-1].length()+1) * CHAR_WIDTH;
		}

		g.setFill(Color.WHITE);
		g.fillRect(cursorX + CHAR_WIDTH, y - (LINE_HEIGHT*0.8), 1, LINE_HEIGHT);
	}

	/**
	 * Sets this entity's sentence text that must be typed out.
	 * This will also reset user input.
	 * @param text The new text
	 */
	public void setText(String text) {
		this.text = text.split(" ");

		// reset user input
		input = new StringBuilder[this.text.length];
		Arrays.setAll(input, i -> new StringBuilder());

		currentWord = 0;
    }

	/**
	 * Runs when any key is pressed;
	 * handles backspaces by deleting characters and decrementing words.
	 * @param e KeyEvent from {@link Scene#onKeyPressedProperty()}
	 */
	public void onKeyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.BACK_SPACE) {
			if (input[currentWord].isEmpty() && currentWord > 0) {
				currentWord--;
			} else if (!input[currentWord].isEmpty()) {
				input[currentWord].deleteCharAt(input[currentWord].length() - 1);
			}
        }
	}

	/**
	 * Runs when any key is typed;
	 * handles typed characters and new words (spaces)
	 * @param e KeyEvent from {@link Scene#onKeyTypedProperty()}
	 */
	public void onKeyTyped(KeyEvent e) {
		String c = e.getCharacter();

		// ignore blank/control characters
        if (c.isEmpty() || Character.isISOControl(c.charAt(0))) return;

		// handle spaces (new words); increment word counter only if current word isn't blank
        if (c.equals(" ")) {
			if (!input[currentWord].isEmpty() && currentWord + 1 < input.length) currentWord++;
			return;
		}

		input[currentWord].append(c);
    }
}
