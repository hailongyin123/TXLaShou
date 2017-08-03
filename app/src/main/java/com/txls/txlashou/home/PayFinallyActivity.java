package com.txls.txlashou.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.txls.txlashou.R;
import com.txls.txlashou.bean.UserBean;
import com.zwy.base.ZwyCommon;

/**
 * 分期支付结果页面
 */
public class PayFinallyActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private TextView repay_money;
    private Button btn_repayment_finally;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_finally);
        UserBean userBean = (UserBean) getIntent().getSerializableExtra("payMoney");
        repay_money.setText("成功还款"+userBean.getResponse().getRepayAmount()+"元");
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_repayment_finally = (Button) findViewById(R.id.btn_repayment_finally);
        repay_money = (TextView) findViewById(R.id.repay_money);
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
