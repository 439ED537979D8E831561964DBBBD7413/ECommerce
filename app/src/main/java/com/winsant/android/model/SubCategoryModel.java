package com.winsant.android.model;

/**
 * Created by Developer on 2/10/2017.
 */

public class SubCategoryModel
{

    private String category_name;
    private String category_image;
    private String category_url;
    private String is_last;
    private String is_expand = "0";

    // TODO : All SubCategory and SubCategory Product Display
    public SubCategoryModel(String category_name, String category_image, String category_url, String is_last, String is_expand) {

        this.category_name = category_name;
        this.category_image = category_image;
        this.category_url = category_url;
        this.is_last = is_last;
        this.is_expand = is_expand;
    }

    // TODO : All SubCategory and SubCategory Product Display
    public SubCategoryModel(String category_name, String category_image, String category_url, String is_last) {

        this.category_name = category_name;
        this.category_image = category_image;
        this.category_url = category_url;
        this.is_last = is_last;
    }

    public String getCategory_name()
    {
        return this.category_name;
    }

    public String getCategory_image()
    {
        return this.category_image;
    }

    public String getCategory_url()
    {
        return this.category_url;
    }

    public String getIs_last()
    {
        return this.is_last;
    }

    public String getIs_expand()
    {
        return this.is_expand;
    }
}
