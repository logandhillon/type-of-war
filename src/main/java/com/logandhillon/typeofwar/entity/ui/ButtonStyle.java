package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Creates a new style of a button, which can be turned into a {@link ButtonEntity} by using
 * {@link ButtonStyle#build(String, float, float, ButtonEntity.ClickHandler)}.
 *
 * @author Logan Dhillon
 * @see ButtonEntity
 */
public class ButtonStyle {
    private static final Font DEFAULT_FONT = Font.font(Fonts.DM_MONO, 18);

    private Color                labelColor;
    private Color                buttonColor;
    private float                w = 192;
    private float                h = 64;
    private ButtonEntity.Variant style;
    private Font                 font;

    public ButtonStyle(ButtonEntity.Variant style) {
        this.style = style;
    }

    /**
     * @param buttonColor the color of the button background
     * @param labelColor  the color of the label
     */
    public ButtonStyle setColors(Color buttonColor, Color labelColor) {
        this.labelColor = labelColor;
        this.buttonColor = buttonColor;
        return this;
    }

    /**
     * Sets the background color and label color to the same color.
     *
     * @param color the color of both the button background and label
     */
    public ButtonStyle setColors(Color color) {
        return setColors(color, color);
    }

    /**
     * sets the size of the button
     */
    public ButtonStyle withSize(float w, float h) {
        this.w = w;
        this.h = h;
        return this;
    }

    public ButtonStyle setFont(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Builds a new {@link ButtonEntity} that is stylized as specified in this instance.
     *
     * @param label   the label to show on the button
     * @param x       the x position to build the entity at
     * @param y       the y position to build the entity at
     * @param onClick the {@link ButtonEntity.ClickHandler} that runs when the button is clicked
     *
     * @return the real {@link ButtonEntity} that can be added to a game scene.
     */
    public ButtonEntity build(String label, float x, float y, ButtonEntity.ClickHandler onClick) {
        // throw error if the attrs without default values are null
        if (labelColor == null || buttonColor == null)
            throw new NullPointerException("Button cannot be built with null colors");

        return new ButtonEntity(
                label, buttonColor, labelColor, x, y, w, h, onClick, font == null ? DEFAULT_FONT : font, style);
    }
}

