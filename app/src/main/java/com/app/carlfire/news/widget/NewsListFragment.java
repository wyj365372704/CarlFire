package com.app.carlfire.news.widget;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.app.carlfire.R;
import com.app.carlfire.beans.NewsBean;
import com.app.carlfire.news.adapter.NewsListAdapter;
import com.app.carlfire.news.mvpview.NewsListView;
import com.app.carlfire.news.presenter.NewsListPresenter;
import com.app.commonlib.base.BaseFragment;
import com.app.commonlib.base.PowerfulRecyclerView;

import java.util.List;

import butterknife.BindView;

public class NewsListFragment extends BaseFragment<NewsListPresenter> implements NewsListView,  PowerfulRecyclerView.OnRefreshListener, PowerfulRecyclerView.OnLoadMoreListener {
    @BindView(R.id.news_list_rv)
    PowerfulRecyclerView mRecyclerView;

    private NewsListAdapter mNewsListAdapter;
    private String TAG = this.getClass().getSimpleName();


    public static NewsListFragment newInstance(String title, int type) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("type", type);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentViewResId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected NewsListPresenter setPresenter() {
        return new NewsListPresenter(this, getArguments().getInt("type"));
    }

    @Override
    protected void initWidgets(Bundle savedInstanceState) {
        mNewsListAdapter = new NewsListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mNewsListAdapter);
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary),
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.nav_bar));
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean visible) {
        super.onFragmentVisibleChange(visible);
        if(visible &&mNewsListAdapter.getItemCount() == 0){
            mPresenter.loadNews();
        }
    }

    @Override
    public void refreshList(List<NewsBean> newsList) {
        mNewsListAdapter.addItems(newsList);
    }

    @Override
    public void loadOrRefreshCompleted() {
        mRecyclerView.refreshCompleted();
        mRecyclerView.loadMoreCompleted();
    }

    @Override
    public void showLoadFailMsg(String message) {
        Toast.makeText(getContext(), "加载失败:" + message, Toast.LENGTH_SHORT).show();
        mRecyclerView.refreshCompleted();
        mRecyclerView.loadMoreCompleted();
    }

    @Override
    public void scroll2Item(int index) {
        mRecyclerView.scrollToPosition(index,false);
    }

    @Override
    public void noMoreShow() {
        Toast.makeText(getContext(), "没有更多了~", Toast.LENGTH_SHORT).show();
        mRecyclerView.noMoreMessageToLoad();
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshNews();
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadNews();
    }
}
