package com.garena.tapme.helper;

import android.os.Bundle;

import com.garena.tapme.application.TapMeApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

/**
 * @author zhaocong
 * @since  29/10/14.
 */
public class DeviceBridge {

    public static DeviceBridge getInstance(){
        if (_mInstance == null){
            synchronized (DeviceBridge.class){
                if (_mInstance == null){
                    _mInstance = new DeviceBridge();
                }
            }
        }
        return _mInstance;
    }


    private static DeviceBridge _mInstance;

    private GoogleApiClient mGoogleApiClient;

    private DeviceBridge() {
        establishConnection();
    }

    public void connectToWearDevice(){
        mGoogleApiClient.connect();
    }

    private void establishConnection(){

        mGoogleApiClient = new GoogleApiClient.Builder(TapMeApplication.getApplicationInstance()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
              @Override
              public void onConnected(Bundle bundle) {
                  AppLogger.i("Connection is ok");
              }

              @Override
              public void onConnectionSuspended(int i) {

              }
          }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
              @Override
              public void onConnectionFailed(ConnectionResult connectionResult) {
                  AppLogger.i("Connection failed");
              }
          }).addApi(Wearable.API).build();

    }

    public boolean isConnected(){
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }
}
