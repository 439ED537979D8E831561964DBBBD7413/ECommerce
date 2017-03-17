package com.winsant.android.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.winsant.android.R;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
//        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

    private Activity activity;
    private EditText edtUserId, edtPassword, edtCPassword;
    private String strUserId, strPassword, strCPassword;
    private Button btnSign_UP_IN, btnLogin;
    private boolean isForLogin = false;
    private LinearLayout ll_login, llFacebookLogin;
    //    llGoogleLogin;
    private KProgressHUD progressHUD;
    private CardView cardCPassword;
    private TextView mToolbar_title;
    private ProgressDialog pDialog;

    //    private GoogleApiClient mGoogleApiClient;
//    private ConnectionResult mConnectionResult;
    private LoginButton loginbutton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private String fbId = "", fbFirstName = "", fbLastName = "", fbEmail = "";
    private String social_logout;
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
//                System.out.println(StaticDataUtility.APP_TAG + " oldAccessToken --> " + oldAccessToken.getToken());
//                System.out.println(StaticDataUtility.APP_TAG + " currentAccessToken --> " + currentAccessToken.getToken());
            }
        };

        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(getString(R.string.title_activity_login));

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
        llFacebookLogin = (LinearLayout) findViewById(R.id.llFacebookLogin);
//        llGoogleLogin = (LinearLayout) findViewById(R.id.llGoogleLogin);
        loginbutton = (LoginButton) findViewById(R.id.login_button);

        cardCPassword = (CardView) findViewById(R.id.cardCPassword);

        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);

        edtUserId.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtPassword.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtCPassword.setTypeface(CommonDataUtility.setTypeFace1(activity));

        edtUserId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtCPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        TextView txtForgotPass = (TextView) findViewById(R.id.txtForgotPass);
        txtForgotPass.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtForgotPass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        btnSign_UP_IN = (Button) findViewById(R.id.btnSign_UP_IN);
        btnSign_UP_IN.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnSign_UP_IN.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnSign_UP_IN.setText("New to Winsant? SIGN UP");

        txtForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSign_UP_IN.setOnClickListener(this);

        // Initializing google plus api client
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(Plus.SCOPE_PLUS_PROFILE)
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();

