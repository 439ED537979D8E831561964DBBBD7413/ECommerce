package com.winsant.android.model;

import java.util.ArrayList;

/**
 * Created by Developer on 2/10/2017.
 */

public class HomeHeaderModel {

    private String name;
    private String banner;
    private String banner_url;
    private String catt_view_all;
    private String banner1;
    private String banner1_url;
    private String banner2;
    private String banner2_url;
    private String is_festival;
    private String festival_banner;

    private ArrayList<HomeProductModel> categoryProductModels;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<BannerModel> bannerList;

    public HomeHeaderModel(String name, String banner, String banner_url, String catt_view_all, String banner1, String banner1_url, String banner2, String banner2_url,
                           String is_festival, String festival_banner, ArrayList<HomeProductModel> categoryProductModels, ArrayList<CategoryModel> categoryModels,
                           ArrayList<BannerModel> bannerList) {

        this.name = name;
        this.banner = banner;
        this.banner_url = banner_url;
        this.catt_view_all = catt_view_all;
        this.banner1 = banner1;
        this.banner1_url = banner1_url;
        this.banner2 = banner2;
        this.banner2_url = banner2_url;
        this.is_festival = is_festival;
        this.festival_banner = festival_banner;
        this.categoryProductModels = categoryProductModels;
        this.categoryModels = categoryModels;
        this.bannerList = bannerList;
    }

    public String getName() {
        return this.name;
    }

    public String getBanner() {
        return this.banner;
    }

    public String getBanner_url() {
        return this.banner_url;
    }

    public String getCatt_view_all() {
        return this.catt_view_all;
    }

    public String getBanner1() {
        return this.banner1;
    }

    public String getBanner1_url() {
        return this.banner1_url;
    }

    public String getBanner2() {
        return this.banner2;
    }

    public String getBanner2_url() {
        return this.banner2_url;
    }

    public String getIs_festival() {
        return this.is_festival;
    }

    public String getFestival_banner() {
        return this.festival_banner;
    }

    public ArrayList<HomeProductModel> getCategoryProductModels() {
        return this.categoryProductModels;
    }

    public ArrayList<CategoryModel> getCategoryModels() {
        return this.categoryModels;
    }

    public ArrayList<BannerModel> getBannerList() {
        return this.bannerList;
    }
}
