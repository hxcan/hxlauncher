package com.stupidbeauty.hxlauncher.logic;

import com.stupidbeauty.hxlauncher.interfaces.ShutDownAt2100LogicInterface;
import com.stupidbeauty.hxlauncher.manager.PackageInformationManager;
import android.os.Debug;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoicePackageNameMapTask;
import com.stupidbeauty.hxlauncher.asynctask.BuildActivityLabelPackageItemInfoMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinShortcutsTask;
import static com.stupidbeauty.blindbox.Constants.Networks.TRANSLATE_REQUEST_QUEUE_NAME;
// import com.stupidbeauty.appstore.core.DownloadFailureReporter;
import static com.stupidbeauty.blindbox.Constants.Networks.RabbitMQPassword;
import static com.stupidbeauty.blindbox.Constants.Networks.RabbitMQUserName;
import static com.stupidbeauty.blindbox.Constants.Networks.TRANSLATE_REQUEST_QUEUE_NAME;
// import com.stupidbeauty.appstore.core.DownloadFailureReporter;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Handler;
import com.stupidbeauty.shutdownat2100.helper.ShutDownAt2100Manager;
import com.stupidbeauty.hxlauncher.logic.ShutDownAt2100Logic;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.protobuf.InvalidProtocolBufferException;
import com.stupidbeauty.hxlauncher.activity.ApplicationUnlockActivity;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import android.net.Uri;
import com.stupidbeauty.hxlauncher.manager.ActiveUserReportManager;
import android.os.Debug;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoicePackageNameMapTask;
import com.stupidbeauty.hxlauncher.asynctask.BuildActivityLabelPackageItemInfoMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinShortcutsTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadPreferenceTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadShortcutsTask;
import com.stupidbeauty.hxlauncher.asynctask.ReqGameDataTask;
import com.stupidbeauty.hxlauncher.asynctask.BindAdapterTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoiceShortcutMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadServerVoiceCommandReponseIgnoreTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadVoiceShortcutIdMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadVoicePackageNameMapTask;
import java.util.Timer;
import java.util.TimerTask;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import com.andexert.library.RippleView;
import com.example.administrator.douyin.Love2;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
// import android.widget.TextView;
import android.widget.Toast;
import com.stupidbeauty.hxlauncher.asynctask.BuildInternationalizationDataPackageNameMapTask;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.adapter.FlipAnimationAdapter;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.stupidbeauty.msclearnfootball.AnswerAvailableEvent;
import com.stupidbeauty.msclearnfootball.VoiceRecognizeResult;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
// import com.stupidbeauty.hxlauncher.asynctask.TranslateRequestSendTask;
import com.stupidbeauty.comgooglewidevinesoftwaredrmremover.app.LanImeUncaughtExceptionHandler;
import com.stupidbeauty.grebe.DownloadRequestor;
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
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
// import java.util.Set;
import java.util.Stack;

// import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.iflytek.cloud.SpeechRecognizer;
import com.stupidbeauty.victoriafresh.VFile;
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
// import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
import static com.stupidbeauty.hxlauncher.Constants.Actions.LegacyInstallShortcut;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.InputtingForPackage;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.PackageNameOfInputting;
import static com.stupidbeauty.hxlauncher.Constants.Numbers.IgnoreVoiceResultLength;
import static com.stupidbeauty.hxlauncher.Constants.Operation.ToggleBuiltinShortcuts;
import static com.stupidbeauty.hxlauncher.Constants.Operation.ToggleHiveLayout;
import static com.stupidbeauty.hxlauncher.Constants.Operation.UnlinkVoiceCommand;
import static com.stupidbeauty.hxlauncher.HxLauncherIconType.PbShortcutIconType;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ActivityIconType;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ShortcutIconType;
import static com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType.LocalVoiceCommandMap;
import static com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType.ServerVoiceCommandResponse;
import com.stupidbeauty.hxlauncher.listener.BuiltinFtpServerErrorListener; 
import android.os.Process;
import com.stupidbeauty.hxlauncher.rpc.VoiceCommandHitDataReporter;

public class ShutDownAt2100Logic
{
  // private VoiceCommandHitDataReporter voiceCommandHitDataReporter=new VoiceCommandHitDataReporter(); //!< The voice command hit data reporter.
  private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";
  // private DownloadFailureReporter downloadFailureReporter=new DownloadFailureReporter(); //!< download failur reporetr.
  private BuiltinFtpServerErrorListener builtinFtpServerErrorListener=null; //!< the builtin ftp server error listener.  Chen xin.
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.

  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。

