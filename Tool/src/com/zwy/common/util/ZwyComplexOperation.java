package com.zwy.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Base64;

/**
 * base64位数据文本转化类
 *
 * @author ForLyp
 */
public class ZwyComplexOperation {

    public static String encodeBase64Img(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * 字符串转化为图片
     *
     * @param data
     * @return
     * @author ForLyp
     */
    public static Bitmap decodeBase64Img(String data) {
        byte[] result = Base64.decode(data, Base64.DEFAULT);
        return ZwyImageUtils.getBitmapFromByte(result);
    }

    /**
     * 文件转化为字符串
     *
     * @param voiceFile
     * @return
     * @author ForLyp
     * ps:同FileOption
     */
    public static String encodeBase64File(File file) {
        String resultString = null;
        try {
            ArrayList<Byte> bytes = new ArrayList<Byte>();
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
            int single;
            while ((single = fis.read()) != -1) {
                bytes.add((byte) single);
            }
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = bytes.get(i);
            }
            resultString = Base64.encodeToString(result, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 字符串转化为byte
     *
     * @param data
     * @return
     * @author ForLyp
     */
    public static byte[] decodeBase64String(String data) {
        byte[] result = Base64.decode(data, Base64.DEFAULT);
        return result;
    }
}
