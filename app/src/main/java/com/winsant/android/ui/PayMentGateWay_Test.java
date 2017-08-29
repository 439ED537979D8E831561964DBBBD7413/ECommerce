package com.winsant.android.ui;

/**
 * Created by Developer on 2/25/2017.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.winsant.android.kprogresshud.KProgressHUD;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class PayMentGateWay_Test extends Activity
{

    private ArrayList<String> post_val = new ArrayList<String>();
    private String post_Data = "";
    WebView webView;
    final Activity activity = this;
    private String tag = "PayMentGateWay";
    private String hash, hashSequence;
//    ProgressDialog progressDialog;

    private KProgressHUD progressHUD;
    String action1 = "";
    // String merchant_key = "JzKklD85"; // live
    // String salt = "rx8giwCnO5"; // live
    // String base_url = "https://secure.payu.in"; // live

    String merchant_key = "kYz2vV"; // test
    String salt = "zhoXe53j"; // test
    String base_url = "https://test.payu.in";

    int error = 0;
    String hashString = "";
    Map<String, String> params;

    String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php"; // success
    String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php"; // failed

    Handler mHandler = new Handler();

    static String getFirstName, getNumber, getEmailAddress, getRechargeAmt, getTxnId, getProductInfo;

    ProgressDialog pDialog;
    private Intent intent;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

//        progressDialog = new ProgressDialog(activity);

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webView = new WebView(this);
        setContentView(webView);

        getRechargeAmt = getIntent().getStringExtra("amount");

        getFirstName = getIntent().getStringExtra("name");
        getEmailAddress = getIntent().getStringExtra("email");
        getNumber = getIntent().getStringExtra("mobile");
        getTxnId = getIntent().getStringExtra("mobile");
        SUCCESS_URL = getIntent().getStringExtra("sURL");
        FAILED_URL = getIntent().getStringExtra("fURL");
        getProductInfo = getIntent().getStringExtra("productInfo");

        //post_val = getIntent().getStringArrayListExtra("post_val");
        //Log.d(tag, "post_val: "+post_val);
        params = new HashMap<String, String>();
        params.put("key", merchant_key);
        params.put("amount", getRechargeAmt);
        params.put("firstname", getFirstName);
        params.put("email", getEmailAddress);
        params.put("phone", getNumber);
        params.put("productinfo", getProductInfo);
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("txnid", getTxnId);
        params.put("service_provider", "payu_paisa");
        params.put("lastname", "");
        params.put("address1", "");
        params.put("address2", "");
        params.put("city", "");
        params.put("state", "");
        params.put("country", "");
        params.put("zipcode", "");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");

   /*for(int i = 0;i<post_val.size();){
   params.put(post_val.get(i), post_val.get(i+1));

    i+=2;
  }*/

        // 8567ea1248360f4a9f25

        /*if (empty(params.get("txnid"))) {
            Random rand = new Random();
            String rndm = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
            txnid = hashCal("SHA-256", rndm).substring(0, 20);
            params.put("txnid", txnid);
        } else
            txnid = params.get("txnid");*/

        hash = "";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        if (empty(params.get("hash")) && params.size() > 0) {
            if (empty(params.get("key"))
                    || empty(params.get("txnid"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))

                    ) {
                error = 1;
            } else {
                String[] hashVarSeq = hashSequence.split("\\|");

                for (String part : hashVarSeq) {
                    hashString = (empty(params.get(part))) ? hashString.concat("") : hashString.concat(params.get(part));
                    hashString = hashString.concat("|");
                }
                hashString = hashString.concat(salt);

                hash = hashCal("SHA-512", hashString);
                action1 = base_url.concat("/_payment");
            }
        } else if (!empty(params.get("hash"))) {
            hash = params.get("hash");
            action1 = base_url.concat("/_payment");
        }

        webView.setWebViewClient(new MyWebViewClient() {

            public void onPageFinished(WebView view, final String url) {
                progressHUD.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if (!progressHUD.isShowing()) {
                    progressHUD.show();
                }
            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        //webView.addJavascriptInterface(new PayUJavaScriptInterface(getApplicationContext()), "PayUMoney");
        webView.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", PayMentGateWay_Test.this.hash);
        mapParams.put("txnid", (empty(PayMentGateWay_Test.this.params.get("txnid"))) ? "" : PayMentGateWay_Test.this.params.get("txnid"));
        Log.d(tag, "txnid: " + PayMentGateWay_Test.this.params.get("txnid"));
        mapParams.put("service_provider", "payu_paisa");

        mapParams.put("amount", (empty(PayMentGateWay_Test.this.params.get("amount"))) ? "" : PayMentGateWay_Test.this.params.get("amount"));
        mapParams.put("firstname", (empty(PayMentGateWay_Test.this.params.get("firstname"))) ? "" : PayMentGateWay_Test.this.params.get("firstname"));
        mapParams.put("email", (empty(PayMentGateWay_Test.this.params.get("email"))) ? "" : PayMentGateWay_Test.this.params.get("email"));
        mapParams.put("phone", (empty(PayMentGateWay_Test.this.params.get("phone"))) ? "" : PayMentGateWay_Test.this.params.get("phone"));

        mapParams.put("productinfo", (empty(PayMentGateWay_Test.this.params.get("productinfo"))) ? "" : PayMentGateWay_Test.this.params.get("productinfo"));
        mapParams.put("surl", (empty(PayMentGateWay_Test.this.params.get("surl"))) ? "" : PayMentGateWay_Test.this.params.get("surl"));
        mapParams.put("furl", (empty(PayMentGateWay_Test.this.params.get("furl"))) ? "" : PayMentGateWay_Test.this.params.get("furl"));
        mapParams.put("lastname", (empty(PayMentGateWay_Test.this.params.get("lastname"))) ? "" : PayMentGateWay_Test.this.params.get("lastname"));

        mapParams.put("address1", (empty(PayMentGateWay_Test.this.params.get("address1"))) ? "" : PayMentGateWay_Test.this.params.get("address1"));
        mapParams.put("address2", (empty(PayMentGateWay_Test.this.params.get("address2"))) ? "" : PayMentGateWay_Test.this.params.get("address2"));
        mapParams.put("city", (empty(PayMentGateWay_Test.this.params.get("city"))) ? "" : PayMentGateWay_Test.this.params.get("city"));
        mapParams.put("state", (empty(PayMentGateWay_Test.this.params.get("state"))) ? "" : PayMentGateWay_Test.this.params.get("state"));

        mapParams.put("country", (empty(PayMentGateWay_Test.this.params.get("country"))) ? "" : PayMentGateWay_Test.this.params.get("country"));
        mapParams.put("zipcode", (empty(PayMentGateWay_Test.this.params.get("zipcode"))) ? "" : PayMentGateWay_Test.this.params.get("zipcode"));
        mapParams.put("udf1", (empty(PayMentGateWay_Test.this.params.get("udf1"))) ? "" : PayMentGateWay_Test.this.params.get("udf1"));
        mapParams.put("udf2", (empty(PayMentGateWay_Test.this.params.get("udf2"))) ? "" : PayMentGateWay_Test.this.params.get("udf2"));

        mapParams.put("udf3", (empty(PayMentGateWay_Test.this.params.get("udf3"))) ? "" : PayMentGateWay_Test.this.params.get("udf3"));
        mapParams.put("udf4", (empty(PayMentGateWay_Test.this.params.get("udf4"))) ? "" : PayMentGateWay_Test.this.params.get("udf4"));
        mapParams.put("udf5", (empty(PayMentGateWay_Test.this.params.get("udf5"))) ? "" : PayMentGateWay_Test.this.params.get("udf5"));
        mapParams.put("pg", (empty(PayMentGateWay_Test.this.params.get("pg"))) ? "" : PayMentGateWay_Test.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());

    }

    private final class PayUJavaScriptInterface {

        PayUJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

                    intent = new Intent();
                    intent.putExtra("result", "done");
                    intent.putExtra("txnid", getTxnId);
                    setResult(101, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Successfully payment", Toast.LENGTH_LONG).show();

                }
            });
        }

        @JavascriptInterface
        public void failure(final String id, String error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    intent = new Intent();
                    intent.putExtra("result", "fail");
                    setResult(101, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Cancel payment", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    intent = new Intent();
                    intent.putExtra("result", "fail");
                    setResult(101, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Failed payment", Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    public void webview_ClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d(tag, "webview_ClientPost called");

        progressHUD.setLabel("Loading").setDetailsLabel("Please wait...");
        //setup and load the progress bar
        // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        // .setMessage("Loading. Please wait...");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }


    public void success(long id, final String paymentId) {

        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;

                //  new PostRechargeData().execute();

                Toast.makeText(getApplicationContext(), "Successfully payment\n redirect from Success Function", Toast.LENGTH_LONG).show();

            }
        });
    }


    public boolean empty(String s) {
        return s == null || s.trim().equals("");
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();


            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }

        return hexString.toString();
    }

    //String SUCCESS_URL = "https://pay.in/sccussful" ; // failed
    //String FAILED_URL = "https://pay.in/failed" ;
    //override the override loading method for the webview client
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

         /*if(url.contains("response.php") || url.equalsIgnoreCase(SUCCESS_URL)){

          new PostRechargeData().execute();

          Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from webview" ,Toast.LENGTH_LONG).show();

                return false;
         }else  */
            if (url.startsWith("http")) {
                //Toast.makeText(getApplicationContext(),url ,Toast.LENGTH_LONG).show();
                progressHUD.show();
                view.loadUrl(url);
                System.out.println("myresult " + url);
                //return true;
            } else {
                return false;
            }

            return true;
        }
    }

    /******************************************* send record to back end ******************************************/
    /*class PostRechargeData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(PayMentGateWay.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String doInBackground(String... args)
        {
            String strStatus = null;
            ProfileSessionManager ProSessionManager = new ProfileSessionManager(PayMentGateWay.this);

            String getUserid   = ProSessionManager.getSpeculatorId();
            String getSpeculationId  = "0";
            String rechargeAmt = getRechargeAmt;
            String postAction = "1";
            //http://speculometer.com/webService/stockApp/speculationMoneyreports.php?
            //access_token=ISOFTINCstockAppCheckDevelop&speculator=1&speculation=&amount=1000&action=1
            ServiceHandler sh = new ServiceHandler();
            String upLoadServerUri = ServiceList.payment_money_url+"speculator="+getUserid+"&speculation="+getSpeculationId+"&amount="+rechargeAmt+"&action="+postAction;

            try{
                String jsonStr = sh.makeServiceCall(upLoadServerUri, ServiceHandler.POST);
                JSONObject jsonObj  = new JSONObject(jsonStr);

                JSONObject jobjDoc = jsonObj.optJSONObject("document");
                JSONObject jobjRes = jobjDoc.optJSONObject("response");

                strStatus   = jobjRes.getString("status");
                //strMessage  = jobjRes.getString("message");
                String strUserId = jobjRes.getString("user_id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return strStatus;
        }

        protected void onPostExecute(final String strStatus)
        {

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    pDialog.dismiss();
                    if(strStatus != null && strStatus.equalsIgnoreCase("0")){
                        Toast.makeText(getApplicationContext(),"Your recharge amount not added in wallet." ,Toast.LENGTH_LONG).show();
                    }else if(strStatus != null && strStatus.equalsIgnoreCase("1")){

                        Toast.makeText(getApplicationContext(),"Your recharge amount added in wallet." ,Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
            });

        }
    }*/

    /******************************************* closed send record to back end ************************************/


}