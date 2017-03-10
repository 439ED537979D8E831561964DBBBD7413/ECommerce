//package com.winsant.android.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;
//import com.winsant.android.R;
//import com.winsant.android.daimajia.slider.library.SliderLayout;
//import com.winsant.android.daimajia.slider.library.SliderTypes.BaseSliderView;
//import com.winsant.android.daimajia.slider.library.SliderTypes.TextSliderView;
//import com.winsant.android.model.CategoryModel;
//import com.winsant.android.model.HomeHeaderModel;
//import com.winsant.android.ui.FestivalActivity;
//import com.winsant.android.ui.ProductDetailsActivity;
//import com.winsant.android.ui.ProductViewAllActivity;
//import com.winsant.android.ui.SpecificCategoryListActivity;
//import com.winsant.android.ui.ViewAllCategoryListActivity;
//import com.winsant.android.utils.CommonDataUtility;
//import com.winsant.android.utils.StaticDataUtility;
//import com.winsant.android.views.CapitalizedTextView;
//import com.winsant.android.views.CircleImageView;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
//public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private Activity activity;
//    private ArrayList<HomeHeaderModel> homeHeaderModels;
//    private Target mTarget;
//
//    public HomePageAdapter(Activity activity, ArrayList<HomeHeaderModel> homeHeaderModels) {
//        this.activity = activity;
//        this.homeHeaderModels = homeHeaderModels;
////        this.clickListener = clickListener;
//    }
//
////    public interface onClickListener {
////        void onClick(int position);
////    }
//
//    private class ViewHolder1 extends RecyclerView.ViewHolder {
//
//        private SliderLayout mDemoSlider;
//        //private EnchantedViewPager mViewPager;
//
//        ViewHolder1(final View itemView) {
//            super(itemView);
//            mDemoSlider = (SliderLayout) itemView.findViewById(R.id.slider);
//
//            //mViewPager = (EnchantedViewPager) itemView.findViewById(R.id.homepage_card_view_pager);
//            //mViewPager.setAdapter(new EnchantedPagerAdapter(activity, homeHeaderModels.get(0).getBannerList()));
//            //mViewPager.useScale();
//            //mViewPager.useAlpha();
//
//            setSliderData(mDemoSlider, homeHeaderModels.get(0).getBannerList());
//        }
//    }
//
//    private class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private CircleImageView category1, category2, category3, category4;
//        private TextView txtCategory1, txtCategory2, txtCategory3, txtCategory4;
//
//        ViewHolder2(final View itemView) {
//            super(itemView);
//
//            category1 = (CircleImageView) itemView.findViewById(R.id.category1);
//            txtCategory1 = (TextView) itemView.findViewById(R.id.txtCategory1);
//
//            category2 = (CircleImageView) itemView.findViewById(R.id.category2);
//            txtCategory2 = (TextView) itemView.findViewById(R.id.txtCategory2);
//
//            category3 = (CircleImageView) itemView.findViewById(R.id.category3);
//            txtCategory3 = (TextView) itemView.findViewById(R.id.txtCategory3);
//
//            category4 = (CircleImageView) itemView.findViewById(R.id.category4);
//            txtCategory4 = (TextView) itemView.findViewById(R.id.txtCategory4);
//
//            txtCategory1.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory2.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory3.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory4.setTypeface(CommonDataUtility.setTypeFace(activity));
//
//            category1.setOnClickListener(this);
//            txtCategory1.setOnClickListener(this);
//            category2.setOnClickListener(this);
//            txtCategory2.setOnClickListener(this);
//            category3.setOnClickListener(this);
//            txtCategory3.setOnClickListener(this);
//            category4.setOnClickListener(this);
//            txtCategory4.setOnClickListener(this);
//
//            txtCategory1.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory2.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory3.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory4.setTypeface(CommonDataUtility.setTypeFace(activity));
//            txtCategory1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            txtCategory2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            txtCategory3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            txtCategory4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//        }
//
//        @Override
//        public void onClick(View v) {
//
//            // TODO : Home Page View All Category Display Activity
//            Intent intent = new Intent(activity, ViewAllCategoryListActivity.class);
//
//            switch (v.getId()) {
//
//                case R.id.category1:
//                case R.id.txtCategory1:
//
//                    intent.putExtra("position", "0");
//                    break;
//
//                case R.id.category2:
//                case R.id.txtCategory2:
//
//                    intent.putExtra("position", "1");
//                    break;
//
//                case R.id.category3:
//                case R.id.txtCategory3:
//
//                    intent.putExtra("position", "2");
//                    break;
//
//                case R.id.category4:
//                case R.id.txtCategory4:
//
//                    intent.putExtra("position", "0");
//                    break;
//            }
//            activity.startActivity(intent);
//            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        }
//    }
//
//    private class ViewHolder3 extends RecyclerView.ViewHolder {
//
//        ImageView festival_banner, main_banner, sub_banner_1, sub_banner_2;
//        CapitalizedTextView main_title;
//        TextView viewAll;
//        RecyclerView home_page_data_list;
//
//        ViewHolder3(final View itemView) {
//            super(itemView);
//
//            festival_banner = (ImageView) itemView.findViewById(R.id.festival_banner);
//            main_banner = (ImageView) itemView.findViewById(R.id.main_banner);
//            sub_banner_1 = (ImageView) itemView.findViewById(R.id.sub_banner_1);
//            sub_banner_2 = (ImageView) itemView.findViewById(R.id.sub_banner_2);
//            main_title = (CapitalizedTextView) itemView.findViewById(R.id.main_title);
//            viewAll = (TextView) itemView.findViewById(R.id.viewAll);
//            home_page_data_list = (RecyclerView) itemView.findViewById(R.id.home_page_data_list);
//            home_page_data_list.setNestedScrollingEnabled(false);
//
//            main_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
//            main_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            viewAll.setTypeface(CommonDataUtility.setTypeFace(activity));
//            viewAll.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        int viewType = 0;
//        if (position == 0) viewType = 0;
//        else if (position == 1) viewType = 1;
//        else viewType = 2;
//
//        return viewType;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//
//        RecyclerView.ViewHolder viewHolder = null;
//        View v;
//
//        switch (viewType) {
//            case 0:
//
//                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_slider_item, viewGroup, false);
//                viewHolder = new ViewHolder1(v);
//
//                break;
//            case 1:
//
//                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_category_item, viewGroup, false);
//                viewHolder = new ViewHolder2(v);
//
//                break;
//            case 2:
//
//                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_item, viewGroup, false);
//                viewHolder = new ViewHolder3(v);
//
//                break;
//        }
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        switch (holder.getItemViewType()) {
//
//            case 0:
//                break;
//
//            case 1:
//
//                ViewHolder2 vh2 = (ViewHolder2) holder;
//                setCategoryData(vh2, homeHeaderModels.get(position).getCategoryModels());
//
//                break;
//
//            case 2:
//
//                System.out.println(StaticDataUtility.APP_TAG + " case 2 position --> " + position);
//
//                final ViewHolder3 vh3 = (ViewHolder3) holder;
//
//                HomeHeaderModel homeHeaderModel = homeHeaderModels.get(position);
//
//                if (homeHeaderModel.getIs_festival().equals("1") && position == 2) {
//                    vh3.festival_banner.setVisibility(View.VISIBLE);
//                } else {
//                    vh3.festival_banner.setVisibility(View.GONE);
//                }
//
//                vh3.festival_banner.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        activity.startActivity(new Intent(activity, FestivalActivity.class));
//                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    }
//                });
//
//                Picasso.with(activity).load(homeHeaderModel.getFestival_banner()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(vh3.festival_banner);
//                Picasso.with(activity).load(homeHeaderModel.getBanner()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(vh3.main_banner);
//                Picasso.with(activity).load(homeHeaderModel.getBanner1()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(vh3.sub_banner_1);
//                Picasso.with(activity).load(homeHeaderModel.getBanner2()).placeholder(R.drawable.no_image_available).resize(1024, 350).into(vh3.sub_banner_2);
//
////                loadImage(homeHeaderModel.getFestival_banner(), vh3.festival_banner);
////                loadImage(homeHeaderModel.getBanner(), vh3.main_banner);
////                loadImage(homeHeaderModel.getBanner1(), vh3.sub_banner_1);
////                loadImage(homeHeaderModel.getBanner2(), vh3.sub_banner_2);
//
////                loadImage1(R.drawable.promo_offer_banner, vh3.festival_banner);
////                loadImage1(R.drawable.promo_offer_banner, vh3.main_banner);
////                loadImage1(R.drawable.offer_banner, vh3.sub_banner_1);
////                loadImage1(R.drawable.offer_banner, vh3.sub_banner_2);
//
//                vh3.main_banner.setTag(position);
//                vh3.sub_banner_1.setTag(position);
//                vh3.sub_banner_2.setTag(position);
//
//                vh3.main_banner.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // TODO : Home Page View All Product Display Activity
//                        Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
//                        intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getBanner_url());
//                        intent.putExtra("name", "");
//                        activity.startActivity(intent);
//                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                    }
//                });
//
//                vh3.sub_banner_1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // TODO : Home Page View All Product Display Activity
//                        Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
//                        intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getBanner1_url());
//                        intent.putExtra("name", "");
//                        activity.startActivity(intent);
//                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                    }
//                });
//
//                vh3.sub_banner_2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // TODO : Home Page View All Product Display Activity
//                        Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
//                        intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getBanner2_url());
//                        intent.putExtra("name", "");
//                        activity.startActivity(intent);
//                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                    }
//                });
//
//                vh3.main_title.setText(homeHeaderModel.getName().toLowerCase(Locale.getDefault()));
////                vh3.main_title.setText(homeHeaderModel.getName());
//                vh3.viewAll.setTag(position);
//                vh3.viewAll.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // TODO : Home Page View All Product Display Activity
//                        Intent intent = new Intent(activity, ProductViewAllActivity.class);
//                        intent.putExtra("url", homeHeaderModels.get((int) v.getTag()).getCatt_view_all());
//                        intent.putExtra("name", homeHeaderModels.get((int) v.getTag()).getName());
//                        activity.startActivity(intent);
//                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                    }
//                });
//
//                if (!(position == 0))
//                    if (position % 3 == 0) {
//
//                        vh3.home_page_data_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
//
//                        HomePageCategoryAdapter homePageCategoryAdapter = new HomePageCategoryAdapter(activity, homeHeaderModels.get(position)
//                                .getCategoryProductModels(),
//                                new HomePageCategoryAdapter.onClickListener() {
//                                    @Override
//                                    public void onClick(int position, String name, String product_url) {
//
//                                        // TODO : Specific Product Details Display Activity
//                                        Intent intent = new Intent(activity, ProductDetailsActivity.class);
//                                        intent.putExtra("url", product_url);
//                                        intent.putExtra("name", name);
//                                        activity.startActivity(intent);
//                                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                                        System.out.println(StaticDataUtility.APP_TAG + " url --> " + product_url);
//                                    }
//
//                                }, "v");
//
//                        vh3.home_page_data_list.setAdapter(homePageCategoryAdapter);
//
//                    } else {
//
//                        vh3.home_page_data_list.setLayoutManager(new GridLayoutManager(activity, 2));
//
//                        HomePageCategoryAdapter homePageCategoryAdapter = new HomePageCategoryAdapter(activity, homeHeaderModels.get(position)
//                                .getCategoryProductModels(),
//                                new HomePageCategoryAdapter.onClickListener() {
//                                    @Override
//                                    public void onClick(int position, String name, String product_url) {
//
//                                        // TODO : Specific Product Details Display Activity
//                                        Intent intent = new Intent(activity, ProductDetailsActivity.class);
//                                        intent.putExtra("url", product_url);
//                                        intent.putExtra("name", name);
//                                        activity.startActivity(intent);
//                                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                                        System.out.println(StaticDataUtility.APP_TAG + " url --> " + product_url);
//                                    }
//                                }, "g");
//
//                        vh3.home_page_data_list.setAdapter(homePageCategoryAdapter);
//                    }
//
//                break;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return homeHeaderModels.size();
//    }
//
//    private void setSliderData(SliderLayout mDemoSlider, ArrayList<String> bannerList) {
//
//        //  textSliderView.image(bannerList.get(i))
//        for (int i = 0; i < bannerList.size(); i++) {
//            TextSliderView textSliderView = new TextSliderView(activity);
//            textSliderView.image(bannerList.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);
//            mDemoSlider.addSlider(textSliderView);
//        }
//
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setDuration(4500);
//    }
//
//    private void setCategoryData(ViewHolder2 vh2, ArrayList<CategoryModel> categoryModels) {
//
//        vh2.txtCategory1.setText(categoryModels.get(0).getCategory_name());
//        vh2.txtCategory2.setText(categoryModels.get(1).getCategory_name());
//        vh2.txtCategory3.setText(categoryModels.get(2).getCategory_name());
//
//        Picasso.with(activity).load(categoryModels.get(0).getCategory_image()).placeholder(R.drawable.no_image_available).resize(120, 120).into(vh2.category1);
//        Picasso.with(activity).load(categoryModels.get(1).getCategory_image()).placeholder(R.drawable.no_image_available).resize(120, 120).into(vh2.category2);
//        Picasso.with(activity).load(categoryModels.get(2).getCategory_image()).placeholder(R.drawable.no_image_available).resize(120, 120).into(vh2.category3);
//    }
//
//    private void loadImage(String url, final ImageView imageView) {
//
//        Glide.with(activity).load(url).asBitmap()
//                .skipMemoryCache(true).placeholder(R.drawable.no_image_available)
//                .into(new SimpleTarget<Bitmap>(1024, 350) {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        imageView.setImageBitmap(resource);
//                        resource.recycle();
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                        imageView.setImageResource(R.drawable.no_image_available);
//                    }
//                });
//    }
//
//    private void loadImage1(int promo_offer_banner, final ImageView imageView) {
//
//        Picasso.with(activity).load(promo_offer_banner).placeholder(R.drawable.no_image_available).into(imageView);
//        Picasso.with(activity).load(promo_offer_banner).placeholder(R.drawable.no_image_available).into(imageView);
//        Picasso.with(activity).load(promo_offer_banner).placeholder(R.drawable.no_image_available).into(imageView);
//        Picasso.with(activity).load(promo_offer_banner).placeholder(R.drawable.no_image_available).into(imageView);
//    }
//}
//
//
///*Glide.with(activity).load(homeHeaderModel.getBanner()).asBitmap().placeholder(R.drawable.no_image_available)
//                        .into(new SimpleTarget<Bitmap>(1024, 400) {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                vh3.festival_banner.setImageBitmap(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                vh3.festival_banner.setImageResource(R.drawable.no_image_available);
//                            }
//                        });
//
//                 Glide.with(activity).load(homeHeaderModel.getBanner()).asBitmap().placeholder(R.drawable.no_image_available)
//                        .into(new SimpleTarget<Bitmap>(1024, 400) {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                vh3.main_banner.setImageBitmap(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                vh3.main_banner.setImageResource(R.drawable.no_image_available);
//                            }
//                        });
//
//                Glide.with(activity).load(homeHeaderModel.getBanner1()).asBitmap().fitCenter().placeholder(R.drawable.no_image_available)
//                        .into(new SimpleTarget<Bitmap>(800, 400) {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                vh3.sub_banner_1.setImageBitmap(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                vh3.sub_banner_1.setImageResource(R.drawable.no_image_available);
//                            }
//                        });
//
//                Glide.with(activity).load(homeHeaderModel.getBanner2()).asBitmap().fitCenter().placeholder(R.drawable.no_image_available)
//                        .into(new SimpleTarget<Bitmap>(800, 400) {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                vh3.sub_banner_2.setImageBitmap(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                vh3.sub_banner_2.setImageResource(R.drawable.no_image_available);
//                            }
//                        });
//
//                //Picasso.with(activity).load(R.drawable.clothing_again).placeholder(R.drawable.no_image_available).into(vh3.main_banner);
//                //Picasso.with(activity).load(R.drawable.mens_clothing).placeholder(R.drawable.no_image_available).into(vh3.sub_banner_1);
//                //Picasso.with(activity).load(R.drawable.mens_clothing).placeholder(R.drawable.no_image_available).into(vh3.sub_banner_2);*/
