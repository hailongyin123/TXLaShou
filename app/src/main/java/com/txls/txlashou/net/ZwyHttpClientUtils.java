package com.txls.txlashou.net;

import android.text.TextUtils;

import com.zwy.base.ZwyLog;
import com.zwy.common.util.ZwyWriteLogUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ZwyHttpClientUtils {
    public static final String TAG = "ZwyHttpClientUtils";
	private static int CONNECTION_TIME_OUT = 30 * 1000;
	private static int SOCKET_TIME_OUT = 30 * 1000;

	/**
	 * 文件上传
	 * 
	 *
	 *            字节流变化量
	 */
	static public interface ZwyUploadListener {
		public void onStart(String aUrl);

		public void onError(String aUrl, String aInfo);

		public void onProgressChanged(String aUrl, long aValue);

		public void onEnd(String aUrl, String aInfo);
	}

	public interface ZwyDownloadListener {
		void onProgressChanged(String name, int progress);

		void onError(String error);
	}

	public static boolean downLoadFile(String aUrl, String aLocalPath,
			String notification, ZwyDownloadListener listener) {
		try {
			File file = new File(aLocalPath);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			} else {
				String path = file.getParent();
				new File(path).mkdirs();
				file.createNewFile();
			}
			URL url = new URL(aUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			int size = conn.getContentLength();
			InputStream is = conn.getInputStream();
			if (is == null)
				return false;
			FileOutputStream fos = new FileOutputStream(aLocalPath);
			byte[] buffer = new byte[1024];
			int len = 0;
			int temp = -1;
			int progress = 0;
			int hasRead = 0;

			while ((len = is.read(buffer)) >= 0) {
				hasRead += len;
				progress = hasRead * 100 / size;
				if (temp != progress) {
					if (listener != null) {
						listener.onProgressChanged(notification, progress);
					}
				}
				temp = progress;
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			is.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (listener != null)
				listener.onError(notification);
			return false;
		}
		return true;
	}

	/**
	 * post请求网络参数
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数对象
	 * @return
	 * @author ForZwy
	 */
	public static String getResponseByPost(String url,
			List<NameValuePair> params, Header[] headers) {
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		if (headers != null) {
			httpPost.setHeaders(headers);
		}
		// 设置HTTP POST请求参数必须用NameValuePair对象
		if (params == null) {
			params = new ArrayList<NameValuePair>();
		}
		HttpResponse httpResponse = null;
		try {
			// 设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第三步，使用getEntity方法活得返回结果
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * post请求网络参数
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数对象
	 * @return
	 * @author ForZwy
	 */
	public static String getResponseByPost(String url,
			List<NameValuePair> params) {

		String result = null;
		HttpPost httpPost = new HttpPost(url);
		// 设置HTTP POST请求参数必须用NameValuePair对象
		ZwyWriteLogUtil.Log(" 设置参数 进行联网操作 " + "\n");
		try {
			if (params == null) {
				params = new ArrayList<NameValuePair>();
			}

			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
					.create();
			multipartEntityBuilder
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntityBuilder.setCharset(Charset.forName(HTTP.UTF_8));

			for (int index = 0; index < params.size(); index++) {
				ZwyLog.i(TAG, "key = " + params.get(index).getName()
						+ " value = " + params.get(index).getValue());
				if (params.get(index).getName().startsWith("noteImg")
						|| params.get(index).getName()
								.equalsIgnoreCase("noteWav")
						|| params.get(index).getName()
								.equalsIgnoreCase("avatar")
						|| params.get(index).getName().startsWith("commImg")
						|| params.get(index).getName()
								.equalsIgnoreCase("commWav")) {
					File file = new File(params.get(index).getValue());
					if(file.exists()){
						multipartEntityBuilder.addBinaryBody(params.get(index)
								.getName(), file);
					}
					
				} else {
					multipartEntityBuilder.addTextBody(params.get(index)
							.getName(), params.get(index).getValue(),
							ContentType.create("text/plain",
									Charset.forName(HTTP.UTF_8)));
				}
			}
			ZwyWriteLogUtil.Log(" 参数设置完成 进行联网操作 " + "\n");
			// 生成 HTTP 实体
			HttpEntity httpEntity = multipartEntityBuilder.build();
			httpPost.setEntity(httpEntity);

			HttpResponse httpResponse = null;
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(),
					CONNECTION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(client.getParams(),
					SOCKET_TIME_OUT);
			ZwyWriteLogUtil.Log("httpResponse  ===");
			httpResponse = client.execute(httpPost);
			ZwyWriteLogUtil.Log("httpResponse.getStatusLine().getStatusCode() = " + httpResponse.getStatusLine().getStatusCode() + "\n");
			ZwyLog.i(TAG, "httpResponse.getStatusLine().getStatusCode() = " + httpResponse.getStatusLine().getStatusCode());
			ZwyWriteLogUtil.Log("httpResponse.getStatusLine().getStatusCode() = " + httpResponse.getStatusLine().getStatusCode()+"\n");
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第三步，使用getEntity方法活得返回结果
				result = EntityUtils.toString(httpResponse.getEntity());
				if (!TextUtils.isEmpty(result)) {
					result = result.replaceAll("<br>", "\n")
							.replaceAll("<\\\\/br>", "\n")
							.replaceAll("<\\\\/ br>", "\n")
							.replaceAll("<\\\\/  br>", "\n")
							.replaceAll("<br\\\\/>", "\n")
							.replaceAll("<br \\\\/>", "\n")
							.replaceAll("<br  \\\\/>", "\n");
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			ZwyWriteLogUtil.Log(e.getMessage());
		} catch (IOException e) {
			ZwyWriteLogUtil.Log(e.getMessage());
			e.printStackTrace();
		}
		ZwyWriteLogUtil.Log(" 网络链接完毕 返回 " + "\n");
        JSONObject jsonObject;
        ZwyLog.i(TAG, "url = " + url);
        try {
            if (TextUtils.isEmpty(result)) {
                ZwyLog.i(TAG, "result = " + "null");
            } else {

                jsonObject = new JSONObject(result);
                ZwyLog.i(TAG, "result = " + jsonObject.toString());
				ZwyWriteLogUtil.Log(" 网络链接完毕 返回   "+jsonObject.toString() + "\n");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
			ZwyWriteLogUtil.Log(" 网络链接完毕  参数格式化错误   " + "\n");
            ZwyLog.i(
                    TAG,
                    "result = " + result + " e.printStackTrace() = "
                            + e.toString());
            e.printStackTrace();
        }
		return result;
	}

}
