package com.winsant.android.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.winsant.android.R;

import java.util.ArrayList;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> offerList;

    public OffersAdapter(Activity activity, ArrayList<String> offerList) {
        this.activity = activity;
        this.offerList = offerList;
    }

    public interface onClickListener {
        void onClick(int position, String name, String product_url);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView offersImage;

        public ViewHolder(final View itemView) {
            super(itemView);

            offersImage = (ImageView) itemView.findViewById(R.id.offersImage);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_offres_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        Glide
                .with(activity)
                .load(offerList.get(position))
                .asBitmap()
                .skipMemoryCache(true)
                .fitCenter()
                .placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(1024, 200) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.offersImage.setImageDrawable(null);
                        viewHolder.offersImage.setImageBitmap(resource);
                    }
                });

//        Glide
//                .with(activity)
//                .load(offerList.get(position))
//                .placeholder(R.drawable.no_image_available)
//                .into(viewHolder.offersImage);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.offersImage);
    }
}
