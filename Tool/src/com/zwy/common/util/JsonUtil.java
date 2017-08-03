package com.zwy.common.util;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    /**
     * 好课的json格式比较奇怪，统一为 key==>JSONArray,所以才有此方法
     *
     * @param json
     * @return JSONArray
     */
    @SuppressWarnings("unchecked")
    public static String getArrayFromString(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            return jsonObject.getJSONArray(iterator.next()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
