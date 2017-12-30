package com.kkiruru.equalizer;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	Equalizer mEqualizer;
	MediaPlayer mMediaPlayer;
	Spinner mSpinner;

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

		final List<String> presetNames = new ArrayList<>();
		int numOfPresets = mEqualizer.getNumberOfPresets();
		for (short p = 0; p < numOfPresets; p++) {
			presetNames.add(mEqualizer.getPresetName(p));
			Log.d("eq", "[" + p + "] presetName " + mEqualizer.getPresetName(p));
		}

		mSpinner = findViewById(R.id.spinner);
		ArrayAdapter spinnerAdapter;
		spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, presetNames);
		mSpinner.setAdapter(spinnerAdapter);
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d("eq", "onItemSelected position " + position + ", id " + id);
				mEqualizer.usePreset((short) position);
				Equalizer.Settings setting = mEqualizer.getProperties();
				Log.d("eq", "setting " + setting.toString());

				updateBandValue();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.d("eq", "onNothingSelected");
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


	private String getBandLevel(short index) {

		StringBuilder bandInfo = new StringBuilder();

		int freqRange[] = mEqualizer.getBandFreqRange(index);
		bandInfo.append(freqRange[0] + "  ~  " + freqRange[1]);
		bandInfo.append("   [ " + mEqualizer.getBandLevel(index) + " ]");

		return bandInfo.toString();
	}


	private void updateBandValue() {

		TextView band1 = findViewById(R.id.band1);
		band1.setText(getBandLevel((short)0));

		TextView band2 = findViewById(R.id.band2);
		band2.setText(getBandLevel((short)1));

		TextView band3 = findViewById(R.id.band3);
		band3.setText(getBandLevel((short)2));

		TextView band4 = findViewById(R.id.band4);
		band4.setText(getBandLevel((short)3));

		TextView band5 = findViewById(R.id.band5);
		band5.setText(getBandLevel((short)4));
	}

}
