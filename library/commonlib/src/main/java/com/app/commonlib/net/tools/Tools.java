package com.app.commonlib.net.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

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
}
