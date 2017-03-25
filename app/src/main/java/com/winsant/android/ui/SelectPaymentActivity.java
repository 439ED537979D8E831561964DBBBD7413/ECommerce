package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.winsant.android.R;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title, txtCouponCode;
    private TextView txtSubTotalPrice, txtDiscountPrice, txtTotalPrice;
    private EditText edtCouponCode;
    private RelativeLayout rl_payment;
    private RadioButton rbCOD, rbPAYU;
    private String TotalPrice, DiscountPrice = "0", is_cod, address_id, paymentId, coupon_id = "0";
    // private String SubTotalPrice, ShippingPrice;
    private Button btnApply;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        activity = SelectPaymentActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(getString(R.string.title_activity_select_payment));

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

        rl_payment = (RelativeLayout) findViewById(R.id.rl_payment);

        edtCouponCode = (EditText) findViewById(R.id.edtCouponCode);
        edtCouponCode.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.BOLD);

        txtCouponCode = (TextView) findViewById(R.id.txtCouponCode);
        txtCouponCode.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.BOLD);

        txtSubTotalPrice = (TextView) findViewById(R.id.txtSubTotalPrice);
        txtSubTotalPrice.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.NORMAL);

        txtDiscountPrice = (TextView) findViewById(R.id.txtDiscountPrice);
        txtDiscountPrice.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.NORMAL);

        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtTotalPrice.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

        is_cod = getIntent().getStringExtra("is_cod");
        address_id = getIntent().getStringExtra("address_id");

        TotalPrice = getIntent().getStringExtra("total");
        txtTotalPrice.setText("Total Price : " + activity.getResources().getString(R.string.Rs) + " " + TotalPrice);

        rbCOD = (RadioButton) findViewById(R.id.rbCOD);
        rbPAYU = (RadioButton) findViewById(R.id.rbPAYU);

        rbCOD.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.BOLD);
        rbPAYU.setTypeface(CommonDataUtility.setTypeFace(activity), Typeface.BOLD);

        btnApply = (Button) findViewById(R.id.btnApply);
        btnApply.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnApply.setOnClickListener(this);

        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnConfirm.setOnClickListener(this);

        if (CommonDataUtility.isTablet(activity)) {
            edtCouponCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtCouponCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtSubTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            rbCOD.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            rbPAYU.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            btnApply.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            btnConfirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        } else {
            edtCouponCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtCouponCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtSubTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtDiscountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            rbCOD.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            rbPAYU.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            btnApply.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            btnConfirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        }

        if (is_cod.equals("1"))
            rbCOD.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        //8866708550
        switch (v.getId()) {

            case R.id.btnConfirm:

                if (CommonDataUtility.checkConnection(activity)) {

                    if (is_cod.equals("1")) {
                        if (!rbCOD.isChecked() && !rbPAYU.isChecked())
                            CommonDataUtility.showSnackBar(rl_payment, "Please select payment method");
                        else if (rbCOD.isChecked()) {
                            paymentId = "";
                            add_customer_order();
                        } else if (rbPAYU.isChecked()) {
                            paymentId = "";
                            add_customer_order();
                        }
                    } else {
                        if (!rbPAYU.isChecked())
                            CommonDataUtility.showSnackBar(rl_payment, "Please select payment method");
                        else {
                            paymentId = "";
                            add_customer_order();
                        }
                        // makePayment();
                    }

                } else
                    CommonDataUtility.showSnackBar(rl_payment, getString(R.string.no_internet));

                break;

            case R.id.btnApply:

                if (btnApply.getText().toString().equals(getString(R.string.apply))) {

                    if (edtCouponCode.getText().toString().equals("")) {
                        CommonDataUtility.showSnackBar(rl_payment, "Please enter coupon code");
                    } else {
                        apply_coupon(edtCouponCode.getText().toString());
                    }

                } else {

                    edtCouponCode.setVisibility(View.VISIBLE);
                    txtCouponCode.setVisibility(View.GONE);
                    remove_coupon();
                }

                break;
        }
    }

    private void showError(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        progressHUD.dismiss();
    }

    public void makePayment(String txnId, String sURL, String fURL, String productinfo) {

        String userEmail = MyApplication.getInstance().getPreferenceUtility().getEmail();
        String userMobile = MyApplication.getInstance().getPreferenceUtility().getMobileNumber();

//        if (!MyApplication.getInstance().getPreferenceUtility().getEmail().equals("")) {
//            userEmail = MyApplication.getInstance().getPreferenceUtility().getEmail();
//        } else {
//            userEmail = "test@gmail.com";
//        }
//
//        if (!MyApplication.getInstance().getPreferenceUtility().getMobileNumber().equals("")) {
//            userMobile = MyApplication.getInstance().getPreferenceUtility().getMobileNumber();
//        } else {
//            userMobile = "9876543210";
//        }

        Intent intent = new Intent(activity, PayMentGateWay.class);
        intent.putExtra("name", MyApplication.getInstance().getPreferenceUtility().getFirstName());
        intent.putExtra("amount", DiscountPrice.equals("0") ? TotalPrice : String.valueOf(total));
        intent.putExtra("txnId", txnId);
        intent.putExtra("sURL", sURL);
        intent.putExtra("fURL", fURL);
        intent.putExtra("productInfo", productinfo);
        intent.putExtra("mobile", userMobile.equals("") ? "9876543210" : userMobile);
        intent.putExtra("email", userEmail.equals("") ? "test@gmail.com" : userEmail);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

            if (data != null) {
                String result = data.getStringExtra("result");
                switch (result) {
                    case "done":
                        String paymentId = data.getStringExtra("txnid");
                        System.out.println(StaticDataUtility.APP_TAG + " Success - Payment ID : " + paymentId);
                        RedirectToOrder();
                        break;
                    case "fail":
                        CommonDataUtility.showSnackBar(rl_payment, " Payment Canceled");
                        System.out.println(StaticDataUtility.APP_TAG + " failure");
                        break;
                    case "back":
                        CommonDataUtility.showSnackBar(rl_payment, " User returned without complete payment process.");
                        System.out.println(StaticDataUtility.APP_TAG + " User returned without login");
                        break;
                }
            } else {
                CommonDataUtility.showSnackBar(rl_payment, " User returned without complete payment process.");
            }
        }
    }

    private void RedirectToOrder() {

        Intent intent = new Intent(activity, OrderActivity.class);
        intent.putExtra("from", "order");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void add_customer_order() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("email", MyApplication.getInstance().getPreferenceUtility().getEmail());
            obj.put("mobile_number", MyApplication.getInstance().getPreferenceUtility().getMobileNumber());
            obj.put("address_id", address_id);
            obj.put("discount_amount", DiscountPrice);
            obj.put("coupon_id", coupon_id);
            obj.put("total_amount", TotalPrice);
            obj.put("payment_id", paymentId);

            if (is_cod.equals("1"))
                obj.put("payment_method", rbCOD.isChecked() ? "cod" : "payu");
            else
                obj.put("payment_method", "payu");

            System.out.println(StaticDataUtility.APP_TAG + " add_customer_order param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " add_customer_order error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.ADD_CUSTOMER_ORDER, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " add_customer_order response --> " + response);

                        try {

                            final JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (rbCOD.isChecked()) {
                                            Toast.makeText(activity, "Your order successfully placed.", Toast.LENGTH_SHORT).show();
                                            RedirectToOrder();
                                        } else {

                                            JSONObject data = jsonObject.optJSONObject("data");
                                            makePayment(data.optString("txnid"), data.optString("success_url").replace("\\/", "")
                                                    , data.optString("failure_url").replace("\\/", ""), data.optString("productinfo"));
                                        }
                                    }
                                });

                            } else {
                                showError("Problem while placing your order. Try again later");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Problem while placing your order. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showError("Problem while placing your order. Try again later");
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

    private void apply_coupon(String coupon_code) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("coupon_code", coupon_code);
            obj.put("total_amount", String.valueOf(TotalPrice));
            obj.put("is_cod", is_cod);

            System.out.println(StaticDataUtility.APP_TAG + " apply_coupon param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " apply_coupon error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.APPLY_COUPON, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " apply_coupon response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();
                                btnApply.setText(getString(R.string.remove));

                                DiscountPrice = jsonObject.optJSONObject("data").optString("coupon_discount_amount").replaceAll("\\.0*$", "");
                                coupon_id = jsonObject.optJSONObject("data").optString("coupon_id");

                                total = Integer.parseInt(TotalPrice) - Integer.parseInt(DiscountPrice);

                                txtSubTotalPrice.setVisibility(View.VISIBLE);
                                txtDiscountPrice.setVisibility(View.VISIBLE);
                                txtSubTotalPrice.setText("Sub Total : " + activity.getResources().getString(R.string.Rs) + " " + TotalPrice);
                                txtDiscountPrice.setText("Discount amount : " + activity.getResources().getString(R.string.Rs) + " " + DiscountPrice);
                                txtTotalPrice.setText("Total : " + activity.getResources().getString(R.string.Rs) + " " + total);

                                edtCouponCode.setVisibility(View.GONE);
                                txtCouponCode.setVisibility(View.VISIBLE);
                                txtCouponCode.setText(edtCouponCode.getText().toString());
                                CommonDataUtility.showSnackBar(rl_payment, message);

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(rl_payment, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(rl_payment, "Problem while apply coupon. Try again later");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    CommonDataUtility.showSnackBar(rl_payment, getString(R.string.no_connection));
                } else {
                    CommonDataUtility.showSnackBar(rl_payment, "Problem while apply coupon. Try again later");
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

    private void remove_coupon() {

        btnApply.setText(getString(R.string.apply));

        txtSubTotalPrice.setVisibility(View.GONE);
        txtDiscountPrice.setVisibility(View.GONE);
        txtTotalPrice.setText("Total : " + activity.getResources().getString(R.string.Rs) + " " + TotalPrice);

        DiscountPrice = "0";
        coupon_id = "0";
        edtCouponCode.setVisibility(View.VISIBLE);
        txtCouponCode.setVisibility(View.GONE);
        edtCouponCode.setText("");
    }
}

//        progressHUD = KProgressHUD.create(activity)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setCancellable(false).show();
//
//        JSONObject obj = new JSONObject();
//        try {
//
//            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
//            obj.put("coupon_code", coupon_code);
//            obj.put("total_amount", String.valueOf(TotalPrice));
//            obj.put("is_cod", is_cod);
//
//            System.out.println(StaticDataUtility.APP_TAG + " remove_coupon param --> " + obj.toString());
//
//        } catch (Exception e) {
//            System.out.println(StaticDataUtility.APP_TAG + " remove_coupon error --> " + e.toString());
//        }
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                StaticDataUtility.SERVER_URL + StaticDataUtility.REMOVE_COUPON, obj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println(StaticDataUtility.APP_TAG + " remove_coupon response --> " + response);
//
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(response.toString());
//                            final String message = jsonObject.optString("message");
//                            final String success = jsonObject.optString("success");
//
//                            if (success.equals("1")) {
//
//                                progressHUD.dismiss();
//
//                            } else {
//                                progressHUD.dismiss();
//                                CommonDataUtility.showSnackBar(rl_payment, message);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            progressHUD.dismiss();
//                            CommonDataUtility.showSnackBar(rl_payment, "Problem while apply coupon. Try again later");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressHUD.dismiss();
//
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    CommonDataUtility.showSnackBar(rl_payment, getString(R.string.no_connection));
//                } else {
//                    CommonDataUtility.showSnackBar(rl_payment, "Problem while apply coupon. Try again later");
//                }
//
//            }
//        }) {
//
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

//    public void makePayment() {
//
//        if (MyApplication.getInstance().getPreferenceUtility().getEmail().equals("")) {
//            userMobile = MyApplication.getInstance().getPreferenceUtility().getMobileNumber();
//        } else if (MyApplication.getInstance().getPreferenceUtility().getMobileNumber().equals("")) {
//            userEmail = MyApplication.getInstance().getPreferenceUtility().getMobileNumber();
//        }
//
//        txnID = getTxnId();
//
//        Double amount = Double.parseDouble("15");
//        String phone = userMobile.equals("") ? "9876543210" : userMobile;
//        String productName = "cart";
//        String firstName = MyApplication.getInstance().getPreferenceUtility().getFirstName();
//        String email = userEmail.equals("") ? "test@gmail.com" : userEmail;
//        String sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
//        String fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";
//        String udf1 = "";
//        String udf2 = "";
//        String udf3 = "";
//        String udf4 = "";
//        String udf5 = "";
//        String key = "JzKklD85";
//        // String key = "7wjvvX"; // Debug
//        String merchantId = "5644058";
//
//        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();
//
//        builder.setAmount(amount)
//                .setTnxId(txnID)
//                .setPhone(phone)
//                .setProductName(productName)
//                .setFirstName(firstName)
//                .setEmail(email)
//                .setsUrl(sUrl)
//                .setfUrl(fUrl)
//                .setUdf1(udf1)
//                .setUdf2(udf2)
//                .setUdf3(udf3)
//                .setUdf4(udf4)
//                .setUdf5(udf5)
//                .setIsDebug(false)
//                .setKey("JzKklD85")
//                .setMerchantId("5644058");// Live Merchant ID
//
//        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
//
////        String salt = "Rui8Hy58";
//        String salt = "rx8giwCnO5";
//        String serverCalculatedHash = hashCal(key + "|" + txnID + "|" + amount + "|" + productName + "|"
//                + firstName + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "|" + salt);
//
//        paymentParam.setMerchantHash(serverCalculatedHash);
//        PayUmoneySdkInitilizer.startPaymentActivityForResult(activity, paymentParam);
//
//        // Recommended
//        // calculateServerSideHashAndInitiatePayment(paymentParam);
//    }
//
//    public static String hashCal(String str) {
//        byte[] hashseq = str.getBytes();
//        StringBuilder hexString = new StringBuilder();
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
//            algorithm.reset();
//            algorithm.update(hashseq);
//            byte messageDigest[] = algorithm.digest();
//            for (byte aMessageDigest : messageDigest) {
//                String hex = Integer.toHexString(0xFF & aMessageDigest);
//                if (hex.length() == 1) {
//                    hexString.append("0");
//                }
//                hexString.append(hex);
//            }
//        } catch (NoSuchAlgorithmException ignored) {
//        }
//        return hexString.toString();
//    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//
//                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
//                System.out.println(StaticDataUtility.APP_TAG + " Success - Payment ID : " + paymentId);
//
//                this.paymentId = paymentId;
//                add_customer_order();
//            } else if (resultCode == RESULT_CANCELED) {
//                CommonDataUtility.showSnackBar(rl_payment, " Payment Canceled");
//                System.out.println(StaticDataUtility.APP_TAG + " failure");
//            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
//                CommonDataUtility.showSnackBar(rl_payment, " Payment Failed");
//                System.out.println(StaticDataUtility.APP_TAG + "failure");
//
//                if (data != null) {
//                    if (!data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {
//                        CommonDataUtility.showSnackBar(rl_payment, " Payment Canceled");
//                        System.out.println(StaticDataUtility.APP_TAG + " failure");
//                    }
//                }
//            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
//                CommonDataUtility.showSnackBar(rl_payment, " User returned without login");
//                System.out.println(StaticDataUtility.APP_TAG + " User returned without login");
//            }
//        }
//    }
