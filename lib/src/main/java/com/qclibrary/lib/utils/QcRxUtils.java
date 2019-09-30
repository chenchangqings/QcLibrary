package com.qclibrary.lib.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

;

public class QcRxUtils {

    public static <T> ObservableTransformer<T, T> ioMainScheduler() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> ObservableTransformer<T, T> composeScheduler(Scheduler subscriber, Scheduler observer) {
        return upstream ->
                upstream.subscribeOn(subscriber)
                        .observeOn(observer);
    }


    public static void cancel(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
