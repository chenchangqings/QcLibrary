package com.rxhttp.lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tvTitle);
        getUser();
    }

    public void getUser(){
        new UserModelImpl().getUserById(1, new NetObserver<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                tvTitle.setText(userBean.getData().get(0).getAccount());
            }

            @Override
            public void onFailed(Throwable e) {
                tvTitle.setText(e.getMessage());
            }

        });
    }


}
