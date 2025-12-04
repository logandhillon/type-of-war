package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.PlayerObject;
import com.logandhillon.typeofwar.entity.RopeEntity;
import com.logandhillon.typeofwar.entity.SentenceEntity;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The main game loop for Type of War.
 *
 * @author Logan Dhillon
 */
public class TypeOfWarScene extends GameScene {
    private final SentenceEntity sentence;
    private final RopeEntity     rope;

    public TypeOfWarScene() {
        sentence = new SentenceEntity(WINDOW_WIDTH / 2.0, (WINDOW_HEIGHT + 192) / 2.0);
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
        g.setFill(Color.BLACK);
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
