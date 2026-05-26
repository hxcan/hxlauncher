package com.stupidbeauty.grebe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.app.Application;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.app.Application;
import android.os.Looper;
import java.util.Timer;
import java.util.TimerTask;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.database.ContentObserver;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import com.stupidbeauty.hxlauncher.R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import com.stupidbeauty.hxlauncher.R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService;
import com.stupidbeauty.hxlauncher.rpc.DownloadResult;
import com.stupidbeauty.hxlauncher.rpc.DownloadListener;
import org.apache.commons.io.FileUtils;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import com.stupidbeauty.hxlauncher.rpc.RecognizerResult;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import com.stupidbeauty.hxlauncher.utils.FileLogger;

public class DownloadRequestor
{
  private Timer timerObj = null; //!< The timer of cancelling download when no progress for a long time.
private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";

  private Notification continiusNotification=null; //!<记录的通知
  private LauncherActivity launcherActivity=null; //!< 启动活动。
  private int NOTIFICATION = 84951; //!< 通知编号。陈欣
  private VoiceUi voiceUi=null; //!< 语音交互对象。

  private String packageName=null; //!< 包名。
  private String fullUrl = null; //!< Downloading full url.

  public Future<File> fileDownloadFuture; //!<The file download future.
  private NotificationManager mNM;

  private static final String TAG="DownloadRequestor"; //!<输出调试信息了时使用的标记

  private CloudRequestorZzaqwb cloudRequestorZzaqwb=new CloudRequestorZzaqwb(); //!<云端请求发送器

  private long downloadId; //!<当前的下载编号

  private void  rememberApkFile(String apkFilePath) 
  {
    if (packageName!=null)
    {
      HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

      HashMap<String, String> apkFilePathMap=baseApplication.getApkFilePathMap(); //!< 获取 APK 安装包路径映射。
            
      Log.d(TAG, "rememberApkFile, package name: " + packageName + ", apk file path: " + apkFilePath); //!< Debug.
      FileLogger.d(TAG, "rememberApkFile: " + packageName);
            
      apkFilePathMap.put(packageName, apkFilePath); //!< 加入映射中。
            
      baseApplication.saveApkFilePathMap(); //!< 保存映射。
    }
  }

  private void stopDownloadNotificationService() 
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    Intent serviceIntent = new Intent(baseApplication, DownloadNotificationService.class); //!<创建意图。

