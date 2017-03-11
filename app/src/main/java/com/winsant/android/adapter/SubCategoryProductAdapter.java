package com.winsant.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.winsant.android.model.CategoryModel;
import com.winsant.android.ui.ProductDetailsActivity;
import com.winsant.android.ui.ProductViewAllActivity;
import com.winsant.android.ui.SpecificCategoryListActivity;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class SubCategoryProductAdapter extends RecyclerView.Adapter<SubCategoryProductAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<CategoryModel> categoryModelList;
    private onClickListener clickListener;
    private Intent intent;

    public SubCategoryProductAdapter(Activity activity, ArrayList<CategoryModel> categoryModelList, onClickListener clickListener) {
        this.activity = activity;
        this.categoryModelList = categoryModelList;
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onFavClick(int position, String product_id, String fav_link, String isFavorite);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView main_banner;
        TextView main_title, viewAll;
        RecyclerView home_page_data_list;

        ViewHolder(final View itemView) {
            super(itemView);

            main_banner = (ImageView) itemView.findViewById(R.id.main_banner);
            main_title = (TextView) itemView.findViewById(R.id.main_title);
            viewAll = (TextView) itemView.findViewById(R.id.viewAll);
            home_page_data_list = (RecyclerView) itemView.findViewById(R.id.home_page_data_list);

            main_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
            main_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            viewAll.setTypeface(CommonDataUtility.setTypeFace(activity));
            viewAll.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_category_product_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CategoryModel homeHeaderModel = categoryModelList.get(position);

//        Picasso.with(activity).load(homeHeaderModel.getCategory_image()).placeholder(R.drawable.no_image_available).into(holder.main_banner);

        Glide.with(activity).load(homeHeaderModel.getCategory_image()).asBitmap().placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(1024, 350) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.main_banner.setImageDrawable(null);
                        holder.main_banner.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        holder.main_banner.setImageResource(R.drawable.no_image_available);
                    }
                });

        holder.main_title.setText(homeHeaderModel.getCategory_name());
        holder.main_title.setTag(position);
        holder.viewAll.setTag(position);
        holder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoryModelList.get((int) v.getTag()).getIs_last().equals("0")) {
                    intent = new Intent(activity, SpecificCategoryListActivity.class);
                } else {
                    intent = new Intent(activity, ProductViewAllActivity.class);
                }

                intent.putExtra("url", categoryModelList.get((int) v.getTag()).getCategory_url());
                intent.putExtra("name", categoryModelList.get((int) v.getTag()).getCategory_name());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        if (position % 3 == 0) {

            holder.home_page_data_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

            SubCategoryProductListAdapter homePageCategoryAdapter = new SubCategoryProductListAdapter(activity, categoryModelList.get(position).getSubCategoryProductList(),
                    new SubCategoryProductListAdapter.onClickListener() {
                        @Override
                        public void onClick(int position, String name, String product_url) {

                            intent = new Intent(activity, ProductDetailsActivity.class);
                            intent.putExtra("url", product_url);
                            intent.putExtra("name", name);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        }

                        @Override
                        public void onFavClick(int position, String product_id, String fav_link, String isFavorite) {
                            if (clickListener != null)
                                clickListener.onFavClick(position, product_id, fav_link, isFavorite);
                        }
                    }, "v");

            holder.home_page_data_list.setAdapter(homePageCategoryAdapter);

        } else {

            holder.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 2));

            SubCategoryProductListAdapter homePageCategoryAdapter = new SubCategoryProductListAdapter(activity, categoryModelList.get(position).getSubCategoryProductList(),
                    new SubCategoryProductListAdapter.onClickListener() {
                        @Override
                        public void onClick(int position, String name, String product_url) {

                            intent = new Intent(activity, ProductDetailsActivity.class);
                            intent.putExtra("url", product_url);
                            intent.putExtra("name", name);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        }

                        @Override
                        public void onFavClick(int position, String product_id, String fav_link, String isFavorite) {
                            if (clickListener != null)
                                clickListener.onFavClick(position, product_id, fav_link, isFavorite);
                        }
                    }, "g");

            holder.home_page_data_list.setAdapter(homePageCategoryAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }
}
