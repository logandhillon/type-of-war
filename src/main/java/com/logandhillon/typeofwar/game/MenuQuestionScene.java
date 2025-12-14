package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.DarkMenuButton;
import com.logandhillon.typeofwar.entity.ui.ModalEntity;
import com.logandhillon.typeofwar.entity.ui.TextEntity;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static com.logandhillon.typeofwar.TypeOfWar.CANVAS_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.CANVAS_WIDTH;

/**
 * This scene simply shows a modal with a message, and two buttons that each do different things; hence a question.
 *
 * @author Logan Dhillon
 */
public class MenuQuestionScene extends UIScene {
    private static final Font TITLE_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 20);
    private static final Font BODY_FONT  = Font.font(Fonts.DM_MONO, 16);

    public MenuQuestionScene(TypeOfWar game, String title, String msg,
                             String opt1Label, Runnable opt1, String opt2Label, Runnable opt2) {
        addEntity(
                new ModalEntity(
                        375, 255, 530, 212,
                        new TextEntity(title.toUpperCase(), TITLE_FONT, TextAlignment.CENTER, VPos.TOP, 265, 16),
                        new TextEntity(msg.toUpperCase(), BODY_FONT, TextAlignment.CENTER, VPos.TOP, 265, 74),
                        new DarkMenuButton(opt1Label, 16, 151, 241, 48, opt1),
                        new DarkMenuButton(opt2Label, 273, 151, 241, 48, opt2)));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }
}
