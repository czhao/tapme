package com.garena.tapme.application;

/**
 * @author zhaocong
 * @since 11/1/14.
 */
public class SystemConst {

    public static boolean SENSOR_STEP_DETECTOR = false;

    public static final long MILL_TO_SECONDS = 1000l;
    public static long TIME_INTERVAL_MILLI_SECONDS = 30 * 60 * MILL_TO_SECONDS; //every 1/2 hour
    public static int TIME_INTERVAL_STEP_MIN = 20;

    public static final String TRACKING_INTENT_FILTER = "com.garena.tapme.action.tracking";

    public static interface SHARE_KEY {

        public static final String SHARED_KEY_TIME_INTERVAL = "time_interval";

    }
}
