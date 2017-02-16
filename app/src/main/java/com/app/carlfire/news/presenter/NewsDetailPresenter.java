package com.app.carlfire.news.presenter;

import com.app.carlfire.news.model.NewsDetailModel;
import com.app.carlfire.news.mvpview.NewsDetailView;
import com.app.commonlib.base.BaseMvpPresenter;

/**
 * Created by Carl on 2016-12-15 015.
 */

public class NewsDetailPresenter extends BaseMvpPresenter<NewsDetailView,NewsDetailModel> {

    public NewsDetailPresenter(NewsDetailView mvpView) {
        super(mvpView);
    }

    @Override
    protected NewsDetailModel setModel() {
        return new NewsDetailModel();
    }
}
