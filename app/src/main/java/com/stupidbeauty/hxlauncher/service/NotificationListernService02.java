package com.stupidbeauty.hxlauncher.service;

import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.asynctask.VoiceShortcutAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import android.service.notification.StatusBarNotification;
import com.stupidbeauty.codeposition.CodePosition;
import android.os.ParcelFileDescriptor;
import java.io.FileOutputStream;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.NotificationChannel;
import android.os.IBinder;
import android.util.Log;
import com.google.protobuf.InvalidProtocolBufferException;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.stupidbeauty.exist.ExistMessageContainer;
import com.stupidbeauty.lanime.Constants;
import com.stupidbeauty.shutdownat2100.Sda2Message;
import android.service.notification.NotificationListenerService;
import com.stupidbeauty.hxlauncher.manager.NotificationDataManager;

public class NotificationListernService02 extends NotificationListenerService
{
  private Notification continiusNotification=null; //!<记录的通知
  private int NOTIFICATION = 163731;

  private long lastPublishTimestamp=0; //!<记录的上次发布局域网服务的时间戳。
  private static final String TAG = "NotificationListernService02"; //!< 输出调试信息时使用的标记。
  private static final String LanServiceName = "com.stupidbeauty.shutdownat2100.android"; //!<局域网服务名字。
  private static final int LanServicePort = 9521; //!<局域网服务的端口号。
  
  public IBinder onBind(Intent intent)
  {
    Log.d(TAG, CodePosition.newInstance().toString() + ", intent: " + intent); // Debug.
    return super.onBind(intent);
  } // public IBinder onBind(Intent intent)

  /**
  * Notification posted.
  */
  public void onNotificationPosted(StatusBarNotification sbn)
  {
    StatusBarNotification[] notificatoins = getActiveNotifications(); // Get the active notifications.
    
    Log.d(TAG, CodePosition.newInstance().toString() + ", notifications amount: " + notificatoins.length); // Debug.
    
    countNotifications(); // Count notificaitons.
  } // public void onNotificationPosted(StatusBarNotification sbn)

	@Override
	/**
	 * get to know about notification listener connected.
	 */
	public void onListenerConnected()
	{
    StatusBarNotification[] notificatoins = getActiveNotifications(); // Get the active notifications.
    
    Log.d(TAG, CodePosition.newInstance().toString() + ", notifications amount: " + notificatoins.length); // Debug.
    countNotifications(); // Count notificaitons.
  } //private void listenClipboard()
  
  public void onNotificationRemoved (StatusBarNotification sbn)
  {
    countNotifications(); // Count notificaitons.
  } // public void onNotificationRemoved (StatusBarNotification sbn)
  
  /**
  * Count notificaitons.
  */
  private void countNotifications()
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //获取应用程序对象。
    NotificationDataManager notificationDataManager = baseApplication.getNotificationDataManager(); // Get the notificaiton data manager.
    
    StatusBarNotification[] notificatoins = getActiveNotifications(); // Get the active notifications.
    notificationDataManager.countNotifications(notificatoins); // Count notificaitons.
  } // private void countNotifications()
}
