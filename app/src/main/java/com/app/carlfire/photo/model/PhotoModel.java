package com.app.carlfire.photo.model;

import com.app.carlfire.beans.PhotoGirl;
import com.app.carlfire.net.parser.PhotoParser;
import com.app.commonlib.base.BaseMvpModel;
import com.app.commonlib.net.listener.NetListener;
import com.app.commonlib.net.NetWorkAccessEngine;

import java.util.List;

/**
 * Created by Carl on 2016-12-09 009.
 */

public class PhotoModel implements BaseMvpModel {
    public void loadPhoto(String url,final PhotoModel.OnLoadPhotoListener listener) {
        NetWorkAccessEngine.getInstance().get(url, null, new PhotoParser(), new NetListener<List<PhotoGirl>>() {
            @Override
            public void onSuccess(List<PhotoGirl> result) {
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface OnLoadPhotoListener {
        void onSuccess(List<PhotoGirl> list);

        void onFailure(String msg);
    }
}
