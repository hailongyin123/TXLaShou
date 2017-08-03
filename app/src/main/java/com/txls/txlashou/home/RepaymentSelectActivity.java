package com.txls.txlashou.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.txls.txlashou.R;
import com.txls.txlashou.adapter.RepaymentNowAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择还款？
 */
public class RepaymentSelectActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private ListView lv_repayment;
    public Button btn_repayment;
    private RepaymentNowAdapter repaymentNowAdapter;
    List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_select);
        repaymentNowAdapter = new RepaymentNowAdapter(this,getData());
        lv_repayment.setAdapter(repaymentNowAdapter);


    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        lv_repayment = (ListView) findViewById(R.id.lv_repayment);
        btn_repayment = (Button) findViewById(R.id.btn_repayment);
        //监听
        img_return.setOnClickListener(this);
        btn_repayment.setOnClickListener(this);

    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "这是第1行测试数据");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "这是第2行测试数据");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "这是第3行测试数据");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "这是第4行测试数据");
        list.add(map);
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_repayment:
                Intent intentDetail = new Intent(this,RepaymentNowActivity.class);
                startActivity(intentDetail);
                break;

        }
    }
}
