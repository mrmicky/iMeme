package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.michael.p12205836.R;

public class SettingsActivity extends Activity {

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private int isVisible = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mediaPlayer = MediaPlayer.create(this, R.raw.settings);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        // Method for creating a volume seek bar
        initControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
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

    public void soundButton(View view) {
        soundVisible();
    }

    private boolean setIsVisible() {
        return true;
    }

    private void soundVisible() {
        if (isVisible == 0) {
            volumeSeekbar.setVisibility(View.VISIBLE);
            isVisible = 1;
        } else {
            volumeSeekbar.setVisibility(View.GONE);
            isVisible = 0;
        }
    }

    // Creates a seek bar for
    private void initControls() {
        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.seekbar);
            volumeSeekbar.setVisibility(View.GONE);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
