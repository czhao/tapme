package com.garena.tapme.service;

import android.app.IntentService;
import android.content.Intent;

import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.storage.ActivityLogger;

/**
 * @author zhaocong
 * @date 30/10/14.
 */
public class ResponseIntentService extends IntentService {

    public ResponseIntentService(){
        super("accept wear intent service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //reset the timer
        AppLogger.i("reset the timer");
        ActivityLogger.resetTimer();
    }
}
