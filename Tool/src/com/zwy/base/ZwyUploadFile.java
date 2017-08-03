package com.zwy.base;

import java.io.File;

/**
 * 文件上传
 *
 * @author ForZwy
 */
public class ZwyUploadFile {

    private OnUploadFileForResultListener listener;

    public void uploadFile(final File file, final String description) {
        if (file.exists()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    upload(file.getAbsolutePath(), description);
                }
            }).start();
        }

    }

    public void uploadFile(final String file_path, final String description) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                upload(file_path, description);
            }
        }).start();
    }

    private void upload(String filePath, String description) {

    }

    public interface OnUploadFileForResultListener {
        public abstract void onResultListener(boolean isUploadSuccess,
                                              String key, String link, String desc);
    }

    public void setListener(OnUploadFileForResultListener resultListener) {
        listener = resultListener;
    }

    public void removeListener() {
        listener = null;
    }

}
