package com.app.commonlib.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.app.commonlib.net.listener.NetListener;
import com.app.commonlib.net.listener.ProgressNetListener;
import com.app.commonlib.net.parser.RespParser;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkAccessEngine {

    private static final NetWorkAccessEngine mNetWorkAccessEngine = new NetWorkAccessEngine();

    private OkHttpClient mHttpClient;
    private Handler mHandler;
    private String TAG = this.getClass().getSimpleName();

    private NetWorkAccessEngine() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static NetWorkAccessEngine getInstance() {
        return mNetWorkAccessEngine;
    }


    @NonNull
    private <T> Call execute(final RespParser<T> parser, final NetListener<T> listener, Request request) {
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Runnable runnable = null;
                if (response.isSuccessful()) {
                    try {
                        final T t = parser.parseResponse(response.body().string());
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onSuccess(t);
                                }
                            }
                        };
                    } catch (final JSONException e) {
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        };
                    }
                } else {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onFailure(response.message());
                            }
                        }
                    };
                }
                response.close();
                mHandler.post(runnable);
            }
        });
        return call;
    }


    /**
     * 发送get请求
     */
    public <T> Call get(final String url, Map<String, String> params, final RespParser<T> parser,
                        final NetListener<T> listener) {
        Request request = new Request.Builder().url(Tools.getFullUrl(url, params, false)).get().build();
        return execute(parser, listener, request);
    }

    /**
     * 发送post请求
     */
    public <T> Call post(final String url, Map<String, String> params, final RespParser<T> parser,
                         final NetListener<T> listener) {
        Request request = new Request.Builder().url(url).post(Tools.getFormRequest(params)).build();
        return execute(parser, listener, request);
    }

    /**
     * 上传文件
     *      当上传多个文件时,onProgress回调情况待定验证
     * @param url
     * @param params
     * @param parser
     * @param listener
     * @param <T>
     * @return
     */
    public <T> Call upLoadFile(final String url, Map<String, Object> params, final RespParser<T> parser,
                         final ProgressNetListener listener) {
        Request request = new Request.Builder().url(url).post(Tools.getMultipartRequest(params,listener)).build();
        return execute(parser, listener, request);
    }

    public <T> Call downLoadFile(final String url, File target, final RespParser<T> parser,
                                 final ProgressNetListener listener) {
        Request request = new Request.Builder().url(url).build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Runnable runnable = null;
                if (response.isSuccessful()) {
                    try {
                        final T t = parser.parseResponse(response.body().string());
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onSuccess(t);
                                }
                            }
                        };
                    } catch (final JSONException e) {
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        };
                    }
                } else {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onFailure(response.message());
                            }
                        }
                    };
                }
                response.close();
                mHandler.post(runnable);
            }
        });
        return call;
    }
   /* public String saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[1024];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            target.createNewFile();

            fos = new FileOutputStream(target);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                if (callback != null) {
                    publishProgress(sum, total);
                }
            }
            fos.flush();

            return target.getAbsolutePath();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }*/

}