    baseApplication.stopService(serviceIntent);
  }
    
  private void requestInstall(String downloadFilePath)
  {
    FileLogger.d(TAG, "requestInstall: " + downloadFilePath);
    notifyDownloadFinished();
    requestInstallApi(downloadFilePath);
  }

  private void notifyDownloadFinished() 
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    ApplicationListData applicationListData = baseApplication.getApplicationListData(); //!<获取本地服务器列表数据对象。
    applicationListData.removeUrl(fullUrl);
  
    if (launcherActivity!=null)
    {
      launcherActivity.reportDownloadFinished(packageName);
    }

    packageName=null;
    fullUrl=null;
  }
  
  private void addApkToInstallSession(String assetName, PackageInstaller.Session session) throws IOException 
  {
    OutputStream packageInSession = session.openWrite("package", 0, -1);
        
    File apkFileObject=new File(assetName);
            
    FileUtils.copyFile(apkFileObject, packageInSession); //!< Copy to output stream.

    packageInSession.close();
  }

  private void requestInstallApi(String downloadFilePath)
  {
    FileLogger.d(TAG, "requestInstallApi: " + downloadFilePath);
    
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    PackageInstaller.Session session = null;
    try 
    {
      PackageInstaller packageInstaller = baseApplication.getPackageManager().getPackageInstaller();
      PackageInstaller.SessionParams params = new PackageInstaller.SessionParams( PackageInstaller.SessionParams.MODE_FULL_INSTALL);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) //!< USER_ACTION_NOT_REQUIRED
      {
        params.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED); //!< Silent update.
      }

      int sessionId = packageInstaller.createSession(params);
      session = packageInstaller.openSession(sessionId);

      addApkToInstallSession(downloadFilePath, session);

      Intent intent = new Intent(baseApplication, LauncherActivity.class);
      intent.setAction(PACKAGE_INSTALLED_ACTION);

      PendingIntent pendingIntent = PendingIntent.getActivity(baseApplication, 0, intent, PendingIntent.FLAG_MUTABLE);

      IntentSender statusReceiver = pendingIntent.getIntentSender();

      session.commit(statusReceiver);
      
      session.close();
    }
    catch (IOException e) 
    {
      FileLogger.e(TAG, "requestInstallApi IOException: " + e.getMessage());
      e.printStackTrace();
    }
    catch (RuntimeException e) 
    {
      if (session != null) 
      {
        session.abandon();
      }
      e.printStackTrace();
    }

    String mWordSeparators = baseApplication.getResources().getString(R.string.prepareInstall); //!< 读取 说明 字符串。Prepare install

    voiceUi.say(mWordSeparators); //!< 说话，prepare install

    stopDownloadNotificationService();
    
    FileLogger.d(TAG, "requestInstallApi done");
  }

  private void requestInstallView(String downloadFilePath)
  {
    FileLogger.d(TAG, "requestInstallView: " + downloadFilePath);
    
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
        
    String type = "application/vnd.android.package-archive";

    File file=new File(downloadFilePath);
        
    Intent intent = new Intent(Intent.ACTION_VIEW);
      
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) 
    {
      try
      {
          String applicationPackageName = baseApplication.getPackageName();
        Uri downloadedApk = FileProvider.getUriForFile(baseApplication, applicationPackageName + ".com.stupidbeauty.upgrademanager.fileprovider", file);
          
        intent.setClipData(ClipData.newRawUri("", downloadedApk));
          
        intent.setDataAndType(downloadedApk, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      }
      catch(IllegalArgumentException e)
      {
        e.printStackTrace();
        
        intent.setDataAndType(Uri.fromFile(file), type);
      }
    }
    else 
    {
      intent.setDataAndType(Uri.fromFile(file), type);
    }

    Log.d(TAG, "requestInstall, starting activity to install apk"); //!< Debug.

    String mWordSeparators = baseApplication.getResources().getString(R.string.prepareInstall); //!< 读取 说明 字符串。Prepare install

    voiceUi.say(mWordSeparators); //!< 说话，prepare install

    baseApplication.startActivity(intent);
        
    stopDownloadNotificationService();
    
    FileLogger.d(TAG, "requestInstallView done");
  }

  public DownloadRequestor() 
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    
    voiceUi=new VoiceUi(baseApplication); //!< 创建语音交互对象。

    cloudRequestorZzaqwb.setContext(baseApplication); //!<设置上下文
      
    DownloadManager.Query q=new DownloadManager.Query(); //!< 构造查询对象。
      
    final DownloadManager downloadManager = (DownloadManager)baseApplication.getSystemService(Context.DOWNLOAD_SERVICE); //!< 获取下载管理器对象。
    baseApplication.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"),true, new ContentObserver(null) 
    {
      @Override
      public void onChange(boolean selfChange) 
      {
        super.onChange(selfChange);
        Cursor localCursor = downloadManager.query( q);
        if (localCursor.getCount() == 0) 
        {
          localCursor.close();
        }
        localCursor.moveToFirst();
        do 
        {
          if ((localCursor.getInt(localCursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) & DownloadManager.STATUS_FAILED )!=0) 
          {
            if (localCursor.getInt(localCursor.getColumnIndex(DownloadManager.COLUMN_REASON)) == DownloadManager.ERROR_INSUFFICIENT_SPACE)
            {
              Log.w("DownloadStatus", " Download failed with ERROR_INSUFFICIENT_SPACE");
              FileLogger.w(TAG, "ERROR_INSUFFICIENT_SPACE");
                                    
              long currentId=localCursor.getLong(localCursor.getColumnIndex(DownloadManager.COLUMN_ID));

              if (currentId==downloadId)
              {
                notifyDownloadFail();
              }
            }
          }
        }
        while (localCursor.moveToNext());
      }
    });
    
    FileLogger.d(TAG, "DownloadRequestor created");
  }

  private void showNotification(String contentText)
	{
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
		CharSequence text = baseApplication.getText(R.string.app_name);

		PendingIntent contentIntent = PendingIntent.getActivity(baseApplication, 0, new Intent(baseApplication, LauncherActivity.class), PendingIntent.FLAG_MUTABLE);

		String downloadingText="Downloading " + contentText; //!< 构造字符串正在下��。陈欣。

		Notification notification = new Notification.Builder(baseApplication)
      .setSmallIcon(R.drawable.ic_launcher)
      .setTicker(text)
      .setWhen(System.currentTimeMillis())
      .setContentTitle(baseApplication.getText(R.string.app_name))
      .setContentText(downloadingText)
      .setContentIntent(contentIntent)
      .setPriority(Notification.PRIORITY_HIGH)
      .build();

		continiusNotification=notification;

    mNM = (NotificationManager)baseApplication.getSystemService(Context.NOTIFICATION_SERVICE);

		mNM.notify(NOTIFICATION, notification);
	}

  private void notifyDownloadProgress(long downloaded, long total) 
  {
    if (launcherActivity!=null)
    {
      Handler uiHandler = new Handler(Looper.getMainLooper());

      Runnable runnable= new Runnable()
      {
        public void run()
        {
          launcherActivity.reportDownloadProgress(packageName, downloaded, total);
        }
      };

      uiHandler.post(runnable);
    }
  }
  
  private void  notifyDownloadFail() 
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    ApplicationListData applicationListData = baseApplication.getApplicationListData(); //!<获取本地服务器列表数据对象。
    applicationListData.removeUrl(fullUrl);
  
    String contentText="Failed to download";
      
    showNotification(contentText);
    
    if (launcherActivity!=null)
    {
      launcherActivity.reportDownloadFailed(packageName);
    }
    
    packageName=null;
    fullUrl=null;
  }
    
  public void requestDownloadUrl(Uri uri, String refererUrl, String applicationName, String packageName)
  {
    FileLogger.d(TAG, "requestDownloadUrl: " + uri);
    
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    HashMap<String, String> apkFilePathMap=baseApplication.getApkFilePathMap(); //!< 获取 APK 安装包路径映射。

    String apkFilePath=apkFilePathMap.get(packageName); //!< 获取安装包文件路径。
        
    Log.d(TAG, "requestDownloadUrl, apkFilePath: " + apkFilePath + ", package name: " + packageName+ ", map length: " + apkFilePathMap.size());
        
    boolean shouldDownload=false;
        
    if (apkFilePath!=null)
    {
      File apkFile=new File(apkFilePath);
        
      if (apkFile.exists())
      {
        boolean isApkFile=checkIsApkFile(apkFilePath);
      
        if (isApkFile)
        {
          requestInstall(apkFilePath);
        }
        else
        {
          shouldDownload=true;
        }
      }
      else
      {
        shouldDownload=true;
      }
    }
    else
    {
      shouldDownload=true;
    }

    if (shouldDownload)
    {
      Log.d(TAG, "requestDownloadUrl, 347, download file path: " + uri);

      ApplicationListData applicationListData = baseApplication.getApplicationListData(); //!<获取本地服务器列表数据对象。

      fullUrl = uri.toString();

      if (applicationListData.containsUrl(fullUrl))
      {
        Log.d(TAG, "requestDownloadUrl, 357, already downloading: " + uri+", ignore");
      }
      else
      {
        applicationListData.addUrl(fullUrl);

        Log.d(TAG, "requestDownloadUrl, url scheme: " + uri.getScheme());
        
        String mWordSeparators = baseApplication.getResources().getString(R.string.startDownload);
    
        voiceUi.say(mWordSeparators);

        downloadByIon(uri);
      }
    }
  }

  private boolean checkIsApkFile(String apkFilePath) 
  {
    boolean result=false;

    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    
    PackageManager packageManager = baseApplication.getPackageManager();
    
    PackageInfo packageInfo=packageManager.getPackageArchiveInfo(apkFilePath, 0);
    
    String expectedVersionName = baseApplication.getAvailableVersionName(packageName); //!< 获取可用的版本名字。

    if (packageInfo!=null)
    {
      String versionName=packageInfo.versionName;

      Log.d(TAG, "checkIsApkFile, version name: " + versionName + "/" + expectedVersionName);

      if (versionName.equals(expectedVersionName))
      {
        result=true;
      }
      else
      {
        if (packageName==null)
        {
          result=true;
        }
      }
    }

    return result;
  }

  private void downloadByIon(Uri uri)
  {
    String targetUrl=uri.toString();

    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    String fileName=uri.getLastPathSegment();
    
    if (packageName!=null)
    {
      fileName=packageName+".apk";
    }
        
    Context context = HxLauncherApplication.getAppContext();

    File downloadFolder = baseApplication.getExternalCacheDir();

    String wholePath =downloadFolder.getPath()+ File.separator  + fileName;
    
    Log.d(TAG, "downloadByIon, 463, url: " + targetUrl + ", path: " + wholePath);

    fileDownloadFuture= Ion.with(baseApplication)
      .load(targetUrl)
      .setTimeout(15000)
      .progress(new ProgressCallback() 
      {
        @Override
        public void onProgress(long downloaded, long total) 
        {
          timerObj.cancel();
          Log.d(TAG, "downloadByIon, 470, progress: " + downloaded + "/" + total + ", " + targetUrl);
          notifyDownloadProgress(downloaded, total);
        }
      })
      .setLogging(TAG, Log.DEBUG)
      .write(new File( wholePath));
      
      fileDownloadFuture.setCallback(new FutureCallback<File>() 
      {
        @Override
        public void onCompleted(Exception e, File file) 
        {
          if (e!=null)
          {
            Toast.makeText(baseApplication, "Download Failed" + targetUrl, Toast.LENGTH_SHORT).show();

            Log.d(TAG,"download error:");
            e.printStackTrace();
                            
            notifyDownloadFail();
          }
          else
          {
            FileLogger.d(TAG, "download completed: " + wholePath);
            Toast.makeText(baseApplication, "Download Completed" + wholePath, Toast.LENGTH_SHORT).show();

            rememberApkFile(wholePath);
                            
            if (checkIsApkFile(wholePath))
            {
              requestInstall(wholePath);
            }
            else
            {
              notifyDownloadFail();
            }
          }
        }
      });
      
      startTimeoutCancelTimer();
  }
  
  private void startTimeoutCancelTimer() 
  {
    timerObj = new Timer();
    TimerTask timerTaskObj = new TimerTask() 
    {
      public void run() 
      {
        Handler uiHandler = new Handler(Looper.getMainLooper());

        Runnable runnable= new Runnable()
        {
          public void run()
          {
            cancelDownload();
          }
        };

        uiHandler.post(runnable);
      }
    };
    timerObj.schedule(timerTaskObj, 15000);
  }
  
  public void cancelDownload()
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    Ion.getDefault(baseApplication).cancelAll(baseApplication);
  }


    public void requestDownloadUrl(String url, String refererUrl, String applicatinName, String packageName, LauncherActivity launcherActivity)
    {
      this.launcherActivity=launcherActivity;
      this.packageName=packageName;
      
      String fixedUrl=url;
      
      if (fixedUrl.startsWith("http"))
      {
      }
      else
      {
        fixedUrl="http://"+fixedUrl;
      }
    
      Uri uri=Uri.parse(fixedUrl);

      requestDownloadUrl(uri, refererUrl, applicatinName, packageName);
    }

    public void requestDownloadUrl(String url, String refererUrl, String applicatinName, String packageName)
    {
      requestDownloadUrl(url, refererUrl, applicatinName, packageName, null);
    }

}