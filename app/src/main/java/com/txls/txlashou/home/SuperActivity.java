package com.txls.txlashou.home;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.txls.txlashou.data.ActivityManager;

/**
 * Activity的父类
 */
public abstract class SuperActivity extends FragmentActivity implements
		View.OnClickListener {
	String mPageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageName = getClass().getName();
		ActivityManager.getInstance().addActivity(this);
		/**
		 * 状态栏沉浸式
		 */
//		if (Build.VERSION.SDK_INT >= 21) {
//			View decorView = getWindow().getDecorView();
//			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//			decorView.setSystemUiVisibility(option);
//			getWindow().setStatusBarColor(Color.TRANSPARENT);
//		}
	}


	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		initView();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initView();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	@Override
	protected void onDestroy() {
		ActivityManager.getInstance().exit(this);
		super.onDestroy();
	}

	public abstract void initView();
	/**
	 * 版本号小于21就隐藏title
	 *
	 * @param view
	 */
	public void isHintTitle(View view) {
		if (Build.VERSION.SDK_INT < 21) {
			view.setVisibility(View.GONE);
		}
	}

}
