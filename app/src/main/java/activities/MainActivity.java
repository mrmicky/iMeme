package activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;

import controller.IMeme;
import com.example.michael.p12205836.R;

public class MainActivity extends Activity {

    IMeme iMeme;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int phoneWidth = display.widthPixels;
        int phoneHeight = display.heightPixels;

        iMeme = new IMeme(this, phoneWidth, phoneHeight);
        setContentView(iMeme);

        mediaPlayer = MediaPlayer.create(this, R.raw.splashtheme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //Toast.makeText(this, "onCreate() for MainActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iMeme.resume();
        //Toast.makeText(this, "onResume() for MainActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            iMeme.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mediaPlayer.stop();
        //Toast.makeText(this, "onPause() for MainActivity", Toast.LENGTH_SHORT).show();
    }

    public void startScore() {
        Intent intentStart = new Intent(this, SplashActivity.class);
        startActivity(intentStart);
    }
}
