package com.yw.base.base;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.yw.base.dialog.MyDialogLoadFragment;
import com.yw.base.utils.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yw on 2017-08-07.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;
    private boolean isWhile = true;
    private MyDialogLoadFragment myDialogLoadFragment;
    protected List<Thread> threadList = new ArrayList<>();
    private boolean isReLogining = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getScreenManager().pushActivity(this);
        try {
            //getGenericSuperclass获取类的超类的类型即<P>p的类型,ParameterizedType参数化类型
            // ,getActualTypeArguments返回表示此类型实际类型参数的 Type 对象的数组
            mPresenter = ((Class<P>) ((ParameterizedType) (getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[0])
                    .newInstance();

        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassCastException e) {
        }
        if (mPresenter != null){
            mPresenter.setContext(this);
            mPresenter.setmView(this);
        }
        setContentView(getLayoutId());
        initView();
    }

    /**
     * 初始化UI
     */
    protected abstract void initView();

    /**
     * 设置布局文件
     */
    protected abstract int getLayoutId();

    //改变通知栏颜色
    public void changeWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isWhile) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                Window window = getWindow();
                window.setStatusBarColor(Color.BLUE);

            }
        }
        if (isWhile){
            setMiuiStatusBarDarkMode(this,true);
            setMeizuStatusBarDarkIcon(this,true);
        }
    }

    public void setWhile(boolean aWhile) {
        isWhile = aWhile;
    }

    //设置成白色的背景，字体颜色为黑色。miui
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //设置成白色的背景，字体颜色为黑色。flyme
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     *  设置加载中提示框的显示
     * @param isLoadDialog
     */
    public void setLoadDialog(boolean isLoadDialog){
        if (isLoadDialog){
            if (myDialogLoadFragment == null){
                myDialogLoadFragment = new MyDialogLoadFragment();
                myDialogLoadFragment.show(getSupportFragmentManager(),"");
            }else{
                myDialogLoadFragment.dismiss();
                myDialogLoadFragment = new MyDialogLoadFragment();
                myDialogLoadFragment.show(getSupportFragmentManager(),"");
            }
        }else{
            if (myDialogLoadFragment != null) {
                myDialogLoadFragment.dismiss();
                myDialogLoadFragment = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        clearThread();
        ActivityStack.getScreenManager().popActivity(this);
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
        setLoadDialog(false);

    }

    /**
     * 重新登录错误
     * @param msg
     */
    public synchronized void onReLoginFail(String msg){
        if (isReLogining){
            return;
        }
        isReLogining = true;
        onFail(msg);
        Intent i = getPackageManager().getLaunchIntentForPackage(this.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        ActivityStack.getScreenManager().clearAllActivity();
        isReLogining = false;
    }
    /**
     * 默认成功返回
     * @param msg
     */
    public void onSuccess(String msg){
        ToastUtils.showSingleToast(msg);
        setLoadDialog(false);
    }

}
