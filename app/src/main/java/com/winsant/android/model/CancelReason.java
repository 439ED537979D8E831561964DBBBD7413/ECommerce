package com.winsant.android.model;

/**
 * Created by ANDROID on 30-05-2016.
 */
public class CancelReason {

    private String name;
    private String id;

    public CancelReason(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
