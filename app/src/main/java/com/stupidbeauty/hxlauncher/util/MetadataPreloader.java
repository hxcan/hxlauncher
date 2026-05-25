// 修改 com/stupidbeauty/hxlauncher/util/MetadataPreloader.java
package com.stupidbeauty.hxlauncher.util;

import android.app.Activity;

import com.stupidbeauty.hxlauncher.LauncherActivity;
import com.stupidbeauty.hxlauncher.asynctask.BuildActivityLabelPackageItemInfoMapTask;
import com.stupidbeauty.hxlauncher.asynctask.BuildInternationalizationDataPackageNameMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoiceShortcutMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadBuiltinVoicePackageNameMapTask;

/**
 * 语音识别所需元数据的统一预加载调度器。
 *
 * 当前阶段（Phase 1）启用活动标签映射构建、国际化数据映射构建、内置语音快捷方式映射构建和内置语音包名映射构建，
 * 后续将逐步加入输入法包名、快捷方式等映射。
 */
public class MetadataPreloader
{

  /**
   * 启动所有语音识别相关的元数据预加载任务。
   *
   * 目前执行 {@link #buildActivityLabelPackageItemInfoMap(LauncherActivity)}、
   * {@link #buildInternationalizationDataPackageNameMap(LauncherActivity)}、
   * {@link #loadBuiltinVoiceShortcutMap(LauncherActivity)} 和
   * {@link #loadBuiltinVoicePackageNameMap(LauncherActivity)}，
   * 其余任务将在后续阶段启用。
   *
   * @param launcherActivity 必须是 {@link com.stupidbeauty.hxlauncher.LauncherActivity} 实例
   */
  public static void preload(LauncherActivity launcherActivity)
  {
    buildActivityLabelPackageItemInfoMap(launcherActivity);
    buildInternationalizationDataPackageNameMap(launcherActivity);
    loadBuiltinVoiceShortcutMap(launcherActivity);
    loadBuiltinVoicePackageNameMap(launcherActivity);

    // Phase 2~4: 暂未启用，保留占位以便未来扩展
    // buildInputtingPackageNameMap(launcherActivity);
    // loadPinnedShortcutsMap(launcherActivity);
  }

  /**
   * 构建「活动显示标签」到「PackageItemInfo（包名+类名）」的映射。
   * 用于语音识别后快速匹配启动目标。
   */
  public static void buildActivityLabelPackageItemInfoMap(LauncherActivity launcherActivity)
  {
    new BuildActivityLabelPackageItemInfoMapTask().execute(launcherActivity);
  }

  /**
   * 构建「应用程序国际化名字」到「包名」的映射。
   * 用于语音识别后通过应用的本地化名称快速匹配启动目标。
   */
  public static void buildInternationalizationDataPackageNameMap(LauncherActivity launcherActivity)
  {
    new BuildInternationalizationDataPackageNameMapTask().execute(launcherActivity);
  }

  /**
   * 载入内置的语音识别结果与快捷方式信息之间的映射。
   * 用于语音识别后快速匹配内置快捷方式。
   */
  public static void loadBuiltinVoiceShortcutMap(LauncherActivity launcherActivity)
  {
    new LoadBuiltinVoiceShortcutMapTask().execute(launcherActivity);
  }

  /**
   * 载入内置的语音识别结果与包条目信息之间的映射。
   * 用于语音识别后快速匹配内置应用。
   */
  public static void loadBuiltinVoicePackageNameMap(LauncherActivity launcherActivity)
  {
    new LoadBuiltinVoicePackageNameMapTask().execute(launcherActivity);
  }

  // ——— 以下为预留方法，当前不启用 ———

  private static void buildInputtingPackageNameMap(LauncherActivity launcherActivity)
  {
    // new BuildInputtingPackageMapTask().execute(launcherActivity);
  }

  private static void loadPinnedShortcutsMap(LauncherActivity launcherActivity)
  {
    // new LoadPinnedShortcutsTask().execute(launcherActivity);
  }
}
