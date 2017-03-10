package com.winsant.android.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.winsant.android.R;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;
import com.winsant.android.utils.VolleyNetWorkCall;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FestivalActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private ImageView festival_banner;
    private EditText edtFirstName, edtLastName, edtMobile, edtEmail, edtGeneral, edtVIP, edtVVIP;
    private String strFirstName, strLastName, strMobile, strEmail, strGeneral = "0", strVIP = "0", strVVIP = "0";
    private String strGeneralPrice = "0", strVIPPrice = "0", strVVIPPrice = "0";
    private String is_General, is_VIP, is_VVIP;
    private TextView txtTotalGeneral, txtTotalVIP, txtTotalVVIP, txtTotal;
    private KProgressHUD progressHUD;
    private TextView mToolbar_title;
    private LinearLayout ll_address;
    private RadioButton rbMale, rbFemale;
    private int TotalPrice = 0;
    private LinearLayout ll_general, ll_vip, ll_vvip;

    private VolleyNetWorkCall netWorkCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        activity = FestivalActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
        mToolbar_title.setText(getString(R.string.app_name));

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

        netWorkCall = new VolleyNetWorkCall();

        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_general = (LinearLayout) findViewById(R.id.ll_general);
        ll_vip = (LinearLayout) findViewById(R.id.ll_vip);
        ll_vvip = (LinearLayout) findViewById(R.id.ll_vvip);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtGeneral = (EditText) findViewById(R.id.edtGeneral);
        edtVIP = (EditText) findViewById(R.id.edtVIP);
        edtVVIP = (EditText) findViewById(R.id.edtVVIP);

        txtTotalGeneral = (TextView) findViewById(R.id.txtTotalGeneral);
        txtTotalVIP = (TextView) findViewById(R.id.txtTotalVIP);
        txtTotalVVIP = (TextView) findViewById(R.id.txtTotalVVIP);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        edtFirstName.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtLastName.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtMobile.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtEmail.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtGeneral.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtVIP.setTypeface(CommonDataUtility.setTypeFace1(activity));
        edtVVIP.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtTotalGeneral.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtTotalVIP.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtTotalVVIP.setTypeface(CommonDataUtility.setTypeFace1(activity));
        txtTotal.setTypeface(CommonDataUtility.setTypeFace1(activity));

        edtFirstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtLastName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtMobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtVIP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        edtVVIP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtTotalGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtTotalVIP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtTotalVVIP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        getPrice();

        festival_banner = (ImageView) findViewById(R.id.festival_banner);
        Glide.with(activity).load(R.drawable.holi_fest).asBitmap()
                .skipMemoryCache(true).placeholder(R.drawable.no_image_available)
                .into(new SimpleTarget<Bitmap>(1024, 300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        festival_banner.setImageDrawable(null);
                        festival_banner.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        festival_banner.setImageResource(R.drawable.no_image_available);
                    }
                });

        Button btnPay = (Button) findViewById(R.id.btnPay);
        btnPay.setTypeface(CommonDataUtility.setTypeFace1(activity));
        btnPay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnPay.setOnClickListener(this);

        edtGeneral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {

//                    if (Integer.parseInt(s.toString()) > 10) {
//                        CommonDataUtility.showSnackBar(ll_address, "At a time you can book max. 10 ticket.");
//                        strGeneral = "0";
//                        txtTotalGeneral.setText("0 * " + strGeneralPrice + " = " + getString(R.string.Rs) + strGeneral);
//                    } else {
                    strGeneral = String.valueOf(Integer.parseInt(s.toString()) * Integer.parseInt(strGeneralPrice));
                    txtTotalGeneral.setText("(" + (s.toString()) + " * " + strGeneralPrice + ")" + " = " + getString(R.string.Rs) + " " + strGeneral);
//                    }
                } else {
                    strGeneral = "0";
                    txtTotalGeneral.setText("0 * 200 = " + getString(R.string.Rs) + strGeneral);
                }

                setTotalCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtVIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {

//                    if (Integer.parseInt(s.toString()) > 10) {
//                        CommonDataUtility.showSnackBar(ll_address, "At a time you can book max. 10 ticket.");
//                        strVIP = "0";
//                        txtTotalVIP.setText("0 * " + strVIPPrice + " = " + getString(R.string.Rs) + strVIP);
//                    } else {
                    strVIP = String.valueOf(Integer.parseInt(s.toString()) * Integer.parseInt(strVIPPrice));
                    txtTotalVIP.setText("(" + (s.toString()) + " * " + strVIPPrice + ")" + " = " + getString(R.string.Rs) + " " + strVIP);
//                    }
                } else {
                    strVIP = "0";
                    txtTotalVIP.setText("0 * 400 = " + getString(R.string.Rs) + strVIP);
                }

                setTotalCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtVVIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    if (!s.toString().equals("1")) {
                        CommonDataUtility.showSnackBar(ll_address, "You can book only 1 VVIP ticket.");
                        strVVIP = "0";
                        txtTotalVVIP.setText("0 * " + strVVIPPrice + " = " + getString(R.string.Rs) + strVVIP);
                    } else {
                        strVVIP = strVVIPPrice;
                        txtTotalVVIP.setText("(" + (s.toString()) + " * " + strVVIPPrice + ")" + " = " + getString(R.string.Rs) + " " + strVVIP);
                    }
                } else {
                    strVVIP = "0";
                    txtTotalVVIP.setText("0 * 7500 = " + getString(R.string.Rs) + strVVIP);
                }

                setTotalCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setTotalCount() {

        if (strGeneral.equals(""))
            strGeneral = "0";
        if (strVIP.equals(""))
            strVIP = "0";
        if (strVVIP.equals(""))
            strVVIP = "0";

        System.out.println(StaticDataUtility.APP_TAG + " total --> " + strGeneral + " -- " + strVIP + " -- " + strVVIP);

        TotalPrice = Integer.parseInt(strGeneral) + Integer.parseInt(strVIP) + +Integer.parseInt(strVVIP);
        txtTotal.setText(String.valueOf(getString(R.string.Rs) + " " + TotalPrice));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        //8866708550
        switch (v.getId()) {

            case R.id.btnPay:

                if (CommonDataUtility.checkConnection(activity)) {
                    strFirstName = edtFirstName.getText().toString();
                    strLastName = edtLastName.getText().toString();
                    strMobile = edtMobile.getText().toString();
                    strEmail = edtEmail.getText().toString();
                    strGeneral = edtGeneral.getText().toString();
                    strVIP = edtVIP.getText().toString();
                    strVVIP = edtVVIP.getText().toString();

                    String message = Validation();

                    if (message.equals("true")) {
                        book_ticket();
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
        else if (!rbMale.isChecked() && !rbFemale.isChecked())
            return "Please select gender";
        else if (strMobile.equals(""))
            return "Please enter mobile number";
        else if (!android.util.Patterns.PHONE.matcher(strMobile).matches())
            return "Please enter valid mobile number";
        else if (strEmail.equals(""))
            return "Please enter email address";
        else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            return "Please enter valid email address";
        else if (!strVVIP.equals("") && !strVVIP.equals("1"))
            return "You can book only one VVIP ticket.";
        else if (is_General.equals("1") || is_VIP.equals("1") || is_VVIP.equals("1")) {
            if (strGeneral.equals("") && strVIP.equals("") && strVVIP.equals(""))
                return "Please enter ticket count";
            else
                return "true";
        } else
            return "true";
    }

    private void getPrice() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        netWorkCall.makeServiceCall(activity, StaticDataUtility.SERVER_URL + StaticDataUtility.GET_PRICE,
                new VolleyNetWorkCall.OnResponse() {
                    @Override
                    public void onSuccessCall(JSONObject response) {

                        try {

                            System.out.println(StaticDataUtility.APP_TAG + " getPrice response --> " + response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String success = jsonObject.optString("success");

                            progressHUD.dismiss();

                            if (success.equals("1")) {

                                strGeneralPrice = jsonObject.optJSONObject("data").optString("genral");
                                strVIPPrice = jsonObject.optJSONObject("data").optString("vip");
                                strVVIPPrice = jsonObject.optJSONObject("data").optString("vip_loung");

                                strGeneral = "0";
                                txtTotalGeneral.setText("0 * " + strGeneralPrice + " = " + getString(R.string.Rs) + strGeneral);
                                strVIP = "0";
                                txtTotalVIP.setText("0 * " + strVIPPrice + " = " + getString(R.string.Rs) + strVIP);
                                strVVIP = "0";
                                txtTotalVVIP.setText("0 * " + strVVIPPrice + " = " + getString(R.string.Rs) + strVVIP);

                                txtTotal.setText(getString(R.string.Rs) + " 0");

                                if (jsonObject.optString("is_genral").equals("0")) {
                                    is_General = "0";
                                    ll_general.setVisibility(View.GONE);
                                    strGeneral = "0";
                                } else {
                                    is_General = "1";
                                }

                                if (jsonObject.optString("is_vip").equals("0")) {
                                    is_VVIP = "0";
                                    ll_vip.setVisibility(View.GONE);
                                    strVIP = "0";
                                } else {
                                    is_VIP = "1";
                                }

                                if (jsonObject.optString("is_vip_loung").equals("0")) {
                                    is_VVIP = "0";
                                    ll_vvip.setVisibility(View.GONE);
                                    strVVIP = "0";
                                } else {
                                    is_VVIP = "1";
                                }
                            } else {

                                Toast.makeText(activity, "Something problem, Try again!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            Toast.makeText(activity, "Something problem, Try again!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onPostSuccessCall(String response) {

                    }

                    @Override
                    public void onFailCall(VolleyError error) {
                        progressHUD.dismiss();
                        Toast.makeText(activity, "Something problem, Try again!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void book_ticket() {

        if (strGeneral.equals(""))
            strGeneral = "0";
        if (strVIP.equals(""))
            strVIP = "0";
        if (strVVIP.equals(""))
            strVVIP = "0";

        JSONObject obj = new JSONObject();
        try {

            obj.put("userid", MyApplication.getInstance().getPreferenceUtility().getUserId());
            obj.put("full_name", strFirstName + " " + strLastName);
            obj.put("mobile_number", strMobile);
            obj.put("email", strEmail);
            obj.put("gender", rbMale.isChecked() ? "male" : "female");
            obj.put("genral", strGeneral);
            obj.put("vip", strVIP);
            obj.put("vip_loung", strVVIP);
            obj.put("TotalPrice", String.valueOf(TotalPrice));
            System.out.println(StaticDataUtility.APP_TAG + " book_ticket param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " book_ticket param error --> " + e.toString());
        }

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL + StaticDataUtility.BOOK_TICKET, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(StaticDataUtility.APP_TAG + " book_ticket response --> " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            progressHUD.dismiss();

                            if (success.equals("1")) {

                                JSONObject data = jsonObject.optJSONObject("data");
                                makePayment(data.optString("txnid"), data.optString("success_url").replace("\\/", "")
                                        , data.optString("failure_url").replace("\\/", ""));

                            } else {
                                CommonDataUtility.showSnackBar(ll_address, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_address, "Something problem while booking ticker,Try again later!!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                CommonDataUtility.showSnackBar(ll_address, "Something problem while booking ticker,Try again later!!!");
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
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

    public void makePayment(String txnId, String sURL, String fURL) {

        Intent intent = new Intent(activity, PayMentGateWay.class);
        intent.putExtra("name", strFirstName + " " + strLastName);
        intent.putExtra("amount", String.valueOf(TotalPrice));
        intent.putExtra("txnId", txnId);
        intent.putExtra("sURL", sURL);
        intent.putExtra("fURL", fURL);
        intent.putExtra("productInfo", "ticket_booking");
        intent.putExtra("mobile", strMobile);
        intent.putExtra("email", strEmail);
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
                        ShowBookTicketDialog("book");
                        break;
                    case "fail":
                        CommonDataUtility.showSnackBar(ll_address, " Payment Canceled");
                        System.out.println(StaticDataUtility.APP_TAG + " failure");
                        ShowBookTicketDialog("fail");
                        break;
                    case "back":
                        CommonDataUtility.showSnackBar(ll_address, " User returned without complete payment process.");
                        System.out.println(StaticDataUtility.APP_TAG + " User returned without login");
                        break;
                }
            } else {
                CommonDataUtility.showSnackBar(ll_address, " User returned without complete payment process.");
            }
        }
    }

    private void ShowBookTicketDialog(String type) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_common, null);
        dialog.setContentView(dialogView);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtTitle.setText("Ticket Booked");

        TextView txtDetails = (TextView) dialogView.findViewById(R.id.txtDetails);
        txtDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        if (type.equals("book"))
            txtDetails.setText("Your invoice sent to your email address and also ticket reference number sent to your mobile number.");
        else
            txtDetails.setText("Your booking process not completed.Please try again.!!!");

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        btnOK.setTypeface(CommonDataUtility.setTypeFace(activity));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }
}
