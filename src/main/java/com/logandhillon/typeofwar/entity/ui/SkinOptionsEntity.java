package com.logandhillon.typeofwar.entity.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SkinOptionsEntity extends Clickable{
    private final Color color;
    private final Runnable onPress;
    private boolean clicked;

    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of this element's hitbox
     * @param h width of this element's hitbox
     */
    public SkinOptionsEntity(float x, float y, float w, float h, Color color, boolean clicked, Runnable onPress) {
        super(x, y, w, h);
        this.color = color;
        this.clicked = clicked;
        this.onPress = onPress;
    }

    @Override
    public void onClick(MouseEvent e) {
        onPress.run();
    }

    public void onPress() {
        onPress.run();
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(this.color);
        g.fillRect(x, y, w, h);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }

    public boolean isClicked() {return this.clicked;}

    public void setClicked(boolean clicked) {this.clicked = clicked;}

    public Color getColor() {return color;}
}
