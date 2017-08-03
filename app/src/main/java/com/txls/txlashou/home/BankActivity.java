package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.txls.txlashou.R;

/**
 * 绑银行卡页？（废除）
 *
 */
public class BankActivity extends SuperActivity {
    private ImageView imgReturn;
    private Button btn_bank_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        imgReturn.setOnClickListener(this);
        btn_bank_card.setOnClickListener(this);
    }

    @Override
    public void initView() {
        imgReturn = (ImageView) findViewById(R.id.img_return);
        btn_bank_card = (Button) findViewById(R.id.btn_bank_card);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_bank_card:
                Intent intentPay  = new Intent(this,PayPasswordActivity.class);
                startActivity(intentPay);
                break;
        }

    }

}
