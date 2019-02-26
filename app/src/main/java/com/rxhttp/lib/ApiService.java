package com.rxhttp.lib;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {
    String BASE_URL_TEST = "http://10.0.2.138:8080";

    static String getBaseHostURL() {
        return BASE_URL_TEST;
    }


    @GET("/user/getUserById")
    Observable<UserBean> getUserById(@QueryMap Map<String, Object> body);

}
