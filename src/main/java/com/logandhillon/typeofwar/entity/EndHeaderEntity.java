package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Header entity that is displayed at the top of the screen during {@link com.logandhillon.typeofwar.game.EndGameScene}
 *
 * @author Jack Ross
 * @see  EndResultEntity
 */
public class EndHeaderEntity extends Entity{
    private static final Font FONT_HEADER = Font.font(Fonts.DM_MONO_MEDIUM, 64);
    private boolean win;
    private String text;
    private final float midScreen = TypeOfWar.CANVAS_WIDTH / 2;

    /**
     *
     * @param win changes display if player's team won or lost game
     */
    public EndHeaderEntity(boolean win) {
        super(0, 0);
        this.win = win;
    }

    @Override
    public void onUpdate(float dt) {

    }

    /**
     *
     * @param g the graphical context to render to.
     * @param x the x position to render the entity at
     * @param y the y position to render the entity at
     *
     */
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {

        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(FONT_HEADER);

        // render divider
        g.setStroke(Color.GREY);
        g.setLineWidth(1d);
        g.strokeLine(midScreen, 198, midScreen, 565);

        if(win) {
            // set win settings
            text = "VICTORY!";
            g.setFill(Colors.GOLD_GRADIENT);
            g.setStroke(Colors.GOLD_GRADIENT);
        } else {
            // set lose settings
            text = "DEFEAT";
            g.setFill(Color.RED);
            g.setStroke(Color.RED);
        }

        // render text
        g.setTextBaseline(VPos.TOP);
        g.fillText(text, midScreen, 80);

        // render line under header
        g.setLineWidth(2d);
        g.strokeLine(midScreen - 200, 165, midScreen + 200, 165);
    }

    @Override
    public void onDestroy() {

    }

    public boolean getResult(){
        // returns result of game
        return this.win;
    }
}
