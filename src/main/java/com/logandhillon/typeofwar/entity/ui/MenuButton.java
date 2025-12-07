package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * A stylized version of the {@link DynamicButtonEntity} made for menus.
 *
 * @author Logan Dhillon
 */
public class MenuButton extends DynamicButtonEntity {
    private static final ButtonEntity.Style DEFAULT_STYLE = new ButtonEntity.Style(
            Color.WHITE, Colors.DEFAULT, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.DM_MONO_MEDIUM, 20));
    private static final ButtonEntity.Style ACTIVE_STYLE  = new ButtonEntity.Style(
            Color.WHITE, Colors.PRIMARY, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.DM_MONO_MEDIUM, 21));

    /**
     * Creates a new dynamic button entity using the preset styles for menu buttons.
     *
     * @param label   the text to show on the button
     * @param w       width
     * @param h       height
     * @param onClick the action that should happen when this button is clicked
     */
    public MenuButton(String label, float x, float y, float w, float h, MouseEventHandler onClick) {
        super(label, x, y, w, h, onClick, DEFAULT_STYLE, ACTIVE_STYLE);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        super.onRender(g, x, y);
        // assuming the fill and font have already been set from super#onRender

        if (this.isActive()) {
            // left arrow
            g.setTextAlign(TextAlignment.LEFT);
            g.fillText(">", x+16, y + h/2);

            // right arrow
            g.setTextAlign(TextAlignment.RIGHT);
            g.fillText("<", x + w - 16, y + h/2);
        }
    }
}
