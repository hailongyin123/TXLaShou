package com.txls.txlashou.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.txls.txlashou.R;
import com.txls.txlashou.bean.PrePayBean;
import com.txls.txlashou.data.UserDataManager;
import com.txls.txlashou.net.ZwyDefine;
import com.txls.txlashou.util.MD5Utils;
import com.txls.txlashou.util.Utils;
import com.txls.txlashou.wight.TimePickerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zwy.base.ZwyCommon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 提前预约还清页
 */
public class PrepaymentActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private Button btn_prepayment;
    private PrePayBean payBean;
    //时间选择
    int mYear, mMonth, mDay;
    private LinearLayout btn;
    private TextView dateDisplay;
    private TextView pre_loan;
    private TextView pre_residue_amount;
    private TextView per_periods;
    private TextView pre_residue_periods;
    private TextView pre_advance_amount;
    private TextView pre_advance_service_fee;
    final int DATE_DIALOG = 1;
    private TimePickerView timePickerView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepayment);
        payBean = (PrePayBean) getIntent().getSerializableExtra("prePayData");
        pre_loan.setText("贷款总金额  ¥" + payBean.getResponse().getLoanAmount() + "元");
        pre_residue_amount.setText("剩余金额      ¥" + payBean.getResponse().getResidueAmount() + "元");
        per_periods.setText("分期期数" + payBean.getResponse().getPeriods() + "期");
        pre_residue_periods.setText("剩余期数" + payBean.getResponse().getResiduePeriods() + "期");
        pre_advance_amount.setText("¥ " + payBean.getResponse().getAdvanceAmount() + "元");
        pre_advance_service_fee.setText("¥ " + payBean.getResponse().getAdvanceServiceFee() + "元");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_prepayment = (Button) findViewById(R.id.btn_prepayment);
        //监听
        img_return.setOnClickListener(this);
        btn_prepayment.setOnClickListener(this);
        //按要求暂时去掉提前预约还清按钮
        //btn_prepayment.setVisibility(View.GONE);
        //时间控件
        btn = (LinearLayout) findViewById(R.id.dateChoose);
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);
        pre_loan = (TextView) findViewById(R.id.pre_loan);
        pre_residue_amount = (TextView) findViewById(R.id.pre_residue_amount);
        per_periods = (TextView) findViewById(R.id.per_periods);
        pre_residue_periods = (TextView) findViewById(R.id.pre_residue_periods);
        pre_advance_amount = (TextView) findViewById(R.id.pre_advance_amount);
        pre_advance_service_fee = (TextView) findViewById(R.id.pre_advance_service_fee);
        //时间处理
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        timePickerView = new TimePickerView(PrepaymentActivity.this);
        timePickerView.setTextColor(Color.RED);
        timePickerView.setTextSize(18);
        timePickerView.setVisibleItems(5);
        timePickerView.setIsCyclic(false);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        timePickerView.show(str);
        btn.addView(timePickerView.popview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_prepayment:
                if (Utils.isOnClickable()) {
                    getData();
                }
                break;
        }
    }

    private void getData() {
        if (UserDataManager.getAuthSign(this).equals("1")) {
            //传递时间
            String time = timePickerView.getValue();
            Map<String, String> params = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("token", UserDataManager.getToken(this));
            map.put("repayTime", time);
            String newStr = MD5Utils.getSignStr(map);
            params.put("message", newStr);
            OkHttpUtils.post().url(ZwyDefine.getUrl("user/queryAdvanceSettlement.action"))
                    .params(params).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ZwyCommon.showToast(PrepaymentActivity.this, "获取数据失败，检查网络");
                            return;
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("预约还款传输时间返回数据 =====  ", response.toString());
                            PrePayBean prePayBean = new PrePayBean();
                            Gson gson = new Gson();
                            prePayBean = gson.fromJson(response, PrePayBean.class);
                            //ZwyCommon.showToast(PrepaymentActivity.this, prePayBean.getMessage());
                            if (prePayBean.getCode().equals("2000")) {
                                Intent intent = new Intent(PrepaymentActivity.this, PrePayFinallyActivity.class);
                                intent.putExtra("money", payBean.getResponse().getRepayAmount() + "元");
                                String intentTime = timePickerView.getIntentTime();
                                intent.putExtra("time", intentTime);
                                startActivity(intent);
                            }else {
                                ZwyCommon.showToast(PrepaymentActivity.this, prePayBean.getMessage());
                            }
                            if (prePayBean.getCode().equals("UR0006")) {
                                ZwyCommon.showToast(PrepaymentActivity.this, "用户未登录或超时,请重新登录");
                                UserDataManager.clear(PrepaymentActivity.this);
                                Intent intent = new Intent(PrepaymentActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }else{
            ZwyCommon.showToast(PrepaymentActivity.this, "用户未认证，请认证后再点击查询");
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        dateDisplay.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Prepayment Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
