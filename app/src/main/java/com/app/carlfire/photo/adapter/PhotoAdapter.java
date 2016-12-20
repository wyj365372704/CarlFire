package com.app.carlfire.photo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.carlfire.R;
import com.app.carlfire.base.BaseRecyclerViewAdapter;
import com.app.carlfire.beans.PhotoGirl;
import com.app.commonlib.img.ImageLoadEngine;

/**
 * Created by Carl on 2016-12-08 008.
 */

public class PhotoAdapter extends BaseRecyclerViewAdapter<PhotoGirl, PhotoAdapter.PhotoViewHolder> {
    @Override
    protected void bindDataToItemView(PhotoViewHolder viewHolder, PhotoGirl item) {
        ImageLoadEngine.loadImage(viewHolder.itemView.getContext(), item.getUrl(), viewHolder.imageView);
    }

    @Override
    protected void setupItemViewListener(PhotoViewHolder viewHolder, PhotoGirl item) {

    }

    @Override
    protected PhotoViewHolder bindViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(inflateItemView(parent, R.layout.item_photo));
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }
}
