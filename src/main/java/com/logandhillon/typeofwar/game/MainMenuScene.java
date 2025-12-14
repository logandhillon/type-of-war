package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.MenuController;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.*;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.atomic.AtomicInteger;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    /**
     * Creates a new main menu
     *
     * @param game the main class that can switch scenes, manage connections, etc.
     */
    public MainMenuScene(TypeOfWar game) {
        float x = 314;
        int y = 176;
        int dy = 48 + 16; // ∆y per button height

        game.setInMenu(true);

        MenuController controller = new MenuController(
                new MenuButton("Practice", x, y, 256, 48, ()-> game.setScene(new PracticeSettingScene(game))),
                new MenuButton("Host Game", x, y + dy, 256, 48, () -> game.setScene(new HostGameScene(game))),
                new MenuButton("Join Game", x, y + 2 * dy, 256, 48, game::showJoinGameMenu),
                new MenuButton("Settings", x, y + 3 * dy, 256, 48, () -> {}),
                new MenuButton("Credits", x, y + 4 * dy, 256, 48, () -> {}),
                new MenuButton("Quit", x, y + 5 * dy, 256, 48, () -> System.exit(0))
        );
        addEntity(controller);

        InputBoxEntity userInput = new InputBoxEntity(16, 47, 316, "YOUR NAME", "YOUR NAME", 20);

        TextEntity skinLabel = new TextEntity("CHOOSE SKIN", Font.font(Fonts.DM_MONO_MEDIUM, 18), Color.WHITE, TextAlignment.LEFT, VPos.TOP, 16, 113);

        SkinOptionsEntity[] skins = new SkinOptionsEntity[4];

        int[] firstSelected = {638, 320, 730, 328, 806, 328, 882, 328};
        int[] secondSelected = {638, 328, 714, 320, 806, 328, 882, 328};
        int[] thirdSelected = {638, 328, 714, 328, 790, 320, 882, 328};
        int[] fourthSelected = {638, 328, 714, 328, 790, 328, 866, 320};

        skins[0] = new SkinOptionsEntity(20, 152, 64, 64, Color.YELLOW, false, ()-> {
            if(!skins[0].isClicked()) {
                skins[0].setPosition(firstSelected[0], firstSelected[1]);
                skins[0].setSize(80, 80);
                for (int i = 0; i < skins.length; i++) {
                    if (i == 0) {
                        continue;
                    }
                    skins[i].setSize(64, 64);
                    skins[i].setPosition(firstSelected[(i * 2)], firstSelected[(i * 2) + 1]);
                    skins[i].setClicked(false);
                }
                skins[0].setClicked(true);
            }
        });

        skins[1] = new SkinOptionsEntity(96, 152, 64, 64, Color.BLUE, false, ()-> {
            if(!skins[1].isClicked()) {
                skins[1].setPosition(secondSelected[2], secondSelected[3]);
                skins[1].setSize(80, 80);
                for (int i = 0; i < skins.length; i++) {
                    if (i == 1) {
                        continue;
                    }
                    skins[i].setSize(64, 64);
                    skins[i].setPosition(secondSelected[(i * 2)], secondSelected[(i * 2) + 1]);
                    skins[i].setClicked(false);
                }
                skins[1].setClicked(true);
            }
        });

        skins[2] = new SkinOptionsEntity(172, 152, 64, 64, Color.RED, false, ()-> {
            if(!skins[2].isClicked()) {
                skins[2].setPosition(thirdSelected[4], thirdSelected[5]);
                skins[2].setSize(80, 80);
                for (int i = 0; i < skins.length; i++) {
                    if (i == 2) {
                        continue;
                    }
                    skins[i].setSize(64, 64);
                    skins[i].setPosition(thirdSelected[(i * 2)], thirdSelected[(i * 2) + 1]);
                    skins[i].setClicked(false);
                }
                skins[2].setClicked(true);
            }
        });

        skins[3] = new SkinOptionsEntity(248, 152, 64, 64, Color.AQUA, false, ()-> {
            if(!skins[3].isClicked()) {
                skins[3].setPosition(fourthSelected[6], fourthSelected[7]);
                skins[3].setSize(80, 80);
                for (int i = 0; i < skins.length; i++) {
                    if (i == 3) {
                        continue;
                    }
                    skins[i].setSize(64, 64);
                    skins[i].setPosition(fourthSelected[(i * 2)], fourthSelected[(i * 2) + 1]);
                    skins[i].setClicked(false);
                }
                skins[3].setClicked(true);
            }
        });
        addEntity(new ModalEntity(618, y, 348, 368, userInput, skinLabel, skins[0], skins[1], skins[2], skins[3]));
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
