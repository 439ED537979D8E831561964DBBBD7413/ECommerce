package com.winsant.android.ui;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment class for each nav menu item
 */
public class VerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private EditText edtCode;
    private String strCode;
    private LinearLayout coordinatorLayout;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title;
    private String is_otp_verified;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        activity = VerifyActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(R.string.title_activity_verify_account);

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

        String isVerify = getIntent().getStringExtra("isVerify");

        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);
        LinearLayout llEmailVerify = (LinearLayout) findViewById(R.id.llEmailVerify);
        LinearLayout llMobileVerify = (LinearLayout) findViewById(R.id.llMobileVerify);

        llEmailVerify.setVisibility(View.GONE);

        TextView txtMobile = (TextView) findViewById(R.id.txtMobile);
        edtCode = (EditText) findViewById(R.id.edtCode);

        Button btnResend = (Button) findViewById(R.id.btnResend);
        Button btnVerify = (Button) findViewById(R.id.btnVerify);
//        Button btnResendEmail = (Button) findViewById(R.id.btnResendEmail);

        txtMobile.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        edtCode.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        btnResend.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        btnVerify.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
//        btnResendEmail.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);

        txtMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnVerify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//        btnResendEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        btnResend.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
//        btnResendEmail.setOnClickListener(this);

        if (!MyApplication.getInstance().getPreferenceUtility().getMobileNumber().equals(""))
            txtMobile.setText(MyApplication.getInstance().getPreferenceUtility().getMobileNumber());

        switch (isVerify) {

            case "mobile":
                llMobileVerify.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean checkPermission() {

        int smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (smsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return false;
        } else {
            listPermissionsNeeded.clear();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 1) {
            // START_INCLUDE(permission_result)

            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);

            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);

            // Check for RECEIVE_SMS
            boolean isReadSms = false;
            if (perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {

                isReadSms = true;

            } else {

                isReadSms = false;
                CommonDataUtility.showSnackBar(coordinatorLayout, "SMS read permission was NOT granted.");
            }

            if (!isReadSms) {
                CommonDataUtility.showSnackBar(coordinatorLayout, "Please give SMS read permission for OTP verification");
                checkPermission();
            }
            //else {
            //    send_otp();
            //}

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(optMessage);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(optMessage,
                new IntentFilter("OtpVerify"));
    }

    private BroadcastReceiver optMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra("isForOtp").equals("true")) {
                strCode = intent.getStringExtra("otp");
                edtCode.setText(strCode);
                MobileVerifyValidation();
            }
        }
    };

    @Override
    public void onClick(View v) {

        String message;

        switch (v.getId()) {
            case R.id.btnResend:

                if (CommonDataUtility.checkConnection(activity)) {
                    send_otp();
                } else {
                    CommonDataUtility.showSnackBar(coordinatorLayout, getString(R.string.no_internet));
                }

                break;

            case R.id.btnVerify:

                if (CommonDataUtility.checkConnection(activity)) {

                    strCode = edtCode.getText().toString();

                    message = MobileVerifyValidation();

                    if (message.equals("true")) {
                        verifiy_otp();
                    } else
                        CommonDataUtility.showSnackBar(coordinatorLayout, message);
                } else {
                    CommonDataUtility.showSnackBar(coordinatorLayout, getString(R.string.no_internet));
                }

                break;

//            case R.id.btnResendEmail:
//
//                if (CommonDataUtility.checkConnection(activity)) {
//                    send_veifiy_email();
//                } else {
//                    CommonDataUtility.showSnackBar(coordinatorLayout, getString(R.string.no_internet));
//                }
//                break;
        }
    }

    private String MobileVerifyValidation() {

        if (strCode.equals(""))
            return "Please enter OTP code";
        else if (!strCode.equals(MyApplication.getInstance().getPreferenceUtility().getString("otp"))) {
            is_otp_verified = "0";
            return "Please enter valid OTP";
        } else {
            is_otp_verified = "1";
            return "true";
        }
    }

    private void send_otp() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("mobile", MyApplication.getInstance().getPreferenceUtility().getMobileNumber());
            System.out.println(StaticDataUtility.APP_TAG + " send_otp param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " send_otp param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.SEND_OTP, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " send_otp response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                MyApplication.getInstance().getPreferenceUtility().setString("otp", jsonObject.optString("otp"));

                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(coordinatorLayout, message);

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(coordinatorLayout, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while resend OTP ,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while resend OTP ,Try again later!!!");
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

    private void verifiy_otp() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("is_otp_verified", is_otp_verified);

            System.out.println(StaticDataUtility.APP_TAG + " verifiy_otp param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " verifiy_otp param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.VERIFIY_OTP, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " verifiy_otp response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {
                                MyApplication.getInstance().getPreferenceUtility().setString("mobile_verify", "1");

                                progressHUD.dismiss();
                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(coordinatorLayout, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while verify otp ,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while verify otp ,Try again later!!!");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
//    private void send_veifiy_email() {
//
//        progressHUD = KProgressHUD.create(activity)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setCancellable(false).show();
//
//        JSONObject obj = new JSONObject();
//        try {
//
//            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
//            obj.put("email", MyApplication.getInstance().getPreferenceUtility().getEmail());
//
//            System.out.println(StaticDataUtility.APP_TAG + " send_veifiy_email param --> " + obj.toString());
//
//        } catch (Exception e) {
//            System.out.println(StaticDataUtility.APP_TAG + " send_veifiy_email param error --> " + e.toString());
//        }
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                StaticDataUtility.SERVER_URL + StaticDataUtility.SEND_VEIFIY_EMAIL, obj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println(StaticDataUtility.APP_TAG + " send_veifiy_email response --> " + response);
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
//                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                                finish();
//
//                            } else {
//                                progressHUD.dismiss();
//                                CommonDataUtility.showSnackBar(coordinatorLayout, message);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            progressHUD.dismiss();
//                            CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while sending verification link ,Try again later!!!");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressHUD.dismiss();
//                CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while sending verification link ,Try again later!!!");
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
