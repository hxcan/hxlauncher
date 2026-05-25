package com.stupidbeauty.hxlauncher.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.ComponentName;
import android.content.Context;
import butterknife.OnLongClick;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.victoriafresh.VFile;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ActivityIconType;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ShortcutIconType;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import android.media.MediaScannerConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import android.net.Uri;
import com.stupidbeauty.hxlauncher.manager.ActiveUserReportManager;
import android.os.Debug;
import org.apache.commons.io.FileUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.Vibrator;
import android.provider.Settings;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stupidbeauty.grebe.DownloadRequestor;
import com.stupidbeauty.hxlauncher.ApplicationInformationAdapter;
import com.stupidbeauty.hxlauncher.BuiltinShortcutsManager;
import com.stupidbeauty.hxlauncher.Constants;
import com.stupidbeauty.hxlauncher.InstalledPackageLoadTaskInterface;
import com.stupidbeauty.hxlauncher.R;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.stupidbeauty.msclearnfootball.AnswerAvailableEvent;
import com.stupidbeauty.msclearnfootball.VoiceRecognizeResult;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.asynctask.VoiceShortcutAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import com.stupidbeauty.hxlauncher.bean.HxShortcutInfo;
import com.stupidbeauty.hxlauncher.callback.LauncherAppsCallback;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.g00fy2.versioncompare.Version;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import static android.content.Intent.ACTION_PACKAGE_REPLACED;
import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_PACKAGE_NAME;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST;

public class ApplicationInformationActivity extends Activity implements  InstalledPackageLoadTaskInterface
{
  private String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "chenxin.e.apk"; //!< 输出文件路径。
  public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
  private     AnimationDrawable rocketAnimation; //!<录音按钮变暗
  private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈
  private BuiltinShortcutsManager builtinShortcutsManager=new BuiltinShortcutsManager(); //!<内置快捷方式管理器
  @BindView(R.id.hitApplicationIcon) ImageView hitApplicationIcon; //!<命中的应用的图标。
  @BindView(R.id.microphoneIcon) ImageView microphoneIcon; //!< 升级按钮图标。
  @BindView(R.id.applicationName2) TextView applicationName2; //!< Application name text view.
  @BindView(R.id.launcher_activity) RelativeLayout launcher_activity; //!<整个启动活动
  private HashMap<String, Long> packageItemLastLaunchTimestampMap=new HashMap<>(); //!<包名加类名的字符串与最后一次启动时间戳之间的映射。

  private HashMap<String, String> serverVoiceCommandResponseIgnoreMap=new HashMap<>(); //!<服务器的回复中，要忽略掉的关系映射

    private HashMap<String, ShortcutInfo> shortcutTitleInfoMap; //!<快捷方式的标题与快捷方式对象本身的映射。
    private HashMap<String, ShortcutInfo> shortcutIdInfoMap; //!<快捷方式的编号与快捷方式对象本身的映射
    private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
    private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。

    private boolean sentVoiceShortcutAssociationData=false; //!<是否已经成功发送语音指令关联快捷方式的数据。
    private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识

    private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限

    private MultiMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
    //    private HashMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
    private HashMap<String, HxShortcutInfo> voiceShortcutIdMap=new HashMap<>(); //!<语音识别结果与快捷方式编号之间的映射关系．

    @BindView(R.id.wallpaper) ImageView wallpaper; //!<墙纸视图。

    @BindView(R.id.progressBar) ProgressBar progressBar; //!<进度条。

    private String voiceRecognizeResultString; //!<语音识别结果。

    int ret = 0;

    @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!< 应用图标图片。陈欣。

    @BindView(R.id.statustextView) TextView statustextView; //!<用来显示状态的文字标签。

    private int recognizeCounter=0; //!<识别计数器．

    private Vibrator vibrator;

    private boolean voiceEndDetected=false; //!<是否已经探测到用户声音结束。

    private int mPageNumber = 1;//{1, 1, 1};

    private final int MSG_LOAD_MORE = 2;

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;

    private String packagename; //!< 包名。陈欣。
    private String activityName; //!< 活动名字。

    private int mCurrMsg = -1;

    private RequestQueue mQueue; //!<Volley请求队列。

