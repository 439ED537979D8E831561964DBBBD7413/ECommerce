package com.winsant.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.winsant.android.R;
import com.winsant.android.ui.FullScreenViewActivity;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public ImageSliderAdapter(Activity activity,
                              ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final ImageView imgDisplay;
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);

        Glide
                .with(_activity)
                .load(_imagePaths.get(position))
                .asBitmap().skipMemoryCache(true)

                .fitCenter()
                .placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(1024, 400) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgDisplay.setImageDrawable(null);
                        imgDisplay.setImageBitmap(resource);
                    }
                });

//        Zoomy.Builder builder = new Zoomy.Builder(_activity)
//                .target(imgDisplay)
//                .interpolator(new OvershootInterpolator());
//        builder.register();

        container.addView(viewLayout);

        imgDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, FullScreenViewActivity.class);
                intent.putStringArrayListExtra("images", _imagePaths);
                _activity.startActivity(intent);

            }
        });

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.gc();
        container.removeView((RelativeLayout) object);
    }
}
