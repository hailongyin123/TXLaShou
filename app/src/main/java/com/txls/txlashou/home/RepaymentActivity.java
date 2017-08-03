package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.txls.txlashou.R;

/**
 * 天下分期二期加入此界面
 * 还款页？（废除）
 */
public class RepaymentActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private TextView tv_repayment;
    private Button btn_repayment;
    private Button btn_prepayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);

    }


    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        tv_repayment = (TextView) findViewById(R.id.tv_repayment);
        btn_repayment = (Button) findViewById(R.id.btn_repayment);
        btn_prepayment = (Button) findViewById(R.id.btn_prepayment);
        //设置监听
        img_return.setOnClickListener(this);
        tv_repayment.setOnClickListener(this);
        btn_repayment.setOnClickListener(this);
        btn_prepayment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_repayment:
                Intent intentNow = new Intent(this,RepaymentSelectActivity.class);
                startActivity(intentNow);
                break;
            case R.id.btn_prepayment:
                Intent intentPrepayment = new Intent(this,PrepaymentActivity.class);
                startActivity(intentPrepayment);
                break;
            case R.id.tv_repayment:
                Intent intentDetails = new Intent(this,RepaymentNotesActivity.class);
                startActivity(intentDetails);
                break;
        }
    }
}
