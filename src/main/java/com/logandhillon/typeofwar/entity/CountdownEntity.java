package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author Logan Dhillon
 */
public class CountdownEntity extends Entity {
    private static final Font     FONT = Font.font(Fonts.DM_MONO_MEDIUM, 144);
    private final        Runnable onTimerEnd;

    private int     currentNumber;
    private float   timer;
    private boolean closed; // closed means the countdown will no longer do anything

    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public CountdownEntity(float x, float y, Runnable onTimerEnd) {
        super(x, y);
        this.onTimerEnd = onTimerEnd;
        currentNumber = 3;
        timer = 0;
    }

    @Override
    public void onUpdate(float dt) {
        if (closed) return;
        timer += dt;

        if (timer >= 1f) {
            currentNumber--;
            timer = 0;
        }

        if (currentNumber < 0) {
            onTimerEnd.run();
            closed = true;
        }
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        if (closed) return;

        g.setFont(FONT);
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.CENTER);

        float delta = 1 - timer;

        g.save();
        g.translate(x, y);
        g.scale(delta, delta);
        g.setFill(Color.hsb(0, 0, 1, delta));
        g.fillText(String.valueOf(currentNumber), 0, 0);
        g.restore();
    }

    @Override
    public void onDestroy() {

    }
}
