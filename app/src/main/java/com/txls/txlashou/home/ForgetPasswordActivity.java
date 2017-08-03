package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.txls.txlashou.R;
import com.zwy.base.ZwyCommon;
import com.zwy.common.util.ZwySystemUtil;

public class ForgetPasswordActivity extends SuperActivity {
    //声明控件
    private Button btn_forget_pwd;
    private ImageView img_return;
    private EditText find_phone_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

    }

    @Override
    public void initView() {
        btn_forget_pwd = (Button) findViewById(R.id.btn_forget_pwd);
        img_return = (ImageView) findViewById(R.id.img_return);
        find_phone_edit = (EditText) findViewById(R.id.find_phone_edit);
        //设置监听
        btn_forget_pwd.setOnClickListener(this);
        img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_pwd:
                //传手机号
                String phoneStr = find_phone_edit.getEditableText().toString();
                if (TextUtils.isEmpty(phoneStr)) {
                    ZwyCommon.showToast(this,"请输入手机账号");
                    return;
                }
                if (!ZwySystemUtil.isMobileNO(phoneStr)) {
                    ZwyCommon.showToast(this,"请输入正确的手机号");
                    return;
                }else{
                    Intent intentFind = new Intent(this,FindPasswordActivity.class);
                    intentFind.putExtra("phoneStr",phoneStr);
                    startActivity(intentFind);
                }
                break;
            case R.id.img_return:
                finish();
                break;
        }

    }


}
