package com.qclibrary.lib.utils;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class QcUtils {
    private static final String TAG = "Utils";
    private static Application mApplication;

    public static Application getApplication() {
        return mApplication;
    }

    public static void setApplication(Application mApplication) {
        QcUtils.mApplication = mApplication;
    }
}
