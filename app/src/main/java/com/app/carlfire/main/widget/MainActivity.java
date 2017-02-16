package com.app.carlfire.main.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.carlfire.news.adapter.MyViewPagerAdapter;
import com.app.carlfire.news.widget.NewsFragment;
import com.app.carlfire.R;
import com.app.carlfire.main.mvpview.MainView;
import com.app.carlfire.main.presenter.MainPresenter;
import com.app.carlfire.photo.widget.PhotoFragment;
import com.app.commonlib.base.BaseActivity;
import com.app.commonlib.widget.NavigationBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    @BindView(R.id.main_content)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    NavigationBar mTableLayout;

    private MyViewPagerAdapter myViewPagerAdapter;

    private int navBarHeight;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter setPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void initWidgets(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        myViewPagerAdapter.addFragment(NewsFragment.newInstance());
        myViewPagerAdapter.addFragment(PhotoFragment.newInstance());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(myViewPagerAdapter);
        List<NavigationBar.TabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new NavigationBar.TabEntity("首页", R.mipmap.ic_home_selected, R.mipmap.ic_home_normal));
        tabEntities.add(new NavigationBar.TabEntity("美女", R.mipmap.ic_girl_selected, R.mipmap.ic_girl_normal));
        mTableLayout.setupWithViewPager(mViewPager, tabEntities);
        mTableLayout.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        navBarHeight = mTableLayout.getMeasuredHeight();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("navigation_index", mTableLayout.getCurrentTabIndex());
    }

    /**
     * EventBus 回调,ScrollAwareFABBehavior为调用者
     * @param event
     */
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showOrHideNavigationBar(Boolean event) {
        mTableLayout.clearAnimation();
        final ViewGroup.LayoutParams layoutParams = mTableLayout.getLayoutParams();
        ValueAnimator valueAnimator;
        ObjectAnimator alpha;
        if (!event) {
            valueAnimator = ValueAnimator.ofInt(navBarHeight, 0);
            alpha = new ObjectAnimator().ofFloat(mTableLayout, "alpha", 1, 0);
        } else {
            valueAnimator = ValueAnimator.ofInt(0, navBarHeight);
            alpha = new ObjectAnimator().ofFloat(mTableLayout, "alpha", 0, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (!mTableLayout.isInLayout()){
                        mTableLayout.setLayoutParams(layoutParams);
                    }
                }
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setTarget(mTableLayout);
        animatorSet.playTogether(valueAnimator, alpha);
        animatorSet.start();
    }
}
