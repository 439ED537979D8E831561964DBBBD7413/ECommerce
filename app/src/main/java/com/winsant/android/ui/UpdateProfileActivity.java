package com.winsant.android.ui;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import java.util.HashMap;
import java.util.Map;


/**
 * Fragment class for each nav menu item
 */
public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private EditText edtFirstName, edtLastName, edtEmail, edtMobile, edtOldPassword, edtPassword, edtCPassword;
    private RadioButton rbMale, rbFemale;
    private String strFirstName, strLastName, strEmail = "", strMobile = "", strOldPassword, strPassword, strCPassword;
    private LinearLayout coordinatorLayout;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title;
    private String missing = "none", isSetPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        activity = UpdateProfileActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(R.string.my_account);

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

        String type = getIntent().getStringExtra("type");
        isSetPassword = getIntent().getStringExtra("isSetPassword");

        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);
        LinearLayout ll_password_update = (LinearLayout) findViewById(R.id.ll_password_update);
        LinearLayout ll_profile_update = (LinearLayout) findViewById(R.id.ll_profile_update);

        CardView cardEmail = (CardView) findViewById(R.id.cardEmail);
        CardView cardMobile = (CardView) findViewById(R.id.cardMobile);

        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMobile = (EditText) findViewById(R.id.edtMobile);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        edtOldPassword = (EditText) findViewById(R.id.edtOldPassword);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);

        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        Button btnUpdate1 = (Button) findViewById(R.id.btnUpdate1);

        edtFirstName.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        edtLastName.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        edtEmail.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        edtMobile.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        btnUpdate.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        btnUpdate1.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);

        edtFirstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        edtLastName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        edtEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        edtMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        btnUpdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        btnUpdate1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        btnUpdate.setOnClickListener(this);
        btnUpdate1.setOnClickListener(this);

        if (type.equals("profile")) {

            ll_profile_update.setVisibility(View.VISIBLE);
            ll_password_update.setVisibility(View.GONE);

            edtFirstName.setText(MyApplication.getInstance().getPreferenceUtility().getFirstName());
            edtLastName.setText(MyApplication.getInstance().getPreferenceUtility().getLastName());

            strFirstName = edtFirstName.getText().toString();
            strLastName = edtLastName.getText().toString();

            if (MyApplication.getInstance().getPreferenceUtility().getMobileNumber().equals("")) {

                missing = "mobile";
                cardMobile.setVisibility(View.VISIBLE);
                cardEmail.setVisibility(View.GONE);

                edtEmail.setText(MyApplication.getInstance().getPreferenceUtility().getEmail());
                strEmail = edtEmail.getText().toString();

            } else if (MyApplication.getInstance().getPreferenceUtility().getEmail().equals("")) {

                missing = "email";
                cardMobile.setVisibility(View.GONE);
                cardEmail.setVisibility(View.VISIBLE);

                edtMobile.setText(MyApplication.getInstance().getPreferenceUtility().getMobileNumber());
                strMobile = edtMobile.getText().toString();

            } else {

                missing = "none";
                cardMobile.setVisibility(View.GONE);
                cardEmail.setVisibility(View.GONE);

                edtEmail.setText(MyApplication.getInstance().getPreferenceUtility().getEmail());
                edtMobile.setText(MyApplication.getInstance().getPreferenceUtility().getMobileNumber());

                strEmail = edtEmail.getText().toString();
                strMobile = edtMobile.getText().toString();
            }
        } else {

            ll_profile_update.setVisibility(View.GONE);
            ll_password_update.setVisibility(View.VISIBLE);

            if (isSetPassword.equals("y"))
                edtOldPassword.setVisibility(View.GONE);
            else
                edtOldPassword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        String message;

        switch (v.getId()) {
            case R.id.btnUpdate:

                if (CommonDataUtility.checkConnection(activity)) {
                    strFirstName = edtFirstName.getText().toString();
                    strLastName = edtLastName.getText().toString();
                    strMobile = edtMobile.getText().toString();
                    strEmail = edtEmail.getText().toString();

                    message = Validation();

                    if (message.equals("true")) {
                        UpdateProfile();
                    } else
                        CommonDataUtility.showSnackBar(coordinatorLayout, message);
                } else {
                    CommonDataUtility.showSnackBar(coordinatorLayout, getString(R.string.no_internet));
                }

                break;

            case R.id.btnUpdate1:

                if (CommonDataUtility.checkConnection(activity)) {

                    if (isSetPassword.equals("y")) {

                        strPassword = edtPassword.getText().toString();
                        strCPassword = edtCPassword.getText().toString();

                        message = SetPassValidation();

                        if (message.equals("true")) {
                            SetPassword();
                        } else
                            CommonDataUtility.showSnackBar(coordinatorLayout, message);

                    } else {

                        strOldPassword = edtOldPassword.getText().toString();
                        strPassword = edtPassword.getText().toString();
                        strCPassword = edtCPassword.getText().toString();

                        message = ChangePassValidation();

                        if (message.equals("true")) {
                            ChangePassword();
                        } else
                            CommonDataUtility.showSnackBar(coordinatorLayout, message);
                    }

                } else {
                    CommonDataUtility.showSnackBar(coordinatorLayout, getString(R.string.no_internet));
                }

                break;
        }
    }

    private String Validation() {

        if (strFirstName.equals(""))
            return "Please enter first name";
        else if (strLastName.equals(""))
            return "Please enter last name";
        else if (!rbMale.isChecked() && !rbFemale.isChecked())
            return "Please select gender";
        else {
            if (missing.equals("mobile") && strMobile.equals(""))
                return "Please enter mobile number";
            else if (missing.equals("email") && strEmail.equals(""))
                return "Please enter email address";
            else
                return "true";
        }
    }

    private String ChangePassValidation() {

        if (strOldPassword.equals(""))
            return "Please enter old password";
        else if (strPassword.equals(""))
            return "Please enter new password";
        else if (!(strPassword.equals(strCPassword)))
            return "Password and Confirm password doesn't match!";
        else
            return "true";
    }

    private String SetPassValidation() {

        if (strPassword.equals(""))
            return "Please enter new password";
        else if (!(strPassword.equals(strCPassword)))
            return "Password and Confirm password doesn't match!";
        else
            return "true";
    }

    private void UpdateProfile() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("first_name", strFirstName);
            obj.put("last_name", strLastName);
            obj.put("mobile_number", strMobile);
            obj.put("email", strEmail);
            obj.put("gender", rbMale.isChecked() ? "male" : "female");

            if (missing.equals("none")) {
                obj.put("is_mobile", "2");
            } else {
                if (missing.equals("mobile"))
                    obj.put("is_mobile", "1");
                else {
                    obj.put("is_mobile", "0");
                }
            }

            System.out.println(StaticDataUtility.APP_TAG + " UpdateProfile param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " UpdateProfile param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.EDIT_ACCOUNT, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " UpdateProfile response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONObject data = jsonObject.optJSONObject("data");

                                MyApplication.getInstance().getPreferenceUtility().setFirstName(data.optString("first_name"));
                                MyApplication.getInstance().getPreferenceUtility().setLastName(data.optString("last_name"));
                                MyApplication.getInstance().getPreferenceUtility().setEmail(data.optString("email"));
                                MyApplication.getInstance().getPreferenceUtility().setMobileNumber(data.optString("mobile_number"));

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
                            CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while updating profile,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while updating profile,Try again later!!!");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String,String>();
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


    private void ChangePassword() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("old_password", strOldPassword);
            obj.put("new_password", strPassword);
            obj.put("is_password_set", MyApplication.getInstance().getPreferenceUtility().getString("is_password_set"));
            System.out.println(StaticDataUtility.APP_TAG + " ChangePassword param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " ChangePassword param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, StaticDataUtility.SERVER_URL + StaticDataUtility.CHANGE_PASSWORD,
                obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(StaticDataUtility.APP_TAG + " ChangePassword response --> " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    final String message = jsonObject.optString("message");
                    final String success = jsonObject.optString("success");

                    if (success.equals("1")) {

                        MyApplication.getInstance().getPreferenceUtility().setString("is_password_set", "1");
                        progressHUD.dismiss();
                        CommonDataUtility.showSnackBar(coordinatorLayout, message);

                        strOldPassword = "";
                        strPassword = "";
                        strCPassword = "";

                        edtOldPassword.setText(strOldPassword);
                        edtPassword.setText(strPassword);
                        edtCPassword.setText(strCPassword);

                    } else {
                        progressHUD.dismiss();
                        CommonDataUtility.showSnackBar(coordinatorLayout, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressHUD.dismiss();
                    CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while reset password,Try again later!!!");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while reset password,Try again later!!!");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String,String>();
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

    private void SetPassword() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("new_password", strPassword);
            obj.put("is_password_set", MyApplication.getInstance().getPreferenceUtility().getString("is_password_set"));
            System.out.println(StaticDataUtility.APP_TAG + " SetPassword param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " SetPassword param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, StaticDataUtility.SERVER_URL + StaticDataUtility.CHANGE_PASSWORD,
                obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(StaticDataUtility.APP_TAG + " SetPassword response --> " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    final String message = jsonObject.optString("message");
                    final String success = jsonObject.optString("success");

                    if (success.equals("1")) {

                        MyApplication.getInstance().getPreferenceUtility().setString("is_password_set", "1");
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
                    CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while change password,Try again later!!!");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(coordinatorLayout, "Something problem while change password,Try again later!!!");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String,String>();
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
