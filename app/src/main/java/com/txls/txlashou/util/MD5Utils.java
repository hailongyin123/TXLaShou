package com.txls.txlashou.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 加密签名处理
 */

public class MD5Utils {
	/**
	 * 加密方法
	 */
	public static String getSignStr(Map<String,String> map){
		//添加签名字段
		map.put("clientType", "2");
		map.put("signature", "20170203");
		ArrayList<String> keyList = new ArrayList<String>();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			keyList.add(key);
		}
		listSort(keyList);
		StringBuffer buString = new StringBuffer();
		for (String key : keyList) {
			buString.append(map.get(key));
		}
		Log.e("排序后的字段",buString.toString());
		String md5Str = md5Password(buString.toString());
		map.put("signature", md5Str);
		String json = simpleMapToJsonStr(map);
		String SignStr = Base64.encode(json.getBytes());
		Log.e("封装过后的==message====",SignStr);
		return SignStr;
	}
	/**
	 * 将JAVA的MAP转换成JSON字符串， 只转换第一层数据
	 * 
	 * @param map
	 * @return
	 */
	public static String simpleMapToJsonStr(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return "null";
		}
		String jsonStr = "{";
		Set<?> keySet = map.keySet();
		for (Object key : keySet) {
			jsonStr += "\"" + key + "\":\"" + map.get(key) + "\",";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return jsonStr;
	}

	/**
	 * md5加密方法
	 * 
	 * @param password
	 * @return
	 */
	public static String md5Password(String password) {
		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 把没一个byte 做一个与运算 0xff;
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;// 加盐
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}

			// 标准的md5加密后的结果
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}

	}

	/** 排序 */
	public static void listSort(List<String> strList) {
		SortUtil comparator = new SortUtil();
		Collections.sort(strList, comparator);
	}

	static class SortUtil implements Comparator<String> {
		// 关于Collator
		private Collator collator = Collator
				.getInstance(java.util.Locale.CHINA);

		public int compare(String o1, String o2) {
			// 把字符串转换为一系列比特，它们可以以比特形式�? CollationKeys 相比�?
			CollationKey key1 = collator.getCollationKey(o1.toString());// 要想不区分大小写进行比较用o1.toString().toLowerCase()
			CollationKey key2 = collator.getCollationKey(o2.toString());
			return key1.compareTo(key2);// 返回的分别为1,0,-1
			// 分别代表大于，等于，小于。要想按照字母降序排序的�? 加个�?-”号
		}
	}
}
