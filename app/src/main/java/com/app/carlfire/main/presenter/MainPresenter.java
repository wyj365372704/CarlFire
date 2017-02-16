package com.app.carlfire.main.presenter;

import com.app.carlfire.main.mvpview.MainView;
import com.app.commonlib.base.BaseMvpModel;
import com.app.commonlib.base.BaseMvpPresenter;

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
