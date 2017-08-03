package com.txls.txlashou.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


/**
 * 用户信息保存管理类
 * 
 * @author ForYHL
 */
public class UserDataManager{
	// 单例
	private static SharedPreferences mUserInfoSharedPreferences = null;
	private static SharedPreferences.Editor userEditor = null;

	public static SharedPreferences getSharedPreferences(Context context) {
		if (mUserInfoSharedPreferences == null)
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		return mUserInfoSharedPreferences;
	}

	/**
	 * 清空 SharedPreferences 中 Token信息。
	 *
	 * @param context
	 *            应用程序上下文环境
	 */
	public static void clear(Context context) {
		v2LogoutService(context);
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.clear();
		userEditor.commit();
	}

	/**
	 * 注销?
	 */
	public static void v2LogoutService(Context mContext) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("token", UserInfoUtil.gettoken(mContext));
//		hashMap.put("userId", UserInfoUtil.getuserId(mContext));
//		OkHttpUtils.post().url(URL.SERVICE_URL + URL.LOGIN_OUT)
//				.params(DataFormUtils.DataEncryption2(hashMap)).build()
//				.execute(new StringCallback() {
//					@Override
//					public void onError(Call call, Exception e, int id) {
//					}
//
//					@Override
//					public void onResponse(String response, int id) {
//					}
//				});
	}

	// put用户手机号

	public static void putUserMobile(Context context, String str) {

		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("USER_MOBILE", str);
		userEditor.commit();
	}

	// get用户手机号
	public static String getUserMobile(Context context) {
		String userMobile;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		userMobile = mUserInfoSharedPreferences.getString("USER_MOBILE", "");
		return userMobile;
	}
	// put token
	public static void putToken(Context context, String token) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("TOKEN", token);
		userEditor.commit();
	}

	// get token
	public static String getToken(Context context) {
		String userToken;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		userToken = mUserInfoSharedPreferences.getString("TOKEN", "");
		return userToken;
	}
	// put authSign
	public static void putAuthSign(Context context, String authSign) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("authSign", authSign);
		userEditor.commit();
	}

	// get authSign
	public static String getAuthSign(Context context) {
		String userToken;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		userToken = mUserInfoSharedPreferences.getString("authSign", "");
		return userToken;
	}
	// put LoanMoney
	public static void putRepayLoanMoney(Context context, String loanMoney) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("repayLoanMoney", loanMoney);
		userEditor.commit();
	}

	// get loanMoney
	public static String getRepayLoanMoney(Context context) {
		String userToken;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		userToken = mUserInfoSharedPreferences.getString("repayLoanMoney", "");
		return userToken;
	}
	// put issuMoney
	public static void putIssueMoney(Context context, String issueMoney) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("issueMoney", issueMoney);
		userEditor.commit();
	}

	// get issueMoney
	public static String getIssueMoney(Context context) {
		String userToken;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		userToken = mUserInfoSharedPreferences.getString("issueMoney", "");
		return userToken;
	}
	// put 预期状态
	public static void putOverdueStatus(Context context, String issueMoney) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("overdueStatus", issueMoney);
		userEditor.commit();
	}

	// get 逾期状态
	public static String getOverdueStatus(Context context) {
		String overdueStatus;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		overdueStatus = mUserInfoSharedPreferences.getString("overdueStatus", "");
		return overdueStatus;
	}

	// put userId
	public static void putUserId(Context context, String userId) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putString("USER_ID", userId);
		userEditor.commit();
	}

	// get userId
	public static String getUserId(Context context) {
		String userId;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		userId = mUserInfoSharedPreferences.getString("USER_ID", "");
		return userId;
	}

	// put islogin
	public static void putIsLogin(Context context, boolean islogin) {
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		if (userEditor == null) {
			userEditor = mUserInfoSharedPreferences.edit();
		}
		userEditor.putBoolean("IS_LOGIN", islogin);
		userEditor.commit();
	}

	// get islogin
	public static boolean getIsLogin(Context context) {
		boolean islogin;
		if (mUserInfoSharedPreferences == null) {
			mUserInfoSharedPreferences = context.getSharedPreferences(
					"TXFQ_USERINFO1.0", Context.MODE_PRIVATE);
		}
		islogin = mUserInfoSharedPreferences.getBoolean("IS_LOGIN", false);
		return islogin;
	}
}
