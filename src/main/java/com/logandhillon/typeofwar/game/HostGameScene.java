package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.*;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

public class HostGameScene extends UIScene {
    private static final Font LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 18);
    public static int startingMultiplier; //TODO: add scaling multiplier to game (starting from this set point)
    private static final int AJITESH_CONSTANT = 22;
    private static final int  OFFSET_Y = -20;

    private final InputBoxEntity nameInput;
    private final DarkMenuButton[] buttons;
    private final InputBoxEntity sentenceInput;
    private final DarkMenuButton startButton;

    public HostGameScene(GameSceneManager mgr) {

        Entity ButtonsLabel = new Entity(0, 0) {
            @Override
            protected void onRender(GraphicsContext g, float x, float y) {
                g.setTextAlign(TextAlignment.LEFT);
                g.setTextBaseline(VPos.TOP);
                g.setFont(LABEL_FONT);
                g.setFill(Color.WHITE);
                g.fillText("BASE STRENGTH MULTIPLIER", x + 16, y + 152 - 31);
            }
            @Override
            public void onUpdate(float dt) {}

            @Override
            public void onDestroy() {}
        };

        buttons = new DarkMenuButton[5];
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            buttons[i] = new DarkMenuButton((8f + (i * 2)) / 10 + "X", 16 + (i * (100 + 8)), 152 + OFFSET_Y, 100, 48, () -> {
                buttons[finalI].setActive(true);
                startingMultiplier = (8 + (finalI * 2)) / 10;
            });
        }

        nameInput = new InputBoxEntity(16, 47 + OFFSET_Y, 530, "Player1's Room", "ROOM NAME", AJITESH_CONSTANT);

        sentenceInput = new InputBoxEntity(16, 255 + OFFSET_Y, 530, "Leave blank to randomly generate", "CUSTOM SENTENCE", 500);

        startButton = new DarkMenuButton("START GAME", 16, 329 + OFFSET_Y, 530, 50, () -> {
           mgr.setScene(new TypeOfWarScene());
        });

        addEntity(new ModalEntity(359, 128, 562, 464, nameInput, sentenceInput, buttons[0], buttons[1], buttons[2], buttons[3], buttons[4], ButtonsLabel, startButton));
    }
    @Override
    protected void render(GraphicsContext g) {
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());
        super.render(g);
    }

    @Override
    protected void onBuild(Scene scene) {

        super.onBuild(scene);
        scene.setOnKeyPressed(e -> {
            nameInput.onKeyPressed(e);
            if (e.isConsumed()) return;
            sentenceInput.onKeyPressed(e);
        });

        scene.setOnKeyTyped( e -> {
            nameInput.onKeyTyped(e);
            sentenceInput.onKeyTyped(e);
        });

    }
}
