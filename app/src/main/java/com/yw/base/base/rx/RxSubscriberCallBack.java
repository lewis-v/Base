package com.yw.base.base.rx;

import android.util.Log;

import com.yw.base.App;
import com.yw.base.rxbus.MySubscriber;
import com.yw.base.utils.NetWorkUtils;



/**
 * Created by yw on 2017-08-08.
 */

public class RxSubscriberCallBack<T> extends MySubscriber<T> {
    private RxApiCallback<T> rxApiCallback;
    public RxSubscriberCallBack(RxApiCallback<T> mapiCallbackRx){
        this.rxApiCallback = mapiCallbackRx;
    }
    @Override
    public void onMError(Throwable e) {//获取服务器信息失败
        e.printStackTrace();
        Log.e("---onerr---",e.getMessage());
        //网络
        if (!NetWorkUtils.isNetConnected(App.getApp().getContext())) {//是否无网络
            rxApiCallback.onFailure(0, "无网络连接");
        }
        //服务器
        else{
            rxApiCallback.onFailure(1, "获取服务器数据失败");
        }
    }

    @Override
    public void onMNext(T t) {//成功时回调
        rxApiCallback.onSuccess(t);

    }
}


