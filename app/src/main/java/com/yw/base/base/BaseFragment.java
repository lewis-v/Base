package com.yw.base.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yw.base.dialog.MyDialogLoadFragment;
import com.yw.base.utils.ToastUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yw on 2017-08-07.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected P mPresenter;
    protected View view;
    private MyDialogLoadFragment myDialogLoadFragment;
    protected List<Thread> threadList = new ArrayList<>();
    private boolean isReLogining = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        try {
            //getGenericSuperclass获取类的超类的类型即<P>p的类型,ParameterizedType参数化类型
            // ,getActualTypeArguments返回表示此类型实际类型参数的 Type 对象的数组
            mPresenter = ((Class<P>) ((ParameterizedType) (getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[0])
                    .newInstance();

        } catch (java.lang.InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassCastException e) {
        }
        if (mPresenter != null){
            mPresenter.setContext(getContext());
            mPresenter.setmView(this);
        }
        view = inflater.inflate(getLayoutId(), container, false);
        initView();
        return view;
    }

    /**
     * 初始化UI
     */
    protected abstract void initView();

    /**
     * 设置布局文件
     */
    protected abstract int getLayoutId();


    /**
     *  设置加载中提示框的显示
     * @param isLoadDialog
     */
    public void setLoadDialog(boolean isLoadDialog){
        if (isLoadDialog){
            if (myDialogLoadFragment == null){
                myDialogLoadFragment = new MyDialogLoadFragment();
                myDialogLoadFragment.show(getActivity().getSupportFragmentManager(),"");
            }else{
                myDialogLoadFragment.dismiss();
                myDialogLoadFragment = new MyDialogLoadFragment();
                myDialogLoadFragment.show(getActivity().getSupportFragmentManager(),"");
            }
        }else{
            if (myDialogLoadFragment != null) {
                myDialogLoadFragment.dismiss();
                myDialogLoadFragment = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        clearThread();
    }

    public void clearThread(){
        for (Thread thread: threadList){
            if (thread != null){
                thread.interrupt();
            }
        }
    }

    /**
     * 默认失败返回
     * @param msg
     */
    public void onFail(String msg){
        ToastUtils.showSingleToast(msg);
    }
    /**
     * 重新登录错误
     * @param msg
     */
    public void onReLoginFail(String msg){
        if (isReLogining){
            return;
        }
        isReLogining = true;
        onFail(msg);
        Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        ActivityStack.getScreenManager().clearAllActivity();
        isReLogining = false;
    }
}
