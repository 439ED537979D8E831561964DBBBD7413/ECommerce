package com.winsant.android.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.winsant.android.R;
import com.winsant.android.model.OrderModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<OrderModel> orderProductArrayList;
    private onClickListener clickListener;

    public OrderAdapter(Activity activity, ArrayList<OrderModel> orderProductArrayList, onClickListener clickListener) {
        this.activity = activity;
        this.orderProductArrayList = orderProductArrayList;
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onReturnClick(int position, String remove_url, String product_id, String is_cancel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView txtName, txtPrice, txtQty, txtReturn, txtColor, txtSize, txtOrder, txtType, txtOrderId, txtTypeDate;
        LinearLayout cardView;

        public ViewHolder(final View itemView) {
            super(itemView);

            cardView = (LinearLayout) itemView.findViewById(R.id.cardView);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            txtReturn = (TextView) itemView.findViewById(R.id.txtReturn);
            txtColor = (TextView) itemView.findViewById(R.id.txtColor);
            txtSize = (TextView) itemView.findViewById(R.id.txtSize);
            txtOrder = (TextView) itemView.findViewById(R.id.txtOrder);
            txtType = (TextView) itemView.findViewById(R.id.txtType);
            txtTypeDate = (TextView) itemView.findViewById(R.id.txtTypeDate);
            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderId);

            txtOrder.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
            txtType.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
            txtName.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.BOLD);
            txtQty.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.BOLD);
            txtPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
            txtColor.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtSize.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtReturn.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtOrderId.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
            txtTypeDate.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);

            if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
                txtOrder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                txtQty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                txtColor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtOrderId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtReturn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                txtType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                txtTypeDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
                txtOrder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                txtQty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtColor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtOrderId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtReturn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                txtType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtTypeDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            } else {
                txtOrder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                txtQty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtColor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                txtSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                txtOrderId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtReturn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txtType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                txtTypeDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_order_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final OrderModel orderModel = orderProductArrayList.get(position);

        viewHolder.productImage.setAdjustViewBounds(true);

        Glide
                .with(activity)
                .load(orderModel.getProduct_image())
                .asBitmap().skipMemoryCache(true)
                .fitCenter()

                .placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(150, 150) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.productImage.setImageBitmap(null);
                        viewHolder.productImage.setImageBitmap(resource);
                    }
                });

        if (!orderModel.getProduct_color().equals("")) {
            viewHolder.txtColor.setVisibility(View.VISIBLE);
            viewHolder.txtColor.setText(String.format("Color : %s", orderModel.getProduct_color()));
        } else
            viewHolder.txtColor.setVisibility(View.GONE);

        if (!orderModel.getProduct_size().equals("")) {
            viewHolder.txtSize.setVisibility(View.VISIBLE);
            viewHolder.txtSize.setText(String.format("Size : %s", orderModel.getProduct_size()));
        } else
            viewHolder.txtSize.setVisibility(View.GONE);

        viewHolder.txtOrder.setText("Placed On : " + orderModel.getOrder_received_date());
        viewHolder.txtOrderId.setText("Order Id : " + orderModel.getOrder_id());
        viewHolder.txtName.setText(orderModel.getName());
        viewHolder.txtQty.setText("Qty : " + orderModel.getQty());

        switch (orderModel.getOrder_status()) {
            case "Canceled":
            case "Deleted":
            case "Return":
            case "Rejected":
                viewHolder.txtType.setTextColor(Color.RED);
                viewHolder.txtType.setBackgroundResource(R.drawable.bg_red);
                viewHolder.txtTypeDate.setTextColor(Color.RED);
                break;
            case "Pending":
            case "Ready For Dispatch":
            case "Dispatched":
                viewHolder.txtType.setTextColor(Color.parseColor("#1B347E"));
                viewHolder.txtType.setBackgroundResource(R.drawable.bg_pending);
                viewHolder.txtTypeDate.setTextColor(Color.parseColor("#1B347E"));
                break;
            default:
                viewHolder.txtType.setTextColor(Color.parseColor("#18AB4B"));
                viewHolder.txtType.setBackgroundResource(R.drawable.bg_deliver);
                viewHolder.txtTypeDate.setTextColor(Color.parseColor("#18AB4B"));
                break;
        }

        switch (orderModel.getOrder_status()) {
            case "Canceled":
            case "Deleted":
            case "Rejected":
                viewHolder.txtType.setText(orderModel.getOrder_status());
                viewHolder.txtTypeDate.setText(orderModel.getOrder_status() + " at (" + orderModel.getOrder_cancel_date() + ")");
                break;
            case "Return":
                viewHolder.txtType.setText(orderModel.getOrder_status());
                viewHolder.txtTypeDate.setText(orderModel.getOrder_status() + " at (" + orderModel.getOrder_return_date() + ")");
                break;
            case "Pending":
                viewHolder.txtType.setText(orderModel.getOrder_status());
                viewHolder.txtTypeDate.setText("Ordered at (" + orderModel.getOrder_received_date() + ")");
                break;
            case "Ready For Dispatch":
            case "Dispatched":
                viewHolder.txtType.setText(orderModel.getOrder_status());
                viewHolder.txtTypeDate.setText(orderModel.getOrder_status() + " at (" + orderModel.getOrder_ready_for_dispatch_date() + ")");
                break;
            case "Delivered":
                viewHolder.txtType.setText(orderModel.getOrder_status());
                viewHolder.txtTypeDate.setText(orderModel.getOrder_status() + " at (" + orderModel.getOrder_delivered_date() + ")");
            default:
                break;
        }

        switch (orderModel.getOrder_status()) {
            case "Pending":
            case "Ready For Dispatch":
            case "Dispatched":
                viewHolder.txtReturn.setText(R.string.cancel_order);
                viewHolder.cardView.setAlpha(1f);
                break;
            case "Delivered":
                viewHolder.txtReturn.setText(R.string.return_order);
                viewHolder.cardView.setAlpha(1f);
                break;
            case "Canceled":
            case "Rejected":
            case "Deleted":
            case "Return":
                viewHolder.txtReturn.setVisibility(View.GONE);
                viewHolder.cardView.setAlpha(0.65f);
                break;
            default:
                break;
        }

        viewHolder.txtReturn.setTag(position);
        viewHolder.txtReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                if (clickListener != null) {

                    switch (orderModel.getIs_cancel()) {
                        case "1":
                            clickListener.onReturnClick(pos, orderProductArrayList.get(pos).getCancel_url(),
                                    orderProductArrayList.get(pos).getProduct_id(), orderModel.getIs_cancel());
                            break;
                        case "0":
                            clickListener.onReturnClick(pos, orderProductArrayList.get(pos).getReturn_url(),
                                    orderProductArrayList.get(pos).getProduct_id(), orderModel.getIs_cancel());
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        int Price;
        int qty = (int) Double.parseDouble(orderModel.getQty());

        Price = (int) Double.parseDouble(orderModel.getPrice()) * qty;
        viewHolder.txtPrice.setText(activity.getResources().getString(R.string.Rs) + " " + String.valueOf(Price).replaceAll("\\.0*$", ""));

    }

    @Override
    public int getItemCount() {
        return orderProductArrayList.size();
    }
}
