package controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.michael.p12205836.R;

import java.io.IOException;
import java.util.ArrayList;

import model.Bullets;
import model.Enemy;
import model.ScrollingBackground;
import model.Ship;

public class IMeme extends SurfaceView implements Runnable {

    // For parsing the context, initialising a thread
    private Context context;
    private Thread thread = null;

    // Lock surface > draw graphics (in Draw)
    private SurfaceHolder surfaceHolder;

    // Array list of "stars" for background animation
    public ArrayList<ScrollingBackground> stars = new ArrayList<ScrollingBackground>();

    // Is game running
    private boolean playing;
    private boolean paused;

    // Draw
    private Canvas canvas;
    private Paint paint;

    // Screens X and Y
    private int screenX;
    private int screenY;

    // Player
    private Ship ship;
    private Bullets bullets;

    // Enemies
    private Bullets[] enemyBullets = new Bullets[800];
    private int nextBullet;
    private int maximumEnemyAttacks = 100;
    private Enemy[] enemies = new Enemy[8];
    private int numberOfEnemies = 0;

    // Sound
    private SoundPool soundPool;
    private int playerExplodeSound = -1;
    private int enemyExplodeSound = -1;
    private int shootSound = -1;

    // Tracking Fields
    private int score = 0;
    private int highscore;
    private int lives = 3;
    private int level = 1;
    private int hasShot = 0;

    // Bitmap
    Bitmap background;

