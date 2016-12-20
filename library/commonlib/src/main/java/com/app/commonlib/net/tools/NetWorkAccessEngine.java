package com.app.commonlib.net.tools;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.app.commonlib.net.parser.RespParser;

import org.json.JSONException;

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
    private String TAG = NetWorkAccessEngine.class.getSimpleName();

    private NetWorkAccessEngine() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static NetWorkAccessEngine getInstance() {
        return mNetWorkAccessEngine;
    }

    /**
     * 发送get请求
     */
    public <T> Call get(final String url, Map<String, String> params, final RespParser<T> parser,
                        final NetListener<T> listener) {
        Request request = new Request.Builder().url(Tools.getFullUrl(url, params, false)).get().build();
        Log.d("url", url);
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
}
