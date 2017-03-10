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
import com.winsant.android.model.CartModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<CartModel> cartProductArrayList;
    private onClickListener clickListener;
//    private int TotalPrice = 0;
//    private TextView txtTotalPrice;

    public CartAdapter(Activity activity, ArrayList<CartModel> cartProductArrayList, onClickListener clickListener) {
        this.activity = activity;
        this.cartProductArrayList = cartProductArrayList;
        this.clickListener = clickListener;
//        this.txtTotalPrice = txtTotalPrice;
//        TotalPrice = 0;
    }

    public interface onClickListener {

        void onRemoveClick(int position, String remove_url, String product_id);

        void onQtyClick(int position, String quantity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView txtName, txtDiscount, txtPrice, txtDiscountPrice, txtQty, txtRemove, txtColor, txtSize;

        public ViewHolder(final View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDiscount = (TextView) itemView.findViewById(R.id.txtDiscount);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtDiscountPrice = (TextView) itemView.findViewById(R.id.txtDiscountPrice);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            txtRemove = (TextView) itemView.findViewById(R.id.txtRemove);
            txtColor = (TextView) itemView.findViewById(R.id.txtColor);
            txtSize = (TextView) itemView.findViewById(R.id.txtSize);

            txtName.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.NORMAL);
            txtDiscountPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
            txtPrice.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtDiscount.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtQty.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.BOLD);
            txtRemove.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtColor.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtSize.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);

            txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtQty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            txtRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtDiscount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtColor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cart_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        CartModel cartModel = cartProductArrayList.get(position);

//        Picasso.with(activity).load(cartModel.getProduct_image()).placeholder(R.drawable.no_image_available).resize(120, 120).into(viewHolder.productImage);

        viewHolder.productImage.setAdjustViewBounds(true);

        Glide
                .with(activity)
                .load(cartModel.getProduct_image())
                .asBitmap()
                .skipMemoryCache(true)
                .fitCenter()
                .placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(120, 120) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.productImage.setImageDrawable(null);
                        viewHolder.productImage.setImageBitmap(resource);
                    }
                });

        if (!cartModel.getColor_name().equals("")) {
            viewHolder.txtColor.setVisibility(View.VISIBLE);
            viewHolder.txtColor.setText(String.format("Color : %s", cartModel.getColor_name()));
        } else
            viewHolder.txtColor.setVisibility(View.GONE);

        if (!cartModel.getSize_name().equals("")) {
            viewHolder.txtSize.setVisibility(View.VISIBLE);
            viewHolder.txtSize.setText(String.format("Size : %s", cartModel.getSize_name()));
        } else
            viewHolder.txtSize.setVisibility(View.GONE);

        viewHolder.txtName.setText(cartModel.getName());

        viewHolder.txtQty.setText("Qty : " + cartModel.getQty());
        viewHolder.txtQty.setTag(position);
        viewHolder.txtQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                if (clickListener != null)
                    clickListener.onQtyClick(pos, cartProductArrayList.get(pos).getAvailability());
            }
        });

        viewHolder.txtRemove.setTag(position);
        viewHolder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                if (clickListener != null)
                    clickListener.onRemoveClick(pos, cartProductArrayList.get(pos).getRemove_url(), cartProductArrayList.get(pos).getProduct_id());
            }
        });

        int Price, DisPrice;
        int qty = (int) Double.parseDouble(cartModel.getQty());

        if (cartModel.getDiscount_per().equals("100")) {

            Price = (int) Double.parseDouble(cartModel.getPrice()) * qty;

            viewHolder.txtDiscountPrice.setText(activity.getResources().getString(R.string.Rs) + " " + String.valueOf(Price).replaceAll("\\.0*$", ""));
            viewHolder.txtDiscountPrice.setGravity(Gravity.CENTER);
            viewHolder.txtDiscountPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

            viewHolder.txtPrice.setVisibility(View.GONE);
            viewHolder.txtDiscount.setVisibility(View.GONE);
            viewHolder.txtPrice.setPaintFlags(0);

        } else {

            Price = (int) Double.parseDouble(cartModel.getPrice()) * qty;
            DisPrice = (int) Double.parseDouble(cartModel.getDiscount_price()) * qty;

            viewHolder.txtDiscountPrice.setText(activity.getResources().getString(R.string.Rs) + " " + String.valueOf(DisPrice).replaceAll("\\.0*$", ""));

            viewHolder.txtPrice.setVisibility(View.VISIBLE);
            viewHolder.txtDiscount.setVisibility(View.VISIBLE);
            viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + String.valueOf(Price).replaceAll("\\.0*$", ""));
            viewHolder.txtPrice.setPaintFlags(viewHolder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.txtDiscount.setText(String.format("%s %% OFF", cartModel.getDiscount_per()));
        }
    }

    @Override
    public int getItemCount() {
        return cartProductArrayList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.productImage);
    }
}
