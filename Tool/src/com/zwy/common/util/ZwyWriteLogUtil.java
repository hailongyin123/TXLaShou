package com.zwy.common.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zwy.app.ZwyContextKeeper;

/**
 * 记录日志的类 方便问题的排查
 *
 * @author Administrator
 */
public class ZwyWriteLogUtil {
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    private static final String sep = " ";
    public static boolean flag = true;

    public static void Log(String str) {
        if (flag) {
            return;
        }
        try {
            String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
            File logFile = new File(sdDir + "/Logs.txt");
            FileOutputStream logStream = new FileOutputStream(logFile, true);
            logStream.write(DATA_FORMAT.format(new Date(System.currentTimeMillis())).getBytes());
            logStream.write(sep.getBytes());
            logStream.write(str.getBytes());
            logStream.write("\r\n".getBytes());
            logStream.flush();
            logStream.close();
        } catch (Exception e) {
        }
    }
}
