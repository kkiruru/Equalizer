package com.kkiruru.equalizer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    EqualizerController mEqualizerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEqualizerController = findViewById(R.id.equalizerController);
    }

    public void OnClickPlay(View view) {
        play(0);
    }

    public void OnClickStop(View view) {
        stop();
    }

    private void play(int selNo) {
        stop();
        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.song1);
        mMediaPlayer.start();

        Log.d("eq", "audioSessionId " + mMediaPlayer.getAudioSessionId());

        mEqualizerController.updateAudioSessionId(mMediaPlayer.getAudioSessionId());
    }

    private void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }


}
