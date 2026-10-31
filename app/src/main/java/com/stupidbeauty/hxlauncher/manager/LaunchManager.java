package com.stupidbeauty.hxlauncher.manager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.stupidbeauty.hxlauncher.Constants;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.WakeLockPackageNameSetData;
import com.stupidbeauty.hxlauncher.logic.ShutDownAt2100Logic;

import java.util.HashSet;
import java.util.Set;

import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_PACKAGE_NAME;

/**
 * 应用程序启动管理器。
 *
 * <p>统一管理「启动一个应用」过程中的全部决策逻辑，供 LauncherActivity 与
 * ApplicationUnlockActivity 共同使用。</p>
 *
 * <p>设计要点：</p>
 * <ol>
 *   <li>{@link #launch(Intent)} 是唯一的启动出口。所有启动路径（图标点击、语音识别、
 *       快捷方式、解锁后启动）都必须经过它。</li>
 *   <li><b>21点关机黑名单检查必须前置于「是否跳转到解锁页」的决策</b>，
 *       否则被加了应用锁的应用会先进入解锁页，绕过黑名单限制。</li>
 *   <li>判定与展示解耦：判定结果封装在 {@link LaunchDecision} 中，由调用方决定如何提示。</li>
 * </ol>
 */
public class LaunchManager
{
  private static final String TAG="LaunchManager"; //!< 输出调试信息时使用的标记。

  private final Context context; //!< 上下文。
  private final ShutDownAt2100Logic shutDownAt2100Logic; //!< 21点关机逻辑。

  public LaunchManager(Context context, ShutDownAt2100Logic shutDownAt2100Logic)
  {
    this.context=context;
    this.shutDownAt2100Logic=shutDownAt2100Logic;
  } //public LaunchManager(Context context, ShutDownAt2100Logic shutDownAt2100Logic)

  /**
   * 启动决策结果。
   */
  public static class LaunchDecision
  {
    public boolean allowed=false; //!< 是否允许启动。
    public String reason=""; //!< 判定原因。
    public String blockedUntil=""; //!< 解除限制的时间点描述。
    public String packageName=""; //!< 应用包名。
    public String applicationLabel=""; //!< 应用显示名。

    public static final String REASON_ALLOWED_NOT_BLACKLISTED="ALLOWED_NOT_BLACKLISTED"; //!< 非黑名单应用，始终允许。
    public static final String REASON_ALLOWED_OUTSIDE_WINDOW="ALLOWED_OUTSIDE_WINDOW"; //!< 黑名单应用，但不在受限窗口内。
    public static final String REASON_BLOCKED_BLACKLIST_IN_WINDOW="BLOCKED_BLACKLIST_IN_WINDOW"; //!< 黑名单应用，且处于受限窗口内。
    public static final String REASON_BLOCKED_COOL_DOWN="BLOCKED_COOL_DOWN"; //!< 处于启动冷却时间内。
    public static final String REASON_BLOCKED_UNKNOWN="BLOCKED_UNKNOWN"; //!< 未知原因。

    /**
     * 是否因黑名单而被禁止启动。
     * @return 因黑名单被禁止返回true。
     */
    public boolean isBlockedByBlacklist()
    {
      return REASON_BLOCKED_BLACKLIST_IN_WINDOW.equals(reason);
    }
  } //public static class LaunchDecision

  /**
   * 判断指定包名的应用当前是否允许启动。
   *
   * <p>这是纯判定方法，不启动任何东西。供 UI 层先行查询使用。</p>
   *
   * @param packageName 应用包名。
   * @return 判定结果。
   */
  public LaunchDecision decideLaunch(String packageName)
  {
    LaunchDecision decision=new LaunchDecision();
    decision.packageName=packageName;

    if (packageName==null)
    {
      decision.allowed=true;
      decision.reason=LaunchDecision.REASON_ALLOWED_NOT_BLACKLISTED;
      return decision;
    } //if (packageName==null)

    decision.applicationLabel=getApplicationLabel(packageName);

    // 21点关机黑名单判定：
    boolean allowed=true;
    if (shutDownAt2100Logic!=null)
    {
      allowed=shutDownAt2100Logic.isLaunchAllowed(packageName);
    } //if (shutDownAt2100Logic!=null)

    if (!allowed)
    {
      decision.allowed=false;
      decision.reason=LaunchDecision.REASON_BLOCKED_BLACKLIST_IN_WINDOW;
      Log.d(TAG, "decideLaunch, blocked by blacklist: " + packageName); //Debug.
      return decision;
    } //if (!allowed)

    decision.allowed=true;
    decision.reason=LaunchDecision.REASON_ALLOWED_OUTSIDE_WINDOW;

    return decision;
  } //public LaunchDecision decideLaunch(String packageName)

