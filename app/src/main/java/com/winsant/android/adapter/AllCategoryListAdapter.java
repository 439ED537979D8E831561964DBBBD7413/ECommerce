package com.winsant.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.model.CategoryModel;
import com.winsant.android.ui.ProductViewAllActivity;
import com.winsant.android.ui.SpecificCategoryListActivity;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class AllCategoryListAdapter extends RecyclerView.Adapter<AllCategoryListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<CategoryModel> AllCategoryList;
    private Intent intent;
    private RecyclerView viewAllList;

    public AllCategoryListAdapter(Activity activity, ArrayList<CategoryModel> AllCategoryList, RecyclerView viewAllList) {
        this.activity = activity;
        this.AllCategoryList = AllCategoryList;
        this.viewAllList = viewAllList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView subCategoryList;
        TextView categoryName;

        public ViewHolder(final View itemView) {
            super(itemView);

            subCategoryList = (RecyclerView) itemView.findViewById(R.id.subCategoryList);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            categoryName.setTypeface(CommonDataUtility.setTypeFace1(activity));

            categoryName.setTypeface(CommonDataUtility.setTitleTypeFace(activity), Typeface.BOLD);

            if (CommonDataUtility.isTablet(activity)) {
                categoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                subCategoryList.setLayoutManager(new GridLayoutManager(activity, 4));
            } else {
                categoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                subCategoryList.setLayoutManager(new GridLayoutManager(activity, 3));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        CategoryModel allCategoryModel = AllCategoryList.get(position);

        viewHolder.categoryName.setText(allCategoryModel.getCategory_name());
        viewHolder.subCategoryList.setAdapter(new AllSubCategoryListAdapter(activity, allCategoryModel.getSubCategoryArrayList(),
                viewAllList, new AllSubCategoryListAdapter.onClickListener() {
            @Override
            public void onClick(String category_name, String category_url, String is_last) {

                // TODO : Specific SubCategory with SubCategory Product Display Activity
                if (is_last.equals("0")) {
                    intent = new Intent(activity, SpecificCategoryListActivity.class);
                } else {
                    intent = new Intent(activity, ProductViewAllActivity.class);
                }

                intent.putExtra("url", category_url);
                intent.putExtra("name", category_name);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }, "circle"));
    }

    @Override
    public int getItemCount() {
        return AllCategoryList.size();

    }
}
