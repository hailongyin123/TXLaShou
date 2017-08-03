package com.txls.txlashou.util;

/**
 * 作者：YHL
 * 时间： 2017/3/7 10:53
 */

public class Utils {
    /**
     * 判断是否可点击
     *
     * @return
     * @author ZhangChongXuan
     */
    public static long lastClickTime;

    public static boolean isOnClickable() {

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return false;
        }
        lastClickTime = time;
        return true;
    }
}
