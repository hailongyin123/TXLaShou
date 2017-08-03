package com.zwy.base;

import java.io.IOException;

import android.media.MediaRecorder;

public class ZwyVoiceRecoder extends MediaRecorder {
    // 0,初始化1，开始，2停止
    public int state = 0;

    static public void ensureRecStopped(ZwyVoiceRecoder aRec) {
        try {
            if (aRec != null) {
                if (aRec.state == 1) {
                    aRec.stop();
                    aRec.release();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * @param aFile
     * @return
     */
    public boolean startRecord(final String aFile) {
        boolean res = true;

        try {
            setAudioSource(MediaRecorder.AudioSource.MIC);
            setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            setOutputFile(aFile);
            prepare();
            start();
            state = 1;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res = false;
        }

        return res;
    }

    @Override
    public void stop() throws IllegalStateException {
        // TODO Auto-generated method stub
        super.stop();
        state = 2;
    }

}
