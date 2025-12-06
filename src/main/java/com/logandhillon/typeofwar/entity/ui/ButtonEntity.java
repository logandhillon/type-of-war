package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.resource.Fonts;
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
 * To create a button, use the {@link ButtonBuilder}.
 *
 * @author Logan Dhillon
 */
public class ButtonEntity extends Clickable {
    private static final int STROKE = 2;

    private final String   label;
    private final Color    buttonColor;
    private final Color    labelColor;
    private final Runnable clickHandler;
    private final Font     font;
    private final float    cx; // horizontal center
    private final float    cy; // vertical center
    private final Style    style;

    /**
     * To create a ButtonEntity, use the {@link ButtonBuilder} class.
     */
    protected ButtonEntity(String label, Color buttonColor, Color labelColor, float x, float y, float w, float h,
                           Runnable onClick, int fontSize, Style style) {
        super(x, y, w, h);
        this.label = label;
        this.buttonColor = buttonColor;
        this.labelColor = labelColor;
        this.clickHandler = onClick;
        this.style = style;

        font = Font.font(Fonts.DM_MONO_MEDIUM, fontSize);
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
        clickHandler.run();
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
        if (style == Style.OUTLINE) {
            g.setStroke(buttonColor);
            g.setLineWidth(STROKE);
            g.setLineDashes(null);
            g.strokeRect(x, y, w, h);
        } else if (style == Style.FILL) {
            g.setFill(buttonColor);
            g.fillRect(x, y, w, h);
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

    public enum Style {
        FILL, OUTLINE
    }
}