  /**
   * 判断指定包名的应用当前是否允许启动。
   *
   * @param packageName 应用包名。
   * @return 允许启动返回true，禁止启动返回false。
   */
  public boolean isLaunchAllowed(String packageName)
  {
    return decideLaunch(packageName).allowed;
  } //public boolean isLaunchAllowed(String packageName)

  /**
   * 检查该应用是不是被锁住了。
   *
   * @param packageName 应用包名。
   * @return 被锁住返回true。
   */
  public boolean isApplicationLocked(String packageName)
  {
    if (packageName==null)
    {
      return false;
    } //if (packageName==null)

    HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

    HashSet<String> applicationLockSet=hxLauncherApplication.getApplicationLockSet(); //获取应用锁集合。

    if (applicationLockSet==null) //集合不存在。
    {
      return false;
    } //if (applicationLockSet==null)

    return applicationLockSet.contains(packageName); //是否包含。
  } //public boolean isApplicationLocked(String packageName)

  /**
   * 统一启动入口。
   *
   * <p>判定顺序：</p>
   * <ol>
   *   <li>21点关机黑名单检查 —— 禁止则直接拦截，<b>绝不跳转到解锁页</b>。</li>
   *   <li>启动冷却时间检查。</li>
   *   <li>应用锁检查 —— 锁定则跳转到解锁页。</li>
   *   <li>实际启动。</li>
   * </ol>
   *
   * @param launchIntent 启动意图。
   * @return 是否真的启动了应用。
   */
  public boolean launch(Intent launchIntent)
  {
    boolean result=false; //结果，是否启动成功。

    if (launchIntent==null) //启动意图不存在。
    {
      return false;
    } //if (launchIntent==null)

    ComponentName component=launchIntent.getComponent();

    if (component==null) //组件不存在。
    {
      return false;
    } //if (component==null)

    String packageName=component.getPackageName(); //获取包名。

    // 第一步：21点关机黑名单检查。前置于解锁页跳转决策，防止 locked 应用绕过。
    LaunchDecision decision=decideLaunch(packageName);

    if (!decision.allowed)
    {
      Log.d(TAG, "launch, blocked by blacklist: " + packageName); //Debug.
      onBlocked(decision);
      return false;
    } //if (!decision.allowed)

    // 第二步：启动冷却时间检查。
    HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

    boolean allowedToLaunch=hxLauncherApplication.checkLaunchCoolDownTime(launchIntent); //检查启动的冷却时间。

    if (!allowedToLaunch)
    {
      Log.d(TAG, "launch, blocked by cool down time: " + packageName); //Debug.
      return false;
    } //if (!allowedToLaunch)

    try //尝试启动活动，并且捕获可能的异常。
    {
      checkAndAquireWakeLock(launchIntent); //检查并获取唤醒锁。

      PackageInformationManager packageInformationManager=new PackageInformationManager(); //软件包信息管理器。

      boolean exported=packageInformationManager.checkExported(launchIntent); //检查该个活动是否被导出了。

      boolean applicationLocked=isApplicationLocked(packageName); //检查该应用是不是被锁住了。

      if (exported) //是导出了。
      {
        if (applicationLocked) //应用被加锁。
        {
          gotoApplicationUnlockActivity(launchIntent); //跳转到应用程序解锁活动。
        } //if (applicationLocked)
        else //应用未被加锁。
        {
          launchIntent.putExtra(Constants.Fields.VoiceRecognizeResult, getVoiceRecognizeResult()); //设置语音识别结果。

          assessExcludeFromRecentActivities(launchIntent); //判断要不要从最近任务列表中排除。

          context.startActivity(launchIntent); //启动活动。
        } //else 应用未被加锁。
      } //if (exported)
      else //未导出。启动该应用的默认活动。
      {
        launchApplicationByPackageName(packageName); //按照包名启动。
      } //else 未导出。

      hxLauncherApplication.rememberLaunchTimestamp(launchIntent); //记录启动时间戳。

      result=true; //启动成功。
    } //try 尝试启动活动，并且捕获可能的异常。
    catch (ActivityNotFoundException exception)
    {
      exception.printStackTrace(); //报告错误。
    } //catch (ActivityNotFoundException exception)
    catch (SecurityException exception)
    {
      exception.printStackTrace(); //报告错误。
    } //catch (SecurityException exception)

    return result;
  } //public boolean launch(Intent launchIntent)

