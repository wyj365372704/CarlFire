package com.app.commonlib.net.tools;

/**
 * Created by Carl on 2016-12-02 002.
 */
public interface NetListener<T> {
    void onSuccess(T result);

    void onFailure(String message);
}