package com.txls.txlashou.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.txls.txlashou.R;

public class MessageDetailsActivity extends SuperActivity {
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

    }

    @Override
    public void initView() {
        //获取控件
        imgReturn = (ImageView) findViewById(R.id.img_return);

        //设置点击事件
        imgReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
        }
    }
}
