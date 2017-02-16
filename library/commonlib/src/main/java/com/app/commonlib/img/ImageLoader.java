package com.app.commonlib.img;

import android.content.Context;
import android.widget.ImageView;

import com.app.commonlib.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Carl on 2016-12-09 009.
 */

public class ImageLoader {
    public static void loadImage(Context context, String url, ImageView targetImageView) {
        loadImage(context, url, targetImageView, R.mipmap.ic_image_loading, R.mipmap.ic_empty_picture);
    }

    public static void loadImage(Context context, String url, ImageView targetImageView, int placeImageResource, int errorImageResource) {
        Glide.with(context).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeImageResource)
                .error(errorImageResource)
                .into(targetImageView);
    }
}
