package com.yw.base.base;

import android.content.Context;

import com.yw.base.base.rx.RxManager;


/**
 * Created by yw on 2017-08-07.
 */

public class BasePresenter<V> {
    protected V mView;
    protected Context context;
    public RxManager mRxManager=new RxManager();


    public BasePresenter(){
    }

    public void setmView(V mView) {
        this.mView = mView;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onDestroy(){
        mRxManager.clear();
    }


}
