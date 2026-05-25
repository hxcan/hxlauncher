package com.stupidbeauty.hxlauncher.manager;

import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.bumptech.glide.Glide;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import android.media.MediaScannerConnection;
import com.stupidbeauty.hxlauncher.Constants;
import com.bumptech.glide.Glide;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Handler;
import com.stupidbeauty.hxlauncher.interfaces.ShutDownAt2100LogicInterface;
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
import android.widget.TextView;
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
import com.stupidbeauty.hxlauncher.rpc.VoiceCommandHitDataReporter;
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

public class NotificationControlManager
{
  private VoiceCommandHitDataReporter voiceCommandHitDataReporter=new VoiceCommandHitDataReporter(); //!< The voice command hit data reporter.
  private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private BuiltinFtpServerErrorListener builtinFtpServerErrorListener=null; //!< the builtin ftp server error listener.  Chen xin.
  private BuiltinFtpServer builtinFtpServer=null; //!< The builtin ftp server.
  private ActiveUserReportManager activeUserReportManager=null; //!< 活跃用户统计管理器。陈欣。
  private VoicePackageMapRegretManager voicePackageMapRegretManager = new VoicePackageMapRegretManager(); //!< Voice command package map regret manager.
  private AnimationDrawable rocketAnimation; //!<录音按钮变暗
  private CloudRequestorZzaqwb cloudRequestorZzaqwb=new CloudRequestorZzaqwb(); //!<云端请求发送器
  private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈
  private ShutDownAt2100Logic shutDownAt2100Logic= null; //!< Logic with shutdownat2100.
  private boolean mscIsInitialized=false; //!<讯飞语音识别是否已经初始化。
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
    private static final String PERMISSION_INSTALL_PACKAGE = Manifest.permission.REQUEST_INSTALL_PACKAGES; //!< 安装应用程序权限

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

    private Vibrator vibrator;

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
            
      this.voiceShortcutIdMap.putAll(voiceShortcutIdMapBuiltin); // 合并。
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
    
