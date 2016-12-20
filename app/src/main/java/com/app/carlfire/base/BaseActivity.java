package com.app.carlfire.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity基类
 *
 * @param <P>
 */

public abstract class BaseActivity<P extends BaseMvpPresenter> extends AppCompatActivity implements BaseMvpView {
    private Unbinder mUnbinder;
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewResId());
        mUnbinder = ButterKnife.bind(this);
        mPresenter = setPresenter();
        initWidgets(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    /**
     * 获取Activity的布局资源
     *
     * @return 布局资源id
     */
    protected abstract int setContentViewResId();

    /**
     * 设置presenter层对象
     *
     * @return presenter层对象
     */
    protected abstract P setPresenter();

    /**
     * 初始化组件工作
     *
     * @param savedInstanceState
     */
    protected void initWidgets(Bundle savedInstanceState) {
    }
}
