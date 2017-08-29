package com.winsant.android.model;

/**
 * Created by Developer on 2/10/2017.
 */

public class OrderModel
{

    private String product_id;
    private String order_id;
    private String name;
    private String price;
    private String product_image;
    private String brand_name;
    private String qty;
    private String product_color;
    private String product_size;
    private String return_url;
    private String cancel_url;
    private String complaint_url;
    private String seller_feed_back_url;
    private String product_feed_back_url;
    private String order_received_date;
    private String is_cancel;
    private String order_status;
    private String order_delivered_date;
    private String order_ready_for_dispatch_date;
    private String order_cancel_date;
    private String order_return_date;

    // TODO : Cart Page Product Details Display
    public OrderModel(String product_id, String name, String product_image, String price, String brand_name, String qty, String product_color,
                      String product_size, String return_url, String cancel_url, String complaint_url, String seller_feed_back_url, String product_feed_back_url,
                      String order_received_date, String is_cancel, String order_id, String order_status, String order_delivered_date, String order_ready_for_dispatch_date,
                      String order_cancel_date, String order_return_date) {

        this.product_id = product_id;
        this.name = name;
        this.brand_name = brand_name;
        this.price = price;
        this.product_image = product_image;
        this.qty = qty;
        this.product_color = product_color;
        this.product_size = product_size;
        this.return_url = return_url;
        this.cancel_url = cancel_url;
        this.complaint_url = complaint_url;
        this.seller_feed_back_url = seller_feed_back_url;
        this.product_feed_back_url = product_feed_back_url;
        this.order_received_date = order_received_date;
        this.is_cancel = is_cancel;
        this.order_id = order_id;
        this.order_status = order_status;
        this.order_delivered_date = order_delivered_date;
        this.order_ready_for_dispatch_date = order_ready_for_dispatch_date;
        this.order_cancel_date = order_cancel_date;
        this.order_return_date = order_return_date;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public String getOrder_id() {
        return this.order_id;
    }

    public String getName() {
        return this.name;
    }

    public String getBrand_name() {
        return this.brand_name;
    }

    public String getPrice() {
        return this.price;
    }

    public String getProduct_image() {
        return this.product_image;
    }

    public String getQty() {
        return this.qty;
    }

    public String getProduct_color() {
        return this.product_color;
    }

    public String getProduct_size() {
        return this.product_size;
    }

    public String getReturn_url() {
        return this.return_url;
    }

    public String getCancel_url() {
        return this.cancel_url;
    }

    public String getComplaint_url() {
        return this.complaint_url;
    }

    public String getSeller_feed_back_url() {
        return this.seller_feed_back_url;
    }

    public String getProduct_feed_back_url() {
        return this.product_feed_back_url;
    }

    public String getOrder_received_date() {
        return this.order_received_date;
    }

    public String getIs_cancel() {
        return this.is_cancel;
    }

    public String getOrder_status() {
        return this.order_status;
    }

    public String getOrder_delivered_date() {
        return this.order_delivered_date;
    }

    public String getOrder_ready_for_dispatch_date() {
        return this.order_ready_for_dispatch_date;
    }

    public String getOrder_cancel_date() {
        return this.order_cancel_date;
    }

    public String getOrder_return_date() {
        return this.order_return_date;
    }

}
