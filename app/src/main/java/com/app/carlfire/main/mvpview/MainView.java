package com.app.carlfire.main.mvpview;

import com.app.commonlib.base.BaseMvpView;

/**
 * Created by Carl on 2016-12-01 001.
 */

public interface MainView extends BaseMvpView {
    /**
     * 控制navigationBar的显隐状态
     *
     * @param targetState 目标状态
     *                    true 显示
     *                    false 隐藏
     */
    void showOrHideNavigationBar(Boolean targetState);
}
