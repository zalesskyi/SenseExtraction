package com.zalesskyi.android.diploma.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.zalesskyi.android.diploma.Api;
import com.zalesskyi.android.diploma.app.model.Request;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class Interactor
        extends BaseInteractor
        implements InteractorContract {
    private static final String TAG = "SenseInteractor";

    public Interactor(Api api) {
        super.mApi = api;
    }

    @Override
    public void toDoGetAbstract(String source, Integer coefficient) {
        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request(source, coefficient);
        String json = toJson(request);
        RequestBody body = RequestBody.create(mediaType, json);
            mApi.getAbstract(body)
                    .subscribeOn(Schedulers.io())
                    .subscribe(next -> {
                        try {
                            Log.i(TAG, next.string());
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }}, err -> Log.e(TAG, err.getMessage()));
    }
}
