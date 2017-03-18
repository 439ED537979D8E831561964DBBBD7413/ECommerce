package com.winsant.android.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.winsant.android.adapter.WishListProductAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.model.HomeProductModel;
import com.winsant.android.ui.LoginActivity;
import com.winsant.android.ui.MyApplication;
import com.winsant.android.ui.ProductDetailsActivity;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Fragment class for each nav menu item
 */
public class WishListFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView viewAllList;
    private ProgressWheel progress_wheel;
    private TextView toolbar_title;
    private ImageView imgError;
    private ArrayList<HomeProductModel> wishListProductList;
    private KProgressHUD progressHUD;
    private WishListProductAdapter adapter;
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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        toolbar_title.setText(getString(R.string.title_activity_wish_list));

        LinearLayout ll_before_login = (LinearLayout) view.findViewById(R.id.ll_before_login);

        progress_wheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        imgError = (ImageView) view.findViewById(R.id.imgError);

        viewAllList = (RecyclerView) view.findViewById(R.id.viewAllList);
        viewAllList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        imgError.setOnClickListener(this);
        ll_before_login.setOnClickListener(this);

        if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
            ll_before_login.setVisibility(View.GONE);
            getData();
        } else {
            viewAllList.setVisibility(View.GONE);
            ll_before_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgError:

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

            case R.id.ll_before_login:
                startActivity(new Intent(activity, LoginActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("WishList Fragment");
    }

    private void getData() {

        viewAllList.setVisibility(View.GONE);

        if (CommonDataUtility.checkConnection(activity)) {

            imgError.setVisibility(View.GONE);
            getWishListData();

        } else {

            imgError.setVisibility(View.VISIBLE);
            TYPE = getString(R.string.no_internet);
            Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
        }
    }

    private void getWishListData() {

        progress_wheel.setVisibility(View.VISIBLE);
        wishListProductList = new ArrayList<>();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            System.out.println(StaticDataUtility.APP_TAG + " getWishListData param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getWishListData param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.WISHLIST, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " getWishListData response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("msg");
                            final String success = jsonObject.optString("success");
                            final String is_login = jsonObject.optString("is_login");

                            if (success.equals("1")) {

                                JSONArray data = jsonObject.optJSONArray("data");

                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {

                                        JSONObject dataObject = data.getJSONObject(i);

                                        wishListProductList.add(new HomeProductModel(dataObject.optString("product_id"), dataObject.optString("product_name"),
                                                dataObject.optString("product_link"), dataObject.optString("price"), dataObject.optString("discount_price"),
                                                dataObject.optString("discount_per"), dataObject.optString("product_image"), "", dataObject.optString("remove_link"),
                                                dataObject.optString("availability"), dataObject.optString("review_count"), dataObject.optString("is_wishlist")));
                                    }
                                }

                                progress_wheel.setVisibility(View.GONE);

                                if (wishListProductList.size() > 0) {
                                    // TODO : Set Data
                                    toolbar_title.setText(getString(R.string.title_activity_wish_list) + " (Total " + wishListProductList.size() + " Products)");
                                    Toast.makeText(activity, "Total " + wishListProductList.size() + " Products", Toast.LENGTH_SHORT).show();

                                    viewAllList.setVisibility(View.VISIBLE);
                                    setData();

                                } else {
                                    viewAllList.setVisibility(View.GONE);
                                    imgError.setVisibility(View.VISIBLE);
                                    TYPE = getString(R.string.no_wish_list_data);
                                    Glide.with(activity).load(R.drawable.no_wishlist).into(imgError);
                                }

                            } else {

                                progress_wheel.setVisibility(View.GONE);

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
                    TYPE = getString(R.string.no_connection);
                    Glide.with(activity).load(R.drawable.no_server).into(imgError);
                } else {
                    noDataError();
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

        adapter = new WishListProductAdapter(activity, wishListProductList,
                new WishListProductAdapter.onClickListener() {
                    @Override
                    public void onClick(int position, String product_id, String product_url) {

                        // TODO : Specific Product Details Display Activity
                        Intent intent = new Intent(activity, ProductDetailsActivity.class);
                        intent.putExtra("url", product_url);
                        intent.putExtra("name", wishListProductList.get(position).getName());
                        startActivity(intent);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                    @Override
                    public void onDeleteClick(int position, String product_id, String remove_link) {

                        // TODO : Delete Specific Product From WishList
                        ConditionDialog(remove_link, position);
                    }
                });

        viewAllList.setAdapter(adapter);
    }

    private void noDataError() {

        progress_wheel.setVisibility(View.GONE);
        TYPE = getString(R.string.no_data);
        Glide.with(activity).load(R.drawable.no_server).into(imgError);
    }

    private void ConditionDialog(final String remove_url, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_change_pincode, null);
        dialog.setContentView(dialogView);

        dialogView.findViewById(R.id.txtAvailability).setVisibility(View.GONE);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtTitle.setText("Terms & Condition");

        TextView txtTerms = (TextView) dialogView.findViewById(R.id.txtTerms);
        txtTerms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        txtTerms.setVisibility(View.VISIBLE);
        txtTerms.setText("Want to remove product from wishlist??");

        dialogView.findViewById(R.id.edtPinCode).setVisibility(View.GONE);

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace(activity));
        btnOK.setTag(getString(R.string.yes));

        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(CommonDataUtility.setTypeFace(activity));
        btnCancel.setTag(getString(R.string.no));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                removeFromWishList(remove_url, position);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void removeFromWishList(String remove_url, final int position) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            System.out.println(StaticDataUtility.APP_TAG + " removeFromWishList param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " removeFromWishList param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, remove_url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " removeFromWishList response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("msg");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wishListProductList.remove(position);
                                        adapter.notifyDataSetChanged();
//                                        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                                    }
                                });

                            } else {
                                showError("remove");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("remove");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("remove");
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

    @Override
    public void onPause() {
        super.onPause();
        Glide.clear(imgError);
    }
}
