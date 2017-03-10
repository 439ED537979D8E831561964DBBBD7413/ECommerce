package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.adapter.AttributeListAdapter;
import com.winsant.android.model.AttributeModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

/**
 * Created by Pc-Android-1 on 10/14/2016.
 * <p>
 * TODO : Specific Product Details Display Activity
 */

public class AttributeActivity extends AppCompatActivity {

    private Activity activity;
    private TextView mToolbar_title;

    private RecyclerView SizeList;
    private RecyclerView ColorList;
    private TextView txtColor, txtSize;
    private String ColorName, SizeName, ColorId, SizeId;

    private Button btnCancel, btnApply;
    private RelativeLayout rl_attribute;

    private ArrayList<AttributeModel> attributeModelArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attribute);

        activity = AttributeActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            mToolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
        mToolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));

        if (getIntent().hasExtra("name"))
            mToolbar_title.setText(getIntent().getStringExtra("name"));
        else
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

        rl_attribute = (RelativeLayout) findViewById(R.id.rl_attribute);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnApply = (Button) findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("type", "no");
                setResult(2, intent);
                finish();
                overridePendingTransition(R.anim.slide_down, 0);
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ColorName.equals("")) {
                    CommonDataUtility.showSnackBar(rl_attribute, "Select color");
                } else if (SizeName.equals("")) {
                    CommonDataUtility.showSnackBar(rl_attribute, "Select size");
                } else {

                    Intent intent = new Intent();
                    intent.putExtra("type", "yes");
                    intent.putExtra("color", ColorName);
                    intent.putExtra("size", SizeName);
                    intent.putExtra("ColorId", ColorId);
                    intent.putExtra("SizeId", SizeId);
                    setResult(2, intent);
                    finish();//finishing activity
                    overridePendingTransition(R.anim.slide_down, 0);
                }
            }
        });

        attributeModelArrayList = (ArrayList<AttributeModel>) getIntent().getSerializableExtra("attrib");

        setUI();
    }

    private void setUI() {

        txtColor = (TextView) findViewById(R.id.txtColor);
        txtSize = (TextView) findViewById(R.id.txtSize);

        ColorList = (RecyclerView) findViewById(R.id.ColorList);
        SizeList = (RecyclerView) findViewById(R.id.SizeList);
        ColorList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SizeList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        ColorList.setAdapter(new AttributeListAdapter(activity, attributeModelArrayList, SizeList, txtSize,
                new AttributeListAdapter.onClickListener() {
                    @Override
                    public void onColorClick(String color_id, String color_name) {
                        ColorId = color_id;
                        ColorName = color_name;
                        txtColor.setText("Color - " + color_name);
                    }

                    @Override
                    public void onSizeClick(String size_id, String size_name) {
                        SizeId = size_id;
                        SizeName = size_name;
                        txtSize.setText("Size - " + size_name);
                    }
                }));

        txtColor = (TextView) findViewById(R.id.txtColor);
        txtSize = (TextView) findViewById(R.id.txtSize);
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("type", "no");
        setResult(2, intent);
        finish();
        overridePendingTransition(R.anim.slide_down, 0);
    }
}