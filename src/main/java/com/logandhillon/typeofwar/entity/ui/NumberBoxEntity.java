package com.logandhillon.typeofwar.entity.ui;

import javafx.scene.input.KeyEvent;

public class NumberBoxEntity extends InputBoxEntity {
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
    public NumberBoxEntity(float x, float y, float w, String placeholder, String label, int charLimit) {
        super(x, y, w, placeholder, label, charLimit);
    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        if (!isActive) return;

        String c = e.getCharacter();

        // ignore blank/control characters
        if (c.isEmpty() || Character.isISOControl(c.charAt(0)) || input.length() >= charLimit || !c.matches("[0-9]")) return;

        input.append(c);
        e.consume();
    }
}
