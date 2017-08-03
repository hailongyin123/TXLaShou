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
 * 注册页面
 */
public class RegisterActivity extends SuperActivity {
    //声明控件
    private ImageView imgReturn;
    private Button btn_register;
    private EditText phone_edit;
    private EditText code_edit;
    private EditText pwd_edit;
    private EditText pwd_agin_edit;
    private TextView send_code_button;
    private TextView register_content;
    private UserBean userBean;
    //注册的字段
    private String mobile;
    private String code;
    private String pwd;
    private String pwdAgin;
    private String checkCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userBean = (UserBean) getIntent().getSerializableExtra("userData");
    }

    @Override
    public void initView() {
        imgReturn = (ImageView) findViewById(R.id.img_return);
        phone_edit = (EditText) findViewById(R.id.reg_phone_edit);
        code_edit = (EditText) findViewById(R.id.reg_phone_code);
        pwd_edit = (EditText) findViewById(R.id.edit_password);
        pwd_agin_edit = (EditText) findViewById(R.id.edit_password_second);
        send_code_button = (TextView) findViewById(R.id.tv_get_phone_code);
        register_content = (TextView) findViewById(R.id.register_content);
        btn_register = (Button) findViewById(R.id.btn_register);
        //监听
        imgReturn.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        send_code_button.setOnClickListener(this);
        register_content.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_register:
                if (Utils.isOnClickable()){
                    register();
                }
                break;
            case R.id.tv_get_phone_code:
                sendMessageCode();
                break;
            case R.id.register_content:
                if (Utils.isOnClickable()) {
                    Intent intent = new Intent(this, WordActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
    /**
     * 注册方法
     */
    private void register() {
        //ProgressDialogManager.showWaiteDialog(this, "正在验证，请稍候...");
        mobile = phone_edit.getEditableText().toString();
        code = code_edit.getEditableText().toString();
        pwd = pwd_edit.getEditableText().toString();
        pwdAgin = pwd_agin_edit.getEditableText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ZwyCommon.showToast(this, "请输入手机账号");
            return;
        }
        if (!ZwySystemUtil.isMobileNO(mobile)) {
            ZwyCommon.showToast(this, "请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ZwyCommon.showToast(this, "请输入验证码");
            return;
        }
        if (code.length() < 6) {
            ZwyCommon.showToast(this, "请输入6位数字验证码");
            return;
        }
        if (!ZwySystemUtil.checkPayPwd(code)) {
            ZwyCommon.showToast(this, "验证码格式有误");
            return;
        }
        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(pwdAgin)) {
            ZwyCommon.showToast(this, "密码不能为空");
            return;
        }
        if (!ZwySystemUtil.checkPwd(pwd)) {
            ZwyCommon.showToast(this, "密码格式不正确");
            return;
        }
        if (!pwd.equals(pwdAgin)) {
            ZwyCommon.showToast(this, "两次密码输入不一致");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("verifyCode", code);
        map.put("checkCode", checkCode);
        //map.put("businessType", "1");
        map.put("userPwd", pwd);
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/userRegister.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(RegisterActivity.this, "请求失败，检查网络");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("注册返回的数据=====", response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response, UserBean.class);
                        if (userBean.getCode().equals("2000")) {
                            ZwyCommon.showToast(RegisterActivity.this, "注册成功，请登录");
                            if (userBean.getResponse().getAuthSign() == 0) {
                                UserDataManager.putToken(RegisterActivity.this, userBean.getResponse().getToken());
                                UserDataManager.putAuthSign(RegisterActivity.this, String.valueOf(userBean.getResponse().getAuthSign()));
                                Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                intentLogin.putExtra("userData", userBean);
                                startActivity(intentLogin);
                            }
                        }else{
                            ZwyCommon.showToast(RegisterActivity.this, userBean.getMessage());
                            return;
                        }
                    }
                });
    }
    //发送验证码
    int time = 0;
    private void sendMessageCode() {
        mobile = phone_edit.getEditableText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ZwyCommon.showToast(this, "请输入手机账号");
            return;
        }
        if (mobile.length() < 11) {
            ZwyCommon.showToast(this, "手机号码格式不正确");
            return;
        }
        if (!ZwySystemUtil.isMobileNO(mobile)) {
            ZwyCommon.showToast(this, "请输入正确的手机号");
            return;
        }
        time = 60;
        handler.sendEmptyMessage(100);
        //发送验证码
        sendPassCode();
    }

    private void sendPassCode() {
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("businessType", "1");
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("sms/smsVerify.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(RegisterActivity.this, "请求失败，检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("注册短信验证码返回的数据=====", response.toString());
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response, UserBean.class);
                        if (userBean.getCode().equals("2000")){
                            ZwyCommon.showToast(RegisterActivity.this, "验证码已发送，请注意查收");
                            checkCode = userBean.getResponse().getCheckCode();
                        }else{
                            ZwyCommon.showToast(RegisterActivity.this, userBean.getMessage());
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
                    send_code_button.setText(time + "s后重发");
                    send_code_button.setEnabled(false);
                    //延迟发送~
                    handler.sendEmptyMessageDelayed(100, 1000);
                    time--;
                } else {
                    send_code_button.setText("获取验证码");
                    send_code_button.setEnabled(true);
                    refreshButton();
                }
            }
        }
    };

    //刷新发送验证码按钮
    private void refreshButton() {
        if (TextUtils.isEmpty(mobile)) {
            send_code_button.setEnabled(false);
        } else {
            if (time <= 0)
                send_code_button.setEnabled(true);
        }
    }
}
