package com.yw.base.base.rx;

/**
 * Created by yw on 2017-08-08.
 */

public interface RxApiCallback<T> {
    void onSuccess(T model);

    void onFailure(int code, String msg);

}
