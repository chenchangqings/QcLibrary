package com.rxhttp.lib;

import com.rxhttp.lib.model.BaseModel;

import io.reactivex.Observable;

public class UserModelImpl extends BaseModel {

    public void getUserById(int id,NetObserver<UserBean> observer){
        Observable<UserBean> observable = HttpClient.getInstence().getUserById(id);
        requestNetwork(observable,observer);
    }
}
