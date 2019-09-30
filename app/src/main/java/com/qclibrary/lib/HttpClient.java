package com.qclibrary.lib;

import com.qclibrary.lib.io.gson.GsonSerializer;
import com.qclibrary.lib.io.http.QcAbsHttpClient;

import okhttp3.Interceptor;

public class HttpClient extends QcAbsHttpClient<ApiService> {

    private static volatile HttpClient mInstance;

    @Override
    protected String initBaseUrl() {
        return "";
    }

    public static HttpClient getInstance() {
        synchronized (HttpClient.class) {
            if (mInstance == null) {
                synchronized (HttpClient.class) {
                    mInstance = new HttpClient();
                }
            }
        }
        return mInstance;
    }

    @Override
    public Interceptor initCommonInterceptor() {
        return super.initCommonInterceptor();
    }
}
