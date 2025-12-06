package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class EndHeaderEntity extends Entity{
    private static final Font FONT_HEADER = Font.font(Fonts.DM_MONO_MEDIUM, 64);
    private boolean win;

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
        g.setFill(win ? Colors.GOLD_GRADIENT : Color.RED);
        g.fillText(win ? "VICTORY!" : "Defeat.", (TypeOfWar.WINDOW_WIDTH.floatValue() / 2) - (FONT_HEADER.getSize() / 2), 150);
    }

    @Override
    public void onDestroy() {

    }

    public boolean getResult(){
        return this.win;
    }
}
