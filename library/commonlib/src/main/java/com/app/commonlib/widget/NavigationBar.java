package com.app.commonlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.commonlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl on 2016-12-08 008.
 */

public class NavigationBar extends LinearLayout {

    private float textSize;//导航条文本字体大小
    private int textSelectedColor;//导航条文本字体色号(选中时)
    private int textUnSelectedColor;//导航条文本字体色号(未选中时)
    private float iconWidth;//导航条图片宽度
    private float iconHeight;//导航条图片高度

    private List<TabEntity> mTabEntitys = new ArrayList<>();
    private ViewPager mViewPager;
    private String TAG = this.getClass().getSimpleName();

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        init();
        obtainAttributes(context, attrs);
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.navigation_bar);
        textSize = ta.getDimension(R.styleable.navigation_bar_textSize, getResources().getDimensionPixelSize(R.dimen.navigation_bar_textSize));
        textSelectedColor = ta.getColor(R.styleable.navigation_bar_textSelectedColor, ContextCompat.getColor(getContext(), R.color.navigation_bar_textSelectedColor));
        textUnSelectedColor = ta.getColor(R.styleable.navigation_bar_textUnSelectedColor, ContextCompat.getColor(getContext(), R.color.navigation_bar_textUnSelectedColor));
        iconWidth = ta.getDimension(R.styleable.navigation_bar_iconWidth, getResources().getDimension(R.dimen.navigation_bar_iconWidth));
        iconHeight = ta.getDimension(R.styleable.navigation_bar_iconHeight, getResources().getDimension(R.dimen.navigation_bar_iconHeight));

        ta.recycle();
    }

    private void inflateView() {
        for (int i = 0; i < mTabEntitys.size(); i++) {
            TabEntity tabEntity = mTabEntitys.get(i);

            LinearLayout tabItem = new LinearLayout(getContext());
            tabItem.setOrientation(LinearLayout.VERTICAL);
            tabItem.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(tabEntity.getUnSelectedIcon());
            imageView.setLayoutParams(new LayoutParams(iconWidth <= 0 ? LayoutParams.WRAP_CONTENT : (int) iconWidth,
                    iconHeight <= 0 ? LayoutParams.WRAP_CONTENT : (int) iconHeight));

            TextView textView = new TextView(getContext());
            textView.setText(tabEntity.getTitle());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setTextColor(textUnSelectedColor);
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            tabItem.addView(imageView);
            tabItem.addView(textView);


            /** 每一个Tab的布局参数 */
            LayoutParams lp_tab = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
            addView(tabItem, i, lp_tab);
        }
    }

    private void inflateListener() {
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            final int finalI = i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                  mViewPager.setCurrentItem(finalI,false);
                }
            });
        }
    }

    public static class TabEntity {
        private String title;
        private int selectedIcon;
        private int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getSelectedIcon() {
            return selectedIcon;
        }

        public void setSelectedIcon(int selectedIcon) {
            this.selectedIcon = selectedIcon;
        }

        public int getUnSelectedIcon() {
            return unSelectedIcon;
        }

        public void setUnSelectedIcon(int unSelectedIcon) {
            this.unSelectedIcon = unSelectedIcon;
        }
    }

    public void setupWithViewPager(ViewPager viewPager, List<TabEntity> entities) {
        this.mViewPager = viewPager;
        entities.removeAll(mTabEntitys);
        mTabEntitys.addAll(entities);
        inflateView();
        inflateListener();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setCurrentTab(viewPager.getCurrentItem());
    }

    /**
     * 设置当前选中菜单下标
     *
     * @param index
     */
    private void setCurrentTab(int index) {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout item = (LinearLayout) getChildAt(i);
            ImageView imageView = (ImageView) item.getChildAt(0);
            TextView textView = (TextView) item.getChildAt(1);
            if (i == index) {
                imageView.setImageResource(mTabEntitys.get(i).getSelectedIcon());
                textView.setTextColor(textSelectedColor);
            } else {
                imageView.setImageResource(mTabEntitys.get(i).getUnSelectedIcon());
                textView.setTextColor(textUnSelectedColor);
            }
        }
    }
}
