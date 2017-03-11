package com.winsant.android.adapter;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.winsant.android.R;
import com.winsant.android.model.AddressModel;
import com.winsant.android.utils.StaticDataUtility;

import java.util.ArrayList;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<AddressModel> addressArrayList;
    private onClickListener clickListener;
    private String from;

    public AddressListAdapter(Activity activity, ArrayList<AddressModel> addressArrayList, onClickListener clickListener, String from) {
        this.activity = activity;
        this.addressArrayList = addressArrayList;
        this.clickListener = clickListener;
        this.from = from;
    }

    public interface onClickListener {
        void onClick(int position, String address_id, String address);

        void onDeleteClick(int position, String address_id, boolean isSelected);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton selectAddress;
        private TextView txtName, txtAddress, txtMobile;
        private Button btnEdit;
        private ImageView imgMore, imgPlace, imgCall;

        public ViewHolder(final View itemView) {
            super(itemView);

            selectAddress = (RadioButton) itemView.findViewById(R.id.selectAddress);

            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            imgMore = (ImageView) itemView.findViewById(R.id.imgMore);
            imgPlace = (ImageView) itemView.findViewById(R.id.imgPlace);
            imgCall = (ImageView) itemView.findViewById(R.id.imgCall);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtMobile = (TextView) itemView.findViewById(R.id.txtMobile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelected(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_all_address_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        AddressModel addressModel = addressArrayList.get(position);

        System.out.println(StaticDataUtility.APP_TAG + " getIsSelected --> " + addressModel.getIsSelected());

        viewHolder.selectAddress.setChecked(addressModel.getIsSelected());
        viewHolder.selectAddress.setTag(position);
        viewHolder.selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelected((int) v.getTag());
            }
        });

        viewHolder.txtName.setText(addressModel.getFirst_name() + " " + addressModel.getLast_name());

        if (from.equals("cart")) {

            viewHolder.imgMore.setVisibility(View.GONE);
            viewHolder.imgCall.setVisibility(View.INVISIBLE);
            viewHolder.imgPlace.setVisibility(View.INVISIBLE);

            if (addressModel.getIsSelected()) {
                viewHolder.btnEdit.setVisibility(View.VISIBLE);
                viewHolder.btnEdit.setTag(position);

                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = (int) v.getTag();

                        if (clickListener != null)
                            clickListener.onClick(pos, addressArrayList.get(pos).getAddress_id(), addressArrayList.get(pos).getAddress());
                    }
                });
            } else {
                viewHolder.btnEdit.setVisibility(View.GONE);
            }
        } else {

            viewHolder.btnEdit.setVisibility(View.GONE);
            viewHolder.imgMore.setVisibility(View.VISIBLE);
            viewHolder.imgCall.setVisibility(View.VISIBLE);
            viewHolder.imgPlace.setVisibility(View.VISIBLE);

            viewHolder.imgMore.setTag(position);
            viewHolder.imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    try {
                        PopupMenu popup = new PopupMenu(activity, v);
                        popup.getMenuInflater().inflate(R.menu.more_item_option, popup.getMenu());
                        popup.show();

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                int pos = (int) v.getTag();

                                switch (item.getItemId()) {
                                    case R.id.edit:

                                        if (clickListener != null)
                                            clickListener.onClick(pos, addressArrayList.get(pos).getAddress_id(), addressArrayList.get(pos).getAddress());

                                        break;

                                    case R.id.delete:

                                        if (clickListener != null)
                                            clickListener.onDeleteClick(pos, addressArrayList.get(pos).getAddress_id(), addressArrayList.get(pos).getIsSelected());

                                        break;
                                    default:
                                        break;
                                }

                                return true;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        if (addressModel.getMobile().equals(""))
            viewHolder.txtMobile.setText(String.format("Mobile No. : %s", addressModel.getPhone()));
        else
            viewHolder.txtMobile.setText(String.format("Mobile No. : %s", addressModel.getMobile()));

        viewHolder.txtAddress.setText(addressModel.getAddress());
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    private void updateSelected(int position) {

        AddressModel addressModel = addressArrayList.get(position);
        if (addressModel.getIsSelected()) {
            addressArrayList.set(position, new AddressModel(addressModel.getAddress_id(), addressModel.getFirst_name(),
                    addressModel.getLast_name(), addressModel.getAddress(), addressModel.getZipcode(), addressModel.getPhone(), addressModel.getMobile(),
                    addressModel.getLandmark(), addressModel.getCountry(), addressModel.getState(), addressModel.getCity(), true, addressModel.getIs_cod()));
        }

        for (int i = 0; i < addressArrayList.size(); i++) {
            AddressModel addressModel1 = addressArrayList.get(i);
            if (i == position)
                addressArrayList.set(position, new AddressModel(addressModel1.getAddress_id(), addressModel1.getFirst_name(),
                        addressModel1.getLast_name(), addressModel1.getAddress(), addressModel1.getZipcode(), addressModel1.getPhone(), addressModel1.getMobile(),
                        addressModel1.getLandmark(), addressModel1.getCountry(), addressModel1.getState(), addressModel1.getCity(), true, addressModel.getIs_cod()));
            else
                addressArrayList.set(i, new AddressModel(addressModel1.getAddress_id(), addressModel1.getFirst_name(),
                        addressModel1.getLast_name(), addressModel1.getAddress(), addressModel1.getZipcode(), addressModel1.getPhone(), addressModel1.getMobile(),
                        addressModel1.getLandmark(), addressModel1.getCountry(), addressModel1.getState(), addressModel1.getCity(), false, addressModel.getIs_cod()));

        }
        notifyDataSetChanged();
    }
}
