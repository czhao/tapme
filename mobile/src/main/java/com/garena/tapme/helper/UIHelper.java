package com.garena.tapme.helper;

import android.widget.Toast;

import com.garena.tapme.application.TapMeApplication;

/**
 * @author zhaocong
 * @date 29/10/14.
 */
public class UIHelper {

    public static void showShortToast(String toastContent){
        Toast.makeText(TapMeApplication.getApplicationInstance(),toastContent,Toast.LENGTH_SHORT).show();
    }
}
