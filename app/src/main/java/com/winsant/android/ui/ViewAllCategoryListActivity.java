package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.winsant.android.R;
import com.winsant.android.adapter.AllCategoryListAdapter;
import com.winsant.android.model.CategoryModel;
import com.winsant.android.model.SubCategoryModel;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.utils.VolleyNetWorkCall;
import com.winsant.android.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pc-Android-1 on 10/14/2016.
 * <p>
 * TODO : Home Page View All Category Display Activity
 */

public class ViewAllCategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private TextView mToolbar_title;

    private ProgressWheel progress_wheel;
    // private RelativeLayout rl_no_data;
    // private TextView emptyData;
    private ImageView imgError;
    private ArrayList<CategoryModel> AllCategoryList;
    private ArrayList<SubCategoryModel> SubCategoryList;
    private String position;
    private String TYPE = "";
    private RecyclerView viewAllList;

    private VolleyNetWorkCall netWorkCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view_all);

        activity = ViewAllCategoryListActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        mToolbar_title.setText(R.string.all_categories);
        position = getIntent().getStringExtra("position");

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

        progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        imgError = (ImageView) findViewById(R.id.imgError);

        viewAllList = (RecyclerView) findViewById(R.id.viewAllList);
        viewAllList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

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

        progress_wheel.setVisibility(View.VISIBLE);
        AllCategoryList = new ArrayList<>();

        netWorkCall.makeServiceCall(activity, StaticDataUtility.SERVER_URL + StaticDataUtility.CATEGORY, new VolleyNetWorkCall.OnResponse() {
            @Override
            public void onSuccessCall(JSONObject response) {

                try {

                    System.out.println(StaticDataUtility.APP_TAG + " all category response --> " + response.toString());

                    JSONObject jsonObject = new JSONObject(response.toString());
                    final String message = jsonObject.optString("msg");
                    final String success = jsonObject.optString("success");

                    if (success.equals("1")) {

                        JSONArray category = jsonObject.optJSONArray("category");

                        if (category.length() > 0) {
                            for (int i = 0; i < category.length(); i++) {

                                SubCategoryList = new ArrayList<>();

                                JSONObject dataObject = category.getJSONObject(i);

                                JSONArray sub_category = dataObject.optJSONArray("sub_category");
                                for (int j = 0; j < sub_category.length(); j++) {

                                    JSONObject subCategoryObject = sub_category.optJSONObject(j);

                                    SubCategoryList.add(new SubCategoryModel(subCategoryObject.optString("category_name"), subCategoryObject.optString("category_image")
                                            , subCategoryObject.optString("category_url"), subCategoryObject.optString("is_last")));
                                }

                                AllCategoryList.add(new CategoryModel(dataObject.optString("category_name"), dataObject.optString("category_image"),
                                        dataObject.optString("category_url"), SubCategoryList));
                            }
                        }

                        // TODO : Set Data
                        setData();

                        progress_wheel.setVisibility(View.GONE);
                        viewAllList.setVisibility(View.VISIBLE);

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
                viewAllList.setVisibility(View.GONE);
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

    private void setData() {

        AllCategoryListAdapter adapter = new AllCategoryListAdapter(activity, AllCategoryList, viewAllList);
        viewAllList.setAdapter(adapter);
        viewAllList.scrollToPosition(Integer.parseInt(position));
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

}