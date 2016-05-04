package model;

import java.util.Random;

public class ScrollingBackground {

    // Variables for initialising the positions of the STARS
    private int scrollSpeed, x, y, maxX, maxY, minX, minY;

    public ScrollingBackground(int screenX, int screenY) {
        // new random generator
        Random random = new Random();
        // Generates a random number between 1 and 10
        scrollSpeed = random.nextInt(10 - 1) + 1;
        // Declares the variables as the parsed in values from display metrics, X and Y
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        // Gives X and Y random integer values based on the screens X and Y (could be anywhere on the screen)
        x = random.nextInt(maxX);
        y = random.nextInt(maxY);
    }

    /**
     * Updates the current location of the STAR on the screen
     * Uses Variable assignment -= to control the speed of the STARS moving upwards
     * if the STAR pos is < 0 control the star with a different random location
     * @param speedControlVariable
     */
    public void update(int speedControlVariable) {
        // Speed up when the player does
        x -= speedControlVariable;
        x -= scrollSpeed;
        // REMAKE THE STARS
        if (x < 0) {
            x = maxX;
            Random random = new Random();
            y = random.nextInt(maxY);
            scrollSpeed = random.nextInt(15);
        }
    }

    /**
     *
     * @return the X value
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return The Y value
     */
    public int getY() {
        return y;
    }
}
