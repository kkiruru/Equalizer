package com.kkiruru.equalizer;

import android.content.Context;
import android.media.audiofx.Equalizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1100416 on 2018. 1. 11..
 */

public class EqualizerController extends LinearLayout {

    private Equalizer mEqualizer;
    private Spinner mSpinner;
    private int mAudioSession;
    private RadioGroup mToggleEffect;
    private ViewGroup mBandList;
    private int mPreset = 0;

    List<FreqLevelItem> freqLevelItems = new ArrayList<>();

    public EqualizerController(Context context) {
        this(context, null);
    }

    public EqualizerController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualizerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        initView();

        try {
            mEqualizer = null;
            mEqualizer = new Equalizer(0, 0);
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
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, presetNames);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("eq", "onItemSelected position " + position + ", id " + id);
                changePreset(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("eq", "onNothingSelected");
            }
        });
        mPreset = 0;

        int numOfBands = mEqualizer.getNumberOfBands();

        for (short i = 0; i < numOfBands; i++) {
            int centerFreq = mEqualizer.getCenterFreq(i);
            short bandLevel = mEqualizer.getBandLevel(i);

            Log.d("eq", "band[" + i + "] centerFreq " + centerFreq + ", bandLevel " + bandLevel);

            int freqRange[] = mEqualizer.getBandFreqRange(i);
            for (int j = 0; j < freqRange.length; j++) {
                Log.d("eq", "__ freqRange[" + j + "] " + "" + freqRange[j]);
            }
            short[] bandLevelRange = mEqualizer.getBandLevelRange();
            for (int j = 0; j < bandLevelRange.length; j++) {
                Log.d("eq", "__ bandLevelRange[" + j + "] " + "" + bandLevelRange[j]);
            }

            int currentPreset = mEqualizer.getCurrentPreset();
            Log.d("eq", "currentPreset " + currentPreset);

            Band band = new Band(centerFreq, bandLevel, freqRange, bandLevelRange[0], bandLevelRange[bandLevelRange.length - 1]);
            FreqLevelItem freqLevelItem = new FreqLevelItem(getContext(), null, 0);
            freqLevelItem.setLevelInfo(band);

            mBandList.addView(freqLevelItem, i);

            freqLevelItems.add(freqLevelItem);
        }

        displayBandLevel();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.equalizer_controller, this, false);

        mToggleEffect = rootView.findViewById(R.id.toggleEffect);
        mToggleEffect.check(R.id.off);
        mToggleEffect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                enableEffect(R.id.on == i);
            }
        });

        mBandList = rootView.findViewById(R.id.bandList);

        addView(rootView);
    }


    private void changePreset(int presetId) {
        mPreset = presetId;
        mEqualizer.usePreset((short) mPreset);

        Equalizer.Settings setting = mEqualizer.getProperties();
        Log.d("eq", "setting " + setting.toString());
        updateBandValue();
        displayBandLevel();
    }


    private void enableEffect(boolean enable) {
        if (mEqualizer != null) {
            mEqualizer.setEnabled(enable);
        }

        if (enable) {
            changePreset(mPreset);
        }
    }


    public void updateAudioSessionId(int id) {
        if (id != mAudioSession) {
            mEqualizer.setEnabled(false);
            mEqualizer = new Equalizer(0, id);
        }

        enableEffect(mToggleEffect.getCheckedRadioButtonId() == R.id.on);
        mAudioSession = id;
    }

    private String getBandLevel(short index) {

        StringBuilder bandInfo = new StringBuilder();

        int freqRange[] = mEqualizer.getBandFreqRange(index);
        bandInfo.append(freqRange[0] + "  ~  " + freqRange[1]);
        bandInfo.append("   [ " + mEqualizer.getBandLevel(index) + " ]");

        return bandInfo.toString();
    }


    private void displayBandLevel() {
        int numOfBands = mEqualizer.getNumberOfBands();

        for (short i = 0; i < numOfBands; i++) {
            int centerFreq = mEqualizer.getCenterFreq(i);
            int bandLevel = mEqualizer.getBandLevel(i);

            Log.d("eq", "band[" + i + "] centerFreq " + centerFreq + ", bandLevel " + bandLevel);

            int freqRange[] = mEqualizer.getBandFreqRange(i);
            for (int j = 0; j < freqRange.length; j++) {
                Log.d("eq", "__ freqRange[" + j + "] " + "" + freqRange[j]);
            }
            short[] bandLevelRange = mEqualizer.getBandLevelRange();
            for (int j = 0; j < bandLevelRange.length; j++) {
                Log.d("eq", "__ bandLevelRange[" + j + "] " + "" + bandLevelRange[j]);
            }

            int currentPreset = mEqualizer.getCurrentPreset();
            Log.d("eq", "currentPreset " + currentPreset);

        }
        Equalizer.Settings setting = mEqualizer.getProperties();
        Log.d("eq", "setting " + setting.toString());

    }


    private void updateBandValue() {

    }

}
