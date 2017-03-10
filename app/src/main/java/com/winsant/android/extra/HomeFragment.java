//package com.winsant.android.ui.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.android.volley.NoConnectionError;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;
//import com.winsant.android.R;
//import com.winsant.android.adapter.HomePageAdapter;
//import com.winsant.android.model.CategoryModel;
//import com.winsant.android.model.HomeHeaderModel;
//import com.winsant.android.model.HomeProductModel;
//import com.winsant.android.ui.MyApplication;
//import com.winsant.android.ui.ProductSearchActivity;
//import com.winsant.android.utils.CommonDataUtility;
//import com.winsant.android.utils.StaticDataUtility;
//import com.winsant.android.utils.VolleyNetWorkCall;
//import com.winsant.android.views.ProgressWheel;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//
//public class HomeFragment extends BaseFragment {
//    //implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener
//
//    private RecyclerView home_page_list;
//    private View rootView;
//    private ProgressWheel progress_wheel;
////    private RelativeLayout rl_no_data;
////    private TextView emptyData;
//
//    private ImageView imgError;
//    private ArrayList<HomeHeaderModel> homeHeaderModels;
//    private ArrayList<HomeProductModel> homeProductModels;
//    private ArrayList<CategoryModel> categoryModels;
//    private ArrayList<String> bannerList;
//    private String TYPE = "";
//    private VolleyNetWorkCall netWorkCall;
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_home_old, container, false);
//
//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
//        activity.setSupportActionBar(toolbar);
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbar_title.setText(getString(R.string.app_name));
//
//        netWorkCall = new VolleyNetWorkCall();
//
//        BindView();
//
//        return rootView;
//    }
//
//    private void BindView() {
//
//        EditText edtSearch = (EditText) rootView.findViewById(R.id.edtSearch);
//        edtSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(activity, ProductSearchActivity.class));
//                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
//
//        progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
////        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
//        imgError = (ImageView) rootView.findViewById(R.id.imgError);
//
//        home_page_list = (RecyclerView) rootView.findViewById(R.id.home_page_list);
//        home_page_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
//        home_page_list.setNestedScrollingEnabled(false);
//
////        home_page_list1 = (RecyclerView) rootView.findViewById(R.id.home_page_list1);
////        home_page_list1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
////        home_page_list1.setNestedScrollingEnabled(false);
//
//        getData();
//
//        imgError.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (CommonDataUtility.checkConnection(activity)) {
//                    getData();
//                } else if (TYPE.equals(getResources().getString(R.string.no_data))
//                        || TYPE.equals(getResources().getString(R.string.no_connection))) {
//                    getData();
//                } else if (TYPE.equals(getResources().getString(R.string.no_internet))) {
//                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//                }
//            }
//        });
//    }
//
//    private void getData() {
//
//        home_page_list.setVisibility(View.GONE);
//
//        if (CommonDataUtility.checkConnection(activity)) {
//
//            imgError.setVisibility(View.GONE);
//            getHomePageData();
//
//        } else {
//
//            imgError.setVisibility(View.VISIBLE);
//            TYPE = getString(R.string.no_internet);
//            Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
//        }
//    }
//
//    private void setHomePageData() {
//
//        HomePageAdapter adapter = new HomePageAdapter(activity, homeHeaderModels);
//        home_page_list.setAdapter(adapter);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MyApplication.getInstance().trackScreenView("Home Fragment");
//    }
//
//    private void getHomePageData() {
//
//        progress_wheel.setVisibility(View.VISIBLE);
//        categoryModels = new ArrayList<>();
//        homeHeaderModels = new ArrayList<>();
//        bannerList = new ArrayList<>();
//
//        netWorkCall.makeServiceCall(activity, StaticDataUtility.SERVER_URL, new VolleyNetWorkCall.OnResponse() {
//            @Override
//            public void onSuccessCall(JSONObject response) {
//
//                try {
//
//                    System.out.println(StaticDataUtility.APP_TAG + " home page response --> " + response.toString());
//
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    final String message = jsonObject.optString("msg");
//                    final String success = jsonObject.optString("success");
//
//                    if (success.equals("1")) {
//
//                        // TODO : Banner
//                        JSONArray banners = jsonObject.optJSONArray("banner");
//                        for (int i = 0; i < banners.length(); i++) {
//                            System.out.println(StaticDataUtility.APP_TAG + " banner --> " + banners.optJSONObject(i).optString("banner_image"));
//                            bannerList.add(banners.optJSONObject(i).optString("banner_image"));
//                        }
//
//                        homeHeaderModels.add(new HomeHeaderModel("", "", "", "", "", "", "", "", "", "", homeProductModels, categoryModels, bannerList));
//
//                        // TODO : Category
//                        JSONArray category = jsonObject.optJSONArray("category");
//                        for (int i = 0; i < category.length(); i++) {
//
//                            JSONObject categoryObject = category.optJSONObject(i);
//                            System.out.println(StaticDataUtility.APP_TAG + " category --> " + categoryObject.toString());
//
//                            categoryModels.add(new CategoryModel(categoryObject.optString("category_name"), categoryObject.optString("category_image")
//                                    , categoryObject.optString("category_url")));
//                        }
//
//                        homeHeaderModels.add(new HomeHeaderModel("", "", "", "", "", "", "", "", "", "", homeProductModels, categoryModels, bannerList));
//
//                        // TODO : Product
//                        JSONArray name = jsonObject.optJSONArray("name");
//                        for (int i = 0; i < name.length(); i++) {
//
//                            homeProductModels = new ArrayList<>();
//                            JSONArray nameObject = name.optJSONArray(i);
//
//                            for (int j = 0; j < nameObject.length(); j++) {
//
//                                JSONObject finalObject = nameObject.optJSONObject(j);
//
//                                if (j == 0) {
//                                    homeHeaderModels.add(new HomeHeaderModel(finalObject.optString("name"), finalObject.optString("banner"),
//                                            finalObject.optString("banner_url"), finalObject.optString("catt_view_all"), finalObject.optString("banner1"),
//                                            finalObject.optString("banner1_url"), finalObject.optString("banner2"), finalObject.optString("banner2_url"),
//                                            jsonObject.optString("is_festival"), jsonObject.optString("festival_banner"), homeProductModels, categoryModels, bannerList));
//                                } else {
//                                    homeProductModels.add(new HomeProductModel(finalObject.optString("product_name"), finalObject.optString("product_full_name"),
//                                            finalObject.optString("product_url"), finalObject.optString("price"), finalObject.optString("discount_price"),
//                                            finalObject.optString("discount_per"), finalObject.optString("product_image"), finalObject.optString("cart_url"),
//                                            finalObject.optString("fav_url")));
//                                }
//                            }
//                        }
//
//                        // TODO : Set Data
//                        setHomePageData();
//
//                        progress_wheel.setVisibility(View.GONE);
//                        home_page_list.setVisibility(View.VISIBLE);
//
//                    } else {
//
//                        noDataError();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                    noDataError();
//                }
//            }
//
//            @Override
//            public void onPostSuccessCall(String response) {
//
//            }
//
//            @Override
//            public void onFailCall(VolleyError error) {
//                progress_wheel.setVisibility(View.GONE);
//                home_page_list.setVisibility(View.GONE);
//                imgError.setVisibility(View.VISIBLE);
//
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    TYPE = getString(R.string.no_connection);
//                    Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
//                } else {
//                    noDataError();
//                }
//            }
//        });
//    }
//
//    private void noDataError() {
//        progress_wheel.setVisibility(View.GONE);
//        imgError.setVisibility(View.VISIBLE);
//        TYPE = getString(R.string.no_data);
//        Glide.with(activity).load(R.drawable.no_data).into(imgError);
//    }
//}
