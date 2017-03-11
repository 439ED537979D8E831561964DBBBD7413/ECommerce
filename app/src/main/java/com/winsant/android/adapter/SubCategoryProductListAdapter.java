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

public class SubCategoryProductListAdapter extends RecyclerView.Adapter<SubCategoryProductListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<HomeProductModel> itemsCategory;
    private onClickListener clickListener;
    private String tag;

    public SubCategoryProductListAdapter(Activity activity, ArrayList<HomeProductModel> itemsCategory, onClickListener clickListener
            , String tag) {
        this.activity = activity;
        this.itemsCategory = itemsCategory;
        this.clickListener = clickListener;
        this.tag = tag;
    }

    public interface onClickListener {
        void onClick(int position, String name, String product_url);

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
            txtPrice.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtDiscountPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

            txtDiscount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

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
        viewHolder.outStockImage.setVisibility(categoryProductModel.getAvailability().equals("0") ? View.VISIBLE : View.GONE);

        viewHolder.imgWishList.setVisibility(View.GONE);

//        if (categoryProductModel.getIsFavorite().equals("1"))
//            viewHolder.imgWishList.setImageResource(R.drawable.ico_wishlist_selected_svg);
//        else viewHolder.imgWishList.setImageResource(R.drawable.ico_wishlist_normal_svg);
//
//        viewHolder.imgWishList.setTag(position);
//
//        viewHolder.imgWishList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
//                    int pos = (int) v.getTag();
//
//                    if (clickListener != null) {
//                        if (itemsCategory.get(pos).getIsFavorite().equals("1")) {
//                            clickListener.onFavClick(pos, itemsCategory.get(pos).getProduct_id(),
//                                    itemsCategory.get(pos).getRemove_url(), itemsCategory.get(pos).getIsFavorite());
//                        } else {
//                            clickListener.onFavClick(pos, itemsCategory.get(pos).getProduct_id(),
//                                    itemsCategory.get(pos).getFav_url(), itemsCategory.get(pos).getIsFavorite());
//                        }
//                    }
//                } else {
//                    Toast.makeText(activity, "Please login first to add product in wishlist", Toast.LENGTH_SHORT).show();
//                    activity.startActivity(new Intent(activity, LoginActivity.class));
//                }
//            }
//        });

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
