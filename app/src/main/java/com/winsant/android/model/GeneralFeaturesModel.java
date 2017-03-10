package com.winsant.android.model;

/**
 * Created by Developer on 2/11/2017.
 */

public class GeneralFeaturesModel {

    private String attr_title;
    private String attr_value;

    public GeneralFeaturesModel(String attr_title, String attr_value) {
        this.attr_title = attr_title;
        this.attr_value = attr_value;
    }

    public String getAttr_title() {
        return this.attr_title;
    }

    public String getAttr_value() {
        return this.attr_value;
    }
}
