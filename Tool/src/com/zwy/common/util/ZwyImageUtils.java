package com.zwy.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.zwy.app.ZwyContextKeeper;
import com.zwy.base.ZwyCommon;

public class ZwyImageUtils {

    /**
     * 要注意的是,这个接口只能处理jpeg格式的图片文件.
     *
     * @param aFile
     * @return
     */
    static public ExifInterface getExif(String aFile) {
        ExifInterface face = null;
        try {
            face = new ExifInterface(aFile);
            return face;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 这个接口只能处理jpeg格式的图片文件.
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {

        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    static public Options getBitmapOptions(String aFile) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(aFile);
            BitmapFactory.decodeStream(fis, null, opts);
            fis.close();
            return opts;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    static public Options getBitmapOptions(InputStream aInput) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(aInput, null, opts);
            return opts;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 自动处理图片旋转(横向切换竖向)
     */
    static public void autoAdjustImageOrientation(String aFile) {

        try {
            // ExifInterface face = new ExifInterface(aFile);
            // face.setAttribute(ExifInterface.TAG_ORIENTATION, 0+"");
            // face.saveAttributes();

            int degree = getExifOrientation(aFile);
            if (degree != 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(aFile);
                Bitmap target = rotate(bitmap, degree);
                ZwyFileOption
                        .saveImg(target, aFile, Bitmap.CompressFormat.JPEG);
                if (!target.isRecycled())
                    target.recycle();
                ExifInterface face = new ExifInterface(aFile);
                face.setAttribute(ExifInterface.TAG_ORIENTATION, 0 + "");
                face.saveAttributes();
            }

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static public Bitmap getFitableBitmap(int aBaseW, int aBaseH, Uri aUri) {
        try {
            // 获取图片尺寸信息
            InputStream input = ZwyContextKeeper.getInstance()
                    .getContentResolver().openInputStream(aUri);
            Options opt = ZwyImageUtils.getBitmapOptions(input);
            input.close();
            // 计算缩放比例，以适配屏幕尺寸为准
            int scale = opt.outWidth * 100 / aBaseW;
            Options readOpt = new Options();
            readOpt.inSampleSize = scale / 100;
            readOpt.inPreferredConfig = Bitmap.Config.RGB_565;
            // 解码指定缩放比的图片
            input = ZwyContextKeeper.getInstance().getContentResolver()
                    .openInputStream(aUri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, readOpt);
            input.close();
            if (aUri.toString().contains("file://")) {
                int degree = getExifOrientation(aUri.getPath());
                Bitmap target = rotate(bitmap, degree);
                return target;
            } else {
                return bitmap;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    static public Bitmap getFitableBitmap(int aBaseW, int aBaseH, String aFile) {
        try {
            InputStream input = ZwyContextKeeper.getInstance()
                    .getContentResolver()
                    .openInputStream(Uri.fromFile(new File(aFile)));
            Options opt = ZwyImageUtils.getBitmapOptions(input);
            input.close();
            int scale = opt.outWidth * 100 / aBaseW;
            Options readOpt = new Options();
            readOpt.inSampleSize = scale / 100;
            readOpt.inPreferredConfig = Bitmap.Config.RGB_565;
            input = new FileInputStream(aFile);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, readOpt);
            input.close();

            int degree = getExifOrientation(aFile);
            if (degree != 0) {
                Bitmap target = rotate(bitmap, degree);
                return target;
            }

            return bitmap;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;

                left = 0;
                top = 0;
                right = width;
                bottom = width;

                height = width;

                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;

                float clip = (width - height) / 2;

                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;

                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }

            Bitmap output = Bitmap
                    .createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right,
                    (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top,
                    (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);// 设置画笔无锯齿

            canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

            // 以下有两种方法画圆,drawRounRect和drawCircle
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
            // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
            canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

            return output;
        }
        return null;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    /**
     * 从asset中加载图片
     *
     * @param context
     * @param aName
     * @return
     * @throws Exception
     * @author ForLyp
     */
    public static Drawable loadImageFromAssets(String aName) throws Exception {
        InputStream ins = ZwyContextKeeper.getInstance().getAssets()
                .open(aName);

        Drawable res = new BitmapDrawable(ins);
        ins.close();
        return res;

    }

    /**
     * 从asset中加载图片
     *
     * @param context
     * @param aName
     * @return
     * @throws Exception
     * @author ForLyp
     */
    public static Bitmap loadBitmapImageFromAssets(String aName)
            throws Exception {
        InputStream ins = ZwyContextKeeper.getInstance().getAssets()
                .open(aName);

        Bitmap res = BitmapFactory.decodeStream(ins);
        ins.close();
        return res;

    }

    public static void saveMyBitmap(Bitmap mBitmap, String path)
            throws IOException {
        if (mBitmap == null) {
            ZwyCommon.showToast("图片保存失败");
            return;
        }
        File f = new File(path);
        if (f.exists() && f.isFile()) {
            f.delete();
        }
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能说明：byte数组转化成Bitmap
     *
     * @param temp
     * @return Bitmap
     * @author liumc
     * @since 2013-5-7 下午3:39:02
     */

    public static Bitmap getBitmapFromByte(byte[] temp) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
        return bitmap;
    }

    /**
     * 功能说明：Bitmap转化成byte数组
     *
     * @param bitmap
     * @param parame -->后缀名
     * @return byte[]
     * @author liumc
     * @since 2013-5-7 下午3:39:47
     */

    public static byte[] getBitmapByte(Bitmap bitmap, String parame) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            if (parame.equals("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
        } catch (NullPointerException e) {
            return null;
        }
        return out.toByteArray();
    }

    /**
     * 圆角图片
     *
     * @param bitmap
     * @return
     * @author ForLyp
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        return getRoundedCornerBitmap(bitmap, 20);
    }

    /**
     * 圆角图标 指定弧度
     *
     * @param bitmap
     * @param roundPx
     * @return
     * @author ForLyp
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap != null)
            return getRoundedCornerBitmap(bitmap, bitmap.getWidth(),
                    bitmap.getHeight(), 20);
        return null;
    }

    /**
     * 指定弧度，指定宽高的圆角
     *
     * @param bitmap
     * @param roundPx
     * @param width
     * @param height
     * @return
     * @author ForLyp
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx,
                                                int width, int height) {
        if (bitmap == null) {
            return null;
        }
        Bitmap bm = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap output = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(
                android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, rect, rect, paint);

        return output;
    }

    /**
     * 使用BitmapFactory.Options的inSampleSize参数来缩放
     *
     * @param bitmap
     * @param h
     * @param w
     * @return
     */

    @SuppressWarnings("deprecation")
    public static Drawable resizeImage2(String path, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不加载bitmap到内存中
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            Log.d("图片缩放", "sampleSize = " + sampleSize);
            options.inSampleSize = sampleSize;
        }

        options.inJustDecodeBounds = false;
        return new BitmapDrawable(BitmapFactory.decodeFile(path, options));
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     * @author ForLyp
     */
    public static Bitmap CompressionBigBitmap(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 文件转化图片  tate
     *
     * @param pathName
     * @param rate     压缩倍数
     * @return
     * @author ForLyp
     */
    public static Bitmap getBitmapByPath(String pathName, int rate) {
        // 读取文件到输入流
        FileInputStream fis = null;
        BitmapFactory.Options options = null;
        try {
            fis = new FileInputStream(pathName);
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = rate;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 使用位图工厂类将包含图片信息的输入流转换成位图
        return BitmapFactory.decodeStream(fis, null, options);
    }

    /**
     * 从资源中加载bitmap
     *
     * @param resId
     * @return
     * @author ForLyp
     */
    public static Bitmap readBitMap(int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = ZwyContextKeeper.getInstance().getResources()
                .openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

}
