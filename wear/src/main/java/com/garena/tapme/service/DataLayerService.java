package com.garena.tapme.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import com.garena.tapme.application.SystemConst;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.storage.AppSettings;
import com.google.android.gms.wearable.*;

/**
 * @author zhaocong
 * @since 11/7/14.
 */
public class DataLayerService extends WearableListenerService{

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        AppLogger.i("onDataChanged");

        for (DataEvent event: dataEvents){

            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                DataMap dataMap = DataMap.fromByteArray(item.getData());
                long timeInterval = dataMap.getLong(SystemConst.SHARE_KEY.SHARED_KEY_TIME_INTERVAL);
                //update the local storage
                AppSettings.updateReminderInterval(timeInterval);
                if (AppSettings.isTrackingEnabled()){
                    //restart the service
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    AppLogger.i("tracking enabled %d",timeInterval);
                    PendingIntent trackTask = PendingIntent.getService(this, 0,
                            new Intent(SystemConst.TRACKING_INTENT_FILTER),
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AppSettings.getReminderInterval(),
                            AppSettings.getReminderInterval(), trackTask);
                }
            }
        }

    }
}
