package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
 * 设置支付密码页
 */
public class PayPasswordActivity extends SuperActivity {
    private ImageView img_return;
    private EditText payPwd;
    private EditText payPwdAgin;
    private Button btn_pay_pwd;
    private UserBean userBean;
    private String pwdStr;
    private String pwdStrAgin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        userBean = (UserBean) getIntent().getSerializableExtra("userData");
    }

    @Override
    public void initView() {
        btn_pay_pwd = (Button) findViewById(R.id.btn_pay_pwd);
        img_return = (ImageView) findViewById(R.id.img_return);
        payPwd = (EditText) findViewById(R.id.pay_pwd);
        payPwdAgin = (EditText) findViewById(R.id.pay_pwd_agin);
        //监听
        btn_pay_pwd.setOnClickListener(this);
        img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pay_pwd:
                if (Utils.isOnClickable()) {
                    pwdStr = payPwd.getEditableText().toString();
                    pwdStrAgin = payPwdAgin.getEditableText().toString();
                    setPayPwd(pwdStr);
                }
                break;
            case R.id.img_return:
               finish();
                break;
        }

    }
    //设置支付密码
    private void setPayPwd(String pwdStr){
        if (TextUtils.isEmpty(pwdStr) && TextUtils.isEmpty(pwdStrAgin)) {
            ZwyCommon.showToast(PayPasswordActivity.this,"支付密码不能为空");
            return;
        }
        if (pwdStr.length() < 6) {
            ZwyCommon.showToast(PayPasswordActivity.this,"支付密码为6位数字");
            return;
        }
        if (!ZwySystemUtil.checkPayPwd(pwdStr)){
            ZwyCommon.showToast(PayPasswordActivity.this,"支付密码格式不正确");
            return;
        }
        if (!pwdStr.equals(pwdStrAgin)) {
            ZwyCommon.showToast(PayPasswordActivity.this,"两次密码输入不一致");
            return;
        }
        String token = UserDataManager.getToken(this);
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("payPw", pwdStr);
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/userSetPayPw.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(PayPasswordActivity.this,"支付密码设置失败，检查网络");
                        return;
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("设置支付密码返回的参数=====",response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response,UserBean.class);
                        if (userBean.getCode().equals("2000")){
                            ZwyCommon.showToast(PayPasswordActivity.this,"支付密码设置成功");
                            Intent intentMain = new Intent(PayPasswordActivity.this,MainActivity.class);
                            intentMain.putExtra("userData",userBean);
                            startActivity(intentMain);
                        }else{
                            ZwyCommon.showToast(PayPasswordActivity.this,userBean.getMessage());
                            return;
                        }
                        if(userBean.getCode().equals("UR0006")){
                            UserDataManager.clear(PayPasswordActivity.this);
                            ZwyCommon.showToast(PayPasswordActivity.this,userBean.getMessage()+",请重新登录");
                            Intent intent = new Intent(PayPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
