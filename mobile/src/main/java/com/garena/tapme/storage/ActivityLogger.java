package com.garena.tapme.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.garena.tapme.application.TapMeApplication;
import com.garena.tapme.helper.TimeHelper;

/**
 * @author zhaocong
 * @date 30/10/14.
 */
public class ActivityLogger {

    private static final String ACTIVITY_EVENT = "activity_event";

    private static final String LATEST_STILL_TIME = "still_latest_update";

    private static final String REMINDER_PERIOD = "reminder_period_seconds";

    private static SharedPreferences _storage;

    private static SharedPreferences getStorage(){
        if (_storage == null){
            _storage = TapMeApplication.getApplicationInstance().getSharedPreferences(ACTIVITY_EVENT, Context.MODE_PRIVATE);
        }
        return _storage;
    }

    /**
     * calculate how long I have remained before the PC
     * @return time period in million seconds
     */
    public static long getStillPeriod(){
        SharedPreferences logFile = getStorage();
        long recordTime = logFile.getLong(LATEST_STILL_TIME, 0l);
        if (recordTime == 0l){
            //no record
            //save a new record
            recordTime = TimeHelper.timestamp();
            SharedPreferences.Editor editor = logFile.edit();
            editor.putLong(LATEST_STILL_TIME,recordTime);
            editor.apply();
        }
        return TimeHelper.timestamp() - recordTime;
    }

    public static void resetTimer(){
        SharedPreferences logFile = getStorage();
        SharedPreferences.Editor editor = logFile.edit();
        editor.putLong(LATEST_STILL_TIME,TimeHelper.timestamp());
        editor.apply();
    }

    public static int getWarningPeriod(){
        SharedPreferences logFile = getStorage();
        return logFile.getInt(REMINDER_PERIOD,10);
    }

}
