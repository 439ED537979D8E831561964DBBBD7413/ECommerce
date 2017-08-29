package com.winsant.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.winsant.android.R;
import com.winsant.android.adapter.FAQListAdapter;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Pc-Android-1 on 9/29/2016.
 */
public class FAQActivity extends AppCompatActivity
{

    private Activity activity;
    FAQListAdapter faqListAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private KProgressHUD progressHUD;
    private LinearLayout ll_faq;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpfaq);

        activity = FAQActivity.this;

        ll_faq = (LinearLayout) findViewById(R.id.ll_faq);
        expListView = (ExpandableListView) findViewById(R.id.listFAQ);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView.setGroupIndicator(null);

        getFAQ();

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id)
            {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {

            @Override
            public void onGroupExpand(int groupPosition)
            {
                expListView.setSelectedGroup(groupPosition);
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener()
        {
            @Override
            public void onGroupCollapse(int groupPosition)
            {

            }
        });
    }

    private void getFAQ()
    {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

        JSONObject obj = new JSONObject();
        try {

            System.out.println(StaticDataUtility.APP_TAG + " getFAQ param --> " + obj.toString());

        } catch (Exception e) {
            System.out.println(StaticDataUtility.APP_TAG + " getFAQ param error --> " + e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StaticDataUtility.SERVER_URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(StaticDataUtility.APP_TAG + " getFAQ response --> " + response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            final String message = jsonObject.optString("message");
                            final String success = jsonObject.optString("success");

                            if (success.equals("1")) {

                                JSONArray jasonarray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jasonarray.length(); i++) {
                                    // Adding child data
                                    listDataHeader.add(jasonarray.getJSONObject(i).get("question").toString());
                                    // Adding child data
                                    List<String> child = new ArrayList<String>();
                                    child.add(jasonarray.getJSONObject(i).get("answer").toString());
                                    listDataChild.put(listDataHeader.get(i), child); // Header, Child data
                                }

                                faqListAdapter = new FAQListAdapter(activity, listDataHeader, listDataChild);
                                expListView.setAdapter(faqListAdapter);

                                progressHUD.dismiss();

                            } else {
                                progressHUD.dismiss();
                                CommonDataUtility.showSnackBar(ll_faq, message);
                            }
                        } catch (JSONException e) {

                            progressHUD.dismiss();
                            CommonDataUtility.showSnackBar(ll_faq, "Something went wrong,Try again later.");
                        }
                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {

                if (error instanceof NoConnectionError)
                {
                    progressHUD.dismiss();
                    CommonDataUtility.showSnackBar(ll_faq, activity.getString(R.string.no_internet));
                } else
                    {
                    progressHUD.dismiss();
                    CommonDataUtility.showSnackBar(ll_faq, "Something went wrong,Try again later.");
                }
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError
            {
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