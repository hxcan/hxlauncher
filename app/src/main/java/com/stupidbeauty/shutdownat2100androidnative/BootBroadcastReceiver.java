package com.stupidbeauty.shutdownat2100androidnative;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;

/**
 * 开机结束广播接收器。
 * @author root 蔡火胜。
 *
 */
@SuppressWarnings("DanglingJavadoc")
public class BootBroadcastReceiver extends BroadcastReceiver
{
	@Override
	/**
	 * 接收到广播。
	 */
	public void onReceive(Context context, Intent intent) 
    {
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			HxLauncherApplication baseApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。

			baseApplication.setFirstRunAfterBoot(true); //设置标志，是启动后初次运行。
        } //if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
    } //public void onReceive(Context context, Intent intent)
} //public class BootBroadcastReceiver extends BroadcastReceiver
