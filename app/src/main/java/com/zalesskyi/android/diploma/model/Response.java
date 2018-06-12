package com.zalesskyi.android.diploma.model;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("abstract")
    private String mAbstract;

    public String getAbstract() {
        return mAbstract;
    }

    public void setAbstract(String anAbstract) {
        mAbstract = anAbstract;
    }
}
