package com.app.carlfire.news.widget;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.app.carlfire.R;
import com.app.carlfire.main.mvpview.MainView;
import com.app.carlfire.main.widget.MainActivity;
import com.app.carlfire.news.adapter.MyViewPagerAdapter;
import com.app.carlfire.news.mvpview.NewsListView;
import com.app.carlfire.news.mvpview.NewsView;
import com.app.carlfire.news.presenter.NewsPresenter;
import com.app.commonlib.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.internal.ListenerMethod;


public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsView {
    @BindView(R.id.news_vp)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    private MyViewPagerAdapter myViewPagerAdapter;
    private String TAG = this.getClass().getSimpleName();

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentViewResId() {
        return R.layout.fragment_news;
    }

    @Override
    protected NewsPresenter setPresenter() {
        return new NewsPresenter(this);
    }

    @Override
    protected void initWidgets(Bundle savedInstanceState) {
        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        myViewPagerAdapter.addFragment(NewsListFragment.newInstance(getString(R.string.host), R.string.host));
        myViewPagerAdapter.addFragment(NewsListFragment.newInstance(getString(R.string.nba), R.string.nba));
        myViewPagerAdapter.addFragment(NewsListFragment.newInstance(getString(R.string.car), R.string.car));
        myViewPagerAdapter.addFragment(NewsListFragment.newInstance(getString(R.string.joke), R.string.joke));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(myViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    @OnClick(R.id.fab)
    public void notifyListScroll2Top() {
        ((NewsListView) myViewPagerAdapter.getItem(mViewPager.getCurrentItem())).scroll2Item(0);
    }
}
