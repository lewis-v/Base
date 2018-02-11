package com.yw.base.base.rx;


import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yw on 2017-08-08.
 */

public class RxManager {

    private CompositeDisposable mCompositeSubscription = new CompositeDisposable ();// 管理订阅者者

    //添加订阅者
    public void add(Flowable flowable, Subscriber subscriber) {
        mCompositeSubscription.add(
                (Disposable) flowable
                        .subscribeOn(Schedulers.io())//设置调用方法前在io线程中执行
                        .unsubscribeOn(Schedulers.io())//设置取消订阅在io线程中执行
                        .observeOn(AndroidSchedulers.mainThread())//设置调用方法后在主线程中执行
                        .subscribeWith(subscriber));//设置订阅者
    }
//    取消所有订阅者
    public void clear() {
        mCompositeSubscription.dispose();// 取消订阅
    }
}
