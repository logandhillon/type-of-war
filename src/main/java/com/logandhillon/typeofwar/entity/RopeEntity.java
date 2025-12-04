package com.logandhillon.typeofwar.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.logandhillon.typeofwar.engine.GameScene.WINDOW_WIDTH;

/**
 * The rope is the part of the main game loop that visualizes who's winning, the players, etc.
 *
 * @author Logan Dhillon
 * @see com.logandhillon.typeofwar.game.TypeOfWarScene
 */
public class RopeEntity extends Entity {
    private static final int HEIGHT        = 4;
    private static final int WIDTH         = WINDOW_WIDTH - 128;
    private static final int PLAYER_MARGIN = 16;

    private final ArrayList<PlayerObject> leftTeam;
    private final ArrayList<PlayerObject> rightTeam;

    public RopeEntity(double x, double y) {
        super(x, y);
        leftTeam = new ArrayList<>();
        rightTeam = new ArrayList<>();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onRender(GraphicsContext g, double x, double y) {
        g.setFill(Color.WHITE);
        g.fillRect(x, (y + HEIGHT) / 2, WIDTH, HEIGHT);

        // render the left team
        float dx = 0;
        for (PlayerObject player: leftTeam) {
            player.onRender(g, x + dx, (y + HEIGHT) / 2);
            dx += PlayerObject.SIZE + PLAYER_MARGIN;
        }

        // render the right team
        dx = 0;
        for (PlayerObject player: rightTeam) {
            player.onRender(g, WIDTH - dx, (y + HEIGHT) / 2);
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
