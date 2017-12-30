package com.kkiruru.equalizer;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	Equalizer mEqualizer;
	MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ToggleButton toggleButton = findViewById(R.id.toggleEffect);
		toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				enableEffect(b);
			}
		});

		try {
			mEqualizer = null;
			mEqualizer = new Equalizer(0, 0);
			mEqualizer.setEnabled(true);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}


		NumberPicker numberPicker = findViewById(R.id.numberPicker);
		final List<String> presetNames = new ArrayList<>();
		int numOfPresets = mEqualizer.getNumberOfPresets();
		for (short p = 0; p < numOfPresets; p++) {
			presetNames.add(mEqualizer.getPresetName(p));
			Log.d("eq", "[" + p + "] presetName " + mEqualizer.getPresetName(p));
		}
		numberPicker.setMinValue(0);
		numberPicker.setMaxValue(presetNames.size()-1);
		numberPicker.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int value) {
				return presetNames.get(value);
			}
		});
		numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

			@Override
			public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal){
				Log.d("eq", "onValueChange " + oldVal + " > "+ newVal);
			}
		});


		int numOfBands = mEqualizer.getNumberOfBands();

		for (short i = 0; i < numOfBands; i++) {
			int centerFreq = mEqualizer.getCenterFreq(i);
			int bandLevel = mEqualizer.getBandLevel(i);

			Log.d("eq", "band[" + i + "] centerFreq " + centerFreq + ", bandLevel " + bandLevel);

			int freqRange[] = mEqualizer.getBandFreqRange(i);
			for (int j = 0; j < freqRange.length; j++) {
				Log.d("eq", "__ freqRange[" + j + "] " + "" + freqRange[j]);
			}

			int currentPreset = mEqualizer.getCurrentPreset();
			Log.d("eq", "currentPreset " + currentPreset);

		}
		Equalizer.Settings setting = mEqualizer.getProperties();
		Log.d("eq", "setting " + setting.toString());
	}

	public void OnClickPlay(View view) {
		play(0);
	}

	public void OnClickStop(View view) {
		stop();
	}


	public void onClickPreset(View view){

		switch (view.getId()){
			case R.id.preset1:
				mEqualizer.usePreset((short)0);
				break;
			case R.id.preset2:
				mEqualizer.usePreset((short)1);
				break;
			case R.id.preset3:
				mEqualizer.usePreset((short)2);
				break;
			case R.id.preset4:
				mEqualizer.usePreset((short)3);
				break;
			case R.id.preset5:
				mEqualizer.usePreset((short)4);
				break;
		}
		Equalizer.Settings setting = mEqualizer.getProperties();
		Log.d("eq", "setting " + setting.toString());
	}

	private void play(int selNo) {
		stop();
		mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.song1);
		mMediaPlayer.start();
	}

	private void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer = null;
		}
	}

	private void enableEffect(boolean enable) {

		if (mEqualizer != null) {
			mEqualizer.setEnabled(false);
		}
		mEqualizer = null;

		if (enable) {
			mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
			mEqualizer.setEnabled(true);
		}
	}

}
