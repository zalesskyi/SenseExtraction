package com.zalesskyi.android.diploma;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface Api {
    @POST("/getAbstract")
    Observable<ResponseBody> getAbstract(@Body RequestBody requestBody);
}
