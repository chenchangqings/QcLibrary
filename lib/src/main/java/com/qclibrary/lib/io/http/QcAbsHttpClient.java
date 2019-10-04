package com.qclibrary.lib.io.http;

import android.util.Log;
import android.view.View;

import com.qclibrary.lib.io.gson.GsonSerializer;
import com.qclibrary.lib.io.http.interceptor.SimpleLoggingInterceptor;
import com.qclibrary.lib.utils.QcStringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class QcAbsHttpClient<T> {
    private final String HTTP_DEFAULT_LOG_TAG = "QcHttpClient";
    private String mHttpLogTag;
    private boolean isLogOpen;
    private OkHttpClient mOkHttpClient;
    private T mApiService;

    private static volatile QcAbsHttpClient mInstance;

    public QcAbsHttpClient build(){
        mOkHttpClient = initHttpClient();
        mApiService = initRetrofitBuilder().build().create(analysisApiService());
        return this;
    }

    public QcAbsHttpClient setLogOpen(boolean isOpen){
        this.isLogOpen = isOpen;
        return this;
    }

    public QcAbsHttpClient setLogTag(String logTag){
        this.mHttpLogTag = logTag;
        return this;
    }

    protected abstract String initBaseUrl();

    public Retrofit.Builder initRetrofitBuilder(){
        Retrofit.Builder builder =  new Retrofit.Builder()
                .baseUrl(initBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonSerializer.getInstance().getGson()));
        if(mOkHttpClient != null){
            builder.client(mOkHttpClient);
        }
        return builder;
    }

    public OkHttpClient initHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS);
        if(isLogOpen){
            builder.addInterceptor(initLogInterceptor());
        }
        Interceptor commonInterceptor = initCommonInterceptor();
        if(commonInterceptor != null){
            builder.addInterceptor(commonInterceptor);
        }
        OkHttpClient okHttpClient = builder.build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(20);
        return okHttpClient;
    }

    public Interceptor initLogInterceptor(){
        return new SimpleLoggingInterceptor(new SimpleLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (!QcStringUtils.isEmpty(mHttpLogTag)) {
                    Log.d(mHttpLogTag, message);
                } else {
                    Log.d(HTTP_DEFAULT_LOG_TAG, message);
                }
            }
        });
    }

    public Interceptor initCommonInterceptor(){
        return null;
    }

    private Class<T> analysisApiService (){
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        Class<T> tClass = (Class<T>) actualTypeArguments[0];
        return tClass;
    }


    public T getApiService(){
        return mApiService;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public boolean isLogOpen() {
        return isLogOpen;
    }
}
