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
import java.io.IOException;
import android.app.Notification;
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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.hxlauncher.rpc.RecognizerResult;

import org.apache.commons.io.FilenameUtils;

// FileLogger 用于外置存储日志
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

  /**
  * 记录安装包路径。
  */
  private void  rememberApkFile(String apkFilePath) 
  {
    if (packageName!=null)
    {
      HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

      HashMap<String, String> apkFilePathMap=baseApplication.getApkFilePathMap(); //!< 获取 APK 安装包路径映射。
            
      Log.d(TAG, "rememberApkFile, package name: " + packageName + ", apk file path: " + apkFilePath); //!< Debug.
      FileLogger.d(TAG, "rememberApkFile, package name: " + packageName + ", apk file path: " + apkFilePath);
            
      apkFilePathMap.put(packageName, apkFilePath); //!< 加入映射中。
            
      baseApplication.saveApkFilePathMap(); //!< 保存映射。
    } //!<if (packageName!=null)
  } //!<private void  rememberApkFile(String apkFilePath)

  /**
  * 停止，下载通知服务。
  */
  private void stopDownloadNotificationService() 
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    Intent serviceIntent = new Intent(baseApplication, DownloadNotificationService.class); //!<创建意图。

    baseApplication.stopService(serviceIntent);
  } //!<private void stopDownloadNotificationService()
    
  /**
  * 要求安装应用
  * @param downloadFilePath 应用安装包路径
  */
  private void requestInstall(String downloadFilePath)
  {
    FileLogger.d(TAG, "requestInstall called, path: " + downloadFilePath);
    notifyDownloadFinished(); //!< Notify download finished.
    requestInstallApi(downloadFilePath); //!< Request install by view.
  } //!<private void requestInstall(String downloadFilePath)

  /**
  * Notify download finished.
  */
  private void notifyDownloadFinished() 
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    ApplicationListData applicationListData = baseApplication.getApplicationListData(); //!<获取本地服务器列表数据对象。
    applicationListData.removeUrl(fullUrl); //!< Remove the download url.
  
    if (launcherActivity!=null)
    {
      launcherActivity.reportDownloadFinished(packageName); //!< 报告，下载 finished。
    }

    packageName=null; //!< Clear package name.
    fullUrl=null; //!< Clear download full url.
  } //!< private void notifyDownloadFinished() //!< Notify download finished.
  
  private void addApkToInstallSession(String assetName, PackageInstaller.Session session) throws IOException 
  {
    FileLogger.d(TAG, "addApkToInstallSession called, path: " + assetName);
    
    OutputStream packageInSession = session.openWrite("package", 0, -1);
        
    File apkFileObject=new File(assetName);
            
    FileUtils.copyFile(apkFileObject, packageInSession); //!< Copy to output stream.

    packageInSession.close();
    
    FileLogger.d(TAG, "addApkToInstallSession completed");
  } //!< private void addApkToInstallSession(String assetName, PackageInstaller.Session session) throws IOException 

  /**
  * 要求安装应用（使用 Session API）
  * @param downloadFilePath 应用安装包路径
  */
  private void requestInstallApi(String downloadFilePath)
  {
    FileLogger.d(TAG, "requestInstallApi called, path: " + downloadFilePath);
    
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    PackageInstaller.Session session = null;
    try 
    {
      FileLogger.d(TAG, "Getting PackageInstaller...");
      PackageInstaller packageInstaller = baseApplication.getPackageManager().getPackageInstaller();
      
      FileLogger.d(TAG, "Creating session params...");
      PackageInstaller.SessionParams params = new PackageInstaller.SessionParams( PackageInstaller.SessionParams.MODE_FULL_INSTALL);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) //!< USER_ACTION_NOT_REQUIRED
      {
        FileLogger.d(TAG, "Setting USER_ACTION_NOT_REQUIRED for Android S+");
        params.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED); //!< Silent update.
      } //!<if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //!<动态权限

      FileLogger.d(TAG, "Creating session...");
      int sessionId = packageInstaller.createSession(params);
      FileLogger.d(TAG, "Session created with id: " + sessionId);
      
      session = packageInstaller.openSession(sessionId);
      FileLogger.d(TAG, "Session opened");

      addApkToInstallSession(downloadFilePath, session);

      FileLogger.d(TAG, "Creating install status receiver intent...");
      Intent intent = new Intent(baseApplication, LauncherActivity.class);
      intent.setAction(PACKAGE_INSTALLED_ACTION);

      PendingIntent pendingIntent = PendingIntent.getActivity(baseApplication, 0, intent, PendingIntent.FLAG_MUTABLE);

      IntentSender statusReceiver = pendingIntent.getIntentSender();
      FileLogger.d(TAG, "Committing session...");

      session.commit(statusReceiver);
      FileLogger.d(TAG, "Session committed successfully");
      
      session.close(); //!< Finish the sesison.
      FileLogger.d(TAG, "Session closed");
    }
    catch (IOException e) 
    {
      FileLogger.e(TAG, "IOException during installation: " + e.getMessage());
      e.printStackTrace(); //!< Report error.
    }
    catch (RuntimeException e) 
    {
      if (session != null) 
      {
        session.abandon();
        FileLogger.e(TAG, "Session abandoned due to RuntimeException");
      }
      e.printStackTrace(); //!< Report error.
      FileLogger.e(TAG, "RuntimeException during installation: " + e.getMessage());
    }

    String mWordSeparators = baseApplication.getResources().getString(R.string.prepareInstall); //!< 读取 说明 字符串。Prepare install

    voiceUi.say(mWordSeparators); //!< 说话，prepare install

    stopDownloadNotificationService(); //!< 停止，下载通知服务。
    
    FileLogger.d(TAG, "requestInstallApi completed");
  } //!<private void requestInstall(String downloadFilePath)

  /**
  * 要求安装应用（使用 View 方式）
  * @param downloadFilePath 应用安装包路径
  */
  private void requestInstallView(String downloadFilePath)
  {
    FileLogger.d(TAG, "requestInstallView called, path: " + downloadFilePath);
    
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
        
        FileLogger.d(TAG, "Using FileProvider URI: " + downloadedApk);
      }
      catch(IllegalArgumentException e)
      {
        e.printStackTrace();
        FileLogger.d(TAG, "IllegalArgumentException with FileProvider, falling back to Uri.fromFile");
        
        intent.setDataAndType(Uri.fromFile(file), type);
      } //!< catch(IllegalArgumentException e)
    }
    else 
    {
      intent.setDataAndType(Uri.fromFile(file), type);
      FileLogger.d(TAG, "Using Uri.fromFile for Android < N");
    }

    Log.d(TAG, "requestInstall, starting activity to install apk"); //!< Debug.
    FileLogger.d(TAG, "Starting install activity");

    String mWordSeparators = baseApplication.getResources().getString(R.string.prepareInstall); //!< 读取 说明 字符串。Prepare install

    voiceUi.say(mWordSeparators); //!< 说话，prepare install

    baseApplication.startActivity(intent);
        
    stopDownloadNotificationService(); //!< 停止，下载通知服务。
    
    FileLogger.d(TAG, "requestInstallView completed");
  } //!<private void requestInstall(String downloadFilePath)

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
            //!< Download failed, go see why
            if (localCursor.getInt(localCursor.getColumnIndex(DownloadManager.COLUMN_REASON)) == DownloadManager.ERROR_INSUFFICIENT_SPACE)
            {
              Log.w("DownloadStatus", " Download failed with ERROR_INSUFFICIENT_SPACE");
              FileLogger.w(TAG, "Download failed with ERROR_INSUFFICIENT_SPACE");
                                    
              long currentId=localCursor.getLong(localCursor.getColumnIndex(DownloadManager.COLUMN_ID)); //!< 获取当前这条记录的编号。
              //!< 陈欣

              if (currentId==downloadId) //!< 正是我们想要下载的编号
              {
                FileLogger.d(TAG, "Download failed for our downloadId: " + downloadId);
                notifyDownloadFail(); //!< 通知下载失败。
              } //!< if (currentId==downloadId) //!< 正是我们想要下载的编号
            }
          }
        }
        while (localCursor.moveToNext());
      }
    });
    
    FileLogger.d(TAG, "DownloadRequestor initialized");
  } //!< public DownloadRequestor() 

  private void showNotification(String contentText)
	{
		//!< In this sample, we'll use the same text for the ticker and the expanded notification
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
		CharSequence text = baseApplication.getText(R.string.app_name);

		//!< The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(baseApplication, 0, new Intent(baseApplication, LauncherActivity.class), PendingIntent.FLAG_MUTABLE);

		String downloadingText="Downloading " + contentText; //!< 构造字符串，正在下载。陈欣。

		//!< Set the info for the views that show in the notification panel.
		Notification notification = new Notification.Builder(baseApplication)
      .setSmallIcon(R.drawable.ic_launcher)  //!< the status icon
      .setTicker(text)  //!< the status text
      .setWhen(System.currentTimeMillis())  //!< the time stamp
      .setContentTitle(baseApplication.getText(R.string.app_name))  //!< the label of the entry
      .setContentText(downloadingText)  //!< the contents of the entry
      .setContentIntent(contentIntent)  //!< The intent to send when the entry is clicked
      .setPriority(Notification.PRIORITY_HIGH)   //!< heads-up
      .build();

		continiusNotification=notification; //!<记录通知

		//!< Send the notification.
    mNM = (NotificationManager)baseApplication.getSystemService(Context.NOTIFICATION_SERVICE);

		mNM.notify(NOTIFICATION, notification);
	} //!< private void showNotification(String contentText)

  /**
  * Notify download progress.
  */
  private void notifyDownloadProgress(long downloaded, long total) 
  {
    if (launcherActivity!=null)
    {
      Handler uiHandler = new Handler(Looper.getMainLooper());

      Runnable runnable= new Runnable()
      {
        /**
        * 具体执行的代码
        */
        public void run()
        {
          launcherActivity.reportDownloadProgress(packageName, downloaded, total); //!< 报告，下载 progress。
        } //!<public void run()
      };

      uiHandler.post(runnable);
    }
  } //!< private void notifyDownloadProgress(long downloaded, long total)
  
  /**
  * 通知，下载失败。
  */
  private void  notifyDownloadFail() 
  {
    FileLogger.d(TAG, "notifyDownloadFail called");
    
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    ApplicationListData applicationListData = baseApplication.getApplicationListData(); //!<获取本地服务器列表数据对象。
    applicationListData.removeUrl(fullUrl); //!< Remove the download url.
  
    String contentText="Failed to download";
      
    showNotification(contentText);
    
    if (launcherActivity!=null) //!< The launcher activity exists
    {
      launcherActivity.reportDownloadFailed(packageName); //!< 报告，下载失败。
    } //!< if (launcherActivity!=null) //!< The launcher activity exists
    
    packageName=null; //!< Clear package name.
    fullUrl=null; //!< Clear download full url.
  } //!<private void  notifyDownloadFail()
    
  /**
  * 请求下载指定网址的安装包。
  */
  public void requestDownloadUrl(Uri uri, String refererUrl, String applicationName, String packageName)
  {
    FileLogger.d(TAG, "requestDownloadUrl called, uri: " + uri + ", package: " + packageName);
    
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    HashMap<String, String> apkFilePathMap=baseApplication.getApkFilePathMap(); //!< 获取 APK 安装包路径映射。

    String apkFilePath=apkFilePathMap.get(packageName); //!< 获取安装包文件路径。
        
    Log.d(TAG, "requestDownloadUrl, apkFilePath: " + apkFilePath + ", package name: " + packageName+ ", map length: " + apkFilePathMap.size()); //!< Debug.
    FileLogger.d(TAG, "requestDownloadUrl, apkFilePath: " + apkFilePath + ", package name: " + packageName);
        
    boolean shouldDownload=false; //!< 是否应当下载。
        
    if (apkFilePath!=null) //!< 路径存在。
    {
      File apkFile=new File(apkFilePath);
        
      if (apkFile.exists()) //!< 文件存在。
      {
        boolean isApkFile=checkIsApkFile(apkFilePath); //!< 检查是不是 APK 文件。
        FileLogger.d(TAG, "APK file exists, isApkFile: " + isApkFile);
      
        //!< 检查是否是有效的安装包文件：
        if (isApkFile) //!< 是 APK 文件
        {
          FileLogger.d(TAG, "APK file is valid, requesting install");
          requestInstall(apkFilePath); //!< 要求安装。
        } //!< if (isApkFile) //!< 是 APK 文件
        else //!<不是 APK 文件。
        {
          shouldDownload=true; //!< 应当下载。
          FileLogger.d(TAG, "APK file is not valid, will download again");
        }
      } //!<if (apkFile.exists()) //!< 文件存在。
      else //!< 文件不存在
      {
        shouldDownload=true; //!< 应当下载。
        FileLogger.d(TAG, "APK file does not exist, will download");
      } //!<else //!< 文件不存在
    } //!<if (apkFilePath!=null) //!< 路径存在。
    else //!< 路径不存在。
    {
      shouldDownload=true; //!< 应当下载。
      FileLogger.d(TAG, "APK file path not found, will download");
    }

    if (shouldDownload) //!< Should download.
    {
      Log.d(TAG, "requestDownloadUrl, 347, download file path: " + uri); //!<debug.
      FileLogger.d(TAG, "URL scheme: " + uri.getScheme());

      ApplicationListData applicationListData = baseApplication.getApplicationListData(); //!<获取本地服务器列表数据对象。

      fullUrl = uri.toString();

      if (applicationListData.containsUrl(fullUrl)) //!<已经包含这个网址。
      {
        Log.d(TAG, "requestDownloadUrl, 357, already downloading: " + uri+", ignore"); //!<debug.
        FileLogger.d(TAG, "Already downloading this URL, ignoring");
      } //!<if (applicationListData.containsUrl(fullUrl)) //!<已经包含这个网址。
      else //!<尚未包含这个网址。
      {
        applicationListData.addUrl(fullUrl); //!<记录，已经请求下载这个网址。

        String mWordSeparators = baseApplication.getResources().getString(R.string.startDownload); //!< 读取 说明 字符串。
    
        voiceUi.say(mWordSeparators); //!< 说话，需要解锁。

        downloadByIon(uri); //!< 使用离子下载来下载。
      } //!<else //!<尚未包含这个网址。
    } //!< if (shouldDownload) //!< Should download.
  } //!< public void requestDownloadUrl(Uri uri, String refererUrl, String applicationName, String packageName)

  /**
  * 检查是不是 APK 文件。
  */
  private boolean checkIsApkFile(String apkFilePath) 
  {
    boolean result=false;

    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    
    PackageManager packageManager = baseApplication.getPackageManager();
    
    PackageInfo packageInfo=packageManager.getPackageArchiveInfo(apkFilePath, 0);
    
    String expectedVersionName = baseApplication.getAvailableVersionName(packageName); //!< 获取可用的版本名字。

    if (packageInfo!=null) //!< 有有效的包信息。
    {
      String versionName=packageInfo.versionName; //!< Get the versin name.

      Log.d(TAG, "checkIsApkFile, version name: " + versionName + "/" + expectedVersionName); //!< Debug.
      FileLogger.d(TAG, "checkIsApkFile, version name: " + versionName + "/" + expectedVersionName);

      if (versionName.equals(expectedVersionName)) //!< The version name is expected.
      {
        result=true; //!< 是安装包。
        FileLogger.d(TAG, "Version name matches, is valid APK");
      } //!< if (versionName==expectedVersionName)
      else //!< Version name not equal
      {
        if (packageName==null) //!< No package name provided
        {
          result=true; //!< It is apk file.
          FileLogger.d(TAG, "No package name provided, accepting APK");
        } //!< if (packageName==null) //!< No package name provided
        else
        {
          FileLogger.d(TAG, "Version name mismatch: " + versionName + " vs " + expectedVersionName);
        }
      } //!< else //!< Version name not equal
    }
    else
    {
      FileLogger.d(TAG, "PackageInfo is null, invalid APK");
    }

    return result;
  } //!< private boolean checkIsApkFile(String apkFilePath)

  /**
  * 使用离子下载来下载。
  * @param uri 要下载的网址。
  */
  private void downloadByIon(Uri uri)
  {
    String targetUrl=uri.toString(); //!<获取目标 URL。

    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。

    String fileName=uri.getLastPathSegment(); //!< 获取文件名。陈欣
    
    if (packageName!=null) //!< Has package name
    {
      fileName=packageName+".apk";
    } //!< if (packageName!=null) //!< Has package name
        
    Context context = HxLauncherApplication.getAppContext();

    File downloadFolder = baseApplication.getExternalCacheDir();

    String wholePath =downloadFolder.getPath()+ File.separator  + fileName;
    
    Log.d(TAG, "downloadByIon, 463, url: " + targetUrl + ", path: " + wholePath); //!< Debug.
    FileLogger.d(TAG, "downloadByIon, url: " + targetUrl + ", path: " + wholePath);

    fileDownloadFuture= Ion.with(baseApplication)
      .load(targetUrl)
      .setTimeout(15000) //!<Set the time out to be 15s.
      .progress(new ProgressCallback() 
      {
        @Override
        public void onProgress(long downloaded, long total) 
        {
          timerObj.cancel(); //!< Cancel the timer of cancel.
          Log.d(TAG, "downloadByIon, 470, progress: " + downloaded + "/" + total + ", " + targetUrl); //!< 报告进度。
          FileLogger.d(TAG, "Download progress: " + downloaded + "/" + total);
          notifyDownloadProgress(downloaded, total); //!< Notify download progress.
        }
      })
      .setLogging(TAG, Log.DEBUG)
      .write(new File( wholePath));
      
      fileDownloadFuture.setCallback(new FutureCallback<File>() 
      {
        @Override
        public void onCompleted(Exception e, File file) 
        {
          //!< download done...
          //!< do stuff with the File or error

          if (e!=null) //!<Some error occured.
          {
            FileLogger.e(TAG, "Download failed: " + e.getMessage());
            Toast.makeText(baseApplication, "Download Failed" + targetUrl, Toast.LENGTH_SHORT).show();

            Log.d(TAG,"download error:"); //!<Debug.
            e.printStackTrace(); //!<Report error.
                            
            //!<                             陈欣
            notifyDownloadFail(); //!< 报告下载失败。
          } //!<if (e!=null) //!<Some error occured.
          else //!< 下载完毕
          {
            FileLogger.d(TAG, "Download completed: " + wholePath);
            Toast.makeText(baseApplication, "Download Completed" + wholePath, Toast.LENGTH_SHORT).show();

            rememberApkFile(wholePath); //!< 记录安装包路径。
                            
            if (checkIsApkFile(wholePath)) //!< 是安装包文件。
            {
              FileLogger.d(TAG, "APK file is valid, requesting install");
              requestInstall(wholePath); //!< 要求安装。陈欣。
            } //!< if (checkIsApkFile(wholePath)) //!< 是安装包文件。
            else //!< 不是安装包。
            {
              FileLogger.d(TAG, "APK file is not valid");
              notifyDownloadFail(); //!< 报告下载失败。
            } //!< else //!< 不是安装包。
          } //!<else //!< 下载完毕
        }
      });
      
      startTimeoutCancelTimer(); //!< Start time out cancel timer.
  } //!<private void downloadByIon(Uri uri)
  
  /**
  * Start time out cancel timer.
  */
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
          /**
          * 具体执行的代码
          */
          public void run()
          {
            cancelDownload(); //!< Cancel download.
          } //!<public void run()
        };

        uiHandler.post(runnable);
      }
    };
    timerObj.schedule(timerTaskObj, 15000); //!< 延时启动。
  } //!< private void startTimeoutCancelTimer()
  
  /**
  * Cancel download.
  */
  public void cancelDownload()
  {
    HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //!<获取应用程序对象。
    Ion.getDefault(baseApplication).cancelAll(baseApplication);
    FileLogger.d(TAG, "Download cancelled");
  } //!< public void cancelDownload() //!< Cancel download.


    public void requestDownloadUrl(String url, String refererUrl, String applicatinName, String packageName, LauncherActivity launcherActivity)
    {
      this.launcherActivity=launcherActivity;
      this.packageName=packageName;
      
      String fixedUrl=url;
      
      if (fixedUrl.startsWith("http")) //!< correct url.
      {
      } //!< if (fixedUrl.startsWith("http")) //!< correct url.
      else //!< Not full url.
      {
        fixedUrl="http://"+fixedUrl;
      } //!< else //!< Not full url.
    
      Uri uri=Uri.parse(fixedUrl);

      requestDownloadUrl(uri, refererUrl, applicatinName, packageName);
    }

    public void requestDownloadUrl(String url, String refererUrl, String applicatinName, String packageName)
    {
      requestDownloadUrl(url, refererUrl, applicatinName, packageName, null);
    }

}
