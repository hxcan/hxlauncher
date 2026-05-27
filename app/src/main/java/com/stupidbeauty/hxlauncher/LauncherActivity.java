package com.stupidbeauty.hxlauncher;

import com.stupidbeauty.hxlauncher.util.MetadataPreloader;
import static com.stupidbeauty.blindbox.Constants.Networks.RabbitMQPassword;
import static com.stupidbeauty.blindbox.Constants.Networks.RabbitMQUserName;
import static com.stupidbeauty.blindbox.Constants.Networks.TRANSLATE_REQUEST_QUEUE_NAME;
import com.stupidbeauty.appstore.core.DownloadFailureReporter;
import android.app.usage.UsageEvents;
// import android.app.usage.UsageEvents.Event;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import android.app.usage.UsageStatsManager;
import android.provider.Settings;
import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.bumptech.glide.Glide;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import com.stupidbeauty.hxlauncher.Constants;
import com.bumptech.glide.Glide;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Handler;
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
import com.stupidbeauty.hxlauncher.manager.PackageInformationManager;
import com.stupidbeauty.hxlauncher.manager.NotificationControlManager;
import com.stupidbeauty.hxlauncher.interfaces.ShutDownAt2100LogicInterface;
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
// import android.os.Vibrator;
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
import android.widget.TextView;
import android.widget.Toast;
import com.stupidbeauty.hxlauncher.asynctask.BuildInternationalizationDataPackageNameMapTask;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.adapter.FlipAnimationAdapter;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
// import com.android.volley.RequestQueue;
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
import com.stupidbeauty.hxlauncher.scanner.AppInfoScanner;
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
import com.stupidbeauty.hxlauncher.manager.VoicePackageMapRegretManager;
import com.stupidbeauty.hxlauncher.manager.ApkDownloadManager;
import com.stupidbeauty.hxlauncher.manager.VoiceIntentManager;
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
import java.util.Set;
import java.util.Stack;
import butterknife.BindView;
import butterknife.BindView;
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
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED;
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
import com.stupidbeauty.hxlauncher.service.NotificationListernService02;
import android.os.Process;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.stupidbeauty.hxlauncher.util.PermissionHelper;
import com.stupidbeauty.hxlauncher.rpc.VoiceCommandHitDataReporter;
import com.stupidbeauty.hxlauncher.utils.FileLogger;

public class LauncherActivity extends Activity implements ShutDownAt2100LogicInterface
{
  private DownloadFailureReporter downloadFailureReporter=new DownloadFailureReporter(); //!< download failur reporetr.
  private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识
  private static final int REQUEST_CODE_USAGE_STATS = 194;
  private ApplicationInformationAdapter mAdapter; //!<适配器。
  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; //!<录音权限。
  private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限
  // private static final String PERMISSION_INSTALL_PACKAGE = Manifest.permission.REQUEST_INSTALL_PACKAGES; //!< 安装应用程序权限
  private SetValuedMap<String, PackageItemInfo> voicePackageNameMapBultin; //!<内置的，语音识别结果与包条目信息之间的映射关系。
  private static final String TAG="LauncherActivity"; //!< 输出调试信息时使用的标记。
  private ApkDownloadManager apkDownloadManager = new ApkDownloadManager(this); //!< Apk download manager.
  private VoiceCommandHitDataReporter voiceCommandHitDataReporter=new VoiceCommandHitDataReporter(); //!< The voice command hit data reporter.
  private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private BuiltinFtpServerErrorListener builtinFtpServerErrorListener=null; //!< the builtin ftp server error listener.  Chen xin.
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
  @BindView(R.id.circularProgressBar) CircularProgressBar circularProgressBar; //!< The circular progrewss bar.
  private AppInfoScanner appInfoScanner; // 成员变量
  @BindView(R.id.launchRipple) RippleView launchRipple; //!<用于转场动画的视图对象
  private PermissionHelper permissionHelper; // ← 新增
  @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!<应用程序图标
  @BindView(R.id.rightTextoperationMethodactTitletextView2) TextView rightTextoperationMethodactTitletextView2; //!<应用程序名字
  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private VoicePackageMapRegretManager voicePackageMapRegretManager = new VoicePackageMapRegretManager(); //!< Voice command package map regret manager.
  private AnimationDrawable rocketAnimation; //!<录音按钮变暗
  private CloudRequestorZzaqwb cloudRequestorZzaqwb=new CloudRequestorZzaqwb(); //!<云端请求发送器
  private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈
  private BuiltinShortcutsManager builtinShortcutsManager=new BuiltinShortcutsManager(); //!<内置快捷方式管理器
  private ShutDownAt2100Logic shutDownAt2100Logic= null; //!< Logic with shutdownat2100.
  @BindView(R.id.hitApplicationIcon) ImageView hitApplicationIcon; //!<命中的应用的图标。
  @BindView(R.id.gridIcon) ImageView gridIcon; //!< The grid icon.
  @BindView(R.id.launcher_activity) RelativeLayout launcher_activity; //!<整个启动活动
  @BindView(R.id.voiceAssistantLayout) RelativeLayout voiceAssistantLayout; //!<语音助手 布局。 陈欣
  @BindView(R.id.loveAnimation) Love2  loveAnimation; //!<点赞爱心动画
  @BindView(R.id.microphoneIcon) ImageView microphoneIcon; //!<麦克风图标。
  @BindView(R.id.applicationNameTextView2) TextView applicationNameTextView2; //!< 介绍文字标签。

  private boolean mscIsInitialized=false; //!<讯飞语音识别是否已经初始化。

  private HashMap<String, String> serverVoiceCommandResponseIgnoreMap=null; //!<服务器的回复中，要忽略掉的关系映射

  private HashMap<String, ShortcutInfo> shortcutTitleInfoMap; //!<快捷方式的标题与快捷方式对象本身的映射。
  private HashMap<String, ShortcutInfo> shortcutIdInfoMap; //!<快捷方式的编号与快捷方式对象本身的映射
  private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
  private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。

  AutoRunManager autoRunManager=new AutoRunManager(this); //!<自动启动管理器。
  PowerManager.WakeLock wakeLock=null; //!<游戏辅助唤醒锁。
  private boolean activityHasBeenResumed=false; //!<活动是否处于被继续的状态，即正常的运行状态。
  private boolean sentVoiceAssociationData=false; //!<是否已经成功发送语音指令关联应用程序数据。

  private List<ShortcutInfo> shortcutInfos=null; //!< 快捷方式列表。

  private ArrayList<ArticleInfo> articleInfoArrayList = null; //!< 应用程序信息列表。

  private boolean builtinShortcutsVisible= true; //!< 内置 快捷方式是否可见。
    
  /**
  * 设置 内置 快捷方式是否可见。
  */
  public void setBuiltinShortcutsVisible (boolean builtinShortcutsVisible)
  {
      this.builtinShortcutsVisible=builtinShortcutsVisible;

      if (mAdapter!=null)
      {
          mAdapter.toggleBuiltinShortcutsVisible(builtinShortcutsVisible); //切换，内置快捷方式是否可见。
      } ///!< 内置 快捷方式是否可见。
  } //public void setBuiltinShortcutsVisible (boolean builtinShortcutsVisible)
    
  /**
  * 应用程序信息列表。
  */
  public void setArticleInfoArrayList (ArrayList<ArticleInfo> articleInfoArrayList )
  {
      this.articleInfoArrayList=articleInfoArrayList;

      if (mAdapter!=null)
      {
          applyArticleInfoArrayList(); // 应用应用程序信息列表。
      } ///!< 应用程序信息列表。
  } //public void setArticleInfoArrayList (ArrayList<ArticleInfo> articleInfoArrayList )
    
    public void setShortcutInfos(List<ShortcutInfo> shortcutInfos)
    {
        this.shortcutInfos=shortcutInfos;
        
        if (mAdapter!=null) // 适配器存在
        {
            applyShortcutInfos(); // 应用快捷方式。
        } //if (mAdapter!=null) // 适配器存在
    } //public void setShortcutInfos(List<ShortcutInfo> shortcutInfos)
    
