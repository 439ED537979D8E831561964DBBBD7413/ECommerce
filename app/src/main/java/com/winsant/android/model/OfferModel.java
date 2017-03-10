package com.winsant.android.model;

import java.io.Serializable;

/**
 * Created by Developer on 2/21/2017.
 */

public class OfferModel implements Serializable {

    private String coupon_title;
    private String coupon_description;
    private String t_and_c;

    public OfferModel(String coupon_title, String coupon_description, String t_and_c) {
        this.coupon_title = coupon_title;
        this.coupon_description = coupon_description;
        this.t_and_c = t_and_c;
    }

    public String getCoupon_title() {
        return this.coupon_title;
    }

    public String getCoupon_description() {
        return this.coupon_description;
    }

    public String getT_and_c() {
        return this.t_and_c;
    }
}
