package com.winsant.android.model;

import java.io.Serializable;

/**
 * Created by Developer on 2/21/2017.
 */

public class BannerModel implements Serializable {

    private String banner_image;
    private String banner_url;

    public BannerModel(String banner_image, String banner_url) {
        this.banner_image = banner_image;
        this.banner_url = banner_url;
    }

    public String getBanner_image() {
        return this.banner_image;
    }

    public String getBanner_url() {
        return this.banner_url;
    }

}
