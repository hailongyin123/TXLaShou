package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txls.txlashou.R;
import com.txls.txlashou.bean.BillListBean;
import com.txls.txlashou.bean.UserBean;
import com.txls.txlashou.data.UserDataManager;
import com.txls.txlashou.net.ZwyDefine;
import com.txls.txlashou.util.MD5Utils;
import com.txls.txlashou.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zwy.base.ZwyCommon;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 还款详情页面
 */
public class RepaymentDetailsActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private Button btn_repayment;
    private TextView derail_num;
    private TextView detail_pay;
    private TextView detail_date;
    private TextView detail_count;
    private TextView detail_overdue;
    private TextView detail_card;
    private UserBean userBean;
    private BillListBean.ResponseBean.RepayPlanBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_details);
        data = (BillListBean.ResponseBean.RepayPlanBean) getIntent().getSerializableExtra("detailData");
        if (data.getRepayStatus() == 1) {
            derail_num.setText("已还清");
        }
        if (data.getRepayStatus() == 2) {
            derail_num.setText("未还清");
        }
        if (data.getRepayStatus() == 3) {
            derail_num.setText(" 逾期");
        }
        detail_card.setText("银行卡尾号" + data.getBankNo());
        detail_count.setText(data.getIssue() + "期");
        detail_date.setText(data.getRepayTime());
        detail_pay.setText(data.getIssueRepayAmount() + "元");
        detail_overdue.setText(data.getOverdueFee() + "元");


    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_repayment = (Button) findViewById(R.id.btn_repayment);
        derail_num = (TextView) findViewById(R.id.detail_num);
        detail_pay = (TextView) findViewById(R.id.detail_pay);
        detail_date = (TextView) findViewById(R.id.detail_date);
        detail_count = (TextView) findViewById(R.id.detail_count);
        detail_overdue = (TextView) findViewById(R.id.detail_overdue);
        detail_card = (TextView) findViewById(R.id.detail_card);
        //监听
        img_return.setOnClickListener(this);
        btn_repayment.setOnClickListener(this);
        //按要求暂时去掉还款按钮
        //btn_repayment.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_repayment:
                if (Utils.isOnClickable()) {
                    isPwd();
                }
                break;
        }

    }

    private void isPwd() {
        //判断是否设置支付密码
        Map<String, String> param = new HashMap<String, String>();
        HashMap<String, String> maps = new HashMap<String, String>();
        maps.put("token", UserDataManager.getToken(this));
        String newString = MD5Utils.getSignStr(maps);
        param.put("message", newString);
        OkHttpUtils.post().url(ZwyDefine.getUrl("user/payPassWordStatus.action"))
                .params(param).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ZwyCommon.showToast(RepaymentDetailsActivity.this, "支付失败，请检查网络~");
                        return;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("输入支付密码返回的数据=====  ", response.toString());
                        userBean = UserBean.getInstance();
                        Gson gson = new Gson();
                        userBean = gson.fromJson(response, UserBean.class);
                        if (userBean.getCode().equals("2000")) {
                            if (userBean.getResponse().getPaymentPasswordStatus() == 1){
                                Intent intent = new Intent(RepaymentDetailsActivity.this, InputPasswordActivity.class);
                                intent.putExtra("repay", data);
                                intent.putExtra("payMoney", userBean);
                                startActivity(intent);
                            }else{
                                ZwyCommon.showToast(RepaymentDetailsActivity.this, "请设置支付密码");
                                Intent intent = new Intent(RepaymentDetailsActivity.this, PayPasswordActivity.class);
                                startActivity(intent);
                            }
                        }else {
                            ZwyCommon.showToast(RepaymentDetailsActivity.this, userBean.getMessage() );
                        }
                        if (userBean.getCode().equals("UR0006")) {
                            UserDataManager.clear(RepaymentDetailsActivity.this);
                            ZwyCommon.showToast(RepaymentDetailsActivity.this, userBean.getMessage() + ",请重新登录");
                            Intent intent = new Intent(RepaymentDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                });
    }
}
