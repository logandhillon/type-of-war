package com.logandhillon.typeofwar.resource;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Contains constants for all {@link javafx.scene.paint.Paint} items (colors, gradients, etc.) that are to be used
 * throughout the game but require continuity.
 *
 * @author Logan Dhillon
 */
public final class Colors {
    public static final LinearGradient GOLD_GRADIENT = new LinearGradient(
            0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0.00, Color.rgb(229, 130, 0)),
            new Stop(0.37, Color.rgb(231, 198, 35)),
            new Stop(1.00, Color.rgb(159, 94, 8)));

    public static final LinearGradient BG_WINNING = new LinearGradient(
            0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(8, 4, 44)),
            new Stop(0.33, Color.BLACK));

    public static final LinearGradient BG_LOSING = new LinearGradient(
            0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(39, 3, 3)),
            new Stop(0.33, Color.BLACK));
}
