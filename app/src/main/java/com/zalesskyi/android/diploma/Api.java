package com.zalesskyi.android.diploma;

import com.zalesskyi.android.diploma.model.Response;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface Api {
    @POST("/getAbstract")
    Observable<Response> getAbstract(@Body RequestBody requestBody);
}
