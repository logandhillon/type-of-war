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

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * This scene simply shows a modal with a message, and a button to return to the main menu.
 *
 * @author Logan Dhillon
 */
public class MenuAlertScene extends UIScene {
    private static final Font TITLE_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 20);
    private static final Font BODY_FONT  = Font.font(Fonts.DM_MONO, 16);

    public MenuAlertScene(String title, String msg, TypeOfWar game) {
        addEntity(new ModalEntity(
                375, 255, 530, 212,
                new TextEntity(title.toUpperCase(), TITLE_FONT, TextAlignment.CENTER, VPos.TOP, 265, 16),
                new TextEntity(msg.toUpperCase(), BODY_FONT, TextAlignment.CENTER, VPos.TOP, 265, 74),
                new DarkMenuButton("OK", 16, 151, 498, 48, game::goToMainMenu)));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }
}
