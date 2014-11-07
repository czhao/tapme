package com.garena.tapme.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Pair;
import com.garena.tapme.R;
import com.garena.tapme.activity.HomeWearActivity;
import com.garena.tapme.application.SystemConst;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.helper.TimeHelper;
import com.garena.tapme.storage.AppSettings;

import java.util.Random;

/**
 * @author zhaocong
 * @since 11/1/14.
 */
public class CheckStepService extends IntentService implements SensorEventListener {

    public CheckStepService() {
        super("tracking_service");
    }

    private SensorManager mSensorManager;
    private Sensor mStepSensor;

    private final static int _notificationId = 1;

    @Override
    protected void onHandleIntent(Intent intent) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mStepSensor != null){
            mSensorManager.registerListener(this,mStepSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        AppLogger.i("service is running");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        AppLogger.i("service on sensor changed");
        if (event != null && event.values.length > 0){
            int step = (int) event.values[0];
            AppLogger.i("step so far %d",step);

            //get last known record
            Pair<Integer,Long> record = AppSettings.getLastKnownRecord();

            if (record.first != 0 && step - record.first <= SystemConst.TIME_INTERVAL_STEP_MIN){
                //fire the notification
                fireNotification();
            }
            //compare with last known result
            AppSettings.addRecord(step, TimeHelper.timestamp());

        }
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void fireNotification(){

        Intent viewIntent = new Intent(this, HomeWearActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);


        long[] pattern = {0,100,100,200,300};

        //randomly pick the wording
        Random randomGenerator = new Random();
        int ram = randomGenerator.nextInt() % 3;
        int reminderStringRes;
        int backgroundRes = R.drawable.bg_android_studio;
        switch (ram){
            case 0:
                reminderStringRes = R.string.label_time_for_walk;
                break;
            case 1:
                reminderStringRes = R.string.label_drink_more_water;
                backgroundRes = R.drawable.drinking_water;
                break;
            case 2:
                reminderStringRes = R.string.label_rest_your_eye;
                backgroundRes = R.drawable.trees;
                break;
            default:
                reminderStringRes = R.string.label_time_for_walk;
        }

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(),backgroundRes));

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.one_finger_tap)
                        .setContentTitle(getString(R.string.app_reminder_title))
                        .setContentText(getString(reminderStringRes))
                        .setVibrate(pattern)
                        .extend(wearableExtender)
                        .setContentIntent(viewPendingIntent);
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Build the notification and issues it with notification manager.
        notificationManager.notify(_notificationId, notificationBuilder.build());
    }
}
