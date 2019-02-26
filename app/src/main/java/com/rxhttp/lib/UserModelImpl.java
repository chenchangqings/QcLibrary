package com.rxhttp.lib;

import io.reactivex.Observable;

public class UserModelImpl extends BaseModelImpl{

    public void getUserById(int id,NetObserver<UserBean> observer){
        Observable<UserBean> observable = HttpClient.getInstence().getUserById(id);
        requestNetwork(observable,observer);
    }
}
