package com.app.carlfire.news.presenter;

import com.app.carlfire.R;
import com.app.carlfire.base.BaseMvpPresenter;
import com.app.carlfire.beans.NewsBean;
import com.app.carlfire.news.model.NewsListModel;
import com.app.carlfire.net.Urls;
import com.app.carlfire.news.mvpview.NewsListView;

import java.util.List;

public class NewsListPresenter extends BaseMvpPresenter<NewsListView, NewsListModel> {
    private int pageIndex = 0;//当前页码
    private int mType;//新闻页面的类型

    public NewsListPresenter(NewsListView mvpView, int mType) {
        super(mvpView);
        this.mType = mType;
    }

    /**
     * 重新获取新闻列表
     */
    public void refreshNews() {
        getNews(0);
    }

    /**
     * 加载更多新闻
     */
    public void loadNews() {
        getNews(pageIndex);
    }

    /**
     * 获取新闻
     *
     * @param index 起始页号[0,+]
     */
    private void getNews(final int index) {
        mModel.loadNewsList(getUrl(index), getID(), new NewsListModel.OnLoadNewsListListener() {
            @Override
            public void onSuccess(List<NewsBean> list) {
                if(list.size() == 0){
                    mView.noMoreShow();
                }else{
                    mView.refreshList(list);
                    mView.loadOrRefreshCompleted();
                    if (index >= pageIndex) {
                        pageIndex += Urls.PAZE_SIZE;
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showLoadFailMsg(msg);
                mView.loadOrRefreshCompleted();
            }
        });
    }

    private String getUrl(int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (mType) {
            case R.string.host:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case R.string.nba:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case R.string.car:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case R.string.joke:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }

    private String getID() {
        String id;
        switch (mType) {
            case R.string.host:
                id = Urls.TOP_ID;
                break;
            case R.string.nba:
                id = Urls.NBA_ID;
                break;
            case R.string.car:
                id = Urls.CAR_ID;
                break;
            case R.string.joke:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }

    @Override
    protected NewsListModel setModel() {
        return new NewsListModel();
    }
}
