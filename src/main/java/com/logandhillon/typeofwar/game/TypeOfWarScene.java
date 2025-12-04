package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.PlayerObject;
import com.logandhillon.typeofwar.entity.RopeEntity;
import com.logandhillon.typeofwar.entity.SentenceEntity;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * The main game loop for Type of War.
 *
 * @author Logan Dhillon
 */
public class TypeOfWarScene extends GameScene {
    private static final LinearGradient BG_WINNING = new LinearGradient(
            0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(8, 4, 44)),
            new Stop(0.33, Color.BLACK));

    private static final LinearGradient BG_LOSING = new LinearGradient(
            0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(39, 3, 3)),
            new Stop(0.33, Color.BLACK));

    private final SentenceEntity sentence;
    private final RopeEntity     rope;

    private boolean isWinning = true;

    public TypeOfWarScene() {
        sentence = new SentenceEntity(WINDOW_WIDTH / 2f, (WINDOW_HEIGHT + 300) / 2f);
        sentence.setText("The quick brown fox jumps over the lazy dog.");
        addEntity(sentence);

        PlayerObject testPlayer = new PlayerObject("Player1", Color.CYAN);

        rope = new RopeEntity(64, WINDOW_HEIGHT);
        rope.addPlayer(testPlayer, RopeEntity.Team.LEFT);
        rope.addPlayer(testPlayer, RopeEntity.Team.LEFT);
        rope.addPlayer(testPlayer, RopeEntity.Team.RIGHT);
        addEntity(rope);
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(isWinning ? BG_WINNING : BG_LOSING);
        g.fillRect(0, 0, 1280, 720);

        // render all other entities
        super.render(g);
    }

    @Override
    protected void onBuild(Scene scene) {
        scene.setOnKeyPressed(sentence::onKeyPressed);
        scene.setOnKeyTyped(sentence::onKeyTyped);
    }
}