      Log.d(TAG, "setInternationalizationDataPackageNameMap, map: " + this.internationalizationDataPackageNameMap); // Debug.
    }
    
    /**
    * Choose a port
    */
    private int chooseRandomPort() 
    {
      int randomIndex = 1239; // Choose a port.

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

    private static final String TAG="LauncherActivity"; //!< 输出调试信息时使用的标记。
    private final String categoryName="default"; //!<要显示的分类的名字。
    
    /**
     * 构造映射，快捷方式的标题与快捷方式对象之间的映射。
     * @param shortcutInfos 快捷方式列表。
     */
    public void buildShortcutTitleInfoMap(List<ShortcutInfo> shortcutInfos)
    {
      if (shortcutInfos != null) // The list exists
      {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住的快捷方式。
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
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住的快捷方式。
      } // if (shortcutInfos!=null) // The list exists
    } //private void buildShortcutTitleInfoMap(List<ShortcutInfo> shortcutInfos)

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
     * 保存映射。
     */
    private void saveVoicePackageNameMap()
    {
      VoicePackageNameMapSaveTask translateRequestSendTask =new VoicePackageNameMapSaveTask(); //创建异步任务。

      translateRequestSendTask.execute(voicePackageNameMap); //执行任务。
    } //private void saveVoicePackageNameMap()

    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    private  File findRandomPhotoFileDcim()
    {
      File result=null;

        String photoDirectoryPath= com.stupidbeauty.hxlauncher.Constants.DirPath.DCIM_SD_CARD_PATH; //照片目录路径。

        File photoDirecotry= new File(photoDirectoryPath); //照片目录。

        Log.d(TAG,"findRandomPhotoFile,photo directory:"+photoDirectoryPath); //Debug.

        IOFileFilter fileFilter= TrueFileFilter.INSTANCE; //文件过滤器。

        IOFileFilter dirFilter= TrueFileFilter.INSTANCE; //文件过滤器。

        try //尝试列出文件，并且捕获可能的异常。
        {

            Collection<File> photoFiles= FileUtils.listFiles(photoDirecotry, fileFilter, dirFilter); //列出全部文件。

            Random random=new Random(); //随机数生成器。

//            LogHelper.d(TAG,"findRandomPhotoFile,photo amount:"+photoFiles.size()); //Debug.

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



    /**
     * 初始化本地日志工具。
     */
    private void initLocalLogUtil()
    {
      Thread.setDefaultUncaughtExceptionHandler(new LanImeUncaughtExceptionHandler()); // 设置未捕获的异常处理器。
    } //private void initLocalLogUtil()

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
     * 考虑是否要重新启动语音识别过程。
     */
    private void assesRestartSpeechRecognize()
    {
    } //private void assesRestartSpeechRecognize()

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

        // 设置返回结果为json格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置云端识别使用的语法id
        mIat.setParameter(SpeechConstant.DOMAIN,"iat");

        setLanguageAndAccentParameters(); //设置语言及区域参数字符串。

        String vadEos=mIat.getParameter(SpeechConstant.VAD_EOS); //获取默认的后端点时间。

        Log.d(TAG,"setParam, default vad eos: "+vadEos); //Debug.

        mIat.setParameter(SpeechConstant.VAD_EOS, "100"); //后端点时间长度。

        mIat.setParameter(SpeechConstant.ASR_PTT, "0"); //不要标点符号。https://www.xfyun.cn/doc/asr/voicedictation/Android-SDK.html#_2%E3%80%81sdk%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97

        result = true;

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
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

            Log.d(TAG, "setLanguageAndAccentParameters, candidate language: " + androidLocaleName+ ", locale counter: " + localeCounter); //Debug.

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

    /**
     * 保存应用的启动计数数据。
     */
    private void saveApplicationLaunchCount()
    {
    } //private void saveApplicationLaunchCount()

    /**
     * 获取活动条目的位置。
     * @param packageItemInfopackageName 包名。
     * @param packageItemInfoname 活动类名。
     * @return 图标的位置编号。
     */
    public int getItemPosition(String packageItemInfopackageName, String packageItemInfoname)
    {
        Log.d(TAG, "getItemPosition, packageNameItemNamePositionMap: " + packageNameItemNamePositionMap); //Debug.
        int result=0; //结果。

        if (packageItemInfoname!=null) //有类名。
        {
            Integer resultInteger=packageNameItemNamePositionMap.get(packageItemInfopackageName+"/"+packageItemInfoname); //从映射中查找对应的数字。

            if (resultInteger!=null) //不是空指针。
            {
                result=resultInteger; //从映射中查找。
            } //if (resultInteger!=null) //不是空指针。
        } //if (packageItemInfoname!=null) //有类名。
        else //没有类名。
        {
            Log.d(TAG, "getItemPosition, packageNamePositionMap: " + packageNamePositionMap + ", package name: " + packageItemInfopackageName); //Debug.

            Integer resultInteger1=packageNamePositionMap.get(packageItemInfopackageName); //从映射中查找。

            if (resultInteger1!=null) //不是空指针。
            {
                result=resultInteger1; //解包。
            } //if (resultInteger1!=null) //不是空指针。
        } //else //没有类名。

        return result;
    } //public int getItemPosition(String packageItemInfopackageName, String packageItemInfoname)

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
    * Stop the download notification.
    */
    public void stopDownloadNotification()
    {
      HxLauncherApplication application = HxLauncherApplication.getInstance(); //获取应用程序对象。
      Intent serviceIntent = new Intent(application, DownloadNotificationService.class); //创建意图。
      
      application.stopService(serviceIntent); // Stop the service.
    } // private void stopDownloadNotification()
    
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
     * 从映射中寻找目标应用的包名。从用户自己积累的映射中寻找。
     * @param voicePackageNameMap 映射对象
     * @return 找到的包名
     */
    private String findVoiceTargetMapPackageName(SetValuedMap<String, PackageItemInfo> voicePackageNameMap)
    {
      String result=""; //结果

      Log.d(TAG, "findVoiceTargetMapPackageName, result: " + voiceRecognizeResultString );

      if (voicePackageNameMap!=null) // The map exists
      {
        if (voicePackageNameMap.containsKey(voiceRecognizeResultString)) //有对应的映射关系。用户自己积累的语音指令与包条目映射。
        {
          Collection<PackageItemInfo> packageItemInfos=(Collection<PackageItemInfo>) voicePackageNameMap.get(voiceRecognizeResultString); //获取集合信息

          Log.d(TAG, "findVoiceTargetMapPackageName, result: " + voiceRecognizeResultString + ",  in map, package item info length: " + packageItemInfos.size() );

          for(PackageItemInfo packageItemInfo: packageItemInfos) //一个个地输出
          {
            String packageName=packageItemInfo.packageName; //获取包名。

            Log.d(TAG, "findVoiceTargetMapPackageName, result: " + voiceRecognizeResultString + ",  iterate, package name: " + packageName );

            if (packageName.isEmpty()) //空白，跳过
            {
            } //if (packageName.isEmpty()) //空白，跳过
            else //不是空白
            {
              result=packageName; //命中了。

              break; //跳出，不用再找
            } //else //不是空白
          } //for(PackageItemInfo packageItemInfo: packageItemInfos) //一个个地输出
        } //if (voicePackageNameMap.contains(voiceRecognizeResultString)) //有对应的映射关系。
        else
        {
          Log.d(TAG, "findVoiceTargetMapPackageName, result: " + voiceRecognizeResultString + ", not in map" );
        }
      } // if (voicePackageNameMap!=null) // The map exists
      
      return result;
    } //private String findVoiceTargetMapPackageName(HashMap<String, PackageItemInfo> voicePackageNameMap)

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

      Log.d(TAG,"checkAndAquireWakeLock, package name: "+packageName); //Debug.

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
