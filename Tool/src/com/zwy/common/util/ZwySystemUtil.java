package com.zwy.common.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;

import com.zwy.app.ZwyContextKeeper;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZwySystemUtil {
    /**
     *
     * @param pwd
     * @return
     * 密码的正则
     */
    public static boolean checkPwd(String pwd){
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }
    public static boolean checkIDCard(String pwd){
        Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }


    /**
     * 验证码正则
     * @param code
     * @return
     */
    public static boolean checkPayPwd(String code){
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(code);
        return m.matches();
    }
    /**
     * 判断包名是否存在
     *
     * @param pname
     * @return
     * @author ForLyp
     */
    public static boolean isExist(String pname) {
        try {
            ZwyContextKeeper.getInstance().getPackageManager()
                    .getApplicationInfo(pname, 0);
            return true;
        } catch (Exception e) {
            // ErrorReport.outException(e);
            return false;
        }

    }

    public static int getApkVersion(String apk_path) {
        try {
            PackageManager pm = ZwyContextKeeper.getInstance()
                    .getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apk_path,
                    PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return info.versionCode;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        float scale = ZwyContextKeeper.getInstance().getResources()
                .getDisplayMetrics().density;
        // if (isX)
        // scale = context.getResources().getDisplayMetrics().xdpi/;
        // else
        // scale = context.getResources().getDisplayMetrics().ydpi;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        float scale = ZwyContextKeeper.getInstance().getResources()
                .getDisplayMetrics().density;
        // if (isX)
        // scale = context.getResources().getDisplayMetrics().xdpi;
        // else
        // scale = context.getResources().getDisplayMetrics().ydpi;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sim卡是否可用
     *
     * @return
     * @author ForLyp
     */
    public static boolean simIsOk() {
        boolean flag = true;
        TelephonyManager telephonyManager = (TelephonyManager) ZwyContextKeeper
                .getInstance().getSystemService(
                        ZwyContextKeeper.getInstance().TELEPHONY_SERVICE);
        int status = telephonyManager.getSimState();
        if ((status == TelephonyManager.SIM_STATE_UNKNOWN)
                || (status == TelephonyManager.SIM_STATE_ABSENT)) {
            flag = false;
        } else {

        }
        return flag;
    }

    /**
     * 根据时间得到距离现有时间多少秒之前之类的
     *
     * @param timeStr
     * @return
     * @author ForLyp
     */
    public static String getStandardDate(String timeStr) {
        long t = System.currentTimeMillis();
        // MM/dd/yyyy hh:mm:ss.fff
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            Date time = dateFormat.parse(timeStr);
            t = time.getTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getStandardDate(t);
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     *
     * @return
     */
    public static String getStandardDate(long t) {

        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - (t);
        long mill = (long) Math.ceil(time / 1000);// 秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 换算时间 1000 换算为1s
     *
     * @param aTs 毫秒值
     * @return
     * @author ForLyp
     */
    static public String getFitableTime(long aTs) {
        SimpleDateFormat df1 = new SimpleDateFormat("ss秒");
        SimpleDateFormat df2 = new SimpleDateFormat("mm分ss秒");
        SimpleDateFormat df3 = new SimpleDateFormat("hh时mm分ss秒");
        final int second = 1000;
        final int minite = 1000 * 60;
        final int hour = 1000 * 60 * 60;

        long val = aTs;
        if (val >= second && val < minite) {
            return df1.format(val);
        }
        if (val >= minite && val < hour) {
            return df2.format(val);
        }
        if (val >= hour) {
            return df3.format(val);
        }
        return "<1s";
    }

    /**
     * 格式化单位
     *
     * @param aSize
     * @return
     * @author ForLyp
     */
    static public String getFitableSize(long aSize) {
        final int base = 1024;
        float val = aSize;
        DecimalFormat df = new DecimalFormat("###.0");
        if (val >= base && val < base * base) {
            return df.format(val / base) + "KB";
        }
        if (aSize > base * base) {
            return df.format(val / (base * base)) + "MB";
        }
        return df.format(val) + "B";
    }

    /**
     * 是否是电话号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    /**
     * 邮箱验证
     *
     * @param email
     * @return
     * @author ForLyp
     */
    public static boolean checkEmail(String email) {// 验证邮箱的正则表达式
        String format = "\\w{2,15}[@][a-z0-9]{2,}[.]\\p{Lower}{2,}";
        // p{Alpha}:内容是必选的，和字母字符[\p{Lower}\p{Upper}]等价。如：200896@163.com不是合法的。
        // w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的。
        // [a-z0-9]{3,}：至少三个[a-z0-9]字符,[]内的是必选的；如：dyh200896@16.com是不合法的。
        // [.]:'.'号时必选的； 如：dyh200896@163com是不合法的。
        // p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的。
        if (email.matches(format)) {
            return true;// 邮箱名合法，返回true
        } else {
            return false;// 邮箱名不合法，返回false
        }
    }

    /**
     * 直接发短信
     *
     * @param phoneNumber
     * @param info
     * @return
     * @author ForLyp
     */
    public static boolean sendOneMsg(String phoneNumber, String info) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        if (isMobileNO(phoneNumber)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, info, null, null);
        } else {
            return false;
        }
        return true;

    }

    /**
     * 格式化时间 Mar 25 2013 10:00:00:000AM 为long
     *
     * @param time
     * @return
     * @author ForLyp
     */
    public static long formatTimeByString(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MMM dd yyyy hh:mm:ss:SSSaa", Locale.ENGLISH);
        try {
            Date date = dateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getFormatTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return dateFormat.format(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dateFormat.format(System.currentTimeMillis());
    }

}
