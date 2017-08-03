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
import com.txls.txlashou.bean.BillListBean;
import com.txls.txlashou.bean.UserBean;
import com.txls.txlashou.data.UserDataManager;
import com.txls.txlashou.net.ZwyDefine;
import com.txls.txlashou.util.MD5Utils;
import com.txls.txlashou.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zwy.base.ZwyCommon;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 输入支付密码页
 */
public class InputPasswordActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private Button submit_input_password;
    private EditText et_input_password;
    private BillListBean.ResponseBean.RepayPlanBean repay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        repay = (BillListBean.ResponseBean.RepayPlanBean)this.getIntent().getSerializableExtra("repay");
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        submit_input_password = (Button) findViewById(R.id.submit_input_password);
        et_input_password = (EditText) findViewById(R.id.et_input_password);
        //监听
        img_return.setOnClickListener(this);
        submit_input_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.submit_input_password:
                if(Utils.isOnClickable()){
                    //提交支付密码
                    inPutPwd();
                }
                break;
        }

    }
    //支付密码调用
    private void inPutPwd() {
        String payPwd = et_input_password.getEditableText().toString();
        if (TextUtils.isEmpty(payPwd)) {
            ZwyCommon.showToast(InputPasswordActivity.this,"支付密码不能为空");
            return;
        }
        if (payPwd.length() != 6) {
            ZwyCommon.showToast(InputPasswordActivity.this,"密码为6位数字");
            return;
        }
        //输入支付密码
        Map<String, String> param = new HashMap<String, String>();
        HashMap<String, String> maps = new HashMap<String, String>();
        maps.put("token", UserDataManager.getToken(this));
        maps.put("repayType", "1");
        maps.put("repayAmount",String.valueOf(repay.getIssueRepayAmount()));
        maps.put("payPw",payPwd);
        String newString = MD5Utils.getSignStr(maps);
        param.put("message", newString);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/confirmAdvanceRepayPlan.action"))
                .params(param).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(InputPasswordActivity.this,"支付失败，请检查网络~");
                        return;
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("输入支付密码返回的数据=====  ",response.toString());
                        UserBean userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response,UserBean.class);
                        if (userBean.getCode().equals("2000")){
                            Intent intent = new Intent(InputPasswordActivity.this,PayFinallyActivity.class);
                            intent.putExtra("payMoney",userBean);
                            startActivity(intent);
                        }else{
                            ZwyCommon.showToast(InputPasswordActivity.this,userBean.getMessage());
                            return;
                        }
                        if (userBean.getCode().equals("UR0006")){
                            UserDataManager.clear(InputPasswordActivity.this);
                            ZwyCommon.showToast(InputPasswordActivity.this, userBean.getMessage() + ",请重新登录");
                            Intent intent = new Intent(InputPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

}
