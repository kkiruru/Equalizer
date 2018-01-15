package com.kkiruru.equalizer.view;

/**
 * Created by 1100416 on 2018. 1. 15..
 */

public class BasePresenter<T extends BaseView> {
	protected T mView;

	final public void attachView(T view){
		mView = view;
	}

	final public void detachView(){
		mView = null;
	}
}
