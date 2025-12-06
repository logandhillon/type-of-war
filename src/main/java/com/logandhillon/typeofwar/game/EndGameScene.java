package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.EndResultEntity;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;
import static com.logandhillon.typeofwar.resource.Colors.BG_LOSING;
import static com.logandhillon.typeofwar.resource.Colors.BG_WINNING;

/**
 * The game scene for {@link EndResultEntity} that show at the end of the game by communicating with parent class {@link GameScene}.
 *
 * @author Jack Ross
 *
 *
 */
public class EndGameScene extends GameScene {
    private static final float ENTITY_GAP = 136;
    private boolean isWinning = true;

    /**
     *
     * @param leftTeamResults is the sum of statistics for the team on the left of the rope
     * @param rightTeamResults is the sum of statistics for the team on the right of the rope
     */
    public EndGameScene(EndResultEntity[] leftTeamResults, EndResultEntity[] rightTeamResults){

        float dx = 0f;
        for(EndResultEntity p : leftTeamResults) {
            // displace entities on left from center (increasing with more teammates per side)
            p.setPosition((TypeOfWar.WINDOW_WIDTH.floatValue() / 2) - (leftTeamResults.length * ENTITY_GAP) + dx , 170);
            dx += ENTITY_GAP;
            addEntity(p);
        }

        dx = 0f;
        for(EndResultEntity p : rightTeamResults) {
            // displace entities on right from center
            p.setPosition((TypeOfWar.WINDOW_WIDTH.floatValue() / 2) + dx , 170);
            dx += ENTITY_GAP;
            addEntity(p);
        }


    }
    @Override
    protected void render(GraphicsContext g) {

        // fill background
        g.setFill(isWinning ? BG_WINNING : BG_LOSING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all end screen entities
        super.render(g);
    }


}
