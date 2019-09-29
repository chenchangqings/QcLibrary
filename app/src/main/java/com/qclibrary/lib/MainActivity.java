package com.qclibrary.lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvTitle;
    private Button btnRequest;
    private UserModelImpl userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tvTitle);
        btnRequest = findViewById(R.id.btn_request);
        userModel =  new UserModelImpl();
    }

    public void getUser(){
        userModel.getUserById(1, new NetObserver<UserBean>() {
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
