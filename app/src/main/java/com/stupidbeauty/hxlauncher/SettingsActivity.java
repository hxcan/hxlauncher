package com.stupidbeauty.hxlauncher;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.app.StatusBarManager;
import com.stupidbeauty.hxlauncher.callback.AddQuickSettingsResultCallback;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.drawable.Icon;
import android.content.ComponentName;
import android.content.Context;
import com.stupidbeauty.codeposition.CodePosition;
import android.os.ParcelFileDescriptor;
import java.io.FileOutputStream;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;
import android.provider.Settings;
import android.content.Intent;
import android.os.Environment;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.bumptech.glide.Glide;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import android.content.pm.ResolveInfo;
import com.bumptech.glide.Glide;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import android.widget.Toast;
import com.stupidbeauty.hxlauncher.asynctask.BuildInternationalizationDataPackageNameMapTask;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.adapter.FlipAnimationAdapter;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import android.provider.Settings;
import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.CheckBox;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.activity.AccountActivity;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.service.ImgBurstTileService;

public class SettingsActivity extends Activity
{
  @BindView(R.id.builtinShortcutscheckBox) CheckBox builtinShortcutscheckBox; //!<内置快捷方式是否可见。
  private static String OptimizeRepairGooglePlayUrl="https://play.google.com/store/apps/details?id=com.stupidbeauty.hxlauncher"; //!<灵桌面应用程序的google play地址。
  private static final String TAG="SettingsActivity"; //!<输出调试信息时使用的标记。

