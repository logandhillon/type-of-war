package com.logandhillon.typeofwar.engine;

import com.logandhillon.typeofwar.entity.Entity;
import com.logandhillon.typeofwar.entity.ui.MenuButton;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A MenuController controls an ordered list of {@link MenuButton}s, providing keyboard controls and a simplified method
 * for entity registration.
 * <p>
 * MenuControllers are immutable— the buttons it shall control cannot change.
 *
 * @author Logan Dhillon
 */
public class MenuController {
    private final MenuButton[] buttons;

    /** idx of currently active button; -1 means none */
    private int activeBtnIdx;

    /**
     * Creates a new immutable menu controller
     *
     * @param buttons ordered list of buttons from top to bottom, which will be used to sort navigation.
     */
    public MenuController(MenuButton... buttons) {
        this.buttons = buttons;
        this.activeBtnIdx = -1;

        for (int i = 0; i < buttons.length; i++) {
            int idx = i; // effectively final instance of i

            // select this button if it's hovered by cursor
            buttons[i].setMouseEnterHandler(e -> {
                if (activeBtnIdx >= 0) buttons[activeBtnIdx].setActive(false);
                activeBtnIdx = idx;
            });

            // unselect all buttons when unselecting button with cursor
            buttons[i].setMouseLeaveHandler(e -> activeBtnIdx = -1);
        }
    }

    /**
     * Handles key presses from JavaFX, used to change the actively selected button or press it.
     * <p>
     * This method should be registered to the {@link Scene#onKeyPressedProperty()} parameter of the
     * {@link GameScene#onBuild(Scene)}
     */
    public void onKeyPressed(KeyEvent e) {
        // when W/UP/SHIFT+TAB pressed, go up (-1) in buttons
        if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W ||
            (e.isShiftDown() && e.getCode() == KeyCode.TAB)) {
            if (activeBtnIdx >= 0) buttons[activeBtnIdx].setActive(false); // deselect old active button (if any)
            activeBtnIdx = Math.max(0, activeBtnIdx - 1);   // decrement idx (no lower than 0)
            buttons[activeBtnIdx].setActive(true); // select new active button
        }
        // when S/DOWN/TAB pressed, go down (+1) in buttons
        else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S ||
                 (!e.isShiftDown() && e.getCode() == KeyCode.TAB)) {
            if (activeBtnIdx >= 0) buttons[activeBtnIdx].setActive(false); // deselect old active button (if any)
            activeBtnIdx = Math.min(buttons.length - 1, activeBtnIdx + 1); // increment idx (no higher than highest idx)
            buttons[activeBtnIdx].setActive(true);  // select new active button
        }
        // when ENTER/SPACE pressed, simulate a click on the active button
        else if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
            buttons[activeBtnIdx].onPress();
        }
    }

    /**
     * Use this to register the buttons in this menu ({@link GameScene#addEntity(Entity)})
     *
     * @return an immutable list of the buttons in this controller.
     */
    public MenuButton[] getButtons() {
        return buttons;
    }
}
