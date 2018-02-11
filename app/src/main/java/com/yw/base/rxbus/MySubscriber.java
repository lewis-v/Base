package com.yw.base.rxbus;

import org.reactivestreams.Subscription;

/**
 * company:52TT
 * data:2018/2/11
 * auth:lewis_v
 */

public abstract class MySubscriber<T> extends MyLambdaSubscriber<T>{
    @Override
    protected void onMSubscribe(Subscription s) {

    }

    @Override
    protected void onMError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    protected void onMComplete() {

    }
}
