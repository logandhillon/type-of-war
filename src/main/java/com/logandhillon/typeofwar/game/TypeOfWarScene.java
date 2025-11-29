package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.RopeEntity;
import com.logandhillon.typeofwar.entity.SentenceEntity;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class TypeOfWarScene extends GameScene {
    private final SentenceEntity sentence;

    public TypeOfWarScene() {
        sentence = new SentenceEntity(WINDOW_WIDTH / 2.0, (WINDOW_HEIGHT + 192) / 2.0);
        sentence.setText("The quick brown fox jumps over the lazy dog.");

        addEntity(sentence);
        addEntity(new RopeEntity(64, WINDOW_HEIGHT));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 1280, 720);

        // render all other entities
        super.render(g);
    }

    private void onKeyTyped(KeyEvent e) {
        sentence.onKeyTyped(e);
    }

    @Override
    protected void onBuild(Scene scene) {
        scene.setOnKeyTyped(this::onKeyTyped);
    }
}
