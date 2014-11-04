package com.garena.tapme.application;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.garena.tapme.helper.AppLogger;

import java.util.List;

/**
 * @author zhaocong
 * @since 11/1/14.
 */
public class WearApplication extends Application {

    public static Application getApplicationInstance(){
        return mAppContext;
    }

    private static WearApplication mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        //check the system configuration
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s:deviceSensors){
            AppLogger.i(s.getName());
        }
        Sensor stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepSensor != null){
            SystemConst.SENSOR_STEP_DETECTOR = true;
            AppLogger.i("step sensor is good to go %d %f",stepSensor.getMinDelay(),stepSensor.getPower());
        }

    }
}
