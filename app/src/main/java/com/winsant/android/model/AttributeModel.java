package com.winsant.android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 2/21/2017.
 */

public class AttributeModel implements Serializable {

    private String color_id;
    private String color_name;
    private ArrayList<SizeModel> sizeList;
    private ArrayList<String> color_images;
    private String isSelect;

    public AttributeModel(String color_id, String color_name, ArrayList<SizeModel> sizeList, String isSelect, ArrayList<String> color_images) {
        this.color_id = color_id;
        this.color_name = color_name;
        this.sizeList = sizeList;
        this.color_images = color_images;
        this.isSelect = isSelect;
    }

    public AttributeModel(String color_id, String color_name, String isSelect, ArrayList<String> color_images) {
        this.color_id = color_id;
        this.color_name = color_name;
        this.isSelect = isSelect;
        this.color_images = color_images;
    }

    public String getColor_id() {
        return this.color_id;
    }

    public String getColor_name() {
        return this.color_name;
    }

    public ArrayList<SizeModel> getSizeList() {
        return this.sizeList;
    }

    public ArrayList<String> getColor_images() {
        return this.color_images;
    }

    public String getIsSelect() {
        return this.isSelect;
    }
}
