package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class EndHeaderEntity extends Entity{
    private static final Font FONT_HEADER = Font.font(Fonts.DM_MONO_MEDIUM, 64);
    private boolean win;
    private String text;
    private final float midScreen = TypeOfWar.WINDOW_WIDTH.floatValue() / 2 - (float)FONT_HEADER.getSize() / 2;

    public EndHeaderEntity(boolean win) {
        super(0, 0);
        this.win = win;
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {

        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(FONT_HEADER);
        if(win) {
            text = "VICTORY!";
            g.setFill(Colors.GOLD_GRADIENT);
            g.setStroke(Colors.GOLD_GRADIENT);
        } else {
            text = "DEFEAT";
            g.setFill(Color.RED);
            g.setStroke(Color.RED);
        }

        g.fillText(text, midScreen, 150);
        g.setLineWidth(2d);
        g.strokeLine(midScreen - FONT_HEADER.getSize() * (text.length() / 2f), 165, midScreen + FONT_HEADER.getSize() * (text.length() / 2f), 165);


    }

    @Override
    public void onDestroy() {

    }

    public boolean getResult(){
        return this.win;
    }
}
