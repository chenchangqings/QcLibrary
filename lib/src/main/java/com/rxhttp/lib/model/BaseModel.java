package com.rxhttp.lib.model;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseModel {

    protected <T> ObservableTransformer<T, T> ioMainScheduler() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> ObservableTransformer<T, T> composeScheduler(Scheduler subscriber, Scheduler observer) {
        return upstream ->
                upstream.subscribeOn(subscriber)
                        .observeOn(observer);
    }

    protected <T> void requestNetwork(Observable<T> observable, Observer<T> observer) {
        requestNetwork(observable, observer, ioMainScheduler());
    }


    protected <T> void requestNetwork(Observable<T> observable, Observer<T> observer, ObservableTransformer<T, T> compose) {
        observable.compose(compose)
                .subscribe(observer);
    }



}