  /**
   * 被拦截时的回调。子类可覆盖以自定义提示。
   *
   * @param decision 判定结果。
   */
  protected void onBlocked(LaunchDecision decision)
  {
  } //protected void onBlocked(LaunchDecision decision)

  /**
   * 获取语音识别结果。由子类提供。
   * @return 语音识别结果。
   */
  protected String getVoiceRecognizeResult()
  {
    return null;
  } //protected String getVoiceRecognizeResult()

  /**
   * 跳转到应用程序解锁活动。
   *
   * @param launchIntentForApplication 目标应用的启动意图。
   */
  protected void gotoApplicationUnlockActivity(Intent launchIntentForApplication)
  {
    Intent launchIntent=new Intent(context, com.stupidbeauty.hxlauncher.activity.ApplicationUnlockActivity.class); //启动意图。

    String packageName=launchIntentForApplication.getComponent().getPackageName(); //获取包名。
    String activityName=launchIntentForApplication.getComponent().getClassName(); //获取活动的类名。

    launchIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
    launchIntent.putExtra(EXTRA_COMPONENT_NAME, activityName); //设置部件名字。

    context.startActivity(launchIntent); //启动活动。
  } //protected void gotoApplicationUnlockActivity(Intent launchIntentForApplication)

  /**
   * 根据包名启动应用程序。
   *
   * @param packageName 包名。
   * @return 是否启动成功。
   */
  public boolean launchApplicationByPackageName(String packageName)
  {
    boolean result=false; //启动结果。

    PackageManager packageManager=context.getPackageManager(); //获取软件包管理器。

    Intent launchIntent=packageManager.getLaunchIntentForPackage(packageName); //获取当前软件包的启动意图。

    if (launchIntent!=null) //意图存在。
    {
      try //尝试启动活动，并且捕获可能的异常。
      {
        result=launch(launchIntent); //启动活动。
      } //try 尝试启动活动，并且捕获可能的异常。
      catch (ActivityNotFoundException exception)
      {
        exception.printStackTrace(); //报告错误。
      } //catch (ActivityNotFoundException exception)
    } //if (launchIntent!=null)

    return result;
  } //public boolean launchApplicationByPackageName(String packageName)

  /**
   * 判断要不要从最近任务列表中排除。
   *
   * @param launchIntent 启动意图。
   */
  protected void assessExcludeFromRecentActivities(Intent launchIntent)
  {
    HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

    if (hxLauncherApplication.shouldExcludeFromRecentActivities(launchIntent)) //应该从最近任务列表中排除。
    {
      // https://stackoverflow.com/a/30700304
      // SEt this flag when launchering.
      launchIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    } //if (hxLauncherApplication.shouldExcludeFromRecentActivities(launchIntent))
  } //protected void assessExcludeFromRecentActivities(Intent launchIntent)

  /**
   * Assess to aquire wake lock.
   *
   * @param packageName 包名。
   */
  protected void assesAquireWakeLock(String packageName)
  {
    HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

    WakeLockPackageNameSetData wakeLockPackageNameSetData=application.getWakeLockPackageNameSetData(); //获取唤醒锁应用包名集合数据。

    if (wakeLockPackageNameSetData==null) //数据不存在。
    {
      return;
    } //if (wakeLockPackageNameSetData==null)

    Set<String> wakeLockPackageNameSet=wakeLockPackageNameSetData.getWakeLockPackageNameSet(); //获取唤醒锁应用包名集合。

    if (wakeLockPackageNameSet==null) //集合不存在。
    {
      return;
    } //if (wakeLockPackageNameSet==null)

    Log.d(TAG,"checkAndAquireWakeLock, package name: "+packageName); //Debug.

    if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
    {
      HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

      hxLauncherApplication.acquireWakeLock(); //获取唤醒锁。
    } //if (wakeLockPackageNameSet.contains(packageName))
  } //protected void assesAquireWakeLock(String packageName)

