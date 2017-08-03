package com.zwy.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.zwy.app.ZwyContextKeeper;


/**
 * 对象流保存
 *
 * @author ForZwy
 */
public class ZwyObjectSerilizer {
    static Object tmpValue = new Object();

    static public String getRootPath() {
        return ZwyContextKeeper.getSavePath();
    }

    /**
     * @param aObj
     * @param filename
     * @return
     */
    static public boolean writeObject(Object aObj, String filename) {
        return writeObject(aObj, getRootPath(), filename);
    }

    /**
     * @param aObj
     * @param filename
     * @return
     */
    static public boolean writeObject(Object aObj, String path, String filename) {
        boolean res = false;
        synchronized (tmpValue) {
            try {
                File file = new File(path
                        + filename);
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(file));
                oos.writeObject(aObj);
                oos.flush();
                oos.close();
                res = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return res;
    }

    static public Object readObject(String path, String fileName) {
        Object res = null;
        synchronized (tmpValue) {
            try {
                File file = new File(path
                        + fileName);
                if (file.exists()) {
                    return readObject(file);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                res = null;
            }
        }
        return res;
    }

    static public Object readObject(String fileName) {
        return readObject(getRootPath(), fileName);
    }

    static public Object readObject(File file) {
        Object res = null;
        synchronized (tmpValue) {
            try {
                if (file.exists()) {
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(file));
                    res = ois.readObject();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                res = null;
            }
        }
        return res;
    }
}
