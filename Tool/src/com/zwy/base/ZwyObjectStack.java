package com.zwy.base;

import java.util.ArrayList;

/**
 * 自定义的一个栈
 *
 * @author ForZwy
 */
public class ZwyObjectStack {
    static ArrayList<Object> mStaticObjStack = new ArrayList<Object>();

    static private void push(Object aObj) {
        mStaticObjStack.add(0, aObj);
    }

    static private Object pop() {
        if (mStaticObjStack.size() > 0) {
            Object res = mStaticObjStack.remove(0);
            return res;
        }
        return null;
    }
}
