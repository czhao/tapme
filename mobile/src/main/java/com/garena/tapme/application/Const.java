package com.garena.tapme.application;

/**
 * @author zhaocong
 * @date 30/10/14.
 */
public class Const {

    public static final long MILL_TO_SECONDS = 1000l;
    public static long TIME_INTERVAL_MILLI_SECONDS = 30 * 60 * MILL_TO_SECONDS; //every 1/2 hour

    public static interface SHARE_KEY {

        public static final String SHARED_KEY_TIME_INTERVAL = "time_interval";

    }
}
