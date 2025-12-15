package com.logandhillon.typeofwar.entity;

import com.logandhillon.typeofwar.entity.core.Entity;
import com.logandhillon.typeofwar.entity.core.GameObject;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * The player object that is to be rendered on the {@link RopeEntity}. This entity cannot be rendered directly to a
 * {@link com.logandhillon.typeofwar.engine.GameScene}, and must be managed by a parent {@link Entity}.
 *
 * @author Logan Dhillon
 */
public class PlayerObject implements GameObject {
    protected static final int SIZE          = 64;
    private static final   int HALF_SIZE     = SIZE / 2;
    private static final   int NAME_OFFSET_Y = 24;

    private static final Font FONT = Font.font(Fonts.DM_MONO, 17);

    private final String name;
    private final Color  color;

    /**
     * Creates a new player with the specified color.
     *
     * @param name  the name of the player
     * @param color the color of the player
     *
     * @apiNote the PlayerEntity should be controlled by a {@link RopeEntity}
     * @see RopeEntity
     */
    public PlayerObject(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void render(GraphicsContext g, float x, float y) {
        // render skin
        g.setFill(color);
        g.fillRect(x, y - HALF_SIZE, SIZE, SIZE);

        // render name
        g.setFill(Color.WHITE);
        g.setFont(FONT);
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(name, x + HALF_SIZE, y - HALF_SIZE - NAME_OFFSET_Y);
    }

    @Override
    public void onDestroy() {

    }
}
