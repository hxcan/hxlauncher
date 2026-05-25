package com.stupidbeauty.hxlauncher.manager;

import com.stupidbeauty.hxlauncher.LauncherActivity;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.bumptech.glide.Glide;
import com.stupidbeauty.codeposition.CodePosition;
import android.media.MediaScannerConnection;
import com.bumptech.glide.Glide;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Handler;
import com.stupidbeauty.hxlauncher.bean.HxShortcutInfo;
import com.stupidbeauty.hxlauncher.callback.LauncherAppsCallback;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType;
import com.stupidbeauty.hxlauncher.logic.ShutDownAt2100Logic;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.protobuf.InvalidProtocolBufferException;
import com.stupidbeauty.hxlauncher.activity.ApplicationUnlockActivity;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import android.os.Debug;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoicePackageNameMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinShortcutsTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadPreferenceTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadShortcutsTask;
import com.stupidbeauty.hxlauncher.asynctask.ReqGameDataTask;
import com.stupidbeauty.hxlauncher.asynctask.BindAdapterTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoiceShortcutMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadVoiceShortcutIdMapTask;
// import java.util.Timer;
import java.util.TimerTask;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
// import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.PowerManager;
import android.os.Vibrator;
import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.andexert.library.RippleView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
// import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.rpc.VoiceCommandHitDataReporter;
import com.stupidbeauty.comgooglewidevinesoftwaredrmremover.app.LanImeUncaughtExceptionHandler;
import com.stupidbeauty.grebe.DownloadRequestor;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import com.stupidbeauty.hxlauncher.callback.LauncherAppsCallback;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType;
import com.stupidbeauty.hxlauncher.manager.VoicePackageMapRegretManager;
import com.stupidbeauty.hxlauncher.manager.ApkDownloadManager;
// import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.iflytek.cloud.SpeechRecognizer;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC;
// import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
import static com.stupidbeauty.hxlauncher.Constants.Actions.LegacyInstallShortcut;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.InputtingForPackage;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.PackageNameOfInputting;
import static com.stupidbeauty.hxlauncher.Constants.Operation.ToggleBuiltinShortcuts;
import static com.stupidbeauty.hxlauncher.HxLauncherIconType.PbShortcutIconType;
import static com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType.LocalVoiceCommandMap;
import com.stupidbeauty.hxlauncher.service.NotificationListernService02;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class ApkDownloadManager
{


  private VoiceCommandHitDataReporter voiceCommandHitDataReporter=new VoiceCommandHitDataReporter(); //!< The voice command hit data reporter.
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈
  private boolean mscIsInitialized=false; //!<讯飞语音识别是否已经初始化。
  private LauncherActivity launcherActivity = null; //!< The launcher activity.
  private HashMap<String, ShortcutInfo> shortcutTitleInfoMap; //!<快捷方式的标题与快捷方式对象本身的映射。
  private HashMap<String, ShortcutInfo> shortcutIdInfoMap; //!<快捷方式的编号与快捷方式对象本身的映射
  private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
  private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。
  PowerManager.WakeLock wakeLock=null; //!<游戏辅助唤醒锁。
  private boolean activityHasBeenResumed=false; //!<活动是否处于被继续的状态，即正常的运行状态。
    private List<ShortcutInfo> shortcutInfos=null; //!< 快捷方式列表。

    private boolean builtinShortcutsVisible= true; //!< 内置 快捷方式是否可见。
    private boolean sentVoiceShortcutAssociationData=false; //!<是否已经成功发送语音指令关联快捷方式的数据。
    private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; //!<录音权限。
    private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限
    private static final String PERMISSION_INSTALL_PACKAGE = Manifest.permission.REQUEST_INSTALL_PACKAGES; //!< 安装应用程序权限
    private final DownloadRequestor downloadRequestor = new DownloadRequestor();

    private boolean foundActivity=false; //!<是否命中了活动。
    private SetValuedMap<String, PackageItemInfo> voicePackageNameMap=null; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的

    private MultiMap<String, PackageItemInfo> activityLabelPackageItemInfoMap=new MultiValueMap<>(); //!<活动的标签，与活动本身信息之间的映射。
    private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。
    int ret = 0;
    private SpeechRecognizer mIat; //!<语言识别器。
    private int recognizeCounter=0; //!<识别计数器．


    private boolean voiceEndDetected=false; //!<是否已经探测到用户声音结束。
    private final int MSG_LOAD_MORE = 2;
    private int mCurrMsg = -1;
    private RequestQueue mQueue; //!<Volley请求队列。

    
    private final String categoryName="default"; //!<要显示的分类的名字。
    
    public ApkDownloadManager(LauncherActivity launcherActivity)
    {
      this.launcherActivity = launcherActivity;
    } // public ApkDownloadManager(LauncherActivity launcherActivity)
    
    /**
     * 保存应用的启动计数数据。
     */
    private void saveApplicationLaunchCount()
    {
    } //private void saveApplicationLaunchCount()

    /**
     * 尝试根据语音识别结果，下载对应包名的应用。
     */
    public boolean tryDownloadApkAssociatedToPackage(String voiceRecognizeResultString, HashMap<String, HxShortcutInfo> voiceShortcutIdMap, SetValuedMap<String, PackageItemInfo> voicePackageNameMapBultin)
    {
      boolean foundUrl=false;
      VoiceIntentManager voiceIntentManager = new VoiceIntentManager(); // Create the voice intent manager object.
      String foundPackageName = voiceIntentManager.findVoiceTargetMapPackageName(voicePackageNameMap, voiceRecognizeResultString); //从映射中寻找目标应用，并启动。从用户自己积累的映射中寻找。
        
      //从内置映射中寻找。
      if (foundPackageName.isEmpty()) //未命中了。
      {
        foundPackageName= voiceIntentManager.findVoiceTargetMapPackageName(voicePackageNameMapBultin, voiceRecognizeResultString); //从映射中寻找目标应用，并启动。从内置映射中寻找。
      } //if (foundActivity) //命中了。

      //从快捷方式映射中寻找，通过点击动作关联起来的映射：
      if (foundPackageName.isEmpty()) //未命中了。
      {
        // VoiceIntentManager voiceIntentManager = new VoiceIntentManager(); // Create the voice intent manager object.
        foundPackageName = voiceIntentManager.findVoiceTargetMapShortcutPackageName(voiceShortcutIdMap, voiceRecognizeResultString); // 从映射中寻找目标快捷方式，并启动。从用户自己积累的映射中寻找。
      } //if (foundActivity) //命中了。

      if (foundPackageName.isEmpty()) //未命中。
      {
      } //if (foundPackageName.isEmpty()) //未命中。
      else //命中了。
      {
        foundUrl = launcherActivity.requestDownloadApk(foundPackageName); // Request download apk.
      } //else //命中了。

      return foundUrl;
    } //private void tryDownloadApkAssociatedToPackage()
    

    /**
     * 尝试根据语音识别结果，下载对应的应用。
     */
    public void tryDownloadApkAssociatedToVoiceCommand(String voiceRecognizeResultString)
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      Map<String,String> internationalizationData=application.getVoicePackageUrlMap(); //获取国际化数据对象。

      if (internationalizationData!=null) //数据存在。
      {
        String internationalizationName=internationalizationData.get(voiceRecognizeResultString); //获取国际化名字。

        if (internationalizationName!=null) //有国际化名字。
        {
          launcherActivity.requestDownloadPackage(internationalizationName, voiceRecognizeResultString, null); //下载应用程序安装包。
        } //if (internationalizationName!=null) //有国际化名字。
      } //if (internationalizationData!=null) //数据存在。
    } //private void tryDownloadApkAssociatedToVoiceCommand()
}
