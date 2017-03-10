package com.winsant.android.model;

import java.io.Serializable;

/**
 * Created by Developer on 2/23/2017.
 */

public class AddressModel implements Serializable {

    private String address_id;
    private String first_name;
    private String last_name;
    private String address;
    private String zipcode;
    private String phone;
    private String mobile;
    private String landmark;
    private String country;
    private String state;
    private String city;
    private String is_cod;
    private boolean isSelected;

    public AddressModel(String address_id, String first_name, String last_name, String address, String zipcode, String phone, String mobile, String landmark
            , String country, String state, String city, boolean isSelected, String is_cod) {

        this.address_id = address_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.zipcode = zipcode;
        this.phone = phone;
        this.mobile = mobile;
        this.landmark = landmark;
        this.country = country;
        this.state = state;
        this.city = city;
        this.isSelected = isSelected;
        this.is_cod = is_cod;
    }

    public String getAddress_id() {
        return this.address_id;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getLandmark() {
        return this.landmark;
    }

    public String getCountry() {
        return this.country;
    }

    public String getState() {
        return this.state;
    }

    public String getCity() {
        return this.city;
    }

    public String getIs_cod() {
        return this.is_cod;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }
}
