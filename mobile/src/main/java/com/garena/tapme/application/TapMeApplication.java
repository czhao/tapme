package com.garena.tapme.application;

import android.app.Application;

/**
 * @author zhaocong
 * @since  29/10/14.
 */
public class TapMeApplication extends Application {

    public static Application getApplicationInstance(){
        return mAppContext;
    }

    private static TapMeApplication mAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }
}
