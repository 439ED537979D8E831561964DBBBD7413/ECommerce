package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.winsant.android.R;
import com.winsant.android.adapter.AddressListAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.model.AddressModel;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.DividerItemDecoration;
import com.winsant.android.utils.StaticDataUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title;
    private RelativeLayout ll_address;
    private RecyclerView AddressList;
    private TextView emptyAddress;
    private String TotalPrice, from;
    private Button btnSetDefault;
    private TableRow tableRow;
    private ArrayList<AddressModel> addressModels;
    private AddressListAdapter addressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_address);

        activity = AllAddressActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(getString(R.string.title_activity_select_address));

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

        ll_address = (RelativeLayout) findViewById(R.id.ll_address);
        tableRow = (TableRow) findViewById(R.id.tableRow);

        btnSetDefault = (Button) findViewById(R.id.btnSetDefault);
        btnSetDefault.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnSetDefault.setOnClickListener(this);

        TextView txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtTotalPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

        if (getIntent().hasExtra("total")) {
            TotalPrice = getIntent().getStringExtra("total");
            txtTotalPrice.setText(activity.getResources().getString(R.string.Rs) + " " + TotalPrice);
        } else {
            TotalPrice = "0.0";
            txtTotalPrice.setText(activity.getResources().getString(R.string.Rs) + TotalPrice);
        }

        from = getIntent().getStringExtra("from");

        emptyAddress = (TextView) findViewById(R.id.emptyAddress);
        emptyAddress.setOnClickListener(this);

        AddressList = (RecyclerView) findViewById(R.id.AddressList);
        AddressList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        TextView txtAddAddress = (TextView) findViewById(R.id.txtAddAddress);
        txtAddAddress.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        txtAddAddress.setOnClickListener(this);

        Button btnDelivery = (Button) findViewById(R.id.btnDelivery);
        btnDelivery.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnDelivery.setOnClickListener(this);

        if (CommonDataUtility.isTablet(activity)) {
            btnDelivery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            btnSetDefault.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            txtAddAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        } else {
            btnDelivery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            btnSetDefault.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            txtAddAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
    }

    private void getData() {

        AddressList.setVisibility(View.GONE);
        if (from.equals("profile")) {
            btnSetDefault.setVisibility(View.VISIBLE);
            tableRow.setVisibility(View.GONE);
        } else {
            btnSetDefault.setVisibility(View.GONE);
            tableRow.setVisibility(View.VISIBLE);
        }

        if (CommonDataUtility.checkConnection(activity)) {

            emptyAddress.setVisibility(View.GONE);
            getAllAddress();

        } else {

            emptyAddress.setVisibility(View.VISIBLE);
            emptyAddress.setText(R.string.no_internet);
            emptyAddress.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_wifi_off_svg, 0, 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public void onClick(View v) {
        //8866708550
        switch (v.getId()) {

            case R.id.emptyAddress:

                if (emptyAddress.getText().toString().equals("Your address list is empty!!\n Please add address for delivery of products.")) {

                    addAddress();

                } else {
                    if (CommonDataUtility.checkConnection(activity)) {
                        getData();
                    } else if (emptyAddress.getText().toString().equals(getResources().getString(R.string.no_data))
                            || emptyAddress.getText().toString().equals(getResources().getString(R.string.no_connection))) {
                        getData();
                    } else if (emptyAddress.getText().toString().equals(getResources().getString(R.string.no_internet))) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }

                break;

            case R.id.btnDelivery:

                if (CommonDataUtility.checkConnection(activity)) {
                    if (addressModels.size() > 0) {
                        for (int i = 0; i < addressModels.size(); i++) {
                            if (addressModels.get(i).getIsSelected()) {

                                Intent intent = new Intent(activity, SelectPaymentActivity.class);
                                intent.putExtra("total", String.valueOf(TotalPrice));
                                intent.putExtra("address_id", addressModels.get(i).getAddress_id());
                                intent.putExtra("is_cod", addressModels.get(i).getIs_cod());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                // getShippingPrice(addressModels.get(i).getZipcode(), addressModels.get(i).getAddress_id(), addressModels.get(i).getIs_cod());
                                return;
                            }
                        }
                    } else {
                        CommonDataUtility.showSnackBar(ll_address, "Please add address for delivery");
                    }
                } else {
                    CommonDataUtility.showSnackBar(ll_address, getString(R.string.no_internet));
                }

                break;

            case R.id.btnSetDefault:

                if (CommonDataUtility.checkConnection(activity)) {
                    if (addressModels.size() > 0) {
                        for (int i = 0; i < addressModels.size(); i++) {
                            if (addressModels.get(i).getIsSelected()) {
                                manage_address_default(i, addressModels.get(i).getAddress_id());
                                return;
                            }
                        }
                    } else {
                        CommonDataUtility.showSnackBar(ll_address, "No addressees available for set as default Address");
                    }
                } else {
                    CommonDataUtility.showSnackBar(ll_address, getString(R.string.no_internet));
                }

                break;

            case R.id.txtAddAddress:

                addAddress();

                break;
        }
    }

    private void addAddress() {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        intent.putExtra("type", "add");
        intent.putExtra("address_id", "0");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void getAllAddress() {

        addressModels = new ArrayList<>();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            System.out.println(StaticDataUtility.APP_TAG + " getAllAddress param --> " + obj.toString());
        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getAllAddress param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.ADDRESS, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " getAllAddress response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONArray data = jsonObject.optJSONArray("data");

                                if (data.length() > 0) {

                                    for (int i = 0; i < data.length(); i++) {

                                        JSONObject dataObject = data.optJSONObject(i);

                                        addressModels.add(new AddressModel(dataObject.optString("address_id"), dataObject.optString("first_name"),
                                                dataObject.optString("last_name"), dataObject.optString("address"), dataObject.optString("zipcode")
                                                , dataObject.optString("phone"), dataObject.optString("mobile"), dataObject.optString("landmark")
                                                , dataObject.optString("country"), dataObject.optString("state"), dataObject.optString("city"),
                                                dataObject.optString("is_default").equals("1"), dataObject.optString("is_cod")));
                                    }

                                    emptyAddress.setVisibility(View.GONE);
                                    AddressList.setVisibility(View.VISIBLE);

                                    setData();

                                } else {

                                    emptyAddress.setVisibility(View.VISIBLE);
                                    btnSetDefault.setVisibility(View.GONE);
                                    AddressList.setVisibility(View.GONE);
                                    emptyAddress.setText("Your address list is empty!!\n Please add address for delivery of products.");
                                }

                                progressHUD.dismiss();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_address, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showErrorView();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorView();
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

    private void showErrorView() {

        progressHUD.dismiss();
        CommonDataUtility.showSnackBar(ll_address, getString(R.string.no_data));

        emptyAddress.setVisibility(View.VISIBLE);
        emptyAddress.setText(getString(R.string.no_data));
        emptyAddress.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_replay_svg, 0, 0);
    }

    private void setData() {

        addressListAdapter = new AddressListAdapter(activity, addressModels, new AddressListAdapter.onClickListener() {
            @Override
            public void onClick(int position, String address_id, String address) {

                Intent intent = new Intent(activity, AddAddressActivity.class);
                intent.putExtra("type", "update");
                intent.putExtra("address_id", address_id);
                intent.putExtra("data", addressModels.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onDeleteClick(int position, String address_id, boolean isSelected) {
                //System.out.println(StaticDataUtility.APP_TAG + " address_id --> " + address_id + " -- " + position);
                remove_address(position, address_id, isSelected);
            }
        }, from);

        AddressList.setAdapter(addressListAdapter);
        AddressList.addItemDecoration(new DividerItemDecoration(activity, R.drawable.divider));
    }

    private void manage_address_default(final int position, String address_id) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("address_id", address_id);

            System.out.println(StaticDataUtility.APP_TAG + " MANAGE_ADDRESS_DEFAULT param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " MANAGE_ADDRESS_DEFAULT error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.MANAGE_ADDRESS_DEFAULT, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " MANAGE_ADDRESS_DEFAULT response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        AddressModel addressModel = addressModels.get(position);

                                        addressModels.set(position, new AddressModel(addressModel.getAddress_id(), addressModel.getFirst_name(),
                                                addressModel.getLast_name(), addressModel.getAddress(), addressModel.getZipcode(), addressModel.getPhone(),
                                                addressModel.getMobile(), addressModel.getLandmark(), addressModel.getCountry(), addressModel.getState(),
                                                addressModel.getCity(), true, addressModel.getIs_cod()));

                                        for (int i = 0; i < addressModels.size(); i++) {
                                            AddressModel addressModel1 = addressModels.get(i);
                                            if (i == position)
                                                addressModels.set(position, new AddressModel(addressModel1.getAddress_id(), addressModel1.getFirst_name(),
                                                        addressModel1.getLast_name(), addressModel1.getAddress(), addressModel1.getZipcode(), addressModel1.getPhone(),
                                                        addressModel1.getMobile(), addressModel1.getLandmark(), addressModel1.getCountry(), addressModel1.getState(),
                                                        addressModel1.getCity(), true, addressModel.getIs_cod()));
                                            else
                                                addressModels.set(i, new AddressModel(addressModel1.getAddress_id(), addressModel1.getFirst_name(),
                                                        addressModel1.getLast_name(), addressModel1.getAddress(), addressModel1.getZipcode(), addressModel1.getPhone(),
                                                        addressModel1.getMobile(), addressModel1.getLandmark(), addressModel1.getCountry(), addressModel1.getState(),
                                                        addressModel1.getCity(), false, addressModel.getIs_cod()));
                                        }

                                        addressListAdapter.notifyDataSetChanged();
                                    }
                                });

                            } else {
                                showError("Problem while set address as default. Try again later");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Problem while set address as default. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("Problem while set address as default. Try again later");
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

    private void remove_address(final int position, String address_id, final boolean isSelected) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("address_id", address_id);

            System.out.println(StaticDataUtility.APP_TAG + " remove_address param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " remove_address error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.REMOVE_ADDRESS, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " remove_address response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                addressModels.remove(position);
                                addressListAdapter.notifyDataSetChanged();

                                if (addressModels.size() > 0) {
                                    if (addressModels.size() >= 1) {
                                        if (isSelected)
                                            if (position == addressListAdapter.getItemCount()) {
                                                manage_address_default(0, addressModels.get(0).getAddress_id());
                                            } else if (position == addressListAdapter.getItemCount()) {
                                                manage_address_default(0, addressModels.get(0).getAddress_id());
                                            } else {
                                                manage_address_default(position, addressModels.get(position).getAddress_id());
                                            }
                                        else {
                                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    emptyAddress.setVisibility(View.VISIBLE);
                                    AddressList.setVisibility(View.GONE);
                                    emptyAddress.setText("Your address list is empty!!\n Please add address for delivery of products.");
                                }

                            } else {
                                showError(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Problem while set remove address. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("Problem while remove address. Try again later");
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

    private void getShippingPrice(String zipcode, final String address_id, final String is_cod) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("zipcode", zipcode);

            System.out.println(StaticDataUtility.APP_TAG + " getShippingPrice param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getShippingPrice error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.REMOVE_ADDRESS, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " getShippingPrice response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                Intent intent = new Intent(activity, SelectPaymentActivity.class);
                                intent.putExtra("total", String.valueOf(TotalPrice));
                                intent.putExtra("shipping_price", String.valueOf(jsonObject.optString("shipping_price")));
                                intent.putExtra("address_id", address_id);
                                intent.putExtra("is_cod", is_cod);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            } else {
                                showError(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Something went wrong. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("Something went wrong. Try again later");
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

    private void showError(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        progressHUD.dismiss();
    }
}
