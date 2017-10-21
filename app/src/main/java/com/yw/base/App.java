package com.yw.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by yw on 2017/10/21.
 */

public class App extends Application{
    private static App app;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static App getApp(){
        if (app == null){
            app = new App();
        }
        return app;
    }

    public Context getContext(){
        return context;
    }
}
