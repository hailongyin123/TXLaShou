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
 * 实名认证页面
 */
public class IDCardActivity extends SuperActivity {
    //声明控件
    private Button btnConfirm;
    private ImageView imgReturn;
    private EditText id_card_number;
    private EditText id_card_name;
    private String idName;
    private String idNumber;
    private UserBean userBean = UserBean.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        userBean = (UserBean) getIntent().getSerializableExtra("userData");
    }


    @Override
    public void initView() {
        btnConfirm = (Button) findViewById(R.id.btn_id_card);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        id_card_number = (EditText) findViewById(R.id.id_card_number);
        id_card_name = (EditText) findViewById(R.id.id_card_name);
        //监听
        btnConfirm.setOnClickListener(this);
        imgReturn.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_id_card:
                if (Utils.isOnClickable()){
                    submit();
                }
                break;
            case R.id.img_return:
                finish();
                break;
        }
    }
    //实名认证方法
    private void submit() {
        idName = id_card_name.getEditableText().toString();
        idNumber = id_card_number.getEditableText().toString();
        if (TextUtils.isEmpty(idName) && TextUtils.isEmpty(idNumber) ) {
            ZwyCommon.showToast(this,"姓名身份证不能为空");
            return;
        }
        if (!ZwySystemUtil.checkIDCard(idNumber)){
            ZwyCommon.showToast(this,"身份证号码格式不正确");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        String token = UserDataManager.getToken(this);
        Log.e("注册存取的token======",token);
        map.put("token", token);
        map.put("realName", idName);
        map.put("idNo", idNumber);
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/userAuth.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(IDCardActivity.this,"请求失败，检查网络");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("身份认证返回的参数=====",response.toString());
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response,UserBean.class);
                        if (userBean.getCode().equals("2000")){
                            ZwyCommon.showToast(IDCardActivity.this,"认证成功，请设置支付密码");
                            UserDataManager.putAuthSign(IDCardActivity.this,"1");
                            //UserDataManager.putOverdueStatus(IDCardActivity.this,userBean.getResponse().getOverdueStatus());
                           // UserDataManager.putLoanMoney(IDCardActivity.this,userBean.getResponse().getLoanAmount());
                            // UserDataManager.putIssueMoney(IDCardActivity.this,userBean.getResponse().getIssueRepayAmount());
                            Intent intentPayPwd = new Intent(IDCardActivity.this,PayPasswordActivity.class);
                            intentPayPwd.putExtra("userData",userBean);
                            startActivity(intentPayPwd);
                        }else{
                            ZwyCommon.showToast(IDCardActivity.this,userBean.getMessage());
                        }
                        if (userBean.getCode().equals("UR0006")){
                            ZwyCommon.showToast(IDCardActivity.this,userBean.getMessage());
                            UserDataManager.clear(IDCardActivity.this);
                            Intent intentMain = new Intent(IDCardActivity.this,LoginActivity.class);
                            startActivity(intentMain);
                        }
                    }
                });
    }

}
