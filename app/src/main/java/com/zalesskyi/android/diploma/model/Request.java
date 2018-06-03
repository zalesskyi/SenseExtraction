package com.zalesskyi.android.diploma.model;

import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("source")
    private String mSource;

    @SerializedName("coefficient")
    private int mCoefficient;

    public Request(String source, int coefficient) {
        mSource = source;
        mCoefficient = coefficient;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public int getCoefficient() {
        return mCoefficient;
    }

    public void setCoefficient(int coefficient) {
        mCoefficient = coefficient;
    }
}
