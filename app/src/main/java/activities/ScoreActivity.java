package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.michael.p12205836.R;

public class ScoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        //Toast.makeText(this, "onCreate() for ScoreActivity", Toast.LENGTH_SHORT).show();

        // Prepare to load fastest time
        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        prefs = getSharedPreferences("score", MODE_PRIVATE);

        // Get a reference to the TextView in our layout
        final TextView highScoreText = (TextView) findViewById(R.id.textView2);

        // Load highscore time
        long highScore = prefs.getInt("score", 100);
        // Put the high score in our TextView
        highScoreText.setText("Highest Score: " + highScore);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume() for ScoreActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, "onPause() for ScoreActivity", Toast.LENGTH_SHORT).show();
    }

    public void startMainMenu(View view) {
        // Create intent
        Intent intentStart = new Intent(this, MainMenuActivity.class);
        // Start game
        startActivity(intentStart);
    }
}