  private AnimationDrawable rocketAnimation; //!<录音按钮变暗

  private CloudRequestorZzaqwb cloudRequestorZzaqwb=new CloudRequestorZzaqwb(); //!<云端请求发送器
  private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈

  private ShutDownAt2100Logic shutDownAt2100Logic= null; //!< Logic with shutdownat2100.
  private ShutDownAt2100Manager shutDownAt2100Manager= null; //!< 管理与21点关机之间的事务。

  private boolean mscIsInitialized=false; //!<讯飞语音识别是否已经初始化。
  private HashMap<String, Long> packageItemLastLaunchTimestampMap=new HashMap<>(); //!<包名加类名的字符串与最后一次启动时间戳之间的映射。

  private HashMap<String, String> serverVoiceCommandResponseIgnoreMap=null; //!<服务器的回复中，要忽略掉的关系映射

  private HashMap<String, ShortcutInfo> shortcutTitleInfoMap; //!<快捷方式的标题与快捷方式对象本身的映射。
  private HashMap<String, ShortcutInfo> shortcutIdInfoMap; //!<快捷方式的编号与快捷方式对象本身的映射
  private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
  private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。

    PowerManager.WakeLock wakeLock=null; //!<游戏辅助唤醒锁。
    private boolean activityHasBeenResumed=false; //!<活动是否处于被继续的状态，即正常的运行状态。
    private boolean sentVoiceAssociationData=false; //!<是否已经成功发送语音指令关联应用程序数据。

    private List<ShortcutInfo> shortcutInfos=null; //!< 快捷方式列表。
    
    private ArrayList<ArticleInfo> articleInfoArrayList = null; //!< 应用程序信息列表。
    
    private boolean builtinShortcutsVisible= true; //!< 内置 快捷方式是否可见。
    
    /**
    * 包名加类名的字符串与图标位置之间的映射。
    */
    public void setPackageNameItemNamePositionMap (HashMap<String, Integer> packageNameItemNamePositionMap)
    {
        this.packageNameItemNamePositionMap=packageNameItemNamePositionMap;
    } //public void setPackageNameItemNamePositionMap (HashMap<String, Integer> packageNameItemNamePositionMap)

    /**
    * 包名字符串与图标位置之间的映射。
    */
    public void setPackageNamePositionMap (HashMap<String, Integer> packageNamePositionMap)
    {
        this.packageNamePositionMap=packageNamePositionMap;
    }
    
        public void setServerVoiceCommandResponseIgnoreMap (HashMap<String, String> serverVoiceCommandResponseIgnoreMap) //!<服务器的回复中，要忽略掉的关系映射
        {
            this.serverVoiceCommandResponseIgnoreMap=serverVoiceCommandResponseIgnoreMap;
        }


    public void setSentVoiceShortcutAssociationData(boolean sentVoiceShortcutAssociationData) {
        this.sentVoiceShortcutAssociationData = sentVoiceShortcutAssociationData;
    }

    private boolean sentVoiceShortcutAssociationData=false; //!<是否已经成功发送语音指令关联快捷方式的数据。
    private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识

    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; //!<录音权限。
    private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限

    private final DownloadRequestor downloadRequestor = new DownloadRequestor();

    private boolean foundActivity=false; //!<是否命中了活动。

    private SetValuedMap<String, PackageItemInfo> voicePackageNameMap=null; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
    private HashMap<String, HxShortcutInfo> voiceShortcutIdMap=null; //!<语音识别结果与快捷方式编号之间的映射关系．
    private HashMap<String, HxShortcutInfo> voiceShortcutIdMapBuiltin=null; //!<语音识别结果与快捷方式编号之间的映射关系．
    
    /**
    * 内置的，语音识别结果与包条目信息之间的映射关系。
    */
    public void setVoicePackageNameMapBuiltin( SetValuedMap<String, PackageItemInfo> voicePackageNameMapBultin)
    {
        this.voicePackageNameMapBultin=voicePackageNameMapBultin;
    } //public setVoicePackageNameMapBuiltin( SetValuedMap<String, PackageItemInfo> voicePackageNameMapBultin)
    
    private SetValuedMap<String, PackageItemInfo> voicePackageNameMapBultin; //!<内置的，语音识别结果与包条目信息之间的映射关系。
    private MultiMap<String, PackageItemInfo> activityLabelPackageItemInfoMap=new MultiValueMap<>(); //!<活动的标签，与活动本身信息之间的映射。
    private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。

    private String voiceRecognizeResultString; //!<语音识别结果。

    int ret = 0;

    private SpeechRecognizer mIat; //!<语言识别器。

