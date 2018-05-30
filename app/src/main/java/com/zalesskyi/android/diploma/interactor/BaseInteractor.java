package com.zalesskyi.android.diploma.interactor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zalesskyi.android.diploma.Api;

public class BaseInteractor {

    protected Api mApi;

    protected String toJson(Object obj) throws NullPointerException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String mgson = gson.toJson(obj);
        return mgson;
    }
}
