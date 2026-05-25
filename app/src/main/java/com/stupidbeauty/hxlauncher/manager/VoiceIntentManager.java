package com.stupidbeauty.hxlauncher.manager;

import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import com.andexert.library.RippleView;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.asynctask.VoiceShortcutAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageItemInfo;
import android.content.pm.ResolveInfo;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import android.media.MediaScannerConnection;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import android.net.Uri;
import com.stupidbeauty.hxlauncher.manager.ActiveUserReportManager;
import android.os.Handler;
import com.stupidbeauty.hxlauncher.logic.ShutDownAt2100Logic;
import java.io.File;
import java.util.List;
import com.stupidbeauty.hxlauncher.manager.PackageInformationManager;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoicePackageNameMapTask;
import com.stupidbeauty.hxlauncher.asynctask.ReqGameDataTask;
import com.stupidbeauty.hxlauncher.asynctask.BindAdapterTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoiceShortcutMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadServerVoiceCommandReponseIgnoreTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadVoicePackageNameMapTask;
import java.util.TimerTask;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
// import android.os.Environment;
import android.os.LocaleList;
import android.os.Vibrator;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.andexert.library.RippleView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.stupidbeauty.hxlauncher.asynctask.BuildInternationalizationDataPackageNameMapTask;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.adapter.FlipAnimationAdapter;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import com.google.protobuf.ByteString;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.stupidbeauty.hxlauncher.service.DownloadNotificationService; 
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.rpc.VoiceCommandHitDataReporter;
import com.stupidbeauty.comgooglewidevinesoftwaredrmremover.app.LanImeUncaughtExceptionHandler;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.HxShortcutInfo;
// import com.stupidbeauty.hxlauncher.callback.LauncherAppsCallback;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType;
import com.stupidbeauty.hxlauncher.manager.VoicePackageMapRegretManager;
import com.stupidbeauty.hxlauncher.manager.ApkDownloadManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.iflytek.cloud.SpeechRecognizer;
import org.apache.commons.io.filefilter.IOFileFilter;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import static android.content.Intent.ACTION_PACKAGE_REPLACED;
import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_PACKAGE_NAME;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
import static com.stupidbeauty.hxlauncher.Constants.Actions.LegacyInstallShortcut;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.PackageNameOfInputting;
// import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ActivityIconType;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ShortcutIconType;
import static com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType.LocalVoiceCommandMap;
import com.stupidbeauty.hxlauncher.service.NotificationListernService02;
import android.os.Process;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class VoiceIntentManager
{
  private VoiceCommandHitDataReporter voiceCommandHitDataReporter=new VoiceCommandHitDataReporter(); //!< The voice command hit data reporter.
  private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private AnimationDrawable rocketAnimation; //!<录音按钮变暗
  private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
  private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。
  private List<ShortcutInfo> shortcutInfos=null; //!< 快捷方式列表。
  private boolean builtinShortcutsVisible= true; //!< 内置 快捷方式是否可见。
  private boolean sentVoiceShortcutAssociationData=false; //!<是否已经成功发送语音指令关联快捷方式的数据。
  private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识
  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; //!<录音权限。
  private static final String PERMISSION_INSTALL_PACKAGE = Manifest.permission.REQUEST_INSTALL_PACKAGES; //!< 安装应用程序权限
  private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。
  private SpeechRecognizer mIat; //!<语言识别器。
  // private int mPageNumber = 1;//{1, 1, 1};
  private static final String TAG="VoiceIntentManager"; //!< 输出调试信息时使用的标记。

    /**
     * 从映射中寻找目标应用的包名。从用户自己积累的映射中寻找。
     * @param voicePackageNameMap 映射对象
     * @return 找到的包名
     */
    public String findVoiceTargetMapPackageName(SetValuedMap<String, PackageItemInfo> voicePackageNameMap, String voiceRecognizeResultString)
    {
      String result=""; //结果

      Log.d(TAG, "findVoiceTargetMapPackageName, result: " + voiceRecognizeResultString );

      if (voicePackageNameMap!=null) // The map exists
      {
        if (voicePackageNameMap.containsKey(voiceRecognizeResultString)) //有对应的映射关系。用户自己积累的语音指令与包条目映射。
        {
          Collection<PackageItemInfo> packageItemInfos=(Collection<PackageItemInfo>) voicePackageNameMap.get(voiceRecognizeResultString); //获取集合信息

          for(PackageItemInfo packageItemInfo: packageItemInfos) //一个个地输出
          {
            String packageName=packageItemInfo.packageName; //获取包名。

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
      } // if (voicePackageNameMap!=null) // The map exists
      
      return result;
    } //private String findVoiceTargetMapPackageName(HashMap<String, PackageItemInfo> voicePackageNameMap)


  /**
    * 寻找数据文件。要忽略的服务器回复映射
    * @return 寻找到的数据文件
    */
  public File findServerVoiceCommandReponseIgnoreMapFile()
  {
    File result=null;

    HxLauncherApplication baseApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
    File filesDir = baseApplication.getFilesDir();

    if (filesDir==null) //该目录不存在。
    {
    } //if (filesDir==null) //该目录不存在。
    else //该目录存在。
    {
      result=new File(filesDir.getAbsolutePath()+"/serverVoiceCommandIgnoreMap.cx"); //指定文件名。

      if (result.exists()) //文件存在。
      {
      } //if (result.exists()) //文件存在。
      else //文件不存在。
      {
        try
        {
          result.createNewFile(); //创建文件。
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      } //else //文件不存在。
    } //else //该目录存在。

    return result;
  } //private File findServerVoiceCommandReponseIgnoreMapFile()

  /**
    * 寻找数据记录文件。
    * @return 数据记录文件．语音识别结果与快捷方式之间的映射文件．
    */
  public File findVoiceShortcutIdMapFile()
  {
    File result=null;

    HxLauncherApplication baseApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
    File filesDir = baseApplication.getFilesDir();

    if (filesDir==null) //该目录不存在。
    {
    } //if (filesDir==null) //该目录不存在。
    else //该目录存在。
    {
      result=new File(filesDir.getAbsolutePath()+"/voiceShortcutIdMap.etn"); //指定文件名。

      if (result.exists()) //文件存在。
      {
      } //if (result.exists()) //文件存在。
      else //文件不存在。
      {
        try
        {
          result.createNewFile(); //创建文件。
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      } //else //文件不存在。
    } //else //该目录存在。

    return result;
  } //private File findVoiceShortcutIdMapFile()

  /**
  * 从映射中寻找目标快捷方式的包名。从用户自己积累的映射中寻找。
  * @param voiceShortcutIdMap 映射对象
  * @return 找到的包名
  */
  public String findVoiceTargetMapShortcutPackageName(HashMap<String, HxShortcutInfo> voiceShortcutIdMap, String voiceRecognizeResultString)
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
}
