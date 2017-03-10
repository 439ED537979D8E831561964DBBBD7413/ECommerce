package com.winsant.android.ui.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winsant.android.R;
import com.winsant.android.ui.AllAddressActivity;
import com.winsant.android.ui.LoginActivity;
import com.winsant.android.ui.MyApplication;
import com.winsant.android.ui.OrderActivity;
import com.winsant.android.ui.SettingsActivity;
import com.winsant.android.ui.UpdateProfileActivity;
import com.winsant.android.ui.VerifyActivity;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;


/**
 * Fragment class for each nav menu item
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private TextView txtUserName, txtMobile, txtEmail, txtChangePassword;
    private LinearLayout ll_before_login, ll_after_login;
    private ImageView imgUpdate;
    private String isSetPassword = "";
    private TextView txtVerify;
    private String Verify = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (MyApplication.getInstance().getPreferenceUtility().getLogin())
//            profile_verify();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        toolbar_title.setText(R.string.my_account);

        ll_before_login = (LinearLayout) view.findViewById(R.id.ll_before_login);
        ll_after_login = (LinearLayout) view.findViewById(R.id.ll_after_login);

        imgUpdate = (ImageView) view.findViewById(R.id.imgUpdate);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtMobile = (TextView) view.findViewById(R.id.txtMobile);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtVerify = (TextView) view.findViewById(R.id.txtVerify);
        TextView txtOrder = (TextView) view.findViewById(R.id.txtOrder);
        TextView txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        TextView txtFAQ = (TextView) view.findViewById(R.id.txtFAQ);
        TextView txtRateApp = (TextView) view.findViewById(R.id.txtRateApp);
        TextView txtAppFeedBack = (TextView) view.findViewById(R.id.txtAppFeedBack);
        TextView txtPolicies = (TextView) view.findViewById(R.id.txtPolicies);
        TextView txtLogout = (TextView) view.findViewById(R.id.txtLogout);
        txtChangePassword = (TextView) view.findViewById(R.id.txtChangePassword);

        txtUserName.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);
        txtMobile.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.BOLD);
        txtEmail.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.BOLD);
        txtOrder.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtAddress.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtFAQ.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtRateApp.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtAppFeedBack.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtVerify.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtPolicies.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtLogout.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);
        txtChangePassword.setTypeface(CommonDataUtility.setTypeFace1(activity), Typeface.NORMAL);

        txtUserName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        txtMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        txtEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        txtVerify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        txtOrder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtFAQ.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtRateApp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtAppFeedBack.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtPolicies.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtLogout.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtChangePassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        txtOrder.setOnClickListener(this);
        txtAddress.setOnClickListener(this);
        txtFAQ.setOnClickListener(this);
        txtRateApp.setOnClickListener(this);
        txtVerify.setOnClickListener(this);
        txtAppFeedBack.setOnClickListener(this);
        txtPolicies.setOnClickListener(this);
        txtChangePassword.setOnClickListener(this);
        txtLogout.setOnClickListener(this);
        imgUpdate.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(StaticDataUtility.APP_TAG + " Profile OnResume");
        MyApplication.getInstance().trackScreenView("Profile Fragment");
        checkLogin();
    }

    private void checkLogin() {

        if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {

            if (MyApplication.getInstance().getPreferenceUtility().getString("is_password_set").equals("0")) {
                txtChangePassword.setText("Set Password");
                isSetPassword = "y";
            } else {
                txtChangePassword.setText("Change Password");
                isSetPassword = "n";
            }

            imgUpdate.setVisibility(View.VISIBLE);
            ll_before_login.setVisibility(View.GONE);
            ll_after_login.setVisibility(View.VISIBLE);

            if (MyApplication.getInstance().getPreferenceUtility().getFirstName().equals("") && MyApplication.getInstance().getPreferenceUtility().getLastName().equals(""))
                txtUserName.setText(R.string.test_login_user);
            else
                txtUserName.setText(MyApplication.getInstance().getPreferenceUtility().getFirstName() + " " + MyApplication.getInstance().getPreferenceUtility().getLastName());
            txtMobile.setText(String.format("+91 %s", MyApplication.getInstance().getPreferenceUtility().getMobileNumber()));
            txtEmail.setText(MyApplication.getInstance().getPreferenceUtility().getEmail());

            checkProfile();

        } else {

            txtUserName.setText(R.string.test_user);
            txtMobile.setText("");
            txtEmail.setText("");

            imgUpdate.setVisibility(View.GONE);
            ll_before_login.setVisibility(View.VISIBLE);
            ll_after_login.setVisibility(View.GONE);

            ll_before_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, LoginActivity.class));
                    // activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }
    }

    private void checkProfile() {
        if (MyApplication.getInstance().getPreferenceUtility().getString("mobile_verify").equals("0")) {
            Verify = "mobile";
            txtVerify.setVisibility(View.VISIBLE);
            txtVerify.setText("Click here to verify your mobile number");
        } else {
            Verify = "both";
            txtVerify.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.txtOrder:
                intent = new Intent(activity, OrderActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
                break;

            case R.id.txtAddress:

                intent = new Intent(activity, AllAddressActivity.class);
                intent.putExtra("from", "profile");
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;

            case R.id.txtVerify:

                if (MyApplication.getInstance().getPreferenceUtility().getMobileNumber().equals("")) {

                    Toast.makeText(activity, "Please add your mobile number in your profile", Toast.LENGTH_SHORT).show();

                    intent = new Intent(activity, UpdateProfileActivity.class);
                    intent.putExtra("type", "profile");
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } else {

                    intent = new Intent(activity, VerifyActivity.class);
                    intent.putExtra("isVerify", Verify);
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                break;

            case R.id.txtFAQ:

                intent = new Intent(activity, SettingsActivity.class);
                intent.putExtra("type", "faq");
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;

            case R.id.txtChangePassword:

                intent = new Intent(activity, UpdateProfileActivity.class);
                intent.putExtra("type", "password");
                intent.putExtra("isSetPassword", isSetPassword);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;

            case R.id.txtRateApp:
                rateApp();
                break;

            case R.id.txtAppFeedBack:
                rateApp();
                break;

            case R.id.txtPolicies:

                intent = new Intent(activity, SettingsActivity.class);
                intent.putExtra("type", "policy");
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;

            case R.id.imgUpdate:


                intent = new Intent(activity, UpdateProfileActivity.class);
                intent.putExtra("type", "profile");
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;

            case R.id.txtLogout:

                CommonDataUtility.clearData();
                checkLogin();

                break;

        }
    }

//    private void profile_verify() {
//
//        JSONObject obj = new JSONObject();
//        try {
//
//            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
//            System.out.println(StaticDataUtility.APP_TAG + " profile_verify param --> " + obj.toString());
//
//        } catch (Exception e) {
//            System.out.println(StaticDataUtility.APP_TAG + " profile_verify param error --> " + e.toString());
//        }
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                StaticDataUtility.SERVER_URL + StaticDataUtility.PROFILE_VERIFY, obj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println(StaticDataUtility.APP_TAG + " profile_verify response --> " + response);
//
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(response.toString());
//                            final String success = jsonObject.optString("success");
//
//                            if (success.equals("1")) {
//
//                                MyApplication.getInstance().getPreferenceUtility().setString("mobile_verify", jsonObject.optString("is_otp_verified"));
////                                MyApplication.getInstance().getPreferenceUtility().setString("email_verify", jsonObject.optString("is_verified"));
////                                checkProfile();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
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

    private void rateApp() {

        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }
}
