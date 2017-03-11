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
import com.winsant.android.model.SizeModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class AttributeListAdapter extends RecyclerView.Adapter<AttributeListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<AttributeModel> attributeModelArrayList;
    private onClickListener clickListener;
    private RecyclerView sizeList;
    private String first = "0";

    public AttributeListAdapter(Activity activity, ArrayList<AttributeModel> attributeModelArrayList, RecyclerView sizeList, onClickListener clickListener) {
        this.activity = activity;
        this.attributeModelArrayList = attributeModelArrayList;
        this.clickListener = clickListener;
        this.sizeList = sizeList;

        setArrayFirstTime();
    }

    private void setArrayFirstTime() {

        setSizeArray(0);
        for (int i = 0; i < attributeModelArrayList.size(); i++)
            attributeModelArrayList.set(i, new AttributeModel(attributeModelArrayList.get(i).getColor_id(), attributeModelArrayList.get(i).getColor_name(),
                    attributeModelArrayList.get(i).getSizeList(), "0"));
    }

    public interface onClickListener {
        void onColorClick(String color_id, String color_name);

        void onSizeClick(String size_id, String size_name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attributeName;
        private LinearLayout ll_attribute;

        public ViewHolder(final View itemView) {
            super(itemView);

            attributeName = (TextView) itemView.findViewById(R.id.attributeName);
            ll_attribute = (LinearLayout) itemView.findViewById(R.id.ll_attribute);
            attributeName.setTypeface(CommonDataUtility.setTypeFace1(activity));

            attributeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attribute_color_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        AttributeModel attributeModel = attributeModelArrayList.get(viewHolder.getAdapterPosition());

        viewHolder.attributeName.setText(attributeModel.getColor_name());

        if (attributeModel.getIsSelect().equals("1"))
            viewHolder.ll_attribute.setBackgroundResource(R.drawable.bg_attribute);
        else
            viewHolder.ll_attribute.setBackgroundResource(R.drawable.bg_offer);

        viewHolder.ll_attribute.setTag(viewHolder.getAdapterPosition());
        viewHolder.ll_attribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                first = "1";

                int pos = (int) v.getTag();

                if (clickListener != null)
                    clickListener.onColorClick(attributeModelArrayList.get(pos).getColor_id(), attributeModelArrayList.get(pos).getColor_name());

                if (attributeModelArrayList.get(pos).getIsSelect().equals("0")) {
                    attributeModelArrayList.set(pos, new AttributeModel(attributeModelArrayList.get(pos).getColor_id(), attributeModelArrayList.get(pos).getColor_name(),
                            attributeModelArrayList.get(pos).getSizeList(), "1"));

                    clickListener.onSizeClick("", "");
                    for (int i = 0; i < attributeModelArrayList.get(viewHolder.getAdapterPosition()).getSizeList().size(); i++)
                        attributeModelArrayList.get(viewHolder.getAdapterPosition()).getSizeList().set(i, new SizeModel(
                                attributeModelArrayList.get(viewHolder.getAdapterPosition()).getSizeList().get(i).getSize_id(),
                                attributeModelArrayList.get(viewHolder.getAdapterPosition()).getSizeList().get(i).getSize_name(), "0"));

                }

                for (int i = 0; i < attributeModelArrayList.size(); i++)
                    if (i == pos)
                        attributeModelArrayList.set(i, new AttributeModel(attributeModelArrayList.get(i).getColor_id(), attributeModelArrayList.get(i).getColor_name()
                                , attributeModelArrayList.get(i).getSizeList(), "1"));
                    else
                        attributeModelArrayList.set(i, new AttributeModel(attributeModelArrayList.get(i).getColor_id(), attributeModelArrayList.get(i).getColor_name()
                                , attributeModelArrayList.get(i).getSizeList(), "0"));

                notifyDataSetChanged();
                setSizeArray(pos);
            }
        });
    }

    private void setSizeArray(int position) {

        if (attributeModelArrayList.get(position).getSizeList().size() > 0)
            sizeList.setAdapter(new SizeAttributeListAdapter(activity, attributeModelArrayList.get(position).getSizeList(), new SizeAttributeListAdapter.onClickListener() {
                @Override
                public void onClick(String size_id, String size_name) {
                    if (clickListener != null)
                        clickListener.onSizeClick(size_id, size_name);
                }
            }));

        else {
            sizeList.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attributeModelArrayList.size();
    }
}
