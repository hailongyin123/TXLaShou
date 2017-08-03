package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txls.txlashou.R;
import com.txls.txlashou.bean.UserBean;
import com.txls.txlashou.data.UserDataManager;
import com.txls.txlashou.net.ZwyDefine;
import com.txls.txlashou.util.MD5Utils;
import com.txls.txlashou.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zwy.base.ZwyCommon;
import com.zwy.common.util.ZwySystemUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 找回密码提交页
 */
public class FindPasswordActivity extends SuperActivity {
    //控件声明
    private ImageView img_return;
    private EditText find_pwd_edit;
    private EditText find_pwd_edit_agin;
    private EditText get_phone_code;
    private TextView find_pwd_code;
    private Button btn_find_pwd;
    //找回密码的字段
    private String mobile;
    private String code;
    private String pwd;
    private String pwdAgin;
    private String checkCode;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        Intent getIntent = getIntent();
        mobile = getIntent.getStringExtra("phoneStr");
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        find_pwd_edit = (EditText) findViewById(R.id.find_pwd_edit);
        find_pwd_edit_agin = (EditText) findViewById(R.id.find_pwd_edit_agin);
        get_phone_code = (EditText) findViewById(R.id.get_phone_code);
        find_pwd_code = (TextView) findViewById(R.id.find_pwd_code);
        btn_find_pwd = (Button) findViewById(R.id.btn_find_pwd);
        //设置监听
        btn_find_pwd.setOnClickListener(this);
        img_return.setOnClickListener(this);
        find_pwd_code.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_pwd:
                if (Utils.isOnClickable()) {
                    //找回密码
                    findPwd();
                }
                break;
            case R.id.img_return:
                finish();
                break;
            case R.id.find_pwd_code:
                //发送验证码
                findPassCode();
                break;
        }

    }
    /**
     * 找回密码方法
     */
    private void findPwd() {
        code = get_phone_code.getEditableText().toString();
        if (TextUtils.isEmpty(code)) {
            ZwyCommon.showToast(this,"请输入验证码");
            return;
        }
        if (code.length() < 6) {
            ZwyCommon.showToast(this,"验证码为6位数字");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("verifyCode", code);
        map.put("userPwd", pwd);
        map.put("confirmUserPwd", pwdAgin);
        map.put("checkCode", checkCode);
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/changeUserPwd.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(FindPasswordActivity.this,"请求失败，检查网络");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("找回密码返回的参数=====",response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response,UserBean.class);
                        if (userBean.getCode().equals("2000")){
                            ZwyCommon.showToast(FindPasswordActivity.this,"密码找回成功");
                            Intent intentLogin = new Intent(FindPasswordActivity.this,LoginActivity.class);
                            startActivity(intentLogin);
                        }else{
                            ZwyCommon.showToast(FindPasswordActivity.this,userBean.getMessage());
                            return;
                        }
                       if (userBean.getCode().equals("UR0006")){
                           UserDataManager.clear(FindPasswordActivity.this);
                            ZwyCommon.showToast(FindPasswordActivity.this, userBean.getMessage() + ",请重新登录");
                            Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                    }
                });
    }
    //发送验证码
    int time = 0;
    private void findPassCode() {
        pwd = find_pwd_edit.getEditableText().toString();
        pwdAgin = find_pwd_edit_agin.getEditableText().toString();
        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(pwdAgin) ) {
            ZwyCommon.showToast(this,"密码不能为空");
            return;
        }
        if (!ZwySystemUtil.checkPwd(pwd) || !ZwySystemUtil.checkPwd(pwdAgin)){
            ZwyCommon.showToast(this,"密码格式不正确");
            return;
        }
        if (!pwd.equals(pwdAgin) ) {
            ZwyCommon.showToast(this,"两次密码输入不一致");
            return;
        }

        time = 60;
        handler.sendEmptyMessage(100);
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("businessType", "2");
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("sms/smsVerify.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(FindPasswordActivity.this,"请求失败，检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("返回的参数=====",response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response,UserBean.class);
                        if (userBean.getCode().equals("2000")){
                            ZwyCommon.showToast(FindPasswordActivity.this,"验证码已发送，请注意查收");
                            checkCode = userBean.getResponse().getCheckCode();
                        }else{
                            ZwyCommon.showToast(FindPasswordActivity.this,userBean.getMessage());
                        }

                    }
                });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == 100) {
                // 倒计时
                if (time > 0) {
                    find_pwd_code.setText(time + "s后重发");
                    find_pwd_code.setEnabled(false);
                    //延迟发送~
                    handler.sendEmptyMessageDelayed(100, 1000);
                    time--;
                } else {
                    find_pwd_code.setText("获取验证码");
                    find_pwd_code.setEnabled(true);
                    refreshButton();
                }
            }
        }
    };
    //刷新发送验证码按钮
    private void refreshButton() {
        if (TextUtils.isEmpty(mobile)) {
            find_pwd_code.setEnabled(false);
        } else {
            if (time <= 0)
                find_pwd_code.setEnabled(true);
        }
    }

}
