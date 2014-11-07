package com.garena.tapme.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import com.garena.tapme.R;
import com.garena.tapme.application.SystemConst;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.storage.AppSettings;

public class HomeWearActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {

            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initUI();
                findViewById(R.id.btn_enable_tracking).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //enable the tracking intent
                        boolean serviceEnabled = AppSettings.isTrackingEnabled();
                        activateTracking(!serviceEnabled);
                        //enable the tracking, show confirmation page
                        Intent confirmAction = new Intent(HomeWearActivity.this, ConfirmUserActionActivity.class);
                        confirmAction.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,ConfirmationActivity.SUCCESS_ANIMATION);
                        startActivity(confirmAction);
                        initUI();
                    }
                });
            }
        });
    }

    private void initUI(){
        boolean serviceEnabled = AppSettings.isTrackingEnabled();
        Button actionBtn = (Button)findViewById(R.id.btn_enable_tracking);
        actionBtn.setText(serviceEnabled ?  R.string.label_disable_tracking : R.string.label_enable_tracking);
    }

    private void activateTracking(boolean isEnableAction) {
        //init a pending intent service to activate tracking
        PendingIntent trackTask = PendingIntent.getService(this, 0,
                new Intent(SystemConst.TRACKING_INTENT_FILTER),
                PendingIntent.FLAG_UPDATE_CURRENT);
        AppSettings.updateTracking(isEnableAction);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (isEnableAction) {
            AppLogger.i("tracking enabled");
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AppSettings.getReminderInterval(),
                    AppSettings.getReminderInterval(), trackTask);
        }else{
            AppLogger.i("tracking disabled");
            alarmManager.cancel(trackTask);
        }
    }
}
