package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.EndHeaderEntity;
import com.logandhillon.typeofwar.entity.EndResultEntity;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;
import static com.logandhillon.typeofwar.resource.Colors.BG_LOSING;
import static com.logandhillon.typeofwar.resource.Colors.BG_WINNING;

/**
 * The game scene for {@link EndResultEntity}s and {@link EndHeaderEntity}s that show at the end of the game by communicating with parent class {@link GameScene}.
 *
 * @author Jack Ross
 *
 *
 */
public class EndGameScene extends GameScene {
    private static final float ENTITY_GAP = 136;
    private static final float BUFFER_GAP = 25;
    private boolean win;

    /**
     *
     * @param leftTeamResults is the sum of statistics for the team on the left of the rope
     * @param rightTeamResults is the sum of statistics for the team on the right of the rope
     */
    public EndGameScene(TypeOfWar mgr, List<EndResultEntity> leftTeamResults, List<EndResultEntity> rightTeamResults, EndHeaderEntity header){

        this.win = header.getResult();
        addEntity(header);

        float dx = 0f;
        for(EndResultEntity p : leftTeamResults) {
            // displace entities on left away from center, displacement increases per teammate
            p.setPosition((TypeOfWar.WINDOW_WIDTH.floatValue() / 2) - (leftTeamResults.size() * ENTITY_GAP) + dx - BUFFER_GAP, 170);
            dx += ENTITY_GAP;
            addEntity(p);
        }

        dx = 0f;
        for(EndResultEntity p : rightTeamResults) {
            // displace entities on right from center
            p.setPosition((TypeOfWar.WINDOW_WIDTH.floatValue() / 2) + dx + BUFFER_GAP , 170);
            dx += ENTITY_GAP;
            addEntity(p);
        }



    }
    @Override
    protected void render(GraphicsContext g) {

        // fill background
        g.setFill(this.win ? BG_WINNING : BG_LOSING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());

        // render all end screen entities
        super.render(g);
    }


}
