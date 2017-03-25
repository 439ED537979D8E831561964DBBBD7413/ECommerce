package com.winsant.android.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.model.OfferModel;
import com.winsant.android.utils.CommonDataUtility;

import java.util.ArrayList;

public class ProductOffersListAdapter extends RecyclerView.Adapter<ProductOffersListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<OfferModel> offersArrayList;
    private onClickListener clickListener;

    public ProductOffersListAdapter(Activity activity, ArrayList<OfferModel> offersArrayList, onClickListener clickListener) {
        this.activity = activity;
        this.offersArrayList = offersArrayList;
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onClick(String t_and_c);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCoupon, t_and_c;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtCoupon = (TextView) itemView.findViewById(R.id.txtCoupon);
            txtCoupon.setTypeface(CommonDataUtility.setTypeFace1(activity));

            t_and_c = (TextView) itemView.findViewById(R.id.t_and_c);
            t_and_c.setTypeface(CommonDataUtility.setTypeFace1(activity));

            if (activity.getResources().getBoolean(R.bool.isTablet) || activity.getResources().getBoolean(R.bool.isLargeTablet)) {
                txtCoupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                t_and_c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                txtCoupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                t_and_c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_offer_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final OfferModel offerModel = offersArrayList.get(position);

        viewHolder.txtCoupon.setText(offerModel.getCoupon_title() + " : " + offerModel.getCoupon_description());

        viewHolder.t_and_c.setTag(position);
        viewHolder.t_and_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();

                if (clickListener != null)
                    clickListener.onClick(offersArrayList.get(pos).getT_and_c());
            }
        });
    }

    @Override
    public int getItemCount() {
        return offersArrayList.size();
    }
}
