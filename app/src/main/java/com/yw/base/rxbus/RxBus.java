package com.yw.base.rxbus;


import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * company:52TT
 * data:2018/1/16
 * auth:lewis_v
 */

public class RxBus {
    private final static String TAG = "---RxBus---";

    private static RxBus Instance;
    private final Subject<Object> subject;
    private final Map<Class<?>,Object> mStickyMap;

    private RxBus(){
        subject = PublishSubject.create().toSerialized();
        mStickyMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault(){
        if (Instance == null){
            synchronized (RxBus.class){
                if (Instance == null){
                    Instance = new RxBus();
                }
            }
        }
        return Instance;
    }

    /**
     * 是否有订阅者
     * @return
     */
    public boolean hasObservable(){
        return subject.hasObservers();
    }

    /**
     * 发射普通消息
     * @param object
     */
    public void post(Object object){
        subject.onNext(object);
    }

    /**
     * 发射sticky消息
     * @param object
     */
    public void postSticky(Object object){
        synchronized (mStickyMap){
            mStickyMap.put(object.getClass(),object);
        }
        post(object);
    }

    /**
     * 获取指定类型的被监听者(普通)
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>Observable<T> tObservable(Class<T> clazz){
        Log.e(TAG,clazz.toString());
        return subject.ofType(clazz);
    }

    /**
     * 获取制定类型的额被监听者(Sticky)
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>Observable<T> tObservableSticky(final Class<T> clazz){
        synchronized (mStickyMap) {
            if (mStickyMap.containsKey(clazz)) {
                return tObservable(clazz).mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        emitter.onNext(clazz.cast(mStickyMap.get(clazz)));
                    }
                }));
            } else {
                return tObservable(clazz);
            }
        }
    }

    /**
     * 移除制定Sticky事件
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T removeStickyEvent(Class<T> clazz){
        synchronized (mStickyMap){
            return clazz.cast(mStickyMap.remove(clazz));
        }
    }

    /**
     * 获取制定类型的事件
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T getStickyEvent(Class<T> clazz){
        synchronized (mStickyMap){
            return clazz.cast(mStickyMap.get(clazz));
        }
    }

    /**
     * 移除所有sticky事件
     */
    public void removeAllStickyEvent(){
        synchronized (mStickyMap){
            mStickyMap.clear();
        }
    }

    public void destory(){
        removeAllStickyEvent();
        if (Instance != null){
            Instance = null;
        }
    }

    /**
     * 取消订阅,释放资源
     * @param disposable
     */
    public static void destory(Disposable disposable){
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
