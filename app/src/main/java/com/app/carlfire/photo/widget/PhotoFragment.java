package com.app.carlfire.photo.widget;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.app.carlfire.R;
import com.app.carlfire.beans.PhotoGirl;
import com.app.carlfire.photo.adapter.PhotoAdapter;
import com.app.carlfire.photo.mvpview.PhotoView;
import com.app.carlfire.photo.presenter.PhotoPresenter;
import com.app.commonlib.base.BaseFragment;
import com.app.commonlib.base.PowerfulRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Carl on 2016-12-08 008.
 */

public class PhotoFragment extends BaseFragment<PhotoPresenter> implements PhotoView, PowerfulRecyclerView.OnLoadMoreListener, PowerfulRecyclerView.OnRefreshListener {
    @BindView(R.id.photo_list_rv)
    PowerfulRecyclerView mRecyclerView;

    private PhotoAdapter mPhotoAdapter;

    public static PhotoFragment newInstance() {

        Bundle args = new Bundle();

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentViewResId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected PhotoPresenter setPresenter() {
        return new PhotoPresenter(this);
    }

    @Override
    protected void initWidgets(Bundle savedInstanceState) {
        mPhotoAdapter = new PhotoAdapter();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mPhotoAdapter);
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
        if(visible &&mPhotoAdapter.getItemCount() == 0){
            mPresenter.loadPhoto();
        }
    }

    @Override
    public void refreshList(List<PhotoGirl> photoGirls) {
        mPhotoAdapter.addItems(photoGirls);
    }

    @Override
    public void loadOrRefreshCompleted() {
        mRecyclerView.refreshCompleted();
        mRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadOrRefreshFailed(String message) {
        Toast.makeText(getContext(), "加载失败:" + message, Toast.LENGTH_SHORT).show();
        mRecyclerView.refreshCompleted();
        mRecyclerView.loadMoreCompleted();
    }

    @Override
    public void noMoreShow() {
        Toast.makeText(getContext(), "没有更多了~", Toast.LENGTH_SHORT).show();
        mRecyclerView.noMoreMessageToLoad();
    }

    @OnClick(R.id.fab)
    @Override
    public void notifyListScroll2Top() {
        mRecyclerView.scrollToPosition(0, false);
    }


    @Override
    public void onRefresh() {
        mPresenter.refreshPhoto();
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadPhoto();
    }
}
