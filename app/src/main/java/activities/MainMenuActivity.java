package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.michael.p12205836.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        //Toast.makeText(this, "onCreate() for SplashActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume() for SplashActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, "onPause() for SplashActivity", Toast.LENGTH_SHORT).show();
    }

    public void startHighScore(View view) {
        Intent intentStart = new Intent(this, ScoreActivity.class);
        startActivity(intentStart);
    }

    public void startSplash(View view) {
        Intent intentStart = new Intent(this, SplashActivity.class);
        startActivity(intentStart);
    }

    public void startGame(View view) {
        Intent intentStart = new Intent(this, MainActivity.class);
        startActivity(intentStart);
    }

    public void startSettings(View view) {
        Intent intentStart = new Intent(this, SettingsActivity.class);
        startActivity(intentStart);
    }
}
