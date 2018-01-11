package com.kkiruru.equalizer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by 1100416 on 2018. 1. 11..
 */

public class FreqLevelItem extends LinearLayout {
    Band band;

    SeekBar mSeekBar;
    TextView level;

    public FreqLevelItem(Context context) {
        this(context, null);
    }

    public FreqLevelItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }

    public FreqLevelItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.freq_level_item, this, false);

        level = rootView.findViewById(R.id.level);
        mSeekBar = rootView.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (band != null) {
                    level.setText(i + band.rangeMin);
                } else {
                    level.setText(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addView(rootView);
    }

    public void setLevelInfo(Band band) {
        this.band = band;

        mSeekBar.setMax(band.rangeMax - band.rangeMin);
        mSeekBar.setProgress(band.level - band.rangeMin);
    }

}
