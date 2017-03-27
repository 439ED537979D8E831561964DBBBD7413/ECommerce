package com.winsant.android.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private EditText edtUserId, edtPassword, edtCPassword, edtOtp;
    private String strUserId, strPassword, strCPassword, user_id, strOTP;
    private Button btnOK;
    private LinearLayout ll_login;
    private KProgressHUD progressHUD;
    private CardView cardUserId, cardPassword, cardCPassword, cardOtp;
    private TextView mToolbar_title, txtResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgort_pass);

        activity = ForgotPasswordActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(getString(R.string.title_activity_forgot_pass));

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

        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        txtResend = (TextView) findViewById(R.id.txtResend);

        cardUserId = (CardView) findViewById(R.id.cardUserId);
        cardPassword = (CardView) findViewById(R.id.cardPassword);
        cardCPassword = (CardView) findViewById(R.id.cardCPassword);
        cardOtp = (CardView) findViewById(R.id.cardOtp);

        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);
        edtOtp = (EditText) findViewById(R.id.edtOtp);

        edtUserId.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtPassword.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtCPassword.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtOtp.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtResend.setTypeface(CommonDataUtility.setTypeFace1(activity));

        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnOK.setOnClickListener(this);
        txtResend.setOnClickListener(this);

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {

            edtUserId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            edtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            edtCPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            edtOtp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            btnOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {

            edtUserId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtCPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtOtp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            btnOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        } else {

            edtUserId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtCPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtOtp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            btnOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    @Override
    public void onClick(View v) {
        //8866708550
        switch (v.getId()) {

            case R.id.btnOK:

                if (CommonDataUtility.checkConnection(activity)) {
                    if (btnOK.getText().toString().equals(getString(R.string.ok))) {

                        strUserId = edtUserId.getText().toString();

                        String message = Validation();

                        if (message.equals("true")) {
                            ForgotPassword();
                        } else {
                            CommonDataUtility.showSnackBar(ll_login, message);
                        }

                    } else if (btnOK.getText().toString().equals(getString(R.string.verify))) {

                        if (edtOtp.getText().toString().equals("")) {
                            CommonDataUtility.showSnackBar(ll_login, "Enter correct OTP");
                        } else {
                            VerifyOtp(edtOtp.getText().toString());
                        }

                    } else {

                        strPassword = edtPassword.getText().toString();
                        strCPassword = edtCPassword.getText().toString();

                        String message = ResetPassValidation();

                        if (message.equals("true")) {
                            ResetPassword();
                        } else {
                            CommonDataUtility.showSnackBar(ll_login, message);
                        }
                    }
                } else {
                    CommonDataUtility.showSnackBar(ll_login, getString(R.string.no_internet));
                }

                break;

            case R.id.txtResend:
                strUserId = edtUserId.getText().toString();

                String message = Validation();

                if (message.equals("true")) {
                    ForgotPassword();
                } else {
                    CommonDataUtility.showSnackBar(ll_login, message);
                }
                break;
        }
    }

    private String Validation() {

        if (strUserId.equals(""))
            return "Please enter valid Email/Mobile Number";
        else
            return "true";
    }

    private String ResetPassValidation() {

        if (strPassword.equals(""))
            return "Please enter password";
        else if (!(strPassword.equals(strCPassword)))
            return "Password and Confirm password doesn't match!";
        else
            return "true";
    }

    private void ForgotPassword() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("username", strUserId);
            System.out.println(StaticDataUtility.APP_TAG + " ForgotPassword param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " ForgotPassword param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.FORGOTTEN_PASSWORD, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " ForgotPassword response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                if (jsonObject.has("otp")) {

                                    user_id = jsonObject.optString("user_id");
                                    strOTP = jsonObject.optString("otp");

                                    txtResend.setVisibility(View.VISIBLE);

                                    CommonDataUtility.showSnackBar(ll_login, message);

                                    btnOK.setText(getString(R.string.verify));

                                    cardUserId.setEnabled(false);
                                    edtUserId.setEnabled(false);
                                    edtUserId.setClickable(false);

                                    cardOtp.setVisibility(View.VISIBLE);
                                    mToolbar_title.setText(getString(R.string.title_activity_verify_otp));

                                } else {

                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_login, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_login, "Something problem while login,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_login, "Something problem while login,Try again later!!!");
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

    private void VerifyOtp(String otp) {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        if (strOTP.equals(otp)) {

            progressHUD.dismiss();

            btnOK.setText(getString(R.string.submit));

            cardUserId.setVisibility(View.GONE);
            cardOtp.setVisibility(View.GONE);
            txtResend.setVisibility(View.GONE);
            cardPassword.setVisibility(View.VISIBLE);
            cardCPassword.setVisibility(View.VISIBLE);

            mToolbar_title.setText(getString(R.string.title_activity_reset_pass));
            CommonDataUtility.showSnackBar(ll_login, "OTP verified successfully!");

        } else {

            progressHUD.dismiss();
            CommonDataUtility.showSnackBar(ll_login, "Enter correct OTP");
        }
    }

    private void ResetPassword() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", user_id);
            obj.put("password", strPassword);
            System.out.println(StaticDataUtility.APP_TAG + " ResetPassword param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " ResetPassword param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.SET_FORGOTTEN_PASSWORD, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " ResetPassword response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();

                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_login, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_login, "Something problem while reset password,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_login, "Something problem while reset password,Try again later!!!");
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
                VerifyOtp(intent.getStringExtra("otp"));
            }
        }
    };

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
                Snackbar.make(ll_login, "SMS read permission was NOT granted.",
                        Snackbar.LENGTH_SHORT).show();
            }

            if (!isReadSms) {
                Snackbar.make(ll_login, "Please give SMS read permission for OTP verification",
                        Snackbar.LENGTH_SHORT).show();
                checkPermission();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
