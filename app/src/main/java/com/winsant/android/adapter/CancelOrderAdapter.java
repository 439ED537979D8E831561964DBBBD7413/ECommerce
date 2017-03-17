package com.winsant.android.adapter;

/**
 * Created by ANDROID on 30-05-2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.model.CancelReason;

import java.util.List;

/**
 * Created by akshay on 1/2/15.
 */
public class CancelOrderAdapter extends ArrayAdapter<CancelReason> {

    private Context context;
    private List<CancelReason> items;

    public CancelOrderAdapter(Context context, int resource, int textViewResourceId, List<CancelReason> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_city, parent, false);
        }
        CancelReason City = items.get(position);
        if (City != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);

            if (lblName != null)
                lblName.setText(items.get(position).getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_city, parent, false);
        }
        CancelReason City = items.get(position);
        if (City != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);

            if (lblName != null)
                lblName.setText(items.get(position).getName());
        }
        return view;
    }
}
