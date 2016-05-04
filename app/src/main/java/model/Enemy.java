package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.michael.p12205836.R;

import java.util.Random;

public class Enemy {
    // Create the rectangle (with floats)
    RectF rect;
    // Random number generator for final bitmap
    Random generator = new Random();
    // Bitmap image used for enemies
    private Bitmap finalBitmap;
    // The variables used for enemy length and height (for drawing)
    private float length;
    private float height;
    // Used to updating position
    private float x;
    private float y;
    // The enemies ship(s) speed variable
    private float shipSpeed;
    // State calcuation variables
    public final int LEFT = 1;
    public final int RIGHT = 2;
    // Initialises as right (later changed when enemies touch right wall)
    private int enemyMoving = RIGHT;
    // Is the current enemy on the screen (boolean true or false)
    boolean isVisible;

    public Enemy(Context context, int row, int column, int screenX, int screenY) {
        // Assigning the values for the construction of an enemy
        rect = new RectF();

        length = screenX / 15;
        height = screenY / 15;

        isVisible = true;

        int padding = screenX / 20;

        x = column * (length / 2 + padding);
        y = row * (length + padding / 2);

        Random rand = new Random();
        int n = rand.nextInt(6 - 1) + 1;
        // Assigning the final bitmap to an enemy (used for randomness)
        if (n == 1) {
            finalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
            finalBitmap = Bitmap.createScaledBitmap(finalBitmap, (int) (length), (int) (height), false);
        } else if (n == 2) {
            finalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
            finalBitmap = Bitmap.createScaledBitmap(finalBitmap, (int) (length), (int) (height), false);
        } else if (n == 3) {
            finalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
            finalBitmap = Bitmap.createScaledBitmap(finalBitmap, (int) (length), (int) (height), false);
        } else if (n == 4) {
            finalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy4);
            finalBitmap = Bitmap.createScaledBitmap(finalBitmap, (int) (length), (int) (height), false);
        } else {
            finalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy5);
            finalBitmap = Bitmap.createScaledBitmap(finalBitmap, (int) (length), (int) (height), false);
        }
        // Setting the ships speed relative to the screens X axis size (from getDisplayWindow, metrics)
        shipSpeed = screenX / 300;
    }

    /**
     * Draw the enemy to the canvas (especially useful for array of enemies drawing efficiently
     * @param canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(finalBitmap, getX(), getY() + getHeight(), null);
    }

    /**
     * The enemy is destroyed (the value is set to false(do not display them))
     */
    public void destroyEnemy() {
        isVisible = false;
    }

    /**
     * Does an enemy exist (if visible true)
     * @return
     */
    public boolean exists() {
        return isVisible;
    }

    /**
     * Get the RectF of an enemy (top, bottom, left and right)
     * @return
     */
    public RectF getRect() {
        return rect;
    }

    /**
     * Get the X axis location of the of the enemy (changes in constructor to visually
     * represent the style of space invaders
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the enemy speed (copied variable names from player ship
     * @return
     */
    public float getShipSpeed() { return shipSpeed; }

    /**
     * Get the Y axis location of the of the enemy (changes in constructor to visually
     * represent the style of space invaders
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @return the length of the enemy (width, bad naming conventions...)
     */
    public float getLength() {
        return length;
    }

    /**
     *
     * @return the height of the enemy
     */
    public float getHeight() {
        return height;
    }

    /**
     * Update the enemy position
     * if the state is left then set based off enemy speed the position of their X
     * Then update the rectangle appropriatly
     */
    public void update() {
        if (enemyMoving == LEFT) {
            x = x - shipSpeed;
        }

        if (enemyMoving == RIGHT) {
            x = x + shipSpeed;
        }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }

    /**
     * Incrase the speed of enemies each time they bump (set up in gameview)
     * increase 15%
     */
    public void incrementShipSpeed() {
        shipSpeed = (float) (shipSpeed * 1.125);
    }

    /**
     * If the enemy touches the edge of the screen
     * change their state to polar oposite l/r
     * change their next Y for update to below current Pos
     */
    public void enemyMovingDown() {
        if (enemyMoving == LEFT) {
            enemyMoving = RIGHT;
        } else {
            enemyMoving = LEFT;
        }
        y = y + height / 2.5f;
    }

    /**
     * Method used for determining whether the player is underneath or not underneath the current enemy
     * if the player is underneath, higher chance of firing a bullet if not active
     * if the player is not underneath the current enemy, very low chance to shoot if not active
     *
     * THIS METHOD USES PARSED IN VALUES OF THE PLAYERS CURRENT LOCATIONS (X and Lenght combined)
     *
     * @param playerShipX
     * @param playerShipLength
     * @return
     */
    public boolean canShoot(float playerShipX, float playerShipLength) {
        int randomNumber = -1;
        // If passing over the player
        if ((playerShipX + playerShipLength > x && playerShipX + playerShipLength < x + length) || (playerShipX > x && playerShipX < x + length)) {
            randomNumber = generator.nextInt(100);
            if (randomNumber == 0) {
                return true;
            }
        } // whilst not passing over the player (lower chance to shoot)
        randomNumber = generator.nextInt(1750);
        if (randomNumber == 0) {
            return true;
        }
        return false;
    }
}
