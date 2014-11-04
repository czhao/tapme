package com.garena.tapme.helper;

import android.util.Log;

/**
 * @author zhaocong
 * @since  29/10/14.
 */
public class AppLogger {

    private static final String _appTag = "tapme";

    public static void i(String format,Object... args){
                
        Log.i(_appTag,String.format(format,args));
    }

}
