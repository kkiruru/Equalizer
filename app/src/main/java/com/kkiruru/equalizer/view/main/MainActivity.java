package com.kkiruru.equalizer.view.main;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.kkiruru.equalizer.model.Band;
import com.kkiruru.equalizer.R;

public class MainActivity extends AppCompatActivity implements MainContract.View{
	MediaPlayer mMediaPlayer;
	Spinner mPreset;
	MainPresenter mMainPresenter;
	private RadioGroup mToggleEffect;

	ViewGroup mBandList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPreset = findViewById(R.id.preset);
		mBandList = findViewById(R.id.bandList);

		mToggleEffect = findViewById(R.id.toggleEffect);
		mToggleEffect.check(R.id.off);
		mToggleEffect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				mMainPresenter.effectOnOff(R.id.on == i);
			}
		});

		mMainPresenter = new MainPresenter(this);
		mMainPresenter.initEqualizer();
	}

	@Override
	protected void onStop(){
		super.onStop();
		mMainPresenter.detachView();
	}

	public void OnClickPlay(View view) {
		play(0);
	}

	public void OnClickStop(View view) {
		stop();
	}

	@TargetApi(Build.VERSION_CODES.M)
	private void play(int selNo) {
		stop();

		mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.the_dlx_two_kids);
		mMediaPlayer.setOnPreparedListener( new MediaPlayer.OnPreparedListener(){
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				Log.d("eq", "onPrepared " + mediaPlayer.getAudioSessionId());
				mMainPresenter.changeAudioSession(mediaPlayer.getAudioSessionId());
			}
		});

		mMediaPlayer.start();
	}


	private void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer = null;
		}
	}


	@Override
	public void showBandInfo(Band[] bands) {
		mBandList.removeAllViews();

		for( int i = 0; i < bands.length; i++ ){
			Band band = bands[i];
			FreqLevelItem freqLevelItem = new FreqLevelItem(this, null, 0);
			freqLevelItem.setLevelInfo(band);
			freqLevelItem.setId(i);
			mBandList.addView(freqLevelItem, i);
		}

	}

	@Override
	public void showPresetList(String[] presetNames) {
		ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, presetNames);
		mPreset.setAdapter(spinnerAdapter);
		mPreset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d("eq", "onItemSelected position " + position + ", id " + id);
				mMainPresenter.changePreset((short)position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.d("eq", "onNothingSelected");
			}
		});
	}

	@Override
	public void showBandLevel(short[] levels) {
		int count = mBandList.getChildCount();

		for( int i =0; i < count ; i++){
			View item = mBandList.getChildAt(i);

			if ( item instanceof FreqLevelItem ){
				FreqLevelItem freqLevelItem = (FreqLevelItem)item;
				freqLevelItem.setBandLevel(levels[i]);
			}
		}
	}
}
