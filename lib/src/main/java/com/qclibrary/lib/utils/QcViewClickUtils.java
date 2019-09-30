package com.qclibrary.lib.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class QcViewClickUtils {
    public static String TAG = "ViewClick";
    private static final int DEFAULT=300;
    /**
     *  view click
     */
    public static void rxViewClick(@NonNull View view, @NonNull Consumer<Object> consumer) {
        rxViewClick(view, DEFAULT, consumer);
    }

    /**
     *  view click
     */
    @SuppressLint("CheckResult")
    public static void rxViewClick(@NonNull View view, long skipDuration, @NonNull Consumer<Object> consumer) {
        RxView.clicks(view)
              .throttleFirst(skipDuration, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
              .subscribe(consumer, new Consumer<Throwable>() {
                  @Override
                  public void accept (Throwable throwable) throws Exception {
                      Log.d(TAG,throwable.getMessage());
                  }
              });
    }
    /**
     * view click and request android permission
     */
    public static void rxViewClick(@NonNull FragmentActivity activity, @NonNull View view, @NonNull Consumer<Boolean> consumer, String... permissions) {
        rxViewClick(activity,view,DEFAULT,consumer,permissions);
    }
    /**
     * view click and request android permission
     */
    @SuppressLint("CheckResult")
    public static void rxViewClick(@NonNull final FragmentActivity activity, @NonNull View view, long skipDuration, @NonNull final Consumer<Boolean> consumer, final String... permissions) {
        RxView.clicks(view)
                .throttleFirst(skipDuration, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .flatMap(new Function<Object, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply (Object o) throws Exception {
                        return new RxPermissions(activity).request(permissions);
                    }
                })
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept (Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                    }
                });
    }
}
