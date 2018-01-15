package com.kkiruru.equalizer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by 1100416 on 2018. 1. 15..
 */

public class PresetItem extends LinearLayout {

	public PresetItem(Context context) {
		this(context, null);
	}

	public PresetItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PresetItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {

	}

}
