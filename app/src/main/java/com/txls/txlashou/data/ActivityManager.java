package com.txls.txlashou.data;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity管理类
 */
public class ActivityManager {
	private List<Activity> mList = new LinkedList<Activity>();
	private static ActivityManager instance;

	private ActivityManager() {
	}

	/**
	 * 单例模式
	 * @return
	 */
	public synchronized static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
		}
		return instance;
	}

	// 添加 Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			System.exit(0);
		}
	}
	//移除（退出）Activity
	public void exit(Activity activity){
		mList.remove(activity);
	}
	public void exitOther(Activity activity){
		try {
			for (Activity otherActivity : mList) {
				if (otherActivity != null&&!otherActivity.equals(activity))
					otherActivity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			System.exit(0);
		}
	}
	//没有用到？
	public void exitFormTo(Activity activity, Class className){
		try {
			for (int i = 0; i < mList.size(); i++) {
				
			}
			for (Activity otherActivity : mList) {
				if (otherActivity != null&&!otherActivity.equals(activity))
					otherActivity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			System.exit(0);
		}
	}
//
//	public void onLowMemory() {
//		super.onLowMemory();
//		System.gc();
//	}
}
