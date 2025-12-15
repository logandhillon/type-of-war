package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.engine.UIScene;
import com.logandhillon.typeofwar.entity.core.BoundEntity;
import com.logandhillon.typeofwar.scene.menu.LobbyGameScene;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * An entity used in {@link LobbyGameScene} to show a graphical representation of a
 * user in the lobby's name, colour, and latency
 *
 * @author Jack Ross
 */
public class LobbyPlayerEntity extends BoundEntity<UIScene> {
    private static final Font LABEL_FONT = Font.font(Fonts.DM_MONO_MEDIUM, 16);
    private final Color color;
    private final String playerName;
    private final int ping;
    private static final int WIDTH = 225;

    /**
     *
     * @param color the color of the given player's skin
     * @param playerName the given player's chosen name
     * @param ping the given player's latency
     */
    public LobbyPlayerEntity(Color color, String playerName, int ping){
        super(0, 0);
        this.color = color;
        this.playerName = playerName;
        this.ping = ping;
    }
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFont(LABEL_FONT);
        g.setFill(color);

        // render player skin
        g.fillRect(x, y, 32, 32);

        g.setTextAlign(TextAlignment.LEFT);
        g.setTextBaseline(VPos.CENTER);
        g.setFill(Color.WHITE);

        // render player name
        g.fillText(this.playerName, x + 48, y + 16);

        g.setTextAlign(TextAlignment.RIGHT);
        g.setFill(Color.GREY);

        // render player latency
        g.fillText(this.ping + "ms", x + WIDTH, y + 16);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }
}
