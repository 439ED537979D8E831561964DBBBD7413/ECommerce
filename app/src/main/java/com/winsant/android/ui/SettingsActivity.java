package com.winsant.android.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.kprogresshud.KProgressHUD;
import com.winsant.android.utils.CommonDataUtility;

public class SettingsActivity extends AppCompatActivity
{

    private TextView Toolbar_title;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            Toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        Toolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(SettingsActivity.this));

        String type = getIntent().getStringExtra("type");

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

        progressHUD = KProgressHUD.create(SettingsActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        WebView webView = (WebView) findViewById(R.id.webView);

        if (type.equals("policy")) {
            Toolbar_title.setText(getString(R.string.title_activity_policy));
            webView.loadUrl("http://api.winsant.com/privacy-policy");
        } else {
            Toolbar_title.setText(getString(R.string.title_activity_faq));
            webView.loadUrl("http://api.winsant.com/faq");
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.setWebViewClient(new MyWebViewClient()
        {

            public void onPageFinished(WebView view, final String url)
            {
                progressHUD.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                if (!progressHUD.isShowing())
                {
                    progressHUD.show();
                }
            }
        });
    }

    private class MyWebViewClient extends WebViewClient
    {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http")) {
                progressHUD.show();
                view.loadUrl(url);
            } else {
                return false;
            }

            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}