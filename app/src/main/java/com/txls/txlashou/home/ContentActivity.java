package com.txls.txlashou.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.ReaderView;
import com.google.gson.Gson;
import com.txls.txlashou.R;
import com.txls.txlashou.bean.UserBean;
import com.txls.txlashou.data.UserDataManager;
import com.txls.txlashou.net.ZwyDefine;
import com.txls.txlashou.util.MD5Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zwy.base.ZwyCommon;
import com.zwy.common.util.ZwyFileOption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Decoder.BASE64Decoder;
import okhttp3.Call;

/**
 * 合同展示界面（pdf）
 */
public class ContentActivity extends SuperActivity {
    private ImageView img_return;
    private Button load_content;
    private UserBean userBean;
    private LinearLayout linearLayout;
    private Context mContext;
    private ReaderView mDocView;
    private MuPDFCore mCore;
    private MuPDFPageAdapter mAdapter;
    //权限声明的变量
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mContext = this;
        linearLayout = (LinearLayout) findViewById(R.id.linear);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Storage Permissions
        // Check if we have write permission
        //Android系统6.0获取SD卡读取权限
        //获取sd卡读写权限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        //查找合同方法
        queryContractPDF();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //退出合同activity时删除合同文件夹
        ZwyFileOption.deleteFolder(Environment.getExternalStorageDirectory().getPath() + "/txlashou");
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        load_content = (Button) findViewById(R.id.load_content);

        load_content.setOnClickListener(this);
        img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.load_content:
                //queryContractPDF();
                break;
        }

    }

    String mPdfFilePath;

    //读取PDF合同
    public void readPdf() {
        mPdfFilePath = Environment.getExternalStorageDirectory().getPath() + "/txlashou/" + "content" + ".pdf";
        mDocView = new ReaderView(mContext);
        try {
            mCore = new MuPDFCore(mContext, mPdfFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCore != null && mCore.countPages() == 0) {
            mCore = null;
        }
        if (null == mCore) {
            Toast.makeText(mContext, "文件加载失败，请返回重新加载", Toast.LENGTH_LONG).show();
        }
        mAdapter = new MuPDFPageAdapter(mContext, mCore);
        linearLayout.setBackgroundColor(Color.BLACK);
        mDocView.setAdapter(mAdapter);
        linearLayout.addView(mDocView);
    }

    //获取合同的路径(旧版)
    public void queryContractPath() {
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", UserDataManager.getToken(this));
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/queryContractPath.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(ContentActivity.this, "获取数据失败，检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("合同", response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response, UserBean.class);
                        if (userBean.getCode().equals("2000")) {
                            Log.e("合同返回的的路径++", userBean.getResponse().getContractPath());
                            new GetPdf().execute();
                        } else {
                            ZwyCommon.showToast(ContentActivity.this, "用户暂无合同");
                        }
                        if (userBean.getCode().equals("UR0006")) {
                            UserDataManager.clear(ContentActivity.this);
                            ZwyCommon.showToast(ContentActivity.this, userBean.getMessage() + ",请重新登录");
                            Intent intent = new Intent(ContentActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    //获取pdf返回的字符串
    public void queryContractPDF() {
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", UserDataManager.getToken(this));
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/queryContract.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(ContentActivity.this, "获取数据失败，检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("pdf合同字符串", response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response, UserBean.class);
                        if (userBean.getCode().equals("2000") && userBean.getResponse().contrPdf != null && !"".equals(userBean.getResponse().contrPdf)) {
                            new GetPdf().execute();
                        } else {
                            ZwyCommon.showToast(ContentActivity.this, "用户暂无合同");
                        }
                        if (userBean.getCode().equals("UR0006")) {
                            UserDataManager.clear(ContentActivity.this);
                            ZwyCommon.showToast(ContentActivity.this, userBean.getMessage() + ",请重新登录");
                            Intent intent = new Intent(ContentActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 下载PDF的异步任务
     */
    class GetPdf extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String base64Str = userBean.getResponse().contrPdf;
            // 去掉前面的data:image/png;base64,
            if (base64Str.indexOf("data:image/png;base64,") != -1) {
                base64Str = base64Str.replace("data:image/png;base64,", "");
            }
            BASE64Decoder decoder = new BASE64Decoder();
            // 生成jpeg图片
            FileOutputStream out = null;
            try {
                // Base64解码
                byte[] b = decoder.decodeBuffer(base64Str);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {// 调整异常数据
                        b[i] += 256;
                    }
                }
                String fileName = Environment.getExternalStorageDirectory().getPath() + "/txlashou";
                File filepack = new File(fileName);
                if (!filepack.exists())
                    filepack.mkdirs();
                String filePath = Environment.getExternalStorageDirectory().getPath() + "/txlashou/" + "content" + ".pdf";
                File file = new File(filePath);
                if (!file.exists())
                    file.createNewFile();
                out = new FileOutputStream(filePath);
                out.write(b);
                out.flush();
            } catch (Exception e) {
                return null;
            } finally {
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;


//            URL url;
//            HttpURLConnection conn;
//            BufferedInputStream bis = null;
//            FileOutputStream fos = null;
//            Log.e("err", "pdf");
//            try {
//                String fileName = Environment.getExternalStorageDirectory().getPath() + "/txlashou";
//                File filepack = new File(fileName);
//                if (!filepack.exists())
//                    filepack.mkdirs();
//                String fileName1 = Environment.getExternalStorageDirectory().getPath() + "/txlashou/" + userBean.getResponse().getContrNbr()+".pdf";
//                File file = new File(fileName1);
//                if (!file.exists()) {
//                    filepack.delete();
//                    filepack.mkdirs();
//                    file.createNewFile();
//                    url = new URL(userBean.getResponse().getContractPath());
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setConnectTimeout(5000);
//                    bis = new BufferedInputStream(conn.getInputStream());
//                    fos = new FileOutputStream(file);
//                    //读取大文件
//                    int count;
//                    byte[] buffer = new byte[4 * 1024];
//                    while ((count = bis.read(buffer)) != -1) {
//                        fos.write(buffer, 0, count);
//                        fos.flush();
//                    }
//                    fos.flush();
//                }
//            } catch (Exception e) {
//                Log.e("err", e.toString());
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (fos != null) {
//                        fos.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (bis != null) {
//                        bis.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @Override
        protected void onPostExecute(String result) {
            readPdf();
        }
    }

}
