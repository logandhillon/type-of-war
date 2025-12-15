package com.logandhillon.typeofwar.game;

import com.google.protobuf.UInt32Value;
import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.MenuController;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.ui.*;
import com.logandhillon.typeofwar.networking.proto.ConfigProto;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static com.logandhillon.typeofwar.TypeOfWar.CANVAS_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.CANVAS_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    private final InputBoxEntity userInput;
    private final SkinOptionsEntity[] skins;
    private final int defaultColor;

    private static final int[][] SKIN_OPTION_POSITIONS = new int[][]{
            { 638, 320, 730, 328, 806, 328, 882, 328 },
            { 638, 328, 714, 320, 806, 328, 882, 328 },
            { 638, 328, 714, 328, 790, 320, 882, 328 },
            { 638, 328, 714, 328, 790, 328, 866, 320 }, };

    /**
     * Creates a new main menu
     *
     * @param game the main class that can switch scenes, manage connections, etc.
     */
    public MainMenuScene(TypeOfWar game) {
        this.defaultColor = TypeOfWar.getUserConfig().getColorIdx().getValue();
        float x = (CANVAS_WIDTH - 652) / 2f;
        int y = 176;
        int dy = 48 + 16; // ∆y per button height

        game.setInMenu(true);

        MenuController controller = new MenuController(
                new MenuButton("Practice", x, y, 256, 48, () -> game.setScene(new PracticeSettingScene(game))),
                new MenuButton("Host Game", x, y + dy, 256, 48, () -> game.setScene(new HostGameScene(game))),
                new MenuButton("Join Game", x, y + 2 * dy, 256, 48, game::showJoinGameMenu),
                new MenuButton("Settings", x, y + 3 * dy, 256, 48, () -> {}),
                new MenuButton("Credits", x, y + 4 * dy, 256, 48, () -> {}),
                new MenuButton("Quit", x, y + 5 * dy, 256, 48, () -> System.exit(0))
        );

        userInput = new InputBoxEntity(16, 47, 316, "YOUR NAME", "YOUR NAME", 20);
        userInput.setInput(TypeOfWar.getUserConfig().getName());
        userInput.setOnBlur(() -> TypeOfWar.updateUserConfig(
                ConfigProto.UserConfig.newBuilder().setName(userInput.getInput()).buildPartial()));

        TextEntity skinLabel = new TextEntity("CHOOSE SKIN", Font.font(Fonts.DM_MONO_MEDIUM, 18),
                                              Color.WHITE, TextAlignment.LEFT, VPos.TOP, 16, 113);

        skins = new SkinOptionsEntity[4];

        for (int i = 0; i < skins.length; i++) {
            int idx = i; // final for use in lambda
            // create at 0,0, since they are updates once the default color is selected.
            skins[i] = new SkinOptionsEntity(0, 0, Colors.PLAYER_SKINS.get(i), () -> handleSkinClick(idx));
        }

        addEntity(new ModalEntity(618, y, 348, 368, userInput, skinLabel, skins[0], skins[1], skins[2], skins[3]));
        addEntity(controller); // add the controller AFTER so input in the player configurator has priority
        skins[this.defaultColor].onPress(); // select the default color
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }

    public String getPlayerName() {
        return userInput.getInput();
    }

    public Color getSkin() {
        for (SkinOptionsEntity s: skins) {
            if (s.isClicked()) {
                return s.getColor();
            }
        }
        return skins[defaultColor].getColor();
    }

    /**
     * Handles the click event of the {@link SkinOptionsEntity} in the main menu player configuration modal.
     *
     * @param clickedSkin the index of the skin that was clicked.
     */
    private void handleSkinClick(int clickedSkin) {
        if (skins[clickedSkin].isClicked()) return;

        for (int i = 0; i < skins.length; i++) {
            skins[i].setSize(i == clickedSkin);
            skins[i].setPosition(
                    SKIN_OPTION_POSITIONS[clickedSkin][(i * 2)],
                    SKIN_OPTION_POSITIONS[clickedSkin][(i * 2) + 1]);
            skins[i].setClicked(i == clickedSkin);
        }

        // save the new color to the disk
        TypeOfWar.updateUserConfig(ConfigProto.UserConfig.newBuilder().setColorIdx(UInt32Value.of(clickedSkin)).buildPartial());
    }
}