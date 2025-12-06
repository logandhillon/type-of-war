package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class EndResultEntity extends Entity{
    private static final float WIDTH = 64f;
    private static final float GAP = 24f;
    private static final int STAT_LINE_HEIGHT = (int) (0.8 * 34);
    private static final int SUBTEXT_LINE_HEIGHT = (int) (1.67 * 18);
    private static final Font FONT_STAT    = Font.font(Fonts.DM_MONO_MEDIUM, 34);
    private static final Font FONT_HEADER = Font.font(Fonts.DM_MONO_MEDIUM, 64);
    private static final Font FONT_SUBTEXT   = Font.font(Fonts.DM_MONO, 18);

    private final String wpm;
    private final String accuracy;
    private final String words;
    private final PlayerObject player;


    public EndResultEntity(int wpm, int accuracy, int words, PlayerObject player) {
        super(0, 0);
        this.wpm = String.valueOf(wpm);
        this.accuracy = String.valueOf(accuracy);
        this.words = String.valueOf(words);
        this.player = player;
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {

        // Render player icon
        player.onRender(g, x, y);

        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(FONT_STAT);

        // Render wpm results
        g.fillText(wpm, x + (WIDTH / 2) , 200 + (2 * GAP));

        // Render accuracy results
        g.fillText(accuracy, x + (WIDTH / 2) , 200 + (3 * GAP) + STAT_LINE_HEIGHT + SUBTEXT_LINE_HEIGHT);

        // Render words results
        g.fillText(words, x + (WIDTH / 2) , 200 + (4 * GAP) + (2 * STAT_LINE_HEIGHT) + (2 * SUBTEXT_LINE_HEIGHT));

        // Render subtitles
        g.setFont(FONT_SUBTEXT);
        g.fillText("wpm", x + (WIDTH / 2), 200 + (2 * GAP) + STAT_LINE_HEIGHT);
        g.fillText("accuracy", x + (WIDTH / 2), 200 + (3 * GAP) + (2 *STAT_LINE_HEIGHT) + SUBTEXT_LINE_HEIGHT);
        g.fillText("words", x + (WIDTH / 2), 200 + (4 * GAP) + (3 * STAT_LINE_HEIGHT) + (2 * SUBTEXT_LINE_HEIGHT));

    }

    @Override
    public void onDestroy() {

    }
}
