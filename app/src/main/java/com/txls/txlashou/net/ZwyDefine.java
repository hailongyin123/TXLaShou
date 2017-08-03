package com.txls.txlashou.net;

import android.text.TextUtils;

public class ZwyDefine {
    // 服务器相关
    String URL = "http://172.31.84.38:8080/txls/";
    // 测试地址
    static public String HOST = "http://mp.tianxiacredit.cn/txls/";

    public static boolean OFFICIAL = true;

    //外部ip
    static public String OFFICIAL_HOST = "http://www.txlashou.com/";

    public static String getUrl(String key) {
        if (!TextUtils.isEmpty(key)) {
            return (OFFICIAL ? OFFICIAL_HOST : HOST)  + key/*                                                                     																															 */;
        }
        return null;
    }
}
