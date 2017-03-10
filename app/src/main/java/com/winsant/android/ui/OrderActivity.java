package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.winsant.android.adapter.CancelOrderAdapter;
import com.winsant.android.adapter.OrderAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.model.CancelReason;
import com.winsant.android.model.OrderModel;
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

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private TextView mToolbar_title;

    private ProgressWheel progress_wheel;
    private RelativeLayout rl_cart;
    //    private TextView emptyData;
    private ImageView imgError;
    private ArrayList<OrderModel> orderProductArrayList;
    private String from;
    private ArrayList<CancelReason> cancel_list;

    private String TYPE = "";
    private RecyclerView CartProductList;
    private KProgressHUD progressHUD;
    private AlertDialog alertdialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);

        activity = OrderActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText("My Order");

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

        from = getIntent().getStringExtra("from");

        setUI();
    }

    private void setUI() {

        findViewById(R.id.ll_top).setVisibility(View.GONE);
        findViewById(R.id.tableRow).setVisibility(View.GONE);

        progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
        imgError = (ImageView) findViewById(R.id.imgError);

        CartProductList = (RecyclerView) findViewById(R.id.CartProductList);
        CartProductList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        imgError.setOnClickListener(this);
        getData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emptyData:

                if (!TYPE.equals(getResources().getString(R.string.no_order_data)))
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

        CartProductList.setVisibility(View.GONE);

        if (CommonDataUtility.checkConnection(activity)) {

            imgError.setVisibility(View.GONE);
            getOrderProductData();

        } else {

            imgError.setVisibility(View.VISIBLE);
            TYPE = getString(R.string.no_internet);
            Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
        }
    }

    private void getOrderProductData() {

        progress_wheel.setVisibility(View.VISIBLE);
        orderProductArrayList = new ArrayList<>();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            System.out.println(StaticDataUtility.APP_TAG + " getOrderProductData param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getOrderProductData error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, StaticDataUtility.SERVER_URL + StaticDataUtility.ORDERS_HISTORY, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " getOrderProductData response --> " + response);

                        try {

                            System.out.println(StaticDataUtility.APP_TAG + " getOrderProductData response --> " + response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONArray data = jsonObject.optJSONArray("data");

                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {

                                        JSONObject dataObject = data.getJSONObject(i);

                                        orderProductArrayList.add(new OrderModel(dataObject.optString("product_id"), dataObject.optString("product_name"),
                                                dataObject.optString("product_image"), dataObject.optString("price"), dataObject.optString("brand_name"),
                                                dataObject.optString("qty"), dataObject.optString("product_color"), dataObject.optString("product_size"),
                                                dataObject.optString("return_url"), dataObject.optString("cancel_url"), dataObject.optString("complaint_url"),
                                                dataObject.optString("seller_feed_back_url"), dataObject.optString("product_feed_back_url"),
                                                dataObject.optString("order_received_date"), dataObject.optString("is_cancel"), dataObject.optString("order_products_id"),
                                                dataObject.optString("order_status"), dataObject.optString("order_delivered_date"),
                                                dataObject.optString("order_ready_for_dispatch_date"), dataObject.optString("order_cancel_date"),
                                                dataObject.optString("order_return_date")));
                                    }


                                    // TODO : Set Data
                                    setData();

                                    mToolbar_title.setText("My Order (" + orderProductArrayList.size() + ")");

                                    Toast.makeText(activity, "Total " + orderProductArrayList.size() + " Products", Toast.LENGTH_SHORT).show();

                                    progress_wheel.setVisibility(View.GONE);
                                    imgError.setVisibility(View.GONE);
                                    CartProductList.setVisibility(View.VISIBLE);

                                } else {

                                    progress_wheel.setVisibility(View.GONE);
                                    if (orderProductArrayList.size() == 0) {
                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        EmptyCart();
                                    }
                                }
                            } else {

                                progress_wheel.setVisibility(View.GONE);
                                if (orderProductArrayList.size() == 0) {
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    EmptyCart();
                                }
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
                CartProductList.setVisibility(View.GONE);
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

    private void setData() {

        if (orderProductArrayList.size() > 0) {
            OrderAdapter orderAdapter = new OrderAdapter(activity, orderProductArrayList, new OrderAdapter.onClickListener() {
                @Override
                public void onReturnClick(int position, String remove_url, String product_id, String is_cancel) {
                    if (is_cancel.equals("1")) {
                        setCancelReason();
                        cancelRequestDialog(remove_url, is_cancel);
                    } else {
                        setReturnReason();
                        returnRequestDialog(remove_url, is_cancel);
                    }
                }
            });

            CartProductList.setAdapter(orderAdapter);
        }
    }

    private void noDataError() {
        progress_wheel.setVisibility(View.GONE);
        TYPE = getString(R.string.no_data);
        Glide.with(activity).load(R.drawable.no_data).into(imgError);
    }

    private void noServerError() {
        progress_wheel.setVisibility(View.GONE);
        TYPE = getString(R.string.no_connection);
        Glide.with(activity).load(R.drawable.no_server).into(imgError);
    }

    private void setCancelReason() {
        cancel_list = new ArrayList<>();
        cancel_list.add(new CancelReason("-1", "Select Cancel Reason"));
        cancel_list.add(new CancelReason("1", "Order Placed by Mistake"));
        cancel_list.add(new CancelReason("2", "Expected Delivery time is too long"));
        cancel_list.add(new CancelReason("3", "Delivery is delayed"));
        cancel_list.add(new CancelReason("4", "Purchased it from somewhere else"));
        cancel_list.add(new CancelReason("5", "Need to change Delivery Address"));
        cancel_list.add(new CancelReason("6", "Item Price is too High"));
        cancel_list.add(new CancelReason("7", "Other"));
    }

    private void setReturnReason() {
        cancel_list = new ArrayList<>();
        cancel_list.add(new CancelReason("-1", "Select Return Reason"));
        cancel_list.add(new CancelReason("1", "Incorrect Product or Size Ordered"));
        cancel_list.add(new CancelReason("2", "Product No Longer Needed"));
        cancel_list.add(new CancelReason("3", "Product Did Not Match Description on Website or in Catalog"));
        cancel_list.add(new CancelReason("4", "Product Did Not Meet Customer’s Expectations"));
        cancel_list.add(new CancelReason("5", "Company Shipped Wrong Product or Size"));
        cancel_list.add(new CancelReason("6", "Deliberate Fraud"));
        cancel_list.add(new CancelReason("7", "Item(s) were missing"));
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
    protected void onStop() {
        super.onStop();
        Glide.clear(imgError);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Glide.clear(imgError);
        if (from.equals("order")) {
            startActivity(new Intent(activity, HomeActivity.class));
            finish();
        } else {
            finish();
        }
    }

    private void RemoveFromOrder(String remove_link, final String is_cancel, String cancel_Reason, String cancel_cat_id) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            if (is_cancel.equals("1")) {
                obj.put("cancel_reason", cancel_Reason);
                obj.put("cancel_cat_id", cancel_cat_id);
            } else {
                obj.put("return_reason", cancel_Reason);
                obj.put("return_cat_id", cancel_cat_id);
            }
            System.out.println(StaticDataUtility.APP_TAG + " RemoveFromCart param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " RemoveFromCart error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, remove_link, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " RemoveFromCart response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        getData();
                                        CommonDataUtility.showSnackBar(rl_cart, message);

                                        mToolbar_title.setText("My Order (" + orderProductArrayList.size() + ")");

                                        if (orderProductArrayList.size() == 0)
                                            EmptyCart();
                                    }
                                });

                            } else {
                                showError(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (is_cancel.equals("1"))
                                showError("Problem while cancel your order.Please try again later");
                            else
                                showError("Problem while return your order.Please try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (is_cancel.equals("1"))
                    showError("Problem while cancel your order.Please try again later");
                else
                    showError("Problem while return your order.Please try again later");
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

    private void EmptyCart() {

        TYPE = getString(R.string.no_order_data);
        imgError.setVisibility(View.VISIBLE);
        Glide.with(activity).load(R.drawable.no_order).into(imgError);
    }

    private void showError(String message) {
        CommonDataUtility.showSnackBar(rl_cart, message);
        progressHUD.dismiss();
    }

    private void cancelRequestDialog(final String remove_url, final String is_cancel) {

        activity.getLayoutInflater();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptView1 = inflater.inflate(R.layout.ride_request_cancel_alert, null);
        Button btnNo = (Button) promptView1.findViewById(R.id.btnNo);
        Button btnYes = (Button) promptView1.findViewById(R.id.btnYes);
        final Spinner sp_reasone = (Spinner) promptView1.findViewById(R.id.sp_reason);

        TextView txtTitle = (TextView) promptView1.findViewById(R.id.txtTitle);
        txtTitle.setText("Order Cancel Request ?");
        TextView txtMsg = (TextView) promptView1.findViewById(R.id.txtMsg);
        txtMsg.setText("Are you sure you want to cancel this order");

        final EditText edtReason = (EditText) promptView1.findViewById(R.id.edtReason);

        CancelOrderAdapter adapter = new CancelOrderAdapter(activity, R.layout.row_city, R.id.lbl_name, cancel_list);
        sp_reasone.setAdapter(adapter);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelReason selectedItem = (CancelReason) sp_reasone.getSelectedItem();

                if (selectedItem.getName().equals("Select Cancel Reason")) {
                    Toast.makeText(activity, "Select cancel reason first.", Toast.LENGTH_SHORT).show();
                } else if (edtReason.getText().toString().equals("")) {
                    Toast.makeText(activity, "Enter cancel reason.", Toast.LENGTH_SHORT).show();
                } else if (CommonDataUtility.checkConnection(activity)) {
                    String Cancel_Reason = edtReason.getText().toString();
                    String cancel_cat_id = "";
                    for (int i = 0; i < cancel_list.size(); i++) {
                        if (cancel_list.get(i).getName().equals(selectedItem.getName())) {
                            cancel_cat_id = cancel_list.get(i).getId();
                            break;
                        }
                    }
                    RemoveFromOrder(remove_url, is_cancel, Cancel_Reason, cancel_cat_id);
                    alertdialog.dismiss();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertdialog = alertDialogBuilder.create();
        alertdialog.setView(promptView1);
        alertdialog.setCancelable(false);
        alertdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertdialog.show();
    }

    private void returnRequestDialog(final String remove_url, final String is_cancel) {

        activity.getLayoutInflater();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptView1 = inflater.inflate(R.layout.ride_request_cancel_alert, null);
        Button btnNo = (Button) promptView1.findViewById(R.id.btnNo);
        Button btnYes = (Button) promptView1.findViewById(R.id.btnYes);
        final Spinner sp_reasone = (Spinner) promptView1.findViewById(R.id.sp_reason);

        TextView txtTitle = (TextView) promptView1.findViewById(R.id.txtTitle);
        txtTitle.setText("Order Return Request ?");
        TextView txtMsg = (TextView) promptView1.findViewById(R.id.txtMsg);
        txtMsg.setText("Are you sure you want to return this order");

        final EditText edtReason = (EditText) promptView1.findViewById(R.id.edtReason);

        CancelOrderAdapter adapter = new CancelOrderAdapter(activity, R.layout.row_city, R.id.lbl_name, cancel_list);
        sp_reasone.setAdapter(adapter);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelReason selectedItem = (CancelReason) sp_reasone.getSelectedItem();

                if (selectedItem.getName().equals("Select Return Reason")) {
                    Toast.makeText(activity, "Select return reason first.", Toast.LENGTH_SHORT).show();
                } else if (edtReason.getText().toString().equals("")) {
                    Toast.makeText(activity, "Enter cancel reason.", Toast.LENGTH_SHORT).show();
                } else if (CommonDataUtility.checkConnection(activity)) {
                    String Return_Reason = edtReason.getText().toString();
                    String cancel_cat_id = "";
                    for (int i = 0; i < cancel_list.size(); i++) {
                        if (cancel_list.get(i).getName().equals(selectedItem.getName())) {
                            cancel_cat_id = cancel_list.get(i).getId();
                            break;
                        }
                    }
                    RemoveFromOrder(remove_url, is_cancel, Return_Reason, cancel_cat_id);
                    alertdialog.dismiss();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertdialog = alertDialogBuilder.create();
        alertdialog.setView(promptView1);
        alertdialog.setCancelable(false);
        alertdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertdialog.show();
    }
}