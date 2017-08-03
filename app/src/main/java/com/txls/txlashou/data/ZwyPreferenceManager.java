package com.txls.txlashou.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.txls.txlashou.bean.Element;
import com.zwy.app.ZwyContextKeeper;

/**
 * 配置信息保存类
 * 
 * @author ForLyp
 */
public class ZwyPreferenceManager {

	static public SharedPreferences getSharedPreferences() {
		Context context = ZwyContextKeeper.getInstance();
		SharedPreferences MyPreferences = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_WORLD_READABLE
						| Context.MODE_WORLD_WRITEABLE);
		return MyPreferences;
	}

	// demo
	public static boolean isNeedDelete() {
		return getSharedPreferences().getBoolean("is_need_delete", true);
	}

	public static void setNeedDelete(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("is_need_delete", flag);
		editor.commit();
	}

	private static int mVerCode;

	/**
	 * @methods name: getVersionCode
	 * @Descripition : 程序当前版本号
	 * @return ：
	 * @date ：2012-7-23 下午04:50:16
	 * @author ：wuxu
	 * @since CodingExample　Ver(编码范例查看) 1.1
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
			} catch (NameNotFoundException e) {
			}
			if (pkInfo == null) {
				return 0;
			}

			mVerCode = pkInfo.versionCode;
		}
		return mVerCode;
	}

	private static String mVerName = null;

	public static String getVersionName() {
		if (mVerName == null) {
			PackageManager pm = ZwyContextKeeper.getInstance()
					.getPackageManager();
			PackageInfo pkInfo = null;
			try {
				pkInfo = pm.getPackageInfo(ZwyContextKeeper.getInstance()
						.getPackageName(),
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
			}
			if (pkInfo == null) {
				return null;
			}

			mVerName = pkInfo.versionName;
		}
		return mVerName;
	}

	// 得到一个随即时间已更新
	public static long getPlanUpdateTime(final Context context) {
		return getSharedPreferences().getLong("plan_update_time", 0);
	}

	// 设置随机时间
	public static void setPlanUpdateTime(final Context context, long status) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("plan_update_time", status);
		editor.commit();
	}

	/**
	 * @Title: getUpdatepath
	 * @Description: 更新地址
	 * @param context
	 * @return
	 * @author wuxu
	 * @date 2012-5-16
	 */
	public static Element getUpdatepath(final Context context) {
		try {
			String data = getSharedPreferences().getString("update_path", "");
			if (data.length() == 0) {
				return null;
			}
			String[] tmp = data.split("####");
			Element element = new Element();
			element.mDownLoadPath = tmp[0];
			element.mDesc = tmp[1];
			element.mVersion = tmp[2];
			return element;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * @Title: setUpdatePath
	 * @Description: 保存更新
	 * @param context
	 *            数据存入顺序依次为：包名+version+versionCode+下载地址+描述
	 * @param data
	 * @author wuxu
	 * @date 2012-5-16
	 */
	public static void setUpdatePath(final Context context, final Element data) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		StringBuilder sb = new StringBuilder();
		if (data.mDownLoadPath != null && data.mDownLoadPath.length() > 0) {
			sb.append(data.mDownLoadPath + "####");
		} else {
			sb.append("error" + "####");
		}
		if (data.mDesc != null) {
			if (data.mDesc.length() > 0) {
				sb.append(data.mDesc + "####");
			} else {
				sb.append("kong" + "####");
			}
		} else {
			sb.append("error");
		}
		if (data.mVersion != null) {
			if (data.mVersion.length() > 0) {
				sb.append(data.mVersion + "####");
			} else {
				sb.append("kong" + "####");
			}
		} else {
			sb.append("error");
		}
		editor.putString("update_path", sb.toString());
		editor.commit();
	}

	/**
	 * @Title: getLastUpdateDate
	 * @Description: 上次更新时间
	 * @return
	 * @author wuxu
	 * @date 2012-5-16
	 */
	public static long getLastUpdateDate() {
		return getSharedPreferences().getLong("last_update_date", 0l);
	}

	public static void setLastUpdateDate() {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("last_update_date", System.currentTimeMillis());
		editor.commit();
	}

	public static long getLastShowUpdateDate(final Context context) {
		return getSharedPreferences().getLong("last_show_update_date", 0);
	}

	public static void setLastShowUpdateDate(final Context context,
			final long date) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("last_show_update_date", date);
		editor.commit();
	}

	static long DAY = 1000 * 60 * 60 * 24;
	static long MINUTE = 1000 * 60;

	public static boolean shouldShowUpdate(Context context) {
		long last = getLastShowUpdateDate(context);
		long now = System.currentTimeMillis();
		if (last <= 0) {
			return true;
		}
		if (now / DAY != last / DAY) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Title: isUpdateFlag
	 * @Description: 是否有更新
	 * @author wuxu
	 * @date 2012-5-11
	 */
	public static boolean isUpdateFlag(Context context) {
		// TODO Auto-generated method stub
		try {
			boolean value = getSharedPreferences().getBoolean("update_flag",
					false);
			return value;
		} catch (Exception e) {
			return true;
		}

	}

	public static void setUpdateFlag(final Context context, final boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("update_flag", flag);
		editor.commit();
	}

	public static void setNetNotificationFlag(final boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("net_notifacation_flag", flag);
		editor.commit();
	}

	public static boolean getNetNotificationFlag() {
		SharedPreferences prefs = getSharedPreferences();
		return prefs.getBoolean("net_notifacation_flag", true);
	}

	public static void setServiceStatus(String service_status) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("service_status", service_status);
		editor.commit();
	}

	public static String getToken() {
		SharedPreferences prefs = getSharedPreferences();
		return prefs.getString("token", null);
	}
	public static void setToken(String token) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("token", token);
		editor.commit();
	}

	public static String getServiceStatus() {
		SharedPreferences prefs = getSharedPreferences();
		return prefs.getString("service_status", null);
	}

	public static void setNewMessageFlag(final boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("new_message_flag", flag);
		editor.commit();
	}

	public static boolean getNewMessageFlag() {
		SharedPreferences prefs = getSharedPreferences();
		return prefs.getBoolean("new_message_flag", false);
	}

    public static void setNeedForceUpdate(final boolean flag) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("need_force_update", flag);
        editor.commit();
    }

    public static boolean getNeedForceUPdate() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getBoolean("need_force_update", false);
    }

    public static void setNeedForceUpdateVersion(final int version) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("need_force_update_version", version);
        editor.commit();
    }

    public static int getNeedForceUpdateVersion() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getInt("need_force_update_version", 0);
    }

    public static void setUpdateNotification(final String notication_flag) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("notication_flag", notication_flag);
        editor.commit();
    }



}
