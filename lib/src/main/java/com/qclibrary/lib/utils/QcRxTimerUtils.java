package com.qclibrary.lib.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class QcRxTimerUtils {
    private static QcRxTimerUtils mInstance;
    private static List<DisposableBean> disposableList;
    private int id = -1;


    public static QcRxTimerUtils getInstance() {
        if (mInstance == null) {
            mInstance = new QcRxTimerUtils();
        }
        return mInstance;
    }

    public int timer(String tag, long milliseconds, final IRxNext next) {
        id++;
        int mId = id;
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        addDisPosable(new DisposableBean(mId, tag, disposable));
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancelById(mId);
                    }

                    @Override
                    public void onComplete() {
                        cancelById(mId);
                    }
                });
        return mId;
    }

    public int interval(String tag, long milliseconds, final IRxNext next) {
        id++;
        int mId = id;
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        addDisPosable(new DisposableBean(mId, tag, disposable));
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mId;
    }


    public int intervalInitial(int initialDelay, String tag, long milliseconds, final IRxNext next) {
        id++;
        int mId = id;
        Observable.interval(initialDelay, milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        addDisPosable(new DisposableBean(mId, tag, disposable));
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mId;
    }


    public void cancelByTag(String tag) {
        if (disposableList == null || tag == null) {
            return;
        }
        for (int i = 0; i < disposableList.size(); i++) {
            if (disposableList.get(i) != null && tag.equals(disposableList.get(i).getTag())) {
                cancel(disposableList.get(i), i);
            }
        }
    }


    public void cancelById(int id) {
        if (disposableList == null) {
            return;
        }
        for (int i = 0; i < disposableList.size(); i++) {
            if (disposableList.get(i) != null && id == disposableList.get(i).getId()) {
                cancel(disposableList.get(i), i);
            }
        }
    }

    private void cancel(DisposableBean bean, int i) {
        Disposable mDisposable = bean.getDisposable();
        if (mDisposable == null)
            return;
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
            disposableList.remove(i);
        } else {
            disposableList.remove(i);
        }
        Log.d("RxTimerUtil", "cancel id=" + bean.getId() + ",tag=" + bean.getTag() + ",size=" + disposableList.size());
    }


    private void addDisPosable(DisposableBean disposableBean) {
        if (disposableBean == null) {
            return;
        }
        if (disposableList == null) {
            disposableList = new ArrayList<>();
        }
        disposableList.add(disposableBean);
        Log.d("RxTimerUtil", "subscribe id=" + disposableBean.getId() + ",tag=" + disposableBean.getTag() + ",size=" + disposableList.size());
    }


    public interface IRxNext {
        void doNext(long number);
    }


    public class DisposableBean {
        int id;
        String tag;
        Disposable disposable;


        public DisposableBean(int id, String tag, Disposable disposable) {
            this.disposable = disposable;
            this.id = id;
            this.tag = tag;
        }

        public Disposable getDisposable() {
            return disposable;
        }

        public void setDisposable(Disposable disposable) {
            this.disposable = disposable;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }


}