//        llGoogleLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!CommonDataUtility.checkConnection(activity)) {
//                    Toast.makeText(activity, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//                } else {
//                    social_provider = "google";
//                    pDialog = new ProgressDialog(activity);
//                    pDialog.setMessage("Please wait...");
//                    pDialog.setIndeterminate(false);
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                    if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
//                        mGoogleApiClient.connect();
//                    else
//                        signInWithGplus();
//                }
//            }
//        });

        //FB
        List<String> permissionNeeds = Arrays.asList("email", "public_profile");

        loginbutton.setReadPermissions(permissionNeeds);
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                try {
                                    JSONObject jobject = response.getJSONObject();
                                    fbId = jobject.optString("id");
                                    fbFirstName = jobject.optString("first_name");
                                    fbLastName = jobject.optString("last_name");
                                    // imageURL = "https://graph.facebook.com/" + fbId + "/picture?type=large";
                                    fbEmail = jobject.optString("email");
                                    if (pDialog != null && pDialog.isShowing())
                                        pDialog.dismiss();

                                    FBLogin();

                                    LoginManager.getInstance().logOut();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    fbEmail = "";
                                    if (!fbId.equals("")) {
                                        if (pDialog != null && pDialog.isShowing())
                                            pDialog.dismiss();

                                        LoginManager.getInstance().logOut();
                                        Toast.makeText(activity, "something went wrong,try another option.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (pDialog != null && pDialog.isShowing())
                                            pDialog.dismiss();
                                        fbId = "";
                                        fbFirstName = "";
                                        fbLastName = "";
                                        Toast.makeText(activity, "something went wrong,try another option.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                if (accessToken == null) {
                    pDialog = new ProgressDialog(activity);
                    pDialog.setMessage("Please wait...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            }

            @Override
            public void onCancel() {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                Log.v("LoginActivity", exception.toString());
            }
        });

        llFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonDataUtility.checkConnection(activity)) {
                    Toast.makeText(activity, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                } else {
                    // social_provider = "facebook";
                    loginbutton.performClick();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtForgotPass:

                startActivity(new Intent(activity, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;

            case R.id.btnLogin:

                strUserId = edtUserId.getText().toString();
                strPassword = edtPassword.getText().toString();
                strCPassword = edtCPassword.getText().toString();

                if (CommonDataUtility.checkConnection(activity)) {
                    if (!isForLogin) {

                        String message = LoginValidation();

                        if (message.equals("true")) {
                            Login();
                        } else {
                            CommonDataUtility.showSnackBar(ll_login, message);
                        }
                    } else {

                        String message = SignUpValidation();

                        if (message.equals("true")) {
                            SignUp();
                        } else {
                            CommonDataUtility.showSnackBar(ll_login, message);
                        }
                    }
                } else {
                    CommonDataUtility.showSnackBar(ll_login, getString(R.string.no_internet));
                }

                break;

            case R.id.btnSign_UP_IN:

                if (isForLogin) {
                    isForLogin = false;
                    btnSign_UP_IN.setText("New to Winsant? SIGN UP");
                    btnLogin.setText("SIGN IN");
                    cardCPassword.setVisibility(View.GONE);
                    llFacebookLogin.setVisibility(View.VISIBLE);
                    mToolbar_title.setText(getString(R.string.title_activity_login));
                } else {
                    isForLogin = true;
                    btnSign_UP_IN.setText("Existing User? SIGN IN");
                    btnLogin.setText("SIGN UP");
                    cardCPassword.setVisibility(View.VISIBLE);
                    llFacebookLogin.setVisibility(View.GONE);
                    mToolbar_title.setText(getString(R.string.title_activity_sign_up));
                }

                break;
        }
    }

    private String LoginValidation() {

        if (strUserId.equals(""))
            return "Please enter valid Email/Mobile Number";
        else if (strUserId.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(strUserId).matches())
            return "Please enter valid Email";
        else if (!android.util.Patterns.PHONE.matcher(strUserId).matches())
            return "Please enter valid Mobile number";
        else if (strPassword.equals(""))
            return "Please enter password";
        else
            return "true";
    }

    private String SignUpValidation() {

        if (strUserId.equals(""))
            return "Please enter valid Email/Mobile number";
        else if (strUserId.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(strUserId).matches())
            return "Please enter valid Email";
        else if (!android.util.Patterns.PHONE.matcher(strUserId).matches())
            return "Please enter valid Mobile number";
        else if (strPassword.equals(""))
            return "Please enter password";
        else if (!(strPassword.equals(strCPassword)))
            return "Password and Confirm password doesn't match!";
        else
            return "true";
    }

    private void Login() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("username", strUserId);
            obj.put("password", strPassword);
            System.out.println(StaticDataUtility.APP_TAG + " Login param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " Login param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.LOGIN, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " Login response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONObject data = jsonObject.optJSONObject("data");

                                MyApplication.getInstance().getPreferenceUtility().setLogin(true);
                                MyApplication.getInstance().getPreferenceUtility().setUserId(data.optString("user_id"));
                                MyApplication.getInstance().getPreferenceUtility().setEmail(data.optString("email"));
                                MyApplication.getInstance().getPreferenceUtility().setFirstName(data.optString("first_name"));
                                MyApplication.getInstance().getPreferenceUtility().setLastName(data.optString("last_name"));
                                MyApplication.getInstance().getPreferenceUtility().setMobileNumber(data.optString("mobile_number"));
//                                MyApplication.getInstance().getPreferenceUtility().setString("email_verify", data.optString("is_verified"));
                                MyApplication.getInstance().getPreferenceUtility().setString("mobile_verify", data.optString("is_otp_verified"));
                                MyApplication.getInstance().getPreferenceUtility().setString("is_password_set", data.optString("is_password_set"));

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

    private void SignUp() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("username", strUserId);
            obj.put("password", strPassword);
            System.out.println(StaticDataUtility.APP_TAG + " SignUp param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " SignUp param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.REGISTER, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " SignUp response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONObject data = jsonObject.optJSONObject("data");

                                MyApplication.getInstance().getPreferenceUtility().setLogin(true);
                                MyApplication.getInstance().getPreferenceUtility().setUserId(data.optString("user_id"));
                                MyApplication.getInstance().getPreferenceUtility().setEmail(data.optString("email"));
                                MyApplication.getInstance().getPreferenceUtility().setFirstName(data.optString("first_name"));
                                MyApplication.getInstance().getPreferenceUtility().setLastName(data.optString("last_name"));
                                MyApplication.getInstance().getPreferenceUtility().setMobileNumber(data.optString("mobile_number"));
                                MyApplication.getInstance().getPreferenceUtility().setString("mobile_verify", data.optString("is_otp_verified"));
                                MyApplication.getInstance().getPreferenceUtility().setString("is_password_set", data.optString("is_password_set"));

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
                            CommonDataUtility.showSnackBar(ll_login, "Something problem while Sign Up,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_login, "Something problem while Sign Up,Try again later!!!");
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
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
//        if (requestCode == 0) {
//            if (responseCode != RESULT_OK) {
//                mIntentInProgress = false;
//                if (mGoogleApiClient != null && mGoogleApiClient.isConnecting()) {
//                    mGoogleApiClient.disconnect();
//                }
//                if (pDialog != null && pDialog.isShowing())
//                    pDialog.dismiss();
//            } else {
//                mIntentInProgress = false;
//                if (mGoogleApiClient != null)
//                    if (!mGoogleApiClient.isConnecting()) {
//                        mGoogleApiClient.connect();
//                    }
//            }
//        }
    }

    private void EmailMobileDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_add_email_mobile, null);
        dialog.setContentView(dialogView);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtTitle.setText("Enter E-mail or Mobile Number");

        final EditText edtEmailMobile = (EditText) dialogView.findViewById(R.id.edtEmailMobile);
        edtEmailMobile.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtEmailMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtEmailMobile.setHint("Enter E-mail or Mobile number here...");

        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(CommonDataUtility.setTypeFace(activity));
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(CommonDataUtility.setTypeFace(activity));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtEmailMobile.getText().toString().equals("")) {
                    edtEmailMobile.setError("Please enter E-mail or Mobile number");
                } else {
                    dialog.dismiss();
                    fbEmail = edtEmailMobile.getText().toString();
                    FBLogin();
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

    private void FBLogin() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("username", fbEmail);
            obj.put("first_name", fbFirstName);
            obj.put("last_name", fbLastName);
            obj.put("fbid", fbId);
            System.out.println(StaticDataUtility.APP_TAG + " FBLogin param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " FBLogin param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.FB_LOGIN, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " FBLogin response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");
                            final String is_username = jsonObject.optString("is_username");

                            if (success.equals("1")) {

                                JSONObject data = jsonObject.optJSONObject("data");

                                MyApplication.getInstance().getPreferenceUtility().setLogin(true);
                                MyApplication.getInstance().getPreferenceUtility().setUserId(data.optString("user_id"));
                                MyApplication.getInstance().getPreferenceUtility().setEmail(data.optString("email"));
                                MyApplication.getInstance().getPreferenceUtility().setFirstName(data.optString("first_name"));
                                MyApplication.getInstance().getPreferenceUtility().setLastName(data.optString("last_name"));
                                MyApplication.getInstance().getPreferenceUtility().setMobileNumber(data.optString("mobile_number"));
                                MyApplication.getInstance().getPreferenceUtility().setString("mobile_verify", data.optString("is_otp_verified"));
                                MyApplication.getInstance().getPreferenceUtility().setString("is_password_set", data.optString("is_password_set"));

                                progressHUD.dismiss();
                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressHUD.dismiss();

                                if (is_username.equals("0")) {
                                    EmailMobileDialog();
                                } else {
                                    CommonDataUtility.showSnackBar(ll_login, message);
                                }
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

}

/**
 * Sign-in into google
 */
//    private void signInWithGplus() {
//        if (!mGoogleApiClient.isConnecting()) {
//            resolveSignInError();
//        }
//    }
//
//    public void signOutFromGplus() {
//        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
//            mGoogleApiClient.connect();
//
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            social_logout = "0";
//        }
//    }
//
//    public void signOutFromGplusFailed() {
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            social_logout = "0";
//        }
//    }
//
//    /**
//     * Method to resolve any signin errors
//     */
//    private void resolveSignInError() {
//        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                mConnectionResult.startResolutionForResult(this, 0);
//            } catch (IntentSender.SendIntentException e) {
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//
//    /**
//     * Fetching user's information name, email, profile pic
//     */
//    private void getProfileInformation() {
//        try {
//            fbEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
//            social_provider = "google";
//            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//                String personName = "";
//                String personlastname = "";
//                if (currentPerson.getName().hasFamilyName())
//                    personlastname = currentPerson.getName().getFamilyName();
//                if (currentPerson.getName().hasGivenName())
//                    personName = currentPerson.getName().getGivenName();
//                else
//                    personName = currentPerson.getDisplayName();
//                String personPhotoUrl = currentPerson.getImage().getUrl();
//                fbId = currentPerson.getId();
//                fbFirstName = personName;
//                fbLastName = personlastname;
//                imageURL = personPhotoUrl;
////                if (currentPerson.getGender() == 1)
////                    fbGender = "female";
////                else if (currentPerson.getGender() == 0)
////                    fbGender = "male";
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                fbEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
//                imageURL = imageURL.substring(0, imageURL.length() - 2) + 400;
//                if (CommonDataUtility.checkConnection(activity)) {
//                    if (pDialog != null && pDialog.isShowing())
//                        pDialog.dismiss();
////                    checkSocialLogin();
//                } else {
//                    if (pDialog != null && pDialog.isShowing())
//                        pDialog.dismiss();
//                    Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                if (pDialog != null && pDialog.isShowing())
//                    pDialog.dismiss();
//                Toast.makeText(activity, "Data not found", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            if (pDialog != null && pDialog.isShowing())
//                pDialog.dismiss();
//            e.printStackTrace();
//        }
//    }
//}


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (mGoogleApiClient.isConnected())
//            getProfileInformation();
//
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//    }

//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!result.hasResolution()) {
//            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
//            return;
//        }
//        if (!mIntentInProgress) {
//            mConnectionResult = result;
//            resolveSignInError();
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }