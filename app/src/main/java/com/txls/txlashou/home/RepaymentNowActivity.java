package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.txls.txlashou.R;

/**
 * 立即还款？（没了？）
 */
public class RepaymentNowActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private Button btn_repayment_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_now);

    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_repayment_now = (Button) findViewById(R.id.btn_repayment_now);
        //监听
        img_return.setOnClickListener(this);
        btn_repayment_now.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_repayment_now:
                Intent intentPay = new Intent(this,InputPasswordActivity.class);
                startActivity(intentPay);
                break;
        }

    }
}
