package com.winsant.android.model;

/**
 * Created by Developer on 2/10/2017.
 */

public class CartModel {

    private String product_id;
    private String name;
    private String product_url;
    private String price;
    private String discount_price;
    private String discount_per;
    private String product_image;
    private String remove_url;
    private String qty;
    private String availability;
    private String color_name;
    private String size_name;
    private String product_color;
    private String product_size;

    // TODO : Cart Page Product Details Display
    public CartModel(String product_id, String name, String product_url, String price, String discount_price, String discount_per, String product_image,
                     String remove_url, String qty, String availability, String color_name, String size_name, String product_color, String product_size) {

        this.product_id = product_id;
        this.name = name;
        this.product_url = product_url;
        this.price = price;
        this.discount_price = discount_price;
        this.discount_per = discount_per;
        this.product_image = product_image;
        this.remove_url = remove_url;
        this.qty = qty;
        this.availability = availability;
        this.color_name = color_name;
        this.size_name = size_name;
        this.product_color = product_color;
        this.product_size = product_size;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public String getName() {
        return this.name;
    }

    public String getProduct_url() {
        return this.product_url;
    }

    public String getPrice() {
        return this.price;
    }

    public String getDiscount_price() {
        return this.discount_price;
    }

    public String getDiscount_per() {
        return this.discount_per;
    }

    public String getProduct_image() {
        return this.product_image;
    }

    public String getRemove_url() {
        return this.remove_url;
    }

    public String getQty() {
        return this.qty;
    }

    public String getAvailability() {
        return this.availability;
    }

    public String getSize_name() {
        return this.size_name;
    }

    public String getColor_name() {
        return this.color_name;
    }

    public String getProduct_color() {
        return this.product_color;
    }

    public String getProduct_size() {
        return this.product_size;
    }
}
