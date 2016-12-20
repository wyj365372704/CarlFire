package com.app.carlfire.main.presenter;

import android.support.annotation.NonNull;

import com.app.carlfire.base.BaseMvpModel;
import com.app.carlfire.base.BaseMvpPresenter;
import com.app.carlfire.main.mvpview.MainView;

/**
 * Created by Carl on 2016-12-01 001.
 */

public class MainPresenter extends BaseMvpPresenter<MainView,BaseMvpModel> {

    public MainPresenter(MainView mvpView) {
        super(mvpView);
    }

    @Override
    protected BaseMvpModel setModel() {
        return null;
    }
}
