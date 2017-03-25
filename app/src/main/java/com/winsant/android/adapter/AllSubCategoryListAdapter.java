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

public class AllSubCategoryListAdapter extends RecyclerView.Adapter<AllSubCategoryListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<SubCategoryModel> subCategoryArrayList;
    private onClickListener clickListener;
    private String type;
    private RecyclerView subCategoryList2;

    public AllSubCategoryListAdapter(Activity activity, ArrayList<SubCategoryModel> subCategoryArrayList,
                                     RecyclerView subCategoryList2, onClickListener clickListener, String type) {
        this.activity = activity;
        this.subCategoryArrayList = subCategoryArrayList;
        this.clickListener = clickListener;
        this.type = type;
        this.subCategoryList2 = subCategoryList2;
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

            if (CommonDataUtility.isTablet(activity))
                subCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            else
                subCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

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
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final SubCategoryModel subCategoryModel = subCategoryArrayList.get(position);

        if (type.equals("square")) {

            if (position == subCategoryArrayList.size() - 1 && subCategoryModel.getIs_expand().equals("1")) {
                viewHolder.ViewMore.setVisibility(View.VISIBLE);
                viewHolder.subCategoryName.setText("View More");
                viewHolder.SpecificSubCategoryImage.setVisibility(View.GONE);
            } else {
                viewHolder.ViewMore.setVisibility(View.GONE);
                viewHolder.ViewLess.setVisibility(View.GONE);
                viewHolder.subCategoryName.setText(subCategoryModel.getCategory_name());
                viewHolder.SpecificSubCategoryImage.setVisibility(View.VISIBLE);
            }

            viewHolder.ViewLess.setVisibility(View.GONE);

            viewHolder.subCategoryName.setTag(position);
            viewHolder.subCategoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();

                    if (subCategoryArrayList.get(pos).getIs_expand().equals("1")) {
                        viewHolder.subCategoryName.setText(subCategoryModel.getCategory_name());
                        viewHolder.SpecificSubCategoryImage.setVisibility(View.VISIBLE);
                        subCategoryList2.setVisibility(View.VISIBLE);
                        subCategoryList2.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in));
                        viewHolder.ViewMore.setVisibility(View.GONE);
                    } else {
                        if (clickListener != null)
                            clickListener.onClick(subCategoryArrayList.get(pos).getCategory_name(),
                                    subCategoryArrayList.get(pos).getCategory_url(), subCategoryArrayList.get(pos).getIs_last());
                    }
                }
            });

            viewHolder.ViewMore.setTag(position);
            viewHolder.ViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();

                    if (subCategoryArrayList.get(pos).getIs_expand().equals("1")) {
                        viewHolder.subCategoryName.setText(subCategoryModel.getCategory_name());
                        viewHolder.SpecificSubCategoryImage.setVisibility(View.VISIBLE);
                        subCategoryList2.setVisibility(View.VISIBLE);
                        subCategoryList2.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in));
                        viewHolder.ViewMore.setVisibility(View.GONE);
                    } else {
                        if (clickListener != null)
                            clickListener.onClick(subCategoryArrayList.get(pos).getCategory_name(),
                                    subCategoryArrayList.get(pos).getCategory_url(), subCategoryArrayList.get(pos).getIs_last());
                    }
                }
            });

            Picasso.with(activity).load(subCategoryModel.getCategory_image()).resize(150, 150).into(viewHolder.SpecificSubCategoryImage);
        } else {
            Picasso.with(activity).load(subCategoryModel.getCategory_image()).resize(120, 120).into(viewHolder.subCategoryImage);

            viewHolder.subCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            viewHolder.subCategoryName.setText(subCategoryModel.getCategory_name());
        }
    }

    @Override
    public int getItemCount() {
        return subCategoryArrayList.size();
    }
}
