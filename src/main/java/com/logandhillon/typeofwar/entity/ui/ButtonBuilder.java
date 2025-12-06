package com.logandhillon.typeofwar.entity.ui;

import javafx.scene.paint.Color;

/**
 * Builds a new {@link ButtonEntity} with custom attributes. To build a button, you must:
 * <ul>
 * <li>set the label</li>
 * <li>set the color</li>
 * <li>set the position</li>
 * <li>set the action</li>
 * </ul>
 * The other provided methods are optional and have default values.
 *
 * @author Logan Dhillon
 */
public class ButtonBuilder {
    private String             label;
    private Color              labelColor;
    private Color              buttonColor;
    private float              x;
    private float              y;
    private float              w        = 192;
    private float              h        = 64;
    private Runnable           onClick;
    private int                fontSize = 18;
    private ButtonEntity.Style style    = ButtonEntity.Style.FILL;

    public ButtonBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * @param buttonColor the color of the button background
     * @param labelColor  the color of the label
     */
    public ButtonBuilder setColors(Color buttonColor, Color labelColor) {
        this.labelColor = labelColor;
        this.buttonColor = buttonColor;
        return this;
    }

    /**
     * @param color the color of the button background and label
     */
    public ButtonBuilder setColors(Color color) {
        return this.setColors(color, color);
    }

    /**
     * sets the position of the button
     */
    public ButtonBuilder at(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * sets the size of the button
     */
    public ButtonBuilder withSize(float w, float h) {
        this.w = w;
        this.h = h;
        return this;
    }

    /**
     * sets the mouse click handler
     *
     * @param onClick the action to run when the button is pressed
     */
    public ButtonBuilder setAction(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    public ButtonBuilder setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public ButtonBuilder setStyle(ButtonEntity.Style style) {
        this.style = style;
        return this;
    }

    // TODO: throw error if button is built with null values
    public ButtonEntity build() {
        return new ButtonEntity(label, buttonColor, labelColor, x, y, w, h, onClick, fontSize, style);
    }
}