  /**
   * 检查并获取唤醒锁。
   *
   * @param launchIntent 启动意图。
   */
  protected void checkAndAquireWakeLock(Intent launchIntent)
  {
    if (launchIntent!=null) //意图存在。
    {
      String packageName=launchIntent.getComponent().getPackageName(); //获取包名。

      assesAquireWakeLock(packageName); //获取唤醒锁。
    } //if (launchIntent!=null)
  } //protected void checkAndAquireWakeLock(Intent launchIntent)

  /**
   * 检查并更正启动意图中的类名。
   *
   * @param launchIntent 要检查的启动意图。
   */
  protected void checkAndCorrectClassNameInLauncherIntent(Intent launchIntent)
  {
    if (launchIntent==null) //意图不存在。
    {
      return;
    } //if (launchIntent==null)

    String className=launchIntent.getComponent().getClassName(); //获取类名。

    int indexOfDollar=className.indexOf('$'); //寻找美元符号。

    if (indexOfDollar>0) //存在美元符号。
    {
      int indexBeforeDollar=indexOfDollar-1; //复制到前一个字符为止。

      String correctedClassName=className.substring(0, indexBeforeDollar); //切出开头的部分。

      String packageName=launchIntent.getComponent().getPackageName(); //获取包名。

      launchIntent.setClassName(packageName, correctedClassName); //设置类名。
    } //if (indexBeforeDollar>0)
  } //protected void checkAndCorrectClassNameInLauncherIntent(Intent launchIntent)

  /**
   * 获取应用程序的显示名。
   *
   * @param packageName 包名。
   * @return 显示名。获取失败时返回包名本身。
   */
  protected String getApplicationLabel(String packageName)
  {
    if (packageName==null)
    {
      return "";
    } //if (packageName==null)

    try
    {
      PackageManager packageManager=context.getPackageManager(); //获取软件包管理器。

      PackageInfo packageInfo=packageManager.getApplicationInfo(packageName, 0); //获取应用程序信息。

      return packageInfo.loadLabel(packageManager).toString(); //获取显示名。
    } //try
    catch (PackageManager.NameNotFoundException e) //未找到该软件包。
    {
      return packageName; //获取失败时返回包名本身。
    } //catch (PackageManager.NameNotFoundException e)
    catch (Exception e)
    {
      return packageName; //获取失败时返回包名本身。
    } //catch (Exception e)
  } //protected String getApplicationLabel(String packageName)

  /**
   * 检查该个活动是否被导出了。
   *
   * @param launchIntent 要检查的意图。
   * @return 活动是否被导出了。
   */
  protected boolean checkExported(Intent launchIntent)
  {
    PackageInformationManager packageInformationManager=new PackageInformationManager(); //软件包信息管理器。

    return packageInformationManager.checkExported(launchIntent); //是否导出。
  } //protected boolean checkExported(Intent launchIntent)

  /**
   * 获取活动的类名。
   *
   * @param launchIntent 启动意图。
   * @return 活动的类名。
   */
  protected String getActivityName(Intent launchIntent)
  {
    if (launchIntent==null || launchIntent.getComponent()==null)
    {
      return "";
    } //if (launchIntent==null || launchIntent.getComponent()==null)

    return launchIntent.getComponent().getClassName();
  } //protected String getActivityName(Intent launchIntent)

  /**
   * 获取活动的标签信息。
   *
   * @param packageName 包名。
   * @return 活动信息。
   */
  protected ActivityInfo getActivityInfo(String packageName, String activityName)
  {
    try
    {
      PackageManager packageManager=context.getPackageManager(); //获取软件包管理器。

      ComponentName cm=new ComponentName(packageName, activityName); //构造组件名字对象。

      return packageManager.getActivityInfo(cm, 0); //获取活动信息。
    } //try
    catch (PackageManager.NameNotFoundException e) //未找到该活动。
    {
      return null;
    } //catch (PackageManager.NameNotFoundException e)
  } //protected ActivityInfo getActivityInfo(String packageName, String activityName)

} //public class LaunchManager