    // Save / Load
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public IMeme(Context context, int x, int y) {

        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        // Scaled background bitmap
        Bitmap preBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background = Bitmap.createScaledBitmap(preBackground, x, y, true);

        // soundPool
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("playerShoot.ogg");
            shootSound = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("playerHit.ogg");
            enemyExplodeSound = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("playerHit.ogg");
            playerExplodeSound = soundPool.load(descriptor, 0);

        } catch (IOException e) {
            Log.e("error", "failed to load sound files");
        }
        // Number of stars
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            ScrollingBackground spec = new ScrollingBackground(x, y);
            stars.add(spec);
        }

        // Get a reference to a file called score.
        prefs = context.getSharedPreferences("score", context.MODE_PRIVATE);
        // Initialize editor
        editor = prefs.edit();
        // Load high score
        highscore = prefs.getInt("score", 20);

        // STARTS THE GAME
        beginGame();
        paused = false;
    }

    /**
     * Create a player, initialise the players bullets.
     * Create an Arraylist of new bullets for enemies
     * Create amount of enemies (for testing, a line of 8)
     */
    private void beginGame() {
        ship = new Ship(context, screenX, screenY);
        bullets = new Bullets(screenY);

        for (int i = 0; i < enemyBullets.length; i++) {
            enemyBullets[i] = new Bullets(screenY);
        }

        numberOfEnemies = 0;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 1; row++) {
                enemies[numberOfEnemies] = new Enemy(context, row, column, screenX, screenY);
                numberOfEnemies++;
            }
        }
    }

    /**
     * Run the game
     * If the pause is false, update and draw
     * else loop
     */
    @Override
    public void run() {
        while (playing) {

            if (!paused) {
                update();
            }
            draw();
        }
    }

    /**
     * Method created to prevent the enemies on better hardware
     * from moving prior to gameView loading
     *
     * @param a is the integer 0,1 depending on state
     */
    public void setHasShot(int a) {
        hasShot = a;
    }

    /**
     * Update the players ship
     * if the player has moved or shot, update the enemies in array of enemies
     * Checks if any enemies can shoot, if returns true continue to shoot based on their x and y
     * increment their bullet count to ensure they don't fire too many times
     * if an enemy touches the side of the screen set to true and force them to move down (move.state = down)
     * General health check lives <= 0 then LOSE etc...
     */
    private void update() {
        boolean touchSide = false;
        boolean lost = false;

        // Player
        ship.update();

        if (bullets.getStatus()) {
            bullets.update(screenY / 50);
        }

        // Enemy
        if (hasShot == 1) {
            for (int i = 0; i < numberOfEnemies; i++) {
                if (enemies[i].exists()) {
                    enemies[i].update();
                    if (enemies[i].canShoot(ship.getX(), ship.getLength())) {
                        if (enemyBullets[nextBullet].shoot(enemies[i].getX() + enemies[i].getLength() / 2, enemies[i].getY() + enemies[i].getHeight(), bullets.DOWN)) {
                            nextBullet++;
                            if (nextBullet == maximumEnemyAttacks) {
                                nextBullet = 0;
                            }
                        }
                    }

                    if (enemies[i].getX() > screenX - enemies[i].getLength() || enemies[i].getX() < 0) {
                        touchSide = true;
                    }
                }
            }
        }
        // Enemy bullet status update and speed calculation
        for (int i = 0; i < enemyBullets.length; i++) {
            if (enemyBullets[i].getStatus()) {
                enemyBullets[i].update(screenY / 150);
            }
        }

        if (touchSide) {
            // Move all the enemies down and change direction
            for (int i = 0; i < numberOfEnemies; i++) {
                enemies[i].enemyMovingDown();

                // Game end?
                if (enemies[i].getY() > screenY - ship.getHeight() * 3) {
                    lost = true;

                    //check for new high score
                    if (score > highscore) {
                        // Save high score
                        editor.putInt("score", score);
                        editor.commit();
                        highscore = score;
                    }
                    // Now end the game
                    doLose();
                }
            }
        }

        if (lost) {
            beginGame();
        }


        // Player if bullet less than (0) then setInactive (allowing shoot)
        if (bullets.trajectory() < 0) {
            bullets.setInactive();
        }

        // Enemy if bullet less than > Y then setInactive (allowing shoot)
        for (int i = 0; i < enemyBullets.length; i++) {
            if (enemyBullets[i].trajectory() > screenY) {
                enemyBullets[i].setInactive();
            }
        }

        /**
         * Checks the status of the bullet, if the player bullet intersects with an enemy
         * destroy that enemy, increment score of player
         * check if game is over
         *
         */
        if (bullets.getStatus()) {
            for (int i = 0; i < numberOfEnemies; i++) {
                if (enemies[i].exists()) {
                    if (RectF.intersects(bullets.getRect(), enemies[i].getRect())) {
                        soundPool.play(enemyExplodeSound, 1, 1, 0, 0, 1);
                        enemies[i].destroyEnemy();
                        bullets.setInactive();
                        score = score + 10;

                        // Win checker
                        if (score > 0 && score % 80 == 0) {
                            paused = true;

                            // Increasing difficulty as we win
                            // Adds lives and increments level, then increases the ships speed.
                            lives += 1;
                            level += 1;
                            beginGame();
                            paused = false;
                            for (int count = 0; count <= level; count++) {
                                for (int x = 0; x < numberOfEnemies; x++) {
                                    enemies[x].incrementShipSpeed();
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         *  Check the status of all enemies from the array of enemies bullets
         *  if the enemy bullet intersects with the player then decrement a life
         *  play relative sound
         *  check for game over (do lose if so)
         */
        for (int i = 0; i < enemyBullets.length; i++) {
            if (enemyBullets[i].getStatus()) {
                if (RectF.intersects(ship.getRect(), enemyBullets[i].getRect())) {
                    enemyBullets[i].setInactive();
                    lives--;
                    soundPool.play(playerExplodeSound, 1, 1, 0, 0, 1);

                    // GAME OVER
                    if (lives == 0) {
                        //check for new high score
                        if (score > highscore) {
                            // Save high score
                            editor.putInt("score", score);
                            editor.commit();
                            highscore = score;
                        }
                        doLose();
                    }
                }
            }
        }
        /**
         * Update the background based on enemy speed (faster they go faster it goes)
         */
        for (ScrollingBackground sd : stars) {
            sd.update((int) enemies[0].getShipSpeed());
        }
    }

    /**
     * Ends the mainactivity, forcing the game back to the mainmenuactivity
     */
    public void doLose() {
        // Exits back to MAIN MENU
        ((Activity) context).finish();
    }

    /**
     * Draw method for the games canvas
     * Ensure lock/unlock of canvas
     * Draw bitmaps of player ship, enemies and draw the background image (all are division scaled)
     */
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();

            // Draw Background
            canvas.drawBitmap(background, 0, 0, null);

            // Draw player ship
            // Adding some height moves the player ship above the instructional buttons
            canvas.drawBitmap(ship.getBitmap(), ship.getX(), screenY - ship.getHeight() * 1.90f, paint);

            // Draw player bullets
            if (bullets.getStatus()) {
                canvas.drawRect(bullets.getRect(), paint);
            }

            // Draw enemies
            for (int i = 0; i < numberOfEnemies; i++) {
                if (enemies[i].exists()) {
                    enemies[i].draw(canvas);
                }
            }

            // Draw enemy bullets
            for (int i = 0; i < enemyBullets.length; i++) {
                if (enemyBullets[i].getStatus()) {
                    canvas.drawRect(enemyBullets[i].getRect(), paint);
                }
            }

            // DRAW STARS IN BACKGROUND
            paint.setColor(Color.argb(255, 255, 255, 255));
            for (ScrollingBackground sd : stars) {
                //  Drawn Y > X to flip the stars
                canvas.drawPoint(sd.getY(), sd.getX(), paint);
            }

            paint.setColor(Color.WHITE);
            paint.setTextSize(screenX / 25);

            // Casting the float to int to make more sense on screen in-game
            int tempInt = (int) enemies[0].getShipSpeed();
            canvas.drawText("Score: " + score + "  Lives: " + lives + "  Level: " + level + "  Speed: " + tempInt, screenX / 8, screenY / 16, paint);

            // Draw all!
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Pause the thread.
     * @throws InterruptedException
     */
    public void pause() throws InterruptedException {
        playing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    /**
     * Resumes the thread.
     */
    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Methods override, determines upon action down certain event
     * Bottom 1/7th of screen divide 2 left/right = move ship left or right
     * Anything in the top 60% of the screen minus the top 10% fires
     * Anything in the top divison of 10 of the screen pauses and unpauses the game
     * Action up (lift finger) assigns the ship has been stopped (as a state)
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 1/6 of screen (bottom) is used for movement
                if (motionEvent.getY() > screenY - screenY / 7) {
                    if (motionEvent.getX() > screenX / 2) {
                        setHasShot(1);
                        ship.setMovementState(ship.RIGHT);
                    } else {
                        setHasShot(1);
                        ship.setMovementState(ship.LEFT);
                    }
                }

                if (motionEvent.getY() < screenY - screenY / 6) {
                    if (bullets.shoot(ship.getX() + ship.getLength() / 2, ship.getY() - ship.getHeight(), bullets.UP)) {
                        setHasShot(1);
                        soundPool.play(shootSound, 1, 1, 0, 0, 1);
                    }
                }

                if (motionEvent.getY() < screenY / 10 && motionEvent.getX() > screenX / 2) {
                    if (!paused) {
                        try {
                            pause();
                            paused = true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("Error:", "GAME PAUSED");
                    } else if (paused) {
                        resume();
                        paused = false;
                        Log.e("Error:", "GAME RESUMED");
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                // Upon releasing touch, ship.STOPPED
                if (motionEvent.getY() > screenY - screenY / 10) {
                    ship.setMovementState(ship.STOPPED);
                }
                break;
        }
        return true;
    }
}