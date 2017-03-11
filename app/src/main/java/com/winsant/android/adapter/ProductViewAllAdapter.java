package com.winsant.android.adapter;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.winsant.android.R;
import com.winsant.android.model.HomeProductModel;
import com.winsant.android.ui.LoginActivity;
import com.winsant.android.ui.MyApplication;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class ProductViewAllAdapter extends RecyclerView.Adapter<ProductViewAllAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<HomeProductModel> viewAllProductList;
    private onClickListener clickListener;
    private String tag;

    public ProductViewAllAdapter(Activity activity, ArrayList<HomeProductModel> viewAllProductList, onClickListener clickListener, String tag) {
        this.activity = activity;
        this.viewAllProductList = viewAllProductList;
        this.clickListener = clickListener;
        this.tag = tag;
    }

    public interface onClickListener {
        void onClick(int position, String product_id, String product_url);

        void onFavClick(int position, String product_id, String fav_link, String isFavorite);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, imgWishList, outStockImage;
        TextView txtName, txtDiscount, txtPrice, txtDiscountPrice;

        public ViewHolder(final View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            outStockImage = (ImageView) itemView.findViewById(R.id.outStockImage);
            imgWishList = (ImageView) itemView.findViewById(R.id.imgWishList);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDiscount = (TextView) itemView.findViewById(R.id.txtDiscount);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtDiscountPrice = (TextView) itemView.findViewById(R.id.txtDiscountPrice);

            txtName.setTypeface(CommonDataUtility.setTypeFace1(activity));
            txtDiscount.setTypeface(CommonDataUtility.setTypeFace1(activity));
            txtDiscountPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

            txtDiscount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onClick(getAdapterPosition(), viewAllProductList.get(getAdapterPosition()).getProduct_id(),
                                viewAllProductList.get(getAdapterPosition()).getProduct_url());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        if (tag.equals("v")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_product_v_item, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_product_g_item, viewGroup, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        HomeProductModel viewAllProductModel = viewAllProductList.get(position);

        Glide
                .with(activity)
                .load(viewAllProductModel.getProduct_image())
                .asBitmap().skipMemoryCache(true)

                .fitCenter()
                .into(new SimpleTarget<Bitmap>(220, 220) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.productImage.setImageBitmap(null);
                        viewHolder.productImage.setImageBitmap(resource);
                    }
                });

        viewHolder.txtName.setText(viewAllProductModel.getName());
        viewHolder.outStockImage.setVisibility(viewAllProductModel.getAvailability().equals("0") ? View.VISIBLE : View.GONE);

        viewHolder.imgWishList.setVisibility(View.VISIBLE);

        if (MyApplication.getInstance().getPreferenceUtility().getLogin())
            if (viewAllProductModel.getIsFavorite().equals("1"))
                viewHolder.imgWishList.setImageResource(R.drawable.ico_wishlist_selected_svg);
            else viewHolder.imgWishList.setImageResource(R.drawable.ico_wishlist_normal_svg);
        else
            viewHolder.imgWishList.setImageResource(R.drawable.ico_wishlist_normal_svg);

        viewHolder.imgWishList.setTag(position);

        viewHolder.imgWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
                    int pos = (int) v.getTag();

                    if (clickListener != null) {
                        if (viewAllProductList.get(pos).getIsFavorite().equals("1")) {
                            clickListener.onFavClick(pos, viewAllProductList.get(pos).getProduct_id(),
                                    viewAllProductList.get(pos).getRemove_url(), viewAllProductList.get(pos).getIsFavorite());
                        } else {
                            clickListener.onFavClick(pos, viewAllProductList.get(pos).getProduct_id(),
                                    viewAllProductList.get(pos).getFav_url(), viewAllProductList.get(pos).getIsFavorite());
                        }
                    }
                } else {
                    Toast.makeText(activity, "Please login first to add product in wishlist", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
            }
        });

        if (viewAllProductModel.getDiscount_per().equals("100")) {
            viewHolder.txtDiscountPrice.setVisibility(View.GONE);
            viewHolder.txtDiscount.setVisibility(View.GONE);

            viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + viewAllProductModel.getPrice().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setGravity(Gravity.CENTER);
            viewHolder.txtPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
            viewHolder.txtPrice.setPaintFlags(0);

        } else {

            viewHolder.txtDiscountPrice.setVisibility(View.VISIBLE);
            viewHolder.txtDiscountPrice.setText(activity.getResources().getString(R.string.Rs) + " " + viewAllProductModel.getDiscount_price().replaceAll("\\.0*$", ""));
            viewHolder.txtDiscount.setVisibility(View.VISIBLE);
            viewHolder.txtDiscount.setText(String.format("%s %% OFF", viewAllProductModel.getDiscount_per()));

            viewHolder.txtPrice.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            viewHolder.txtPrice.setGravity(Gravity.CENTER | Gravity.START);
            viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + viewAllProductModel.getPrice().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setPaintFlags(viewHolder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return viewAllProductList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.productImage);
    }
}
