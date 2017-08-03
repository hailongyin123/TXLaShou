package com.zwy.app;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 公共的context对象
 *
 * @author Administrator
 */
public class ZwyContextKeeper {
    /**
     * 全局管理的一个activity列表
     * 用的时候将相应的activity加载进来即可
     *
     * @author ForLyp
     */
    private static List<Activity> mList = new LinkedList<Activity>();
    static Handler mHandler = null;
    static Context mInstance = null;

    public static Context getInstance() {
        return mInstance;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    private static void setInstance(Context aInstance, Handler aHandler) {
        ZwyContextKeeper.mInstance = aInstance.getApplicationContext();
        ZwyContextKeeper.mHandler = aHandler;
    }

    static public void init(Context aInstance, Handler aHandler) {
        setInstance(aInstance, aHandler);
    }

    /**
     * sd卡路径
     *
     * @return
     * @author ForLyp
     */
    public static String getSDCardPath() {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
            String file_path = sdDir + "/" + mInstance.getPackageName() + "/";
            return file_path;
        }
        return "";
    }

    /**
     * data/data路径
     *
     * @return
     * @author ForLyp
     */
    public static String getDataPath() {
        File path = ZwyContextKeeper.getInstance().getFilesDir();
        return path.getAbsolutePath();
    }

    /**
     * 有sd卡保存sd卡，没有保存data路径
     *
     * @return
     * @author ForLyp
     */
    public static String getSavePath() {
        String path = getSDCardPath();
        if (TextUtils.isEmpty(path)) {
            return getDataPath();
        }
        return path;
    }

    public static void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 退出管理的全部activity
     *
     * @author ForLyp
     */
    public static void exit() {
        try {
            for (int i = 0; i < mList.size(); i++) {
                Activity activity = (Activity) mList.get(i);
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
