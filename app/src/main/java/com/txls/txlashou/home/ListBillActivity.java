package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.txls.txlashou.R;
import com.txls.txlashou.adapter.ListBillAdapter;
import com.txls.txlashou.bean.BillListBean;
import com.txls.txlashou.bean.NotesBean;
import com.txls.txlashou.data.UserDataManager;
import com.txls.txlashou.net.ZwyDefine;
import com.txls.txlashou.util.MD5Utils;
import com.txls.txlashou.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zwy.base.ZwyCommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 账单页面（已出账单）
 */
public class ListBillActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private ListView listView;
    private TextView bill_notes;
    private TextView tv_will_bill;
    private TextView all_list_money;
    //集合对象
    private List<BillListBean.ResponseBean.RepayPlanBean> data ;
    private BillListBean.ResponseBean.RepayPlanBean detailData;
    private BillListBean bill;
    private ListBillAdapter listBillAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        //获取传值
        bill = (BillListBean) this.getIntent().getSerializableExtra("billData");
        if( bill!=null && bill.getResponse()!=null){
            all_list_money.setText("待还款总金额"+bill.getResponse().getRepayTotalAmount()+"元");
        }
        if(getData() != null){
            listBillAdapter = new ListBillAdapter(this, getData());
            //去掉ListView分割线
            listView.setDividerHeight(0);

//            //ListView的item监听(暂时去掉)
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                    if (data.get(position).getRepayStatus() == 1){
//                        showToast("账单已还清");
//                        return;
//                    }else if (data.get(position).getClickStatus() == 1){
//                        detailData = data.get(position);
//                        Intent intent = new Intent(ListBillActivity.this,RepaymentDetailsActivity.class);
//                        intent.putExtra("detailData", detailData);
//                        cancelToast();
//                        startActivity(intent);
//                    }else {
//                        showToast("请还距离时间最久的账单");
//                    }
//                }
//            });
            listView.setAdapter(listBillAdapter);
        }

    }
    //获取集合数据
    private List<BillListBean.ResponseBean.RepayPlanBean> getData() {
        data = bill.getResponse().getRepayPlan();
        if(data != null){
            if (data.size() == 0){
                ZwyCommon.showToast(this,"账单数据为空");
                return data;
            }
        } else {
            ZwyCommon.showToast(this,"账单数据为空");
            return data;
        }
        return data;
    }
    //Toast多显问题
    private Toast mToast;
    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(ListBillActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void onBackPressed() {
        cancelToast();
        super.onBackPressed();
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        listView = (ListView) findViewById(R.id.bill_listView);
        bill_notes = (TextView) findViewById(R.id.bill_notes);
        all_list_money = (TextView) findViewById(R.id.all_list_money);
        tv_will_bill = (TextView) findViewById(R.id.tv_will_bill);
        //监听
        img_return.setOnClickListener(this);
        bill_notes.setOnClickListener(this);
        tv_will_bill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                cancelToast();
                break;
            case R.id.bill_notes:
                //还款记录
                if(Utils.isOnClickable()){
                    getNote();
                }
                break;
            case R.id.tv_will_bill:
                if (Utils.isOnClickable()){
                    getBill();
                }
                break;
        }

    }
    //未出账单查询
    private void getBill() {
        Map<String, String> params = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", UserDataManager.getToken(this));
        map.put("flag","2");
        String newStr = MD5Utils.getSignStr(map);
        params.put("message", newStr);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/queryRepayPlan.action"))
                .params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(ListBillActivity.this,"获取数据失败，检查网络");
                        return;
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("未出账单返回的信息数据=====  ",response.toString());
                        BillListBean billListBean = BillListBean.getInstance();
                        Gson gson = new Gson();
                        billListBean = gson.fromJson(response,BillListBean.class);
                        if(billListBean.getCode().equals("2000")){
                            Intent intent = new Intent(ListBillActivity.this,ListTimeLineActivity.class);
                            intent.putExtra("willData",billListBean);
                            startActivity(intent);
                        }else {
                            ZwyCommon.showToast(ListBillActivity.this,billListBean.getMessage());
                            return;
                        }
                        if (billListBean.getCode().equals("UR0006")){
                            UserDataManager.clear(ListBillActivity.this);
                            ZwyCommon.showToast(ListBillActivity.this,"用户未登录或超时,请重新登录");
                            Intent intent = new Intent(ListBillActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    //还款记录查询方法
    private void getNote() {
        Map<String, String> param = new HashMap<String, String>();
        HashMap<String, String> maps = new HashMap<String, String>();
        maps.put("token", UserDataManager.getToken(this));
        String newString = MD5Utils.getSignStr(maps);
        param.put("message", newString);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/userRepayRecord.action"))
                .params(param).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(ListBillActivity.this,"获取数据失败，检查网络");
                        return;
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("查询还款记录返回的信息数据=====  ",response.toString());
                        NotesBean notesBean = new NotesBean();
                        Gson gson = new Gson();
                        notesBean = gson.fromJson(response,NotesBean.class);
                        if(notesBean.getCode().equals("2000")){
                            Intent intent = new Intent(ListBillActivity.this,RepaymentNotesActivity.class);
                            intent.putExtra("notesData",notesBean);
                            startActivity(intent);
                        }else {
                            ZwyCommon.showToast(ListBillActivity.this,notesBean.getMessage());
                            return;
                        }
                        if (notesBean.getCode().equals("UR0006")){
                            ZwyCommon.showToast(ListBillActivity.this,notesBean.getCode()+",请重新登录");
                            UserDataManager.clear(ListBillActivity.this);
                            Intent intent = new Intent(ListBillActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
