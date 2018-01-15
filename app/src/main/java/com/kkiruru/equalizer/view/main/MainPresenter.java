package com.kkiruru.equalizer.view.main;

import android.media.audiofx.Equalizer;
import android.util.Log;

import com.kkiruru.equalizer.model.Band;

/**
 * Created by 1100416 on 2018. 1. 15..
 */

public class MainPresenter extends MainContract.Presenter {
	private Equalizer mEqualizer;
	private int mAudioSession = -1;
	private boolean mEffectEnabled = false;

	private short mPreset = 0;


	public MainPresenter(MainContract.View view) {
		attachView(view);
	}

	@Override
	public void initEqualizer() {
		initEqualizer(0);
		mAudioSession = 0;
		displayEQStatus(mEqualizer);
	}

	@Override
	public void changePreset(short presetId) {
		mEqualizer.usePreset(presetId);
		updateBandLevel(mEqualizer);
		mPreset = presetId;
	}

	@Override
	void changeAudioSession(int audioSession) {
		initEqualizer(audioSession);
		mAudioSession = audioSession;
		enableEqualisingEffect(mEffectEnabled);
	}

	@Override
	void effectOnOff(boolean effectOn) {
		mEffectEnabled = effectOn;
		enableEqualisingEffect(mEffectEnabled);
	}


	private void initEqualizer(int audioSession) {
		if (mAudioSession != audioSession) {
			try {
				mEqualizer = new Equalizer(0, audioSession);
				mEqualizer.usePreset(mPreset);
				mEqualizer.setParameterListener(mEqualizerParameterChangeListener);
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}
		}
	}

	private void enableEqualisingEffect(boolean enable) {
		if ( mEqualizer != null ){
			mEqualizer.setEnabled(enable);
		}
	}

	private void displayEQStatus(Equalizer equalizer) {
		if (mView != null && equalizer != null) {
			int numOfPresets = equalizer.getNumberOfPresets();
			String[] presetNames = new String[numOfPresets];

			for (short p = 0; p < numOfPresets; p++) {
				presetNames[p] = equalizer.getPresetName(p);
			}
			((MainContract.View) mView).showPresetList(presetNames);

			int numOfBands = mEqualizer.getNumberOfBands();
			Band[] bands = new Band[numOfBands];
			for (short i = 0; i < numOfBands; i++) {
				int centerFreq = mEqualizer.getCenterFreq(i);
				short bandLevel = mEqualizer.getBandLevel(i);
				int freqRange[] = mEqualizer.getBandFreqRange(i);
				short[] bandLevelRange = mEqualizer.getBandLevelRange();

				Band band = new Band(centerFreq, bandLevel, freqRange, bandLevelRange[0], bandLevelRange[bandLevelRange.length - 1]);
				bands[i] = band;
			}
			((MainContract.View) mView).showBandInfo(bands);
		}
	}

	private void updateBandLevel(Equalizer equalizer) {
		if (equalizer != null) {
			int numOfBands = equalizer.getNumberOfBands();
			short[] bandLevels = new short[numOfBands];
			for (short i = 0; i < numOfBands; i++) {
				bandLevels[i] = equalizer.getBandLevel(i);
			}

			if (mView != null) {
				((MainContract.View) mView).showBandLevel(bandLevels);
			}
		}
	}


	private Equalizer.OnParameterChangeListener mEqualizerParameterChangeListener = new Equalizer.OnParameterChangeListener() {
		@Override
		public void onParameterChange(Equalizer equalizer, int i, int i1, int i2, int i3) {
			Log.d("eq", "onParameterChange : i " + i + ", i1 " + i1 + ", i2 " + i2 + ", i3 " + i3);
			updateBandLevel(equalizer);
		}
	};


}
