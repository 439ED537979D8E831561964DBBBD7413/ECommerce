package com.winsant.android.model;

import java.io.Serializable;

/**
 * Created by Developer on 2/21/2017.
 */

public class SizeModel implements Serializable
{

    private String size_id;
    private String size_name;
    private String isSelect;
    private String price;
    private String discount_price;
    private String discount_per;

    public SizeModel(String size_id, String size_name, String isSelect, String price, String discount_price, String discount_per)
    {
        this.size_id = size_id;
        this.size_name = size_name;
        this.isSelect = isSelect;
        this.price = price;
        this.discount_price = discount_price;
        this.discount_per = discount_per;
    }

    public String getSize_id()
    {
        return this.size_id;
    }

    public String getSize_name()
    {
        return this.size_name;
    }

    public String getPrice()
    {
        return this.price;
    }

    public String getDiscount_price()
    {
        return this.discount_price;
    }

    public String getDiscount_per()
    {
        return this.discount_per;
    }

    public String getIsSelect()
    {
        return this.isSelect;
    }
}
