package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author Logan Dhillon
 */
public class GameStatisticsEntity extends Entity {
    private static final Font FONT_WPM    = Font.font(Fonts.DM_MONO_MEDIUM, 48);
    private static final Font FONT_HEADER = Font.font(Fonts.DM_MONO_MEDIUM, 28);
    private static final Font FONT_BODY   = Font.font(Fonts.DM_MONO, 20);

    private static final int WPM_CHAR_WIDTH   = 29;
    private static final int BODY_LINE_HEIGHT = 36;

    private final float width;

    private float elapsedSeconds; // used to calculate time (min) for wpm
    private float updateTimer; // used to recalculate stats every second

    private String wpm              = "0";
    private String accuracyText     = "-";
    private String completionText   = "-";
    private String speedRankText    = "-";
    private String accuracyRankText = "-";

    private boolean isWinning;
    private int     rawWpm;
    private float   accuracy;
    private int     typedWords = 0;
    private int     wordCount;

    /**
     * Creates an entity at the specified position.
     *
     * @param x     x-position (from left)
     * @param y     y-position (from top)
     * @param width total entity width, including whitespace, excluding margin
     */
    public GameStatisticsEntity(float x, float y, float width) {
        super(x, y);
        this.width = width;

        // reset counters
        this.elapsedSeconds = 0;
        this.updateTimer = 0;
    }

    @Override
    public void onUpdate(float dt) {
        elapsedSeconds += dt;
        updateTimer += dt;

        if (updateTimer >= 1.0f) {
            updateTimer -= 1.0f;
            rawWpm = (int)(typedWords / (elapsedSeconds / 60f));
            wpm = String.valueOf((int)(rawWpm * accuracy));
        }
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.LEFT);

        // left side stats
        g.setFont(FONT_WPM);
        g.fillText(wpm, x, y);

        g.setFont(FONT_HEADER);
        g.fillText("wpm", x + wpm.length() * WPM_CHAR_WIDTH + 16, y);

        g.setFont(FONT_BODY);
        g.fillText(accuracyText, x, y + BODY_LINE_HEIGHT);
        g.fillText(completionText, x, y + 2 * BODY_LINE_HEIGHT);

        // right side stats
        g.setTextAlign(TextAlignment.RIGHT);

        g.fillText(speedRankText, x + width, y + BODY_LINE_HEIGHT);
        g.fillText(accuracyRankText, x + width, y + 2 * BODY_LINE_HEIGHT);

        g.setFont(FONT_HEADER);
        g.setFill(isWinning ? Colors.GOLD_GRADIENT : Color.RED);
        g.fillText(isWinning ? "You're in the lead!" : "Catch-up!", x + width, y);
    }

    @Override
    public void onDestroy() {

    }

    /**
     * Updates the current player statistics, calculates what they are, and stores them for rendering. Call this
     * whenever the statistics change.
     *
     * @param correctChars amount of correct characters the user has typed
     * @param typedChars   amount of total characters the user has typed in this session
     * @param correctWords net correct words the user has finished
     * @param typedWords   amount of total words the user has typed, including erased ones (correct or not)
     * @param isWinning    if the player's team is winning
     */
    public void updateStats(int correctChars, int typedChars, int correctWords, int typedWords, boolean isWinning) {
        this.typedWords = typedWords;
        this.accuracy = typedChars == 0 ? 0 : (float)correctChars / typedChars;
        this.accuracyText = (int)(accuracy * 100) + "% accuracy";
        this.completionText = this.wordCount == 0 ? "-" : correctWords + "/" + this.wordCount + " words";
        this.isWinning = isWinning;

        // TODO: placeholder values, impl. these when multiplayer comes
        this.speedRankText = "#0 in speed";
        this.accuracyRankText = "#0 in accuracy";
    }

    /**
     * Sets the word count of the current {@link SentenceEntity}. This should be called only when the sentence changes.
     *
     * @param wordCount new word count
     */
    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    /**
     * Resets the timer that is used to calculate WPM to 0.
     *
     * @apiNote Should be used whenever (a) the sentence changes or (b) the user starts typing for the first time (so
     * the dead time before they started typing isn't counted).
     */
    public void resetTimer() {
        elapsedSeconds = 0;
    }
}
