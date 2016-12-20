package com.app.carlfire.news.widget;

import com.app.carlfire.R;
import com.app.carlfire.base.BaseActivity;
import com.app.carlfire.news.mvpview.NewsDetailView;
import com.app.carlfire.news.presenter.NewsDetailPresenter;

public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailView{

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected NewsDetailPresenter setPresenter() {
        return new NewsDetailPresenter(this);
    }
}
