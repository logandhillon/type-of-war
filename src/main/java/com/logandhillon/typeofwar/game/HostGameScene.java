package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.DarkMenuButton;
import com.logandhillon.typeofwar.entity.ui.InputBoxEntity;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.atomic.AtomicInteger;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The host game menu allows the user to input parameters needed for hosting a live server
 *
 * @author Jack Ross
 */
public class HostGameScene extends UIScene {
    private static final Font  LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 18);
    // TODO: add scaling multiplier to game (starting from this set point) and default it to 1
    private              float startingMultiplier;

    private static final int AJITESH_CONSTANT = 25;

    private final InputBoxEntity   nameInput;
    private final DarkMenuButton[] buttons;
    private final InputBoxEntity   sentenceInput;

    /**
     * Creates a new main menu
     *
     * @param mgr the {@link GameSceneManager} responsible for switching active scenes.
     */
    public HostGameScene(TypeOfWar mgr) {

        /*
          Basic label above buttons indicating purpose of the base strength multiplier
         */
        Entity ButtonsLabel = new Entity(0, 0) {
            @Override
            protected void onRender(GraphicsContext g, float x, float y) {
                // set initial text variables
                g.setTextAlign(TextAlignment.LEFT);
                g.setTextBaseline(VPos.TOP);
                g.setFont(LABEL_FONT);
                g.setFill(Color.WHITE);

                // render label
                g.fillText("BASE STRENGTH MULTIPLIER", x + 16, y + 152 - 31);
            }

            @Override
            public void onUpdate(float dt) {}

            @Override
            public void onDestroy() {}
        };

        // create new list of radio buttons
        buttons = new DarkMenuButton[5];

        // create integer to be set in OnPress()
        AtomicInteger currentButton = new AtomicInteger();

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            // adds new button to list
            buttons[i] = new DarkMenuButton((8f + (i * 2)) / 10 + "X", 16 + (i * (100 + 8)), 152, 100, 48, () -> {

                // keep highlighted when clicked
                buttons[finalI].setActive(true, true);
                currentButton.set(finalI);

                // un-highlight non-selected buttons
                for (int j = 0; j < buttons.length; j++) {
                    if (currentButton.get() != j) {
                        buttons[j].setActive(false, false);
                    }
                }

                // set variable to value of button pressed
                startingMultiplier = (8f + (finalI * 2)) / 10;
            });
        }

        nameInput = new InputBoxEntity(16, 47, 530, "Player1's Room", "ROOM NAME", AJITESH_CONSTANT);

        sentenceInput = new InputBoxEntity(16, 255, 530, "Leave blank to randomly generate", "CUSTOM SENTENCE", 500);

        DarkMenuButton startButton = new DarkMenuButton("START GAME", 16, 337, 530, 50, () -> {
            // sets new scene when clicked
            mgr.setScene(new TypeOfWarScene(mgr)); // TODO #6: Make this go to server on click (not game)
        });

        // create background modal
        addEntity(new LabeledModalEntity(359, 128, 562, 464, "HOST NEW GAME", mgr, nameInput, sentenceInput, buttons[0],
                                         buttons[1], buttons[2], buttons[3], buttons[4], ButtonsLabel, startButton));
    }

    @Override
    protected void render(GraphicsContext g) {
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all entities
        super.render(g);
    }

    public float getStartingMultiplier() {
        return startingMultiplier;
    }

    public String getRoomName() {
        return nameInput.getInput();
    }

    public String getCustomSentence() {
        return sentenceInput.getInput();
    }
}
