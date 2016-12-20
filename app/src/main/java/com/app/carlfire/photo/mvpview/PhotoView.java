package com.app.carlfire.photo.mvpview;

import com.app.carlfire.base.BaseMvpView;
import com.app.carlfire.beans.NewsBean;
import com.app.carlfire.beans.PhotoGirl;

import java.util.List;

/**
 * Created by Carl on 2016-12-08 008.
 */

public interface PhotoView extends BaseMvpView{
    void refreshList(List<PhotoGirl> photoGirls);

    void loadOrRefreshCompleted();

    void loadOrRefreshFailed(String message);

    void notifyListScroll2Top();

    void noMoreShow();
}
