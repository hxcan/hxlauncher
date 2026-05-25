package com.stupidbeauty.hxlauncher.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.CheckBox;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.activity.AccountActivity;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.rotatingactiveuser.RotatingActiveUserClient; // RotatingActiveUserClient
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication; // HxLauncherApplication
import com.stupidbeauty.hxlauncher.Constants; // Constants.
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import com.stupidbeauty.codeposition.CodePosition;
import android.os.ParcelFileDescriptor;
import java.io.FileOutputStream;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;

public class NotificationDataManager
{
  private RotatingActiveUserClient rotatingActiveUserClient=null; //!< 滚动活跃用户统计客户端。陈欣。
  private HashMap<String, Integer> packagenNotificationAmountMap = new HashMap<>(); //!< The map of package name to notification amount.
  private static final String TAG = "NotificationDataManager"; //!< 输出调试信息时使用的标记。

  /**
  * 开始报告活跃用户。
  */
  public void startReportActiveUser() 
  {
    if (rotatingActiveUserClient==null)
    {
      Context context=HxLauncherApplication.getAppContext(); // 获取应用程序上下文。
      rotatingActiveUserClient=new RotatingActiveUserClient(context); // 创建客户端对象。
    } //if (rotatingActiveUserClient==null)

    rotatingActiveUserClient.reportActiveUser(); // 报告活跃用户。
  } //public void StartReportActiveUser()
  
  /**
  * Get the notification amount.
  */
  public int getNotificationAmount( String packageName )
  {
    Integer countValue = packagenNotificationAmountMap.get(packageName);
    int origianlCopuint = 0;
    
    if (countValue != null) // The value exists
    {
      origianlCopuint = countValue;
    } // if (countValue != null) // The value exists

    return origianlCopuint;
  } // public int getNotificationAmount( String holderpackageName )
    
  /**
  * Count notificaitons.
  */
  public void countNotifications(StatusBarNotification [] notificatoins)
  {
    // Chen xin.
    
    packagenNotificationAmountMap.clear(); // clearn the map.
    
    for(StatusBarNotification currentNotification: notificatoins) // Count one by one
    {
      String groupKey = currentNotification.getGroupKey(); // Get the group key.
      
      Log.d(TAG, CodePosition.newInstance().toString() + ", notification: " + currentNotification + ", group key: " + groupKey); // Debug.
      
      if (( currentNotification.getNotification().flags & Notification.FLAG_GROUP_SUMMARY) == 0) // Not a group summary.
      {
        // Chen xin.
        String packageName = currentNotification.getPackageName(); // Get the package name.

        int origianlCopuint = getNotificationAmount(packageName); // Get the existing notifictaion amount.

        origianlCopuint = origianlCopuint + 1;
        
        packagenNotificationAmountMap.put(packageName, origianlCopuint);
      } // if (groupKey == null) // Not a group container.
    } // for(StatusBarNotification currentNotification: notificatoins) // Count one by one
    
    broadcastNotificationChange(); // Send broad cast about notificaiton change.
  } // public void countNotifications(StatusBarNotification [] notificatoins)
  
  /**
  * Send broad cast about notificaiton change.
  */
  private void broadcastNotificationChange()
  {
    // Chen xin.
    
    Intent intent4Wrk = new Intent(); //创建意图对象。晚安诸位。

    intent4Wrk.setAction(Constants.NativeMessage.NotificationBadgeChanged); // Notification badge changed.


    Context appContext= HxLauncherApplication.getAppContext(); //获取应用程序上下文。
    LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(appContext); //Get the local broadcast manger instance.

    lclBrdcstMngr.sendBroadcast(intent4Wrk); //发送广播。

  } // private void broadcastNotificationChange()
}