    private String recordSoundFilePath; //!<录音文件路径．

    private int recognizeCounter=0; //!<识别计数器．

    private boolean voiceEndDetected=false; //!<是否已经探测到用户声音结束。

    private int mPageNumber = 1;//{1, 1, 1};

    private final int MSG_REFRESH   = 1;
    private final int MSG_LOAD_MORE = 2;
    private ShutDownAt2100LogicInterface launcherActivity = null; //!< The interface of shut down at 2100 logic.

    private int mCurrMsg = -1;

    private RequestQueue mQueue; //!<Volley请求队列。
    
    private ArrayList<ArticleInfo> builtinShortcuts =null; //!< 内置快捷方式列表。
    
    /**
    * 设置 活动的标签，与活动本身信息之间的映射。
    */
    public void setActivityLabelPackageItemInfoMap (MultiMap<String, PackageItemInfo> activityLabelPackageItemInfoMap) 
    {   
        this.activityLabelPackageItemInfoMap=activityLabelPackageItemInfoMap;
    } //public void setActivityLabelPackageItemInfoMap (MultiMap<String, PackageItemInfo> activityLabelPackageItemInfoMap)
    
    public void setVoiceShortcutIdMap (HashMap<String, HxShortcutInfo> voiceShortcutIdMap) //!<语音识别结果与快捷方式编号之间的映射关系．
    {
      this.voiceShortcutIdMap=voiceShortcutIdMap;
        
      if (voiceShortcutIdMapBuiltin!=null)
      {
        this.voiceShortcutIdMap.putAll(voiceShortcutIdMapBuiltin); // 合并。
      }
    }
    
    /**
    * 设置语音识别结果与包条目信息之间的映射关系。本设备独有的
    */
    public void  setVoicePackageNameMap (SetValuedMap<String, PackageItemInfo> voicePackageNameMap) 
    {
      this.voicePackageNameMap=voicePackageNameMap;
    } //public void  setVoicePackageNameMap (SetValuedMap<String, PackageItemInfo> voicePackageNameMap)
    
    public void setInternationalizationDataPackageNameMap(HashMap<String, String>  internationalizationDataPackageNameMap)
    {
      this.internationalizationDataPackageNameMap=internationalizationDataPackageNameMap;
    
      Log.d(TAG, "setInternationalizationDataPackageNameMap, map: " + this.internationalizationDataPackageNameMap); // Debug.
    }
    
    /**
    * 选择随机端口。
    */
    private int chooseRandomPort() 
    {
      int randomIndex=1239; //随机选择一个文件。

      return randomIndex;
    } //private int chooseRandomPort()

    /**
     * 向语音指令服务器发送请求。
     * @param voiceRecognizeResultString 语音识别结果
     */
    private void requestVoiceCommandServer(String voiceRecognizeResultString)
    {
      //向后端发送请求：
      cloudRequestorZzaqwb.sendAudioSpeechRecognitionRequest(voiceRecognizeResultString); //发送请求
    } //private void requestVoiceCommandServer(String voiceRecognizeResultString)

    /**
     * 设置结果，发送语音关联数据的结果。
     * @param result 发送结果。
     */
    public void setSendVoiceAssociationDataResult(Boolean result)
    {
      sentVoiceAssociationData=result; //记录。
    } //public void setSendVoiceAssociationDataResult(Boolean result)

    /**
     * Report that the operation has failed.
     * @param string 服务器回复的结果说明文字。
     */
    protected void reportOperationFail(String string)
    {
      Toast.makeText(HxLauncherApplication.getAppContext(), string, Toast.LENGTH_LONG).show();   //做一个提示，Failed adding address ,please retry.
    } //protected void reportOperationFail()

    private static final String TAG="ShutDownAt2100Logic"; //!< The tag for debug code.
    private final String categoryName="default"; //!<要显示的分类的名字。
    
    /**
     * 载入内置的语音识别结果与快捷方式信息之间的映射。
     */
    private void loadBuiltinVoiceShortcutMap()
    {
      LoadBuiltinVoiceShortcutMapTask translateRequestSendTask =new LoadBuiltinVoiceShortcutMapTask(); //创建异步任务。

      translateRequestSendTask.execute(this); //执行任务。
    } //private void loadBuiltinVoiceShortcutMap()

    /**
     * 载入内置的语音识别结果与包条目信息之间的映射。
     */
    private void loadBuiltinVoicePackageNameMap()
    {
      LoadBuiltinVoicePackageNameMapTask translateRequestSendTask =new LoadBuiltinVoicePackageNameMapTask(); //创建异步任务。

      translateRequestSendTask.execute(this); //执行任务。
    } //private void loadBuiltinVoicePackageNameMap()

