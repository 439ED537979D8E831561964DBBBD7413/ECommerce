package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.winsant.android.R;
import com.winsant.android.adapter.AllSubCategoryListAdapter;
import com.winsant.android.adapter.SubCategoryProductAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.model.CategoryModel;
import com.winsant.android.model.HomeProductModel;
import com.winsant.android.model.SubCategoryModel;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.utils.VolleyNetWorkCall;
import com.winsant.android.views.CapitalizedTextView;
import com.winsant.android.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pc-Android-1 on 10/14/2016.
 * <p>
 * TODO : Specific SubCategory with SubCategory Product Display Activity
 */

public class SpecificCategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private CapitalizedTextView mToolbar_title;

    private ProgressWheel progress_wheel;
    private ImageView imgError;
    private ArrayList<SubCategoryModel> SubCategoryList;
    private ArrayList<CategoryModel> CategoryProductList;
    private ArrayList<HomeProductModel> subCategoryProductList;

    private NestedScrollView ns_main;
    private RecyclerView subCategoryList1, subCategoryList2, subCategoryProductListView;
    private String url;
    private KProgressHUD progressHUD;
    private String TYPE = "";

    private VolleyNetWorkCall netWorkCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_category);

        activity = SpecificCategoryListActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (CapitalizedTextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        String name = getIntent().getStringExtra("name");
        if (!name.equals(""))
            mToolbar_title.setText(getIntent().getStringExtra("name"));
        else
            mToolbar_title.setText(getString(R.string.app_name));

        url = getIntent().getStringExtra("url");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ico_arrow_back_svg);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        netWorkCall = new VolleyNetWorkCall();

        setUI();
    }

    private void setUI() {

        ns_main = (NestedScrollView) findViewById(R.id.ns_main);

        progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        imgError = (ImageView) findViewById(R.id.imgError);

        subCategoryList1 = (RecyclerView) findViewById(R.id.subCategoryList1);
        subCategoryList1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        subCategoryList2 = (RecyclerView) findViewById(R.id.subCategoryList2);
        subCategoryList2.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        subCategoryProductListView = (RecyclerView) findViewById(R.id.subCategoryProductListView);
        subCategoryProductListView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

//        subCategoryList1.setNestedScrollingEnabled(false);

        imgError.setOnClickListener(this);

        getData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emptyData:

                if (CommonDataUtility.checkConnection(activity)) {
                    getData();
                } else if (TYPE.equals(getResources().getString(R.string.no_data))
                        || TYPE.equals(getResources().getString(R.string.no_connection))) {
                    getData();
                } else if (TYPE.equals(getResources().getString(R.string.no_internet))) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }

                break;

            default:
                break;
        }
    }

    private void getData() {

        ns_main.setVisibility(View.GONE);

        if (CommonDataUtility.checkConnection(activity)) {

            imgError.setVisibility(View.GONE);
            getProductViewAllData();

        } else {

            imgError.setVisibility(View.VISIBLE);
            TYPE = getString(R.string.no_internet);
            Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
        }
    }

    private void getProductViewAllData() {

        progress_wheel.setVisibility(View.VISIBLE);
        SubCategoryList = new ArrayList<>();
        CategoryProductList = new ArrayList<>();

        netWorkCall.makeServiceCall(activity, url, new VolleyNetWorkCall.OnResponse() {
            @Override
            public void onSuccessCall(JSONObject response) {

                try {

                    System.out.println(StaticDataUtility.APP_TAG + " home page response --> " + response.toString());

                    JSONObject jsonObject = new JSONObject(response.toString());
                    final String message = jsonObject.optString("msg");
                    final String success = jsonObject.optString("success");

                    mToolbar_title.setText(jsonObject.optString("main_title"));

                    if (success.equals("1")) {

                        // TODO : SubCategory Display
                        JSONArray category = jsonObject.optJSONArray("category");

                        if (category.length() > 0) {
                            for (int i = 0; i < category.length(); i++) {

                                JSONObject dataObject = category.getJSONObject(i);

                                SubCategoryList.add(new SubCategoryModel(dataObject.optString("category_name"), dataObject.optString("category_image")
                                        , dataObject.optString("category_url"), dataObject.optString("is_last")));
                            }
                        }

                        // TODO : SubCategory Product Display
                        JSONArray data = jsonObject.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {

                            subCategoryProductList = new ArrayList<>();

                            JSONObject dataObject = data.optJSONObject(i);
                            JSONArray dataArray = dataObject.optJSONArray("data");

                            for (int j = 0; j < dataArray.length(); j++) {

                                JSONObject finalObject = dataArray.optJSONObject(j);

                                subCategoryProductList.add(new HomeProductModel(finalObject.optString("product_id"), finalObject.optString("product_name"),
                                        finalObject.optString("product_link"), finalObject.optString("price"), finalObject.optString("discount_price"),
                                        finalObject.optString("discount_per"), finalObject.optString("product_image"), finalObject.optString("fav_url"),
                                        finalObject.optString("remove_link"), finalObject.optString("availability"), finalObject.optString("review_count"),
                                        finalObject.optString("is_wishlist")));
                            }

                            CategoryProductList.add(new CategoryModel(dataObject.optString("category_name"), dataObject.optString("category_banner")
                                    , dataObject.optString("category_url"), subCategoryProductList, dataObject.optString("is_last")));
                        }

                        // TODO : Set Data
                        setData();

                        progress_wheel.setVisibility(View.GONE);
                        ns_main.setVisibility(View.VISIBLE);

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
                ns_main.setVisibility(View.GONE);
                imgError.setVisibility(View.VISIBLE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    TYPE = getString(R.string.no_internet);
                    Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
                } else {
                    noServerError();
                }
            }
        });
    }

    private Intent intent;

    private void setData() {

        //  TODO : SubCategory Display
        ArrayList<SubCategoryModel> subCategoryList11 = new ArrayList<>();
        ArrayList<SubCategoryModel> subCategoryList21 = new ArrayList<>();

        if (SubCategoryList.size() > 0)
            if (SubCategoryList.size() >= 7) {

                int middle = SubCategoryList.size() / 2;

                for (int i = 0; i < middle; i++) {
                    subCategoryList11.add(new SubCategoryModel(SubCategoryList.get(i).getCategory_name(), SubCategoryList.get(i).getCategory_image(),
                            SubCategoryList.get(i).getCategory_url(), SubCategoryList.get(i).getIs_last()));
                }

                for (int i = middle; i < SubCategoryList.size(); i++) {
                    subCategoryList21.add(new SubCategoryModel(SubCategoryList.get(i).getCategory_name(), SubCategoryList.get(i).getCategory_image(),
                            SubCategoryList.get(i).getCategory_url(), SubCategoryList.get(i).getIs_last()));
                }

//                ViewGroup.LayoutParams params = subCategoryList1.getLayoutParams();
//                params.height = 385;
//                subCategoryList1.setLayoutParams(params);
//
//                ViewGroup.LayoutParams params1 = subCategoryList2.getLayoutParams();
//                params1.height = 385;
//                subCategoryList2.setLayoutParams(params1);

                subCategoryList1.setAdapter(new AllSubCategoryListAdapter(activity, subCategoryList11,
                        new AllSubCategoryListAdapter.onClickListener() {
                            @Override
                            public void onClick(String category_name, String category_url, String is_last) {

                                // TODO : SubCategory View All Product Display Activity
                                if (is_last.equals("0")) {
                                    intent = new Intent(activity, SpecificCategoryListActivity.class);
                                } else {
                                    intent = new Intent(activity, ProductViewAllActivity.class);
                                }

                                intent.putExtra("url", category_url);
                                intent.putExtra("name", category_name);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }, "square"));

                subCategoryList2.setAdapter(new AllSubCategoryListAdapter(activity, subCategoryList21,
                        new AllSubCategoryListAdapter.onClickListener() {
                            @Override
                            public void onClick(String category_name, String category_url, String is_last) {

                                if (is_last.equals("0")) {
                                    intent = new Intent(activity, SpecificCategoryListActivity.class);
                                } else {
                                    intent = new Intent(activity, ProductViewAllActivity.class);
                                }
                                // TODO : SubCategory View All Product Display Activity
                                intent.putExtra("url", category_url);
                                intent.putExtra("name", category_name);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }, "square"));

            } else {

                subCategoryList1.setAdapter(new AllSubCategoryListAdapter(activity, SubCategoryList,
                        new AllSubCategoryListAdapter.onClickListener() {
                            @Override
                            public void onClick(String category_name, String category_url, String is_last) {

                                // TODO : SubCategory View All Product Display Activity
                                if (is_last.equals("0")) {
                                    intent = new Intent(activity, SpecificCategoryListActivity.class);
                                } else {
                                    intent = new Intent(activity, ProductViewAllActivity.class);
                                }

                                intent.putExtra("url", category_url);
                                intent.putExtra("name", category_name);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }, "square"));

                subCategoryList2.setVisibility(View.GONE);
            }
        else {
            subCategoryList1.setVisibility(View.GONE);
            subCategoryList2.setVisibility(View.GONE);
        }

        //  TODO : SubCategory Product Display
        subCategoryProductListView.setAdapter(new SubCategoryProductAdapter(activity, CategoryProductList, new SubCategoryProductAdapter.onClickListener() {
            @Override
            public void onFavClick(int position, String product_id, String fav_link, String isFavorite) {
                if (isFavorite.equals("1"))
                    addRemoveWishList(fav_link, position, "remove");
                else
                    addRemoveWishList(fav_link, position, "add");
            }
        }));
    }

    private void noDataError() {
        progress_wheel.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        TYPE = getString(R.string.no_data);
        Glide.with(activity).load(R.drawable.no_data).into(imgError);
    }

    private void noServerError() {
        progress_wheel.setVisibility(View.GONE);
        TYPE = getString(R.string.no_connection);
        Glide.with(activity).load(R.drawable.no_server).into(imgError);
    }

    private void addRemoveWishList(String fav_link, final int position, final String favType) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            System.out.println(StaticDataUtility.APP_TAG + " addRemoveWishList param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " addRemoveWishList param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, fav_link, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " addedToWishList response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("msg");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

//                                        CategoryModel categoryModel = CategoryProductList.get(position);
//
//                                        CategoryProductList.set(position, new HomeProductModel(homeProductModel.getProduct_id(), homeProductModel.getName(),
//                                                homeProductModel.getProduct_url(), homeProductModel.getPrice(), homeProductModel.getDiscount_price(),
//                                                homeProductModel.getDiscount_per(), homeProductModel.getProduct_image(), homeProductModel.getFav_url(),
//                                                homeProductModel.getRemove_url(), homeProductModel.getAvailability(), homeProductModel.getReview_count(),
//                                                favType.equals("add") ? "1" : "0"));
//
//                                        adapter.notifyItemChanged(position);
                                    }
                                });

                            } else {
                                showError(favType);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError(favType);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError(favType);
            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        Volley.newRequestQueue(activity).add(jsonObjReq);
    }

    private void showError(String favType) {
        if (favType.equals("add")) {
            Toast.makeText(activity, "Problem while adding from the wish list. Try again later", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Problem while removing from the wishlist. Try again later", Toast.LENGTH_SHORT).show();
        }

        progressHUD.dismiss();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Glide.clear(imgError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.clear(imgError);
    }
}