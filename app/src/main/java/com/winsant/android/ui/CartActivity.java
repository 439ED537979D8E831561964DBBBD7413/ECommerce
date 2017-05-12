package com.winsant.android.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
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
import com.winsant.android.adapter.CartAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.model.CartModel;
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

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private TextView mToolbar_title;

    private ProgressWheel progress_wheel;
    private RelativeLayout rl_cart;
    // private RelativeLayout rl_no_data;
    // private TextView emptyData;
    private TextView txtPinCode, txtTotalPrice;
    private ArrayList<CartModel> cartProductArrayList;
    private CartAdapter cartAdapter;
    private TableRow tableRow;
    private ImageView imgError;

    private int TotalPrice;
    private RecyclerView CartProductList;
    private KProgressHUD progressHUD;
    private String strPinCode;
    private String Verify, message;
    private String TYPE = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);

        activity = CartActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText("My Cart");

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

    @Override
    protected void onStart() {
        super.onStart();
        checkProfile();
    }

    private void setUI() {

        findViewById(R.id.ll_top).setVisibility(View.GONE);

        tableRow = (TableRow) findViewById(R.id.tableRow);
        imgError = (ImageView) findViewById(R.id.imgError);

        progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
        txtPinCode = (TextView) findViewById(R.id.txtPinCode);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);

        txtTotalPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

        Button btnChange = (Button) findViewById(R.id.btnChange);
        Button btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setTypeface(CommonDataUtility.setTypeFace1(activity));

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
            txtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            btnContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
            txtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            btnContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        } else {
            txtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            btnContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }

        CartProductList = (RecyclerView) findViewById(R.id.CartProductList);
        CartProductList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        btnChange.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        txtPinCode.setText("Pincode 000000");
        imgError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emptyData:

                if (!TYPE.equals(getResources().getString(R.string.no_cart_data)))
                    if (CommonDataUtility.checkConnection(activity)) {
                        getData();
                    } else if (TYPE.equals(getResources().getString(R.string.no_data))
                            || TYPE.equals(getResources().getString(R.string.no_connection))) {
                        getData();
                    } else if (TYPE.equals(getResources().getString(R.string.no_internet))) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }

                break;

            case R.id.btnChange:
                changePinDialog();
                break;

            case R.id.btnContinue:

                if (CommonDataUtility.checkConnection(activity)) {
                    Intent intent;
                    if (MyApplication.getInstance().getPreferenceUtility().getEmail().equals("")) {

                        Toast.makeText(activity, "Please add your email address in your profile", Toast.LENGTH_SHORT).show();

                        intent = new Intent(activity, UpdateProfileActivity.class);
                        intent.putExtra("type", "profile");
                        intent.putExtra("isVerify", "email");
                        startActivity(intent);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    } else if (MyApplication.getInstance().getPreferenceUtility().getMobileNumber().equals("")) {

                        Toast.makeText(activity, "Please add your mobile number in your profile", Toast.LENGTH_SHORT).show();

                        intent = new Intent(activity, UpdateProfileActivity.class);
                        intent.putExtra("type", "profile");
                        intent.putExtra("isVerify", "mobile");
                        startActivity(intent);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    } else if (Verify.equals("mobile")) {

                        intent = new Intent(activity, OtpVerifyActivity.class);
                        intent.putExtra("isVerify", Verify);
                        intent.putExtra("mobile", MyApplication.getInstance().getPreferenceUtility().getMobileNumber());
                        intent.putExtra("user_id", MyApplication.getInstance().getPreferenceUtility().getUserId());
                        startActivity(intent);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    } else if (cartProductArrayList.size() > 0) {
                        createJson();
                    }

                } else {
                    CommonDataUtility.showSnackBar(rl_cart, getString(R.string.no_internet));
                }

                break;

            default:
                break;
        }
    }

    private void checkProfile() {
        if (MyApplication.getInstance().getPreferenceUtility().getString("mobile_verify").equals("0")) {
            Verify = "mobile";
            message = "Please verify your mobile number";
        } else {
            Verify = "both";
        }
    }

    private void getData() {

        CartProductList.setVisibility(View.GONE);

        if (!MyApplication.getInstance().getPreferenceUtility().getString("pincode").equals("")) {
            strPinCode = MyApplication.getInstance().getPreferenceUtility().getString("pincode");
            txtPinCode.setText((Html.fromHtml("Deliver to " + "<font color='#1B347E'> <b>" + strPinCode + "</b></font>")));
        }

        if (CommonDataUtility.checkConnection(activity)) {
            getCartProductData();

        } else {

            imgError.setVisibility(View.VISIBLE);
            TYPE = getString(R.string.no_internet);
            Glide.with(activity).load(R.drawable.no_wifi).into(imgError);
        }
    }

    private void getCartProductData() {

        progress_wheel.setVisibility(View.VISIBLE);
        cartProductArrayList = new ArrayList<>();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            System.out.println(StaticDataUtility.APP_TAG + " getCartProductData param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getCartProductData error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, StaticDataUtility.SERVER_URL + StaticDataUtility.CART, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " getCartProductData response --> " + response);

                        try {

                            System.out.println(StaticDataUtility.APP_TAG + " getCartProductData response --> " + response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONArray data = jsonObject.optJSONArray("data");

                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {

                                        JSONObject dataObject = data.getJSONObject(i);

                                        cartProductArrayList.add(new CartModel(dataObject.optString("product_id"), dataObject.optString("product_name"),
                                                dataObject.optString("product_link"), dataObject.optString("price"), dataObject.optString("discount_price"),
                                                dataObject.optString("discount_per"), dataObject.optString("product_image"), dataObject.optString("remove_link"),
                                                dataObject.optString("qty"), dataObject.optString("availability"), dataObject.optString("color_name"),
                                                dataObject.optString("size_name"), dataObject.optString("product_color"), dataObject.optString("product_size")));

                                        if (dataObject.optString("discount_per").equals("100")) {
                                            TotalPrice += (int) Double.parseDouble(dataObject.optString("price"));
                                        } else {
                                            TotalPrice += (int) Double.parseDouble(dataObject.optString("discount_price"));
                                        }
                                    }
                                }

                                // TODO : Set Data
                                setData();

                                MyApplication.getInstance().getPreferenceUtility().setInt("total_cart", cartProductArrayList.size());

                                mToolbar_title.setText("My Cart (" + cartProductArrayList.size() + ")");

                                Toast.makeText(activity, "Total " + cartProductArrayList.size() + " Products", Toast.LENGTH_SHORT).show();
                                txtTotalPrice.setText(activity.getResources().getString(R.string.Rs) + " " + TotalPrice);

                                progress_wheel.setVisibility(View.GONE);
                                CartProductList.setVisibility(View.VISIBLE);

                            } else {

                                progress_wheel.setVisibility(View.GONE);

                                if (cartProductArrayList.size() == 0) {
                                    MyApplication.getInstance().getPreferenceUtility().setInt("total_cart", cartProductArrayList.size());
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

        if (cartProductArrayList.size() > 0) {
            cartAdapter = new CartAdapter(activity, cartProductArrayList, new CartAdapter.onClickListener() {
                @Override
                public void onRemoveClick(int position, String remove_url, String product_id) {
                    RemoveFromCartDialog(position, remove_url, product_id);
                }

                @Override
                public void onQtyClick(int position, String quantity) {
                    changeQtyDialog(position, quantity);
                }
            });

            CartProductList.setAdapter(cartAdapter);
            tableRow.setVisibility(View.VISIBLE);
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

    private void RemoveFromCartDialog(final int position, final String remove_link, final String product_id) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_change_pincode, null);
        dialog.setContentView(dialogView);

        dialogView.findViewById(R.id.txtAvailability).setVisibility(View.GONE);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        txtTitle.setText("Remove from cart");

        TextView txtTerms = (TextView) dialogView.findViewById(R.id.txtTerms);
        txtTerms.setVisibility(View.VISIBLE);
        txtTerms.setText("Want to remove product from cart??");

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            txtTerms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtTerms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtTerms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }

        dialogView.findViewById(R.id.edtPinCode).setVisibility(View.GONE);

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace(activity));
        btnOK.setText(getString(R.string.yes));

        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(CommonDataUtility.setTypeFace(activity));
        btnCancel.setText(getString(R.string.no));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RemoveFromCart(position, remove_link, product_id);
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

    private void RemoveFromCart(final int position, String remove_link, String product_id) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("product_id", product_id);
            System.out.println(StaticDataUtility.APP_TAG + " RemoveFromCart param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " RemoveFromCart error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                remove_link, obj,
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

                                        cartProductArrayList.remove(position);
                                        cartAdapter.notifyDataSetChanged();
                                        CommonDataUtility.showSnackBar(rl_cart, message);

                                        mToolbar_title.setText("My Cart (" + cartProductArrayList.size() + ")");
                                        MyApplication.getInstance().getPreferenceUtility().setInt("total_cart", cartProductArrayList.size());

                                        TotalAfterRemove();

                                        if (cartProductArrayList.size() == 0) {
                                            mToolbar_title.setText("My Cart");
                                            EmptyCart();
                                        }
                                    }
                                });

                            } else {
                                showError(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Problem while remove from the cart. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("Problem while remove from the cart. Try again later");
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

    private void EmptyCart() {

        tableRow.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        Glide.with(activity).load(R.drawable.no_empty_cart).into(imgError);
    }

    private void showError(String message) {
        CommonDataUtility.showSnackBar(rl_cart, message);
        progressHUD.dismiss();
    }

    private void changePinDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_change_pincode, null);
        dialog.setContentView(dialogView);

        TextView txtAvailability = (TextView) dialogView.findViewById(R.id.txtAvailability);
        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtTitle.setText("Enter Delivery Pin Code");

        txtAvailability.setVisibility(View.GONE);

        final EditText edtPinCode = (EditText) dialogView.findViewById(R.id.edtPinCode);
        edtPinCode.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtPinCode.setHint("Enter pin code here...");

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            edtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            edtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtAvailability.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            edtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        if (!MyApplication.getInstance().getPreferenceUtility().getString("pincode").equals(""))
            edtPinCode.setText(MyApplication.getInstance().getPreferenceUtility().getString("pincode"));

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace(activity));
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(CommonDataUtility.setTypeFace(activity));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtPinCode.getText().toString().equals("")) {
                    edtPinCode.setError("Enter pin code");
                } else {
                    MyApplication.getInstance().getPreferenceUtility().setString("pincode", edtPinCode.getText().toString());
                    strPinCode = MyApplication.getInstance().getPreferenceUtility().getString("pincode");
                    txtPinCode.setText("Deliver to " + strPinCode);
                }

                dialog.dismiss();
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

    private void changeQtyDialog(final int position, final String quantity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_change_pincode, null);
        dialog.setContentView(dialogView);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtAvailability = (TextView) dialogView.findViewById(R.id.txtAvailability);
        txtTitle.setText("Enter Quantity");

        txtAvailability.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
        txtAvailability.setText("Availability Quantity : " + quantity);

        final EditText edtPinCode = (EditText) dialogView.findViewById(R.id.edtPinCode);
        edtPinCode.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtPinCode.setHint("Quantity");

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            txtAvailability.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtAvailability.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtAvailability.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            edtPinCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace(activity));
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(CommonDataUtility.setTypeFace(activity));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtPinCode.getText().toString().equals("")) {
                    edtPinCode.setError("Enter quantity");
                } else if (Integer.parseInt(edtPinCode.getText().toString()) > Integer.parseInt(quantity)) {

                    Toast.makeText(activity, "Please enter less quantity compare to availability quantity ", Toast.LENGTH_SHORT).show();

                } else {

                    dialog.dismiss();

                    TotalPrice = 0;
                    int qty = (int) Double.parseDouble(edtPinCode.getText().toString());

                    setTotal(qty, position);

                    CartModel cartModel = cartProductArrayList.get(position);

                    cartProductArrayList.set(position, new CartModel(cartModel.getProduct_id(), cartModel.getName(), cartModel.getProduct_url(),
                            cartModel.getPrice(), cartModel.getDiscount_price(), cartModel.getDiscount_per(), cartModel.getProduct_image(), cartModel.getRemove_url(),
                            String.valueOf(qty), cartModel.getAvailability(), cartModel.getColor_name(), cartModel.getSize_name(), cartModel.getProduct_color(),
                            cartModel.getProduct_size()));

                    cartAdapter.notifyItemChanged(position);


                }
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

    private void TotalAfterRemove() {

        TotalPrice = 0;
        int Price, DisPrice;

        for (int i = 0; i < cartProductArrayList.size(); i++) {
            CartModel cartModel = cartProductArrayList.get(i);

            if (Double.parseDouble(cartModel.getDiscount_per()) == 100) {
                Price = (int) Double.parseDouble(cartModel.getPrice()) * Integer.parseInt(cartModel.getQty());
                TotalPrice += Price;
            } else {
                DisPrice = (int) Double.parseDouble(cartModel.getDiscount_price()) * Integer.parseInt(cartModel.getQty());
                TotalPrice += DisPrice;
            }
        }

        txtTotalPrice.setText(activity.getResources().getString(R.string.Rs) + " " + TotalPrice);
    }

    private void setTotal(int qty, int position) {

        int Price, DisPrice;

        for (int i = 0; i < cartProductArrayList.size(); i++) {
            CartModel cartModel = cartProductArrayList.get(i);

            if (i == position) {
                if (Double.parseDouble(cartModel.getDiscount_per()) == 100) {
                    Price = (int) Double.parseDouble(cartModel.getPrice()) * qty;
                    TotalPrice += Price;
                } else {
                    DisPrice = (int) Double.parseDouble(cartModel.getDiscount_price()) * qty;
                    TotalPrice += DisPrice;
                }
            } else {
                if (Double.parseDouble(cartModel.getDiscount_per()) == 100) {
                    Price = (int) Double.parseDouble(cartModel.getPrice()) * Integer.parseInt(cartModel.getQty());
                    TotalPrice += Price;
                } else {
                    DisPrice = (int) Double.parseDouble(cartModel.getDiscount_price()) * Integer.parseInt(cartModel.getQty());
                    TotalPrice += DisPrice;
                }
            }
        }

        txtTotalPrice.setText(activity.getResources().getString(R.string.Rs) + " " + TotalPrice);
    }

    JSONArray jsonArray = new JSONArray();

    private void createJson() {

        try {

            JSONObject jsonObject;

            for (int i = 0; i < cartProductArrayList.size(); i++) {

                CartModel cartModel = cartProductArrayList.get(i);

                jsonObject = new JSONObject();

                jsonObject.put("product_id", cartModel.getProduct_id());
                jsonObject.put("qty", cartModel.getQty());
                jsonObject.put("product_color", cartModel.getColor_name());
                jsonObject.put("color_id", cartModel.getProduct_color());
                jsonObject.put("product_size", cartModel.getSize_name());
                jsonObject.put("size_id", cartModel.getProduct_size());

                jsonArray.put(jsonObject);
            }

            all_add_to_cart();

        } catch (Exception e) {

            progressHUD.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(StaticDataUtility.APP_TAG + " createJson error --> " + e.getMessage());
        }
    }

    private void all_add_to_cart() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("data", jsonArray);

            System.out.println(StaticDataUtility.APP_TAG + " ALL_ADD_TO_CART param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " ALL_ADD_TO_CART error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.ALL_ADD_TO_CART, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " ALL_ADD_TO_CART response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(activity, AllAddressActivity.class);
                                        intent.putExtra("from", "cart");
                                        intent.putExtra("total", String.valueOf(TotalPrice));
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                });

                            } else {
                                showError(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Problem while check out from the cart. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("Problem while check out from the cart. Try again later");
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
}