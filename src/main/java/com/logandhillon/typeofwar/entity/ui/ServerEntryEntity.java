package com.logandhillon.typeofwar.entity.ui;

import com.logandhillon.typeofwar.game.JoinGameScene;
import com.logandhillon.typeofwar.resource.Colors;
import com.logandhillon.typeofwar.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ServerEntryEntity extends DynamicButtonEntity{
    private static final Font ADDRESS_FONT = Font.font(Fonts.DM_MONO, 16);
    private static final Font LATENCY_FONT = Font.font(Fonts.DM_MONO, 14);
    private static final ButtonEntity.Style DEFAULT_STYLE = new ButtonEntity.Style(
            Color.WHITE, Colors.DEFAULT, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.DM_MONO_MEDIUM, 16));
    private static final ButtonEntity.Style ACTIVE_STYLE  = new ButtonEntity.Style(
            Color.WHITE, Colors.PRIMARY, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.DM_MONO_MEDIUM, 16));
    private static final double ROUNDING_RADIUS = 4;

    private final Runnable clickHandler;

    private String serverName;
    private String serverAddress;
    private int ping;

    /**
     * Creates a new server entry entity
     *
     * @param serverName   the host's server's name
     * @param serverAddress the server's IP address
     * @param ping         the user's latency connecting to the server
     * @param x
     * @param y
     * @param w            width
     * @param h            height
     * @param onClick      the action that should happen when this button is clicked by the mouse
     */

    public ServerEntryEntity(float x, float y, float w, float h, String serverName,
                             String serverAddress, int ping, Runnable onClick) {
        super(serverName, x, y, w, h, e -> onClick.run(), DEFAULT_STYLE, ACTIVE_STYLE);
        this.clickHandler = onClick;
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.ping = ping;
    }


    /**
     * Renders the button background, then the button text on top of it, based on what was supplied when the button was
     * created.
     *
     * @param g the graphical context to render to.
     * @param x the x position to render the entity at
     * @param y the y position to render the entity at
     */
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {

        // render button background
        g.setFill(style.buttonColor());
        g.fillRoundRect(x, y, w, h, ROUNDING_RADIUS, ROUNDING_RADIUS);

        // render name
        g.setFill(style.labelColor());
        g.setFont(style.font());
        g.setTextBaseline(VPos.CENTER);
        g.setTextAlign(TextAlignment.LEFT);
        g.fillText(this.serverName, x + 12, y + h / 2);

        // render server address
        g.setFill(Color.GREY);
        g.setFont(ADDRESS_FONT);
        g.fillText(this.serverAddress, x + 216, y + h / 2);

        // render server address
        g.setTextAlign(TextAlignment.RIGHT);
        g.setFont(LATENCY_FONT);
        g.fillText(this.ping + " ms", x + w - 12 , y + h / 2);
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public void setData(JoinGameScene.ServerEntry data) {
        this.serverName = data.name();
        this.serverAddress = data.address();
        this.ping = data.ping();
    }
}
