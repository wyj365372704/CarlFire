package com.app.carlfire.base;


/**
 * 基础业务实现层presenter
 *
 * @param <V> 需要植入的view实现,通常为activity/fragment
 */
public abstract class BaseMvpPresenter<V extends BaseMvpView, M extends BaseMvpModel> {
    protected V mView;
    protected M mModel;

    public BaseMvpPresenter(V mvpView) {
        this.mView = mvpView;
        this.mModel = setModel();
    }

    protected abstract M setModel();

    protected void detach() {
        mView = null;
        mModel = null;
    }
}
