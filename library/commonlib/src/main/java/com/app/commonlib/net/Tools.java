package com.app.commonlib.net;

import com.app.commonlib.net.listener.ProgressNetListener;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class Tools {
    /**
     * 参数列表追加至url,并返回新的url
     *
     * @param url        原url,形如http://www.demo.com ,不含参数列表,不含参数连接符"?"
     *                   非法url示例
     *                   http://www.demo.com?key=123
     *                   http://www.demo.com?
     * @param params     参数列表集合,若不含参数可null
     * @param urlEncoder 当为true时,参数列表进行UTF-8编码
     * @return 追加参数列表后的url, 形如http://www.demo.com?key=123&pwd=abc
     */
    protected static String getFullUrl(String url, Map<String, String> params, boolean urlEncoder) {
        StringBuilder urlFull = new StringBuilder();
        urlFull.append(url);
        if (params != null && params.size() > 0) {
            urlFull.append("?");
            for (Map.Entry<String, String> param : params.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();
                if (urlEncoder) {//只对key和value编码
                    try {
                        key = URLEncoder.encode(key, "UTF-8");
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                urlFull.append(key).append("=").append(value).append("&");
            }
            urlFull.deleteCharAt(urlFull.length() - 1);
        }
        return urlFull.toString();
    }

    /**
     * @param params
     * @return
     */
    protected static RequestBody getFormRequest(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();
                builder.add(key, value);
            }
        }
        return builder.build();
    }

    protected static RequestBody getMultipartRequest(Map<String, Object> params, final ProgressNetListener listener) {
        final MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                String key = param.getKey();
                Object value = param.getValue();
                if (!(value instanceof File)) {
                    builder.addFormDataPart(key, value.toString());
                } else {
                    final File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), new RequestBody() {
                        @Override
                        public MediaType contentType() {
                            return MultipartBody.FORM;
                        }

                        @Override
                        public void writeTo(BufferedSink sink) throws IOException {
                            Source source ;
                            try {
                                source = Okio.source(file);
                                Buffer buffer = new Buffer();
                                long current = 0;
                                for (long readCount; (readCount = source.read(buffer, 2048)) != -1; ) {
                                    sink.write(buffer, readCount);
                                    current += readCount;
                                    if(listener!=null){
                                        listener.onProgress(file.getName(),current,file.length());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
        return builder.build();
    }
}
