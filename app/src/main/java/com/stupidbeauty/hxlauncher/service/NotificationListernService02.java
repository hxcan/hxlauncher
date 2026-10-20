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
import com.stupidbeauty.shutdownat2100.helper.ShutDownAt2100Manager;
import com.stupidbeauty.hxlauncher.logic.ShutDownAt2100Logic;
import com.stupidbeauty.hxlauncher.interfaces.ShutDownAt2100LogicInterface;

public class NotificationListernService02 extends NotificationListenerService
{
  private Notification continiusNotification=null; //!<记录的通知
  private int NOTIFICATION = 163731;

  private long lastPublishTimestamp=0; //!<记录的上次发布局域网服务的时间戳。
  private static final String TAG = "NotificationListernService02"; //!< 输出调试信息时使用的标记。
  private static final String LanServiceName = "com.stupidbeauty.shutdownat2100.android"; //!<局域网服务名字。
  private static final int LanServicePort = 9521; //!<局域网服务的端口号。

  private ShutDownAt2100Logic shutDownAt2100Logic=null; //!< 21点关机逻辑。通知触发时检查是否到关机时间。

  /**
   * Stub implementation of ShutDownAt2100LogicInterface for use in NotificationListenerService.
   * Service 里不需要触发 APK 下载流程（这是 LauncherActivity 的事）。
   */
  private static class ServiceShutDownAt2100LogicInterface implements ShutDownAt2100LogicInterface
  {
    @Override
    public boolean requestDownloadApk(String foundPackageName)
    {
      return false; //!< 在 Service 上下文中不触发 APK 下载。
    }
  } //public boolean requestDownloadApk(String foundPackageName)

  @Override
  public void onCreate()
  {
    super.onCreate();
    Log.d(TAG, CodePosition.newInstance().toString() + ", onCreate"); // Debug.

    shutDownAt2100Logic=new ShutDownAt2100Logic(new ServiceShutDownAt2100LogicInterface()); //!< 初始化21点关机逻辑。
  } //public void onCreate()

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

    // 通知变动时，触发21点关机检查。
    if (shutDownAt2100Logic!=null) // Logic exists.
    {
      shutDownAt2100Logic.checkShutDownTime(); // Check shut down time.
    } //if (shutDownAt2100Logic!=null) // Logic exists.
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

    // 监听连接建立时，也触发一次21点关机检查。
    if (shutDownAt2100Logic!=null) // Logic exists.
    {
      shutDownAt2100Logic.checkShutDownTime(); // Check shut down time.
    } //if (shutDownAt2100Logic!=null) // Logic exists.
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
