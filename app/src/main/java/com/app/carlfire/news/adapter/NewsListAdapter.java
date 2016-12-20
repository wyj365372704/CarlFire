package com.app.carlfire.news.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.carlfire.R;
import com.app.carlfire.base.BaseRecyclerViewAdapter;
import com.app.carlfire.beans.NewsBean;
import com.app.commonlib.img.ImageLoadEngine;

/**
 * Created by Carl on 2016-12-01 001.
 */

public class NewsListAdapter extends BaseRecyclerViewAdapter<NewsBean,NewsListAdapter.NewsViewHolder> {

    @Override
    protected NewsViewHolder bindViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(inflateItemView(parent, R.layout.item_news));
    }

    @Override
    protected void bindDataToItemView(NewsViewHolder viewHolder, NewsBean item) {
        ImageLoadEngine.loadImage(viewHolder.itemView.getContext(),item.getImgsrc(),viewHolder.mImageView);
        viewHolder.mTitleTextView.setText(item.getTitle());
        viewHolder.mDescTextView.setText(item.getDigest());
        viewHolder.mPublishTime.setText(item.getPtime());
    }

    @Override
    protected void setupItemViewListener(NewsViewHolder viewHolder, NewsBean item) {
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mDescTextView;
        private TextView mPublishTime;
        NewsViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.ivNews);
            mTitleTextView = (TextView) itemView.findViewById(R.id.tvTitle);
            mDescTextView = (TextView) itemView.findViewById(R.id.tvDesc);
            mPublishTime = (TextView) itemView.findViewById(R.id.tbPublishTime);
        }
    }
}
