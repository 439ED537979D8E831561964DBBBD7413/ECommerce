package com.winsant.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.winsant.android.R;
import com.winsant.android.actionitembadge.library.ActionItemBadge;
import com.winsant.android.adapter.HomePageAdapter;
import com.winsant.android.daimajia.slider.library.SliderLayout;
import com.winsant.android.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.winsant.android.daimajia.slider.library.SliderTypes.TextSliderView;
import com.winsant.android.model.BannerModel;
import com.winsant.android.model.CategoryModel;
import com.winsant.android.model.HomeHeaderModel;
import com.winsant.android.model.HomeProductModel;
import com.winsant.android.ui.CartActivity;
import com.winsant.android.ui.FestivalActivity;
import com.winsant.android.ui.LoginActivity;
import com.winsant.android.ui.MyApplication;
import com.winsant.android.ui.ProductSearchActivity;
import com.winsant.android.ui.SpecificCategoryListActivity;
import com.winsant.android.ui.ViewAllCategoryListActivity;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.utils.VolleyNetWorkCall;
import com.winsant.android.views.CircleImageView;
import com.winsant.android.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener {
    //implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener

    private RecyclerView home_page_list;
    private View rootView;
    private ProgressWheel progress_wheel;

    private LinearLayout ll_home;
    private ImageView imgError;
    private ArrayList<HomeHeaderModel> homeHeaderModels;
    private ArrayList<HomeProductModel> homeProductModels;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<BannerModel> bannerList;
    private String TYPE = "";
    private VolleyNetWorkCall netWorkCall;

    private SliderLayout mDemoSlider;
    private CircleImageView category1;
    private CircleImageView category2;
    private CircleImageView category3;
    private TextView txtCategory1;
    private TextView txtCategory2;
    private TextView txtCategory3;
    private MenuItem cart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        toolbar_title.setText(getString(R.string.app_name));

        netWorkCall = new VolleyNetWorkCall();

        BindView();

        return rootView;
    }

    private void BindView() {

//        if (CommonDataUtility.isTablet(activity)) {
//            Toast.makeText(activity, "Tablet", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(activity, "Smart Phone", Toast.LENGTH_SHORT).show();
//        }
//
//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            Toast.makeText(activity, "Tablet_1", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(activity, "Smart_Phone_1", Toast.LENGTH_SHORT).show();
//        }

        EditText edtSearch = (EditText) rootView.findViewById(R.id.edtSearch);
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ProductSearchActivity.class));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        imgError = (ImageView) rootView.findViewById(R.id.imgError);
        ll_home = (LinearLayout) rootView.findViewById(R.id.ll_home);

        home_page_list = (RecyclerView) rootView.findViewById(R.id.home_page_list);
        home_page_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        home_page_list.setNestedScrollingEnabled(false);

        mDemoSlider = (SliderLayout) rootView.findViewById(R.id.slider);
        homePageCategory();

        getData();

        imgError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonDataUtility.checkConnection(activity)) {
                    getData();
                } else if (TYPE.equals(getResources().getString(R.string.no_data))
                        || TYPE.equals(getResources().getString(R.string.no_connection))) {
                    getData();
                } else if (TYPE.equals(getResources().getString(R.string.no_internet))) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            }
        });
    }

    private void homePageCategory() {
        category1 = (CircleImageView) rootView.findViewById(R.id.category1);
        txtCategory1 = (TextView) rootView.findViewById(R.id.txtCategory1);

        category2 = (CircleImageView) rootView.findViewById(R.id.category2);
        txtCategory2 = (TextView) rootView.findViewById(R.id.txtCategory2);

        category3 = (CircleImageView) rootView.findViewById(R.id.category3);
        txtCategory3 = (TextView) rootView.findViewById(R.id.txtCategory3);

        CircleImageView category4 = (CircleImageView) rootView.findViewById(R.id.category4);
        TextView txtCategory4 = (TextView) rootView.findViewById(R.id.txtCategory4);

        txtCategory1.setTypeface(CommonDataUtility.setTypeFace(activity));
        txtCategory2.setTypeface(CommonDataUtility.setTypeFace(activity));
        txtCategory3.setTypeface(CommonDataUtility.setTypeFace(activity));
        txtCategory4.setTypeface(CommonDataUtility.setTypeFace(activity));

        category1.setOnClickListener(this);
        txtCategory1.setOnClickListener(this);
        category2.setOnClickListener(this);
        txtCategory2.setOnClickListener(this);
        category3.setOnClickListener(this);
        txtCategory3.setOnClickListener(this);
        category4.setOnClickListener(this);
        txtCategory4.setOnClickListener(this);

        txtCategory1.setTypeface(CommonDataUtility.setTypeFace(activity));
        txtCategory2.setTypeface(CommonDataUtility.setTypeFace(activity));
        txtCategory3.setTypeface(CommonDataUtility.setTypeFace(activity));
        txtCategory4.setTypeface(CommonDataUtility.setTypeFace(activity));

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {

            txtCategory1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtCategory2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtCategory3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtCategory4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {

            txtCategory1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtCategory2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtCategory3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtCategory4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        } else {

            txtCategory1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtCategory2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtCategory3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtCategory4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }


    }

    private void getData() {

        ll_home.setVisibility(View.GONE);

        if (CommonDataUtility.checkConnection(activity)) {

            imgError.setVisibility(View.GONE);
            getHomePageData();

        } else {

            imgError.setVisibility(View.VISIBLE);
            TYPE = getString(R.string.no_internet);
            Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
        }
    }

    private void setHomePageData() {

        HomePageAdapter adapter = new HomePageAdapter(activity, homeHeaderModels);
        home_page_list.setAdapter(adapter);
    }

    private void getHomePageData() {

        progress_wheel.setVisibility(View.VISIBLE);
        categoryModels = new ArrayList<>();
        homeHeaderModels = new ArrayList<>();
        bannerList = new ArrayList<>();

        netWorkCall.makeServiceCall(activity, StaticDataUtility.SERVER_URL, new VolleyNetWorkCall.OnResponse() {
            @Override
            public void onSuccessCall(JSONObject response) {

                try {

                    System.out.println(StaticDataUtility.APP_TAG + " home page response --> " + response.toString());

                    JSONObject jsonObject = new JSONObject(response.toString());
                    final String message = jsonObject.optString("msg");
                    final String success = jsonObject.optString("success");

                    if (success.equals("1")) {

                        // TODO : Banner
                        JSONArray banners = jsonObject.optJSONArray("banner");
                        for (int i = 0; i < banners.length(); i++) {
                            bannerList.add(new BannerModel(banners.optJSONObject(i).optString("banner_image"), banners.optJSONObject(i).optString("banner_url")));
                        }

                        // TODO : Category
                        JSONArray category = jsonObject.optJSONArray("category");
                        for (int i = 0; i < category.length(); i++) {

                            JSONObject categoryObject = category.optJSONObject(i);
                            System.out.println(StaticDataUtility.APP_TAG + " category --> " + categoryObject.toString());

                            categoryModels.add(new CategoryModel(categoryObject.optString("category_name"), categoryObject.optString("category_image")
                                    , categoryObject.optString("category_url")));
                        }

                        // TODO : Product
                        JSONArray name = jsonObject.optJSONArray("name");
                        for (int i = 0; i < name.length(); i++) {

                            homeProductModels = new ArrayList<>();
                            JSONArray nameObject = name.optJSONArray(i);

//                            if (activity.getResources().getBoolean(R.bool.isTablet)) {
//
//                                for (int j = 0; j < 4; j++) {
//
//                                    JSONObject finalObject = nameObject.optJSONObject(j);
//
//                                    if (j == 0) {
//                                        homeHeaderModels.add(new HomeHeaderModel(finalObject.optString("name"), finalObject.optString("banner"),
//                                                finalObject.optString("banner_url"), finalObject.optString("catt_view_all"), finalObject.optString("banner1"),
//                                                finalObject.optString("banner1_url"), finalObject.optString("banner2"), finalObject.optString("banner2_url"),
//                                                jsonObject.optString("is_festival"), jsonObject.optString("festival_banner"), homeProductModels, categoryModels, bannerList));
//                                    } else {
//                                        homeProductModels.add(new HomeProductModel(finalObject.optString("product_name"), finalObject.optString("product_full_name"),
//                                                finalObject.optString("product_url"), finalObject.optString("price"), finalObject.optString("discount_price"),
//                                                finalObject.optString("discount_per"), finalObject.optString("product_image"), finalObject.optString("cart_url"),
//                                                finalObject.optString("fav_url")));
//                                    }
//                                }
//
//                            } else {

                                for (int j = 0; j < nameObject.length(); j++) {

                                    JSONObject finalObject = nameObject.optJSONObject(j);

                                    if (j == 0) {
                                        homeHeaderModels.add(new HomeHeaderModel(finalObject.optString("name"), finalObject.optString("banner"),
                                                finalObject.optString("banner_url"), finalObject.optString("catt_view_all"), finalObject.optString("banner1"),
                                                finalObject.optString("banner1_url"), finalObject.optString("banner2"), finalObject.optString("banner2_url"),
                                                jsonObject.optString("is_festival"), jsonObject.optString("festival_banner"), homeProductModels, categoryModels, bannerList));
                                    } else {
                                        homeProductModels.add(new HomeProductModel(finalObject.optString("product_name"), finalObject.optString("product_full_name"),
                                                finalObject.optString("product_url"), finalObject.optString("price"), finalObject.optString("discount_price"),
                                                finalObject.optString("discount_per"), finalObject.optString("product_image"), finalObject.optString("cart_url"),
                                                finalObject.optString("fav_url")));
                                    }
//                                }

                            }
                        }

                        // TODO : Set Data
                        setSliderData();
                        setCategoryData();
                        setHomePageData();

                        progress_wheel.setVisibility(View.GONE);
                        ll_home.setVisibility(View.VISIBLE);

                    } else {

                        noDataError();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    noDataError();
                }
            }

            @Override
            public void onPostSuccessCall(String response) {

            }

            @Override
            public void onFailCall(VolleyError error) {
                progress_wheel.setVisibility(View.GONE);
                ll_home.setVisibility(View.GONE);
                imgError.setVisibility(View.VISIBLE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    TYPE = getString(R.string.no_connection);
                    Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
                } else {
                    noDataError();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        // TODO : Home Page View All Category Display Activity
        Intent intent = new Intent(activity, ViewAllCategoryListActivity.class);

        switch (v.getId()) {

            case R.id.category1:
            case R.id.txtCategory1:

                intent.putExtra("position", "0");
                break;

            case R.id.category2:
            case R.id.txtCategory2:

                intent.putExtra("position", "1");
                break;

            case R.id.category3:
            case R.id.txtCategory3:

                intent.putExtra("position", "2");
                break;

            case R.id.category4:
            case R.id.txtCategory4:

                intent.putExtra("position", "0");
                break;
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void noDataError() {
        progress_wheel.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        TYPE = getString(R.string.no_data);
        Glide.with(activity).load(R.drawable.no_data).into(imgError);
    }

    private void setSliderData() {

        for (int i = 0; i < bannerList.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(activity);
            textSliderView.image(bannerList.get(i).getBanner_image()).setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putInt("position", i);
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(4500);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        int position = slider.getBundle().getInt("position", 0);

        if (!bannerList.get(position).getBanner_url().equals(""))
            if (bannerList.get(position).getBanner_url().equals("http://www.winsant.com/holi-festival")) {
                startActivity(new Intent(activity, FestivalActivity.class));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                Intent intent = new Intent(activity, SpecificCategoryListActivity.class);
                intent.putExtra("url", bannerList.get(position).getBanner_url());
                intent.putExtra("name", "");
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
    }

    private void setCategoryData() {

        txtCategory1.setText(categoryModels.get(0).getCategory_name());
        txtCategory2.setText(categoryModels.get(1).getCategory_name());
        txtCategory3.setText(categoryModels.get(2).getCategory_name());

        Picasso.with(activity).load(categoryModels.get(0).getCategory_image()).placeholder(R.drawable.no_image_available).into(category1);
        Picasso.with(activity).load(categoryModels.get(1).getCategory_image()).placeholder(R.drawable.no_image_available).into(category2);
        Picasso.with(activity).load(categoryModels.get(2).getCategory_image()).placeholder(R.drawable.no_image_available).into(category3);
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.clear(imgError);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Home Fragment");
        setBadge();
        activity.invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.activity_action_cart)).setIcon(R.drawable.ico_menu_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cart = menu.findItem(1);
        setBadge();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 1:

                if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
                    startActivity(new Intent(activity, CartActivity.class));
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBadge() {

        if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
            int total = MyApplication.getInstance().getPreferenceUtility().getInt("total_cart");
            if (!(total == 0)) {
                ActionItemBadge.Update(activity, cart, R.drawable.ico_menu_cart, StaticDataUtility.style, total);
            }
        }
    }
}
