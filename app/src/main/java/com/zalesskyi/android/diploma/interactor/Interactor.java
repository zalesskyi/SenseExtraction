package com.zalesskyi.android.diploma.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zalesskyi.android.diploma.Api;
import com.zalesskyi.android.diploma.model.Request;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Interactor
        extends BaseInteractor
        implements InteractorContract {
    private static final String TAG = "SenseInteractor";

    public Interactor(Api api) {
        super.mApi = api;
    }

    @Override
    public Observable<JsonObject> toDoGetAbstract(String source, Integer coefficient) {
        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request(source, coefficient);
        String json = toJson(request);
        RequestBody body = RequestBody.create(mediaType, json);
            return mApi.getAbstract(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(response -> {
                        try {
                            return new Gson().fromJson(response.string(), JsonObject.class);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                            return null;
                        }
                    });
    }
}
