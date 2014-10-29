package com.garena.tapme.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garena.tapme.R;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.helper.DeviceBridge;
import com.garena.tapme.service.ActivityRecognitionIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;


public class HomeActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener  {

    private ActivityRecognitionClient mActivityRecognitionClient;
    private final static int DETECTION_INTERVAL_MILLISECONDS = 5000; //5 seconds
    private boolean mInProgress;

    private enum Action {
        ENABLE_SERVICE,DISABLE_SERVICE,NOT_SPECIFIED
    }

    private Action currentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //add on click listener
        findViewById(R.id.tapme_btn_test_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //push a notification to the wear app
                DeviceBridge.getInstance().connectToWearDevice();
            }
        });

        findViewById(R.id.tapme_enable_activity_recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enable the physical activity recognition
                initializeActivityRecognition();
                currentAction = Action.ENABLE_SERVICE;
            }
        });

        findViewById(R.id.tapme_disable_activity_recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeActivityRecognition();
                currentAction = Action.DISABLE_SERVICE;
            }
        });

    }

    private void initializeActivityRecognition(){
        mActivityRecognitionClient = new ActivityRecognitionClient(this, this, this);
        mActivityRecognitionClient.connect();
    }

    //service callback - start
    @Override
    public void onConnected(Bundle bundle) {
        AppLogger.i("service connected");
        mInProgress = true;
        //query the updates and those updates will be sent via broadcast
        PendingIntent mActivityRecognitionPendingIntent = PendingIntent.getService(HomeActivity.this,
                0, new Intent(HomeActivity.this,ActivityRecognitionIntentService.class),PendingIntent.FLAG_UPDATE_CURRENT);

        switch (currentAction){
            case ENABLE_SERVICE:
                mActivityRecognitionClient.requestActivityUpdates(
                        DETECTION_INTERVAL_MILLISECONDS,
                        mActivityRecognitionPendingIntent);
                break;
            case DISABLE_SERVICE:
                mActivityRecognitionClient.removeActivityUpdates(mActivityRecognitionPendingIntent);
                break;
        }
        mActivityRecognitionClient.disconnect();
    }

    @Override
    public void onDisconnected() {
        mInProgress = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    //service callback - end

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
