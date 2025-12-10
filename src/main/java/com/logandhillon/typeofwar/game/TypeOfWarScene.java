package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.GameStatisticsEntity;
import com.logandhillon.typeofwar.entity.PlayerObject;
import com.logandhillon.typeofwar.entity.RopeEntity;
import com.logandhillon.typeofwar.entity.SentenceEntity;
import com.logandhillon.typeofwar.resource.WordGen;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.io.IOException;

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

    private final SentenceEntity sentence;
    private final RopeEntity rope;
    private final GameStatisticsEntity stats;

    private boolean isWinning = true;

    public TypeOfWarScene() {
        stats = new GameStatisticsEntity(64, 144, WINDOW_WIDTH.floatValue() - 128);
        addEntity(stats);

        sentence = new SentenceEntity(WINDOW_WIDTH.floatValue() / 2f, (WINDOW_HEIGHT.floatValue() + 300) / 2f);
        addEntity(sentence);

        try {
            sentence.setText(WordGen.generateSentence(10000));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PlayerObject testPlayer = new PlayerObject("Player1", Color.CYAN);

        rope = new RopeEntity(64, WINDOW_HEIGHT.floatValue());
        rope.addPlayer(testPlayer, RopeEntity.Team.LEFT);
        rope.addPlayer(testPlayer, RopeEntity.Team.LEFT);
        rope.addPlayer(testPlayer, RopeEntity.Team.RIGHT);
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

    @Override
    protected void onBuild(Scene scene) {
        scene.setOnKeyPressed(sentence::onKeyPressed);
        scene.setOnKeyTyped(sentence::onKeyTyped);
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
        stats.restartSession();
    }

    /**
     * Marks this typing session as finished, and propagates that information to {@link GameStatisticsEntity}.
     */
    public void onTypingFinished() {
        stats.finishSession();
    }
}
