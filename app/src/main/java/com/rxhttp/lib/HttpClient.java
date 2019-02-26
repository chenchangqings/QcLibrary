package com.rxhttp.lib;

import android.util.Log;

import com.rxhttp.lib.gson.GsonSerializer;
import com.rxhttp.lib.interceptor.SimpleLoggingInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {
    private static HttpClient mHttpClient;
    private final ApiService mApiService;
    private final String TAG = "HttpClient";

    public static HttpClient getInstence(){
        if (mHttpClient==null){
            synchronized (HttpClient.class) {
                if (mHttpClient == null)
                    mHttpClient = new HttpClient();
            }
        }
        return mHttpClient;
    }

    private HttpClient() {
        SimpleLoggingInterceptor loggingInterceptor = new SimpleLoggingInterceptor(message -> Log.d("RetrofitLog", message));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(20);
        mApiService = new Retrofit.Builder()
                .baseUrl(ApiService.getBaseHostURL())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonSerializer.getInstance().getGson()))
                .client(okHttpClient)
                .build().create(ApiService.class);
    }

    public Observable<UserBean> getUserById(int id){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return mApiService.getUserById(parameters);
    }





}
