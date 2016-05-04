package model;

import android.graphics.RectF;

public class Bullets {
    // Create the rectangle (with floats)
    private RectF rect;
    // The variabels used for starting positions
    private float x;
    private float y;
    // State calcuation variables
    public final int UP = 0;
    public final int DOWN = 1;
    // Direction
    int heading = -1;
    // Size of bullet
    private int width;
    private int height;
    // Is a bullet currently active (if so, can't fire)
    private boolean isActive;

    public Bullets(int screenY) {

        width = screenY / 200;
        height = screenY / 35;
        isActive = false;

        rect = new RectF();
    }

    public RectF getRect() {
        return rect;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    /**
     *  Method calculates the variable updates based on the state of the bullet
     *  If down then Get the Y and add the bullets height (moving it down)
     *  Else return it's position
     * @return
     */
    public float trajectory() {
        if (heading == DOWN) {
            return y + height;
        } else {
            return y;
        }
    }

    /**
     * If a bullet is not active, then assign the current position based on parsed in values
     * the values are the current position of CALLER (enemy/player) X and Y
     * And a direction the bullet is to travel (a state)
     *
     * @param startX
     * @param startY
     * @param direction
     * @return
     */
    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }
        return false;
    }

    /**
     * Update the current position of the bullet
     * Depending on state, up or down, and based on speed of bullet (parsed value)
     * Update the Y of the bullet UP = -Y DOWN = +Y
     * @param speed
     */
    public void update(long speed) {

        if (heading == UP) {
            y = y - speed;
        } else {
            y = y + speed;
        }

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }
}
