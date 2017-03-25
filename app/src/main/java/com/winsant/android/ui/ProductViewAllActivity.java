package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.winsant.android.adapter.ProductViewAllAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.model.HomeProductModel;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pc-Android-1 on 10/14/2016.
 * <p>
 * TODO : Home Page/SubCategory View All Product Display Activity
 */

public class ProductViewAllActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private TextView mToolbar_title;

    private ProgressWheel progress_wheel;
    //    private RelativeLayout rl_no_data;
//    private TextView emptyData;
    private ImageView imgError;
    private ArrayList<HomeProductModel> productArrayList;
    private ProductViewAllAdapter adapter;

    private boolean isGrid = true;
    private RecyclerView viewAllList;
    private String url, type, name;
    private ImageView imgType;
    private KProgressHUD progressHUD;
    private String TYPE = "";
    private String totalProduct;
    private int offset = 0;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    // Load More
    private boolean isLoading = false;
    private int visibleThreshold = 4;
    private int lastVisibleItem, totalItemCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view_all);

        activity = ProductViewAllActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        if (getIntent().hasExtra("name"))
            name = getIntent().getStringExtra("name");
        else
            name = getString(R.string.app_name);

        mToolbar_title.setText(name);
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

        setUI();
    }

    private void setUI() {

        findViewById(R.id.ll_top).setVisibility(View.VISIBLE);

        imgType = (ImageView) findViewById(R.id.imgType);

        progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        imgError = (ImageView) findViewById(R.id.imgError);

        viewAllList = (RecyclerView) findViewById(R.id.viewAllList);
        imgError.setOnClickListener(this);
        imgType.setOnClickListener(this);

//        viewAllList.addOnScrollListener(new EndlessRecyclerOnScrollListener(activity, type, totalProduct) {
//            @Override
//            public void onLoadMore() {
//                getProductViewAllData();
//            }
//        });

        viewAllList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (type.equals("g")) {
                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                } else {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                }

                if (Integer.parseInt(totalProduct) > offset)
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        isLoading = true;
                        getProductViewAllData();
                    }
            }
        });

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

            case R.id.imgType:

                if (isGrid) {
                    type = "v";
                    imgType.setImageResource(R.drawable.ico_list_svg);
                    isGrid = false;
                } else {
                    type = "g";
                    imgType.setImageResource(R.drawable.ico_grid_svg);
                    isGrid = true;
                }
                setData();

                break;

            default:
                break;
        }

    }

    private void getData() {

        type = "g";
        productArrayList = new ArrayList<>();
        viewAllList.setVisibility(View.GONE);

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

        if (isLoading) {
            progressHUD = KProgressHUD.create(activity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false).show();
        } else {
            progress_wheel.setVisibility(View.VISIBLE);
        }

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("offset", offset);
            System.out.println(StaticDataUtility.APP_TAG + " getProductViewAllData param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getProductViewAllData param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            System.out.println(StaticDataUtility.APP_TAG + " getProductViewAllData response --> " + response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                offset = jsonObject.optInt("offset");
                                totalProduct = jsonObject.optString("totalProduct");

                                if (isLoading)
                                    if (type.equals("g"))
                                        gridLayoutManager.scrollToPosition(adapter.getItemCount() - 1);
                                    else
                                        linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);

                                JSONArray data = jsonObject.optJSONArray("data");

                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {

                                        JSONObject dataObject = data.getJSONObject(i);

                                        productArrayList.add(new HomeProductModel(dataObject.optString("product_id"), dataObject.optString("product_name"),
                                                dataObject.optString("product_link"), dataObject.optString("price"), dataObject.optString("discount_price"),
                                                dataObject.optString("discount_per"), dataObject.optString("product_image"), dataObject.optString("fav_link")
                                                , dataObject.optString("remove_link"), dataObject.optString("availability"), dataObject.optString("review_count"),
                                                dataObject.optString("is_wishlist")));
                                    }

                                    // TODO : Set Data
                                    setData();
                                    Toast.makeText(activity, productArrayList.size() + "/" + String.valueOf(totalProduct) + " Products", Toast.LENGTH_SHORT).show();
                                    // mToolbar_title.setText(name + " (" + productArrayList.size() + "/" + String.valueOf(totalProduct) + " Products)");

                                    if (isLoading)
                                        progressHUD.dismiss();
                                    else
                                        progress_wheel.setVisibility(View.GONE);
                                    viewAllList.setVisibility(View.VISIBLE);
                                    isLoading = false;

                                } else {
                                    noDataError();
                                }

                            } else {
                                noDataError();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            noDataError();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress_wheel.setVisibility(View.GONE);
                viewAllList.setVisibility(View.GONE);
                imgError.setVisibility(View.VISIBLE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    TYPE = getString(R.string.no_internet);
                    Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
                } else {
                    noServerError();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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

    private void setData() {

        if (isLoading) {
            System.out.println(StaticDataUtility.APP_TAG + " total count --> " + productArrayList.size());
            adapter.notifyItemChanged(adapter.getItemCount());
        } else {

            if (type.equals("g")) {

                if (CommonDataUtility.isTablet(activity))
                    gridLayoutManager = new GridLayoutManager(activity, 3);
                else
                    gridLayoutManager = new GridLayoutManager(activity, 2);
                viewAllList.setLayoutManager(gridLayoutManager);

            } else {
                linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                viewAllList.setLayoutManager(linearLayoutManager);
            }

            adapter = new ProductViewAllAdapter(activity, productArrayList, new ProductViewAllAdapter.onClickListener() {
                @Override
                public void onClick(int position, String product_id, String product_url) {

                    // TODO : Specific Product Details Display Activity
                    Intent intent = new Intent(activity, ProductDetailsActivity.class);
                    intent.putExtra("url", product_url);
                    intent.putExtra("name", productArrayList.get(position).getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                @Override
                public void onFavClick(int position, String product_id, String fav_link, String isFavorite) {

                    if (isFavorite.equals("1"))
                        addRemoveWishList(fav_link, position, "remove");
                    else
                        addRemoveWishList(fav_link, position, "add");
                }
            }, type);

            viewAllList.setAdapter(adapter);
        }
    }

    private void noDataError() {

        if (isLoading)
            progressHUD.dismiss();
        else
            progress_wheel.setVisibility(View.GONE);

        viewAllList.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        TYPE = getString(R.string.no_data);
        Glide.with(activity).load(R.drawable.no_data).into(imgError);
        isLoading = false;
    }

    private void noServerError() {

        if (isLoading)
            progressHUD.dismiss();
        else
            progress_wheel.setVisibility(View.GONE);

        viewAllList.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        TYPE = getString(R.string.no_connection);
        Glide.with(activity).load(R.drawable.no_server).into(imgError);
        isLoading = false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.activity_action_cart)).setIcon(R.drawable.ico_menu_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case 1:
//
//                Toast.makeText(this, "Cart Click", Toast.LENGTH_SHORT).show();
//
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                fav_link, obj,
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

                                        HomeProductModel homeProductModel = productArrayList.get(position);

                                        productArrayList.set(position, new HomeProductModel(homeProductModel.getProduct_id(), homeProductModel.getName(),
                                                homeProductModel.getProduct_url(), homeProductModel.getPrice(), homeProductModel.getDiscount_price(),
                                                homeProductModel.getDiscount_per(), homeProductModel.getProduct_image(), homeProductModel.getFav_url(),
                                                homeProductModel.getRemove_url(), homeProductModel.getAvailability(), homeProductModel.getReview_count(),
                                                favType.equals("add") ? "1" : "0"));

                                        adapter.notifyItemChanged(position);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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
}

//    private boolean checkPermissionGallery() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                return false;
//
//            } else {
//                return true;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    intent();
//                else
//                    Toast.makeText(activity, "Please give permission for storing image to sdcard!!", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//    private void intent() {
//        Intent intent = new Intent(activity, FullScreenViewActivity.class);
//        intent.putExtra("type", type);
//        intent.putExtra("tag", tag);
//        intent.putExtra("imagesList", imageArrayList);
//        intent.putExtra("position", Position);
//        activity.startActivity(intent);
//    }

//    private void getImages() {
//
//        imageArrayList = new ArrayList<>();
//
//        progressHUD = KProgressHUD.create(activity)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setCancellable(false).show();
//
//        JSONObject obj = new JSONObject();
//        try {
//
//            obj.put("secret", StaticDataUtility.SecretKey);
//            obj.put("flag", tag);
//
//            System.out.println(StaticDataUtility.LOG_TAG + " getImages param --> " + obj.toString());
//
//        } catch (Exception e) {
//            System.out.println(StaticDataUtility.LOG_TAG + " getImages param error --> " + e.toString());
//        }
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                StaticDataUtility.Server_url + StaticDataUtility.getImages, obj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            System.out.println(StaticDataUtility.LOG_TAG + " getImages response --> " + response.toString());
//
//                            JSONObject jsonObject = new JSONObject(response.toString());
//                            final String message = jsonObject.optString("msg");
//
//                            activity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                            if (jsonObject.has("error")) {
//                                String status = jsonObject.optString("error");
//
//                                if (status.equals("false")) {
//
//                                    JSONArray data = jsonObject.optJSONArray("data");
//                                    imageArrayList.clear();
//
//                                    for (int i = 0; i < data.length(); i++) {
//                                        imageArrayList.add(new ImageBean(String.valueOf(i),
//                                                jsonObject.optString("IMAGE_URL") + "/" + data.optString(i)));
//                                    }
//
//                                    dismissDialog();
//
//                                    mToolbar_title.setText(String.format("%s Rangoli Design (%S)", type, String.valueOf(imageArrayList.size())));
//
//                                    CategoryImagesList.setAdapter(new CategoryImagesAdapter(activity, imageArrayList, type, new CategoryImagesAdapter.ClickListener() {
//                                        @Override
//                                        public void onClick(int position) {
//                                            Position = position;
//                                            if (checkPermissionGallery()) {
//                                                intent();
//                                            }
//                                        }
//                                    }));
//
//                                } else {
//
//                                    dismissDialog();
//                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                dismissDialog();
//                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//
//                            dismissDialog();
//                            Toast.makeText(activity, "Something went wrong,Try again later.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                dismissDialog();
//                System.out.println(StaticDataUtility.LOG_TAG + " error --> " + error.toString());
//
//                if (error instanceof NoConnectionError) {
//                    Toast.makeText(activity, activity.getString(R.string.connect_to_internet), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(activity, "Something went wrong,Try again later.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }) {
//            /**
//             * Passing some request headers
//             */
//            @Override
//            public Map getHeaders() throws AuthFailureError {
//                HashMap headers = new HashMap();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        // Adding request to request queue
//        Volley.newRequestQueue(activity).add(jsonObjReq);
//    }
//
//    private void dismissDialog() {
//        progressHUD.dismiss();
//    }