package com.txls.txlashou.home;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.txls.txlashou.R;
import com.txls.txlashou.data.CommonDataInfo;
import com.txls.txlashou.download.DownLoadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 下载word
 */
public class WordReadActivity extends SuperActivity {

    // 下载成功
    public static final int DOWNLOAD_ERROR = 7;
    // 下载失败
    public static final int DOWNLOAD_SUCCESS = 6;
    private ListView listV = null;
    private List<File> list = null;
    private int a[] = { 1,2, 2 };
    private ArrayList<HashMap<String, Object>> recordItem;
    private boolean isInstall = false;

    private File PresentFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listV = (ListView) this.findViewById(R.id.list);
        list_files();
    }

    private void list_files() {
        File path = android.os.Environment.getExternalStorageDirectory();
        PresentFile = path;
        File[] file = path.listFiles();
        fill(file);
    }

    @Override
    protected void onResume() {
        isInstall = false;
        isInstall();
        super.onResume();
    }
    private void fill(File[] file) {
        SimpleAdapter adapter = null;
        recordItem = new ArrayList<HashMap<String, Object>>();
        list = new ArrayList<File>();
        for (File f : file) {
            if (Invalid(f) == 2) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[2]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
            if (Invalid(f) == 1) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[1]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
            if (Invalid(f) == 0) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[0]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
        }
        adapter = new SimpleAdapter(this, recordItem, R.layout.word_item,
                new String[] { "picture", "name" }, new int[] { R.id.picture,
                R.id.text });
        listV.setAdapter(adapter);
        listV.setAdapter(adapter);
        listV.setOnItemClickListener(new Clicker());
    }

    private int Invalid(File f) {
        if (f.isDirectory()) {
            return 0;
        } else {
            String filename = f.getName().toLowerCase();
            if (filename.endsWith(".doc")) {
                return 1;
            } else if (filename.endsWith(".pdf")) {
                return 2;
            }
            return 3;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
// 下载成功
                    File file = (File) msg.obj;
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.fromFile(file),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                    break;
                case DOWNLOAD_ERROR:
// 下载失败
                    break;
                default:
                    break;
            }
        }

    };

    private class Clicker implements AdapterView.OnItemClickListener {
        private ProgressDialog mProgressDialog;

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            File file = list.get(arg2);
            PresentFile = file;
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                fill(files);
            } else {
                if (isInstall) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setClassName("cn.wps.moffice_eng",
                            "cn.wps.moffice.documentmanager.PreStartActivity");
                    Uri uri = Uri.fromFile(file);
                    System.out.println(uri);
                    intent.setData(uri);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Environment.getExternalStorageState());
                    Toast.makeText(WordReadActivity.this, "开始下载...", 0).show();
                    mProgressDialog = new ProgressDialog(WordReadActivity.this);
                    mProgressDialog
                            .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.show();

                    final File file1 = new File(
                            Environment.getExternalStorageDirectory(),
                            DownLoadUtil.getFileName("office" + ".apk"));
                    new Thread() {
                        public void run() {
                            File downloadfile = DownLoadUtil
                                    .downLoad(
                                            "http://kad.www.wps.cn/wps/download/android/kingsoftoffice_2052/moffice_2052_wpscn.apk",
                                            file1.getAbsolutePath(),
                                            mProgressDialog);
                            Message msg = Message.obtain();
                            if (downloadfile != null) {
// 下载成功,安装....
                                msg.obj = downloadfile;
                                msg.what = DOWNLOAD_SUCCESS;
                            } else {
// 提示用户下载失败.
                                msg.what = DOWNLOAD_ERROR;
                            }
                            handler.sendMessage(msg);
                            mProgressDialog.dismiss();
                        }
                    }.start();

                }
            }
        }
    }

    public void isInstall() {

        List<PackageInfo> list = getPackageManager().getInstalledPackages(
                PackageManager.GET_PERMISSIONS);

        StringBuilder stringBuilder = new StringBuilder();

        for (PackageInfo packageInfo : list) {
            stringBuilder.append("package name:" + packageInfo.packageName
                    + "\n");
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            stringBuilder.append("应用名称:"
                    + applicationInfo.loadLabel(getPackageManager()) + "\n");
            if (packageInfo.permissions != null) {

                for (PermissionInfo p : packageInfo.permissions) {
                    stringBuilder.append("权限包括:" + p.name + "\n");
                }
            }
            stringBuilder.append("\n");
            if ("cn.wps.moffice_eng".equals(packageInfo.packageName)) {
                isInstall = true;

            }
        }
        System.out.println(isInstall);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PresentFile.isDirectory()) {
                if (PresentFile.equals(android.os.Environment
                        .getExternalStorageDirectory())) {
                    this.finish();
                } else {
                    PresentFile = PresentFile.getParentFile();
                    File file = PresentFile;
                    File[] files = file.listFiles();
                    fill(files);
                }
            }
            if (PresentFile.isFile()) {
                if (PresentFile.getParentFile().isDirectory()) {
                    PresentFile = PresentFile.getParentFile();
                    File file = PresentFile;
                    File[] files = file.listFiles();
                    fill(files);
                }
            }
        }
        return false;
    }


    @Override
    public void initView() {

    }



    @Override
    public void onClick(View v) {

    }
}
