package com.stupidbeauty.hxlauncher.activity;

import com.stupidbeauty.hxlauncher.Constants;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.stupidbeauty.hxlauncher.InstalledPackageLoadTaskInterface;
import com.stupidbeauty.hxlauncher.R;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.g00fy2.versioncompare.Version;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.stupidbeauty.grebe.DownloadRequestor;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.Vibrator;
import android.provider.Settings;
import com.stupidbeauty.hxlauncher.ApplicationInformationAdapter;
import com.stupidbeauty.hxlauncher.BuiltinShortcutsManager;
import com.stupidbeauty.hxlauncher.Constants;
import com.stupidbeauty.hxlauncher.InstalledPackageLoadTaskInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import butterknife.BindView;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.stupidbeauty.msclearnfootball.AnswerAvailableEvent;
import com.stupidbeauty.msclearnfootball.VoiceRecognizeResult;
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
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.stupidbeauty.msclearnfootball.AnswerAvailableEvent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.asynctask.VoiceShortcutAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_PACKAGE_NAME;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import com.stupidbeauty.voiceui.VoiceUi;

public class ApplicationUnlockActivity extends Activity 
{
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  
  // View name of the header image. Used for activity scene transitions
  public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

  // View name of the header title. Used for activity scene transitions
  public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

  private     AnimationDrawable rocketAnimation; //!<录音按钮变暗

  private Stack<VoiceCommandHitDataObject> voiceCommandHitDataStack=new Stack<>(); //!<语音命中数据记录栈

  private BuiltinShortcutsManager builtinShortcutsManager=new BuiltinShortcutsManager(); //!<内置快捷方式管理器

  @BindView(R.id.hitApplicationIcon) ImageView hitApplicationIcon; //!<命中的应用的图标。
  @BindView(R.id.microphoneIcon) ImageView microphoneIcon; //!< 升级按钮图标。
  @BindView(R.id.welcome_logon_pwd) EditText welcome_logon_pwd; //!< 密码输入框。
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
  private HashMap<String, HxShortcutInfo> voiceShortcutIdMap=new HashMap<>(); //!<语音识别结果与快捷方式编号之间的映射关系．

  @BindView(R.id.wallpaper) ImageView wallpaper; //!<墙纸视图。

  @BindView(R.id.progressBar) ProgressBar progressBar; //!<进度条。

  private String voiceRecognizeResultString; //!<语音识别结果。

  int ret = 0;

  @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!< 应用图标图片。陈欣。

  @BindView(R.id.statustextView) TextView statustextView; //!<用来显示状态的文字标签。

  private int recognizeCounter=0; //!<识别计数器．

  private Vibrator vibrator;

  private final int MSG_LOAD_MORE = 2;

  private ImageView mHeaderImageView;
  private TextView mHeaderTitle;

  private String packagename; //!< 包名。陈欣。
  private String activityName; //!< 活动名字。

  private int mCurrMsg = -1;

  private RequestQueue mQueue; //!<Volley请求队列。

  @OnClick(R.id.loveAnimation)
  public void unlockAndLaunch()
  {
    String password=welcome_logon_pwd.getText().toString(); // 获取输入的内容。
    
    if (password.equals(Constants.Literal.PASSWORD_CX)) // 密码正确。
    {
      Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
      mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

      Intent activitylaunchIntent=new Intent(mainIntent);
      activitylaunchIntent.setClassName(packagename, activityName); //设置类名。

      try //尝试启动活动，并且捕获可能的异常。
      {
        startActivity(activitylaunchIntent); //启动活动。
      } //try //尝试启动活动，并且捕获可能的异常。
      catch (ActivityNotFoundException exception)
      {
        exception.printStackTrace(); //报告错误。
      } //catch (ActivityNotFoundException exception)
      catch (SecurityException exception) //安全异常。
      {
        exception.printStackTrace(); //报告错误。
      } //catch (SecurityException exception) //安全异常。
    } // if (password.equals(PASSWORD_CX)) // 密码正确。
  }

  private ApplicationInformationAdapter mAdapter; //!<适配器。

  private static final String TAG="ApplicationInformationB"; //!<输出调试信息时使用的标记。
  private final String categoryName="default"; //!<要显示的分类的名字。

    @Override
    protected void onNewIntent(Intent intent)
    {
      super.onNewIntent(intent);

      processIntent(intent); // 处理意图。陈欣。
    }

    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);

      getWindow().setEnterTransition(null);

      setContentView(R.layout.application_unlock_activity); // 设置视图

      ButterKnife.bind(this); //视图注入。
      
      voiceUi=new VoiceUi(this); // 创建语音交互对象。

      mHeaderImageView = findViewById(R.id.applicationIconrightimageView2);
      mHeaderTitle = findViewById(R.id.rightTextoperationMethodactTitletextView2);

      Intent intent=getIntent(); // 获取意图。

      processIntent(intent);
    } //protected void onCreate(Bundle savedInstanceState)

    private void processIntent(Intent intent) 
    {
      String title=intent.getStringExtra(EXTRA_PACKAGE_NAME);

      activityName = intent.getStringExtra(EXTRA_COMPONENT_NAME); // 获取活动名字。陈欣。

      packagename=title; // 记录包名。陈欣。

      mHeaderTitle.setText(title);

      HxLauncherApplication hxlauncherApplication=HxLauncherApplication.getInstance(); // 获取应用对象。
      String currentVersionName=hxlauncherApplication.getVersionName(packagename); // 获取版本名字。

      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      HashMap<String, Drawable> launchIconMap=application.getLaunchIconMap(); //获取启动图标缓存。

      Drawable result=launchIconMap.get(packagename + "/" + activityName); //获取缓存绘图对象。

      Glide.with(application).load("").placeholder(result).into(applicationIconrightimageView2); //显示图标。
      
      welcome_logon_pwd.setText(""); // 清空密码。
      
      String mWordSeparators = getResources().getString(R.string.needUnlock); // 读取 说明 字符串。
      
      voiceUi.say(mWordSeparators); // 说话，需要解锁。
    }

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

}
