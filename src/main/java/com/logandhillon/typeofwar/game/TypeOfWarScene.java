package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TypeOfWarScene extends GameScene {
    @Override
    protected void onUpdate() {

    }

    @Override
    protected void render(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 1280, 720);

        g.setFill(Color.WHITE);
        g.fillRect(64, (WINDOW_HEIGHT + 16) / 2.0, WINDOW_WIDTH - 128, 16);

        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(new Font(32));
        g.fillText("The quick brown fox jumps over the lazy dog.", WINDOW_WIDTH/2.0, (WINDOW_HEIGHT+192)/2.0);
    }
}
