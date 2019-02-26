package com.rxhttp.lib;

import java.util.List;

public class UserBean extends BaseBean<List<UserBean>>{
    private int id;
    private String account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
