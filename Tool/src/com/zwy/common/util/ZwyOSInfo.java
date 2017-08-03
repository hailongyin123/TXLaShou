package com.zwy.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.zwy.app.ZwyContextKeeper;
import com.zwy.base.ZwyLog;

/**
 * 获取设备系统信息
 */
public class ZwyOSInfo {
    static public DisplayMetrics getDisplayMetrics() {
        return ZwyContextKeeper.getInstance().getResources()
                .getDisplayMetrics();
    }
    static int screenHeight;
    static int screenWidth;
    static public int getPhoneWidth() {
        if(screenWidth <=0){
            DisplayMetrics dis = getDisplayMetrics();
            screenWidth = dis.widthPixels;
        }

        return screenWidth;
    }

    static public int getPhoneHeight() {
        if(screenHeight<=0){
            DisplayMetrics dis = getDisplayMetrics();
             screenHeight = dis.heightPixels;
        }

        return screenHeight;
    }

    static String imei = null;

    /**
     * 得到imei
     *
     * @return
     * @author ForLyp
     */
    static public String getImei() {
        if (imei == null) {
            try {
                Context context = ZwyContextKeeper.getInstance();
                TelephonyManager mTelephonyMgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                imei = mTelephonyMgr.getDeviceId();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (TextUtils.isEmpty(imei)) {
                    imei = "null_imei";
                }
            }
        }

        return imei;

    }

    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    public static String getPhoneVesion() {
        return android.os.Build.VERSION.SDK;
    }

    public static String getPhoneVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    public static String mAppName = null;

    public static String getAppName() {
        if (TextUtils.isEmpty(mAppName)) {
            PackageManager pm = ZwyContextKeeper.getInstance()
                    .getPackageManager();
            try {
                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = pm.getApplicationInfo(ZwyContextKeeper
                            .getInstance().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    applicationInfo = null;
                }
                mAppName = (String) pm.getApplicationLabel(applicationInfo);
            } catch (Exception e) {
                mAppName = "";
            }
        }
        return mAppName;
    }

    private static int mVerCode;

    /**
     * @param context
     * @return ：
     * @methods name: getVersionCode
     * @Descripition : 程序当前版本号
     * @date ：2012-7-23 下午04:50:16
     * @author ：wuxu
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public static int getVersionCode() {
        if (mVerCode == 0) {
            PackageManager pm = ZwyContextKeeper.getInstance()
                    .getPackageManager();
            PackageInfo pkInfo = null;
            try {
                pkInfo = pm.getPackageInfo(ZwyContextKeeper.getInstance()
                                .getPackageName(),
                        PackageManager.GET_UNINSTALLED_PACKAGES);
            } catch (NameNotFoundException e) {
            }
            if (pkInfo == null) {
                return 0;
            }

            mVerCode = pkInfo.versionCode;
        }
        return mVerCode;
    }

    private static String mVerName = null;

    public static String getVersionName() {
        if (mVerName == null) {
            PackageManager pm = ZwyContextKeeper.getInstance()
                    .getPackageManager();
            PackageInfo pkInfo = null;
            try {
                pkInfo = pm.getPackageInfo(ZwyContextKeeper.getInstance()
                                .getPackageName(),
                        PackageManager.GET_UNINSTALLED_PACKAGES);
            } catch (NameNotFoundException e) {
            }
            if (pkInfo == null) {
                return null;
            }

            mVerName = pkInfo.versionName;
        }
        return mVerName;
    }

    public static String sign;

    public static Signature[] getSignature() {

        PackageManager pm = ZwyContextKeeper.getInstance().getPackageManager();
        PackageInfo packageinfo;
        try {
            packageinfo = pm.getPackageInfo(ZwyContextKeeper.getInstance()
                    .getPackageName(), PackageManager.GET_SIGNATURES);
            return packageinfo.signatures;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            ZwyLog.i("test", "pubKey = " + pubKey + " signNumber = "
                    + signNumber);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    public static String getMd5Sign() {
        if (!TextUtils.isEmpty(sign)) {
            return sign;
        }
        Signature[] signs = getSignature();
        if ((signs == null) || (signs.length == 0)) {
            return null;
        } else {

            Signature sign = signs[0];
            String signMd5 = "signs";
            return "";
        }
    }

        public static final String KEYSTRING_USER_AGENT = "user_agent_key";

        public static String getUAFromProperties()
        {
            try {
                FileInputStream is = getPropertyStream();
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                byte buf[] = new byte[1024];
                for(int k = 0; -1 != (k = is.read(buf));)
                    bytearrayoutputstream.write(buf, 0, k);

                String fileString = new String(bytearrayoutputstream.toByteArray(), "UTF-8");

                return getProperties(KEYSTRING_USER_AGENT, fileString);

                //System.out.println("IS FILE Android Platform  " + bytearrayoutputstream.size() +  "  "+());

            } catch (Exception e) {
                // TODO: handle exception

                System.out.println("IS FILE erororo");
                e.printStackTrace();
            }
            return null;
        }



        public static FileInputStream getPropertyStream()
        {
            try {

                File property = new java.io.File("/opl/etc/properties.xml");
                if(property.exists())
                {
                    return new FileInputStream(new java.io.File("/opl/etc/properties.xml"));
                }
                else
                {
                    property = new java.io.File("/opl/etc/product_properties.xml");
                    if(property.exists())
                    {
                        return new FileInputStream(new java.io.File("/opl/etc/product_properties.xml"));
                    }
                    else
                    {
                        return null;
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }


        public static String getProperties(String key, String content)
        {
            String STARTKEY = "<"+key+">";
            String ENDKEY = "</"+key+">";
            content = content.replace("\r", "");
            content = content.replace("\n", "");

            int startIndex = content.indexOf(STARTKEY) + STARTKEY.length();
            int endIndex = content.indexOf(ENDKEY);
            if(startIndex > -1 && endIndex > -1)
            {
                return content.substring(startIndex, endIndex);
            }
            else
                return null;
        }

}
