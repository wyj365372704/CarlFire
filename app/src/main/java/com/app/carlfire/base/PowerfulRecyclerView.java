package com.app.carlfire.base;


import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.carlfire.R;

/**
 * Created by Carl on 2016-12-11 011.
 */

public class PowerfulRecyclerView extends FrameLayout {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;

    private boolean isRefreshing = false;
    private boolean isLoadMoreing = false;
    private boolean hasMoreMessage = true;

    private OnRefreshListener onRefreshListener;
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public PowerfulRecyclerView(Context context) {
        this(context, null);
    }

    public PowerfulRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PowerfulRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_recycler, this, false);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.power_recycle_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.container);
        addView(inflate, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 通知UI执行刷新操作
     */
    private void notifyRefresh() {
        if (isRefreshing || isLoadMoreing)
            return;
        isRefreshing = true;
        if (null != onRefreshListener) {
            onRefreshListener.onRefresh();
        }
    }

    /**
     * 通知UI执行加载更多操作
     */
    private void notifyLoadMore() {
        if ((isRefreshing || isLoadMoreing || !hasMoreMessage))
            return;
        isLoadMoreing = true;
        ((BaseRecyclerViewAdapter) mRecyclerView.getAdapter()).notifyLoadMoreState(BaseRecyclerViewAdapter.STATE_LOADING);
        if (null != onLoadMoreListener) {
            onLoadMoreListener.onLoadMore();
        }
    }

    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(final RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * 允许下拉刷新开关
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
        if (enable) {
            mSwipeRefreshLayout.setOnRefreshListener(new PullToRefreshListener());
        } else {
            mSwipeRefreshLayout.setOnRefreshListener(null);
        }
    }

    /**
     * 允许上拉加载更多开关
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        ((BaseRecyclerViewAdapter) mRecyclerView.getAdapter()).setEnableLoadMore(enable);
        if (enable) {
            mRecyclerView.addOnScrollListener(new LoadMoreScrollListener());
        } else {
            mRecyclerView.clearOnScrollListeners();
        }
    }

    /**
     * 设置下拉刷新环颜色
     * @param colors
     */
    public void setColorSchemeColors(@ColorInt int... colors) {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    /**
     * 刷新完成通知接口
     */
    public void refreshCompleted() {
        isRefreshing = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 加载更多完成通知接口
     */
    public void loadMoreCompleted() {
        isLoadMoreing = false;
        ((BaseRecyclerViewAdapter) mRecyclerView.getAdapter()).notifyLoadMoreState(BaseRecyclerViewAdapter.STATE_NORMAL);
    }

    /**
     * 没有更多可加载通知接口
     */
    public void noMoreMessageToLoad(){
        isLoadMoreing = false;
        hasMoreMessage = false;
        ((BaseRecyclerViewAdapter) mRecyclerView.getAdapter()).notifyLoadMoreState(BaseRecyclerViewAdapter.STATE_LASTED);
    }

    /**
     * 通知列表滑动至指定位置
     * @param position
     * @param smooth 是否平滑
     */
    public void scrollToPosition(int position, boolean smooth) {
        if (smooth) {
            mRecyclerView.smoothScrollToPosition(position);
        } else {
            mRecyclerView.scrollToPosition(position);
        }
    }

    public void setHasFixedSize(boolean hasFixedSize){
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    private class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
        private int lastCompletelyVisibleItemPosition;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int[] temp = ((StaggeredGridLayoutManager) manager).findLastCompletelyVisibleItemPositions(null);
                lastCompletelyVisibleItemPosition = temp[temp.length - 1];
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastCompletelyVisibleItemPosition + 1 == recyclerView.getAdapter().getItemCount()) {
                notifyLoadMore();
            }
        }
    }

    private class PullToRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            notifyRefresh();
        }
    }
}

