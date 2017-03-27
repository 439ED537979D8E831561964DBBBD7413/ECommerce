package com.winsant.android.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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

public class WishListProductAdapter extends RecyclerView.Adapter<WishListProductAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<HomeProductModel> viewAllProductList;
    private onClickListener clickListener;

    public WishListProductAdapter(Activity activity, ArrayList<HomeProductModel> viewAllProductList, onClickListener clickListener) {
        this.activity = activity;
        this.viewAllProductList = viewAllProductList;
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onClick(int position, String product_id, String product_url);

        void onDeleteClick(int position, String product_id, String remove_link);
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
                        clickListener.onClick(getAdapterPosition(), viewAllProductList.get(getAdapterPosition()).getProduct_id(),
                                viewAllProductList.get(getAdapterPosition()).getProduct_url());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_product_v_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        HomeProductModel viewAllProductModel = viewAllProductList.get(position);

        Glide
                .with(activity)
                .load(viewAllProductModel.getProduct_image())
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

        viewHolder.txtName.setText(viewAllProductModel.getName());

        viewHolder.imgWishList.setVisibility(View.VISIBLE);
        viewHolder.imgWishList.setImageResource(R.drawable.ico_delete_svg);
        viewHolder.imgWishList.setTag(position);
        viewHolder.imgWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();

                if (clickListener != null)
                    clickListener.onDeleteClick(pos, viewAllProductList.get(pos).getProduct_id(), viewAllProductList.get(pos).getRemove_url());
            }
        });

        viewHolder.outStockImage.setVisibility(viewAllProductModel.getAvailability().equals("0") ? View.VISIBLE : View.GONE);

        if (viewAllProductModel.getDiscount_per().equals("100")) {
            viewHolder.txtDiscountPrice.setText(activity.getResources().getString(R.string.Rs) + " " + viewAllProductModel.getPrice().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setVisibility(View.GONE);
            viewHolder.txtDiscount.setVisibility(View.GONE);
        } else {
            viewHolder.txtDiscountPrice.setText(activity.getResources().getString(R.string.Rs) + " " + viewAllProductModel.getDiscount_price().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + viewAllProductModel.getPrice().replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setPaintFlags(viewHolder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.txtDiscount.setText(String.format("%s %% OFF", viewAllProductModel.getDiscount_per()));
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
