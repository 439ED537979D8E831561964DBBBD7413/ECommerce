package com.winsant.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.adapter.FullScreenAdapter;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.views.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Pc-Android-1 on 10/14/2016.
 * <p>
 * TODO : Specific Product Details Display Activity
 */

public class FullScreenViewActivity extends AppCompatActivity {

    private Activity activity;
    private TextView mToolbar_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        activity = FullScreenViewActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }

        mToolbar_title.setText(getString(R.string.app_name));
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ico_arrow_back_svg);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });
        }

        setUI();
    }

    private void setUI() {

        ArrayList<String> productImages = getIntent().getStringArrayListExtra("images");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        FullScreenAdapter fullScreenAdapter = new FullScreenAdapter(activity, productImages);
        viewPager.setAdapter(fullScreenAdapter);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }
}
