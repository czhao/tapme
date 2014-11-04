package com.garena.tapme.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import com.garena.tapme.application.WearApplication;

/**
 * @author zhaocong
 * @since 11/1/14.
 */
public class AppSettings {

    private static SharedPreferences _storage;

    private static final String ENABLE_STATUS = "status";
    private static final String STEP_COUNT_LAST_KNOWN = "step_count_last_known";
    private static final String STEP_COUNT_RECORD_TIMESTAMP = "step_count_record_time";

    private static SharedPreferences getStorage(){
        if (_storage == null){
            _storage = WearApplication.getApplicationInstance().getSharedPreferences("app_profile", Context.MODE_PRIVATE);
        }
        return _storage;
    }

    public static boolean isTrackingEnabled(){
        return getStorage().getBoolean(ENABLE_STATUS,false);
    }

    public static void updateTracking(boolean isEnabled){
        SharedPreferences.Editor editor = getStorage().edit();
        editor.putBoolean(ENABLE_STATUS,isEnabled);
        editor.apply();
    }

    public static void addRecord(int step, long timestamp){
        SharedPreferences.Editor editor = getStorage().edit();
        editor.putInt(STEP_COUNT_LAST_KNOWN,step);
        editor.putLong(STEP_COUNT_RECORD_TIMESTAMP, timestamp);
        editor.apply();
    }

    public static Pair<Integer,Long> getLastKnownRecord(){
        int step = getStorage().getInt(STEP_COUNT_LAST_KNOWN,0);
        long time = getStorage().getLong(STEP_COUNT_RECORD_TIMESTAMP, 0l);
        return new Pair<Integer, Long>(step,time);
    }

}
