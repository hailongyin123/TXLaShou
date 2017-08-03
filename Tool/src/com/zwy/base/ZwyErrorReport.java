package com.zwy.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zwy.app.ZwyContextKeeper;
import com.zwy.common.util.ZwyOSInfo;

/**
 * 错误日志报告
 *
 * @author ForZwy
 */
public class ZwyErrorReport {
    public static boolean ENABLE = false;

    public static final String ERR_TAG = "ErrorReport.java";

    public static final String LOG_TYPE_EXCEPTION = "Exception";

    public static final String LOG_TYPE_CRASH_EXCEPTION = "Crash_Exception";

    public static final String INDEX_NAME = "Error_Index.log";

    public static final String INFO_NAME = "Error_Info.log";

    public static final String ZIP_FILE = "Error.zip";

    public static final String CLASS_INDEX_FIELD_NAME = "ERR_TAG";

    public static final int ERR_LOG_VERSION = 1;

    public static final String SEND_URL = "";

    public static final String FilePath = ZwyContextKeeper.getSDCardPath()
            + "error/";

    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    private static final UncaughtExceptionHandler sDefUncaughtExceptionHandler = Thread
            .getDefaultUncaughtExceptionHandler();

    /**
     * 日志概要输出流
     */
    // private static FileOutputStream errorIndexOS = null;

    /**
     * 日志详情输出流
     */
    // private static FileOutputStream errorInfoOS = null;

    /**
     * 加载之后就可以自动保存错误日志
     *
     * @return
     * @author ForLyp
     */
    public static final boolean init() {

        if (!ENABLE) {
            return false;
        }

        try {
            if (sDefUncaughtExceptionHandler == Thread
                    .getDefaultUncaughtExceptionHandler()) {
                Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {
                        outException(ex, LOG_TYPE_CRASH_EXCEPTION);
                        sDefUncaughtExceptionHandler.uncaughtException(thread,
                                ex);
                    }
                });
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        outIndexString(DATA_FORMAT.format(new Date(System.currentTimeMillis()))
                + "|ERR_LOG_VERSION: " + ERR_LOG_VERSION);

        return true;
    }

    /**
     * 输出异常信息
     *
     * @param e 异常信息
     */
    public static final void outException(Throwable e) {
        if (!ENABLE) {
            return;
        }
        Log.e("ErrorReport", e.getClass().getName());
        outException(e, null);
    }

    /**
     * 输出异常信息
     *
     * @param e    异常信息
     * @param type 异常类新
     */
    public static final void outException(Throwable e, String type) {
        if (!ENABLE) {
            return;
        }
        if (e == null) {
            return;
        }
        if (type == null) {
            type = LOG_TYPE_EXCEPTION;
        }
        if (!init()) {
            return;
        }
        long startIndex = -1;

        outInfoString(e.toString());
        StackTraceElement[] stack = e.getStackTrace();
        for (StackTraceElement element : stack) {
            outInfoString("\tat " + element + "\t, Index:"
                    + getClassIndex(element));
        }

        StackTraceElement[] parentStack = stack;
        Throwable throwable = e.getCause();
        while (throwable != null) {
            outInfoString("Caused by: ");
            outInfoString(throwable.toString());
            StackTraceElement[] currentStack = throwable.getStackTrace();
            int duplicates = countDuplicates(currentStack, parentStack);
            for (int i = 0; i < currentStack.length - duplicates; i++) {
                outInfoString("\tat " + currentStack[i] + "\t, Index:"
                        + getClassIndex(currentStack[i]));
            }
            if (duplicates > 0) {
                outInfoString("\t... " + duplicates + " more");
            }
            parentStack = currentStack;
            throwable = throwable.getCause();
        }
        long endIndex = -1;

        StackTraceElement firstElement = null;
        if (e.getStackTrace().length > 0) {
            firstElement = e.getStackTrace()[0];
        }
        outIndexString(DATA_FORMAT.format(new Date(System.currentTimeMillis()))
                + "|" + e.toString() + "|" + firstElement == null ? "NULL"
                : firstElement + "|" + type + "|" + startIndex + "|" + endIndex);
    }

    public static void delLogFiles() {
        if (!ENABLE) {
            return;
        }
        getIndexLogFile().delete();
        getInfoLogFile().delete();
    }

    private static String getClassIndex(StackTraceElement element) {
        if (!ENABLE) {
            return null;
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(element.getClassName());
        } catch (ClassNotFoundException ex) {
            return "UnKnowClassIndex";
        }
        while (true) {
            Class<?> declaringClass = clazz.getDeclaringClass();
            if (declaringClass == null) {
                break;
            } else {
                clazz = declaringClass;
            }
        }
        Field indexField = null;
        try {
            indexField = clazz.getField(CLASS_INDEX_FIELD_NAME);
        } catch (Exception e) {
            return "UnKnowClassIndex";
        }
        String classIndex = null;
        try {
            classIndex = (String) indexField.get(null);
        } catch (Exception e) {
            classIndex = "UnKnowClassIndex";
        }
        return classIndex;
    }