    @OnClick(R.id.loveAnimation)
    public void deleteItem()
    {
        Uri uri = Uri.parse("package:" + packagename);
        Intent uIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, uri);
        startActivity(uIntent);
    }

    /**
     * 手动开始语音识别。
     */
    @OnClick(R.id.hitApplicationIcon)
    public void showInformationUrl()
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      Map<String,String> internationalizationData=application.getPackageNameInformationUrlMap(); // 获取 map, package name information url.
      Map<String, String> packageNameApplicationMap=application.getPackageNameApplicationNameMap(); //获取包名与应用程序名字的映射

      if (internationalizationData!=null) //数据存在。
      {
        String internationalizationName=internationalizationData.get(packagename); //获取国际化名字。

        Log.d(TAG,"showInformationUrl, information url: "+ internationalizationName+ ", package name: " + packagename); //Debug.

        if (internationalizationName!=null) //有国际化名字。
        {
          Log.d(TAG,"tryDownloadApkAssociatedToPackage, 183, package url: "+ internationalizationName+ ", package name: " + packagename); //Debug.

//           String applicationName=packageNameApplicationMap.get(packagename); //应用程序名字
          try
          {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(internationalizationName))); // Show information url browser.
          }
          catch (ActivityNotFoundException e)
          {
            e.printStackTrace();
          }
          catch (NullPointerException e) // 捕获空指针异常。
          {
            e.printStackTrace();
          }

