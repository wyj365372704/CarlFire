package com.app.commonlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Carl on 2016-12-01 001.
 */
public abstract class BaseFragment<P extends BaseMvpPresenter> extends Fragment implements BaseMvpView {
    //是否可见
    protected boolean isVisible = false;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;
    private Unbinder mUnbinder;
    protected P mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(setContentViewResId(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mPresenter = setPresenter();
        initWidgets(savedInstanceState);
        isVisible = savedInstanceState != null && savedInstanceState.getBoolean("isVisible", false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        if (!isVisible && getUserVisibleHint())
            onFragmentVisibleChange(true);
        if (isVisible && !getUserVisibleHint())
            onFragmentVisibleChange(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isPrepared)
            return;
        if (isVisible && !getUserVisibleHint())
            onFragmentVisibleChange(false);
        else if (!isVisible && getUserVisibleHint())
            onFragmentVisibleChange(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isVisible", isVisible);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detach();
    }

    protected void onFragmentVisibleChange(boolean visible) {
        isVisible = visible;
    }

    /**
     * 获取Fragment的布局id
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
    protected abstract void initWidgets(Bundle savedInstanceState);
}
