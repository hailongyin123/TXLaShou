package com.zwy.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zwy.app.ZwyContextKeeper;

/**
 * 这是..提示框？...Toast？...获取渠道标识？...隐藏软键盘？？
 */
public class ZwyCommon {
    static String mSrc = null;
    private static SimpleDateFormat day_format;

    static public void showToast(String content) {
        Toast.makeText(ZwyContextKeeper.getInstance(), content,
                Toast.LENGTH_SHORT).show();
    }
    static public void showToast(int content) {
        Toast.makeText(ZwyContextKeeper.getInstance(), content,
                Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context, String message) {
        try {
            if (null == context) {
                return;
            }
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 200);
            toast.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    static public void showSimpleDialog(Context context, String title,
                                        String content, DialogInterface.OnClickListener aCallBack) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(content)
                .setPositiveButton("确定", aCallBack).create();
        if(!(context instanceof Activity)){
            dialog.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    static public void showTwoBtnDialog(Context context, String title,
                                        String content, DialogInterface.OnClickListener aCallBack1,
                                        DialogInterface.OnClickListener aCallBack2) {

        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(content)
                .setPositiveButton("确定", aCallBack1)
                .setNegativeButton("取消", aCallBack2)
                .create();
        if(!(context instanceof Activity)){
            dialog.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    static public void showTwoBtnDialog(Context context, String title,
                                        String content, String button1, DialogInterface.OnClickListener aCallBack1, String button2,
                                        DialogInterface.OnClickListener aCallBack2) {

        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(content)
                .setPositiveButton(button1, aCallBack1)
                .setNegativeButton(button2, aCallBack2)
                .create();
        if(!(context instanceof Activity)){
            dialog.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    /**
     * 得到渠道标识
     *
     * @param context
     * @return
     * @author ForLyp
     */
    public static final String getSrc(Context context) {
        if (mSrc == null) {
            AssetManager assetManager = context.getAssets();
            String[] files = null;
            try {
                files = assetManager.list("");
            } catch (IOException e) {

            }

            if (files != null) {
                for (String file : files) {
                    if (file.contains("config.txt")) {
                        InputStream inputStream = null;
                        try {
                            inputStream = assetManager.open(file);

                            BufferedReader d = new BufferedReader(
                                    new InputStreamReader(inputStream));

                            mSrc = d.readLine().trim();

                            d.close();
                            inputStream.close();
                        } catch (IOException e) {

                        }
                        break;
                    }
                }
            }
        }
        return mSrc;
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @author ForLyp
     */
    public static void hideInputMethod(Activity context) {
        try {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            context.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }

    }

}
