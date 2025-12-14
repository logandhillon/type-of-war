package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.DarkMenuButton;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.entity.ui.NumberBoxEntity;
import com.logandhillon.typeofwar.resource.Colors;
import javafx.scene.canvas.GraphicsContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;

import static com.logandhillon.typeofwar.TypeOfWar.CANVAS_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.CANVAS_WIDTH;

public class PracticeSettingScene extends UIScene {
    private static final Logger          LOG = LoggerContext.getContext().getLogger(PracticeSettingScene.class);

    private final        NumberBoxEntity wpmInput;

    PracticeSettingScene(TypeOfWar mgr) {

        wpmInput = new NumberBoxEntity(16, 47, 530, "80", "COMPUTER WPM", 3);

        DarkMenuButton startPracticeButton = new DarkMenuButton("START PRACTICE", 16, 129, 530, 48, () -> {
            try {
                mgr.setScene(new TypeOfWarPracticeScene(mgr, getWPM()));
            } catch (IOException e) {
                LOG.fatal("Failed to instantiate TypeOfWarPracticeScene", e);
            }
        });

        LabeledModalEntity practiceModal = new LabeledModalEntity(
                359, 232, 562, 256, "PRACTICE", mgr, wpmInput, startPracticeButton);
        addEntity(practiceModal);
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }

    public int getWPM() {
        return wpmInput.getInput().isBlank() ? 80 : Integer.parseInt(wpmInput.getInput());
    }
}
