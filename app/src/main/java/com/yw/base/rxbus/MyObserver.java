package com.yw.base.rxbus;

/**
 * company:52TT
 * data:2018/1/18
 * auth:lewis_v
 */

public abstract class MyObserver<T> extends MyLambdaObserver<T>{

    @Override
    protected void onMError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    protected void onMComplete() {

    }
}