  @Override
  /**
    * 活动被创建。
    */
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.settings_activity); //设置界面内容。

    ButterKnife.bind(this); //视图注入。

    loadPreference(); //载入选项。
  } //protected void onCreate(Bundle savedInstanceState)

  /**
    * 载入选项。
    */
  private void loadPreference()
  {
    boolean builtinShortcutsVisible= PreferenceManagerUtil.isBuiltinShortcutsVisible(); //内置快捷方式是否可见。

    builtinShortcutscheckBox.setChecked(builtinShortcutsVisible); //切换是否选中。

    boolean useHiveLayout=PreferenceManagerUtil.isHiveLayout(); //是否要使用蜂窝布局
  } //private void loadPreference()

  @OnClick(R.id.ratelanime_button1)
  /**
    * 在GooglePlay上评分。
    */
  public void rateApplicationOnGooglePlay() 
  {
    openURL(OptimizeRepairGooglePlayUrl); //打开网址。
    } //public void rateApplicationOnGooglePlay()

    @OnClick(R.id.notificationBadge_button1)
    /**
     * Goto activity of granting notification badge permission.
     */
    public void notificationBadge_button1ay() 
    {
      // Chen xin.
      // openURL(OptimizeRepairGooglePlayUrl); //打开网址。
      
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      Log.d(TAG, "gotoFileManagerSettingsPage"); //Debug.

      Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);  // notification listener.

      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      String packageNmae=getPackageName();
      Log.d(TAG, "gotoFileManagerSettingsPage, package name: " + packageNmae); //Debug.

      String url = "package:"+packageNmae;

      Log.d(TAG, "gotoFileManagerSettingsPage, url: " + url); //Debug.

      Uri packageUri =  Uri.parse(url); // Parse the uri.
      Log.d(TAG, CodePosition.newInstance().toString() + ", uri: " + packageUri); // Debug.

      // intent.setData(packageUri);

      startActivity(intent);
  } //public void rateApplicationOnGooglePlay()
    
  @OnClick(R.id.addQuickSettingser_button1)
  /**
    * Add the quick settings button.
    */
  public void addQuickSettingser_button1ay() 
  {
    StatusBarManager statusBarManager = (StatusBarManager) (getSystemService(Context.STATUS_BAR_SERVICE)); // Get the status bar manager.
    
    AddQuickSettingsResultCallback resultCallback = new AddQuickSettingsResultCallback(); // Create the callback object.
    
    ExecutorService executior = Executors.newFixedThreadPool(1); // Create the executor service.
    
    Icon icon = Icon.createWithResource(this, R.drawable.ic_launcher); // Creathe the icon object.
    
    CharSequence label = getText(R.string.app_name);
    
    String activityName = "com.stupidbeauty.hxlauncher.service.ImgBurstTileService"; // The activity name.
    String packageName = getPackageName(); // Get the package name.
    
    ComponentName componentName = new ComponentName(this, ImgBurstTileService.class); //构造组件名字对象。

    statusBarManager.requestAddTileService(componentName, label, icon, executior, resultCallback);
  } //public void rateApplicationOnGooglePlay()

  @OnClick(R.id.setDefaultLauncher_button1)
  /**
    * Set this application as the default launcher.
    */
  public void setAsDefaultLauncheray() 
  {
    // openURL(OptimizeRepairGooglePlayUrl); //打开网址。
    checkDefaultLauncher(); // 检查自己是不是默认启动器，并处理。
  } //public void rateApplicationOnGooglePlay()

    /**
     * 检查自己是不是默认启动器。
     * @return 自己是不是默认启动器。
     */
    private boolean isMyAppLauncherDefault()
    {
        boolean result=false; //结果。

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        String myPackageName=getPackageName(); //获取包名。

        if (currentHomePackage.equals(myPackageName)) //是的。
        {
            result=true;
        } //if (currentHomePackage.equals(myPackageName)) //是的。

        return result;
    } //private boolean isMyAppLauncherDefault()



    /**
     * 检查自己是不是默认启动器，并处理。
     */
    private void checkDefaultLauncher()
    {
      if (isMyAppLauncherDefault()) //是默认启动器。
      {
      } //if (isMyAppLauncherDefault()) //是默认启动器。
      else //不是默认启动器。
      {
          //显示吐息，请设置默认启动器：
          Toast.makeText(HxLauncherApplication.getAppContext(), R.string.pleaseSetHxLauncherAsTheDefaultLauncher, Toast.LENGTH_LONG).show();   //做一个提示，已经登录成功.


          //弹出设置界面，选择默认启动器：
          try
          {
              startActivity(new Intent(Settings.ACTION_HOME_SETTINGS)); //弹出界面。
          }
          catch (ActivityNotFoundException e)
          {
              e.printStackTrace();
          }
      } //else //不是默认启动器。
    } //private void checkDefaultLauncher()

    private void openURL(String url)
    {
      // Strangely, some Android browsers don't seem to register to handle HTTP:// or HTTPS://.
      // Lower-case these as it should always be OK to lower-case these schemes.
      if (url.startsWith("HTTP://")) 
      {
        url = "http" + url.substring(4);
      }
      else if (url.startsWith("HTTPS://")) 
      {
        url = "https" + url.substring(5);
      }
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      try 
      {
        launchIntent(intent);
      }
      catch (ActivityNotFoundException ignored) 
      {
        Log.w(TAG, "Nothing available to handle " + intent);
      }
    }

    /**
     * 切换是否要使用蜂窝布局
     * @param isChecked 是否被选中
     */
    @OnCheckedChanged(R.id.hiveLayoutinShortcutscheckBox)
    public void toggleUseHiveLayout(boolean isChecked)
    {
      PreferenceManagerUtil.setUseHiveLayout(isChecked); //保存选项。

      Intent intent4Wrk = new Intent(); //创建意图对象。晚安诸位。
      Bundle extras=new Bundle(); //创建数据包。
      extras.putBoolean("useHiveLayout", isChecked); //加入选项，是否显示内置快捷方式。

      intent4Wrk.putExtras(extras); //设置附加数据。

      intent4Wrk.setAction(Constants.Operation.ToggleHiveLayout);//切换是否显示内置快捷方式。

      Context appContext= HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(appContext); //Get the local broadcast manger instance.

      lclBrdcstMngr.sendBroadcast(intent4Wrk); //发送广播。
    } //public void toggleUseHiveLayout(boolean isChecked)

    /**
     * 切换是否显示内置快捷方式。
     */
    @OnCheckedChanged(R.id.builtinShortcutscheckBox)
    public void toggleBuiltinShortcuts(boolean isChecked)
    {
      PreferenceManagerUtil.setBuiltinShortcutsVisible(isChecked); //保存选项。

      Intent intent4Wrk = new Intent(); //创建意图对象。晚安诸位。
      Bundle extras=new Bundle(); //创建数据包。
      extras.putBoolean("builtinShortcutsVisible", isChecked); //加入选项，是否显示内置快捷方式。

      intent4Wrk.putExtras(extras); //设置附加数据。

      intent4Wrk.setAction(Constants.Operation.ToggleBuiltinShortcuts);//切换是否显示内置快捷方式。

      Context appContext= HxLauncherApplication.getAppContext(); //获取应用程序上下文。
      LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(appContext); //Get the local broadcast manger instance.

      lclBrdcstMngr.sendBroadcast(intent4Wrk); //发送广播。
    } //public void toggleBuiltinShortcuts()

    /**
     * Like {@link #rawLaunchIntent(Intent)} but will show a user dialog if nothing is available to handle.
     */
    private void launchIntent(Intent intent)
    {
        try {
            rawLaunchIntent(intent);
        } catch (ActivityNotFoundException ignored) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.msg_intent_failed);
            builder.setPositiveButton(R.string.button_ok, null);
            builder.show();
        }
    }

    /**
     * Like {@link #launchIntent(Intent)} but will tell you if it is not handle-able
     * via {@link ActivityNotFoundException}.
     *
     * @throws ActivityNotFoundException
     */
    private void rawLaunchIntent(Intent intent)
    {
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            Log.d(TAG, "Launching intent: " + intent + " with extras: " + intent.getExtras());
            startActivity(intent);
        }
    }

    /**
     * 跳转到设置应用程序的启动频率的活动界面。
     */
    @OnClick(R.id.appFrequencybutton1)
    public void gotoApplicationFrequencyActivity()
    {
        Intent launchIntent=new Intent(this, ApplicationFrequencySettingsActivity.class); //启动意图。
        startActivity(launchIntent); //启动活动。
    } //public void gotoApplicationFrequencyActivity()

    @OnClick(R.id.appAliasbutton1)
    public void gotoApplicationAliasActivity()
    {
        Intent launchIntent=new Intent(this, ApplicationAliasSettingsActivity.class); //启动意图。
        startActivity(launchIntent); //启动活动。
    } //public void gotoApplicationAliasActivity()


    /**
     * 跳转到自动运行设置活动界面。
     */
    @OnClick(R.id.lanime_button1)
    public void gotoAutoRunSettingsActivity()
    {
        Intent launchIntent=new Intent(this, AutoRunSettingsActivity.class); //启动意图。
        startActivity(launchIntent); //启动活动。

    }

    /**
     * 跳转到账号信息界面。
     */
    @OnClick(R.id.myAccountbutton1)
    public void gotoAccountActivity()
    {
        Intent launchIntent=new Intent(this, AccountActivity.class); //启动意图。
        startActivity(launchIntent); //启动活动。
    } //public void gotoAccountActivity()
}




