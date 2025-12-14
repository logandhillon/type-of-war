package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.game.EndGameScene;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Formatting of the end-game statistics that show on the {@link EndGameScene}
 * @author Jack Ross
 */
public class EndResultEntity extends Entity{
    private static final float WIDTH = 64f;
    private static final float GAP = 24f;
    private static final int STAT_LINE_HEIGHT = (int) (1.1 * 34);
    private static final int SUBTEXT_LINE_HEIGHT = (int) (1.67 * 18);
    private static final Font FONT_STAT    = Font.font(Fonts.DM_MONO_MEDIUM, 34);
    private static final Font FONT_SUBTEXT = Font.font(Fonts.DM_MONO, 18);
    private static final int  PLAYER_GAP   = 16;

    private final int wpm;
    private final int accuracy;
    private final int words;
    private final PlayerObject player;



    public EndResultEntity(int wpm, int accuracy, int words, PlayerObject player) {
        super(0, 0);
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.words = words;
        this.player = player;
    }

    @Override
    public void onUpdate(float dt) {

    }

    /**
     *
     * @param g the graphical context to render to.
     * @param x the x position to render the entity at
     * @param y the y position to render the entity at
     *
     */
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        float center = x + (WIDTH / 2);

        // render player icon + name
        player.render(g, x, y);

        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(FONT_STAT);

        // render wpm results
        g.fillText(String.valueOf(this.wpm), center, y + PLAYER_GAP + (2 * GAP));

        // render accuracy results
        g.fillText(String.valueOf(this.accuracy), center, y + PLAYER_GAP + (3 * GAP) + STAT_LINE_HEIGHT + SUBTEXT_LINE_HEIGHT);

        // render words results
        g.fillText(String.valueOf(this.words), center, y + PLAYER_GAP + (4 * GAP) + (2 * STAT_LINE_HEIGHT) + (2 * SUBTEXT_LINE_HEIGHT));

        // render subtitles
        g.setFont(FONT_SUBTEXT);
        g.fillText("wpm", center, y + PLAYER_GAP + (2 * GAP) + STAT_LINE_HEIGHT);
        g.fillText("accuracy", center, y + PLAYER_GAP + (3 * GAP) + (2 * STAT_LINE_HEIGHT) + SUBTEXT_LINE_HEIGHT);
        g.fillText("words", center, y + PLAYER_GAP + (4 * GAP) + (3 * STAT_LINE_HEIGHT) + (2 * SUBTEXT_LINE_HEIGHT));
    }

    @Override
    public void onDestroy() {

    }
}
