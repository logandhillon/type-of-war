package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;

/**
 * The rope is the part of the main game loop that visualizes who's winning, the players, etc.
 *
 * @author Logan Dhillon
 * @see com.logandhillon.typeofwar.game.TypeOfWarScene
 */
public class RopeEntity extends Entity {
    private static final int THICKNESS      = 3;
    private static final int WIDTH          = WINDOW_WIDTH.intValue() - 128;
    private static final int PLAYER_MARGIN  = 16;
    private static final int DIVIDER_HEIGHT = 144;

    private final ArrayList<PlayerObject> leftTeam;
    private final ArrayList<PlayerObject> rightTeam;

    public RopeEntity(float x, float y) {
        super(x, y);
        leftTeam = new ArrayList<>();
        rightTeam = new ArrayList<>();
    }

    @Override
    public void onUpdate(float dt) {
    }

    @Override
    public void onRender(GraphicsContext g, float x, float y) {
        // middle (cross to win) line
        g.setStroke(Color.hsb(0, 0, 0.5, 0.5));
        g.setLineWidth(2);
        g.setLineDashes(8);
        g.strokeLine(
                WINDOW_WIDTH.floatValue() / 2f,
                (y - DIVIDER_HEIGHT) / 2f,
                WINDOW_WIDTH.floatValue() / 2f,
                (y + DIVIDER_HEIGHT) / 2f
        );

        // main rope
        g.setStroke(Color.WHITE);
        g.setLineWidth(THICKNESS);
        g.setLineDashes(null);
        g.strokeLine(
                x,
                (y + THICKNESS) / 2f,
                x + WIDTH,
                (y + THICKNESS) / 2f
        );

        // left team
        float dx = 0;
        for (PlayerObject player: leftTeam) {
            player.onRender(g, x + dx, (y + THICKNESS) / 2);
            dx += PlayerObject.SIZE + PLAYER_MARGIN;
        }

        // right team
        dx = 0;
        for (PlayerObject player: rightTeam) {
            player.onRender(g, WIDTH - dx, (y + THICKNESS) / 2);
            dx += PlayerObject.SIZE + PLAYER_MARGIN;
        }
    }

    /**
     * Adds a new player that will be controlled and rendered by this rope.
     *
     * @param player the player to add.
     * @param team   the team that this player is a part of (left or right). ideally, the player on this client is
     *               always on the LEFT side.
     *
     * @see com.logandhillon.typeofwar.engine.GameScene
     */
    public void addPlayer(PlayerObject player, Team team) {
        if (team == Team.LEFT)
            leftTeam.add(player);
        else if (team == Team.RIGHT)
            rightTeam.add(player);
    }

    @Override
    public void onDestroy() {
        leftTeam.clear();
        rightTeam.clear();
    }

    public enum Team {
        LEFT, RIGHT
    }
}
