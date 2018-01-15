package com.kkiruru.equalizer.view.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kkiruru.equalizer.R;
import com.kkiruru.equalizer.model.Band;

/**
 * Created by 1100416 on 2018. 1. 11..
 */

public class FreqLevelItem extends LinearLayout {
    private Band band;
    private SeekBar mSeekBar;

    private TextView mCenterFreq;
    private TextView mLevelMin;
    private TextView mLevelMax;

    private TextView mLevel;

    interface OnBandLevelChangeListener {
        void onBandLevelChange(View band, short level);

        void onBandLevelChangeStop();
    }

    OnBandLevelChangeListener mBandLevelChangeListener;


    public FreqLevelItem(Context context) {
        this(context, null);
    }

    public FreqLevelItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreqLevelItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.freq_level_item, this, false);

        mCenterFreq = rootView.findViewById(R.id.centerFreq);
        mLevelMin = rootView.findViewById(R.id.levelMin);
        mLevelMax = rootView.findViewById(R.id.levelMax);

        mLevel = rootView.findViewById(R.id.currentLevel);
        mSeekBar = rootView.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (band != null) {
                    mLevel.setText("" + (i + band.rangeMin));
                } else {
                    mLevel.setText("" + i);
                }
                if (mBandLevelChangeListener != null) {
                    mBandLevelChangeListener.onBandLevelChange(FreqLevelItem.this, (short) (i + band.rangeMin));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBandLevelChangeListener != null) {
                    mBandLevelChangeListener.onBandLevelChangeStop();
                }
            }
        });
        addView(rootView);
    }

    public void setLevelInfo(Band band) {
        this.band = band;

        mCenterFreq.setText(displayNameOfHz(band.centerFreq));

        mLevelMin.setText("" + band.rangeMin);
        mLevelMax.setText("" + band.rangeMax);

        mSeekBar.setMax(band.rangeMax - band.rangeMin);
        mSeekBar.setProgress(band.level - band.rangeMin);
    }

    private String displayNameOfHz(int freq) {
        String display = String.valueOf(freq) + "mHz";
        if (1000 * 1000 * 1000 < freq) {
            display = String.format("%.1f", (freq / 1000) / 1000f) + "MHz";
        } else if (1000 * 1000 < freq) {
            display = String.format("%.1f", (freq / 1000) / 1000f) + "KHz";
        } else if (1000 < freq) {
            display = String.format("%d", freq / 1000) + "Hz";
        }
        return display;
    }

    public void setBandLevel(short level) {
        band.level = level;
        setLevelInfo(band);
    }

    public void setBandLevelChangeListener(OnBandLevelChangeListener listener) {
        mBandLevelChangeListener = listener;
    }

}
