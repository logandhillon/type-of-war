package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A stylized version of the {@link DynamicButtonEntity} made for in-game contexts.
 *
 * @author Logan Dhillon
 */
public class GameButton extends DynamicButtonEntity {
    private static final ButtonEntity.Style DEFAULT_STYLE = new ButtonEntity.Style(
            Color.WHITE, Colors.DEFAULT, Variant.OUTLINE, true, Font.font(Fonts.DM_MONO_MEDIUM, 16));
    private static final ButtonEntity.Style ACTIVE_STYLE  = new ButtonEntity.Style(
            Color.WHITE, Colors.PRIMARY, Variant.OUTLINE, true, Font.font(Fonts.DM_MONO_MEDIUM, 17));

    /**
     * Creates a new dynamic button entity using the preset styles for menu buttons.
     *
     * @param label   the text to show on the button
     * @param w       width
     * @param h       height
     * @param onPress the action that should happen when this button is clicked
     */
    public GameButton(String label, float x, float y, float w, float h, Runnable onPress) {
        super(label.toUpperCase(), x, y, w, h, e -> onPress.run(), DEFAULT_STYLE, ACTIVE_STYLE);
    }
}
