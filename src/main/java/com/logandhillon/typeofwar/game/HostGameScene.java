package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.*;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

public class HostGameScene extends UIScene {
    private static final int AJITESH_CONSTANT = 22;
    private final InputBoxEntity nameInput;
    private final DarkMenuButton[] buttons;
    private final InputBoxEntity sentenceInput;
    public HostGameScene(GameSceneManager mgr) {

        // modal entity
        // room name + "input field"
        // base strength buttons
        // custom sentence + "input field"
        // start game button

        buttons = new DarkMenuButton[5];
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            buttons[i] = new DarkMenuButton((8f + (i * 2)) / 10 + "X", 16 + (i * (100 + 8)), 152, 100, 48, () -> {
                buttons[finalI].setActive(true);

            });
        }

        nameInput = new InputBoxEntity(16, 47, 530, "Player1's Room", "ROOM NAME", AJITESH_CONSTANT);

        sentenceInput = new InputBoxEntity(16, 255, 530, "Leave blank to randomly generate", "CUSTOM SENTENCE", 500);
        addEntity(new ModalEntity(359, 128, 562, 464, nameInput, sentenceInput, buttons[0], buttons[1], buttons[2], buttons[3], buttons[4]));
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
