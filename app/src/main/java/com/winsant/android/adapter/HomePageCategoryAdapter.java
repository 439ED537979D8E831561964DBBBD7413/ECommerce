package com.winsant.android.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.winsant.android.R;
import com.winsant.android.model.HomeProductModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class HomePageCategoryAdapter extends RecyclerView.Adapter<HomePageCategoryAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<HomeProductModel> itemsCategory;
    private onClickListener clickListener;
    private String tag;

    public HomePageCategoryAdapter(Activity activity, ArrayList<HomeProductModel> itemsCategory, onClickListener clickListener
            , String tag) {
        this.activity = activity;
        this.itemsCategory = itemsCategory;
        this.clickListener = clickListener;
        this.tag = tag;
    }

    public interface onClickListener {
        void onClick(int position, String name, String product_url);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, outStockImage;
        TextView txtName, txtDiscount, txtPrice, txtDiscountPrice;

        public ViewHolder(final View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            outStockImage = (ImageView) itemView.findViewById(R.id.outStockImage);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDiscount = (TextView) itemView.findViewById(R.id.txtDiscount);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtDiscountPrice = (TextView) itemView.findViewById(R.id.txtDiscountPrice);

            txtName.setTypeface(CommonDataUtility.setTypeFace1(activity));
            txtDiscount.setTypeface(CommonDataUtility.setTypeFace1(activity));
            txtPrice.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtDiscountPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);


            if (activity.getResources().getBoolean(R.bool.isTablet) || activity.getResources().getBoolean(R.bool.isLargeTablet)) {

                txtDiscount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            } else {

                txtDiscount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onClick(getAdapterPosition(), itemsCategory.get(getAdapterPosition()).getName(),
                                itemsCategory.get(getAdapterPosition()).getProduct_url());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v;
        if (tag.equals("v")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_product_v_item, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_product_g_item, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        HomeProductModel categoryProductModel = itemsCategory.get(position);

        Glide
                .with(activity)
                .load(categoryProductModel.getProduct_image())
                .asBitmap().skipMemoryCache(true)

                .fitCenter()
                .placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.productImage.setImageBitmap(null);
                        viewHolder.productImage.setImageBitmap(resource);
                    }
                });

        viewHolder.txtName.setText(categoryProductModel.getName());

        if (categoryProductModel.getDiscount_per().equals("100")) {
            viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + categoryProductModel.getPrice().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setGravity(Gravity.CENTER);
            viewHolder.txtPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
            viewHolder.txtDiscountPrice.setVisibility(View.GONE);
            viewHolder.txtDiscount.setVisibility(View.GONE);
        } else {
            viewHolder.txtDiscountPrice.setText(activity.getResources().getString(R.string.Rs) + " " + categoryProductModel.getDiscount_price().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + categoryProductModel.getPrice().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setPaintFlags(viewHolder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.txtDiscount.setText(String.format("%s %% OFF", categoryProductModel.getDiscount_per()));
        }
    }

    @Override
    public int getItemCount() {
        return itemsCategory.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.productImage);
    }
}
