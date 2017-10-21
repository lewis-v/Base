package com.yw.base.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.lang.reflect.ParameterizedType;

/**
 * Created by yw on 2017-08-07.
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment {
    protected P mPresenter;
    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        //设置返回键dismiss并处罚onBack()方法
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    onBack();
                    return true;
                }
                return false;
            }
        });
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


    public void onBack(){
        dismiss();
    }
}
