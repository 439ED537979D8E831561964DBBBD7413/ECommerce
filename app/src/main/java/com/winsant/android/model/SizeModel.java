package com.winsant.android.model;

import java.io.Serializable;

/**
 * Created by Developer on 2/21/2017.
 */

public class SizeModel implements Serializable {

    private String size_id;
    private String size_name;
    private String isSelect;

    public SizeModel(String size_id, String size_name, String isSelect) {
        this.size_id = size_id;
        this.size_name = size_name;
        this.isSelect = isSelect;
    }

    public String getSize_id() {
        return this.size_id;
    }

    public String getSize_name() {
        return this.size_name;
    }

    public String getIsSelect() {
        return this.isSelect;
    }
}
