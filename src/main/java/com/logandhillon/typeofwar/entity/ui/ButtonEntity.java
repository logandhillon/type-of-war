package com.logandhillon.typeofwar.entity.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * A button is a stylized {@link Clickable} entity that can have a button style, label, and action that runs when it's
 * clicked.
 * <p>
 * To create a button, use the {@link ButtonStyle}.
 *
 * @author Logan Dhillon
 * @see ButtonStyle
 */
public class ButtonEntity extends Clickable {
    private static final int STROKE          = 2;
    private static final int ROUNDING_RADIUS = 16;

    private final String       label;
    private final Color        buttonColor;
    private final Color        labelColor;
    private final ClickHandler clickHandler;
    private final Font         font;
    private final Variant      style;
    private final boolean isRounded;

    private final float cx; // horizontal center
    private final float cy; // vertical center

    /**
     * To create a ButtonEntity, use the {@link ButtonStyle} class.
     */
    protected ButtonEntity(String label, Color buttonColor, Color labelColor, float x, float y, float w, float h,
                           ClickHandler onClick, Font font, Variant style) {
        super(x, y, w, h);
        this.label = label;
        this.buttonColor = buttonColor;
        this.labelColor = labelColor;
        this.clickHandler = onClick;
        this.style = style;
        this.font = font;

        isRounded = style == Variant.ROUNDED_OUTLINE || style == Variant.ROUNDED_SOLID;

        cx = x + w / 2;
        cy = y + h / 2;
    }

    /**
     * Runs the event handler was supplied when the Button was created.
     *
     * @param e the mouse event provided by JavaFX
     */
    @Override
    public void onClick(MouseEvent e) {
        clickHandler.onClick(e);
    }

    @Override
    public void onUpdate(float dt) {

    }

    /**
     * Renders the button background, then the button text on top of it, based on what was supplied when the button was
     * created.
     *
     * @param g the graphical context to render to.
     * @param x the x position to render the entity at
     * @param y the y position to render the entity at
     */
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        // render button background
        if (style == Variant.OUTLINE || style == Variant.ROUNDED_OUTLINE) {
            g.setStroke(buttonColor);
            g.setLineWidth(STROKE);
            g.setLineDashes(0);

            if (isRounded) g.strokeRoundRect(x, y, w, h, ROUNDING_RADIUS, ROUNDING_RADIUS);
            else g.strokeRect(x, y, w, h);
        } else {
            g.setFill(buttonColor);
            if (isRounded) g.fillRoundRect(x, y, w, h, ROUNDING_RADIUS, ROUNDING_RADIUS);
            else g.fillRect(x, y, w, h);
        }

        // render label
        g.setFill(labelColor);
        g.setFont(font);
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.CENTER);
        g.fillText(label, cx, cy);
    }

    @Override
    public void onDestroy() {

    }

    public enum Variant {
        SOLID, OUTLINE, ROUNDED_SOLID, ROUNDED_OUTLINE
    }

    public interface ClickHandler {
        void onClick(MouseEvent e);
    }
}
