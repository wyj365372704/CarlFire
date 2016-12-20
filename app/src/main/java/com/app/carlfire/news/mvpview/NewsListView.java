package com.app.carlfire.news.mvpview;

import com.app.carlfire.base.BaseMvpView;
import com.app.carlfire.beans.NewsBean;

import java.util.List;

/**
 * Created by Carl on 2016-12-01 001.
 */

public interface NewsListView extends BaseMvpView {
    void refreshList(List<NewsBean> newsList);

    void loadOrRefreshCompleted();

    void showLoadFailMsg(String message);

    void scroll2Item(int index);

    void noMoreShow();
}