    /**
     * 载入映射，对于服务器的回复中，要忽略掉的识别结果映射关系
     */
    private void loadServerVoiceCommandReponseIgnore()
    {
      LoadServerVoiceCommandReponseIgnoreTask translateRequestSendTask =new LoadServerVoiceCommandReponseIgnoreTask(); //创建异步任务。

      translateRequestSendTask.execute(this); //执行任务。
    } //private void loadServerVoiceCommandReponseIgnore()
    
    /**
    *  Check shut down time.
    */
    public void testShutDown()
    {
      shutDownAt2100Manager.executeFallBackShutDown(); // Execute fall back shut down.
    } // public void testShutDown()

    /**
     *  Check shut down time.
     */
    public void checkShutDownTime()
    {
      shutDownAt2100Manager.checkShutDownTime(); // Check shut down time.
      
      boolean exceededShutDownTime=shutDownAt2100Manager.getExceededShutDownTime(); // Get the status of whether exceeded the shut down time.
      
      String shutDownAt2100PackageName="com.stupidbeauty.shutdownat2100androidnative"; // Shut down at 2100 pakcgae name.
      
      if (exceededShutDownTime) // Exceeded
      {
        PackageInformationManager packageInformatinManager = new PackageInformationManager(); // The package information manager.
        // boolean installed = packageInformatinManager.checkInstalled(packageName); // 检查该应用是不是已经安装了。
        boolean currentlyInstalledShutDownAt2100 = packageInformatinManager.checkInstalled(shutDownAt2100PackageName); // Check whether iit is installed.
        
        if (currentlyInstalledShutDownAt2100) // Currently, installed.
        {
          shutDownAt2100Manager.setEverInstalledShutDownAt2100(); // Remember , ever installed shut down at 2100.
        } // if (!currentlyInstalledShutDownAt2100) // Current, shut down at 2100 is not installed
        else // Current, shut down at 2100 is not installed
        {
          boolean everInstalledShutDownAt2100=shutDownAt2100Manager.getEverInstalledShutDownAt2100(); // Check , if installed shut down at 2100 ever.

          if (everInstalledShutDownAt2100) // Ever installed shut down at 2100 application.
          {
            HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。
            shutDownAt2100Manager.sendShutDownAt2100InstalledBroadcast(application, true); // Send the broadcast to make the other applications know.
            launcherActivity.requestDownloadApk(shutDownAt2100PackageName); // Request to download and install shut down at 2100 apk.
            
            shutDownAt2100Manager.executeFallBackShutDown(); // Execute fall back shut down.
          } // if (everInstalledShutDownAt2100) // Ever installed shut down at 2100 application.
        } // else // Currently, installed.
      } // if (exceededShutDownTime) // Exceeded
    } // public void checkShutDownTime()

    /**
     * 保存映射。
     */
    private void saveVoicePackageNameMap()
    {
      VoicePackageNameMapSaveTask translateRequestSendTask =new VoicePackageNameMapSaveTask(); //创建异步任务。

      translateRequestSendTask.execute(voicePackageNameMap); //执行任务。
    } //private void saveVoicePackageNameMap()

    /**
     * 初始化本地日志工具。
     */
    private void initLocalLogUtil()
    {
      Thread.setDefaultUncaughtExceptionHandler(new LanImeUncaughtExceptionHandler()); // 设置未捕获的异常处理器。
    } //private void initLocalLogUtil()

    /**
     * 保存应用的启动计数数据。
     */
    private void saveApplicationLaunchCount()
    {
    } //private void saveApplicationLaunchCount()

    /**
     * 构造映射，应用的国际化名字与包名之间的映射。
     */
    private void buildInternationalizationDataPackageNameMap()
    {
        BuildInternationalizationDataPackageNameMapTask translateRequestSendTask =new BuildInternationalizationDataPackageNameMapTask(); //创建异步任务。

        translateRequestSendTask.execute(this); //执行任务。
    } //private void buildInternationalizationDataPackageNameMap()

    /**
     * 构造映射，活动的标签，与活动的包条目信息之间的映射。用于语音识别之后快速命中活动。
     */
    private void buildActivityLabelPackageItemInfoMap()
    {
        BuildActivityLabelPackageItemInfoMapTask translateRequestSendTask =new BuildActivityLabelPackageItemInfoMapTask(); //创建异步任务。

        translateRequestSendTask.execute(this); //执行任务。
    } //private void buildActivityLabelPackageItemInfoMap()

