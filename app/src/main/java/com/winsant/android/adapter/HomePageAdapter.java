package com.winsant.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.thefinestartist.finestwebview.FinestWebView;
import com.winsant.android.R;
import com.winsant.android.model.HomeHeaderModel;
import com.winsant.android.ui.ProductDetailsActivity;
import com.winsant.android.ui.ProductViewAllActivity;
import com.winsant.android.ui.SpecificCategoryListActivity;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.views.CapitalizedTextView;

import java.util.ArrayList;
import java.util.Locale;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<HomeHeaderModel> homeHeaderModels;

    public HomePageAdapter(Activity activity, ArrayList<HomeHeaderModel> homeHeaderModels) {
        this.activity = activity;
        this.homeHeaderModels = homeHeaderModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView festival_banner, main_banner, sub_banner_1, sub_banner_2;
        CapitalizedTextView main_title;
        TextView viewAll;
        RecyclerView home_page_data_list;
        TableRow tblBanner;

        ViewHolder(final View itemView) {
            super(itemView);

            tblBanner = (TableRow) itemView.findViewById(R.id.tblBanner);

            festival_banner = (ImageView) itemView.findViewById(R.id.festival_banner);
            main_banner = (ImageView) itemView.findViewById(R.id.main_banner);
            sub_banner_1 = (ImageView) itemView.findViewById(R.id.sub_banner_1);
            sub_banner_2 = (ImageView) itemView.findViewById(R.id.sub_banner_2);
            main_title = (CapitalizedTextView) itemView.findViewById(R.id.main_title);
            viewAll = (TextView) itemView.findViewById(R.id.viewAll);
            home_page_data_list = (RecyclerView) itemView.findViewById(R.id.home_page_data_list);
            home_page_data_list.setNestedScrollingEnabled(false);

            main_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
            viewAll.setTypeface(CommonDataUtility.setTypeFace(activity));

            if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
                main_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                viewAll.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
                main_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                viewAll.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                main_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                viewAll.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        int viewType = 0;
        if (position == 0) viewType = 0;
        else if (position == 1) viewType = 1;
        else viewType = 2;

        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final HomeHeaderModel homeHeaderModel = homeHeaderModels.get(position);

        if (homeHeaderModel.getIs_festival().equals("1") && position == 0) {
            holder.festival_banner.setVisibility(View.VISIBLE);
        } else {
            holder.festival_banner.setVisibility(View.GONE);
        }

        holder.festival_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeHeaderModel.getFestival_banner_url().equals(""))
                    showWebView(homeHeaderModel.getFestival_banner_url());
            }
        });

//        Picasso.with(activity).load(homeHeaderModel.getFestival_banner()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(holder.festival_banner);
//        Picasso.with(activity).load(homeHeaderModel.getBanner()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(holder.main_banner);
//        Picasso.with(activity).load(homeHeaderModel.getBanner1()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(holder.sub_banner_1);
//        Picasso.with(activity).load(homeHeaderModel.getBanner2()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(holder.sub_banner_2);

        loadImage(homeHeaderModel.getFestival_banner(), holder.festival_banner);
        loadImage(homeHeaderModel.getBanner(), holder.main_banner);

        if (homeHeaderModel.getBanner1().equals("") || homeHeaderModel.getBanner2().equals(""))
            holder.tblBanner.setVisibility(View.GONE);
        else {
            holder.tblBanner.setVisibility(View.VISIBLE);
            loadImage(homeHeaderModel.getBanner2(), holder.sub_banner_2);
            loadImage(homeHeaderModel.getBanner1(), holder.sub_banner_1);
        }

        holder.main_banner.setTag(position);
        holder.sub_banner_1.setTag(position);
        holder.sub_banner_2.setTag(position);

        holder.main_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!homeHeaderModels.get((int) v.getTag()).getBanner_url().equals("")) {
                    Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
                    intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getBanner_url());
                    intent.putExtra("name", "");
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });

        holder.sub_banner_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!homeHeaderModels.get((int) v.getTag()).getBanner1_url().equals("")) {
                    Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
                    intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getBanner1_url());
                    intent.putExtra("name", "");
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });

        holder.sub_banner_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!homeHeaderModels.get((int) v.getTag()).getBanner2_url().equals("")) {
                    Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
                    intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getBanner2_url());
                    intent.putExtra("name", "");
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });

        holder.main_title.setText(homeHeaderModel.getName().toLowerCase(Locale.getDefault()));
        holder.viewAll.setTag(position);
        holder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO : Home Page View All Product Display Activity
                Intent intent = new Intent(activity, ProductViewAllActivity.class);
                intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getCatt_view_all());
                intent.putExtra("name", homeHeaderModels.get((int) v.getTag()).getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        if (position % 3 == 0) {

            holder.home_page_data_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

            HomePageCategoryAdapter homePageCategoryAdapter = new HomePageCategoryAdapter(activity, homeHeaderModels.get(position)
                    .getCategoryProductModels(),
                    new HomePageCategoryAdapter.onClickListener() {
                        @Override
                        public void onClick(int position, String name, String product_url) {

                            // TODO : Specific Product Details Display Activity
                            Intent intent = new Intent(activity, ProductDetailsActivity.class);
                            intent.putExtra("url", product_url);
                            intent.putExtra("name", name);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            System.out.println(StaticDataUtility.APP_TAG + " url --> " + product_url);
                        }

                    }, "v");

            holder.home_page_data_list.setAdapter(homePageCategoryAdapter);

        } else {

            if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
                holder.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 4));
            } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
                holder.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 3));
            } else {
                holder.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 2));
            }

//            if (CommonDataUtility.isTablet(activity))
//                holder.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 3));
//            else
//                holder.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 2));

            HomePageCategoryAdapter homePageCategoryAdapter = new HomePageCategoryAdapter(activity, homeHeaderModels.get(position)
                    .getCategoryProductModels(),
                    new HomePageCategoryAdapter.onClickListener() {
                        @Override
                        public void onClick(int position, String name, String product_url) {

                            // TODO : Specific Product Details Display Activity
                            Intent intent = new Intent(activity, ProductDetailsActivity.class);
                            intent.putExtra("url", product_url);
                            intent.putExtra("name", name);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        }
                    }, "g");

            holder.home_page_data_list.setAdapter(homePageCategoryAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return homeHeaderModels.size();
    }

    private void loadImage(String url, final ImageView imageView) {

        Glide.with(activity).load(url).asBitmap().skipMemoryCache(true).placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(1024, 350) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(null);
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imageView.setImageResource(R.drawable.no_image_available);
                    }
                });
    }

    private void showWebView(String url) {

        new FinestWebView.Builder(activity).theme(R.style.FinestWebViewTheme)
                .titleDefault("Winsant")
                .showUrl(false)
                .statusBarColorRes(R.color.bluePrimaryDark)
                .toolbarColorRes(R.color.bluePrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.bluePrimaryLight)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColorRes(R.color.bluePrimaryDark)
                .menuSelector(R.drawable.selector_light_theme)
                .menuTextGravity(Gravity.CENTER)
                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show(url);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.festival_banner);
        Glide.clear(holder.main_banner);
        Glide.clear(holder.sub_banner_1);
        Glide.clear(holder.sub_banner_2);
    }
}