package com.yw.base.main;

import android.os.Bundle;

import com.yw.base.R;
import com.yw.base.base.BaseActivity;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


}