    /**
    * 应用快捷方式。
    */
    private void applyShortcutInfos() 
    {
        if (shortcutInfos!=null)
        {
          mAdapter.clearShortcuts(); // Clear shortcuts.
          for(ShortcutInfo shortcutInfo: shortcutInfos) //一个个地显示。
          {
              mAdapter.addShortcut(shortcutInfo); //添加快捷方式。
          } //for(ShortcutInfo shortcutInfo: shortcutInfos) //一个个地显示。

          mAdapter.updateItemPositionMap(); //整体更新条目的位置映射
          mAdapter.notifyDataSetChanged(); //通知，数据集发生变化。
        }
    } //private void applyShortcutInfos()
    
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
      if (!permissionHelper.handlePermissionsResult(requestCode, permissions, grantResults))
      {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      }
      // 注意：原逻辑在这里没有做任何事，所以不需要额外处理
    }
    private boolean sentVoiceShortcutAssociationData=false; //!<是否已经成功发送语音指令关联快捷方式的数据。

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
    
    private MultiMap<String, PackageItemInfo> activityLabelPackageItemInfoMap=new MultiValueMap<>(); //!<活动的标签，与活动本身信息之间的映射。
    private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。

    @BindView(R.id.wallpaper) ImageView wallpaper; //!<墙纸视图。

    @BindView(R.id.progressBar) ProgressBar progressBar; //!<进度条。

    private String voiceRecognizeResultString; //!<语音识别结果。

    int ret = 0;

    @BindView(R.id.statustextView) TextView statustextView; //!<用来显示状态的文字标签。

    private SpeechRecognizer mIat; //!<语言识别器。

    private String recordSoundFilePath; //!<录音文件路径．

    private int recognizeCounter=0; //!<识别计数器．

    // private Vibrator vibrator;

    private boolean voiceEndDetected=false; //!<是否已经探测到用户声音结束。

    private int mPageNumber = 1;//{1, 1, 1};

    private final int MSG_REFRESH   = 1;
    private final int MSG_LOAD_MORE = 2;
    private boolean mIsLastPage = true;

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
    
  private void applyBuiltinShortcuts()
  {
      mAdapter.setBuiltinShortcuts(builtinShortcuts); //设置文章信息列表。
      mAdapter.notifyDataSetChanged(); //通知数据变更。
  } //private void applyBuiltinShortcuts()
    
  /**
  * 设置内置快捷方式列表。
  */
  public void setBuiltinShortcuts (ArrayList<ArticleInfo> builtinShortcuts )
  {
      this.builtinShortcuts= builtinShortcuts;

      if (mAdapter!=null)
      {
          applyBuiltinShortcuts();
      }
  } //public void setBuiltinShortcuts (ArrayList<ArticleInfo> builtinShortcuts )
    
  public void setVoiceShortcutIdMap (HashMap<String, HxShortcutInfo> voiceShortcutIdMap) //!<语音识别结果与快捷方式编号之间的映射关系．
  {
    this.voiceShortcutIdMap=voiceShortcutIdMap;

    if (voiceShortcutIdMapBuiltin!=null)
    {
      this.voiceShortcutIdMap.putAll(voiceShortcutIdMapBuiltin); // 合并。
    }
  }
    
  /**
  * 语音识别结果与快捷方式编号之间的映射关系．
  */
  public void setVoiceShortcutIdMapBuiltin (HashMap<String, HxShortcutInfo> voiceShortcutIdMapBuiltin)
  {
    this.voiceShortcutIdMapBuiltin=voiceShortcutIdMapBuiltin;

    if (voiceShortcutIdMap!=null) // voice short cut id map exists
    {
      this.voiceShortcutIdMap.putAll(voiceShortcutIdMapBuiltin); // 合并。
    } // if (voiceShortcutIdMap!=null) // voice short cut id map exists
  } //public void setVoiceShortcutIdMapBuiltin (HashMap<String, HxShortcutInfo> voiceShortcutIdMapBuiltin)
    
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
  }
    
  /**
  * Choose a port
  */
  private int chooseRandomPort()
  {
    int randomIndex = 1239; // Choose a port.

    return randomIndex;
  } //private int chooseRandomPort()

  private final RecognizerListener mRecognizerListener=new RecognizerListener()
  {
    @Override
    public void onVolumeChanged(int i, byte[] bytes)
    {
    }

    @Override
    public void onBeginOfSpeech()
    {
      voiceRecognizeResultString=""; //重置识别结果。
    }

    @Override
    public void onEndOfSpeech()
    {
      voiceEndDetected=true; //记录，已经探测到用户声音结束。

      animateRecognizeButtonDark(); //动画变暗

      assesRestartSpeechRecognize(); //考虑是否要重新启动语音识别过程。
    } //public void onEndOfSpeech()

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b)
    {
      progressBar.setVisibility(View.INVISIBLE); //隐藏显示进度条。

      //完整内容:
      String text=recognizerResult.getResultString(); //结果字符串。

      Gson gson=new Gson(); //创建gson对象。

      VoiceRecognizeResult voiceRecognizeResult=gson.fromJson(text, VoiceRecognizeResult.class); //解析成结果对象。
      String saidText=voiceRecognizeResult.getSaidText(); //获取完整的说出内容。

      voiceRecognizeResultString=voiceRecognizeResultString+saidText; //追加结果。

      Log.i(TAG,"onResult, result: "+voiceRecognizeResultString+", is empty?: " + voiceRecognizeResultString.isEmpty()+ ", length: " + voiceRecognizeResultString.length()); //Debug.

      boolean isLast=voiceRecognizeResult.isLs(); //获取属性，是否是最终结果。

      //             boolean exceededShutDownTime=shutDownAt2100Manager.isExceededShutDownTime();

      if (voiceRecognizeResultString.length()<=IgnoreVoiceResultLength) //内容过短，不处理。
      {
        Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
        showIconList(); //显示图标列表

        applicationNameTextView2.setText(R.string.empty); // 清空内容。
      } //if (voiceRecognizeResultString.isEmpty()) //是空白内容，不处理。
      else //不是空白内容，处理。
      {
        applicationNameTextView2.setText(voiceRecognizeResultString); // 显示识别结果。
        launchByVoiceRecognize(isLast); //按照语音识别结果来启动应用的活动。
      } //else //不是空白内容，处理。
    } //public void onResult(RecognizerResult recognizerResult, boolean b)


    @Override
    public void onError(SpeechError speechError)
    {
      progressBar.setVisibility(View.INVISIBLE); //隐藏显示进度条。
      String errorText=speechError.getErrorDescription(); //获取错误信息。
      int errorCode=speechError.getErrorCode(); //获取错误码。

      //             做出错后的处理，陈欣。

      Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
      showIconList(); //显示图标列表

      animateRecognizeButtonDark(); //动画变暗

          permissionHelper.checkPermission(); // Check permiisson again
    } // public void onError(SpeechError speechError)

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle)
    {
    }
  };

  /**
    * 根据语音指令启动应用。
    * @param isFinalResult 是否是最终识别结果。
    */
  private void launchByVoiceRecognize(boolean isFinalResult)
  {
    if (foundActivity) //此次语音识别过程中已经命中了应用，则不再查找。
    {
    } //if (foundActivity) //此次语音识别过程中已经命中了应用，则不再查找。
    else //此次语音识别过程中还没有命中应用。
    {
      HxLauncherApplication baseApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      ApplicationListData applicationListData =baseApplication.getApplicationListData(); //获取本地服务器列表数据对象。

      if (applicationListData !=null) //存在对象。
      {
        foundActivity=findVoiceTargetApplicationNameAndLaunch(); //根据语音识别结果，在已安装的应用程序列表中查找，。

        if (foundActivity) //命中了。
        {
        } //if (foundActivity) //命中了。
        else //未命中。
        {
          requestVoiceCommandServer(voiceRecognizeResultString); //向语音指令服务器发送请求。
        } //else //未命中。
      } //if (applicationListData!=null) //存在对象。
    } //else //此次语音识别过程中还没有命中应用。
  } //private void launchByVoiceRecognize()

  /**
    * 向语音指令服务器发送请求。
    * @param voiceRecognizeResultString 语音识别结果
    */
  private void requestVoiceCommandServer(String voiceRecognizeResultString)
  {
    //向后端发送请求：
    cloudRequestorZzaqwb.sendAudioSpeechRecognitionRequest(voiceRecognizeResultString); //发送请求
  } //private void requestVoiceCommandServer(String voiceRecognizeResultString)

  private void processEventVoiceLaunchFailed()
  {
    showTouchHand();

    Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
    showIconList(); //显示图标列表
    tryDownloadApkForVoiceResult(); //尝试根据语音识别结果下载安装包
  }

  private void tryDownloadApkForVoiceResult()
  {
    //尝试下载：
    boolean foundUrl = apkDownloadManager.tryDownloadApkAssociatedToPackage(voiceRecognizeResultString, voiceShortcutIdMap, voicePackageNameMapBultin); //尝试根据语音识别结果，下载对应包名的应用。

    if (foundUrl) //找到了下载地址。
    {
    } //if (foundUrl) //找到了下载地址。
    else //未找到下载地址
    {
      apkDownloadManager.tryDownloadApkAssociatedToVoiceCommand(voiceRecognizeResultString); // 尝试根据语音识别结果，下载对应的应用。
    } //else //未找到下载地址
  } // private void tryDownloadApkForVoiceResult()

  private void showTouchHand()
  {
    //显示手指：
    hitApplicationIcon.setImageResource(R.drawable.d09069860932b3c86a7cfae8570e2handindexfingersleeveillustrationbyvexels); //显示手。
    hitApplicationIcon.setVisibility(View.VISIBLE); //显示出来。

    hitApplicationIcon.setRotationX(-90); //先跳着旋转

    hitApplicationIcon.animate().rotationX(0).start(); //动画，旋转。 陈欣
  }
    
  /**
  * 跳转到应用程序解锁活动。
  */
  private void gotoApplicationUnlockActivity(Intent launchIntentForApplication)
  {
    Intent launchIntent=new Intent(this, ApplicationUnlockActivity.class); //启动意图。

    String packageName=launchIntentForApplication.getComponent().getPackageName(); //获取包名。
    String activityName=launchIntentForApplication.getComponent().getClassName(); //获取活动的类名。

    launchIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
    launchIntent.putExtra(EXTRA_COMPONENT_NAME, activityName); // 设置部件名字。

    startActivity(launchIntent); //启动活动。
  } // private void gotoApplicationUnlockActivity(Intent launchIntent)

  /**
    * 显示应用程序信息活动
    * @param itemView 视图对象
    * @param packageName 包名
    */
  public void showApplicationInformation(View itemView, String packageName, String activityName)
  {
    Intent launchIntent=new Intent(this, ApplicationInformationActivity.class); //启动意图。

    launchIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
    launchIntent.putExtra(EXTRA_COMPONENT_NAME, activityName); // 设置部件名字。

    startActivity(launchIntent); //启动活动。
  } //public void showApplicationInformation(View itemView, String packageName)

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

  @BindView(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。


  private final String categoryName="default"; //!<要显示的分类的名字。
  final Map<String,PackageCountObject> packageNameCountObjectMap=new HashMap<>(); //软件包名字与计数对象之间的映射。
  final List<PackageCountObject> packageCountObjectList=new ArrayList<>(); //!<计数对象列表。
    
  /**
  *  设置应用程序信息适配器。
  */
  public void  setApplicationInformationAdapter (ApplicationInformationAdapter mAdapter)
  {
      this.mAdapter=mAdapter;

      if (shortcutInfos!=null) // 快捷方式列表存在
      {
          applyShortcutInfos(); // 应用快捷方式列表。
      } //if (shortcutInfos!=null) // 快捷方式列表存在

      if (articleInfoArrayList!=null) // 应用程序列表存在
      {
          applyArticleInfoArrayList(); // 应用应用程序列表。
      } //if (articleInfoArrayList!=null) // 应用程序列表存在

      mAdapter.toggleBuiltinShortcutsVisible(builtinShortcutsVisible); //切换，内置快捷方式是否可见。

      if (builtinShortcuts!=null)
      {
        applyBuiltinShortcuts();
      }
  } //public void  setApplicationInformationAdapter (ApplicationInformationAdapter mAdapter)
    
  /**
  * 应用应用程序列表。
  */
  private void applyArticleInfoArrayList()
  {
    mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
    mAdapter.notifyDataSetChanged(); //通知数据变更。
  } //private void applyArticleInfoArrayList()

  /**
    * 要求显示系统的墙纸在本活动后面。
    */
  private void askShowSystemWallpaper()
  {
    WindowManager.LayoutParams p=getWindow().getAttributes();
    p.flags |= WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
  } //private void askShowSystemWallpaper()
    
  /**
  * 启动内置 FTP 服务器。
  */
  private void startBultinFtpServer()
  {
    builtinFtpServer=new BuiltinFtpServer(this); //!< The builtin ftp server.
    builtinFtpServerErrorListener=new BuiltinFtpServerErrorListener(this); // Create the builtin ftp server error listener.
    int actualPort = chooseRandomPort(); // 选择随机端口。
    builtinFtpServer.setPort(actualPort); // 设置自动选择FTP监听端口。
    builtinFtpServer.setAllowActiveMode(false); // 设置不允许主动模式。
    builtinFtpServer.setErrorListener(builtinFtpServerErrorListener); // Set the error listener. Chen xin.
    builtinFtpServer.start(); //启动服务器。
  } //        startBultinFtpServer(); // 启动内置 FTP 服务器。
    
  /**
  * 计划启动内置 FTP 服务器。
  */
  private void scheduleStartBuiltinFtpServer()
  {
    Timer timerObj = new Timer();
    TimerTask timerTaskObj = new TimerTask()
    {
      public void run()
      {
        startBultinFtpServer(); // 启动内置 FTP 服务器。
      }
    };
    timerObj.schedule(timerTaskObj, 15000); // 延时启动。
  } //private void scheduleStartBuiltinFtpServer()

  @Override
  /**
  * 活动被创建。
  */
  protected void onCreate(Bundle savedInstanceState)
  {
    long startTimestamp=System.currentTimeMillis(); // 记录开始时间戳。

    super.onCreate(savedInstanceState);

    appInfoScanner = new AppInfoScanner(this);
    permissionHelper = new PermissionHelper(this); // ← 初始化

    askShowSystemWallpaper(); //要求显示系统的墙纸在本活动后面。

    requestWindowFeature(Window.FEATURE_NO_TITLE); //不显示标题栏。

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
    {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    setContentView(R.layout.launcher_activity); // 显示界面。

    ButterKnife.bind(this); //视图注入。

    voiceUi=new VoiceUi(this); // 创建语音交互对象。

    permissionHelper.checkPermission(); //检查权限。

    assessInitializeMsc(); // 考虑要不要初始化讯飞语音识别。
    Log.w(TAG, "onCreate, 789, timestamp: " + System.currentTimeMillis()); //Debug.

    // initLocalLogUtil(); //初始化本地日志工具。【已移除：旧崩溃检测器】
    Log.w(TAG, "onCreate, 791, timestamp: " + System.currentTimeMillis()); //Debug.

    scheduleStartBuiltinFtpServer(); // 计划启动内置 FTP 服务器。

    registerBroadcastReceiver(); //注册广播事件接收器。
    Log.w(TAG, "onCreate, 795, timestamp: " + System.currentTimeMillis()); //Debug.

    registerLauncherAppsCallback(); //注册启动器应用的回调对象。

    bindAdapter(); //绑定适配器。
    Log.w(TAG, "onCreate, 800, timestamp: " + System.currentTimeMillis()); //Debug.

    reqGameData(); //开始获取数据。

    // 保留其他未聚合的方法调用
    loadVoicePackageNameMap(); //载入语音识别结果与包名之间的映射。
    Log.w(TAG, "onCreate, 807, timestamp: " + System.currentTimeMillis()); //Debug.
    loadVoiceShortcutIdMap(); //载入语音识别结果与快捷方式之间的映射。

    // 只替换 preload 方法包含的 4 个调用
    MetadataPreloader.preload(this);

    loadShortcuts(); //载入快捷方式列表。

    Log.w(TAG, "onCreate, 820, timestamp: " + System.currentTimeMillis()); //Debug.
    loadPreference(); //载入选项。

    loadBuiltinShortcuts(); //载入内置快捷方式列表。

    Log.w(TAG, "onCreate, 825, timestamp: " + System.currentTimeMillis()); //Debug.
    // checkDefaultLauncher(); //检查自己是不是默认启动器，并处理。

    initializeCloudRequestor(); // 初始化云端请求器

    Log.w(TAG, "onCreate, 830, timestamp: " + System.currentTimeMillis()); //Debug.
    createMicrophoneAnimation(); //Creqte microhpone animation

    // shutDownAt2100Manager=new ShutDownAt2100Manager(this);
    shutDownAt2100Logic=new ShutDownAt2100Logic(this);

    long endTimestamp=System.currentTimeMillis(); // 记录开始时间戳。
    Log.w(TAG, "onCreate, 834, leave onCreate, timestamp: " + System.currentTimeMillis()); //Debug.

    startNotifictionListenerService(); // Start the notification listener service.
  }

  /**
  * 初始化云端请求器
  */
  private void initializeCloudRequestor()
  {
      cloudRequestorZzaqwb.setContext(this); //设置上下文

      cloudRequestorZzaqwb.setRecognizerListener(voiceCommandListerner); //设置语音服务器结果监听器
  } //private void initializeCloudRequestor()

  /**
  * Create microphone animation
  */
  private void createMicrophoneAnimation() //Creqte microhpone animation
  {
    if (microphoneIcon!=null)
    {
      rocketAnimation=(AnimationDrawable) (microphoneIcon.getBackground());
    } // microphoneIcon
  } //private void createMicrophoneAnimation()

  com.stupidbeauty.hxlauncher.rpc.RecognizerListener voiceCommandListerner=new com.stupidbeauty.hxlauncher.rpc.RecognizerListener()
  {
    private static final String TAG="RecognizerListener"; //!<输出调试信息时使用的标记。

    @Override
    public void onResult(com.stupidbeauty.hxlauncher.rpc.RecognizerResult recognizerResult, boolean isFinalResult)
    {
      String resultPackageName=recognizerResult.getResultString(); //获取包名

      String ignorePackageName=null; //查找要忽略的包名。

      if (serverVoiceCommandResponseIgnoreMap==null)
      {
        loadServerVoiceCommandReponseIgnore(); // 载入映射。
      }
      else
      {
        ignorePackageName=serverVoiceCommandResponseIgnoreMap.get(voiceRecognizeResultString); //查找要忽略的包名。
      }

      Log.d(TAG, "556, onResult, ignore packange name: " + ignorePackageName); //Debug.

      if (resultPackageName.equals(ignorePackageName)) //要忽略
      {
        processEventVoiceLaunchFailed(); //后续步骤
      } //if (resultPackageName.equals(ignorePackageName)) //要忽略
      else  //不要忽略
      {
        showHitApplication(resultPackageName); //显示命中了应用的图标。

        boolean launchSucces= launchApplicationByPackageName(resultPackageName); //尝试启动包名对应的应用

        Log.d(TAG, "548, onResult, packange name: " + resultPackageName + ", launch success: " + launchSucces); //Debug.

        if (launchSucces) //启动成功
        {
          String activityName=null;
          rememberVoiceCommandHitData(voiceRecognizeResultString, resultPackageName, activityName, ActivityIconType, ServerVoiceCommandResponse); //记录语音识别命中应用的数据
        } //if (launchSucces) //启动成功
        else //启动失败
        {
          processEventVoiceLaunchFailed(); //后续步骤
        } //else //启动失败
      } //else  //不要忽略
    }

    @Override
    public void onError(com.stupidbeauty.hxlauncher.rpc.SpeechError speechError)
    {
      Log.d(TAG, "550, onError "); //Debug.

      processEventVoiceLaunchFailed(); //执行语音指令命中失败后的行为
    }
  };

  /**
    * 载入内置快捷方式列表。
    */
  private void loadBuiltinShortcuts()
  {
    LoadBuiltinShortcutsTask translateRequestSendTask =new LoadBuiltinShortcutsTask(); //创建异步任务。

    translateRequestSendTask.execute(this, mAdapter, builtinShortcutsManager); //执行任务。
  } //private void loadBuiltinShortcuts()

  /**
    * 载入选项。
    */
  private void loadPreference()
  {
    LoadPreferenceTask translateRequestSendTask =new LoadPreferenceTask(); //创建异步任务。

    translateRequestSendTask.execute(this, mAdapter); //执行任务。
  } //private void loadPreference()

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
    * 载入快捷方式列表。
    */
  public void loadShortcuts()
  {
    LoadShortcutsTask translateRequestSendTask =new LoadShortcutsTask(); //创建异步任务。

    translateRequestSendTask.execute(this, mAdapter); //执行任务。
  } //private void loadShortcuts()

  /**
    * 构造映射，快捷方式的标题与快捷方式对象之间的映射。
    * @param shortcutInfos 快捷方式列表。
    */
  public void buildShortcutTitleInfoMap(List<ShortcutInfo> shortcutInfos)
  {
    if (shortcutInfos != null) // The list exists
    {
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后才有钉住的快捷方式。
      {
        shortcutTitleInfoMap=new HashMap<>(); //创建映射。
        shortcutIdInfoMap=new HashMap<>(); //创建映射。

        for(ShortcutInfo shortcutInfo: shortcutInfos) //一个个地显示。
        {
          String title = shortcutInfo.getShortLabel().toString(); //获取标题。
          String shortcutId= shortcutInfo.getId(); //获取编号

          shortcutTitleInfoMap.put(title, shortcutInfo); //加入映射。
          shortcutIdInfoMap.put(shortcutId, shortcutInfo); //加入映射。
        } //for(ShortcutInfo shortcutInfo: shortcutInfos) //一个个地显示。
      } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后才有钉住的快捷方式。
    } // if (shortcutInfos!=null) // The list exists
  } //private void buildShortcutTitleInfoMap(List<ShortcutInfo> shortcutInfos)

  /**
    * 考虑要不要执行自动启动动作。
    */
  private void assessAutoRun()
  {
    autoRunManager.assessAutoRun(); //由自动启动管理器来管理这件事。
  } //private void assessAutoRun()

  /**
  * 载入内置的语音识别结果与快捷方式信息之间的映射。
  */
  private void loadBuiltinVoiceShortcutMap()
  {
    MetadataPreloader.loadBuiltinVoiceShortcutMap(this);
  }

  /**
  * 载入内置的语音识别结果与包条目信息之间的映射。
  */
  private void loadBuiltinVoicePackageNameMap()
  {
    MetadataPreloader.loadBuiltinVoicePackageNameMap(this);
  }

  /**
    * 载入映射，对于服务器的回复中，要忽略掉的识别结果映射关系
    */
  private void loadServerVoiceCommandReponseIgnore()
  {
    LoadServerVoiceCommandReponseIgnoreTask translateRequestSendTask =new LoadServerVoiceCommandReponseIgnoreTask(); //创建异步任务。

    translateRequestSendTask.execute(this); //执行任务。
  } //private void loadServerVoiceCommandReponseIgnore()

  /**
    * 载入语音识别结果与快捷方式之间的映射。
    */
  private void loadVoiceShortcutIdMap()
  {
      LoadVoiceShortcutIdMapTask translateRequestSendTask =new LoadVoiceShortcutIdMapTask(); //创建异步任务。

      translateRequestSendTask.execute(this); //执行任务。
  } //private void loadVoiceShortcutIdMap()

  /**
    * 载入语音识别结果与包名之间的映射。
    */
  private void loadVoicePackageNameMap()
  {
      LoadVoicePackageNameMapTask translateRequestSendTask =new LoadVoicePackageNameMapTask(); //创建异步任务。

      translateRequestSendTask.execute(this); //执行任务。
  } //private void loadVoicePackageNameMap()

  /**
    * 保存映射
    */
  private void saveServerVoiceCommandResponseIgnoreMap()
  {
      VoicePackageMapMessageProtos.VoicePackageMapMessage.Builder translateRequestMessage= VoicePackageMapMessageProtos.VoicePackageMapMessage.newBuilder(); //创建一个消息对象。

      for(String currentVoiceRecognizeResult: serverVoiceCommandResponseIgnoreMap.keySet()) //一个个地保存。
      {
        String currentPackageName=serverVoiceCommandResponseIgnoreMap.get(currentVoiceRecognizeResult); //获取包名。

        VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage.Builder translateRequestMessageBuilder= VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage.newBuilder();

        translateRequestMessageBuilder.setVoiceRecognizeResult(currentVoiceRecognizeResult).setPackageName(currentPackageName); //设置各个字段。
        translateRequestMessageBuilder.setIconType(PbShortcutIconType); //设置图标类型．

        translateRequestMessage.addMap(translateRequestMessageBuilder); //添加映射关系。
      } //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

      //放入一张照片：
      //随机选择一张照片并复制：
      File photoFileDcim=findRandomPhotoFileDcim(); //随机寻找一个照片文件。

      try //尝试构造请求对象，并且捕获可能的异常。
      {
          ByteString photoByteArray=null; //照片的字节数组。

          if (photoFileDcim!=null) //找到了照片文件。
          {
              byte[] photoBytes= FileUtils.readFileToByteArray(photoFileDcim); //将照片文件内容全部读取。
              photoByteArray=ByteString.copyFrom(photoBytes); //构造照片的字节字符串。
          } //if (photoFile!=null) //找到了照片文件。

          long eventTimeStamp=System.currentTimeMillis(); //获取时间戳。

          translateRequestMessage.setPictureFileContent(photoByteArray); //设置照片文件内容。
      } //try //尝试构造请求对象，并且捕获可能的异常。
      catch (Exception e)
      {
        e.printStackTrace();
      }

      byte[] serializedContent=translateRequestMessage.build().toByteArray(); //序列化成字节数组。

      VoiceIntentManager voiceIntentManager = new VoiceIntentManager(); // Create the voice intent manager object.
      File photoFile = voiceIntentManager.findServerVoiceCommandReponseIgnoreMapFile(); //寻找数据记录文件。

      try
      {
        FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
  } //private void saveServerVoiceCommandResponseIgnoreMap()

  /**
    * 保存映射。语音识别结果与快捷方式编号之间的映射．
    */
  private void saveVoiceShortcutIdMap()
  {
      VoicePackageMapMessageProtos.VoicePackageMapMessage.Builder translateRequestMessage= VoicePackageMapMessageProtos.VoicePackageMapMessage.newBuilder(); //创建一个消息对象。

      for(String currentVoiceRecognizeResult: voiceShortcutIdMap.keySet()) //一个个地保存。
      {
          String currentPackageName=voiceShortcutIdMap.get(currentVoiceRecognizeResult).packageName; //获取包名。
          String currentActivityName=voiceShortcutIdMap.get(currentVoiceRecognizeResult).shortcutId; //获取快捷方式编号。

          VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage.Builder translateRequestMessageBuilder= VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage.newBuilder();

          translateRequestMessageBuilder.setVoiceRecognizeResult(currentVoiceRecognizeResult).setPackageName(currentPackageName).setShortcutId(currentActivityName); //设置各个字段。
          translateRequestMessageBuilder.setIconType(PbShortcutIconType); //设置图标类型．

          translateRequestMessage.addMap(translateRequestMessageBuilder); //添加映射关系。
      } //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

      byte[] serializedContent=translateRequestMessage.build().toByteArray(); //序列化成字节数组。

      VoiceIntentManager voiceIntentManager = new VoiceIntentManager(); // Create the voice intent manager object.
      File photoFile = voiceIntentManager.findVoiceShortcutIdMapFile(); //寻找数据记录文件。

      try
      {
        FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }

      PreferenceManagerUtil.setVoiceShortCutIdMapVersion(BuildConfig.VERSION_CODE); //设置语音命中快捷方式数据文件的版本号
  } //private void saveVoiceShortcutIdMap()

    /**
     * 保存映射。
     */
    private void saveVoicePackageNameMap()
    {
      VoicePackageNameMapSaveTask translateRequestSendTask =new VoicePackageNameMapSaveTask(); //创建异步任务。

      translateRequestSendTask.execute(voicePackageNameMap); //执行任务。
    } //private void saveVoicePackageNameMap()

    /**
     * 更新语音命中应用映射数据文件的版本
     */
    private void updateVoicePackageNameMapVersion()
    {
      PreferenceManagerUtil.setVoicePackageNameMapVersion(BuildConfig.VERSION_CODE); //设置语音命中数据文件的版本号
    } //private void updateVoicePackageNameMapVersion()

    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    private  File findRandomPhotoFileDcim()
    {
      File result=null;

        String photoDirectoryPath= com.stupidbeauty.hxlauncher.Constants.DirPath.DCIM_SD_CARD_PATH; //照片目录路径。

        File photoDirecotry= new File(photoDirectoryPath); //照片目录。

        IOFileFilter fileFilter= TrueFileFilter.INSTANCE; //文件过滤器。

        IOFileFilter dirFilter= TrueFileFilter.INSTANCE; //文件过滤器。

        try //尝试列出文件，并且捕获可能的异常。
        {

            Collection<File> photoFiles= FileUtils.listFiles(photoDirecotry, fileFilter, dirFilter); //列出全部文件。

            Random random=new Random(); //随机数生成器。

            if (photoFiles.size()>0) //有照片文件。
            {
                int randomIndex=random.nextInt(photoFiles.size()); //随机选择一个文件。

                result=(File)((photoFiles.toArray())[randomIndex]); //选择指定的文件。

            } //if (photoFiles.size()>0) //有照片文件。


        } //try //尝试列出文件，并且捕获可能的异常。
        catch (IllegalArgumentException illegalArgumentException) //参数不符合要求。
        {
            illegalArgumentException.printStackTrace(); //输出调用栈。
        } //catch (IllegalArgumentException illegalArgumentException) //参数不符合要求。


        return result;
    } //private  File findRandomPhotoFile()

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Log.d(TAG, "onKeyDown, keyCode: " + keyCode + ", keyEvent: " + event); //Debug.

        boolean result=false; //结果。
        switch (keyCode) //根据键盘码来动作。
        {
            case KeyEvent.KEYCODE_MEDIA_PLAY: //播放键。
                commandRecognizebutton2(); //开始识别。

                result=true;
                break;

                default: //其它按钮。
                    result=super.onKeyDown(keyCode, event); //由亲代处理。

                    break; //跳出。
        } //switch (keyCode) //根据键盘码来动作。
        return result;
    } //public boolean onKeyDown(int keyCode, KeyEvent event)

    /**
     * 寻找语音识别与软件包映射文件。
     * @return 语音识别与软件包映射文件。
     */
    private  File findVoicePackageMapFile()
    {
      File result=null;

      File filesDir=getFilesDir();

      if (filesDir==null) //该目录不存在。
      {
      } //if (filesDir==null) //该目录不存在。
      else //该目录存在。
      {
        result=new File(filesDir.getAbsolutePath()+"/voicePackageNameMap.proto"); //指定文件名。

        if (result.exists()) //文件存在。
        {
        } //if (result.exists()) //文件存在。
        else //文件不存在。
        {
          try
          {
            boolean createResult=result.createNewFile(); //创建文件。
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        } //else //文件不存在。
      } //else //该目录存在。

      return result;
    } //private  File findRandomPhotoFile()

    /**
     * 考虑要不要初始化讯飞语音识别。
     */
    private void assessInitializeMsc()
    {
      long startTimestamp=System.currentTimeMillis(); // 记录开始时间戳。

      if (mscIsInitialized) //已经初始化。
      {
      } //if (mscIsInitialized) //已经初始化。
      else  //尚未初始化。
      {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) //网络已经连接。
        {
          initializeMsc(); //初始化讯飞语音识别。

          mscIsInitialized=true; //已经初始化。
        } //if (isConnected) //网络已经连接。
      } //else  //尚未初始化。
      long endTimestamp=System.currentTimeMillis(); // 记录开始时间戳。
      Log.w(TAG, "assessInitializeMsc, 1649, leave assessInitializeMsc, timestamp: " + System.currentTimeMillis()); //Debug.
    } //private void assessInitializeMsc()

    /**
     * 初始化 MSC。
     */
    private void initializeMsc()
    {
      ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

      Log.w(TAG, "initializeMsc, 729, timestamp: " + System.currentTimeMillis() + ", active network: " + activeNetwork + ", is connected: " + isConnected); //Debug.

      Log.w(TAG, "initializeMsc, 720, timestamp: " + System.currentTimeMillis()); //Debug.

      SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56e142d3"); //创建工具。
      Log.w(TAG, "initializeMsc, 723, timestamp: " + System.currentTimeMillis()); //Debug.

      mIat= SpeechRecognizer.createRecognizer(this, null);
      Log.w(TAG, "initializeMsc, 725, timestamp: " + System.currentTimeMillis()); //Debug.
    } //private void initializeMsc()

    /**
     * 初始化数据
     */
    private void reqGameData()
    {
      ReqGameDataTask translateRequestSendTask =new ReqGameDataTask(); //创建异步任务。

        translateRequestSendTask.execute(this, mAdapter); //执行任务。
    } //private void reqGameData()

    @Override
    /**
     * 按了后退键。
     */
    public void onBackPressed()
    {
        cancelRecognize(); //取消识别。
    } //public void onBackPressed()

    /**
     * 显示图标列表
     */
    private void showIconList()
    {
      Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
      mRecyclerView.setVisibility(View.VISIBLE);

      mRecyclerView.setRotationY(-30);
      mRecyclerView.setRotationX(-30);

      mRecyclerView.animate().rotationY(0).rotationX(0).start();
      
      gridIcon.setVisibility(View.GONE);
    } //private void showIconList()

    /**
     * 取消识别。
     */
    @OnClick({R.id.gridIcon})
    public void cancelRecognize()
    {
      Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
      showIconList(); //显示图标列表

      if (mIat!=null) //存在识别对象
      {
        mIat.cancel(); //取消。
      } //if (mIat!=null) //存在识别对象

      animateRecognizeButtonDark(); //动画变暗
    } //private void cancelRecognize()

    /**
     * 动画让按照变亮
     */
    private void animateRecognizeButtonLight()
    {
      microphoneIcon.setBackgroundResource(R.drawable.record_button_go_light); //设置背景

      rocketAnimation=(AnimationDrawable)(microphoneIcon.getBackground());

      rocketAnimation.start(); //开始变亮
    } //private void animateRecognizeButtonLight()

    /**
     * 动画变暗
     */
    private void animateRecognizeButtonDark()
    {
      microphoneIcon.setBackgroundResource(R.drawable.record_button_go_dark); //设置背景

      rocketAnimation=(AnimationDrawable)(microphoneIcon.getBackground());

      rocketAnimation.start(); //开始变暗
    } //private void         animateRecognizeButtonDark()

    /**
     * 绑定适配器。
     */
    private void bindAdapter()
    {
        BindAdapterTask translateRequestSendTask =new BindAdapterTask(); //创建异步任务。

        translateRequestSendTask.execute(this, mRecyclerView); //执行任务。
    } //private void bindAdapter()

    private void showStaggeredGridLayoutManager() 
    {
        int columnsPerRow= getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(columnsPerRow,StaggeredGridLayoutManager.VERTICAL); //布局管理器。
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * 初始化本地日志工具。
     */
    private void initLocalLogUtil()
    {
      Thread.setDefaultUncaughtExceptionHandler(new LanImeUncaughtExceptionHandler()); // 设置未捕获的异常处理器。
    } //private void initLocalLogUtil()

    /**
     * 解除注册广播接收器。
     */
    private void unregisterBroadcastReceiver()
    {
      LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(this); //Get the broadcast manager instance.
      lclBrdcstMngr.unregisterReceiver(mBroadcastReceiver); //注册接收器。

      unregisterReceiver(mBroadcastReceiver);
    } //private void unregisterBroadcastReceiver()

    /**
     * 注册启动器应用的回调对象。
     */
    private void registerLauncherAppsCallback()
    {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) //21 之后才有启动器应用对象。
      {
        LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。

        LauncherApps.Callback launcherAppsCallback=new LauncherAppsCallback(this); // 创建回调对象。

        launcherApps.registerCallback(launcherAppsCallback); //注册回调对象。
      }
    } //private void registerLauncherAppsCallback()

    @Override
    protected void onNewIntent(Intent intent)
    {
      FileLogger.d(TAG, "onNewIntent, intent: " + intent);
      super.onNewIntent(intent);

      String action=intent.getAction();

      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
      {
        if (LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT.equals(action))
        {
          LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE));

          LauncherApps.PinItemRequest pinItemRequest=launcherApps.getPinItemRequest(intent);

          pinShortcut(pinItemRequest);
        }
      }
      
      if (PACKAGE_INSTALLED_ACTION.equals(intent.getAction()))
      {
        Bundle extras = intent.getExtras();
        FileLogger.d(TAG, CodePosition.newInstance().toString() + ", onNewIntent: PACKAGE_INSTALLED_ACTION received");
        FileLogger.d(TAG, "onNewIntent: extras=" + extras);
        
        if (extras != null)
        {
          int status = extras.getInt(PackageInstaller.EXTRA_STATUS);
          String message = extras.getString(PackageInstaller.EXTRA_STATUS_MESSAGE);
          String packageName=extras.getString(PackageInstaller.EXTRA_PACKAGE_NAME);

          FileLogger.d(TAG, "onNewIntent: status=" + status + ", message=" + message + ", packageName=" + packageName);

          switch (status) 
          {
            case PackageInstaller.STATUS_PENDING_USER_ACTION:
              FileLogger.d(TAG, "onNewIntent: STATUS_PENDING_USER_ACTION - attempting to start confirm dialog");
              Intent confirmIntent = (Intent) extras.get(Intent.EXTRA_INTENT);
              FileLogger.d(TAG, "onNewIntent: confirmIntent=" + confirmIntent);
              if (confirmIntent != null)
              {
                // 为 MIUI 添加额外的 Flags
                confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                confirmIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                
                FileLogger.d(TAG, "onNewIntent: starting confirmIntent with action=" + confirmIntent.getAction() + ", flags=" + confirmIntent.getFlags());
                try
                {
                  startActivity(confirmIntent);
                  FileLogger.d(TAG, "onNewIntent: confirmIntent started successfully");
                }
                catch (Exception e)
                {
                  FileLogger.e(TAG, "onNewIntent: failed to start confirmIntent: " + e.getMessage(), e);
                }
              }
              else
              {
                FileLogger.e(TAG, "onNewIntent: confirmIntent is null!");
              }
              break;

            case PackageInstaller.STATUS_SUCCESS:
              FileLogger.d(TAG, "onNewIntent: STATUS_SUCCESS - " + packageName);
              Toast.makeText(this, "Install succeeded!", Toast.LENGTH_SHORT).show();
              launchApplicationByPackageName(packageName);

              break;

            case PackageInstaller.STATUS_FAILURE:
            case PackageInstaller.STATUS_FAILURE_ABORTED:
            case PackageInstaller.STATUS_FAILURE_BLOCKED:
            case PackageInstaller.STATUS_FAILURE_CONFLICT:
            case PackageInstaller.STATUS_FAILURE_INCOMPATIBLE:
            case PackageInstaller.STATUS_FAILURE_INVALID:
            case PackageInstaller.STATUS_FAILURE_STORAGE:
              FileLogger.e(TAG, "onNewIntent: STATUS_FAILURE_* - " + status + ", " + message);
              Toast.makeText(this, "Install failed! " + status + ", " + message, Toast.LENGTH_SHORT).show();
              break;
            default:
              FileLogger.w(TAG, "onNewIntent: Unrecognized status - " + status);
              Toast.makeText(this, "Unrecognized status received from installer: " + status, Toast.LENGTH_SHORT).show();
          }
        }

      }
      else
      {
      }
    }

    /**
     * 注册广播事件接收器。
     */
    private void registerBroadcastReceiver()
    {
      long startTimestamp=System.currentTimeMillis(); // 记录开始时间戳。
      Log.w(TAG, "registerBroadcastReceiver, 1876, enter registerBroadcastReceiver, timestamp: " + System.currentTimeMillis()); //Debug.
      Log.d(TAG, "registerBroadcastReceiver."); //Debug.

      IntentFilter intentFilter = new IntentFilter(); //创建意图过滤器。

      intentFilter.addAction(Constants.NativeMessage.APPLICATION_LAUNCHED); //应用程序被启动。
      intentFilter.addAction(Constants.NativeMessage.NotificationBadgeChanged); // Notification badge changed.
      intentFilter.addAction(Constants.Operation.PinShortcut); //钉住快捷方式。
      intentFilter.addAction(UnlinkVoiceCommand); //断开语音指令关联。
      intentFilter.addAction(ToggleBuiltinShortcuts); //切换是否要显示内置快捷方式。
      intentFilter.addAction(ToggleHiveLayout); //切换是否要使用蜂窝布局。

      LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(this); //Get the broadcast manager instance.
      lclBrdcstMngr.registerReceiver(mBroadcastReceiver, intentFilter); //注册接收器。

      //注册全局的广播接收器：
      //软件包增加：
      IntentFilter packageChangeIntentFilter=new IntentFilter(); //创建意图过滤器。
      packageChangeIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); //应用被添加。
      packageChangeIntentFilter.addDataScheme("package"); //加入数据模式。

      registerReceiver(mBroadcastReceiver, packageChangeIntentFilter); //注册广播事件接收器。

      // package removed
      IntentFilter packageRemovedChangeIntentFilter=new IntentFilter(); //创建意图过滤器。
      packageRemovedChangeIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); // package removed.
      packageRemovedChangeIntentFilter.addDataScheme("package"); //加入数据模式。

      registerReceiver(mBroadcastReceiver, packageRemovedChangeIntentFilter); //注册广播事件接收器。

      //软件包变化：
      IntentFilter packageChangeIntentFilter1=new IntentFilter(); //创建意图过滤器。
      packageChangeIntentFilter1.addAction(ACTION_PACKAGE_CHANGED); //应用被改变
      packageChangeIntentFilter1.addDataScheme("package"); //加入数据模式。

      registerReceiver(mBroadcastReceiver, packageChangeIntentFilter1); //注册广播事件接收器。

      //软件包变化：
      IntentFilter packageChangeIntentFilter2=new IntentFilter(); //创建意图过滤器。
      packageChangeIntentFilter2.addAction(ACTION_PACKAGE_REPLACED); //应用被改变
      packageChangeIntentFilter2.addDataScheme("package"); //加入数据模式。

      registerReceiver(mBroadcastReceiver, packageChangeIntentFilter2); //注册广播事件接收器。

      //壁纸变化：
      IntentFilter wallpaperChangeIntentFilter=new IntentFilter(); //创建意图过滤器。
      wallpaperChangeIntentFilter.addAction(Intent.ACTION_WALLPAPER_CHANGED); //壁纸发生变化。

      registerReceiver(mBroadcastReceiver, wallpaperChangeIntentFilter); //注册广播事件接收器。

      //兰心输入法正在为某个软件包输入：
      IntentFilter lanimeInputtingIntentFilter=new IntentFilter(); //创建意图过滤器。
      lanimeInputtingIntentFilter.addAction(InputtingForPackage); //壁纸发生变化。

      registerReceiver(mBroadcastReceiver, lanimeInputtingIntentFilter); //注册广播事件接收器。

      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后才有钉住快捷方式的广播。
      {
        //添加钉住的快捷方式：
        IntentFilter pinShortcutsIntentFilter=new IntentFilter(); //创建意图过滤器。
        pinShortcutsIntentFilter.addAction(LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT); //钉住快捷方式。

        registerReceiver(mBroadcastReceiver, pinShortcutsIntentFilter); //注册广播事件接收器。
      } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后才有钉住快捷方式的广播。

      //传统的安装快捷方式：
      IntentFilter legacyShortcutIntentFilter=new IntentFilter(); //创建意图过滤器。
      legacyShortcutIntentFilter.addAction(LegacyInstallShortcut); //安装快捷方式。

      registerReceiver(mBroadcastReceiver, legacyShortcutIntentFilter); //注册广播事件接收器。
    } //private void registerBroadcastReceiver()

    /**
     * 广播接收器。
     */
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
      private final String TAG="BroadcastReceiver"; //!< The tag used for debug code.

      @Override
      /**
      * 接收到广播。
      */
      public void onReceive(Context context, Intent intent)
      {
        String action = intent.getAction(); //获取广播中带的动作字符串。

        Log.d(TAG,"1587, onReceive,got broadcast:"+action + ", equals package_added?: " + (Intent.ACTION_PACKAGE_ADDED.equals(action))); //Debug.

        if (Constants.NativeMessage.APPLICATION_LAUNCHED.equals(action)) // Application being launched.
        {
          Bundle extras=intent.getExtras(); //获取参数包。
          String packageName=extras.getString(Constants.NativeMessage.APPLICATION_LAUNCHED_PACKAGE_KEY); //get the progress value.

          countApplicationLaunch(packageName); //检查是否要显示错误码。
        } //if (Constants.NativeMessage.APPLICATION_LAUNCHED.equals(action)) //虚拟卡启动结果。
        else if ((Intent.ACTION_PACKAGE_ADDED.equals(action)) || (Intent.ACTION_PACKAGE_CHANGED.equals(action))  || (ACTION_PACKAGE_REPLACED.equals(action))   || (Intent.ACTION_PACKAGE_REMOVED.equals(action)) ) // Package related change.
        {
          Bundle extras=intent.getExtras(); //获取参数包。
          int uid=extras.getInt(Intent.EXTRA_UID); //获取该软件包对应的用户编号。

          Log.d(TAG,"1604 ,onReceive,got broadcast:"+action + ", uid: " + uid); //Debug.

          showNewlyAddedPackage(uid); //显示新安装的软件包。
        } //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
        else if ((Constants.NativeMessage.NotificationBadgeChanged.equals(action))  ) // Notification badge changed.
        {
          // showNewlyAddedPackage(uid); // refresh packges.
          solveLauncherIntents(); // Refersh application list.
        } //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
        else if (InputtingForPackage.equals(action)) //兰心输入法输入。
        {
          Bundle extras=intent.getExtras(); //获取参数包。
          String packageName=extras.getString(PackageNameOfInputting); // Get the package name.

          assesReleaseWakeLock(packageName); // Assess to release wake lock.
          assesAquireWakeLock(packageName); // Assess to aquire wake lock
        } //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
        else if (ToggleBuiltinShortcuts.equals(action)) //切换内置的快捷方式是否显示。
        {
          Bundle extras=intent.getExtras(); //获取参数包。
          boolean uid=extras.getBoolean("builtinShortcutsVisible"); //获取状态符，是否要可见。

          toggleBuiltinShortcutsVisible(uid); //切换，内置快捷方式是否可见。
        } //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
        else if (ToggleHiveLayout.equals(action)) //切换是否使用蜂窝布局。
        {
          Bundle extras=intent.getExtras(); //获取参数包。
          boolean uid=extras.getBoolean("useHiveLayout"); //获取状态符，是否要可见。

          toggleHIveLayout(uid); //切换，是否使用蜂窝布局。
        } //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
        else if (UnlinkVoiceCommand.equals(action)) //断开语音指令关联。
        {
          Bundle extras=intent.getExtras(); //获取参数包。
          boolean uid=extras.getBoolean("useHiveLayout"); //获取状态符，是否要可见。

          unlinkVoiceCommand(); //断开语音指令关联。
        } //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
        else if (LegacyInstallShortcut.equals(action)) //传统的安装快捷方式。
        {
        } //else if (LegacyInstallShortcut.equals(action)) //传统的安装快捷方式。

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后都有钉住快捷方式的广播。
        {
          if (LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT.equals(action)) //钉住快捷方式。
          {
            LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。

            LauncherApps.PinItemRequest pinItemRequest=launcherApps.getPinItemRequest(intent); //获取钉住请求对象。

            pinShortcut(pinItemRequest); //钉住快捷方式。
          } //if (LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT.equals(action)) //钉住快捷方式。
          else if (Constants.Operation.PinShortcut.equals(action)) //钉住快捷方式。
          {
//                    LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。
//
//                    LauncherApps.PinItemRequest pinItemRequest=launcherApps.getPinItemRequest(intent); //获取钉住请求对象。

            HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象．

            RuntimeInformationStore runtimeInformationStore=hxLauncherApplication.getRuntimeInformationStore(); //获取运行时信息存储对象．

//                    runtimeInformationStore.setShortcut(shortcutInfo); //记录快捷方式．

            ShortcutInfo shortcutInfo=runtimeInformationStore.getShortcut(); //获取快捷方式。

            pinShortcut(shortcutInfo); //钉住快捷方式。
          } //if (LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT.equals(action)) //钉住快捷方式。
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后都有钉住快捷方式的广播。
      } //public void onReceive(Context context, Intent intent)
    }; //private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()

    /**
     * 断开语音指令关联。
     */
    private void unlinkVoiceCommand()
    {
        Log.d(TAG, ",unlinkVoiceCommand, stack length: " + voiceCommandHitDataStack.size()); //Debug.
        VoiceCommandHitDataObject commandToUnlink=null; //最终要断开关联的对象

        //寻找要断开关联的命令对象：
        if (voiceCommandHitDataStack.empty()) //栈为空，不行动
        {

        } //if (voiceCommandHitDataStack.empty()) //栈为空，不行动
        else  //栈不为空，做事
        {
            VoiceCommandHitDataObject voiceCommandHitDataObject=voiceCommandHitDataStack.pop(); //获取最后一个

            String packageName=voiceCommandHitDataObject.getPackageName(); //获取包名。

            Log.d(TAG, ",unlinkVoiceCommand, stack length: " + voiceCommandHitDataStack.size()+ ", packange name: " + packageName); //Debug.


            if (packageName.equals(BuildConfig.APPLICATION_ID)) //包名就是桌面。再看后面一个
            {

                if (voiceCommandHitDataStack.empty()) //栈为空，不动作
                {

                } //if (voiceCommandHitDataStack.empty()) //栈为空，不动作
                else //栈不为空，动作
                {
                    VoiceCommandHitDataObject voiceCommandHitDataObject1=voiceCommandHitDataStack.pop(); //取出一个

                    commandToUnlink=voiceCommandHitDataObject1; //记住
                } //else //栈不为空，动作

            } //if (packageName.equals(BuildConfig.APPLICATION_ID)) //包名就是桌面
            else  //包名不是桌面
            {
                commandToUnlink=voiceCommandHitDataObject; //记住
            } //else  //包名不是桌面
        } //else  //栈不为空，做事

        Log.d(TAG, ",unlinkVoiceCommand, stack length: " + voiceCommandHitDataStack.size()+ ", command unlink: " + commandToUnlink); //Debug.
        Log.d(TAG, ",unlinkVoiceCommand, stack length: " + voiceCommandHitDataStack.size()+ ", command unlink: " + commandToUnlink.getPackageName()); //Debug.
        Log.d(TAG, ",unlinkVoiceCommand, stack length: " + voiceCommandHitDataStack.size()+ ", command unlink: " + commandToUnlink.getActivityName()); //Debug.
        Log.d(TAG, ",unlinkVoiceCommand, stack length: " + voiceCommandHitDataStack.size()+ ", command unlink: " + commandToUnlink.getIconType()); //Debug.


        //断开关联：
        if (commandToUnlink!=null) //有内容
        {
            VoiceCommandSourceType voiceCommandSourceType=commandToUnlink.getVoiceCommandSourceType(); //获取语音命中方式。
            LauncherIconType launcherIconType=commandToUnlink.getIconType(); //获取图标类型
            String voiceCommandString=commandToUnlink.getVoiceRecognizeResult(); //获取语音识别结果
            String voiceCommandPackage=commandToUnlink.getPackageName(); //获取包名
            String voiceCommandActivityName=commandToUnlink.getActivityName(); //获取活动名

            if (voiceCommandSourceType==LocalVoiceCommandMap) //本地映射
            {
                if (launcherIconType==ActivityIconType) //是应用图标
                {
                    Collection<PackageItemInfo> packageItemInfos=(Collection<PackageItemInfo>) voicePackageNameMap.get(voiceCommandString); //获取集合信息

                    for(PackageItemInfo packageItemInfo: packageItemInfos) //一个个地输出
                    {
                        String packageName=packageItemInfo.packageName; //获取包名。
                        String activityName=packageItemInfo.name; //获取活动名

                        if ((packageName.equals(voiceCommandPackage)) && (activityName.equals(voiceCommandActivityName))) //包名和类名都一致
                        {

                            voicePackageNameMap.removeMapping(voiceCommandString, packageItemInfo); //删除关联
                            saveVoicePackageNameMap(); //保存映射。


                            break; //跳出，不再找了
                        } //if ((packageName.equals(voiceCommandPackage)) && (activityName.equals(voiceCommandActivityName))) //包名和类名都一致

                    } //for(PackageItemInfo packageItemInfo: packageItemInfos) //一个个地输出

                } //if (launcherIconType==ActivityIconType) //是应用图标
                else  //是快捷方式图标
                {
                    HxShortcutInfo hxShortcutInfo=voiceShortcutIdMap.get(voiceCommandString); //获取快捷方式对象
                    String packageName=hxShortcutInfo.packageName; //获取包名。
                    String activityName=hxShortcutInfo.shortcutId; //获取快捷方式编号。

                    if ((packageName.equals(voiceCommandPackage)) && (activityName.equals(voiceCommandActivityName))) //一致
                    {
                        voiceShortcutIdMap.remove(voiceCommandString, hxShortcutInfo); //删除映射

                        saveVoiceShortcutIdMap();
                    } //if ((packageName.equals(voiceCommandPackage)) && (activityName.equals(voiceCommandActivityName))) //一致

                } //else  //是快捷方式图标
            } //if (voiceCommandSourceType==LocalVoiceCommandMap) //本地映射
            else //服务器处理结果
            {
                rememberServerVocieCommandResponseInginore(voiceCommandString, voiceCommandPackage); //记录，要忽略服务器对于这个语音识别结果和这个包的回复。
            } //else //服务器处理结果
        } //if (commandToUnlink!=null) //有内容
    } //private void unlinkVoiceCommand()

    /**
     * 记录，要忽略服务器对于这个语音识别结果和这个包的回复。
     * @param voiceCommandString 语音识别指令
     * @param voiceCommandPackage 服务器之前返回的包名
     */
    private void rememberServerVocieCommandResponseInginore(String voiceCommandString, String voiceCommandPackage)
    {
        serverVoiceCommandResponseIgnoreMap.put(voiceCommandString, voiceCommandPackage); //加入映射中

        saveServerVoiceCommandResponseIgnoreMap(); //保存映射
    } //private void rememberServerVocieCommandResponseInginore(String voiceCommandString, String voiceCommandPackage)

    /**
     * 切换，是否使用蜂窝布局。
     * @param uid 是否使用蜂窝布局。
     */
    public void toggleHIveLayout(Boolean uid)
    {
        if (uid) //要使用蜂窝布局
        {
//            showHiveLayout(); //显示蜂窝布局
            showStaggeredGridLayoutManager(); //显示瀑布布局
        } //if (uid) //要使用蜂窝布局
        else //不使用蜂窝布局
        {
            showStaggeredGridLayoutManager(); //显示瀑布布局
        } //else //不使用蜂窝布局
    } //private void toggleHIveLayout(Boolean uid)

    /**
     * 切换，内置快捷方式是否可见。
     * @param uid 是否可见。
     */
    private void toggleBuiltinShortcutsVisible(boolean uid)
    {
        mAdapter.toggleBuiltinShortcutsVisible(uid); //切换可见性。

//        updateItemPositionMap(); //整体更新条目的位置映射

        mAdapter.notifyDataSetChanged(); //通知数据变化。
    } //private void toggleBuiltinShortcutsVisible(boolean uid)

    /**
     * 钉住快捷方式。
     * @param shortcutInfo 要钉住的快捷方式。
     */
    private void pinShortcut(ShortcutInfo shortcutInfo)
    {
        mAdapter.addShortcut(shortcutInfo); //向适配器添加快捷方式。

        mAdapter.updateItemPositionMap(); //整体更新条目的位置映射

        mAdapter.notifyDataSetChanged(); //通知数据变更。

    } //private void pinShortcut(ShortcutInfo shortcutInfo)

    /**
     * 钉住快捷方式。
     * @param pinItemRequest 钉住请求对象。
     */
    private void pinShortcut(LauncherApps.PinItemRequest pinItemRequest)
    {
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后才有钉住快捷方式的请求对象。
      {
        ShortcutInfo shortcutInfo=pinItemRequest.getShortcutInfo(); //获取快捷方式对象。

        mAdapter.addShortcut(shortcutInfo); //向适配器添加快捷方式。

        mAdapter.updateItemPositionMap(); //整体更新条目的位置映射

        mAdapter.notifyDataSetChanged(); //通知数据变更。

        pinItemRequest.accept(); //接受。
      } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26 之后才有钉住快捷方式的请求对象。
    } //private void pinShortcut(LauncherApps.PinItemRequest pinItemRequest)
    
    /**
     * 显示新安装的软件包。
     * @param uid 软件包的用户编号。
     */
    public void showNewlyAddedPackage(int uid)
    {
      PackageManager packageManager=getPackageManager(); //获取软件包管理器。

      String[] packageNames=packageManager.getPackagesForUid(uid); //获取对应的软件包列表。

      if (packageNames!=null) //软件包列表存在。
      {
        for (String packageName: packageNames) //一个个地遍历。
        {
          try
          {
            PackageInfo packageInfo=packageManager.getPackageInfo(packageName,0); //获取对应的软件包信息。

            Intent launchIntent= packageManager.getLaunchIntentForPackage(packageName); //获取当前软件包的启动意图。

            if (launchIntent!=null) //有启动意图。
            {
              ApplicationInfo applicationInfo=packageInfo.applicationInfo; //获取应用程序信息。

              Drawable applicationIcon=packageManager.getApplicationIcon(applicationInfo); //获取应用程序的图标。

              if (packageInfo.activities != null) // 列表存在
              {
                for(ActivityInfo currentActivity: packageInfo.activities) //一个个活动地处理
                {
                  String activityName=currentActivity.name; //获取活动名字

                  HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象

                  HashMap<String, Drawable> activityIconMap=hxLauncherApplication.getLaunchIconMap(); //获取启动图标缓存

                  activityIconMap.remove(packageName + "/" + activityName); //删除对应的映射
                } //for(AcitivityInfo currentActivity: packageInfo.activities) //一个个活动地处理
              } // if (packageInfo.activities) // 列表存在
                
              CharSequence applicationLabel=packageManager.getApplicationLabel(applicationInfo); //获取应用程序的文字。
            } //else //有启动意图。
          }
          catch (PackageManager.NameNotFoundException e) //未找到该软件包。
          {
            e.printStackTrace(); //报告错误。
          } //catch (PackageManager.NameNotFoundException e) //未找到该软件包。
        } //for (String packageInfo:packageNames) //一个个地遍历。
      } //if (packageNames!=null) //软件包列表存在。

      solveLauncherIntents(); //重新解析启动器意图列表

      buildActivityLabelPackageItemInfoMap(); //构造映射，活动文本标签与包信息对象之间的映射
    } //public void showNewlyAddedPackage(int uid)

    /**
     * 检查是否要显示错误信息。
     * @param packageName 错误码。
     */
    private void countApplicationLaunch(String packageName)
    {
        PackageCountObject packageCountObject=packageNameCountObjectMap.get(packageName); //获取对应的计数对象。

        if (packageCountObject!=null) //计数对象存在。
        {
            packageCountObject.incrementLikelyCount(); //受欢迎度计数。

            PackageCountObject firstObjectInList=packageCountObjectList.get(0);  //获取第一个计数对象。

            if (!(firstObjectInList.getPackageName().equals(packageName))) //点击的不是第一个软件包。
            {
                firstObjectInList.decrementLikelyCount(); //受欢迎度降低。
            } //else //点击的不是第一个软件包。
        } //if (packageCountObject!=null) //计数对象存在。
    } //private void checkErrorCode(int errorCode)

    /**
     * 启动友军"21 点关机"的服务。
     */
    protected void startFriendShutDownAt2100Service()
    {
      Intent intent = new Intent();
      intent.setComponent(new ComponentName("com.stupidbeauty.shutdownat2100androidnative", "com.stupidbeauty.shutdownat2100androidnative.TimeCheckService")); //设置组件。

      try //启动服务
      {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //前台服务
        {
          startForegroundService(intent); //启动前台服务
        } //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //前台服务
        else //没有前台服务
        {
          startService(intent); //启动服务。
        } //else //没有前台服务
      } //try //启动服务
      catch (IllegalStateException e) //不允许启动服务。
      {
        e.printStackTrace(); //报告错误。
      } //catch (IllegalStateException e) //不允许启动服务。
    } //protected void startFriendShutDownAt2100Service()

    @Override
    /**
     * 活动重新处于活跃状态。
     */
    protected void onResume()
    {
      long startTimestamp=System.currentTimeMillis(); // 记录开始时间戳。
      Log.w(TAG, "onResume, 2381, enter onResume, timestamp: " + System.currentTimeMillis()); //Debug.
      super.onResume(); //超类继续工作。

      activityHasBeenResumed=true; //处于正常运行状态。

      HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。
      hxLauncherApplication.releaseWakeLock(); //释放唤醒锁。
      hxLauncherApplication.killBackgroundProcesses(); // Kill background processes.

      startFriendShutDownAt2100Service();

      assessInitializeMsc(); //考虑要不要初始化讯飞语音识别。

      commandRecognizebutton2(); //开始识别。

      createActiveUserReportManager(); // 创建管理器，活跃用户统计。陈欣

      // shutDownAt2100Manager.checkShutDownTime(); // Check shut down time.
      shutDownAt2100Logic.checkShutDownTime(); // Check shut down time.
      
      scrollToLastApplication(); // Scroll to last  active application.
    } //protected void onResume()

    /**
    * 创建管理器，活跃用户统计。陈欣
    */
    private void createActiveUserReportManager()
    {
      if (activeUserReportManager==null) // 还不存在管理器。
      {
        activeUserReportManager=new ActiveUserReportManager(); // 创建管理器。
            
        activeUserReportManager.startReportActiveUser(); // 开始报告活跃用户。
      } //if (activeUserReportManager==null)
    } //private void createActiveUserReportManager()

    /**
     * 手动开始语音识别。
     */
    @OnClick({R.id.voiceAssistantLayout, R.id.microphoneIcon, R.id.hitApplicationIcon})
    public void manualStartVoiceRecognize()
    {
      commandRecognizebutton2(); //启动语音识别。

      shutDownAt2100Logic.checkShutDownTime(); // Check shut down time.
    } //public void manualStartVoiceRecognize()

    /**
     * 活动被暂停。
     */
    @Override
    protected void onPause()
    {
      super.onPause();

      activityHasBeenResumed=false; //不是处于正常运行状态。
    } //protected void onPause()

    /**
     * 考虑，是否要发送语音指令关联应用数据。
     */
    private void assesSendVoiceAssociationData()
    {
      if (sentVoiceAssociationData) //已经成功发送。
      {
      } //if (sentVoiceAssociationData) //已经成功发送。
      else //尚未成功发送。
      {
        sendVoiceAssociationData(); //发送语音指令关联数据。
      } //else //尚未成功发送。
    } //private void assesSendVoiceAssociationData()

    /**
     * 考虑，是否要发送语音指令关联快捷方式数据。
     */
    private void assesSendVoiceShortcutAssociationData()
    {
      if (sentVoiceShortcutAssociationData) //已经成功发送。
      {
      } //if (sentVoiceAssociationData) //已经成功发送。
      else //尚未成功发送。
      {
        sendVoiceShortcutAssociationData(); //发送语音指令关联数据。
      } //else //尚未成功发送。
    } //private void assesSendVoiceShortcutAssociationData()

    /**
     * 考虑是否要重新启动语音识别过程。
     */
    private void assesRestartSpeechRecognize()
    {
    } //private void assesRestartSpeechRecognize()

    /**
     * 在线命令词识别。
     */
    public void commandRecognizebutton2()
    {
      Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
      hideIconList(); //隐藏图标列表

      recognizeCounter=recognizeCounter+1; //计数．

      voiceEndDetected=false; //重置状态，未探测到用户的声音结束。

      foundActivity=false; //重置，未找到对应的活动。

      if (mIat==null) //识别器未创建。
      {
        mIat= SpeechRecognizer.createRecognizer(this,null); //创建识别器。
      } //if (mIat==null) //识别器未创建。

      if (mIat==null) //仍然创建失败。
      {
        Log.i(TAG, "commandRecognizebutton2, failed to start voice recognize."); //Debug.
        Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
        showIconList(); //显示图标列表

        animateRecognizeButtonDark(); //动画变暗
      } //if (mIat==null) //仍然创建失败。
      else //创建成功。
      {
        Log.i(TAG, "commandRecognizebutton2, starting voice recognize. Is listening: "+ mIat.isListening()); //Debug.

        if (!setParam()) //参数设置失败。
        {
          statustextView.setText("请先构建语法。");

          return;
        } //if (!setParam()) //参数设置失败。

        ret = mIat.startListening(mRecognizerListener); //开始识别。
        if (ret != ErrorCode.SUCCESS) //未能启动识别
        {
          if (ret == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED)
          {
            // 未安装则跳转到提示安装页面
            // mInstaller.install();
          } else
          {
            statustextView.setText("识别失败，错误码：" + ret);
          }

          Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
          showIconList(); //显示图标列表

          animateRecognizeButtonDark(); //动画变暗
        } //if (ret != ErrorCode.SUCCESS) //未能启动识别
        else  //成功启动识别
        {
          progressBar.setVisibility(View.INVISIBLE); //隐藏显示进度条。

          animateRecognizeButtonLight(); //动画让按照变亮
        } //else  //成功启动识别

        hitApplicationIcon.setVisibility(View.GONE); //隐藏命中的应用图标。

        String appNamePrompt=getString(R.string.appNamePrompt); // 获取提示的应用名字。

        String myString = "Messages";

        String formatted = getString(R.string.sayToMeAppName, appNamePrompt);

        applicationNameTextView2.setText(formatted); // 显示提示。
        applicationNameTextView2.setVisibility(View.VISIBLE); // 显示提示文字标签。
      } //else //创建成功。
    } //public void commandRecognizebutton2()

    /**
     * 隐藏图标列表
     */
    private void hideIconList()
    {
      Log.d(TAG, CodePosition.newInstance().toString()); // Debug.
      if (mRecyclerView!=null)
      {
        mRecyclerView.animate().rotationX(-30).rotationY(-30).withEndAction(hideIconLitRunnalbe).start();
      }  //if (mRecyclerView!=null)
      
      gridIcon.setVisibility(View.VISIBLE); // Show the grid icon.
    } //private void hideIconList()

    Runnable hideIconLitRunnalbe=new Runnable() 
    {
      @Override
      public void run() 
      {
        mRecyclerView.setRotationX(-30);
        mRecyclerView.setRotationY(-30);

        mRecyclerView.setVisibility(View.INVISIBLE); //隐藏
      }
    };

    /**
     * 参数设置
     *
     * @return 是否设置成功。
     */
    public boolean setParam()
    {
      boolean result = false;

      if (mIat!=null) //识别器存在。
      {
        // 设置识别引擎
        String mEngineType = SpeechConstant.TYPE_CLOUD;

        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);

        // 设置返回结果为 json 格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置云端识别使用的语法 id
        mIat.setParameter(SpeechConstant.DOMAIN,"iat");

        setLanguageAndAccentParameters(); //设置语言及区域参数字符串。

        String vadEos=mIat.getParameter(SpeechConstant.VAD_EOS); //获取默认的后端点时间。

        mIat.setParameter(SpeechConstant.VAD_EOS, "100"); //后端点时间长度。

        mIat.setParameter(SpeechConstant.ASR_PTT, "0"); //不要标点符号。https://www.xfyun.cn/doc/asr/voicedictation/Android-SDK.html#_2%E3%80%81sdk%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97

        result = true;

        // 设置音频保存路径，保存音频格式支持 pcm、wav，设置路径为 sd 卡请注意 WRITE_EXTERNAL_STORAGE 权限
        // 注：AUDIO_FORMAT 参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        recordSoundFilePath=Environment.getExternalStorageDirectory() + "/msc/asr."+ recognizeCounter +".wav"; //构造录音文件路径．

        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, recordSoundFilePath); //设置录音存储路径。

        // mIat.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false"); //不获取焦点。
      } //if (mIat!=null) //识别器存在。

      return result;
    }

    /**
     * 设置语言及区域参数字符串。
     */
    private void setLanguageAndAccentParameters()
    {
      boolean foundDirectLanguage=false; //是否已经直接找到匹配的语言

      //获取系统当前的语言。
      Locale locale=Locale.getDefault(); //获取默认语系。

      String androidLocaleName=locale.toString(); //获取语系名字。

      Locale zhCnLocale=Locale.SIMPLIFIED_CHINESE;

      if (androidLocaleName.startsWith("zh_CN")) //简体中文。
      {
        mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        foundDirectLanguage=true;
      } //简体中文。
      else if (androidLocaleName.startsWith("en")) //英语
      {
        mIat.setParameter(SpeechConstant.LANGUAGE,"en_us");

        foundDirectLanguage=true;
      } //else if (androidLocaleName.startsWith("en")) //英语
      else //其它语言。后面还有机会选择
      {
        mIat.setParameter(SpeechConstant.LANGUAGE,"en_us");

        foundDirectLanguage=false; //未直接找到匹配的语言
      } //else //英语。

      Locale[] locales = Locale.getAvailableLocales();
      ArrayList<String> localcountries=new ArrayList<String>();
      for(Locale l:locales)
      {
        localcountries.add(l.getDisplayLanguage().toString());
      }
      
      String[] languages=(String[]) localcountries.toArray(new String[localcountries.size()]);

      if (foundDirectLanguage) //直接找到了语言
      {
      } //if (foundDirectLanguage) //直接找到了语言
      else //未直接找到语言
      {
        LocaleList localeList;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) //选择多种语言
        {
          localeList= LocaleList.getDefault(); //获取默认语言列表

          int localeSize=localeList.size();

          int localeCounter=0;

          for(localeCounter=0; localeCounter< localeSize; localeCounter++)
          {
            Locale locale1=localeList.get(localeCounter);

            androidLocaleName=locale1.toString(); //获取语系名字。

            if (androidLocaleName.startsWith("zh_CN")) //简体中文。
            {
              mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
              mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

              foundDirectLanguage=true;
            } //简体中文。
            else if (androidLocaleName.startsWith("en")) //英语
            {
              mIat.setParameter(SpeechConstant.LANGUAGE,"en_us");

              foundDirectLanguage=true;
            } //else if (androidLocaleName.startsWith("en")) //英语
            else //其它语言。后面还有机会选择
            {
              mIat.setParameter(SpeechConstant.LANGUAGE,"en_us");

              foundDirectLanguage=false; //未直接找到匹配的语言
            } //else //英语。

            if (foundDirectLanguage) //找到了。
            {
              break; //不用再找了
            } //if (foundDirectLanguage) //找到了。
          } //for(localeCounter=0; localeCounter< localeSize; localeCounter++)
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) //选择多种语言
      } //else //未直接找到语言
    } //private void setLanguageAndAccentParameters()

    @Override
    /**
     * 活动被停止。被另一个活动完全覆盖。
     */
    protected void onStop()
    {
      super.onStop(); //超类停止。

      saveApplicationLaunchCount(); //保存应用的启动计数数据。
    } //protected void onStop()

    @Override
    /**
     * 活动正在被销毁。
     */
    protected void onDestroy()
    {
      super.onDestroy();

      SpeechUtility speechUtility=SpeechUtility.getUtility(); //检查语音识别辅助对象是否存在

      if (speechUtility!=null) //存在
      {
        speechUtility.destroy(); //销毁
      } //if (speechUtility!=null) //存在

      unregisterBroadcastReceiver(); //解除注册广播接收器。
    } //protected void onDestroy()

    /**
     * 保存应用的启动计数数据。
     */
    private void saveApplicationLaunchCount()
    {
    } //private void saveApplicationLaunchCount()

  /**
    * 根据语音识别结果，在已安装的应用程序列表中查找。
    * @param courtList 已安装的应用程序列表。
    */
  private boolean findVoiceTargetApplicationNameAndLaunch()
  {
    boolean foundActivity=false; //返回结果，是否命中了活动。
    String Result = ""; // 结果。

    String maskFileName=""; //获取掩码图片文件名。

    boolean directHit=false; //是否直接命中了识别结果对应的应用程序。

    //根据活动标签的映射来匹配：

    Collection<PackageItemInfo> packageItemInfos=(Collection<PackageItemInfo>) activityLabelPackageItemInfoMap.get(voiceRecognizeResultString); //寻找集合

    if (packageItemInfos!=null) //存在映射目标
    {
      Object packageItemInfoObject=packageItemInfos.toArray()[0]; //获取第一个

      PackageItemInfo packageItemInfo= ((PackageItemInfo)packageItemInfoObject); //根据识别结果从映射中寻找。
      //            PackageItemInfo packageItemInfo= ((PackageItemInfo[])packageItemInfos.toArray())[0]; //根据识别结果从映射中寻找。
      //        PackageItemInfo packageItemInfo= activityLabelPackageItemInfoMap.get(voiceRecognizeResultString); //根据识别结果从映射中寻找。

      if (packageItemInfo!=null) //找到了。
      {
        PackageManager packageManager=getPackageManager(); //获取软件包管理器。

        Intent launchIntent= packageManager.getLaunchIntentForPackage(packageItemInfo.packageName); //获取当前软件包的启动意图。

        if (launchIntent!=null) // The launch intent exists
        {
          directHit=true; //直接命中了识别结果对应的应用程序。

          foundActivity=true; //命中了。

          Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
          mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

          Intent activitylaunchIntent=new Intent(mainIntent);
          activitylaunchIntent.setClassName(packageItemInfo.packageName, packageItemInfo.name); //设置类名。

          scrollToIconPosition(packageItemInfo.packageName, packageItemInfo.name); //滚动到图标所在的位置。

          showHitApplication(packageItemInfo); //显示命中了应用的图标。

          launchApplication(activitylaunchIntent); //启动应用。

          AnswerAvailableEvent event=new AnswerAvailableEvent(packageItemInfo.packageName, packageItemInfo.name); //构造事件对象

          rememberVoicePackageNameRelationship(event); //记录语音识别结果与应用包名之间的关系。

          voiceCommandHitDataReporter.reportVoiceCommandHitData(voiceRecognizeResultString, packageItemInfo.packageName, packageItemInfo.name, recordSoundFilePath, ActivityIconType, voiceRecognizeResultString); //报告语音识别命中应用的数据。命中活动．
        } // if (launchIntent!=null) // The launch intent exists
      } //if (packageItemInfo!=null) //找到了。
    } //if (packageItemInfos!=null) //存在映射目标

    //根据钉住的快捷方式的准确名字来匹配：
    if (foundActivity) //命中了活动。
    {
    } //if (foundActivity) //命中了活动。
    else  //未命中活动。
    {
      if (shortcutTitleInfoMap!=null) //映射本身存在。
      {
        ShortcutInfo mappedPackageName=shortcutTitleInfoMap.get(voiceRecognizeResultString); //从映射中寻找快捷方式。

        if (mappedPackageName!=null) //存在映射。
        {
          directHit=true; //直接命中了识别结果对应的应用程序。

          foundActivity=true; //命中了。

          launchShortcut(mappedPackageName); //启动快捷方式。
        } //if (applicationLabel.equals(voiceRecognizeResultString)) //正是等于识别结果。
      } //if (shortcutTitleInfoMap!=null) //映射本身存在。
    } //else  //未命中活动。

    //根据国际化名字的映射来匹配：
    if (foundActivity) //命中了活动，则不需要再根据国际化名字来匹配。
    {
    } //if (foundActivity) //命中了活动，则不需要再根据国际化名字来匹配。
    else //未命中活动，尝试根据国际化名字来匹配。
    {
      //按照应用程序的国际化名字来匹配：
      //注意，目前还是以应用程序包名为单位，后面要改成以具体活动名字为单位：

      String mappedPackageName=internationalizationDataPackageNameMap.get(voiceRecognizeResultString); //从映射中寻找包名。

      if (mappedPackageName!=null) //存在映射。
      {
        PackageManager packageManager=getPackageManager(); //获取软件包管理器。

        Intent launchIntent= packageManager.getLaunchIntentForPackage(mappedPackageName); //获取当前软件包的启动意图。

        if (launchIntent!=null) // The launch intent exists
        {
          directHit=true; //直接命中了识别结果对应的应用程序。

          foundActivity=true; //命中了。
          scrollToIconPosition(mappedPackageName, null); //滚动到图标所在的位置。

          showHitApplication(mappedPackageName); //显示命中了应用的图标。

          launchApplication(launchIntent); //启动应用。
        } // if (launchIntent!=null) // The launch intent exists
      } //if (applicationLabel.equals(voiceRecognizeResultString)) //正是等于识别结果。
    } //else //未命中活动，尝试根据国际化名字来匹配。

    if (directHit) //是直接命中了应用程序。
    {
      voiceRecognizeResultString=null; //清空识别结果。
    } //if (directHit) //是直接命中了应用程序。
    else //未直接命中应用。
    {
      foundActivity=findVoiceTargetMapApplicationAndLaunch(voicePackageNameMap); //从映射中寻找目标应用，并启动。从用户自己积累的映射中寻找。

      //从内置软件包映射中寻找：
      if (foundActivity) //命中了。
      {
      } //if (foundActivity) //命中了。
      else //未命中。
      {
        foundActivity=findVoiceTargetMapApplicationAndLaunch(voicePackageNameMapBultin); //从映射中寻找目标快捷方式，并启动。从用户自己积累的映射中寻找。
      } //else //未命中。

      //从快捷方式映射中寻找，通过点击动作关联起来的映射：
      if (foundActivity) //命中了。
      {
      } //if (foundActivity) //命中了。
      else //未命中。
      {
        foundActivity=findVoiceTargetMapShortcutAndLaunch(voiceShortcutIdMap); //从映射中寻找目标快捷方式，并启动。从用户自己积累的映射中寻找。
      } //else //未命中。
    } //else //未直接命中应用。

    return foundActivity;
  } //private void findVoiceTargetApplicationNameAndLaunch(List<PackageInfo> courtList)

  /**
    * 显示命中了应用的图标。
    * @param mappedPackageName 应用的包名。
    */
  private void showHitApplication(String mappedPackageName)
  {
    PackageManager packageManager=getPackageManager(); //获取软件包管理器。

    String activityTitle=""; //获取标题．

    try
    {
      Drawable activityDrawable=packageManager.getApplicationIcon(mappedPackageName); //获取图标。

      hitApplicationIcon.setVisibility(View.VISIBLE);
      hitApplicationIcon.setImageDrawable(activityDrawable); //显示图标。

      animateHitApplication(); //动画旋转，命中应用。陈欣
    }
    catch (PackageManager.NameNotFoundException e)
    {
      e.printStackTrace();
    } //catch (PackageManager.NameNotFoundException e)
  } //private void showHitApplication(String mappedPackageName)

  /**
    * 动画旋转，命中应用。
    */
  private void animateHitApplication()
  {
    hitApplicationIcon.animate().rotationYBy(360).start();

    float x=hitApplicationIcon.getX()+voiceAssistantLayout.getX();
    float y=hitApplicationIcon.getY()+voiceAssistantLayout.getY();

    View viewObject=launcher_activity.findViewById(R.id.loveAnimation);

    if (loveAnimation!=null) //找到了视图
    {
        loveAnimation.animateHeart(x, y); //显示点赞爱心动画
    } //if (loveAnimation!=null) //找到了视图
  } //private void animateHitApplication()

  /**
    * 显示点赞爱心动画
    * @param itemViewgetX X 坐标
    * @param itemViewgetY Y 坐标
    */
  public void animateHeart(float itemViewgetX, float itemViewgetY)
  {
      float x=itemViewgetX;
      float y=itemViewgetY;

      if (loveAnimation!=null) //找到了视图
      {
          loveAnimation.animateHeart(x, y); //显示点赞爱心动画
      } //if (loveAnimation!=null) //找到了视图

  } //public void animateHeart(float itemViewgetX, float itemViewgetY)

  /**
    * 显示命中了快捷方式的图标。
    * @param packageName 包名
    * @param activityName 快捷方式编号
    */
  private void showHitShortCut(String packageName, String activityName)
  {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式
      {
          LauncherApps launcherApps=(LauncherApps)(getSystemService(Context.LAUNCHER_APPS_SERVICE));

          ShortcutInfo shortcutInfo=shortcutIdInfoMap.get(activityName); //获取快捷方式对象

          if (shortcutInfo!=null) //快捷方式信息对象存在
          {
              String articleTitle=shortcutInfo.getShortLabel().toString(); //获取文章标题。

              Drawable applicationIcon=launcherApps.getShortcutIconDrawable(shortcutInfo, 0); //获取图标。

              hitApplicationIcon.setVisibility(View.VISIBLE);
              hitApplicationIcon.setImageDrawable(applicationIcon); //显示图标。

              animateHitApplication(); //动画旋转，命中应用。陈欣
          } //if (shortcutInfo!=null) //快捷方式信息对象存在
      } //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) //21 之后才有启动应用
  } //private void showHitShortCut(String packageName, String activityName)

  /**
    * 显示命中了应用的图标。
    * @param packageItemInfo 应用的活动条目信息。
    */
  private void showHitApplication(PackageItemInfo packageItemInfo)
  {
    PackageManager packageManager=getPackageManager(); //获取软件包管理器。

    String activityTitle=""; //获取标题．

    ComponentName cm = new ComponentName(packageItemInfo.packageName, packageItemInfo.name); //构造组件名字对象。

    try
    {
      ActivityInfo activityInfo=packageManager.getActivityInfo(cm, 0); //获取活动信息。

      Drawable activityDrawable=packageManager.getActivityIcon(cm); //获取图标。

      hitApplicationIcon.setVisibility(View.VISIBLE);
      hitApplicationIcon.setImageDrawable(activityDrawable); //显示图标。

      animateHitApplication(); //动画旋转，命中应用。陈欣
    }
    catch (PackageManager.NameNotFoundException e)
    {
      e.printStackTrace();
    } //catch (PackageManager.NameNotFoundException e)
  } //private void showHitApplication(PackageItemInfo packageItemInfo)

  /**
    * 滚动到图标所在的位置。
    * @param packageItemInfopackageName 应用程序包名。
    * @param packageItemInfoname 活动的类名。
    */
  private void scrollToIconPosition(String packageItemInfopackageName, String packageItemInfoname)
  {
    if (mAdapter!=null) // The adapater exists
    {
      int position = mAdapter.getItemPosition(packageItemInfopackageName, packageItemInfoname); //获取位置。

      mRecyclerView.smoothScrollToPosition(position); //丝滑滚动到位置。
    } // if (mAdapter!=null) // The adapater exists
  } //private void scrollToIconPosition(String packageItemInfopackageName, String packageItemInfoname)
    
  /**
  * Scroll to last  active application.
  */
  private void scrollToLastApplication()
  {
    UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

    long currentTimeMillis = System.currentTimeMillis();
    long time10MinutesAgo = currentTimeMillis - (15 * 1000); // 15 seconds ago

    UsageEvents usageEvents = usageStatsManager.queryEvents(time10MinutesAgo, currentTimeMillis);

    // if (usageStatsList!=null && !usageStatsList.isEmpty())
    String packageName = null;

    String activityName = null;

    if (usageEvents!=null && usageEvents.hasNextEvent()) // Not empty.
    {
      while (usageEvents.hasNextEvent())
      {
        UsageEvents.Event lastAppUsageStatsiter = new UsageEvents.Event();
        usageEvents.getNextEvent(lastAppUsageStatsiter);

        int eventType = lastAppUsageStatsiter.getEventType();

        if ( eventType == UsageEvents.Event.ACTIVITY_PAUSED) // Stopped.
        {
          packageName = lastAppUsageStatsiter.getPackageName();
          activityName = lastAppUsageStatsiter.getClassName();
        } // if (lastAppUsageStatsiter.getEventType() == UsageEvents.Event.ACTIVITY_STOPPED) // Stopped.

      } // for(UsageStats lastAppUsageStats : usageStatsList)

      if (packageName!=null) // Found valid event.
      {
        scrollToIconPosition(packageName, activityName);
      } // if (packageName!=null) // Found valid event.

    } // if (usageStatsList!=null && !usageStatsList.isEmpty())
    else // Do not have permission.
    {
      requestUsageStatsPermission() ;
    } // else // Do not have permission.
  } // private void scrollToLastApplication()

  private boolean hasUsageStatsPermission()
  {
    boolean result = false; // The result;
    UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
    if (usageStatsManager == null)
    {
    }
    else // The usage stats manager exists.
    {
      long currentTimeMillis = System.currentTimeMillis();
      long time10MinutesAgo = currentTimeMillis - (10 * 60 * 1000); // 10 minutes ago

      try
      {
        // 尝试调用需要权限的方法
        Map<String, UsageStats> usageMap = usageStatsManager.queryAndAggregateUsageStats(time10MinutesAgo, currentTimeMillis);

        if (usageMap!=null) // Got the map successfully.
        {
          if (usageMap.size() > 0) // Their are items in the map.
          {
            result = true;
          } // if (usageMap.size() > 0) // Their are items in the map.
        } // if (usageMap!=null) // Got the map successfully.
      }
      catch (SecurityException e)
      {
        // 捕获 SecurityException 表明权限未被授予
        // return false;
      }
    } // else // The usage stats manager exists.

    return result;
  } // private boolean hasUsageStatsPermission()

  private void showSettingsScreen()
  {
    // 打开应用设置页面
    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
    startActivityForResult(intent, REQUEST_CODE_USAGE_STATS);
  }

  private void onPermissionGranted()
  {
    // 权限已被用户授予，可以继续使用
    // 在这里添加你需要执行的操作
    // loadApplications();
  }

  private void onPermissionDenied()
  {
    // 权限被用户拒绝
    // 可以提示用户为什么需要这个权限
  }

  private void requestUsageStatsPermission()
  {
    if (!hasUsageStatsPermission())
    {
      // 如果权限未被授予，则引导用户前往设置页面
      showSettingsScreen();
    }
    else
    {
      // 如果权限已经被授予，则可以开始使用它
      onPermissionGranted();
    }
  }

  /**
  * 构造映射，应用的国际化名字与包名之间的映射。
  */
  private void buildInternationalizationDataPackageNameMap()
  {
    MetadataPreloader.buildInternationalizationDataPackageNameMap(this);
  }

    /**
     * 构造映射，活动的标签，与活动的包条目信息之间的映射。用于语音识别之后快速命中活动。
     */
    private void buildActivityLabelPackageItemInfoMap()
    {
      MetadataPreloader.buildActivityLabelPackageItemInfoMap(this);
    } //private void buildActivityLabelPackageItemInfoMap()

  /**
  * 解析启动意图。
  */
  private void solveLauncherIntents()
  {
    appInfoScanner.refresh(); // 清除缓存，下次 scan 会重新扫描

    // 1. 委托扫描
    ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>(appInfoScanner.scanLauncherApps());

    // 2. 重建 position 映射（这是 UI 状态，必须每次重算）
    packageNameItemNamePositionMap.clear();

    packageNamePositionMap.clear();

    for (int position = 0; position < articleInfoArrayList.size(); position++)
    {
        ArticleInfo app = articleInfoArrayList.get(position);

        String key = app.getPackageName() + "/" + app.getActivityName();

        packageNameItemNamePositionMap.put(key, position);

        packageNamePositionMap.put(app.getPackageName(), position);
    }

    // 3. 更新 UI
    if (mAdapter != null)
    {
        mAdapter.setArticleInfoArrayList(articleInfoArrayList);

        mAdapter.notifyDataSetChanged();
    }
  }

    @Override
    /**
    * Request to download and install package apk.
    */
    public boolean requestDownloadApk(String foundPackageName) 
    {
      boolean foundUrl=false;
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      Map<String,String> internationalizationData=application.getPackageNameUrlMap(); //获取国际化数据对象。
      Map<String, String> packageNameApplicationMap=application.getPackageNameApplicationNameMap(); //获取包名与应用程序名字的映射

      if (internationalizationData!=null) //数据存在。
      {
        String internationalizationName=internationalizationData.get(foundPackageName); //获取国际化名字。

        if (internationalizationName!=null) //有国际化名字。
        {
          foundUrl=true;

          String applicationName=packageNameApplicationMap.get(foundPackageName); //应用程序名字

          requestDownloadPackage(internationalizationName, applicationName, foundPackageName); //下载应用程序安装包。
        } //if (internationalizationName!=null) //有国际化名字。
      } //if (internationalizationData!=null) //数据存在。

      return foundUrl;
    } // public boolean requestDownloadApk(String shutDownAt2100PackageName)

    /**
     * 下载应用程序安装包。
     * @param internationalizationName 安装包路径。
     */
    public void requestDownloadPackage(String internationalizationName, String applicationName, String packageName)
    {
      PackageInformationManager packageInformatinManager = new PackageInformationManager(); // The package information manager.
      boolean installed = packageInformatinManager.checkInstalled(packageName); // 检查该应用是不是已经安装了。

      if (installed) // 该应用已经安装。
      {
      } // if (installed) // 该应用已经安装。
      else // 该应用尚未安装。
      {
        String string="Downloading " + applicationName; // 通知字符串，正在下载。陈欣。
          
        startTimeCheckService(applicationName); // 启动下载通知服务。陈欣。
        
        launchRipple.setVisibility(View.VISIBLE); // Show donwloading progress float icon.
        circularProgressBar.setProgress(0); // Reset progress.

        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。
        
        if (packageName==null) // The package name does not exist
        {
          Map<String, String> apkUrlPackageNameMap = application.getApkUrlPackageNameMap(); // Get the map of apk url to package name.
          
          if (apkUrlPackageNameMap!=null) // The url to package name map exists
          {
            packageName = apkUrlPackageNameMap.get(internationalizationName); // Get the package name from apk url.
          } // if (apkUrlPackageNameMap!=null) // The url to package name map exists
        } // if (iconUrl==null) // The icon url does not exist
        
        if (packageName!=null) // The package name exists
        {
          String iconUrl=application.getIconForPackage(packageName); // Get icon url.
          
          Glide.with(application).load(iconUrl).placeholder(R.drawable.vector_66_11).into(applicationIconrightimageView2); //显示图标。

          Map<String, String> packageNameApplicationMap=application.getPackageNameApplicationNameMap(); // 获取包名与应用程序名字的映射

          rightTextoperationMethodactTitletextView2.setText(applicationName); // Show applicaiton name.

          downloadRequestor.requestDownloadUrl(internationalizationName, internationalizationName, applicationName, packageName, this); //要求下载网址
        } // if (packageName!=null) // The package name exists
      } // else // 该应用尚未安装。
    } //private void requestDownloadPackage(String internationalizationName)
    
    /**
    * 报告，下载 finished。
    */
    public void reportDownloadFinished(String packageName)
    {
      launchRipple.setVisibility(View.INVISIBLE); // Hide the downloading icon.
      circularProgressBar.setProgress(0); // Reset progress.
      
      NotificationControlManager notificationControlManager = new NotificationControlManager(); // Craet ethe notificaityon control manager.
      notificationControlManager.stopDownloadNotification(); // Stop the download notification.
      // stopDownloadNotification(); // Stop the download notification.
    } // public void reportDownloadFinished(String packageName)
    
    /**
    * 报告，下载 progress。
    */
    public void reportDownloadProgress(String packageName, long downloaded, long total) 
    {
      circularProgressBar.setProgressMax(total);

      circularProgressBar.setProgressWithAnimation(downloaded, 323l);
    } // public void reportDownloadProgress(String packageName, long downloaded, long total)
    
    /**
    * 报告，下载失败。
    */
    public void  reportDownloadFailed(String packageName) 
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。
      
      application.startCheckUpgrade(); // Start check upgrade again.

      // Chen xin. Voice ui download failec.
      String mWordSeparators = getResources().getString(R.string.downloadFailed); // load explain text string. download failed

      voiceUi.say(mWordSeparators); // say , download failed.
      launchRipple.setVisibility(View.INVISIBLE); // Hide donwloading progress float icon.
      circularProgressBar.setProgress(0); // Reset progress.
      
      NotificationControlManager notificationControlManager = new NotificationControlManager(); // Craet ethe notificaityon control manager.
      notificationControlManager.stopDownloadNotification(); // Stop the download notification.
      
      // Chen xin. report download failure.
      downloadFailureReporter.reportDownloadFailure(packageName, RabbitMQUserName, RabbitMQPassword, TRANSLATE_REQUEST_QUEUE_NAME); // report download failure.
    } // public void  reportDownloadFailed(String packageName)
    
    /**
    * 显示信息页面。
    */
    private void showUrlPage(String internationalizationName) 
    {
      try
      {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(internationalizationName)));
      }
      catch (ActivityNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (NullPointerException e) // 捕获空指针异常。
      {
        e.printStackTrace();
      }
    } // private void  showInformationPage(String internationalizationName)

  /**
    * Start the notification listener service.
    */
    private void startNotifictionListenerService()
    {
      Intent serviceIntent = new Intent(this, NotificationListernService02.class); //创建意图。
      
      // serviceIntent.putExtra("applicationName", applicationName);

      startService(serviceIntent); //启动服务。
    } // private void startNotifictionListenerService() 
  
    /**
    * 启动时间检查服务。
    */
    private void startTimeCheckService(String applicationName) 
    {
      Intent serviceIntent = new Intent(this, DownloadNotificationService.class); //创建意图。
      
      serviceIntent.putExtra("applicationName", applicationName);

      startService(serviceIntent); //启动服务。
    } //private void startTimeCheckService()

  /**
  * 从映射中寻找目标快捷方式，并启动。
  * @param voiceShortcutIdMap 要进行寻找的映射．
  * @return 是否寻找到了．
  */
  private boolean findVoiceTargetMapShortcutAndLaunch(HashMap<String, HxShortcutInfo> voiceShortcutIdMap)
    {
      boolean result=false; //结果，是否命中了活动。
      
      if (voiceShortcutIdMap!=null) // The map exists. Loaded.
      {
        if (voiceShortcutIdMap.containsKey(voiceRecognizeResultString)) //有对应的映射关系。用户自己积累的语音指令与包条目映射。
        {
          String packageName=voiceShortcutIdMap.get(voiceRecognizeResultString).packageName; //获取包名。
          String activityName=voiceShortcutIdMap.get(voiceRecognizeResultString).shortcutId; //获取快捷方式编号。

          result=true; //命中了。

          scrollToIconPosition(packageName, activityName); //滚动到图标所在的位置。

          showHitShortCut(packageName, activityName); //显示命中了快捷方式的图标。

          rememberVoiceCommandHitData(voiceRecognizeResultString, packageName, activityName, ShortcutIconType, LocalVoiceCommandMap); //记录语音识别命中应用的数据

          result=launchShortcut(packageName, activityName); //启动快捷方式．

          List<String> idList= Arrays.asList(activityName); //构造编号列表．

          if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。
          {
            LauncherApps.ShortcutQuery query=new LauncherApps.ShortcutQuery(); //创建查询器．

            int queryFlag= FLAG_MATCH_PINNED | FLAG_MATCH_MANIFEST | FLAG_MATCH_DYNAMIC; //查询标志位

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.P) //25 之后才有快捷方式编号。
            {
              queryFlag = queryFlag | FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
            } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。

            query.setQueryFlags(queryFlag); //设置标志位．

            query.setShortcutIds(idList); //设置编号列表．
            query.setPackage(packageName); //设置包名．

            LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。

            List<ShortcutInfo> shortcutInfos=launcherApps.getShortcuts(query, Process.myUserHandle()); //获取快捷方式列表．

            if (shortcutInfos.size()>0) //有快捷方式。
            {
              ShortcutInfo shortcutInfo=shortcutInfos.get(0); //获取第一个快捷方式信息．

              String shortcutTitle=shortcutInfo.getShortLabel().toString(); //获取标题．

              voiceCommandHitDataReporter.reportVoiceCommandHitData(voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, ShortcutIconType, shortcutTitle); // Report voice command hit data. Hit short cut.
            } //if (shortcutInfos.size()) //有快捷方式。
          } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。
        } //if (voicePackageNameMap.contains(voiceRecognizeResultString)) //有对应的映射关系。
      } // if (voiceShortcutIdMap!=null) // The map exists. Loaded.

      return result;
    } //private boolean findVoiceTargetMapShortcutAndLaunch(HashMap<String, HxShortcutInfo> voiceShortcutIdMap)

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
    } //private boolean rememberVoiceCommandHitData(String voiceRecognizeResultString, String packageName, String activityName, LauncherIconType activityIconType)

    /**
     * 从映射中寻找目标应用，并启动。
     */
    private boolean findVoiceTargetMapApplicationAndLaunch(   SetValuedMap<String, PackageItemInfo> voicePackageNameMap)
    {
      boolean result=false; //结果，是否命中了活动。

      if (voicePackageNameMap!=null) // Exists
      {
        if (voicePackageNameMap.containsKey(voiceRecognizeResultString)) // 有对应的映射关系。用户自己积累的语音指令与包条目映射。
        {
          Collection<PackageItemInfo> packageItemInfos=(Collection<PackageItemInfo>) voicePackageNameMap.get(voiceRecognizeResultString); //获取集合信息

          for (PackageItemInfo packageItemInfo: packageItemInfos) //一个个地尝试
          {
            String packageName=packageItemInfo.packageName; //获取包名。
            String activityName=packageItemInfo.name; //获取活动名。

            result=true; //命中了。

            if (activityName.isEmpty()) //不包含活动名。
            {
              scrollToIconPosition(packageName, null); //滚动到图标所在的位置。

              result= launchApplicationByPackageName(packageName); //根据包名启动应用程序。
            } //if (activityName==null) //不包含活动名。
            else //包含活动名。
            {
              Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
              mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

              Intent activitylaunchIntent=new Intent(mainIntent);
              activitylaunchIntent.setClassName(packageName, activityName); //设置类名。

              scrollToIconPosition(packageName, activityName); //滚动到图标所在的位置。

              showHitApplication(packageItemInfo); //显示命中了应用的图标。

              result = launchApplication(activitylaunchIntent); //启动活动。
            } //else //包含活动名。

            if (result) //成功地寻找并且启动了应用
            {
              PackageManager packageManager=getPackageManager(); //获取软件包管理器。

              String activityTitle=""; //获取标题．

              ComponentName cm = new ComponentName(packageName,activityName); //构造组件名字对象。

              try 
              {
                ActivityInfo activityInfo=packageManager.getActivityInfo(cm, 0); //获取活动信息。

                activityTitle=activityInfo.loadLabel(packageManager).toString(); //获取标题。
              }
              catch (PackageManager.NameNotFoundException e)
              {
                e.printStackTrace();
              } //catch (PackageManager.NameNotFoundException e)


              if (voicePackageMapRegretManager.shouldRegret(voiceRecognizeResultString, packageItemInfo, activityTitle)) // Should regret.
              {
                // Chen xin. regret. Unlink.
                voicePackageNameMap.removeMapping(voiceRecognizeResultString, packageItemInfo); // 删除关联
                saveVoicePackageNameMap(); //保存映射。
              } // if (voicePackageMapRegretManager.shouldRegret(voiceRecognizeResultString, packageItemInfo, articleTitle)) // Should regret.
              else // Not regret.
              {
                rememberVoiceCommandHitData(voiceRecognizeResultString, packageName, activityName, ActivityIconType, LocalVoiceCommandMap); //记录语音识别命中应用的数据
              } // else // Not regret.

              voiceCommandHitDataReporter.reportVoiceCommandHitData(voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, ActivityIconType, activityTitle); // Report voice command hit data. Hit activity.

              break; //跳出
            } //if (result) //成功地寻找并且启动了应用
          } //for (PackageItemInfo packageItemInfo: packageItemInfos) //一个个地尝试
        } //if (voicePackageNameMap.contains(voiceRecognizeResultString)) //有对应的映射关系。
      } // if (voicePackageNameMap!=null) // Exists

      return result;
    } //private void findVoiceTargetMapApplicationAndLaunch()

    /**
     * 发送语音指令关联数据。
     */
    private void sendVoiceShortcutAssociationData()
    {
        VoiceShortcutAssociationDataSendTask translateRequestSendTask =new VoiceShortcutAssociationDataSendTask(); //创建异步任务。
        translateRequestSendTask.setLauncherActivity(this); //设置启动活动。

        translateRequestSendTask.execute(); //执行任务。
    } //private void sendVoiceShortcutAssociationData()

    /**
     * 发送语音指令关联数据。
     */
    private void sendVoiceAssociationData()
    {
        VoiceAssociationDataSendTask translateRequestSendTask =new VoiceAssociationDataSendTask(); //创建异步任务。
        translateRequestSendTask.setLauncherActivity(this); //设置启动活动。

        translateRequestSendTask.execute(); //执行任务。
    } //private void sendVoiceAssociationData()

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
                showHitApplication(packageName); //显示命中了应用的图标。

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

    /**
     * 启动快捷方式。
     * @param shortcutInfo 要启动的快捷方式信息对象。
     */
    public void launchShortcut(ShortcutInfo shortcutInfo)
    {
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。
      {
        String shortcutId=shortcutInfo.getId(); //Get the shortcut id.

        String packageName=shortcutInfo.getPackage(); //获取包名。

        hideIconList(); //隐藏图标列表

        launchShortcut(packageName, shortcutId); //启动快捷方式。
      } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。
    } //public void launchShortcut(ShortcutInfo shortcutInfo)

    /**
     * 启动快捷方式。
     * @param shortcutInfo 要启动的快捷方式信息对象。
     */
    public boolean launchShortcut(String packageName, String shortcutId)
    {
        boolean launchSuccess=false; //是否成功启动。

        try //尝试启动快捷方式
        {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。
            {
                LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。

                launcherApps.startShortcut(packageName, shortcutId, null, null, Process.myUserHandle()); //启动快捷方式。

                launchSuccess=true; //成功启动。

            } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25 之后才有快捷方式编号。
        } //try //尝试启动快捷方式
        catch (ActivityNotFoundException e)
        {
          e.printStackTrace();
        } //catch (ActivityNotFoundException e)

        if (launchSuccess) //成功启动。
        {
        } //if (launchSuccess) //成功启动。
        else //未能成功启动。
        {
          launchSuccess= launchApplicationByPackageName(packageName); //启动应用程序本身。
        } //else //未能成功启动。

        return launchSuccess;
    } //public void launchShortcut(ShortcutInfo shortcutInfo)

    /**
     * 启动应用。
     * @param launchIntent 启动用的意图。
     */
    public boolean launchApplication(Intent launchIntent)
    {
      HxLauncherApplication hxLauncherApplication = HxLauncherApplication.getInstance(); //获取应用程序对象。
      boolean result=false; //结果

      try //尝试启动活动，并且捕获可能的异常。
      {
        if (launchIntent!=null) //启动意图存在。
        {
          boolean allowedToLaunch = hxLauncherApplication.checkLaunchCoolDownTime(launchIntent); //检查启动的冷却时间。

          if (allowedToLaunch) //允许启动。
          {
            checkAndAquireWakeLock(launchIntent); //检查并获取唤醒锁。

            PackageInformationManager packageInformatinManager = new PackageInformationManager(); // The package information manager.
            // boolean installed = packageInformatinManager.checkInstalled(packageName); // 检查该应用是不是已经安装了。
            boolean exported =  packageInformatinManager.checkExported(launchIntent); //检查该个活动是否被导出了。

            String packageName=launchIntent.getComponent().getPackageName(); //获取包名。
            
            boolean applicationLocked=checkApplicationLocked(launchIntent); // 检查该应用是不是被锁住了。
            
            if (exported) //是导出了。
            {
              if (applicationLocked) // 应用被加锁。
              {
                gotoApplicationUnlockActivity(launchIntent); // 跳转到应用程序解锁活动。
              }
              else // 应用未被加锁。
              {
                launchIntent.putExtra( Constants.Fields.VoiceRecognizeResult, voiceRecognizeResultString ); // SEt the voice recognize result.
                assessExcludeFromRecentActivities(launchIntent); // Assess to exclude from recent activites.
              
                startActivity(launchIntent); //启动活动。
              } // else // 应用未被加锁。
            } //if (exported) //是导出了。e
            else //未导出。启动该应用的默认活动。
            {
              launchApplicationByPackageName(packageName); //按照包名启动。
            } //else //未导出。

            hxLauncherApplication.rememberLaunchTimestamp(launchIntent); //记录启动时间戳。
          } //if (allowedToLaunch) //允许启动。
        } //                    if (launchIntent!=null) //启动意图存在。

        result=true; //启动成功

        hideIconList(); //隐藏图标列表
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
    * Assess to exclude from recent activites.
    */
    private void assessExcludeFromRecentActivities(Intent launchIntent)
    {
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。

      if (hxLauncherApplication.shouldExcludeFromRecentActivities(launchIntent)) // Should exclude from recent activities
      {
        // https://stackoverflow.com/a/30700304
        // SEt this flag when launchering.
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
      } // if (hxLauncherApplication.shouldExcludeFromRecentActivities(launchIntent)) // Should exclude from recent activities
    } // private void assessExcludeFromRecentActivities(Intent launchIntent)
    
    /**
    * 检查该应用是不是被锁住了。
    */
    private boolean checkApplicationLocked(Intent launchIntent)
    {
      boolean result=true; //结果。

      if (launchIntent!=null) //启动意图存在。
      {
        String packageName=launchIntent.getComponent().getPackageName(); //获取包名。

        HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
        
        HashSet<String> applicationLockSet = hxLauncherApplication.getApplicationLockSet(); // 获取应用锁集合。
        
        if (applicationLockSet!=null) // The set exists
        {
          result = applicationLockSet.contains(packageName); // 是否 包含。
        } // if (applicationLockSet!=null) // The set exists
      } //if (launchIntent!=null) //启动意图存在。
      else //启动意图不存在。
      {
        result=false; //未导出。
      } //else //启动意图不存在。

      return result;
    } // private boolean checkApplicationLocked(Intent launchIntent)

    /**
     * 主动崩溃。
     */
    private void crashIntended()
    {
       startActivity(null);
    } //private void crashIntended()

    /**
     * 判断要不要释放唤醒锁。
     * @param uid 当前为其进行输入的包名。
     */
    private void assesReleaseWakeLock(String uid)
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      Set<String> wakeLockPackageNameSet=application.getWakeLockPackageNameSetData().getWakeLockPackageNameSet(); //获取唤醒锁应用包名集合数据。

      if (wakeLockPackageNameSet.contains(uid)) //在唤醒锁名单中。
      {
      } //if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
      else //不在唤醒锁名单中。应当释放。
      {
        HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。
        hxLauncherApplication.releaseWakeLock(); //释放唤醒锁。
      } //else //不在唤醒锁名单中。应当释放。
    } //private void assesReleaseWakeLock(String uid)
    
    /**
    * Assess to aquire wake lock
    */
    private void assesAquireWakeLock(String packageName)
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      Set<String> wakeLockPackageNameSet=application.getWakeLockPackageNameSetData().getWakeLockPackageNameSet(); //获取唤醒锁应用包名集合数据。

      if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
      {
        HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

        hxLauncherApplication.acquireWakeLock(); //获取唤醒锁。
      } //if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
    } // private void assesAquireWakeLock(String packageName)

    /**
     * 检查并获取唤醒锁。
     * @param launchIntent 启动意图。
     */
    private void checkAndAquireWakeLock(Intent launchIntent)
    {
      if (launchIntent!=null) //意图存在。
      {
        String packageName=launchIntent.getComponent().getPackageName(); //获取包名。
        
        assesAquireWakeLock(packageName); // Assess to aquire wake lock
      } //if (launchIntent!=null) //意图存在。
    } //private void checkAndAquireWakeLock(Intent launchIntent)

    /**
     * 显示软件包列表。
     * @param packageInfoList 软件包列表。
     */
    private void showInstalledPackages(List<PackageInfo>  packageInfoList)
    {
      solveLauncherIntents(); //解析启动意图，并填充数据。
    } //void showInstalledPackages(List<PackageInfo>  packageInfoList)

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

    /**
     * 答案可用。
     * @param event 事件对象。
     */
    public void answerAvailable(AnswerAvailableEvent event)
    {
        String answerValue=event.getAnswerValue(); //获取答案值。

        LauncherIconType launcherIconType=event.getIconType(); //获取图标类型．

        if (launcherIconType==LauncherIconType.ActivityIconType) //活动．
        {
            rememberVoicePackageNameRelationship(event); //记录语音识别结果与应用包名之间的关系。
        } //if (launcherIconType==LauncherIconType.ActivityIconType) //活动．
        else //快捷方式．
        {
            rememberVoiceShortcutIdRelationship(event); //记录语音识别结果与快捷方式编号之间的映射关系．
        } //else //快捷方式．
    } //@Subscribe public void answerAvailable(AnswerAvailableEvent event)

    /**
     * 记录语音识别结果与快捷方式编号之间的映射关系．
     * @param event 包含有快捷方式编号的消息对象．
     */
    private void rememberVoiceShortcutIdRelationship(AnswerAvailableEvent event)
    {
        if (voiceRecognizeResultString!=null) //有识别结果。
        {
            HxShortcutInfo currentPackageItemInfo=new HxShortcutInfo(); //当前的包条目信息对象。

            currentPackageItemInfo.packageName=event.getAnswerValue(); //获取包名。
            currentPackageItemInfo.shortcutId=event.getShortcutId(); //记录快捷方式编号。

            voiceShortcutIdMap.put(voiceRecognizeResultString, currentPackageItemInfo); //加入映射中。

            saveVoiceShortcutIdMap(); //保存映射。

            assesSendVoiceShortcutAssociationData(); //考虑，是否要发送语音指令关联快捷方式数据。
        } //if (voiceRecognizeResultString!=null) //有识别结果。
    } //private void rememberVoiceShortcutIdRelationship(AnswerAvailableEvent event)

    /**
     * 记录语音识别结果与应用包名之间的关系。
     * @param currentRelationship 包含应用包名信息的事件对象。
     */
    private void rememberVoicePackageNameRelationship(AnswerAvailableEvent currentRelationship)
    {
      if ((voiceRecognizeResultString!=null) && (!voiceRecognizeResultString.isEmpty())) //有识别结果。
      {
          PackageItemInfo currentPackageItemInfo=new PackageItemInfo(); //当前的包条目信息对象。

          currentPackageItemInfo.packageName=currentRelationship.getAnswerValue(); //获取包名。
          currentPackageItemInfo.name=currentRelationship.getActivityName(); //记录活动名字。

          if (voicePackageNameMap!=null)
          {
              voicePackageNameMap.put(voiceRecognizeResultString, currentPackageItemInfo); //加入映射中。

              saveVoicePackageNameMap(); //保存映射。

              assesSendVoiceAssociationData(); //考虑，是否要发送语音指令关联应用数据。
          }
      } //if (voiceRecognizeResultString!=null) //有识别结果。
    } //private void rememberVoicePackageNameRelationship(String answerValue)
}