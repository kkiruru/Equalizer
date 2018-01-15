package com.kkiruru.equalizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1100416 on 2018. 1. 11..
 */

public class PresetBoard {
    List<Preset> presets = new ArrayList<>();
    int currentPreset = 0;

    void addPreset( Preset preset ){
        presets.add(preset);
    }

    void setCurrentPreset(int index){
        currentPreset = index;
    }

    int getCurrentPreset(){
        return currentPreset;
    }
}
