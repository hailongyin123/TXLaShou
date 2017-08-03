package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.txls.txlashou.R;

/**
 * 提前还款？（废除）
 */
public class PrepaymentTwoActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private Button btn_prepayment_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepayment_two);

    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_prepayment_two = (Button) findViewById(R.id.btn_prepayment_two);
        //监听
        img_return.setOnClickListener(this);
        btn_prepayment_two.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_prepayment_two:
                Intent intentInput = new Intent(this,PayFinallyActivity.class);
                startActivity(intentInput);
                break;
        }

    }
}
