package com.txls.txlashou.bean;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zwy.app.ZwyContextKeeper;

/**
 * 版本迭代实体
 */
public class Element {

	public String mGid = null;
	public String mVersion = null;
	public int mVersionCode = 0;
	public String mDownLoadPath = null;
	public String mIconPath = null;
	public String mDesc = null;

	private static int mVerCode;

	/**
	 * 获取当前程序的版本号
	 * @return
     */
	public static int getVersionCode() {
		if (mVerCode == 0) {
			PackageManager pm = ZwyContextKeeper.getInstance()
					.getPackageManager();
			PackageInfo pkInfo = null;
			try {
				pkInfo = pm.getPackageInfo(ZwyContextKeeper.getInstance()
								.getPackageName(),
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (PackageManager.NameNotFoundException e) {
			}
			if (pkInfo == null) {
				return 0;
			}

			mVerCode = pkInfo.versionCode;
		}
		return mVerCode;
	}

}
