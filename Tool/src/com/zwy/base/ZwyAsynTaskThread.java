package com.zwy.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 线程
 *
 * @author ForLyp
 */
public class ZwyAsynTaskThread extends Thread {
    Handler mHandler;

    public ZwyAsynTaskThread(String aName) {
        setName(aName);
    }

    public void execRunnable(Runnable aTask) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage();
            msg.obj = aTask;
            mHandler.sendMessage(msg);
        }
    }

    public void run() {
        Looper.prepare();
        mHandler = new Handler() {

            public void handleMessage(Message msg) {
                Runnable task = (Runnable) msg.obj;
                task.run();
            }
        };
        Looper.loop();
    }
}
