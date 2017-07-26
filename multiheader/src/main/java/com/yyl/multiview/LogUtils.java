package com.yyl.multiview;

import android.util.Log;

/**
 * Created by yuyunlong on 2017/7/21/021.
 */

public class LogUtils {


    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.i(tag, msg);
    }
}
