package com.zwy.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.zwy.app.ZwyContextKeeper;

public class ZwyFileOption {
    public static final String ERR_TAG = "FileOption.java";

    /**
     * sd卡是否可用
     *
     * @return
     * @author ForLyp
     */
    static public boolean isExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 保存图片到文件
     *
     * @param aBitmap
     * @param aFile
     * @param aFormat
     * @return
     * @author ForLyp
     */
    static public boolean saveImg(Bitmap aBitmap, String aFile,
                                  Bitmap.CompressFormat aFormat) {

        if (aBitmap == null) {
            return false;
        }
        try {
            File file = new File(aFile);
            if (file.exists())
                file.delete();
            else
                file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(aFile);
            aBitmap.compress(aFormat, 100, fos);// (0-100)压缩文件

            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制文件
     */
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {

        if (!sourceFile.exists()) {
            return;
        }
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 复制文件
     */
    public static void copyFile(String sFile, String tFile) throws IOException {
        File sourceFile = new File(sFile);
        File targetFile = new File(tFile);

        copyFile(sourceFile, targetFile);
    }

    /**
     * 复制文件夹
     */
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();

        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(
                        new File(targetDir).getAbsolutePath() + File.separator
                                + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 删除文件
     */
    public static void delFile(String strFileName) {
        if(strFileName == null){
            return;
        }
        File myFile = new File(strFileName);
        if (myFile!=null&&myFile.exists()) {
            myFile.delete();
        }
    }

    /**
     * 检查文件或目录是否存在
     */
    public static boolean isExist(String strFileName) {
        if(strFileName==null){
            return false;
        }
        File myFile = new File(strFileName);
        return myFile!=null&&myFile.exists();
    }

    /**
     * 在指定目录查找相应后缀名的文件
     */
    public static void getFiles(String folder, String ext,
                                ArrayList<String> files) {
        File[] file = (new File(folder)).listFiles();
        for (File file2 : file) {
            if (file2.getName().endsWith(ext)) {
                files.add(file2.getName());
            }
        }
    }

    /**
     * 从raw中复制文件
     *
     * @param aContext
     * @param aSrcId
     * @param aTar
     * @return
     * @author ForLyp
     */
    static public boolean copyFileFromRaw(int aSrcId, String aTar) {
        File src = new File(aTar);
        if (src.exists()) {
            return true;
        }
        try {
            InputStream inputStream = ZwyContextKeeper.getInstance()
                    .getResources().openRawResource(aSrcId);

            OutputStream outStream = new FileOutputStream(aTar);
            BufferedInputStream bin = null;
            BufferedOutputStream bout = null;

            bin = new BufferedInputStream(inputStream);

            bout = new BufferedOutputStream(outStream);

            byte[] b = new byte[1024];

            int len = bin.read(b);

            while (len != -1) {
                bout.write(b, 0, len);
                len = bin.read(b);
            }

            bin.close();
            bout.close();

        } catch (IOException e) {
            return true;
        }
        return false;
    }

    static public boolean copyFileFromAssets(String aSrc, String aTar) {
        File src = new File(aTar);
        if (src.exists()) {
            return true;
        }

        try {
            src.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        AssetManager assetManager = ZwyContextKeeper.getInstance().getAssets();
        try {
            InputStream inputStream = assetManager.open(aSrc);

            OutputStream outStream = new FileOutputStream(aTar);
            BufferedInputStream bin = null;
            BufferedOutputStream bout = null;

            bin = new BufferedInputStream(inputStream);

            bout = new BufferedOutputStream(outStream);

            byte[] b = new byte[1024];

            int len = bin.read(b);

            while (len != -1) {
                bout.write(b, 0, len);
                len = bin.read(b);
            }

            bin.close();
            bout.close();

        } catch (IOException e) {
            return true;
        }
        return false;
    }

    public static Map<String, Drawable> getZip(String path) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(path);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile.entries(); // 获取zip文件中的目录及文件列表
        ZipEntry entry = null;
        Map<String, Drawable> drawable = new HashMap<String, Drawable>();
        while (e.hasMoreElements()) {
            entry = e.nextElement();
            if (!entry.isDirectory()) {
                // 如果文件不是目录，则添加到列表中
                try {
                    drawable.put(
                            entry.getName(),
                            Drawable.createFromStream(
                                    zipFile.getInputStream(entry), null));
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        try {

            zipFile.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return drawable;
    }

    /**
     * 删除一个文件夹（不管里面有没有文件都可以删除）
     */
    public static void deleteDirectory(String DirectoryName) {
        File file = new File(DirectoryName);
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length && files != null; i++) {
                files[i].delete();
            }
            file.delete();
        }

    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectorys(sPath);
            }
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectorys(String sPath) {
        boolean flag;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectorys(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 保存字符串到文件中
     *
     * @param aFileName
     * @param data
     * @author ForLyp
     */
    public static void dumpToFile(String aFileName, String data) {
        try {
            if (data == null || aFileName == null)
                return;

            new File(aFileName).delete();

            OutputStream outStream = new FileOutputStream(aFileName);

            BufferedOutputStream bout = null;

            bout = new BufferedOutputStream(outStream);

            byte[] b = data.getBytes();

            int len = b.length;

            bout.write(b, 0, len);

            bout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //

    /**
     * 加载文件为字符串
     *
     * @param aFileName
     * @return
     * @author ForLyp
     */
    public static String loadFromFile(String aFileName) {
        StringBuilder sb = new StringBuilder();
        File file = new File(aFileName);
        if (!file.exists())
            return null;

        try {
            InputStream input = new FileInputStream(aFileName);
            byte[] b = new byte[1024];

            int len = input.read(b);

            while (len != -1) {
                sb.append(new String(b));
                len = input.read(b);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static File createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {

        } else {
            try {
                file.getParentFile().mkdir();
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean createFile(File fileDir) {
        if (!fileDir.exists()) {
            File parentFile = fileDir.getParentFile();
            if (parentFile == null || !parentFile.exists()) {
                createFile(parentFile);
            }
            return fileDir.mkdir();
        }
        return true;
    }

    public static String readAssetFile(String aFileName) {

        Context context = ZwyContextKeeper.getInstance();
        byte[] buffer = new byte[1024 * 10];
        int read_len = 0;
        String res = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(context
                    .getAssets().open(aFileName));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = bis.read(buffer);

            while (len != -1) {
                bos.write(buffer, 0, len);
                len = bis.read(buffer);
            }
            res = new String(bos.toByteArray());
            bis.close();
            bis = null;
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] readFile(String aFileName) {
        File file = new File(aFileName);
        int file_len = (int) file.length();

        byte[] bs = new byte[file_len + 100];
        int read_len = 0;
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            read_len = bis.read(bs);
            bis.close();
            bis = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (read_len == file_len) {
            return bs;
        } else {
            bs = null;
            return bs;
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为B
     * @throws Exception
     */
    public static long getFolderSize(java.io.File file) {
        long size = 0;
        java.io.File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param file File实例
     * @return long 单位为b
     * @throws Exception
     */
    public static long getFileSize(java.io.File file) {
        try {
            if (file == null || !file.exists()) {
                return 0;
            } else {
                return file.length();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else if (size < 1024 * 1024 * 1024 * 1024 * 1024) {
            float tbsize = size / 1024f / 1024f / 1024f / 1024f;
            return formater.format(tbsize) + "TB";
        } else {
            return "too large";
        }
    }
}
