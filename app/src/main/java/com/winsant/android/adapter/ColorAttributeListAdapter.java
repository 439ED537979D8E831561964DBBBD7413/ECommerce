package com.winsant.android.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.model.AttributeModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class ColorAttributeListAdapter extends RecyclerView.Adapter<ColorAttributeListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<AttributeModel> attributeModelArrayList;
    private onClickListener clickListener;

    public ColorAttributeListAdapter(Activity activity, ArrayList<AttributeModel> attributeModelArrayList, onClickListener clickListener) {
        this.activity = activity;
        this.attributeModelArrayList = attributeModelArrayList;
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onClick(String color_id, String color_name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attributeName;
        private LinearLayout ll_attribute;

        public ViewHolder(final View itemView) {
            super(itemView);

            attributeName = (TextView) itemView.findViewById(R.id.attributeName);
            ll_attribute = (LinearLayout) itemView.findViewById(R.id.ll_attribute);
            attributeName.setTypeface(CommonDataUtility.setTypeFace1(activity));

            if (activity.getResources().getBoolean(R.bool.isLargeTablet)) {
                attributeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            } else if (activity.getResources().getBoolean(R.bool.isTablet)) {
                attributeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                attributeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attribute_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final AttributeModel attributeModel = attributeModelArrayList.get(position);

        viewHolder.attributeName.setText(attributeModel.getColor_name());

        if (attributeModel.getIsSelect().equals("1"))
            viewHolder.ll_attribute.setBackgroundResource(R.drawable.bg_attribute);
        else
            viewHolder.ll_attribute.setBackgroundResource(R.drawable.bg_offer);

        viewHolder.ll_attribute.setTag(position);
        viewHolder.ll_attribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();

                if (clickListener != null)
                    clickListener.onClick(attributeModelArrayList.get(pos).getColor_id(), attributeModelArrayList.get(pos).getColor_name());

                if (attributeModelArrayList.get(pos).getIsSelect().equals("0"))
                    attributeModelArrayList.set(pos, new AttributeModel(attributeModelArrayList.get(pos).getColor_id(), attributeModelArrayList.get(pos).getColor_name(),
                            "1", attributeModelArrayList.get(pos).getColor_image(), attributeModelArrayList.get(pos).getColor_images()));

                for (int i = 0; i < attributeModelArrayList.size(); i++)
                    if (i == pos)
                        attributeModelArrayList.set(i, new AttributeModel(attributeModelArrayList.get(i).getColor_id(), attributeModelArrayList.get(i).getColor_name(),
                                "1", attributeModelArrayList.get(i).getColor_image(), attributeModelArrayList.get(i).getColor_images()));
                    else
                        attributeModelArrayList.set(i, new AttributeModel(attributeModelArrayList.get(i).getColor_id(), attributeModelArrayList.get(i).getColor_name(),
                                "0", attributeModelArrayList.get(i).getColor_image(), attributeModelArrayList.get(i).getColor_images()));

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributeModelArrayList.size();
    }
}