    /**
     * Counts the number of duplicate stack frames, starting from the end of the
     * stack.
     *
     * @param currentStack a stack to compare
     * @param parentStack  a stack to compare
     * @return the number of duplicate stack frames.
     */
    private static int countDuplicates(StackTraceElement[] currentStack,
                                       StackTraceElement[] parentStack) {
        if (!ENABLE) {
            return -1;
        }
        int duplicates = 0;
        int parentIndex = parentStack.length;
        for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0; ) {
            StackTraceElement parentFrame = parentStack[parentIndex];
            if (parentFrame.equals(currentStack[i])) {
                duplicates++;
            } else {
                break;
            }
        }
        return duplicates;
    }

    private static void outIndexString(String outStr) {
        if (!ENABLE) {
            return;
        }
        try {
            // errorIndexOS.write(outStr.getBytes());
            // errorIndexOS.write("\r\n".getBytes());
            // errorIndexOS.flush();
            File index = getIndexLogFile();
            FileWriter fw = new FileWriter(index, true);
            fw.write(outStr);
            fw.write("\r\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    public static void outInfoString(String outStr) {
        if (!ENABLE) {
            return;
        }
        try {
            if (TextUtils.isEmpty(outStr)) {
                return;
            }
            File info = getInfoLogFile();
            FileWriter fw = new FileWriter(info, true);
            fw.write(outStr);
            fw.write("\r\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    /**
     * 获得概要日志文件地址
     *
     * @return 概要日志文件地址
     */
    public static final File getIndexLogFile() {
        if (!ENABLE) {
            return null;
        }
        File dirFile = new File(FilePath);
        dirFile.mkdirs();
        File indexLogFile = new File(dirFile, INDEX_NAME);
        if (!indexLogFile.exists()) {
            try {
                indexLogFile.createNewFile();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return indexLogFile;
    }

    /**
     * 获得详情日志文件地址
     *
     * @return 概要日志文件地址
     */
    public static final File getInfoLogFile() {
        if (!ENABLE) {
            return null;
        }
        File dirFile = new File(FilePath);
        dirFile.mkdirs();
        File infoLogFile = new File(dirFile, INFO_NAME);
        if (!infoLogFile.exists()) {
            try {
                infoLogFile.createNewFile();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return infoLogFile;
    }

    /**
     * 获得日志压缩文件
     *
     * @return 日志压缩文件地址
     */
    public static final File getZipFile() {
        if (!ENABLE) {
            return null;
        }
        File dirFile = new File(FilePath);
        dirFile.mkdirs();
        File indexLogFile = new File(dirFile, ZIP_FILE);
        return indexLogFile;
    }

    /**
     * @param @param context
     * @return void
     * @Title: send
     * @Description: 发送错误日志到服务器
     * @author anony_god
     * @date 2013-7-1 下午01:33:33
     * @version V1.0
     */

    public static final void post_error() {
        if (!ENABLE)
            return;
        if (getInfoLogFile().length() == 0) {
            ZwyErrorReport.init();
            // 没有错误日志
            return;
        }
//        try {
//            new Thread() {
//                public void run() {
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("error_file",
//                            getInfoLogFile().getAbsolutePath()));
//                    params.add(new BasicNameValuePair("name", ZwyOSInfo
//                            .getAppName()));
//                    params.add(new BasicNameValuePair("package",
//                            ZwyContextKeeper.getInstance().getPackageName()));
//                    params.add(new BasicNameValuePair("bundle_id", ZwyOSInfo
//                            .getImei()));
//                    params.add(new BasicNameValuePair("os_name", ZwyOSInfo
//                            .getPhoneModel()));
//                    params.add(new BasicNameValuePair("os_version", ZwyOSInfo
//                            .getPhoneVersionName()));
//                    params.add(new BasicNameValuePair("phone_type", "android"));
//                    params.add(new BasicNameValuePair("version_code", ZwyOSInfo
//                            .getVersionCode() + ""));
//                    params.add(new BasicNameValuePair("version_name", ZwyOSInfo
//                            .getVersionName()));
//                    String url = "http://106.186.123.117/" + "log/upload";
//                    String res = ZwyNet.getResponseByMultiPost(url, params);
//                    if ("{\"result_code\":200}".equals(res)) {
//                        getIndexLogFile().delete();
//                        getInfoLogFile().delete();
//                    }
//                    ZwyErrorReport.init();
//                }
//
//                ;
//            }.start();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }

        // 将文件上传服务器
    }
}
