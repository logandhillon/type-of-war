package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class EndResultEntity extends Entity{
    private static final Font FONT_STAT    = Font.font(Fonts.DM_MONO_MEDIUM, 34);
    private static final Font FONT_HEADER = Font.font(Fonts.DM_MONO_MEDIUM, 64);
    private static final Font FONT_SUBTEXT   = Font.font(Fonts.DM_MONO, 18);

    private final int wpm;
    private final int accuracy;
    private final int words;
    private final PlayerObject player;

    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public EndResultEntity(float x, float y, int wpm, int accuracy, int words, PlayerObject player) {
        super(x, y);
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.words = words;
        this.player = player;
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
    }

    @Override
    public void onDestroy() {

    }
}
