package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.engine.GameScene;
import com.logandhillon.typeofwar.entity.EndResultEntity;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_HEIGHT;
import static com.logandhillon.typeofwar.TypeOfWar.WINDOW_WIDTH;
import static com.logandhillon.typeofwar.resource.Colors.BG_LOSING;
import static com.logandhillon.typeofwar.resource.Colors.BG_WINNING;

public class EndGameScene extends GameScene {
    private static final float ENTITY_GAP = 136;
    private boolean isWinning = true;

    public EndGameScene(EndResultEntity[] leftTeamResults, EndResultEntity[] rightTeamResults){
        float dx = 0f;
        for(EndResultEntity p : leftTeamResults) {
            p.setPosition((TypeOfWar.WINDOW_WIDTH.floatValue() / 2) - (leftTeamResults.length * ENTITY_GAP) + dx , 170);
            dx += ENTITY_GAP;
            addEntity(p);
        }
        dx = 0f;
        for(EndResultEntity p : rightTeamResults) {
            p.setPosition((TypeOfWar.WINDOW_WIDTH.floatValue() / 2) + dx , 170);
            dx += ENTITY_GAP;
            addEntity(p);
        }


    }
    @Override
    protected void render(GraphicsContext g) {
        g.setFill(isWinning ? BG_WINNING : BG_LOSING);
        g.fillRect(0, 0, WINDOW_WIDTH.doubleValue(), WINDOW_HEIGHT.doubleValue());
        super.render(g);
    }


}
