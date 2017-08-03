package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.txls.txlashou.R;
import com.txls.txlashou.bean.UserBean;

/**
 * 用户信息页
 */
public class UserMessageActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private UserBean userBean;
    private TextView userName;
    private TextView userIDCard;
    private TextView loan;
    private TextView repayLoan;
    private TextView stagingNumber;
    private TextView repayNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        userBean = UserBean.getInstance();

    }

    @Override
    public void initView() {
        //找控件
        userName = (TextView) findViewById(R.id.user_name);
        userIDCard = (TextView) findViewById(R.id.user_id_card);
        loan = (TextView) findViewById(R.id.user_loan);
        repayLoan = (TextView) findViewById(R.id.repayment_loan);
        stagingNumber = (TextView) findViewById(R.id.staging_number);
        repayNumber = (TextView) findViewById(R.id.repayment_number);
        img_return = (ImageView) findViewById(R.id.img_return);
        //监听
        img_return.setOnClickListener(this);
        //赋值
        Intent intent = getIntent();//获取Intent
        userBean = (UserBean) intent.getSerializableExtra("userData");
        userName.setText(userBean.getResponse().getRealName());
        userIDCard.setText(userBean.getResponse().getIdNo());
        loan.setText(userBean.getResponse().getLoanAmount());
        repayLoan.setText(userBean.getResponse().getRepayAmount());
        stagingNumber.setText(userBean.getResponse().getIssue());
        repayNumber.setText(userBean.getResponse().getIssueRepayAmount());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
            break;
        }
    }
}
