package com.winsant.android.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winsant.android.R;
import com.winsant.android.model.SubCategoryModel;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.views.CircleImageView;

import java.util.ArrayList;

public class AllSubCategoryListAdapter1 extends RecyclerView.Adapter<AllSubCategoryListAdapter1.ViewHolder> {

    private Activity activity;
    private ArrayList<SubCategoryModel> subCategoryArrayList;
    private onClickListener clickListener;
    private String type;
    private RecyclerView subCategoryList2;
    private AllSubCategoryListAdapter adapter;

    public AllSubCategoryListAdapter1(Activity activity, ArrayList<SubCategoryModel> subCategoryArrayList,
                                      RecyclerView subCategoryList2, AllSubCategoryListAdapter adapter, onClickListener clickListener, String type) {
        this.activity = activity;
        this.subCategoryArrayList = subCategoryArrayList;
        this.clickListener = clickListener;
        this.type = type;
        this.subCategoryList2 = subCategoryList2;
        this.adapter = adapter;
    }

    public interface onClickListener {
        void onClick(String category_name, String category_url, String is_last);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView subCategoryImage;
        ImageView SpecificSubCategoryImage, ViewMore, ViewLess;
        TextView subCategoryName;

        public ViewHolder(final View itemView) {
            super(itemView);

            if (type.equals("circle")) {
                subCategoryImage = (CircleImageView) itemView.findViewById(R.id.subCategoryImage);
            } else {
                SpecificSubCategoryImage = (ImageView) itemView.findViewById(R.id.SpecificSubCategoryImage);
                ViewMore = (ImageView) itemView.findViewById(R.id.ViewMore);
                ViewLess = (ImageView) itemView.findViewById(R.id.ViewLess);
            }
            subCategoryName = (TextView) itemView.findViewById(R.id.subCategoryName);
            subCategoryName.setTypeface(CommonDataUtility.setTypeFace1(activity));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onClick(subCategoryArrayList.get(getAdapterPosition()).getCategory_name(),
                                subCategoryArrayList.get(getAdapterPosition()).getCategory_url(), subCategoryArrayList.get(getAdapterPosition()).getIs_last());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        if (type.equals("circle")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_category_item, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.specific_sub_category_item, viewGroup, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if (type.equals("square")) {

            if (position == subCategoryArrayList.size()) {
                viewHolder.ViewLess.setVisibility(View.VISIBLE);
                viewHolder.subCategoryName.setText("View Less");
                viewHolder.SpecificSubCategoryImage.setVisibility(View.GONE);
            } else {

                SubCategoryModel subCategoryModel = subCategoryArrayList.get(position);

                viewHolder.ViewLess.setVisibility(View.GONE);
                viewHolder.subCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                viewHolder.subCategoryName.setText(subCategoryModel.getCategory_name());
                viewHolder.SpecificSubCategoryImage.setVisibility(View.VISIBLE);

                Picasso.with(activity).load(subCategoryModel.getCategory_image()).resize(150, 150).into(viewHolder.SpecificSubCategoryImage);
            }

            viewHolder.ViewMore.setVisibility(View.GONE);

            viewHolder.subCategoryName.setTag(position);
            viewHolder.subCategoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) v.getTag();

                    if (pos == getItemCount() - 1) {
                        subCategoryList2.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_out));
                        subCategoryList2.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            viewHolder.ViewLess.setTag(position);
            viewHolder.ViewLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subCategoryList2.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_out));
                    subCategoryList2.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {

            SubCategoryModel subCategoryModel = subCategoryArrayList.get(position);

            Picasso.with(activity).load(subCategoryModel.getCategory_image()).resize(120, 120).into(viewHolder.subCategoryImage);

            viewHolder.subCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            viewHolder.subCategoryName.setText(subCategoryModel.getCategory_name());
        }
    }

    @Override
    public int getItemCount() {
        return subCategoryArrayList.size() + 1;
    }
}
