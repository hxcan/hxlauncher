package com.stupidbeauty.hxlauncher.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.PowerManager;
import android.service.quicksettings.TileService;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;

public class ImgBurstTileService extends TileService 
{

  // Called when the user adds your tile.
  @Override
  public void onTileAdded() {
    super.onTileAdded();
  }

  // Called when your app can update your tile.
  @Override
  public void onStartListening() 
  {
    super.onStartListening();
    
    startFriendShutDownAt2100Service(); // Start the shut down at 2100 service.
  } // public void onStartListening() 
  
  /**
    * 启动友军“21点关机”的服务。
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



  // Called when your app can no longer update your tile.
  @Override
  public void onStopListening() 
  {
    super.onStopListening();
    startFriendShutDownAt2100Service(); // Start the shut down at 2100 service.
  } // public void onStopListening() 

  // Called when the user taps on your tile in an active or inactive state.
  @Override
  public void onClick() 
  {
    super.onClick();
    
    Intent launchIntent = new Intent(this, LauncherActivity.class); //启动意图。
    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    // String packageName=launchIntentForApplication.getComponent().getPackageName(); //获取包名。
    // String activityName=launchIntentForApplication.getComponent().getClassName(); //获取活动的类名。

    // launchIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
    // launchIntent.putExtra(EXTRA_COMPONENT_NAME, activityName); // 设置部件名字。

    // startActivity(launchIntent); //启动活动。

    
    startActivityAndCollapse(launchIntent); // Start launcher acdtitiyv.
  } // public void onClick() 

  // Called when the user removes your tile.
  @Override
  public void onTileRemoved() {
    super.onTileRemoved();
  }
}
