package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.txls.txlashou.R;
import com.txls.txlashou.bean.UserBean;
import com.txls.txlashou.data.ActivityManager;
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
 * 登录界面
 */
public class LoginActivity extends SuperActivity {
    //声明控件
    private Button register;
    private Button login;
    private TextView forgetPassword;
    private EditText phone_edit;
    private EditText password_edit;
    private UserBean userBean;
    //登陆的字段
    private String mobile;
    private String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    //双击返回键退出程序
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                UserDataManager.clear(this);
                ActivityManager.getInstance().exit();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initView() {
        register = (Button) findViewById(R.id.login_btn_register);
        login = (Button) findViewById(R.id.login_btn_login);
        forgetPassword = (TextView) findViewById(R.id.login_forget_pwd);
        phone_edit = (EditText) findViewById(R.id.login_phone_num);
        password_edit = (EditText) findViewById(R.id.login_password);
        //监听
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_register:
                if (Utils.isOnClickable()) {
                    Intent intentRegister = new Intent(this, RegisterActivity.class);
                    startActivity(intentRegister);
                }
                break;
            case R.id.login_btn_login:
                if (Utils.isOnClickable()){
                     mobile = phone_edit.getEditableText().toString();
                     userPwd = password_edit.getEditableText().toString();
                    if (TextUtils.isEmpty(mobile)) {
                        ZwyCommon.showToast(LoginActivity.this, "手机号不能为空");
                        return;
                    }
                    if (!ZwySystemUtil.isMobileNO(mobile)) {
                        ZwyCommon.showToast(LoginActivity.this, "请输入正确的手机号");
                        return;
                    }
                    if (TextUtils.isEmpty(userPwd)) {
                        ZwyCommon.showToast(LoginActivity.this, "密码不能为空");
                        return;
                    }
                    if (!ZwySystemUtil.checkPwd(userPwd)) {
                        ZwyCommon.showToast(LoginActivity.this, "请输入8-16位密码数字组合");
                        return;
                    }
                login(mobile, userPwd);
                }
                break;
            case R.id.login_forget_pwd:
                if (Utils.isOnClickable()) {
                    Intent intentForget = new Intent(this, ForgetPasswordActivity.class);
                    startActivity(intentForget);
                }
                break;
        }

    }
    /**
     * 登录方法
     */
    private void login(String mobile, final String userPwd) {
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("userPwd", userPwd);
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/userLogin.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(LoginActivity.this, "登录失败，检查网络");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response, UserBean.class);
                        if (userBean.getCode().equals("2000")) {
                            Log.e("登录返回的数据  =====", response.toString());
                            UserDataManager.putToken(LoginActivity.this, userBean.getResponse().getToken());
                            UserDataManager.putAuthSign(LoginActivity.this, String.valueOf(userBean.getResponse().getAuthSign()));
                            ZwyCommon.showToast(LoginActivity.this, "登录成功");
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("userData",userBean);
                            startActivity(intent);
                        }else{
                            ZwyCommon.showToast(LoginActivity.this, userBean.getMessage());
                        }
                    }
                });
    }

}
