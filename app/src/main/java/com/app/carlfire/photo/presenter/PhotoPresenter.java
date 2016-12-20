package com.app.carlfire.photo.presenter;

import android.util.Log;

import com.app.carlfire.base.BaseMvpPresenter;
import com.app.carlfire.beans.PhotoGirl;
import com.app.carlfire.net.Urls;
import com.app.carlfire.photo.model.PhotoModel;
import com.app.carlfire.photo.mvpview.PhotoView;

import java.util.List;

/**
 * Created by Carl on 2016-12-08 008.
 */

public class PhotoPresenter extends BaseMvpPresenter<PhotoView, PhotoModel> {
    private int pageIndex = 1;//当前页码

    public PhotoPresenter(PhotoView mvpView) {
        super(mvpView);
    }

    /**
     * 重新获取图片列表
     */
    public void refreshPhoto() {
        getPhoto(1);
    }

    /**
     * 加载更多图片
     */
    public void loadPhoto() {
        getPhoto(pageIndex);
    }

    /**
     * 获取图片
     *
     * @param index 起始页号[0,+]
     */
    private void getPhoto(final int index) {
        mModel.loadPhoto(getUrl(index), new PhotoModel.OnLoadPhotoListener() {
            @Override
            public void onSuccess(List<PhotoGirl> list) {
                if (list.size() == 0) {
                    mView.noMoreShow();
                } else {
                    mView.refreshList(list);
                    mView.loadOrRefreshCompleted();
                    if (index >= pageIndex) {
                        pageIndex += 1;
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.loadOrRefreshFailed(msg);
            }
        });
    }

    private String getUrl(int pageIndex) {
        StringBuffer sb = new StringBuffer();
        sb.append(Urls.PHOTO_HOST).append(pageIndex);
        return sb.toString();
    }

    @Override
    protected PhotoModel setModel() {
        return new PhotoModel();
    }
}
