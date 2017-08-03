package com.zwy.base;

import java.io.File;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.zwy.app.ZwyContextKeeper;
import com.zwy.common.util.ZwyOSInfo;
import com.zwy.tool.R;

/**
 * 状态栏消息管理
 * 
 * @author Administrator
 * 
 */
public class ZwyNotificationsManager {
	public static int DOWNLOAD_FLAG = 1000;
	public static int DIARY_UPDATE_FLAG = 2000;
	public static int MESSAGE_FLAG = 3000;
	public static int AUCTION_MESSAGE_FLAg = 4000;
	public static int DOWNLOAD_FILE_FLAG = 5000;

	public static void showMessageNotification(Context context, int icon,String name,
			String content, Class toClass) {
        showMessageNotification(context, icon, name, content ,toClass, null);
	}

    public static void showMessageNotification(Context context, int icon,String name,
			String content, Class toClass, Bundle bundle) {
		NotificationManager mNotificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationMgr.cancel(MESSAGE_FLAG);
		Notification n = new Notification();
		n.tickerText = name;
		n.when = System.currentTimeMillis();
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		//没有则不显示 只有声音
		n.icon = icon;
		Date date = new Date();
		if (date.getHours() > 8 && date.getHours() < 22)
			n.defaults = Notification.DEFAULT_ALL;
		PendingIntent pintent = null;
		Intent in = new Intent();
		// 点击要加载的activity
		in.setClass(context, toClass);
        if(bundle != null){
            in.putExtras(bundle);
        }
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String nitofication = content;

		pintent = PendingIntent.getActivity(context, 0, in,
				PendingIntent.FLAG_UPDATE_CURRENT);
		n.contentIntent = pintent;
		n.setLatestEventInfo(context, name, nitofication, pintent);
		mNotificationMgr.notify(MESSAGE_FLAG, n);
	}

	public static void closeNotification(final Context context, int id) {
		try {
			NotificationManager mNotificationMgr = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationMgr.cancel(id);
		} catch (Exception e) {

		}
	}

	public static void showDownLoadNotification(Context context, int icon,
			String name, int progress) {
		NotificationManager mNotificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (progress == 100)
			mNotificationMgr.cancel(DOWNLOAD_FLAG);
		Notification n = new Notification();
		n.tickerText = "下载提示";
		n.when = System.currentTimeMillis();
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.icon = icon;
		Date date = new Date();
		if ((progress == 1 || progress == 100) && date.getHours() > 8
				&& date.getHours() < 22)
			n.defaults = Notification.DEFAULT_SOUND;
		//
		PendingIntent pintent = null;
		Intent in = new Intent();

		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String nitofication = "";
		if (progress < 100) {
			n.contentView = new RemoteViews(context.getPackageName(),
					R.layout.download_notification);
			n.contentView.setProgressBar(R.id.myProcessBar, 100, progress,
					false);
			n.contentView.setTextViewText(R.id.title, ZwyOSInfo.getAppName());
			n.contentView.setImageViewResource(R.id.notify_icon, icon);
			nitofication = "正在下载";
		} else {
			nitofication = "软件" + "下载完成，点击安装";

			File f = new File(ZwyContextKeeper.getSDCardPath() + name);
			if (f.exists() && f.isFile()) {
				in.setAction(android.content.Intent.ACTION_VIEW);
				/* 调用getMIMEType()来取得MimeType */
				String type = "application/vnd.android.package-archive";
				/* 设置intent的file与MimeType */
				in.setDataAndType(Uri.fromFile(f), type);
			}
		}

		pintent = PendingIntent.getActivity(context, 0, in,
				PendingIntent.FLAG_UPDATE_CURRENT);
		n.contentIntent = pintent;

		if (progress >= 100)
			n.setLatestEventInfo(context, ZwyOSInfo.getAppName(), nitofication,
					pintent);
		mNotificationMgr.notify(DOWNLOAD_FLAG, n);
	}

	public static void showDownLoadFileNotification(Context context, int flag,
			int icon, String name, int progress) {
		NotificationManager mNotificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (progress == 100)
			mNotificationMgr.cancel(flag);
		Notification n = new Notification();
		n.tickerText = "下载提示";
		n.when = System.currentTimeMillis();
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.icon = icon;
		Date date = new Date();
		if ((progress == 1 || progress == 100) && date.getHours() > 8
				&& date.getHours() < 22)
			n.defaults = Notification.DEFAULT_SOUND;
		//
		PendingIntent pintent = null;
		Intent in = new Intent();

		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String nitofication = "";
		if (progress < 100) {
			n.contentView = new RemoteViews(context.getPackageName(),
					R.layout.download_notification);
			n.contentView.setProgressBar(R.id.myProcessBar, 100, progress,
					false);
			n.contentView.setTextViewText(R.id.title, name);
			n.contentView.setImageViewResource(R.id.notify_icon, icon);
			nitofication = name + "正在下载";
		} else {
			nitofication = name + "下载完成";
			File f = new File(ZwyContextKeeper.getSDCardPath() + name);
			if (f.exists() && f.isFile()) {
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				in.setAction(android.content.Intent.ACTION_VIEW);
				Uri uri = Uri.fromFile(f);
				String type = ZwyOpenFileUtil.getMIMEType(name);
				in.setDataAndType(uri, type);
			}
		}

		pintent = PendingIntent.getActivity(context, 0, in,
				PendingIntent.FLAG_UPDATE_CURRENT);
		n.contentIntent = pintent;

		if (progress >= 100)
			n.setLatestEventInfo(context, ZwyOSInfo.getAppName(), nitofication,
					pintent);
		mNotificationMgr.notify(flag, n);
	}

}
