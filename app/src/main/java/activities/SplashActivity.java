package activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.michael.p12205836.R;

public class SplashActivity extends Activity {

    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //Toast.makeText(this, "onCreate() for SplashActivity", Toast.LENGTH_SHORT).show();

        mediaPlayer = MediaPlayer.create(this, R.raw.mainmenu);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        //Toast.makeText(this, "onResume() for SplashActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    public void startMainMenu(View view) {
        Intent intentStart = new Intent(this, MainMenuActivity.class);
        startActivity(intentStart);
    }
}

