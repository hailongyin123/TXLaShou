package com.txls.txlashou.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.txls.txlashou.R;
import com.txls.txlashou.adapter.TimeLineAdapter;
import com.txls.txlashou.bean.BillListBean;
import com.zwy.base.ZwyCommon;

import java.util.List;

/**
 *账单页未出账单类（时间轴）
 */
public class ListTimeLineActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private ListView listView;
    private TextView all_list_money;
    private TextView last_repayment_date;
    private List<BillListBean.ResponseBean.RepayPlanBean> data ;
    private BillListBean bill;
    private TimeLineAdapter timeLineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_time_line);
        bill = (BillListBean)getIntent().getSerializableExtra("willData");
        if( bill!=null && bill.getResponse()!=null){
            all_list_money.setText("待还款总金额"+bill.getResponse().getRepayTotalAmount()+"元");
            if(bill.getResponse().getRepayFinalTime()!=null&&!"".equals(bill.getResponse().getRepayFinalTime())){
                last_repayment_date.setText("最后还款日期  "+bill.getResponse().getRepayFinalTime());
            }
        }
        listView.setDividerHeight(0);
        if( getData() != null){
            timeLineAdapter = new TimeLineAdapter(this, getData());

            listView.setAdapter(timeLineAdapter);
        }
    }
    @Nullable
    private List<BillListBean.ResponseBean.RepayPlanBean> getData() {
        data = bill.getResponse().getRepayPlan();
       if(data != null){
           if (data.size() == 0){
               ZwyCommon.showToast(this,"未出账单数据为空");
               return data;
           }
       } else {
           ZwyCommon.showToast(this,"未出账单数据为空");
           return data;
       }
        return data;
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        listView = (ListView) findViewById(R.id.time_line_listview);
        all_list_money = (TextView) findViewById(R.id.all_list_money);
        last_repayment_date = (TextView) findViewById(R.id.last_repayment_date);
        //监听
        img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;

        }

    }
}
