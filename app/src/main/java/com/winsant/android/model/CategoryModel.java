package com.winsant.android.model;

import java.util.ArrayList;

/**
 * Created by Developer on 2/10/2017.
 */

public class CategoryModel {

    private String category_name;
    private String category_image;
    private String category_url;
    private String is_last;

    private ArrayList<SubCategoryModel> subCategoryArrayList;
    private ArrayList<HomeProductModel> subCategoryProductList;

    public CategoryModel(String category_name, String category_image, String category_url) {

        this.category_name = category_name;
        this.category_image = category_image;
        this.category_url = category_url;
    }

    // TODO : All Category Display
    public CategoryModel(String category_name, String category_image, String category_url, ArrayList<SubCategoryModel> subCategoryArrayList) {

        this.category_name = category_name;
        this.category_image = category_image;
        this.category_url = category_url;
        this.subCategoryArrayList = subCategoryArrayList;
    }

    // TODO : All Category with SubCategory and SubCategory Product Display
    public CategoryModel(String category_name, String category_image, String category_url, ArrayList<HomeProductModel> subCategoryProductList, String is_last) {

        this.category_name = category_name;
        this.category_image = category_image;
        this.category_url = category_url;
        this.subCategoryProductList = subCategoryProductList;
        this.is_last = is_last;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public String getIs_last() {
        return this.is_last;
    }

    public String getCategory_image() {
        return this.category_image;
    }

    public String getCategory_url() {
        return this.category_url;
    }

    public ArrayList<SubCategoryModel> getSubCategoryArrayList() {
        return this.subCategoryArrayList;
    }

    public ArrayList<HomeProductModel> getSubCategoryProductList() {
        return this.subCategoryProductList;
    }
}
