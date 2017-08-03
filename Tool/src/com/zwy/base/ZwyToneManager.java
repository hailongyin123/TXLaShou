package com.zwy.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.zwy.app.ZwyContextKeeper;

public class ZwyToneManager {
    public static MediaPlayer player = new MediaPlayer();
    private Context mContext;
    private AudioManager audioManager;

    public ZwyToneManager() {
        mContext = ZwyContextKeeper.getInstance();

    }

    int voiceModel;
    OnPlayCompletionListener onPlayCompletionListener;

    public interface OnPlayCompletionListener {
        void onOverListener();

        void onStartListener();
    }

    public void setOnPlayCompletionListener(OnPlayCompletionListener listener) {
        onPlayCompletionListener = listener;
    }

    OnCompletionListener mOnCompletionListener = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            stop();
        }
    };

    private void initFlag() {
        // TODO Auto-generated method stub
//		SharedPreferences setting = mContext.getSharedPreferences("setting" + UserInfoBean.username,
//				mContext.MODE_PRIVATE);
//		if (setting.contains("voice")) {
//			voiceModel = setting.getInt("voice", -1);
//		} else {
//			voiceModel = -1;
//		}
    }

    public void play(String filePath) {
        try {
            initFlag();
            audioManager = (AudioManager) mContext
                    .getSystemService(Service.AUDIO_SERVICE);
            if (voiceModel == 1) {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            } else {
                audioManager.setMode(AudioManager.MODE_NORMAL);
            }
            int maxVolume = audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            player.reset();
            player.setOnCompletionListener(mOnCompletionListener);
            player.setDataSource(filePath);
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void play(File file) {
        try {
            initFlag();
            final FileInputStream fis = new FileInputStream(file);
            audioManager = (AudioManager) mContext
                    .getSystemService(Service.AUDIO_SERVICE);
            int maxVolume = audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            if (voiceModel == 1) {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            } else {
                audioManager.setMode(AudioManager.MODE_RINGTONE);
            }
            player.reset();
            player.setOnCompletionListener(mOnCompletionListener);
            player.setDataSource(fis.getFD());
            player.prepare();
            player.start();
            if (onPlayCompletionListener != null) {
                onPlayCompletionListener.onStartListener();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isPlaying() {
        // TODO Auto-generated method stub
        return player.isPlaying();
    }

    public void stop() {
        audioManager = (AudioManager) mContext
                .getSystemService(Service.AUDIO_SERVICE);
        int maxVolume = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        player.stop();
        if (onPlayCompletionListener != null) {
            onPlayCompletionListener.onOverListener();
        }
    }

}
