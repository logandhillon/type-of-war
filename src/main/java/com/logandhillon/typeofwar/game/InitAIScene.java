package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.DarkMenuButton;
import com.logandhillon.typeofwar.entity.ui.InputBoxEntity;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.events.MouseEvent;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

public class InitAIScene extends UIScene {
    private final int setWPM;
    private InputBoxEntity wpmInput;

    InitAIScene(TypeOfWar mgr){
        InputBoxEntity wpmInput = new InputBoxEntity(16, 47, 530, "80", "COMPUTER WPM", 3);
        setWPM = Integer.parseInt(wpmInput.getInput());

        DarkMenuButton startPracticeButton = new DarkMenuButton("START PRACTICE", 16, 129, 530, 48, ()-> {
           mgr.setScene(new TypeOfWarScene());
        });

        LabeledModalEntity practiceModal = new LabeledModalEntity(359, 232, 562, 256, "PRACTICE", mgr, wpmInput, startPracticeButton);
        addEntity(practiceModal);

        this.addHandler(this::onKeyPressed); //FIXME: make this work
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }

    private void onKeyPressed(KeyEvent e) {

    }
    public int getWPM() {return wpmInput.getInput().isBlank() ? 80 : setWPM;}
}
