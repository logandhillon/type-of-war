package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * This is an input field, which handles user input in a box and can be retrieved as a string.
 * <p>
 * This entity exposes onKeyPressed and onKeyTyped events that should be registered to the parent scene.
 *
 * @author Logan Dhillon
 * @see InputBoxEntity#onKeyPressed(KeyEvent)
 * @see InputBoxEntity#onKeyTyped(KeyEvent) 
 */
public class InputBoxEntity extends Clickable {
    private static final int  INPUT_FONT_SIZE = 20;
    private static final int  CORNER_RADIUS   = 16;
    private static final int  MARGIN_X        = 16;
    private static final int  MARGIN_Y        = 12;
    private static final Font INPUT_FONT      = Font.font(Fonts.DM_MONO, INPUT_FONT_SIZE);
    private static final Font LABEL_FONT      = Font.font(Fonts.DM_MONO_MEDIUM, 18);

    private final float  maxWidth;
    private final String placeholder;
    private final String label;
    private final int    charLimit;

    private StringBuilder input;
    private boolean       isActive;

    /**
     * Creates an input field at the specified position. THe height will be calculated from a fixed y-margin of 12px and
     * the font size.
     *
     * @param x           x-position (from left)
     * @param y           y-position (from top)
     * @param w           width of the input box
     * @param placeholder placeholder text (shown when box is blank)
     * @param label       the label to show above the input box, will use the same font size.
     * @param charLimit   maximum allowed characters in this field
     */
    public InputBoxEntity(float x, float y, float w, String placeholder, String label, int charLimit) {
        super(x, y, w, INPUT_FONT_SIZE * 1.3f + 2 * MARGIN_Y); // calc height of box

        this.maxWidth = w - 2 * MARGIN_X;
        this.placeholder = placeholder;
        this.label = label;
        this.charLimit = charLimit;
        this.isActive = false;

        this.input = new StringBuilder();
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Colors.DEFAULT_DARKER);
        g.fillRoundRect(x, y, w, h, CORNER_RADIUS, CORNER_RADIUS);

        g.setTextAlign(TextAlignment.LEFT);
        g.setTextBaseline(VPos.TOP);
        g.setFont(LABEL_FONT);
        g.setFill(Color.WHITE);
        g.fillText(label, x, y - 31);

        g.setFont(INPUT_FONT);
        if (input.isEmpty()) {
            // render placeholder
            g.setFill(Color.hsb(0, 0, 1, 0.2));
            g.fillText(placeholder, x + MARGIN_X, y + MARGIN_Y, maxWidth);
        } else {
            // render input (font is already white)
            g.fillText(input.toString(), x + MARGIN_X, y + MARGIN_Y, maxWidth);
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Handles input by attaching to the key press event (for backspacing)
     *
     * @return true if the event has been consumed (should stop executing)
     */
    public void onKeyPressed(KeyEvent e) {
        if (isActive) {
            if (e.getCode() == KeyCode.BACK_SPACE && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
            e.consume(); // consume if active
        }
    }

    /**
     * Handles input by attaching to the key typed event (for entering input)
     *
     * @return true if the event has been consumed (should stop executing)
     */
    public void onKeyTyped(KeyEvent e) {
        if (!isActive) return;

        String c = e.getCharacter();

        // ignore blank/control characters
        if (c.isEmpty() || Character.isISOControl(c.charAt(0)) || input.length() >= charLimit) return;

        input.append(c);
    }

    /**
     * Gets the current input buffer.
     *
     * @return content of input buffer as a string
     */
    public String getInput() {
        return input.toString();
    }

    /**
     * Sets the input buffer to a new one with predetermined text
     *
     * @param input the text to fill the new input buffer with
     */
    public void setInput(String input) {
        this.input = new StringBuilder(input);
    }

    @Override
    public void onClick(MouseEvent e) {
        this.isActive = true;
    }

    @Override
    public void onBlur(MouseEvent e) {
        this.isActive = false;
    }
}