//           requestDownloadPackage(internationalizationName, applicationName); //下载应用程序安装包。
        } //if (internationalizationName!=null) //有国际化名字。
      } //if (internationalizationData!=null) //数据存在。
    } //public void manualStartVoiceRecognize()
    
    /**
    * Check for storage permission.
    */
    private boolean checkStoragePermission()
    {
      boolean isFileManager=Environment.isExternalStorageManager();
      
      return isFileManager;
    } // private void checkStoragePermission()
    
    @OnClick(R.id.shareApkge_button1)
    public void shareApkge_button1()
    {
      boolean storagePermission = checkStoragePermission(); // Check for storage permission.
      
      if (storagePermission) // We have storage permisison.
      {
        saveBitmap(); //保存

        Intent share = new Intent(Intent.ACTION_SEND);

        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        share.setType("application/vnd.android.package-archive");

        File f = new File(outputFile);

        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", f);

        share.putExtra(Intent.EXTRA_STREAM, photoURI);

        startActivity(Intent.createChooser(share, "Share APK"));
      } // if (storagePermission) // We have storage permisison.
      else // We do not have storage permission.
      {
        toggleApplicationLock(); // Request for stoarge manager permission.
      } // else // We do not have storage permission.
    
    } // public void shareApkge_button1()

	/**
	 * 要求扫描照片。
	 * @param path 照片文件的路径。
	 */
	private void scanFile(String path)
	{
		MediaScannerConnection.scanFile
		(
      this,
      new String[] { path },
      null,
      new MediaScannerConnection.OnScanCompletedListener() 
      {
        public void onScanCompleted(String path, Uri uri) 
        {
          Log.i("TAG", "Finished scanning " + path);
        }
      }
    );
	} //private void scanFile(String path)

	/**
  * 获取输出流。
  * @return 输出流。
  */
  private OutputStream getOutputStream()
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS", Locale.US);
    Date date = new Date(); //获取日期。

    String dateTimeString=format.format(date); //格式化成字符串。
    PackageManager packageManager = getPackageManager();

    String applicationName = getString(R.string.installerPackage); // The default application name.
    
    try
    {
      ApplicationInfo applicatonInformation = packageManager.getApplicationInfo( packagename , 0);
      applicationName = (String) (packageManager.getApplicationLabel(applicatonInformation));
    }
    catch(NameNotFoundException e)
    {
    }

    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + applicationName + "." + packagename +".apk"; //!<输出文件路径。

    OutputStream result;

    FileOutputStream fOut = null;
    try 
    {
      fOut = new FileOutputStream(outputFile);
    }
    catch (FileNotFoundException e) 
    {
      e.printStackTrace();
    }

    result=fOut;

    return result;
  } //private OutputStream getOutputStream()

  public void saveBitmap()
  {
    // Bitmap drawing = new Bitmap(); //Get the bitmap.
    
    PackageManager packageManager = getPackageManager();

    try
    {
      ApplicationInfo applicatonInformation = packageManager.getApplicationInfo( packagename , 0);

      String apkFilePath = applicatonInformation.sourceDir; // The apk file path.
      Log.d(TAG, CodePosition.newInstance().toString()+ ", apk file path: " + apkFilePath ); // Debug.

      File apkFile = new File(apkFilePath);
      
      // Chen xin.

      Bitmap.CompressFormat format= Bitmap.CompressFormat.PNG;
      int quality=90;
      OutputStream stream=getOutputStream();
    
      if (stream != null) // The stream exists
      {
        FileUtils.copyFile(apkFile, stream); // Copy the apk file.
      } // if (stream != null) // The stream exists
    }
    catch(IOException e)
    {
    }
    catch(NameNotFoundException e)
    {
    }
    
    // drawing.compress(format, quality, stream);

		scanFile(outputFile); //扫描该图片。

		String voiceRecognizeResultString="kubectl.KubernetesClient.dll.sh.part.zzapwn";
		String packageItemInfopackageName="kubectl.KubernetesClient.dll.sh.part.zzapwm";
		String packageItemInfoname="kubectl.KubernetesClient.dll.sh.part.zzapwl";
		String recordSoundFilePath="kubectl.KubernetesClient.dll.sh.part.zzapwk";
	} //public void saveBitmap()

    /**
     * Request for storage manager permission.
     */
    public void toggleApplicationLock()
    {
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      Log.d(TAG, "gotoFileManagerSettingsPage"); //Debug.

      Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);  // 跳转语言和输入设备

      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      String packageNmae=getPackageName();
      Log.d(TAG, "gotoFileManagerSettingsPage, package name: " + packageNmae); //Debug.

      String url = "package:"+packageNmae;

      Log.d(TAG, "gotoFileManagerSettingsPage, url: " + url); //Debug.

      intent.setData(Uri.parse(url));

      startActivity(intent);
    } //public void toggleBuiltinShortcuts()
    
    /**
     * 启动应用。
     * @param launchIntent 启动用的意图。
     */
    public boolean launchApplication(Intent launchIntent)
    {
      boolean result=false; //结果

      Log.d(TAG, "launchApplication, launch intent: " + launchIntent); //Debug.
      try //尝试启动活动，并且捕获可能的异常。
      {
        if (launchIntent!=null) //启动意图存在。
        {
          HxLauncherApplication application = HxLauncherApplication.getInstance(); //获取应用程序对象。
          boolean allowedToLaunch = application.checkLaunchCoolDownTime(launchIntent); // 检查启动的冷却时间。

          Log.d(TAG, "launchApplication, allowed to launch: " + allowedToLaunch); //Debug.

          if (allowedToLaunch) //允许启动。
          {
            startActivity(launchIntent); //启动活动。
          } // if (allowedToLaunch) //允许启动。
        } //                    if (launchIntent!=null) //启动意图存在。

        result=true; //启动成功
      } //try //尝试启动活动，并且捕获可能的异常。
      catch (ActivityNotFoundException exception)
      {
        exception.printStackTrace(); //报告错误。
      } //catch (ActivityNotFoundException exception)
      catch (SecurityException exception) //安全异常。
      {
        exception.printStackTrace(); //报告错误。
      } //catch (SecurityException exception) //安全异常。

      return result;
    } //private void launchApplication(Intent launchIntent)

    /**
     * 根据包名启动应用程序。
     * @param packageName 包名。
     */
    private boolean launchApplicationByPackageName(String packageName)
    {
      boolean result=false; //启动结果
      PackageManager packageManager=getPackageManager(); //获取软件包管理器。

      Intent launchIntent= packageManager.getLaunchIntentForPackage(packageName); //获取当前软件包的启动意图。

      if (launchIntent!=null) //意图存在。
      {
        try //尝试启动活动，并且捕获可能的异常。
        {
          launchApplication(launchIntent); //启动活动。

          result=true; //成功
        } //try //尝试启动活动，并且捕获可能的异常。
        catch (ActivityNotFoundException exception)
        {
          exception.printStackTrace(); //报告错误。
        } //catch (ActivityNotFoundException exception)
      } //if (launchIntent!=null) //意图存在。
      //意图不存在，则说明对应的应用不存在，后续应当触发自动下载。
      //else //意图不存在，则说明对应的应用不存在，后续应当触发自动下载。

      return result;
    } //private void launchApplicationByPackageName(String packageName)

    @OnClick(R.id.launchIconon)
    public void launchIconon()
    {
      try //尝试启动活动，并且捕获可能的异常。
      {
        launchApplicationByPackageName(packagename); //按照包名启动。
      } //try //尝试启动活动，并且捕获可能的异常。
      catch (ActivityNotFoundException exception)
      {
        exception.printStackTrace(); //报告错误。
      } //catch (ActivityNotFoundException exception)
      catch (SecurityException exception) //安全异常。
      {
        exception.printStackTrace(); //报告错误。
      } //catch (SecurityException exception) //安全异常。
    } // public void launchIconon()

    /**
     * 手动开始语音识别。
     */
    @OnClick({R.id.voiceAssistantLayout, R.id.microphoneIcon})
    public void requestUpgradePackage()
    {
        Log.d(TAG, "manualStartVoiceRecognize."); //Debug.

        {
            HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

            Map<String,String> internationalizationData=application.getPackageNameUrlMap(); //获取国际化数据对象。
            Map<String, String> packageNameApplicationMap=application.getPackageNameApplicationNameMap(); //获取包名与应用程序名字的映射

            if (internationalizationData!=null) //数据存在。
            {
                String internationalizationName=internationalizationData.get(packagename); //获取国际化名字。

                Log.d(TAG,"tryDownloadApkAssociatedToPackage, internationalizationName: "+ internationalizationName+ ", package name: " + packagename); //Debug.


                if (internationalizationName!=null) //有国际化名字。
                {
                    Log.d(TAG,"tryDownloadApkAssociatedToPackage, 183, package url: "+ internationalizationName+ ", package name: " + packagename); //Debug.

                    String applicationName=packageNameApplicationMap.get(packagename); //应用程序名字

                    requestDownloadPackage(internationalizationName, applicationName); //下载应用程序安装包。
                } //if (internationalizationName!=null) //有国际化名字。
            } //if (internationalizationData!=null) //数据存在。

        } //else //命中了。

//        commandRecognizebutton2(); //启动语音识别。
    } //public void manualStartVoiceRecognize()

    /**
     * 下载应用程序安装包。
     * @param internationalizationName 安装包路径。
     */
    private void requestDownloadPackage(String internationalizationName, String applicationName)
    {
      Log.w(TAG, "requestDownloadPackage, 200, timestamp: " + System.currentTimeMillis()); //Debug.

      downloadRequestor.requestDownloadUrl(internationalizationName, internationalizationName, applicationName, packagename); //要求下载网址
    } //private void requestDownloadPackage(String internationalizationName)

    private final DownloadRequestor downloadRequestor = new DownloadRequestor();

    /**
     * 报告结果，翻译请求的发送结果。
     * @param result 是否发送成功。
     */
    public void processApplicationInfoLoadResult(List<PackageInfo> result)
    {
//        showInstalledPackages(result);
    } //public void processApplicationInfoLoadResult(Boolean result)


    private ApplicationInformationAdapter mAdapter; //!<适配器。

    private static final String TAG="ApplicationInformationB"; //!<输出调试信息时使用的标记。
    private final String categoryName="default"; //!<要显示的分类的名字。

    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.d(TAG, "onNewIntent, intent: " + intent); //Debug.
        super.onNewIntent(intent);

        processIntent(intent); // 处理意图。陈欣。
    }

    /**
     * 要求显示系统的墙纸在本活动后面。
     */
    private void askShowSystemWallpaper()
    {
      WindowManager.LayoutParams p=getWindow().getAttributes();
      p.flags |= WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
    } //private void askShowSystemWallpaper()
    
    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      askShowSystemWallpaper(); //要求显示系统的墙纸在本活动后面。

      setContentView(R.layout.application_information_activity); // 设置视图

      ButterKnife.bind(this); //视图注入。

      mHeaderImageView = findViewById(R.id.applicationIconrightimageView2);
      mHeaderTitle = findViewById(R.id.rightTextoperationMethodactTitletextView2);

      mHeaderTitle.setTransitionName( VIEW_NAME_HEADER_TITLE);

      Intent intent=getIntent(); // 获取意图。

      processIntent(intent);
    } //protected void onCreate(Bundle savedInstanceState)

    private void processIntent(Intent intent) 
    {
      String title=intent.getStringExtra(EXTRA_PACKAGE_NAME);

      activityName = intent.getStringExtra(EXTRA_COMPONENT_NAME); // 获取活动名字。陈欣。

      packagename = title; // 记录包名。陈欣。

      mHeaderTitle.setText(title);

      final PackageManager pm = getApplicationContext().getPackageManager();
      ApplicationInfo ai;
      try {
        ai = pm.getApplicationInfo( packagename , 0);
      } catch (final PackageManager.NameNotFoundException  e) {
      ai = null;
      }
      
      String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
      
      // 陈欣，找到改写的名字。
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      ApplicationNameInternationalizationData internationalizationData=application.getApplicationNameInternationalizationData(); //获取国际化数据对象。

      if (internationalizationData!=null) //数据存在。
      {
        String internationalizationName=internationalizationData.getInternationalizationName(packagename); //获取国际化名字。

        if (internationalizationName!=null) //有国际化名字。
        {
          applicationName = internationalizationName; // Use the internationalization name.
          // holder.mTextView.setText(internationalizationName); //显示国际化名字。
        } //if (internationalizationName!=null) //有国际化名字。
      } //if (internationalizationData!=null) //数据存在。

      
      applicationName2.setText(applicationName); // Show application name.
        
      checkUpgrade(title); // 检查这个软件包是否可以升级。

      HxLauncherApplication hxlauncherApplication=HxLauncherApplication.getInstance(); // 获取应用对象。
      String currentVersionName=hxlauncherApplication.getVersionName(packagename); // 获取版本名字。

      // HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      HashMap<String, Drawable> launchIconMap=application.getLaunchIconMap(); //获取启动图标缓存。

      Drawable result=launchIconMap.get(packagename + "/" + activityName); //获取缓存绘图对象。

      Glide.with(application).load("").placeholder(result).into(applicationIconrightimageView2); //显示图标。
    }

    /**
     * 检查这个软件包是否可以升级。
     * @param packageName 软件 包名。陈欣
     */
    private void checkUpgrade(String packageName)
    {
      HxLauncherApplication hxlauncherApplication=HxLauncherApplication.getInstance(); // 获取应用对象。
      String currentVersionName=hxlauncherApplication.getVersionName(packageName); // 获取版本名字。

      String availableVersonName = hxlauncherApplication.getAvailableVersionName(packageName); // 获取可用的版本名字。

      Log.d(TAG, "checkUpgrade. avaialable version: " + availableVersonName + ", current version: " + currentVersionName); //Debug.

      Version availableVersion= new Version(availableVersonName); // 已有版本对象。

      if (availableVersion.isHigherThan(currentVersionName)) // 有新版本
      {
        showUpgradeIcon(); // 显示升级按钮
      } //if (availableVersonName > currentVersionName) // 有新版本
      else // 无新版本
      {
        hideUpgradeIcon(); // 隐藏升级按钮。
      } //else // 无新版本
    } //private void checkUpgrade(String packageName)

    /**
     * 隐藏升级按钮。
     */
    private void hideUpgradeIcon()
    {
        microphoneIcon.setVisibility(View.INVISIBLE); // 隐藏。
    } //private void hideUpgradeIcon()

    /**
     * 显示升级按钮
     */
    private void showUpgradeIcon()
    {
      microphoneIcon.setVisibility(View.VISIBLE); // 显示。陈欣。
    } //private void showUpgradeIcon()

    private void resetPreferredLauncherAndOpenChooser()
    {
      PackageManager packageManager = getPackageManager();
      ComponentName componentName = new ComponentName(getApplicationContext(), com.stupidbeauty.hxlauncher.activity.FakeLauncherActivity.class);
      packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

      Intent selector = new Intent(Intent.ACTION_MAIN);
      selector.addCategory(Intent.CATEGORY_HOME);
      selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(selector);

      packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    /**
     * 切换是否应用锁。
     */
    @OnCheckedChanged(R.id.lock0)
    public void toggleApplicationLock(boolean isChecked)
    {
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      
      if (isChecked) // 锁住
      {
        hxLauncherApplication.addApplicationLock(packagename); // 增加应用锁。
      } // if (isChecked) // 锁住
      else // 不锁住
      {
        hxLauncherApplication.removeApplicationLock(packagename); // 删除应用锁。
      } // else // 不锁住
    } //public void toggleBuiltinShortcuts()

    /**
     * 记录启动时间戳。
     * @param launchIntent 启动意图。
     */
    private void rememberLaunchTimestamp(Intent launchIntent)
    {
      String packageName=launchIntent.getComponent().getPackageName(); //获取包名。
      String activityName=launchIntent.getComponent().getClassName(); //获取活动的类名。

      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      HashMap<Integer, Integer> seekBarValueCoolDownTimeMap=hxLauncherApplication.getSeekBarValueCoolDownTimeMap(); //获取映射。

      HashMap<String, Integer> packageItemLaunchCoolDownMap=hxLauncherApplication.getPackageItemLaunchCoolDownMap(); //获取映射。包条目信息字符串与实际冷却时间秒数之间的映射。

      long currentTimestamp=System.currentTimeMillis(); //当前的时间戳。

      packageItemLastLaunchTimestampMap.put(packageName+"/"+activityName, currentTimestamp); //记录。
    } //private void rememberLaunchTimestamp(Intent launchIntent)

  // 在 ApplicationInformationActivity 类中添加长按事件
  @OnLongClick(R.id.applicationName2)
  public boolean copyApplicationName(View view)
  {
    // 获取应用名字
    String appName = applicationName2.getText().toString();

    // 复制到剪贴板
    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("application_name", appName);
    clipboard.setPrimaryClip(clip);

    // 显示提示
    Toast.makeText(this, "已复制应用名字: " + appName, Toast.LENGTH_SHORT).show();

    return true; // 表示已处理长按事件
  }
}
