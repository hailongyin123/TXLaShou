package com.txls.txlashou.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.txls.txlashou.R;
import com.txls.txlashou.bean.BillListBean;
import com.txls.txlashou.bean.PrePayBean;
import com.txls.txlashou.bean.UserBean;
import com.txls.txlashou.data.ActivityManager;
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
 * 首页
 */
public class MainActivity extends SuperActivity {
    //声明控件
    private TextView userRepayLoan;
    private TextView repayNum;
    private LinearLayout idName;
    private LinearLayout listSelect;
    private LinearLayout repayment;
    private LinearLayout contentSelect;
    private UserBean userBean;
    private Context mContext;

    //侧滑控件
    private SlidingMenu slidingMenu;
    private ImageView iconSliding;
    private View slidingMenuView;
    //侧滑子布局
    private RelativeLayout mRLMessage;
    private RelativeLayout mRLPassword;
    private RelativeLayout mRLUs;
    private RelativeLayout mRLExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        /**
         * 状态栏透明
         */
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_main);
        userBean = (UserBean) getIntent().getSerializableExtra("userData");
        userRepayLoan.setText("00.00");
        repayNum.setText("每期还款金额:00.00元");
        mContext = this;
        initSlidingMenu();
    }
    //初始化侧滑界面
    private void initSlidingMenu(){
        // configure the SlidingMenu
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);

        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         * SlidingMenu沉浸式状态栏
         */
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        //为侧滑菜单设置子布局及监听
        slidingMenuView =  View.inflate(mContext,R.layout.activity_slidingmenu,null);
        slidingMenu.setMenu(slidingMenuView);
        mRLMessage = (RelativeLayout) slidingMenuView.findViewById(R.id.rl_message_center);
        mRLMessage.setOnClickListener(this);
        mRLPassword = (RelativeLayout) slidingMenuView.findViewById(R.id.rl_password_safe);
        mRLPassword.setOnClickListener(this);
        mRLUs = (RelativeLayout) slidingMenuView.findViewById(R.id.rl_about_us);
        mRLUs.setOnClickListener(this);
        mRLExit = (RelativeLayout) slidingMenuView.findViewById(R.id.rl_exit_login);
        mRLExit.setOnClickListener(this);
    }

    //双击返回键退出程序
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                UserDataManager.clear(this);
                ActivityManager.getInstance().exit();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取首页数据
        getMainData();
    }

    @Override
    public void initView() {
        userRepayLoan = (TextView) findViewById(R.id.pay_money_number);
        repayNum = (TextView) findViewById(R.id.IssueRepayAmount);
        idName = (LinearLayout) findViewById(R.id.ll_id_name);
        listSelect = (LinearLayout) findViewById(R.id.ll_list_select);
        repayment = (LinearLayout) findViewById(R.id.ll_repayment);
        contentSelect = (LinearLayout) findViewById(R.id.ll_content_select);
        iconSliding = (ImageView) findViewById(R.id.iv_sliding_menu);
        //首页模块监听
        idName.setOnClickListener(this);
        listSelect.setOnClickListener(this);
        repayment.setOnClickListener(this);
        contentSelect.setOnClickListener(this);
        iconSliding.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelToast();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message_center:
                showToast("消息中心");
                Intent intentMessageCenter = new Intent(mContext, MessageCenter.class);
                startActivity(intentMessageCenter);
                break;
            case R.id.rl_about_us:
                showToast("关于我们");
                Intent intentAboutUs = new Intent(mContext, AboutUsActivity.class);
                startActivity(intentAboutUs);
                break;
            case R.id.rl_password_safe:
                showToast("密码安全");
                Intent intentPasswordSafe = new Intent(mContext, ForgetPasswordActivity.class);
                startActivity(intentPasswordSafe);
                break;
            case R.id.rl_exit_login:
                Intent intentExitLogin = new Intent(mContext, LoginActivity.class);
                startActivity(intentExitLogin);
                showToast("退出登录");
                break;
            case R.id.iv_sliding_menu:
                if (Utils.isOnClickable()) {
                    slidingMenu.toggle();
                }
                break;
            case R.id.ll_id_name:
                if (Utils.isOnClickable()) {
                    autoUser();
                }
                break;
            case R.id.ll_list_select:
                if (Utils.isOnClickable()) {
                    billList();
                }
                break;
            case R.id.ll_repayment:
                //showToast("此功能暂未开通！");
                //提前预约还清功能去掉
                if (Utils.isOnClickable()) {
                    Intent intent = new Intent(MainActivity.this, RepaymentActivity.class);
                    startActivity(intent);
                    //prePay();
                }
                break;
            case R.id.ll_content_select:
                if (Utils.isOnClickable()) {
                    if (UserDataManager.getAuthSign(mContext).equals("1")) {
                        Intent intentContent = new Intent(this, ContentActivity.class);
                        startActivity(intentContent);
                    } else if (UserDataManager.getAuthSign(mContext).equals("0")) {
                        showToast("用户未认证，请认证后再点击查询");
                    }
                }
                break;
        }
    }
    //Toast多显问题
    private Toast mToast;
    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
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
    //获取首页数据
    private void getMainData() {
        if (UserDataManager.getAuthSign(mContext).equals("1")) {
            Map<String, String> params = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("token", UserDataManager.getToken(this));
            String newStr = MD5Utils.getSignStr(map);
            params.put("message", newStr);
            OkHttpUtils.post().url(ZwyDefine.getUrl("user/customerLoanInfo.action"))
                    .params(params).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ZwyCommon.showToast(MainActivity.this, "获取数据失败，检查网络");
                            return;
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("认证的用户返回的用户个人信息数据=====  ", response.toString());
                            userBean = UserBean.getInstance();
                            Gson gson = new Gson();
                            userBean = gson.fromJson(response, UserBean.class);
                            if (userBean.getCode().equals("2000")) {
                                UserDataManager.putRepayLoanMoney(MainActivity.this, userBean.getResponse().getRepayAmount());
                                UserDataManager.putIssueMoney(MainActivity.this, userBean.getResponse().getIssueRepayAmount());
                                UserDataManager.putOverdueStatus(MainActivity.this,userBean.getResponse().getOverdueStatus());
                            } else if (userBean.getCode().equals("UR0006")) {
                                UserDataManager.clear(MainActivity.this);
                                ZwyCommon.showToast(MainActivity.this, userBean.getMessage() + ",请重新登录");
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (userBean.getCode().equals("UR00004")) {
                                ZwyCommon.showToast(MainActivity.this, userBean.getMessage());
                                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(intent);
                            }else {
                                    ZwyCommon.showToast(MainActivity.this,userBean.getMessage());
                                    return;
                            }
                            if (UserDataManager.getAuthSign(mContext).equals("1")) {
                                userRepayLoan.setText(UserDataManager.getRepayLoanMoney(MainActivity.this));
                                repayNum.setText("每期还款金额:" + UserDataManager.getIssueMoney(MainActivity.this) + "元");
                            }
                        }
                    });
        }
    }
    //用户认证方法
    private void autoUser() {
        if (UserDataManager.getAuthSign(mContext).equals("0")) {
            Intent intentID = new Intent(this, IDCardActivity.class);
            intentID.putExtra("userData", userBean);
            startActivity(intentID);
        }
        if (UserDataManager.getAuthSign(mContext).equals("1")) {
            Map<String, String> params = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("token", UserDataManager.getToken(this));
            String newStr = MD5Utils.getSignStr(map);
            params.put("message", newStr);
            OkHttpUtils.post().url(ZwyDefine.getUrl("user/customerLoanInfo.action"))
                    .params(params).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ZwyCommon.showToast(MainActivity.this, "获取数据失败，检查网络");
                            return;
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("认证的用户返回的用户个人信息数据=====  ", response.toString());
                            userBean = UserBean.getInstance();
                            Gson gson = new Gson();
                            userBean = gson.fromJson(response, UserBean.class);
                            if (userBean.getCode().equals("2000")) {
                                Intent intent = new Intent(MainActivity.this, UserMessageActivity.class);
                                intent.putExtra("userData", userBean);
                                startActivity(intent);
                            }else {
                                ZwyCommon.showToast(MainActivity.this,userBean.getMessage());
                                return;
                            }
                            if (userBean.getCode().equals("UR0006")) {
                                ZwyCommon.showToast(MainActivity.this, "用户未登录或超时,请重新登录");
                                UserDataManager.clear(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }
    //已出账单查询方法
    private void billList() {
        if (UserDataManager.getAuthSign(mContext).equals("1")) {
            //已出账单查询
            Map<String, String> params = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("token", UserDataManager.getToken(this));
            map.put("flag", "1");
            String newStr = MD5Utils.getSignStr(map);
            params.put("message", newStr);
            OkHttpUtils.post().url(ZwyDefine.getUrl("user/queryRepayPlan.action"))
                    .params(params).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ZwyCommon.showToast(MainActivity.this, "获取数据失败，检查网络");
                            return;
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("已出账单返回的信息数据=====  ", response.toString());
                            BillListBean billListBean = BillListBean.getInstance();
                            Gson gson = new Gson();
                            billListBean = gson.fromJson(response, BillListBean.class);
                            if (billListBean.getCode().equals("2000")) {
                                Intent intent = new Intent(MainActivity.this, ListBillActivity.class);
                                intent.putExtra("billData", billListBean);
                                startActivity(intent);
                            }else {
                                ZwyCommon.showToast(MainActivity.this,userBean.getMessage());
                                return;
                            }
                            if (billListBean.getCode().equals("UR0006")) {
                                ZwyCommon.showToast(MainActivity.this, "用户未登录或超时,请重新登录");
                                UserDataManager.clear(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        } else {
            ZwyCommon.showToast(MainActivity.this, "用户未认证，请认证后再点击查询");
        }
    }
    //提前预约还清方法
    private void prePay() {

        if (UserDataManager.getAuthSign(mContext).equals("1")) {
            //提前预约还清
            Map<String, String> params = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("token", UserDataManager.getToken(this));
            String newStr = MD5Utils.getSignStr(map);
            params.put("message", newStr);
            OkHttpUtils.post().url(ZwyDefine.getUrl("user/queryAdvanceRepayPlan.action"))
                    .params(params).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ZwyCommon.showToast(MainActivity.this, "获取数据失败，检查网络");
                            return;
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("预约还款信息数据 =====  ", response.toString());
                            PrePayBean prePayBean = new PrePayBean();
                            Gson gson = new Gson();
                            prePayBean = gson.fromJson(response, PrePayBean.class);
                            if (prePayBean.getCode().equals("2000")) {
                                if (UserDataManager.getOverdueStatus(MainActivity.this).equals("0")){
                                    Intent intent = new Intent(MainActivity.this, PrepaymentActivity.class);
                                    intent.putExtra("prePayData", prePayBean);
                                    startActivity(intent);
                                }else{
                                    ZwyCommon.showToast(MainActivity.this, "" +
                                            "，不可提前预约还清");
                                }
                            }else{
                                ZwyCommon.showToast(MainActivity.this,userBean.getMessage());
                                return;
                            }
                            if (prePayBean.getCode().equals("UR0006")) {
                                ZwyCommon.showToast(MainActivity.this, "用户未登录或超时,请重新登录");
                                UserDataManager.clear(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        } else {
            ZwyCommon.showToast(MainActivity.this, "用户未认证，请认证后再点击查询");
        }
    }
}
