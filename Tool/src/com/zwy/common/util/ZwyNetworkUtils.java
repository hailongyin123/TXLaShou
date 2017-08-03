package com.zwy.common.util;

import com.zwy.app.ZwyContextKeeper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 网络检测
 *
 * @author NightwisH
 */
public class ZwyNetworkUtils {
    public final static int NONE = 0; // 无网络
    public final static int WIFI = 1; // Wi-Fi
    public final static int MOBILE = 2; // 3G,GPRS

    /**
     * 获取当前网络状态
     *
     * @return
     */
    public static int getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) ZwyContextKeeper
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        // 手机网络判断
        NetworkInfo info = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null) {
            State state = info.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return MOBILE;
            }
        }
        // Wifi网络判断
        info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null) {
            State state = info.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return WIFI;
            }
        }
        return NONE;
    }

    public static boolean isNetCanUse() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ZwyContextKeeper
                    .getInstance().getSystemService(
                            Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

}