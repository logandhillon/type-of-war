package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The lobby game menu shows all users in a lobby and allots the host with special permissions to start the
 * {@link TypeOfWarScene}
 *
 * @author Jack Ross
 * @see LobbyPlayerEntity
 */
public class LobbyGameScene extends UIScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(LobbyGameScene.class);
    private static final Font   LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 18);
    private static final float  ENTITY_GAP = 48;

    private final LabeledModalEntity lobbyModal;
    private final String             roomName;

    private float dyTeam1;
    private float dyTeam2;

    /**
     * @param mgr       the game manager responsible for switching active scenes.
     * @param roomName  the name of the lobby stated in {@link HostGameScene}
     * @param isHosting determines if the user is the host of the given lobby or not
     */
    public LobbyGameScene(TypeOfWar mgr, String roomName, boolean isHosting) {
        this.roomName = roomName;

        // containers for each team
        PlayerContainer leftContainer = new PlayerContainer(16, 47, 257, 206);
        PlayerContainer rightContainer = new PlayerContainer(289, 47, 257, 206);

        // labels for each container
        ContainerLabel leftLabel = new ContainerLabel(16, 16, 1);
        ContainerLabel rightLabel = new ContainerLabel(289, 16, 2);

        // shows different buttons at bottom depending on if the user is hosting
        DarkMenuButton startButton = new DarkMenuButton(isHosting ? "START GAME" : "WAITING FOR HOST TO START...",
                                                        16, 269, 530, 48, () -> {
            if (isHosting) mgr.startGame(null, 0);
            // don't do anything if not hosting (button is disabled)
        });

        if (!isHosting) {
            startButton.setActive(false, true);
        }

        lobbyModal = new LabeledModalEntity(
                359, 162, 562, 396, roomName, mgr,
                leftContainer, rightContainer, leftLabel, rightLabel, startButton);

        addEntity(lobbyModal);
    }

    /**
     * Adds a player to the list of players on the corresponding team.
     *
     * @param name  player name
     * @param color player skin's color
     * @param team  player team (1 or 2)
     */
    public void addPlayer(String name, Color color, int team) {
        LOG.info("Adding player \"{}\" to team {}", name, team);

        if (team == 1) {
            var p = new LobbyPlayerEntity(color, name, 0); // TODO: impl. ping
            p.setPosition(32, p.getY() + dyTeam1 + 128);
            dyTeam1 += ENTITY_GAP;
            lobbyModal.addEntity(p);
            return;
        }

        if (team == 2) {
            var p = new LobbyPlayerEntity(color, name, 0); // TODO: impl. ping
            p.setPosition(305, p.getY() + dyTeam2 + 128);
            dyTeam2 += ENTITY_GAP;
            lobbyModal.addEntity(p);
            return;
        }

        // if neither of the if-branches were handled, throw error
        throw new IllegalArgumentException("Team must be either 1 or 2!");
    }

    public void clearPlayers() {
        LOG.info("Clearing player list");
        clearEntities(true, LobbyPlayerEntity.class::isInstance);
        dyTeam1 = 0;
        dyTeam2 = 0;
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

    public String getRoomName() {
        return roomName;
    }
}

