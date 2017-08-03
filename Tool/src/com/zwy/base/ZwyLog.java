package com.zwy.base;

import com.zwy.common.util.ZwyOSInfo;

import android.util.Log;

public class ZwyLog {
    private static final String TAG = "zhiwy";
    private static String LOGTAG;
    public static boolean DEBUG = true;

    private static boolean isCanLog() {
        String imei = ZwyOSInfo.getImei();
        if ("357555057192512".equals(imei) || "357556057192510".equals(imei)
                || "357555057192512".equals(imei)|| "865267029534863".equals(imei)) {
            return true;
        }
//        return DEBUG || Log.isLoggable(TAG, Log.VERBOSE);
        return true;
    }

    private static String getMessage(String msg) {
        StackTraceElement[] stackTraceElements = new Throwable()
                .getStackTrace();
        LOGTAG = stackTraceElements[2].getFileName();
        return "method=(" + stackTraceElements[2].getMethodName() + "()/line:"
                + stackTraceElements[2].getLineNumber() + ")" + ": message = "
                + msg;
    }

    public static void v(String msg) {
        if (isCanLog()) {
            String message = getMessage(msg);
            Log.v(LOGTAG, message);
        }
    }

    public static void d(String msg) {
        if (isCanLog()) {
            String message = getMessage(msg);
            Log.d(LOGTAG, message);
        }
    }

    public static void i(String msg) {
        if (isCanLog()) {
            String message = getMessage(msg);
            Log.i(LOGTAG, message);
        }
    }

    public static void e(String msg) {
        if (isCanLog()) {
            String message = getMessage(msg);
            Log.e(LOGTAG, message);
        }
    }

    public static void w(String msg) {
        if (isCanLog()) {
            String message = getMessage(msg);
            Log.w(LOGTAG, message);
        }
    }

    public static void wtf(String msg) {
        if (isCanLog()) {
            String message = getMessage(msg);
            Log.wtf(LOGTAG, message);
        }
    }

    public static void i(String tag, String msg) {
        if (isCanLog()) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isCanLog()) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isCanLog()) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isCanLog()) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isCanLog()) {
            Log.v(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (isCanLog()) {
            Log.wtf(tag, msg);
        }
    }
}
