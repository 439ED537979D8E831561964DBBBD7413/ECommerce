package com.winsant.android.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import java.util.Timer;
import java.util.TimerTask;

public class OtpVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private EditText edtOtp;
    private Button btnOK;
    private LinearLayout ll_login;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title, txtResend;
    private String strUserId, strMobile;
    private String strCode;
    private String is_otp_verified;
    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        activity = OtpVerifyActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(getString(R.string.title_activity_verify_otp));

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

        strUserId = getIntent().getStringExtra("user_id");
        strMobile = getIntent().getStringExtra("mobile");

        ll_login = (LinearLayout) findViewById(R.id.ll_login);

        txtResend = (TextView) findViewById(R.id.txtResend);
        TextView txtOtpNumber = (TextView) findViewById(R.id.txtOtpNumber);
        edtOtp = (EditText) findViewById(R.id.edtOtp);

        edtOtp.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtResend.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtOtpNumber.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        txtOtpNumber.setText("OTP SENT TO " + strMobile);

        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnOK.setOnClickListener(this);
        txtResend.setOnClickListener(this);

        if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {

            edtOtp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txtResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtOtpNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            btnOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        } else if (activity.getResources().getBoolean(R.bool.isTablet)) {

            edtOtp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtOtpNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            btnOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        } else {

            edtOtp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtOtpNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            btnOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        if (checkPermission())
            send_otp();
    }

    @Override
    public void onClick(View v) {

        //8866708550
        switch (v.getId()) {

            case R.id.btnOK:

                Verify();

                break;

            case R.id.txtResend:

                if (CommonDataUtility.checkConnection(activity)) {
                    send_otp();
                } else {
                    CommonDataUtility.showSnackBar(ll_login, getString(R.string.no_internet));
                }

                break;
        }
    }

    public void initializeTimerTask() {

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtResend.setEnabled(false);
                txtResend.setText("Resend OTP after : " + millisUntilFinished / 1000 + " seconds");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                txtResend.setEnabled(true);
                txtResend.setText("Resend OTP");
            }

        }.start();
    }

    private void Verify() {

        String message;

        if (CommonDataUtility.checkConnection(activity)) {

            strCode = edtOtp.getText().toString();

            message = MobileVerifyValidation();

            if (message.equals("true")) {
                verifiy_otp();
            } else
                CommonDataUtility.showSnackBar(ll_login, message);

        } else {
            CommonDataUtility.showSnackBar(ll_login, getString(R.string.no_internet));
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

            obj.put("userid", strUserId);
            obj.put("mobile", strMobile);
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
                                CommonDataUtility.showSnackBar(ll_login, message);

                                initializeTimerTask();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_login, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_login, "Something problem while resend OTP ,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_login, "Something problem while resend OTP ,Try again later!!!");
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

                                MyApplication.getInstance().getPreferenceUtility().setLogin(true);
                                MyApplication.getInstance().getPreferenceUtility().setString("otp", "");
                                MyApplication.getInstance().getPreferenceUtility().setString("mobile_verify", "1");

                                progressHUD.dismiss();
                                Toast.makeText(activity, "OTP verified successfully!", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_login, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_login, "Something problem while verify otp ,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_login, "Something problem while verify otp ,Try again later!!!");
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
        MyApplication.getInstance().getPreferenceUtility().setString("otp", "");
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
                Verify();
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
            } else {
                send_otp();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
