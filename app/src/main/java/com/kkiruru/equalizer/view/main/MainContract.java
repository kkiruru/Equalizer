package com.kkiruru.equalizer.view.main;

import com.kkiruru.equalizer.model.Band;
import com.kkiruru.equalizer.view.BasePresenter;
import com.kkiruru.equalizer.view.BaseView;

/**
 * Created by 1100416 on 2018. 1. 15..
 */

public interface MainContract {
	interface View extends BaseView {
		void showBandInfo(Band[] bands);
		void showPresetList(String[] presetNames);
		void showBandLevel(short levels[]);
	}

	abstract class Presenter extends BasePresenter {
		abstract void initEqualizer();
		abstract void changePreset(short presetId);
		abstract void changeAudioSession(int audioSession);
		abstract void effectOnOff(boolean effectOn);
	}
}
