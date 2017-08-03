package com.zwy.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.zwy.base.ZwyErrorReport;
import com.zwy.base.ZwyLog;

import java.lang.reflect.Field;


/**
 * 公共的app
 *
 * @author ForLyp
 */
public class ZwyApp extends Application {

    public static Context mContext;

    public void onCreate() {
        mContext = this;
        super.onCreate();
        try {
            ZwyLog.DEBUG = (Boolean) getBuildConfigValue("DEBUG");
        } catch (Exception e) {
            ZwyLog.DEBUG = false;
        }

        ZwyContextKeeper.init(this, new Handler());
        ZwyErrorReport.post_error();
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static Object getBuildConfigValue(String fieldName) {
        try {
            Class<?> clazz = Class.forName(mContext.getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
