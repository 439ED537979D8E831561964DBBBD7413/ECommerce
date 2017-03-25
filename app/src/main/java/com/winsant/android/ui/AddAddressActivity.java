package com.winsant.android.ui;

import android.app.Activity;
import android.os.Bundle;
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
import com.winsant.android.model.AddressModel;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private EditText edtFirstName, edtLastName, edtMobile, edtPincode, edtCity, edtState, edtCountry, edtAddress, edtLandmark;
    private String strAddressId, strFirstName, strLastName, strMobile, strPincode, strCity, strState, strCountry, strAddress, strLandmark, is_default;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title;
    private LinearLayout ll_address;
    private AddressModel addressModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        activity = AddAddressActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        String type = getIntent().getStringExtra("type");
        if (type.equals("add"))
            mToolbar_title.setText(getString(R.string.title_activity_add_new_address));
        else {
            mToolbar_title.setText(getString(R.string.title_activity_update_address));
            addressModel = (AddressModel) getIntent().getSerializableExtra("data");
        }

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

        ll_address = (LinearLayout) findViewById(R.id.ll_address);

        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtPincode = (EditText) findViewById(R.id.edtPincode);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtState = (EditText) findViewById(R.id.edtState);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtLandmark = (EditText) findViewById(R.id.edtLandmark);

        edtFirstName.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtLastName.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtMobile.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtPincode.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtCity.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtState.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtCountry.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtAddress.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtLandmark.setTypeface(CommonDataUtility.setTypeFace1(activity));

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnSave.setOnClickListener(this);

        if (CommonDataUtility.isTablet(activity)) {
            edtFirstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtLastName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtPincode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtCity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtState.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtCountry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            edtLandmark.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            btnSave.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        } else {
            edtFirstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtLastName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtPincode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtCity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtState.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtCountry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            edtLandmark.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            btnSave.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        if (type.equals("update")) {

            btnSave.setText(getString(R.string.update));

            strAddressId = addressModel.getAddress_id();
            strFirstName = addressModel.getFirst_name();
            strLastName = addressModel.getLast_name();
            strPincode = addressModel.getZipcode();
            strMobile = addressModel.getMobile();
            strCity = addressModel.getCity();
            strState = addressModel.getState();
            strCountry = addressModel.getCountry();
            strAddress = addressModel.getAddress();
            strLandmark = addressModel.getLandmark();
            if (addressModel.getIsSelected())
                is_default = "1";
            else
                is_default = "0";

            edtFirstName.setText(strFirstName);
            edtLastName.setText(strLastName);
            edtMobile.setText(strMobile);
            edtPincode.setText(strPincode);
            edtCity.setText(strCity);
            edtState.setText(strState);
            edtCountry.setText(strCountry);
            edtAddress.setText(strAddress);
            edtLandmark.setText(strLandmark);

        } else {

            btnSave.setText(getString(R.string.save));

            strAddressId = "0";
            is_default = "0";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        //8866708550
        switch (v.getId()) {

            case R.id.btnSave:

                if (CommonDataUtility.checkConnection(activity)) {
                    strFirstName = edtFirstName.getText().toString();
                    strLastName = edtLastName.getText().toString();
                    strMobile = edtMobile.getText().toString();
                    strPincode = edtPincode.getText().toString();
                    strCity = edtCity.getText().toString();
                    strState = edtState.getText().toString();
                    strCountry = edtCountry.getText().toString();
                    strAddress = edtAddress.getText().toString();
                    strLandmark = edtLandmark.getText().toString();

                    String message = Validation();

                    if (message.equals("true")) {
                        AddAddress();
                    } else {
                        CommonDataUtility.showSnackBar(ll_address, message);
                    }
                } else {
                    CommonDataUtility.showSnackBar(ll_address, getString(R.string.no_internet));
                }

                break;
        }
    }

    private String Validation() {

        if (strFirstName.equals(""))
            return "Please enter first name";
        else if (strLastName.equals(""))
            return "Please enter last name";
        else if (strMobile.equals(""))
            return "Please enter mobile number";
        else if (!android.util.Patterns.PHONE.matcher(strMobile).matches())
            return "Please enter valid mobile number";
        else if (strPincode.equals(""))
            return "Please enter pin code";
        else if (strCity.equals(""))
            return "Please enter city";
        else if (strState.equals(""))
            return "Please enter state";
        else if (strCountry.equals(""))
            return "Please enter country";
        else if (strAddress.equals(""))
            return "Please enter address";
        else
            return "true";
    }

    private void AddAddress() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("first_name", strFirstName);
            obj.put("last_name", strLastName);
            obj.put("mobile", strMobile);
            obj.put("phone", strMobile);
            obj.put("address", strAddress);
            obj.put("zipcode", strPincode);
            obj.put("city", strCity);
            obj.put("state", strState);
            obj.put("landmark", strLandmark);
            obj.put("country", strCountry);
            obj.put("address_id", strAddressId);
            obj.put("is_default", is_default);
            System.out.println(StaticDataUtility.APP_TAG + " AddAddress param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " AddAddress param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.ADD_ADDRESS, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " AddAddress response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                progressHUD.dismiss();
                                Toast.makeText(activity, "Successfully add address", Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_address, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_address, "Something problem while login,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_address, "Something problem while login,Try again later!!!");
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
