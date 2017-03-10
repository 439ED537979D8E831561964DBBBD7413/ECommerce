package com.winsant.android.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.model.GeneralFeaturesModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class GeneralFeatureAdapter extends RecyclerView.Adapter<GeneralFeatureAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<GeneralFeaturesModel> generalFeaturesModels;

    public GeneralFeatureAdapter(Activity activity, ArrayList<GeneralFeaturesModel> generalFeaturesModels) {

        this.activity = activity;
        this.generalFeaturesModels = generalFeaturesModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAttrTitle, txtAttrValue;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtAttrTitle = (TextView) itemView.findViewById(R.id.txtAttrTitle);
            txtAttrValue = (TextView) itemView.findViewById(R.id.txtAttrValue);

            txtAttrTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtAttrTitle.setTypeface(CommonDataUtility.setTypeFace(activity));

            txtAttrValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtAttrValue.setTypeface(CommonDataUtility.setTypeFace(activity));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_features, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GeneralFeaturesModel featuresModel = generalFeaturesModels.get(position);

        holder.txtAttrTitle.setText(featuresModel.getAttr_title());
        holder.txtAttrValue.setText(featuresModel.getAttr_value());

    }

    @Override
    public int getItemCount() {
        return generalFeaturesModels.size();
    }
}
