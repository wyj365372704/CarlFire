package com.app.carlfire.news.model;

import com.app.carlfire.base.BaseMvpModel;
import com.app.carlfire.beans.NewsBean;
import com.app.carlfire.net.parser.NewsParser;
import com.app.commonlib.net.tools.NetListener;
import com.app.commonlib.net.tools.NetWorkAccessEngine;

import java.util.List;

/**
 * Created by Carl on 2016-12-01 001.
 */

public class NewsListModel implements BaseMvpModel {
    public void loadNewsList(String url, String type, final OnLoadNewsListListener listener) {
        NetWorkAccessEngine.getInstance().get(url, null, new NewsParser(type), new NetListener<List<NewsBean>>() {
            @Override
            public void onSuccess(List<NewsBean> result) {
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface OnLoadNewsListListener {
        void onSuccess(List<NewsBean> list);

        void onFailure(String msg);
    }
}
