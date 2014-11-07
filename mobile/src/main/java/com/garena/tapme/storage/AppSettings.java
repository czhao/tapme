package com.garena.tapme.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.garena.tapme.application.Const;
import com.garena.tapme.application.TapMeApplication;
import com.garena.tapme.helper.TimeHelper;

/**
 * @author zhaocong
 * @date 30/10/14.
 */
public class AppSettings {

    private static final String REMINDER_INTERVAL = "reminder_interval";

    private static SharedPreferences _storage;

    private static SharedPreferences getStorage(){
        if (_storage == null){
            synchronized (AppSettings.class) {
                if (_storage == null) {
                    _storage = TapMeApplication.getApplicationInstance().getSharedPreferences("app_settings", Context.MODE_PRIVATE);
                }
            }
        }
        return _storage;
    }

    public static long getReminderInterval(){
        return getStorage().getLong(REMINDER_INTERVAL, Const.TIME_INTERVAL_MILLI_SECONDS);
    }

    public static void updateReminderInterval(long timeInterval){
        SharedPreferences.Editor editor = getStorage().edit();
        editor.putLong(REMINDER_INTERVAL, timeInterval);
        editor.apply();
    }

}
