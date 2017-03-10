package com.winsant.android.model;

/**
 * Created by Developer on 2/10/2017.
 */

public class HomeProductModel {

    private String product_id;
    private String name;
    private String product_full_name;
    private String product_url;
    private String price;
    private String discount_price;
    private String discount_per;
    private String product_image;
    private String cart_url;
    private String fav_url;
    private String remove_url;
    private String review_count;
    private String availability;
    private String isFavorite;

    // TODO : Home Page Product Details Display
    public HomeProductModel(String name, String product_full_name, String product_url, String price, String discount_price, String discount_per, String product_image
            , String cart_url, String fav_url) {

        this.name = name;
        this.product_full_name = product_full_name;
        this.product_url = product_url;
        this.price = price;
        this.discount_price = discount_price;
        this.discount_per = discount_per;
        this.product_image = product_image;
        this.cart_url = cart_url;
        this.fav_url = fav_url;
    }

    // TODO : Wish List Page Product Details Display && SubCategory Product Details Display
    public HomeProductModel(String product_id, String name, String product_url, String price, String discount_price, String discount_per, String product_image
            , String fav_url, String remove_url, String availability, String review_count, String isFavorite) {

        this.product_id = product_id;
        this.name = name;
        this.product_url = product_url;
        this.price = price;
        this.discount_price = discount_price;
        this.discount_per = discount_per;
        this.product_image = product_image;
        this.fav_url = fav_url;
        this.remove_url = remove_url;
        this.availability = availability;
        this.review_count = review_count;
        this.isFavorite = isFavorite;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public String getName() {
        return this.name;
    }

    public String getProduct_full_name() {
        return this.product_full_name;
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

    public String getFav_url() {
        return this.fav_url;
    }

    public String getRemove_url() {
        return this.remove_url;
    }

    public String getCart_url() {
        return this.cart_url;
    }

    public String getReview_count() {
        return this.review_count;
    }

    public String getAvailability() {
        return this.availability;
    }

    public String getIsFavorite() {
        return this.isFavorite;
    }
}
