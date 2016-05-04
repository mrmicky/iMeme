package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.michael.p12205836.R;

public class Ship {
    // Create the rectangle (with floats)
    RectF rect;
    // Bitmap image used for enemies
    private Bitmap bitmap;
    // The variables used for enemy length and height (for drawing)
    private float length;
    private float height;
    // Parsed in values fromt he display metrics getdisplay window feature (x and y)
    private int screenX;
    private int screenY;
    // Variables used for ship movement calculations
    private float x;
    private float y;
    // player ship current speed
    private float shipSpeed;
    // variables for determining if the player has stopped
    public int STOPPED = 0;
    private int shipMoving = STOPPED;
    // State calcuation variables
    public final int LEFT = 1;
    public final int RIGHT = 2;

    public Ship(Context context, int screenX, int screenY) {
        // Assigning the values for the construction of the player ship
        rect = new RectF();

        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 10;
        height = screenY / 10;

        x = screenX / 2;
        y = screenY - 20;
        // Bitmap instantiation for player ship
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership2);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (length), (int) (height), false);
        // Setting the ships speed relative to the display metric X axis(screen resolution calculation) / 150
        shipSpeed = screenX / 150;
    }

    /**
     *
     * @return the player ship bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     *
     * @return the rectF of the player (top, bottom, left, right)
     */
    public RectF getRect() {
        return rect;
    }

    /**
     *
     * @return the X for updating pos
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @return the Y for updating pos
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @return the height of the player ship for determining bullet starting points etc
     */
    public float getHeight() {
        return height;
    }

    /**
     *
     * @return the length of the player ship for determining extra features such as DRAWING getX + ship.getHeight() in game view
     */
    public float getLength() {
        return length;
    }

    /**
     * Setting the state of the ship to moving if the touch event returns true
     * @param state
     */
    public void setMovementState(int state) {
        shipMoving = state;
    }

    /**
     * Check the state of the player ship,
     * left = minus the ships speed from x and assign to x
     * CONTAINS CHECKER FOR IF X < 0 (adds touching edge feature)
     *
     * right = plussing the ships speed to the x and assign to x
     * CONTAINS CHECKER FOR HITTING THE MAX X OF THE SCREEN
     *
     * UPDATES the current rectF positions of the top, bottom, left, right
     * relative tot he newly assigned X and Y
     */
    public void update() {
        if (shipMoving == LEFT) {
            x = x - shipSpeed;

            x = (x < 0) ? 0 : x;
        }

        if (shipMoving == RIGHT) {
            x = x + shipSpeed;


            int maxX = this.screenX - this.bitmap.getWidth();
            x = (x > maxX) ? maxX : x;
        }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }
}
