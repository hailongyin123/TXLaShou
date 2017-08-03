package com.txls.txlashou.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommonDataInfo implements Serializable {
    //序列号
    private static final long serialVersionUID = 3694015902320170369L;

    private HashMap<String, Object> values;

    public HashMap<String, Object> getHashMap() {
        return values;
    }

    public CommonDataInfo() {
        values = new HashMap<String, Object>();
    }

    public CommonDataInfo(HashMap<String, Object> map_values) {
        if (map_values != null) {
            this.values = map_values;
        } else {
            this.values = new HashMap<String, Object>();
        }
    }

    public CommonDataInfo(String res) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(res);
            values = getHashMapByJSONObject(jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            this.values = new HashMap<String, Object>();
            e.printStackTrace();
        }
    }

    public CommonDataInfo(JSONObject aObj) {
        // values = new HashMap<String, Object>();
        values = getHashMapByJSONObject(aObj);
    }

    public HashMap<String, Object> getHashMapByJSONObject(JSONObject aObj) {
        HashMap values = new HashMap<String, Object>();
        Iterator iter = aObj.keys();
        while (iter.hasNext()) {
            try {
                String key = (String) iter.next();
                if (JSONObject.NULL == aObj.get(key)) {
                    values.put(key, null);
                } else {
                    if (aObj.get(key) instanceof JSONObject) {
                        values.put(key,
                                getHashMapByJSONObject((JSONObject) aObj
                                        .get(key)));
                    } else {
                        values.put(key, aObj.get(key));
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return values;
    }

    public JSONObject getJSONObject(String aKey) {
        JSONObject res = null;

        Object tmp = values.get(aKey);
        if (tmp != null && tmp != JSONObject.NULL) {
            if (tmp instanceof JSONObject) {
                res = (JSONObject) tmp;
            }
        }
//		res = (JSONObject) tmp;
        return res;
    }

    public HashMap<String, Object> getHashMapByKey(String aKey) {
        HashMap<String, Object> res = null;

        Object tmp = values.get(aKey);
        if (tmp != null && tmp != JSONObject.NULL) {
            if (tmp instanceof Map) {
                res = (HashMap<String, Object>) tmp;
            }
        }
        return res;
    }

    public String getString(String aKey) {

        String res = null;
        try {
            Object tmp = values.get(aKey);
            if (tmp != null && tmp != JSONObject.NULL) {
                if (tmp instanceof String || tmp instanceof Integer
                        || tmp instanceof Double || tmp instanceof Float) {
                    res = tmp + "";
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return res;
    }

    public Object getObject(String aKey) {
        try {
            Object tmp = values.get(aKey);
            return tmp;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public void put(String aKey, Object aValue) {
        values.put(aKey, aValue);
    }

    public int getInt(String aKey) {
        int res = 0;
        try {
            Object tmp = values.get(aKey);
            if (tmp != null && tmp != JSONObject.NULL) {
                if (tmp instanceof Integer) {
                    res = (Integer) tmp;
                } else {
                    res = Integer.parseInt(tmp + "");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return res;
    }

    public long getLong(String aKey) {
        long res = 0;
        try {
            Object tmp = values.get(aKey);
            if (tmp != null && tmp != JSONObject.NULL) {
                if (tmp instanceof Long) {
                    res = (Long) tmp;
                } else {
                    res = Long.parseLong(tmp + "");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return res;
    }

    public JSONArray getJSONArray(String aKey) {
        JSONArray res = null;
        Object tmp = values.get(aKey);
        if (tmp != null && tmp != JSONObject.NULL) {
            if (tmp instanceof JSONArray) {
                res = (JSONArray) tmp;
            }
        }
        return res;
    }

    public boolean getBoolean(String aKey) {
        boolean res = false;
        Object tmp = values.get(aKey);
        if (tmp != null && tmp != JSONObject.NULL) {
            if (tmp instanceof Boolean) {
                res = (Boolean) tmp;
            }
        }
        return res;
    }

    public float getFloat(String aKey) {
        float res = 0f;
        Object tmp = values.get(aKey);
        if (tmp != null && tmp != JSONObject.NULL) {
            if (tmp instanceof Float) {
                res = (Float) tmp;
            }
        }
        return res;
    }

    static public List<CommonDataInfo> doHttpStaff(JSONArray jarray) {
        List<CommonDataInfo> result = new ArrayList<CommonDataInfo>();
        try {
            int count = jarray.length();
            for (int i = 0; i < count; i++) {
                result.add(new CommonDataInfo(jarray.getJSONObject(i)));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return result;
        }
        return result;

    }

    static public List<CommonDataInfo> doHttpStaff(String res) {
        List<CommonDataInfo> result = new ArrayList<CommonDataInfo>();
        if (TextUtils.isEmpty(res)) {
            return null;
        }
        try {
            JSONArray jarray = new JSONArray(res);
            int count = jarray.length();
            for (int i = 0; i < count; i++) {
                result.add(new CommonDataInfo(jarray.getJSONObject(i)));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public void clean() {
        if (values != null) {
            values.clear();
        }
    }

}