  /**
  * 从映射中寻找目标快捷方式的包名。从用户自己积累的映射中寻找。
  * @param voiceShortcutIdMap 映射对象
  * @return 找到的包名
  */
  private String findVoiceTargetMapShortcutPackageName(HashMap<String, HxShortcutInfo> voiceShortcutIdMap)
  {
    String result="";
      
    if (voiceShortcutIdMap!=null) // The map exists
    {
      if (voiceShortcutIdMap.containsKey(voiceRecognizeResultString)) //有对应的映射关系。用户自己积累的语音指令与包条目映射。
      {
        String packageName=voiceShortcutIdMap.get(voiceRecognizeResultString).packageName; //获取包名。

        result=packageName; //命中了。
      } //if (voicePackageNameMap.contains(voiceRecognizeResultString)) //有对应的映射关系。
    } // if (voiceShortcutIdMap!=null) // The map exists

    return result;
  } //private String findVoiceTargetMapShortcutPackageName(HashMap<String, HxShortcutInfo> voiceShortcutIdMap)

    /**
     * 记录语音识别命中应用的数据
     * @param voiceRecognizeResultString 语音识别结果
     * @param packageName 包名
     * @param activityName 活动名
     * @param activityIconType 目标类型。活动还是快捷方式
     */
    private void rememberVoiceCommandHitData(String voiceRecognizeResultString, String packageName, String activityName, LauncherIconType activityIconType, VoiceCommandSourceType voiceCommandSourceType)
    {
        VoiceCommandHitDataObject voiceCommandHitDataObject=new VoiceCommandHitDataObject(); //创建实例

        voiceCommandHitDataObject.setVoiceRecognizeResult(voiceRecognizeResultString);
        voiceCommandHitDataObject.setPackageName(packageName);
        voiceCommandHitDataObject.setActivityName(activityName);
        voiceCommandHitDataObject.setIconType(activityIconType);
        voiceCommandHitDataObject.setVoiceCommandSourceType(voiceCommandSourceType);

        voiceCommandHitDataStack.push(voiceCommandHitDataObject); //加入栈中

        Log.d(TAG, "rememberVoiceCommandHitData, stack size: " + voiceCommandHitDataStack.size()); //Debug.
    } //private boolean rememberVoiceCommandHitData(String voiceRecognizeResultString, String packageName, String activityName, LauncherIconType activityIconType)

    /**
    * 检查该应用是不是被锁住了。
    */
    private boolean checkApplicationLocked(Intent launchIntent)
    {
      boolean result=true; //结果。

      if (launchIntent!=null) //启动意图存在。
      {
        String packageName=launchIntent.getComponent().getPackageName(); //获取包名。
        String activityName=launchIntent.getComponent().getClassName(); //获取活动的类名。

        String activity=activityName; //活动名字。

        HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
        
        HashSet<String> applicationLockSet=hxLauncherApplication.getApplicationLockSet(); // 获取应用锁集合。

        result=applicationLockSet.contains(packageName); // 是否 包含。
      } //if (launchIntent!=null) //启动意图存在。
      else //启动意图不存在。
      {
        result=false; //未导出。
      } //else //启动意图不存在。

      return result;
    } // private boolean checkApplicationLocked(Intent launchIntent)

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

    /**
     * 检查并获取唤醒锁。
     * @param launchIntent 启动意图。
     */
    public ShutDownAt2100Logic(ShutDownAt2100LogicInterface launchIntent)
    {
      launcherActivity=launchIntent; // Remember launcher activity.
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      shutDownAt2100Manager=new ShutDownAt2100Manager(hxLauncherApplication); // Create shut down at 2100 manager.
    } //private void checkAndAquireWakeLock(Intent launchIntent)

    /**
     * 检查并更正启动意图中的类名。
     * @param launchIntent 要检查的启动意图。
     */
    private void checkAndCorrectClassNameInLauncherIntent(Intent launchIntent)
    {
        String className=launchIntent.getComponent().getClassName(); //获取类名。

        int indexOfDollar=className.indexOf('$'); //寻找美元符号。

        if (indexOfDollar>0) //存在美元符号。
        {
            int indexBeforeDollar=indexOfDollar-1; //复制到前一个字符为止。
            String correctedClassName=className.substring(0, indexBeforeDollar); //切出开头的部分。

            String packageName=launchIntent.getComponent().getPackageName(); //获取包名。

            launchIntent.setClassName(packageName, correctedClassName); //设置类名。
        } //if (indexBeforeDollar>0) //存在美元符号。
    } //private void checkAndCorrectClassNameInLauncherIntent(Intent launchIntent)

}
