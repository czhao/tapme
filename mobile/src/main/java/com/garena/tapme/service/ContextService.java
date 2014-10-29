package com.garena.tapme.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;

/**
 * @author zhaocong
 * @since 10/29/14.
 */
public class ContextService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private final static int DETECTION_INTERVAL_MILLISECONDS = 9000;
    private ActivityRecognitionClient mActivityRecognitionClient;

    @Override
    public void onCreate() {
        super.onCreate();
        initGoogleAPI();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void initGoogleAPI(){
        mActivityRecognitionClient = new ActivityRecognitionClient(this, this, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        //request for updates

        PendingIntent mActivityRecognitionPendingIntent = PendingIntent.getService(this,0, new Intent(this,ActivityRecognitionIntentService.class),0);
        mActivityRecognitionClient.requestActivityUpdates(
                DETECTION_INTERVAL_MILLISECONDS,
                mActivityRecognitionPendingIntent);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
