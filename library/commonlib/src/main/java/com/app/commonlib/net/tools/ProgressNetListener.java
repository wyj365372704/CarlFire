package com.app.commonlib.net.tools;

/**
 * Created by Carl on 2017-01-06 006.
 */

public interface ProgressNetListener<T> extends NetListener<T>{
    void onProgress(String fileName,long current,long count);
}
