package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.txls.txlashou.R;
import com.zwy.base.ZwyCommon;

/**
 * 提前预约还清结果页面
 */
public class PrePayFinallyActivity extends SuperActivity {
    private ImageView img_return;
    private TextView prepay_money;
    private Button btn_repayment_finally;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_pay_finally);
        prepay_money.setText("*您预约的"+getIntent().getStringExtra("time")+"提前还款服务已预约成功，还款金额为"+getIntent().getStringExtra("money")+",请确保已绑定的银行卡余额充足。提前还款预约成功后将不可取消。");
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_repayment_finally = (Button) findViewById(R.id.btn_repayment_finally);
        prepay_money = (TextView) findViewById(R.id.prepay_money);
        //监听
        img_return.setOnClickListener(this);
        btn_repayment_finally.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                ZwyCommon.showToast(this,"请点击完成返回首页！");
                break;
            case R.id.btn_repayment_finally:
                Intent intentMain = new Intent(this,MainActivity.class);
                startActivity(intentMain);
                break;
        }

    }
}
