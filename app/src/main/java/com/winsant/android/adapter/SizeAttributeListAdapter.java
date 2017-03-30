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
import com.winsant.android.model.SizeModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class SizeAttributeListAdapter extends RecyclerView.Adapter<SizeAttributeListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<SizeModel> sizeModelArrayList;
    private onClickListener clickListener;

    public SizeAttributeListAdapter(Activity activity, ArrayList<SizeModel> sizeModelArrayList, onClickListener clickListener) {
        this.activity = activity;
        this.sizeModelArrayList = sizeModelArrayList;
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onClick(String size_id, String size_name);
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

        final SizeModel sizeModel = sizeModelArrayList.get(position);

        viewHolder.attributeName.setText(sizeModel.getSize_name());

        if (sizeModel.getIsSelect().equals("1"))
            viewHolder.ll_attribute.setBackgroundResource(R.drawable.bg_attribute);
        else
            viewHolder.ll_attribute.setBackgroundResource(R.drawable.bg_offer);

        viewHolder.ll_attribute.setTag(position);
        viewHolder.ll_attribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();

                if (clickListener != null)
                    clickListener.onClick(sizeModelArrayList.get(pos).getSize_id(), sizeModelArrayList.get(pos).getSize_name());

                if (sizeModelArrayList.get(pos).getIsSelect().equals("0"))
                    sizeModelArrayList.set(pos, new SizeModel(sizeModelArrayList.get(pos).getSize_id(), sizeModelArrayList.get(pos).getSize_name(),
                            "1"));

                for (int i = 0; i < sizeModelArrayList.size(); i++)
                    if (i == pos)
                        sizeModelArrayList.set(i, new SizeModel(sizeModelArrayList.get(i).getSize_id(), sizeModelArrayList.get(i).getSize_name(),
                                "1"));
                    else
                        sizeModelArrayList.set(i, new SizeModel(sizeModelArrayList.get(i).getSize_id(), sizeModelArrayList.get(i).getSize_name(),
                                "0"));

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizeModelArrayList.size();
    }
}
