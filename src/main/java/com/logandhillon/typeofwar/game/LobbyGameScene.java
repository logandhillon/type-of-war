package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.engine.GameSceneManager;
import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.DarkMenuButton;
import com.logandhillon.typeofwar.entity.ui.LabeledModalEntity;
import com.logandhillon.typeofwar.entity.ui.LobbyPlayerEntity;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

public class LobbyGameScene extends UIScene {


    private static final Font LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 18);
    private static final float ENTITY_GAP = 48;

    public LobbyGameScene(GameSceneManager mgr, String roomName, boolean isHosting, LobbyPlayerEntity[] team1, LobbyPlayerEntity[] team2){

        PlayerContainer leftContainer = new PlayerContainer(16, 47, 257, 206);
        PlayerContainer rightContainer = new PlayerContainer(289, 47, 257, 206);
        ContainerLabel leftLabel = new ContainerLabel(16, 16, 1);
        ContainerLabel rightLabel = new ContainerLabel(289, 16, 2);



        DarkMenuButton startButton = new DarkMenuButton(isHosting? "START GAME" : "WAITING FOR HOST TO START...", 16, 269, 530, 48, ()-> {
                //TODO #6: Start the game for everyone
            });
        if(!isHosting){
            startButton.setActive(false, true);
        }

        LabeledModalEntity lobbyModal = new LabeledModalEntity(359, 162, 562, 396, roomName, mgr, leftContainer, rightContainer, leftLabel, rightLabel, startButton);

        addEntity(lobbyModal);

        float dy = 0;

        for(LobbyPlayerEntity p : team1){
            p.setPosition(32, p.getY() + dy + 128);
            dy += ENTITY_GAP;
            lobbyModal.addEntity(p);
        }

        dy = 0;
        for(LobbyPlayerEntity p : team2){
            p.setPosition(305, p.getY() + dy + 128);
            dy += ENTITY_GAP;
            lobbyModal.addEntity(p);
        }

    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.BG_WINNING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all other entities
        super.render(g);
    }

    private static final class PlayerContainer extends Entity {

        private final float w;
        private final float h;
        /**
         * Creates an entity at the specified position.
         *
         * @param x x-position (from left)
         * @param y y-position (from top)
         */
        public PlayerContainer(float x, float y, float w, float h) {
            super(x, y);
            this.w = w;
            this.h = h;
        }

        @Override
        protected void onRender(GraphicsContext g, float x, float y) {
            // render container
            g.setFill(Colors.DEFAULT_DARKER);
            g.fillRoundRect(x, y, w, h, 8, 8);
        }

        @Override
        public void onUpdate(float dt) {

        }

        @Override
        public void onDestroy() {

        }
    }
    private static final class ContainerLabel extends Entity {

        private final int teamNumber;

        /**
         * Creates an entity at the specified position.
         *
         * @param x x-position (from left)
         * @param y y-position (from top)
         */
        public ContainerLabel(float x, float y, int teamNumber) {
            super(x, y);
            this.teamNumber = teamNumber;
        }

        @Override
        protected void onRender(GraphicsContext g, float x, float y) {
            // set initial text variables
            g.setTextAlign(TextAlignment.LEFT);
            g.setTextBaseline(VPos.TOP);
            g.setFont(LABEL_FONT);
            g.setFill(Color.WHITE);

            // render label
            g.fillText("TEAM " + this.teamNumber, x, y);
        }

        @Override
        public void onUpdate(float dt) {

        }

        @Override
        public void onDestroy() {

        }
    }
}

