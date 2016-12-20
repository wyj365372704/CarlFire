package com.app.carlfire.news.presenter;

import com.app.carlfire.base.BaseMvpModel;
import com.app.carlfire.base.BaseMvpPresenter;
import com.app.carlfire.main.mvpview.MainView;
import com.app.carlfire.news.mvpview.NewsView;

/**
 * Created by Carl on 2016-12-01 001.
 */

public class NewsPresenter extends BaseMvpPresenter<NewsView,BaseMvpModel> {

    public NewsPresenter(NewsView mvpView) {
        super(mvpView);
    }

    @Override
    protected BaseMvpModel setModel() {
        return null;
    }

}
