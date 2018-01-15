package com.kkiruru.equalizer;

/**
 * Created by 1100416 on 2018. 1. 15..
 */

public class BasePresenter<T extends BaseView> {
	protected T mView;

	void attachView(T view){
		mView = view;
	}

	void detachView(){
		mView = null;
	}
}
