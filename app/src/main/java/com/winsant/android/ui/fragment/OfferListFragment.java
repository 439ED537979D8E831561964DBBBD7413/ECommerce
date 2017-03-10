package com.winsant.android.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.winsant.android.R;
import com.winsant.android.adapter.OffersAdapter;
import com.winsant.android.ui.MyApplication;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.utils.VolleyNetWorkCall;
import com.winsant.android.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Fragment class for each nav menu item
 */
public class OfferListFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView viewAllList;
    private ProgressWheel progress_wheel;
    //    private RelativeLayout rl_no_data;
    private TextView toolbar_title;
    private ImageView imgError;
    private ArrayList<String> OfferList;
    private VolleyNetWorkCall netWorkCall;

    private String TYPE = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        netWorkCall = new VolleyNetWorkCall();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        toolbar_title.setText(getString(R.string.title_activity_offer));

        progress_wheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        imgError = (ImageView) view.findViewById(R.id.imgError);

        viewAllList = (RecyclerView) view.findViewById(R.id.viewAllList);
        viewAllList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        imgError.setOnClickListener(this);

        getData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emptyData:

                if (!TYPE.equals(getResources().getString(R.string.no_wish_list_data)))
                    if (CommonDataUtility.checkConnection(activity)) {
                        getData();
                    } else if (TYPE.equals(getResources().getString(R.string.no_data))
                            || TYPE.equals(getResources().getString(R.string.no_connection))) {
                        getData();
                    } else if (TYPE.equals(getResources().getString(R.string.no_internet))) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Offer Fragment");
    }

    private void getData() {

        viewAllList.setVisibility(View.GONE);

        if (CommonDataUtility.checkConnection(activity)) {

            imgError.setVisibility(View.GONE);
            getOfferData();

        } else {

            imgError.setVisibility(View.VISIBLE);
            TYPE = getString(R.string.no_internet);
//            imgError.setImageResource(R.drawable.ico_wifi_off_svg);
            Glide.with(activity).load(R.drawable.ico_wifi_off_svg).into(imgError);
        }
    }

    private void getOfferData() {

        progress_wheel.setVisibility(View.VISIBLE);
        OfferList = new ArrayList<>();

        netWorkCall.makeServiceCall(activity, StaticDataUtility.SERVER_URL + StaticDataUtility.OFFER, new VolleyNetWorkCall.OnResponse() {
            @Override
            public void onSuccessCall(JSONObject response) {

                System.out.println(StaticDataUtility.APP_TAG + " getOfferData response --> " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    final String message = jsonObject.optString("message");
                    final String success = jsonObject.optString("success");

                    if (success.equals("1")) {

                        JSONArray data = jsonObject.optJSONArray("data");

                        if (data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {

                                JSONObject dataObject = data.getJSONObject(i);

                                OfferList.add(dataObject.optString("coupon_image"));
                            }
                        }

                        progress_wheel.setVisibility(View.GONE);

                        if (OfferList.size() > 0) {
                            // TODO : Set Data
                            toolbar_title.setText(getString(R.string.title_activity_offer) + " (" + OfferList.size() + ")");
                            viewAllList.setVisibility(View.VISIBLE);
                            setData();

                        } else {
                            noOfferError();
                        }

                    } else {
                        noOfferError();
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
                    TYPE = getString(R.string.no_connection);
                    Glide.with(activity).load(R.drawable.ico_wifi_off_svg).into(imgError);
//                    imgError.setImageResource(R.drawable.ico_wifi_off_svg);
                } else {
                    noDataError();
                }
            }
        });
    }

    private void setData() {

        OffersAdapter adapter = new OffersAdapter(activity, OfferList);
        viewAllList.setAdapter(adapter);
    }

    private void noDataError() {

        imgError.setVisibility(View.VISIBLE);
        progress_wheel.setVisibility(View.GONE);
        TYPE = getString(R.string.no_data);
//        imgError.setImageResource(R.drawable.ico_no_data_svg);
        Glide.with(activity).load(R.drawable.ico_no_data_svg).into(imgError);
    }

    private void noOfferError() {
        progress_wheel.setVisibility(View.GONE);
        viewAllList.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
//        imgError.setImageResource(R.drawable.ico_offer_svg);
        Glide.with(activity).load(R.drawable.ico_offer_svg).into(imgError);
        TYPE = getString(R.string.no_offer_hint);
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.clear(imgError);
    }
}
