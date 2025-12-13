package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.GameStatisticsEntity;
import com.logandhillon.typeofwar.entity.PlayerObject;
import com.logandhillon.typeofwar.entity.RopeEntity;
import com.logandhillon.typeofwar.entity.SentenceEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.List;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;
import static com.logandhillon.typeofwar.resource.Colors.BG_LOSING;
import static com.logandhillon.typeofwar.resource.Colors.BG_WINNING;

/**
 * The main game loop for Type of War.
 *
 * @author Logan Dhillon
 */
public class TypeOfWarScene extends GameScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(TypeOfWarScene.class);

    private final GameStatisticsEntity stats;

    private boolean isWinning = true;

    public TypeOfWarScene(List<PlayerObject> team1, List<PlayerObject> team2) {
        stats = new GameStatisticsEntity(64, 144, WINDOW_WIDTH.floatValue() - 128);
        addEntity(stats);

        SentenceEntity sentence = new SentenceEntity(
                WINDOW_WIDTH.floatValue() / 2f, (WINDOW_HEIGHT.floatValue() + 300) / 2f);
        addEntity(sentence);
        sentence.setText("The quick brown fox jumps over the lazy dog.");

        RopeEntity rope = new RopeEntity(64, WINDOW_HEIGHT.floatValue());
        for (var p: team1) rope.addPlayer(p, RopeEntity.Team.LEFT);
        for (var p: team2) rope.addPlayer(p, RopeEntity.Team.RIGHT);
        addEntity(rope);
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(isWinning ? BG_WINNING : BG_LOSING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }

    /**
     * Propagates an update in the {@link GameStatisticsEntity} instance.
     *
     * @param correctChars amount of correct characters the user has typed
     * @param typedChars   amount of total characters the user has typed in this session
     * @param correctWords net correct words the user has finished
     */
    public void updateStats(int correctChars, int typedChars, int correctWords) {
        stats.updateStats(correctChars, typedChars, correctWords, isWinning);
    }

    /**
     * Sets the word count in the {@link GameStatisticsEntity} instance.
     *
     * @param wordCount new word count of the sentence
     */
    public void setWordCount(int wordCount) {
        stats.setWordCount(wordCount);
    }

    /**
     * Resets the timer used for calculating WPM in the {@link GameStatisticsEntity}.
     */
    public void resetWPMTimer() {
        LOG.info("Resetting WPM timer");
        stats.restartSession();
    }

    /**
     * Marks this typing session as finished, and propagates that information to {@link GameStatisticsEntity}.
     */
    public void onTypingFinished() {
        LOG.info("Typing finished, closing session");
        stats.finishSession();
    }
}
