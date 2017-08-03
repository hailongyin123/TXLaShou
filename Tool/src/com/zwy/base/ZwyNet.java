package com.zwy.base;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class ZwyNet {
    private static int CONNECTION_TIME_OUT = 30 * 1000;
    private static int SOCKET_TIME_OUT = 30 * 1000;
    protected static String charset = HTTP.UTF_8;


    public static String getResponseByMultiPost(String url,
                                                List<NameValuePair> params) {

        String result = null;
//        HttpPost httpPost = new HttpPost(url);
//        // 设置HTTP POST请求参数必须用NameValuePair对象
//
//        try {
//            if (params == null) {
//                params = new ArrayList<NameValuePair>();
//            }
//            MultipartEntity entity = new MultipartEntity();
//            for (int index = 0; index < params.size(); index++) {
//                if (params.get(index).getName().startsWith("error_file")) {
//                    File file = new File(params.get(index).getValue());
//                    if (file != null && file.exists() && file.isFile()) {
//                        entity.addPart(params.get(index).getName(),
//                                new FileBody(file));
//                    }
//
//                } else {
//                    // Normal string data
//                    try {
////                        entity.addPart(
////                                params.get(index).getName(),
////                                new StringBody(params.get(index).getValue(),
////                                        ContentType.create("text/plain",
////                                                Charset.forName(HTTP.UTF_8))));
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//            HttpResponse httpResponse = null;
//            httpPost.setEntity(entity);
//            DefaultHttpClient client = new DefaultHttpClient();
//            HttpConnectionParams.setConnectionTimeout(client.getParams(),
//                    CONNECTION_TIME_OUT);
//            HttpConnectionParams.setSoTimeout(client.getParams(),
//                    SOCKET_TIME_OUT);
//            httpResponse = client.execute(httpPost);
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                // 第三步，使用getEntity方法活得返回结果
//                result = EntityUtils.toString(httpResponse.getEntity());
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    /**
     * post请求网络参数
     *
     * @param url    地址
     * @param params 参数对象
     * @return
     * @author ForZwy
     */
    public static String getResponseByPost(String url,
                                           List<NameValuePair> params) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
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
}
