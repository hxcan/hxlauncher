package com.stupidbeauty.hxlauncher.application;

import com.stupidbeauty.appstore.bean.AndroidPackageInformation;
import com.stupidbeauty.upgrademanager.parser.TimeStampParser;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import com.stupidbeauty.shutdownat2100.helper.ShutDownAt2100Manager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import android.app.ActivityManager;
import com.stupidbeauty.upgrademanager.listener.PackageNameUrlMapDataListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.stupidbeauty.upgrademanager.UpgradeManager;
import com.stupidbeauty.hxlauncher.listener.BuiltinFtpServerErrorListener; 
import android.os.Process;
import com.stupidbeauty.builtinftp.BuiltinFtpServer;
import java.util.TimerTask;
import com.stupidbeauty.hxlauncher.rpc.MessageFactory;
import android.os.Debug;
import com.upokecenter.cbor.CBORException;
import java.lang.reflect.Field;
import com.upokecenter.cbor.CBORObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import androidx.multidex.MultiDex;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import com.stupidbeauty.hxlauncher.asynctask.LoadPackageItemLaunchCoolDownMap;
import com.stupidbeauty.hxlauncher.asynctask.LoadVoicePackageUrlMapTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadApplicationLockSetTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadApplicationNameInternationalFileTask;
import com.stupidbeauty.hxlauncher.asynctask.LoadPackageItemAliasMapTask;
import com.google.gson.Gson;
import com.stupidbeauty.hxlauncher.Constants;
import com.stupidbeauty.hxlauncher.PackageItemAliasMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.PackageItemAliasMapMessageProtos;
import com.stupidbeauty.hxlauncher.PackageItemLaunchCoolDownMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.PackageItemLaunchCoolDownMapMessageProtos;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.VoicePackageMapJsonItem;
import com.stupidbeauty.hxlauncher.bean.VoicePackageUrlMapData;
import com.stupidbeauty.hxlauncher.bean.WakeLockPackageNameSetData;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.hxlauncher.encryption.CryptoHandler;
import com.stupidbeauty.hxlauncher.encryption.MessageEncryptor;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.victoriafresh.VFile;
import org.apache.commons.io.FileUtils;
import com.stupidbeauty.hxlauncher.bean.ApplicationLockInformation;
import com.stupidbeauty.hxlauncher.manager.NotificationDataManager;

/**
 * 应用程序对象。
 * @author root 蔡火胜。
 */
public class HxLauncherApplication extends Application implements PackageNameUrlMapDataListener
{
	private List<AndroidPackageInformation> packages = null; //!< package list.
  private HashMap<String, Long> packageItemLastLaunchTimestampMap=new HashMap<>(); //!<包名加类名的字符串与最后一次启动时间戳之间的映射。
  private UpgradeManager upgradeManager=null; //!< upgrade manager.
	private  HashMap<String, String > packageNameApplicationNameMap; //!<包名与应用程序名的映射
	private HashMap<String, String> apkUrlPackageNameMap; //!< The map of apk url to package name.
  private HashSet<String> applicationLockSet=null; //!< 应用锁集合。
  private HashMap<String, String> apkFilePathMap=null; //!< 包名与安装包文件路径之间的映片。陈欣
  private RuntimeInformationStore runtimeInformationStore=new RuntimeInformationStore(); //!<运行时信息存储对象．
  private HashMap<String, List<String> > packageNameExtraPackageNamesMap; //!< Map of packge name to extra package names.
  private HashMap<String, String> packageNameInstallerTypeMap; //!< Map of package name to installer type.
  private HashMap<String, String> packageNameIconUrlMap; //!< map of package name of icon url.
  private NotificationDataManager notificationDataManager = new NotificationDataManager(); //!< The notification data manager.
  
  /**
  * 记录启动时间戳。
  * @param launchIntent 启动意图。
  */
  public void rememberLaunchTimestamp(Intent launchIntent)
  {
    String packageName=launchIntent.getComponent().getPackageName(); //获取包名。
    String activityName=launchIntent.getComponent().getClassName(); //获取活动的类名。

    HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
    HashMap<Integer, Integer> seekBarValueCoolDownTimeMap=hxLauncherApplication.getSeekBarValueCoolDownTimeMap(); //获取映射。

    HashMap<String, Integer> packageItemLaunchCoolDownMap=hxLauncherApplication.getPackageItemLaunchCoolDownMap(); //获取映射。包条目信息字符串与实际冷却时间秒数之间的映射。

    long currentTimestamp=System.currentTimeMillis(); //当前的时间戳。

    packageItemLastLaunchTimestampMap.put(packageName+"/"+activityName, currentTimestamp); //记录。
  } //private void rememberLaunchTimestamp(Intent launchIntent)

  /**
  * 检查启动的冷却时间。
  * @param launchIntent 启动意图。
  * @return 根据冷却时间，是否允许启动。
  */
  public boolean checkLaunchCoolDownTime(Intent launchIntent)
  {
    boolean result=false; //结果，是否允许启动。

      String packageName=""; //包名。
      String activityName=""; //活动类名。

      if (launchIntent!=null) //意图存在。
      {
        packageName=launchIntent.getComponent().getPackageName(); //获取包名。

        activityName=launchIntent.getComponent().getClassName(); //获取活动的类名。
      } //if (launchIntent!=null) //意图存在。

      if (activityName.startsWith(".")) //相对名字。
      {
        activityName=packageName+activityName; //构造完整名字。
      } //if (activityName.startsWith(".")) //相对名字。

      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      HashMap<Integer, Integer> seekBarValueCoolDownTimeMap=hxLauncherApplication.getSeekBarValueCoolDownTimeMap(); //获取映射。

      HashMap<String, Integer> packageItemLaunchCoolDownMap=hxLauncherApplication.getPackageItemLaunchCoolDownMap(); //获取映射。包条目信息字符串与实际冷却时间秒数之间的映射。

      if (packageItemLaunchCoolDownMap!=null) // 映射存在。
      {
        if (packageItemLaunchCoolDownMap.containsKey(packageName+"/"+activityName)) //有这个键。
        {
          int launchCoolDownTime=packageItemLaunchCoolDownMap.get(packageName+"/"+activityName); //获取冷却时间。

          if (launchCoolDownTime<0) //永远禁止。
          {
            result=false; //不允许启动。
          } //if (launchCoolDownTime<0) //永远禁止。
          else //没有永远禁止。
          {
            if (packageItemLastLaunchTimestampMap.containsKey(packageName+"/"+activityName)) //有记录上次启动时间。
            {
              long lastLaunchTimeStamp = packageItemLastLaunchTimestampMap.get(packageName+"/"+activityName); //获取上次的启动时间戳。

              long currentTimestamp=System.currentTimeMillis(); //当前的时间戳。

              if ((lastLaunchTimeStamp+launchCoolDownTime*1000)>currentTimestamp) //还未超过冷却时间。
              {
              } //if ((lastLaunchTimeStamp+launchCoolDownTime*1000)>currentTimestamp) //还未超过冷却时间。
              else  //已经超过冷却时间。
              {
                result=true; //允许启动。
              } //else  //已经超过冷却时间。
            } //if (packageItemLastLaunchTimestampMap.containsKey(packageName+"/"+activityName)) //有记录上次启动时间。
            else //没有记录上次启动时间。
            {
              result=true; //允许启动。
            } //else //没有记录上次启动时间。
          } //else //没有永远禁止。
        } //if (packageItemLaunchCoolDownMap.containsKey(packageName+"/"+activityName)) //有这个键。
        else //没有这个键，未限制。
        {
          result=true;
        } //else //没有这个键，未限制。
      }
      else
      {
        result=true;
      }

      return result;
    } //private boolean checkLaunchCoolDownTime(Intent launchIntent)

	/**
	* Set 包名与应用程序名的映射 
	*/
	public void setPackageNameApplicationNameMap (HashMap<String, String > packageNameApplicationNameMap)
	{
    if (this.packageNameApplicationNameMap==null)
    {
      this.packageNameApplicationNameMap=packageNameApplicationNameMap;
    } // if (this.packageNameApplicationNameMap==null)
    else
    {
      this.packageNameApplicationNameMap.putAll(packageNameApplicationNameMap);
    }
	} //public void setPackageNameApplicationNameMap (HashMap<String, String > packageNameApplicationNameMap)

	@Override
	/**
	* Set 包名与应用程序名的映射 
	*/
	public void setApkUrlPackageNameMap(HashMap<String, String > packageNameApplicationNameMap)
	{
    if (this.apkUrlPackageNameMap==null)
    {
      this.apkUrlPackageNameMap=packageNameApplicationNameMap;
    } // if (this.packageNameApplicationNameMap==null)
    else
    {
      this.apkUrlPackageNameMap.putAll(packageNameApplicationNameMap);
    }
	} //public void setPackageNameApplicationNameMap (HashMap<String, String > packageNameApplicationNameMap)

  public HashSet<String> getApplicationLockSet()
  {
    return applicationLockSet;
  } // public HashSet<String> getApplicationLockSet()
  
  /**
  * Set package name installer type map.
  */
  public void setPackageNameInstallerTypeMap(HashMap<String, String> packageNameInstallerTypeMap)
  {
    if (this.packageNameInstallerTypeMap==null)
    {
      this.packageNameInstallerTypeMap=packageNameInstallerTypeMap;
    }
    else // NOt null
    {
      this.packageNameInstallerTypeMap.putAll(packageNameInstallerTypeMap);
    } // else // NOt null
  } // public void setPackageNameInstallerTypeMap(HashMap<String, String> packageNameInstallerTypeMap)

  /**
  * Set the map of package name to extra package names list.
  */
  public void setPackageNameExtraPackageNamesMap(HashMap<String, List<String> > packageNameExtraPackageNamesMap)
  {
    Log.d(TAG, "setPackageNameExtraPackageNamesMap, dance ball result in provided map: " + packageNameExtraPackageNamesMap.get("com.lew.game.danceball.lenovo"));
    if (this.packageNameExtraPackageNamesMap==null)
    {
      this.packageNameExtraPackageNamesMap=packageNameExtraPackageNamesMap;
    }
    else // NOt null
    {
      this.packageNameExtraPackageNamesMap.putAll(packageNameExtraPackageNamesMap);
    } // else // NOt null

    Log.d(TAG, "setPackageNameExtraPackageNamesMap, dance ball result in final map: " + this.packageNameExtraPackageNamesMap.get("com.lew.game.danceball.lenovo"));
  } // public void setPackageNameExtraPackageNamesMap(HashMap<String, List<String> > packageNameExtraPackageNamesMap)

  /**
  * 设置应用锁集合。
  */
  public void setApplicationLockSet(HashSet<String> applicationLockSet)
  {
    this.applicationLockSet=applicationLockSet;
  } // public void setApplicationLockSet(HashSet<String> applicationLockSet)

  public RuntimeInformationStore getRuntimeInformationStore() 
  {
    return runtimeInformationStore;
  }

  public CryptoHandler getCryptoHandler() 
  {
    return cryptoHandler;
  }

	/**
	* Get icon url.
	*/
	public String getIconForPackage(String packagename) 
	{
    String result="";
    
    if (packageNameIconUrlMap!=null)
    {
      result=packageNameIconUrlMap.get(packagename);
    } // if (packageNameIconUrlMap!=null)
    
    return result;
	} // public String getIconForPackage(String packagename)

  private CryptoHandler cryptoHandler=new CryptoHandler(); //!<加密管理器。
  private MessageEncryptor messageEncryptor=new MessageEncryptor(); //!<消息加密器。

  public MessageEncryptor getMessageEncryptor() 
  {
    return messageEncryptor;
  }

  private HashMap<String, Integer> packageItemLaunchCoolDownMap=null; //!<映射。包条目信息字符串与实际冷却时间秒数之间的映射。
  private HashMap<String, String> packageItemAliasMap=null; //!<映射。包条目信息字符串与别名之间的映射。

  public HashMap<String, String> getPackageItemAliasMap() 
  {
    return packageItemAliasMap;
  }

	@Override
	/**
	* Set the package list.
	*/
	public void setPackages(List<AndroidPackageInformation> packageList)
	{
    if (packageList.size() > 0) // The package contains inforamtion
    {
      this.packages = packageList;
    } // if (packageList.size()) // The package contains inforamtion
	} // public void setPackages(List<AndroidPackageInformation> packageList)

	public void  setPackageItemAliasMap(HashMap<String, String>  map) 
	{
		packageItemAliasMap=map;
	}
	
	/**
	* Start check upgrade.
	*/
	public void startCheckUpgrade() 
	{
    if (upgradeManager==null) // Upgrade manager does not exist
    {
      upgradeManager=new UpgradeManager(this); // Create upgrade manager.
    } // if (upgradeManager==null) // Upgrade manager does not exist
      
		upgradeManager.setPackageNameUrlMapDataListener(this);
      
		upgradeManager.checkUpgrade(); // Check upgrade.
	} // private void startCheckUpgrade()

	private HashMap<Integer, Integer> seekBarValueCoolDownTimeMap =new HashMap<>(); //!<映射。寻找条上的索引，与实际冷却时间秒数之间的映射。
	private HashMap<Integer, Integer> coolDownTimeSeekBarValueMap = new HashMap<>(); //!<映射。 The map to cool down time to seek bar value.
	private boolean firstRunAfterBoot=false; //!<标志，是否是启动后初次运行。
	PowerManager.WakeLock wakeLock=null; //!<游戏辅助唤醒锁。

	public HashMap<String, Integer> getPackageItemLaunchCoolDownMap() 
	{
		loadPackageItemLaunchCoolDownMap(); // 载入。
	
		return packageItemLaunchCoolDownMap;
	} //public HashMap<String, Integer> getPackageItemLaunchCoolDownMap() 

	public void setPackageItemLaunchCoolDownMap(HashMap<String, Integer> map) 
	{
		this.packageItemLaunchCoolDownMap=map;
	}
	
	/**
	* Get the map of cool down time to seek bar value.
	*/
	public HashMap<Integer, Integer> getCoolDownTimeSeekBarValueMap()
	{
		return coolDownTimeSeekBarValueMap;
	} // public HashMap<Integer, Integer> getCoolDownTimeSeekBarValueMap()

	public HashMap<Integer, Integer> getSeekBarValueCoolDownTimeMap() 
	{
		return seekBarValueCoolDownTimeMap;
	} // public HashMap<Integer, Integer> getSeekBarValueCoolDownTimeMap() 

	public boolean isFirstRunAfterBoot() {
		return firstRunAfterBoot;
	}

	public void setFirstRunAfterBoot(boolean firstRunAfterBoot) {
		this.firstRunAfterBoot = firstRunAfterBoot;
	}

	@Override
	protected void attachBaseContext(Context base) 
	{
		super.attachBaseContext(base);

		MultiDex.install(this); //启动MultiDex.
	}

	private VoicePackageUrlMapData voicePackageUrlMapData; //!<语音识别结果与软件包下载地址之间的映射。
	public HashMap<String, String> getVoicePackageUrlMap() 
	{
		return voicePackageUrlMap;
	}

	private HashMap<String, String> voicePackageUrlMap; //!<语音识别结果与包名之间的映射关系。
	
	/**
	* 语音识别结果与包名之间的映射关系。
	*/
	public void setVoicePackageUrlMap (HashMap<String, String> voicePackageUrlMap)
	{
    if (this.voicePackageUrlMap==null)
		{
			this.voicePackageUrlMap=voicePackageUrlMap;
		}
		else // voice package url map exists
		{
      if (voicePackageUrlMap!=null) // The source map exists
      {
        this.voicePackageUrlMap.putAll(voicePackageUrlMap);
      } // if (voicePackageUrlMap!=null) // The source map exists
		} // else // voice package url map exists

//       this.voicePackageUrlMap=voicePackageUrlMap;
	} //public void setVoicePackageUrlMap (HashMap<String, String> voicePackageUrlMap)

	public HashMap<String, String> getPackageNameUrlMap() 
	{
    return packageNameUrlMap;
	}

	private HashMap<String, String> packageNameUrlMap; //!<包名与下载地址之间的映射关系。

  private HashMap<String, String> packageNameInformationUrlMap; //!< 包名与信息页面地址之间的映射关系。

  /**
  * 获取数据对象。包名与信息页面地址之间的映射。
  */
  public HashMap<String, String> getPackageNameInformationUrlMap() 
  {
    return packageNameInformationUrlMap;
  } // public HashMap<String, String> getPackageNameInformationUrlMap()

	/**
	* 设置包名与信息页面地址之间的映射。
	*/
	public void setPackageNameInformationUrlMap(HashMap<String, String> packageNameInformationUrlMap) 
	{
		if (this.packageNameInformationUrlMap==null)
		{
			this.packageNameInformationUrlMap=packageNameInformationUrlMap;
		}
		else 
		{
			this.packageNameInformationUrlMap.putAll(packageNameInformationUrlMap);
		}
	} // public void setPackageNameInformationUrlMap(HashMap<String, String> packageNameInformationUrlMap)

	/**
	* 设置包名与下载地址之间的映射关系。
	*/
  public void setPackageNameUrlMap (HashMap<String, String> packageNameUrlMap) //!<包名与下载地址之间的映射关系。
  {
    if (this.packageNameUrlMap==null)
    {
      this.packageNameUrlMap=packageNameUrlMap;
    }
    else // NOt null
    {
      this.packageNameUrlMap.putAll(packageNameUrlMap);
    } // else // NOt null
    
		Log.d(TAG,"setPackageNameUrlMap, cn.j.yoyo: "+  this.packageNameUrlMap.get("cn.j.yoyo") + ", this: " + this); //Debug.
  } //public void setPackageNameUrlMap (HashMap<String, String> packageNameUrlMap)

	private HashMap<String, String> packageNameVersionNameMap; //!< 包名与可用版本号之间的映射关系。
	
	/**
	* set 包名与可用版本号之间的映射关系。
	*/
	public void setPackageNameVersionNameMap (HashMap<String, String> packageNameVersionNameMap) 
	{
		if (this.packageNameVersionNameMap==null)
		{
			this.packageNameVersionNameMap=packageNameVersionNameMap;
		}
		else
		{
			this.packageNameVersionNameMap.putAll(packageNameVersionNameMap);
		}
	} //public void setPackageNameVersionNameMap (HashMap<String, String> packageNameVersionNameMap)

  public void setPackageNameIconUrlMap(HashMap<String, String> packageNameUrlMap)
  {
    if (this.packageNameIconUrlMap==null)
    {
      this.packageNameIconUrlMap=packageNameUrlMap;
    }
    else // NOt null
    {
      this.packageNameIconUrlMap.putAll(packageNameUrlMap);
    } // else // NOt null
  } // public void setPackageNameIconUrlMap(HashMap<String, String> packageNameUrlMap)

	/**
	 * 获取唤醒锁。
	 */
	public void acquireWakeLock()
	{
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "HxLauncher::WakeLockForGames");

		Log.d(TAG,"acquireWakeLock, wake lock: "+wakeLock + ", this: " + this); //Debug.

		wakeLock.acquire();
	} //private void acquireWakeLock()
	
	/**
	* Kill background processes.
	*/
	public void killBackgroundProcesses()
	{
    killBackgroundProcessesCoolDownMap(); // Kill the processes with cool down map items
    
    killBackgroundProcessesLocked(); // Kill the processes with lock.
	} // public void killBackgroundProcesses()
	
	/**
	* Kill the processes with cool down map items
	*/
	private void killBackgroundProcessesCoolDownMap()
	{
    // Chen xin.
    if (packageItemLaunchCoolDownMap!=null)
    {
      List<String> keys = new ArrayList<>(packageItemLaunchCoolDownMap.keySet());
      


      
      for(String packageNameItem: keys    )
      {
        String[] itemParts=packageNameItem.split("/");
        String packageName=itemParts[0];
        Log.d(TAG, CodePosition.newInstance().toString()+ ", killing package name: "+ packageName); // Debug.
        killBackgroundProcessesForPackage(packageName); // kill background processes for this package.
      } // applicationLockSet
    } // if (packageItemLaunchCoolDownMap!=null)

	} // private void killBackgroundProcessesCoolDownMap()
	
	/**
	* Should exclude from recent activities
	*/
	public boolean shouldExcludeFromRecentActivities(Intent launchIntent)
	{
    // cool down map:
      boolean result=false; //结果，是否允许启动。

      String packageName=""; //包名。
      String activityName=""; //活动类名。

      if (launchIntent!=null) //意图存在。
      {
        packageName=launchIntent.getComponent().getPackageName(); //获取包名。

        activityName=launchIntent.getComponent().getClassName(); //获取活动的类名。
      } //if (launchIntent!=null) //意图存在。

      if (activityName.startsWith(".")) //相对名字。
      {
        activityName=packageName+activityName; //构造完整名字。
      } //if (activityName.startsWith(".")) //相对名字。

      // HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      // HashMap<Integer, Integer> seekBarValueCoolDownTimeMap=hxLauncherApplication.getSeekBarValueCoolDownTimeMap(); //获取映射。
      // 
      // HashMap<String, Integer> packageItemLaunchCoolDownMap=hxLauncherApplication.getPackageItemLaunchCoolDownMap(); //获取映射。包条目信息字符串与实际冷却时间秒数之间的映射。

      if (packageItemLaunchCoolDownMap!=null) // 映射存在。
      {
        if (packageItemLaunchCoolDownMap.containsKey(packageName+"/"+activityName)) // It is in cool down map.
        {
          {
            {
              result=true; // Should exclude.
            } //else //没有记录上次启动时间。
          } //else //没有永远禁止。
        } //if (packageItemLaunchCoolDownMap.containsKey(packageName+"/"+activityName)) //有这个键。
      }

    
    
    // lock:
    if (!result) // not exclude for cool down map.
    {
      result=applicationLockSet.contains(packageName); // included in lock set.
    } //if (launchIntent!=null) //启动意图存在。
    
    return result;
	} // public boolean shouldExcludeFromRecentActivities(Intent launchIntent)
	
	/**
	* Kill the processes with lock.
	*/
	private void killBackgroundProcessesLocked()
	{
    // Chen xin.
    
    if (applicationLockSet!=null)
    {
      for(String packageNameItem: applicationLockSet    )
      {
        String[] itemParts=packageNameItem.split("/");
        String packageName=itemParts[0];
        Log.d(TAG, CodePosition.newInstance().toString()+ ", killing package name: "+ packageName); // Debug.
      
        killBackgroundProcessesForPackage(packageName); // kill background processes for this package.
      } // applicationLockSet
    } // if (applicationLockSet!=null)
	} // private void killBackgroundProcessesLocked()
	
	/**
	* kill background processes for this package.
	*/
	private void killBackgroundProcessesForPackage(String packageName)
	{
    // Chenx in.
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        am.killBackgroundProcesses(packageName); // Kill background processes.
	} // private void killBackgroundProcessesForPackage(String packageName)

	public void releaseWakeLock() //释放唤醒锁。
	{
		Log.d(TAG,"releaseWakeLock, wake lock: "+wakeLock + ", this: " + this); //Debug.

		if (wakeLock!=null) //唤醒锁存在。
    {
      wakeLock.release();
      wakeLock=null; //置空，避免重复释放。
    } //if (wakeLock!=null) //唤醒锁存在。
	} //public void releaseWakeLock()

	public ApplicationNameInternationalizationData getApplicationNameInternationalizationData() 
	{
		return applicationNameInternationalizationData;
	} //public ApplicationNameInternationalizationData getApplicationNameInternationalizationData() 

  public void setApplicationNameInternationalizationData (ApplicationNameInternationalizationData applicationNameInternationalizationData) 
	{
		this.applicationNameInternationalizationData=applicationNameInternationalizationData;
	} //public ApplicationNameInternationalizationData getApplicationNameInternationalizationData() 

	public WakeLockPackageNameSetData getWakeLockPackageNameSetData() 
	{
		return wakeLockPackageNameSetData;
	}

	private WakeLockPackageNameSetData wakeLockPackageNameSetData; //!<唤醒锁应用程序包名列表容器数据。

	private ApplicationNameInternationalizationData applicationNameInternationalizationData ; //!<应用程序可读名字国际化数据。

	public HashMap<String, Drawable> getLaunchIconMap()
	{
		return applicationListData.getLaunchIconMap();
	}
	
	/**
	* Get the notificaiton data manager.
	*/
	public NotificationDataManager getNotificationDataManager()
	{
    return notificationDataManager;
	} // public NotificationDataManaager getNotificationDataManager()

	public ApplicationListData getApplicationListData()
	{
		return applicationListData;
	}

	/**
	 * 将对象加入到本地服务器载入完毕的回调列表中。
	 * @param localServerListLoadListener 要加入的回调对象。
	 */
	public void addLocalServerListLoadListener(LocalServerListLoadListener localServerListLoadListener)
	{
		applicationListData.addLocalServerListLoadListener(localServerListLoadListener); //加入列表。

	} //public void addLocalServerListLoadListener(LocalServerListLoadListener localServerListLoadListener)



	@SuppressLint("StaticFieldLeak")
	private static HxLauncherApplication mInstance = null;

	public static HxLauncherApplication getInstance() 
	{
      if (mInstance == null) 
      {
        mInstance = new HxLauncherApplication();
      }
      return mInstance;
	}

	private ApplicationListData applicationListData; //!< 应用程序列表数据。

	private static Context mContext;
	private static final String TAG="HxLauncherApplication"; //!< 输出调试信息时使用的标记。
	
	@Override
	/**
	 * 程序被创建。
	 */
	public void onCreate() 
	{
		long startTimestamp=System.currentTimeMillis(); // 记录开始时间戳。

		super.onCreate(); //创建超类。

		mInstance = this;
		// Initialize FileLogger for external storage logging
		com.stupidbeauty.hxlauncher.utils.FileLogger.init(this);


		mContext = getApplicationContext(); //获取应用程序上下文。

		//启用严格模式：

		StrictMode.setThreadPolicy(
			new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
// 				.penaltyLog()
				.build()
		);

		StrictMode.setVmPolicy(
			new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
// 				.penaltyLog()
				.build()
			);

		registerBroadcastReceiver(); //注册广播事件接收器。

		loadApplicationNameInternationalFile(); //载入应用程序名字的国际化信息文件。

		loadWakeLockPackageNameSetData(); //载入唤醒锁应用程序包名集合。

		loadVoicePackageUrlMap(); //载入语音识别结果与包下载地址之间的映射。
		loadApplicationLockSet(); // 载入应用锁集合。

		loadApplicationList(); //载入应用程序列表。

		loadPackageItemLaunchCoolDownMap(); //载入包条目的启动冷却时间数据。

		loadPackageItemAliasMap(); //载入包条目的别名数据。

		initializeSeekBarValueCoolDownTimeMap(); //初始化映射。
		initializeCoolDownTimeSeekBarValueMap(); // 初始化映射。 cool down time to seek bar value.
		
		startCheckUpgrade(); // Start check upgrade.
	} //public void onCreate()
	
	/**
	* 初始化映射。 cool down time to seek bar value.
	*/
	private void initializeCoolDownTimeSeekBarValueMap() 
	{
    coolDownTimeSeekBarValueMap.put(0, 0);
    coolDownTimeSeekBarValueMap.put(30, 1);
    coolDownTimeSeekBarValueMap.put(1*60, 2);
    coolDownTimeSeekBarValueMap.put(10*60, 3);
    coolDownTimeSeekBarValueMap.put(30*60, 4);
    coolDownTimeSeekBarValueMap.put(1*60*60, 5);
    coolDownTimeSeekBarValueMap.put(2*60*60, 6);
    coolDownTimeSeekBarValueMap.put(-1, 7);
	} // private void initializeCoolDownTimeSeekBarValueMap()

	/**
	 * 初始化映射。
	 */
	private void initializeSeekBarValueCoolDownTimeMap()
	{
    seekBarValueCoolDownTimeMap.put(0, 0);
    seekBarValueCoolDownTimeMap.put(1, 30);
    seekBarValueCoolDownTimeMap.put(2, 1*60);
    seekBarValueCoolDownTimeMap.put(3, 10*60);
    seekBarValueCoolDownTimeMap.put(4, 30*60);
    seekBarValueCoolDownTimeMap.put(5, 1*60*60);
    seekBarValueCoolDownTimeMap.put(6, 2*60*60);
    seekBarValueCoolDownTimeMap.put(7, -1);
	} //private void initializeSeekBarValueCoolDownTimeMap()

	/**
	 * 载入包条目的别名数据。
	 */
	private void loadPackageItemAliasMap()
	{
        LoadPackageItemAliasMapTask translateRequestSendTask =new LoadPackageItemAliasMapTask(); //创建异步任务。
//        translateRequestSendTask.setLauncherActivity(this); //设置启动活动。

        translateRequestSendTask.execute(this); //执行任务。

// 		File photoFile=findPackageItemAliasMapFile(); //寻找映射文件。
// 
// 		packageItemAliasMap=new HashMap<>(); //创建映射。
// 
// 		if (photoFile!=null) //不是空指针。
// 		{
// 			if (photoFile.exists()) //文件存在。
// 			{
// 				try
// 				{
// 					byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。
// 
// 					PackageItemAliasMapMessageProtos.PackageItemAliasMapMessage translateRequestMessage=PackageItemAliasMapMessageProtos.PackageItemAliasMapMessage.parseFrom(photoBytes); //创建一个消息对象。
// 
// 					List<PackageItemAliasMapItemMessageProtos.PackageItemAliasMapItemMessage> relationships=translateRequestMessage.getMapList(); //获取关系列表。
// 
// 					for(PackageItemAliasMapItemMessageProtos.PackageItemAliasMapItemMessage currentRelationship: relationships) //一个个地加入映射中。
// 					{
// 						Log.i(TAG, "loadVoicePackageNameMap, package name: "+ currentRelationship.getPackageItemName() + ", auto run: " + currentRelationship.getAlias()); //Debug.
// 
// 
// 						packageItemAliasMap.put(currentRelationship.getPackageItemName(), currentRelationship.getAlias()); //加入映射。
// 					} //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。
// 
// 
// 				}
// 				catch (IOException e)
// 				{
// 					e.printStackTrace();
// 				} //catch (IOException e)
// 
// 			} //if (photoFile.exists()) //文件存在。
// 
// 		} //if (photoFile!=null) //不是空指针。

	} //private void loadPackageItemAliasMap()

	/**
	 * 载入语音识别结果与包名之间的映射。
	 */
	private void loadPackageItemLaunchCoolDownMap()
	{
    LoadPackageItemLaunchCoolDownMap translateRequestSendTask =new LoadPackageItemLaunchCoolDownMap(); //创建异步任务。
    //        translateRequestSendTask.setLauncherActivity(this); //设置启动活动。

    translateRequestSendTask.execute(this); //执行任务。

// 		File photoFile=findRandomPhotoFile(); //随机寻找一个照片文件。
// 
// 		packageItemLaunchCoolDownMap=new HashMap<>(); //创建映射。
// 
// 		if (photoFile!=null) //不是空指针。
// 		{
// 			if (photoFile.exists()) //文件存在。
// 			{
// 				try
// 				{
// 					byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。
// 
// 					PackageItemLaunchCoolDownMapMessageProtos.PackageItemLaunchCoolDownMapMessage translateRequestMessage=PackageItemLaunchCoolDownMapMessageProtos.PackageItemLaunchCoolDownMapMessage.parseFrom(photoBytes); //创建一个消息对象。
// 
// 					List<PackageItemLaunchCoolDownMapItemMessageProtos.PackageItemLaunchCoolDownMapItemMessage> relationships=translateRequestMessage.getMapList(); //获取关系列表。
// 
// 					for(PackageItemLaunchCoolDownMapItemMessageProtos.PackageItemLaunchCoolDownMapItemMessage currentRelationship: relationships) //一个个地加入映射中。
// 					{
// 						Log.i(TAG, "loadVoicePackageNameMap, package name: "+ currentRelationship.getPackageItemName() + ", auto run: " + currentRelationship.getLaunchCoolDown()); //Debug.
// 
// 
// 						packageItemLaunchCoolDownMap.put(currentRelationship.getPackageItemName(), currentRelationship.getLaunchCoolDown()); //加入映射。
// 					} //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。
// 
// 
// 				}
// 				catch (IOException e)
// 				{
// 					e.printStackTrace();
// 				} //catch (IOException e)
// 
// 			} //if (photoFile.exists()) //文件存在。
// 
// 		} //if (photoFile!=null) //不是空指针。

	} //private void loadVoicePackageNameMap()


	/**
	 * 注册广播事件接收器。
	 */
	private void registerBroadcastReceiver()
	{
		//本地广播接收器，应用被启动：
		IntentFilter intentFilter = new IntentFilter(); //创建意图过滤器。

		intentFilter.addAction(Constants.NativeMessage.APPLICATION_LAUNCHED); //应用程序被启动。

		LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(this); //Get the broadcast manager instance.
		lclBrdcstMngr.registerReceiver(mBroadcastReceiver, intentFilter); //注册接收器。

		//注册全局的广播接收器，应用新增，应用删除：
		IntentFilter packageChangeIntentFilter=new IntentFilter(); //创建意图过滤器。
		packageChangeIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); //应用被添加。
		packageChangeIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); //应用被删除。
		
		packageChangeIntentFilter.addDataScheme("package"); //加入数据模式。

		registerReceiver(mBroadcastReceiver,packageChangeIntentFilter); //注册广播事件接收器。
		

		//外部存储上的应用可用了：
		IntentFilter extranlPackageChangeIntentFilter=new IntentFilter(); //创建意图过滤器。
        extranlPackageChangeIntentFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE); //外部存储上的应用可用了。
        registerReceiver(mBroadcastReceiver,extranlPackageChangeIntentFilter); //注册广播事件接收器。
		
	} //private void registerBroadcastReceiver()

	/**
	 * 广播接收器。
	 */
	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{
		private final String TAG="BroadcastReceiver"; //!<输出调试信息时使用的标记。

		@Override
		/**
		 * 接收到广播。
		 */
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction(); //获取广播中带的动作字符串。

			Log.d(TAG,"onReceive,got broadcast:"+action); //Debug.

			if (Constants.NativeMessage.APPLICATION_LAUNCHED.equals(action)) //虚拟卡启动结果。
			{
				Bundle extras=intent.getExtras(); //获取参数包。
				String packageName=extras.getString(Constants.NativeMessage.APPLICATION_LAUNCHED_PACKAGE_KEY); //get the progress value.
			} //if (Constants.NativeMessage.APPLICATION_LAUNCHED.equals(action)) //虚拟卡启动结果。
			else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
			{
				Bundle extras=intent.getExtras(); //获取参数包。
				int uid=extras.getInt(Intent.EXTRA_UID); //获取该软件包对应的用户编号。

				showNewlyAddedPackage(uid); //显示新安装的软件包。
			} //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
			else if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) //外部存储上的应用可用了。
			{
				Bundle extras=intent.getExtras(); //获取参数包。

				CharSequence[] newPackageNameList=extras.getCharSequenceArray(Intent.EXTRA_CHANGED_PACKAGE_LIST); //获取新软件包名字列表。
				
				showExternalPackageList(newPackageNameList); //显示外部存储上的应用程序列表。
			} //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
			else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) //应用被卸载。
			{
				Bundle extras=intent.getExtras(); //获取参数包。

				int uid=extras.getInt(Intent.EXTRA_UID); //获取该软件包对应的用户编号。

				String packageName=intent.getData().getSchemeSpecificPart(); //获取软件包名字。

				showRemovedPackage(packageName); //更新显示，有软件包被卸载。
			} //else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) //应用被安装。
		} //public void onReceive(Context context, Intent intent)
	}; //private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()

	/**
	 * 更新显示，有软件包被卸载。
	 * @param uid 被卸载的软件包的用户编号。
	 */
	public void showRemovedPackage(String packageName)
	{
		applicationListData.removePackage(packageName); //删除被卸载的软件包。

	} //public void showRemovedPackage(int uid)
	
	/**
	* 显示外部存储上的应用程序列表。
	*/
	public void showExternalPackageList(CharSequence[] newPackageNameList)
	{
    applicationListData.addExternalPackageList(newPackageNameList); //加入外部存储上的软件包列表。
	} //public void showExternalPackageList(List<String> newPackageNameList)

	/**
	 * 显示新安装的软件包。
	 * @param uid 软件包的用户编号。
	 */
	public void showNewlyAddedPackage(int uid)
	{
		applicationListData.addNewlyAddedPackage(uid); //加入新安装的软件包。
	} //public void showNewlyAddedPackage(int uid)
	
	/**
	* Get the map of apk url to package name.
	*/
	public HashMap<String, String> getApkUrlPackageNameMap()
	{
		return apkUrlPackageNameMap;
	} // public HashMap<String, String> getApkUrlPackageNameMap()

	public HashMap<String, String> getPackageNameApplicationNameMap() 
	{
		return packageNameApplicationNameMap;
	}

	/**
	 *  // 获取可用的版本名字。
	 * @param packageName
	 * @return
	 */
	public String getAvailableVersionName(String packageName)
	{
    String result= packageNameVersionNameMap.get(packageName); // 获取可用 版本号名字。

    return result;
	} //public String getAvailableVersionName(String packageName)
	
	/**
	* 载入应用锁集合。
	*/
	private void loadApplicationLockSet() 
	{
    LoadApplicationLockSetTask translateRequestSendTask =new LoadApplicationLockSetTask(); // 创建异步任务。

    translateRequestSendTask.execute(this); // 执行任务。
	} // private void loadApplicationLockSet()

	/**
	 * 载入语音识别结果与包下载地址之间的映射。
	 */
	private void loadVoicePackageUrlMap()
	{
    LoadVoicePackageUrlMapTask translateRequestSendTask =new LoadVoicePackageUrlMapTask(); //创建异步任务。

    translateRequestSendTask.execute(this); //执行任务。
	} //private void loadVoicePackageUrlMap()

	/**
	 * 载入唤醒锁应用程序包名集合。
	 */
	private void loadWakeLockPackageNameSetData()
	{
    String qrcFileName="wakeLockPackageNameSet.json"; //文件名。
    String fullQrcFileName=":/WakeLockPackageNameSet/"+qrcFileName; //构造完整的qrc文件名。

    VFile qrcHtmlFile=new VFile(this, fullQrcFileName); //qrc网页文件。

    String fileContent=qrcHtmlFile.getFileTextContent(); //获取文件的完整内容。

    Log.w(TAG, "loadWakeLockPackageNameSetData, 572, loadWakeLockPackageNameSetData, timestamp: " + System.currentTimeMillis()); //Debug.
		Gson gson=new Gson();
        Log.w(TAG, "loadWakeLockPackageNameSetData, 564, loadWakeLockPackageNameSetData, timestamp: " + System.currentTimeMillis()); //Debug.

		wakeLockPackageNameSetData = gson.fromJson(fileContent, WakeLockPackageNameSetData.class); //解析。
        Log.w(TAG, "loadWakeLockPackageNameSetData, 577, loadWakeLockPackageNameSetData, timestamp: " + System.currentTimeMillis()); //Debug.
	} //private void loadWakeLockPackageNameSetData()

	/**
	 * 载入应用程序名字的国际化信息文件。
	 */
	private void loadApplicationNameInternationalFile()
	{
    LoadApplicationNameInternationalFileTask translateRequestSendTask =new LoadApplicationNameInternationalFileTask(); //创建异步任务。

    translateRequestSendTask.execute(this); //执行任务。
	} //private void loadApplicationNameInternationalFile()

	/**
	 * 获取版本名字。
	 * @param packageName 包名。陈欣
	 * @return 这个软件包现在的版本名字。
	 */
	public String getVersionName(String packageName)
	{
		String versionName=null;
		try
		{
		PackageManager packageManager=getPackageManager(); //获取软件包管理器。
		PackageInfo packageInfo=packageManager.getPackageInfo(packageName,0); //获取对应的软件包信息。


			 versionName= packageInfo.versionName; // 获取版本号名字。
	}
                catch (PackageManager.NameNotFoundException e) //未找到该软件包。
	{
		e.printStackTrace(); //报告错误。
	} //catch (PackageManager.NameNotFoundException e) //未找到该软件包。

		return versionName;
	} //public String getVersionName(String packageName)



	/**
	 * 载入应用程序列表。
	 */
	private void loadApplicationList()
	{
		applicationListData =new ApplicationListData(this); //创建数据对象。

		applicationListData.loadApplicationList(); //载入本地服务器列表。
	} //private void loadApplicationList()

	/**
	 * 获取应用程序上下文。
	 * @return 应用程序上下文。
	 */
	public static Context getAppContext() 
	{ 
		return mContext; 
	}  //public static Context getAppContext()

	/**
	 * 保存映射。包条目字符串，与别名之间的映射。
	 */
	public void savePackageItemAliasMap()
	{
		PackageItemAliasMapMessageProtos.PackageItemAliasMapMessage.Builder translateRequestMessage= PackageItemAliasMapMessageProtos.PackageItemAliasMapMessage.newBuilder(); //创建一个消息对象。

		for(String currentVoiceRecognizeResult: packageItemAliasMap.keySet()) //一个个地保存。
		{
// 			String currentPackageName=packageItemAliasMap.get(currentVoiceRecognizeResult); //获取包名。
			String alias=packageItemAliasMap.get(currentVoiceRecognizeResult); //获取包名。

			Log.i(TAG,"savePackageNameAutoRunMap, package name: "+currentVoiceRecognizeResult+ ", auto run: " + alias); //Debug.


// 			PackageItemAliasMapItemMessageProtos.PackageItemAliasMapItemMessage.Builder translateRequestMessageBuilder= PackageItemAliasMapItemMessageProtos.PackageItemAliasMapItemMessage.newBuilder();
			PackageItemAliasMapItemMessageProtos.PackageItemAliasMapItemMessage.Builder packageAliasMapBuilder= PackageItemAliasMapItemMessageProtos.PackageItemAliasMapItemMessage.newBuilder();

			packageAliasMapBuilder.setAlias(alias).setPackageItemName(currentVoiceRecognizeResult); //设置字段。

			translateRequestMessage.addMap(packageAliasMapBuilder); // 添加映射关系。
		} //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

		byte[] serializedContent=translateRequestMessage.build().toByteArray(); //序列化成字节数组。

		File photoFile=findPackageItemAliasMapFile(); //寻找映射文件。

		try
		{
          FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
		}
		catch (IOException e)
		{
          e.printStackTrace();
		}
	} //public void savePackageItemAliasMap() //保存映射。
	
	/**
	* 增加应用锁。
	*/
	public void addApplicationLock(String packagename) 
	{
      if (applicationLockSet==null) // 集合不存在。
      {
        applicationLockSet=new HashSet<>(); // 创建对象。
      } // if (applicationLockSet==null) // 集合不存在。
	
      applicationLockSet.add(packagename); // 向集合中增加。
      
      saveApplicationLockSet(); // 保存应用锁集合。
	} // public void addApplicationLock(String packagename)
	
	/**
	* 删除应用锁。
	*/
	public void removeApplicationLock(String packagename)
	{
      applicationLockSet.remove(packagename); // 从集合中删除。
      
      saveApplicationLockSet(); // 保存应用锁集合。
	} // public void removeApplicationLock(String packagename)
	
	/**
	* 保存应用锁集合。
	*/
	private void saveApplicationLockSet() 
	{
      // 	陈欣

      MessageFactory messageFactory=new MessageFactory(); // 创建消息工厂。

      byte[] serializedContent=messageFactory.constructApplicationLockMessage(applicationLockSet); //序列化成字节数组。

      File photoFile=findApplicationLockFile(); // 寻找锁集合文件。

      try
      {
        FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
			
        Log.d(TAG, "saveApplicationLockSet, content: " + serializedContent.toString()); // Debug.
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
	} // private void saveApplicationLockSet()

	/**
	 * 保存映射。包条目字符串，与安装包文件路径之间的映射。
	 */
	public void saveApkFilePathMap()
	{
    CBORObject cborObject= CBORObject.NewMap(); //创建对象
        
		for(String currentVoiceRecognizeResult: apkFilePathMap.keySet()) //一个个地保存。
		{
			String currentPackageName=apkFilePathMap.get(currentVoiceRecognizeResult); //获取包名。

      cborObject.Add(currentVoiceRecognizeResult, currentPackageName); // 加入映射中。
		} //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

    byte[] array=cborObject.EncodeToBytes();

		byte[] serializedContent=array; //序列化成字节数组。

		File photoFile=findApkFilePathMapFile(); //寻找映射文件。

		try
		{
			FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
			
			Log.d(TAG, "saveApkFilePathMap, content: " + serializedContent.toString()); // Debug.
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	} //public void savePackageItemAliasMap() //保存映射。
	
	/**
	* 获取 APK 安装包路径映射。
	*/
	public HashMap<String, String> getApkFilePathMap()
	{
    if (apkFilePathMap==null)
    {
        loadApkFilePathMap(); // 载入映射。
    } //if (apkFilePathMap==null)
    
    return apkFilePathMap;
	} //public void HashMap<String, String> getApkFilePathMap()
	
	/**
	* 载入映射。
	*/
	private void loadApkFilePathMap() 
	{
    CBORObject result=null; //结果。

    byte[] payloadData=null; //获取负载字节数组。

    File apkFilePathMapFile=findApkFilePathMapFile(); //寻找映射文件。
    
    apkFilePathMap=new HashMap<>(); // 创建映射。

    try
    {
      payloadData=FileUtils.readFileToByteArray(apkFilePathMapFile); // 载入内容。
      
      Log.d(TAG, "loadApkFilePathMap, payloadData: " + payloadData.toString()); // Debug.
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    //解析消息：
    try
    {
      CBORObject videoStreamMessage= CBORObject.DecodeFromBytes(payloadData); //解析消息。
      
      java.util.Collection<java.util.Map.Entry<CBORObject,CBORObject>> entries=  videoStreamMessage.getEntries(); // 获取条目列表。
      
      for(java.util.Map.Entry<CBORObject,CBORObject> entry: entries) // 一个个地加入。
      {
        CBORObject keyObject=entry.getKey();
        CBORObject valueObject=entry.getValue();
        
        apkFilePathMap.put(keyObject.AsString(), valueObject.AsString()); // 加入映射。
      } //for(entry: entries) // 一个个地加入。
    }
    catch (CBORException e)
    {
      e.printStackTrace(); // 报告错误。
    }
	} //private void loadApkFilePathMap()

	/**
	 * 保存映射。包条目字符串，与实际冷却时间之间的映射。
	 */
	public void savePackageItemLaunchCoolDownMap()
	{
		PackageItemLaunchCoolDownMapMessageProtos.PackageItemLaunchCoolDownMapMessage.Builder translateRequestMessage= PackageItemLaunchCoolDownMapMessageProtos.PackageItemLaunchCoolDownMapMessage.newBuilder(); //创建一个消息对象。

		for(String currentVoiceRecognizeResult: packageItemLaunchCoolDownMap.keySet()) //一个个地保存。
		{
			Integer currentPackageName=packageItemLaunchCoolDownMap.get(currentVoiceRecognizeResult); //获取包名。

			Log.i(TAG,"savePackageNameAutoRunMap, package name: "+currentVoiceRecognizeResult+ ", auto run: " + currentPackageName); //Debug.


			PackageItemLaunchCoolDownMapItemMessageProtos.PackageItemLaunchCoolDownMapItemMessage.Builder translateRequestMessageBuilder= PackageItemLaunchCoolDownMapItemMessageProtos.PackageItemLaunchCoolDownMapItemMessage.newBuilder();

			translateRequestMessageBuilder.setLaunchCoolDown(currentPackageName).setPackageItemName(currentVoiceRecognizeResult); //设置字段。

			translateRequestMessage.addMap(translateRequestMessageBuilder); //添加映射关系。
		} //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

		byte[] serializedContent=translateRequestMessage.build().toByteArray(); //序列化成字节数组。

		File photoFile=findRandomPhotoFile(); //随机寻找一个照片文件。

		try
		{
			FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	} //public void savePackageItemLaunchCoolDownMap()
	
	/**
	* 寻找锁集合文件。
	*/
	private File findApplicationLockFile() 
	{
      File result=null;

      File filesDir=getFilesDir();

      if (filesDir==null) //该目录不存在。
      {
      } //if (filesDir==null) //该目录不存在。
      else //该目录存在。
      {
        result=new File(filesDir.getAbsolutePath()+"/applicationLock.rties"); //指定文件名。

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
	} // private File findApplicationLockFile()
	
	/**
	*  寻找映射文件。 包名与安装包路径之间的映片。陈欣。
	*/
	private File findApkFilePathMapFile()
	{
    File result=null;

    File filesDir=getFilesDir();

    if (filesDir==null) //该目录不存在。
    {
    } //if (filesDir==null) //该目录不存在。
    else //该目录存在。
    {
      result=new File(filesDir.getAbsolutePath()+"/apkFilePathMap.cx.d"); //指定文件名。

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
	} //private File findApkFilePathMapFile() //寻找映射文件。

	/**
	 * 寻找映射文件。包条目信息与别名之间的映射。
	 * @return 映射文件。包条目信息与别名之间的映射。
	 */
	private File findPackageItemAliasMapFile()
	{
		File result=null;

		File filesDir=getFilesDir();

		Log.d(TAG, "findRandomPhotoFile, files dir: "+ filesDir); //Debug.

		if (filesDir==null) //该目录不存在。
		{

		} //if (filesDir==null) //该目录不存在。
		else //该目录存在。
		{
			result=new File(filesDir.getAbsolutePath()+"/packageItemAliasMap.oeo"); //指定文件名。
//            R

			if (result.exists()) //文件存在。
			{

			} //if (result.exists()) //文件存在。
			else //文件不存在。
			{
				try
				{
					boolean createResult=result.createNewFile(); //创建文件。

					Log.d(TAG, "findRandomPhotoFile, create file result: " + createResult); //Debug.

				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			} //else //文件不存在。
		} //else //该目录存在。

		return result;
	} //private File findPackageItemAliasMapFile()

	/**
	 * 随机寻找一个照片文件。
	 * @return 随机寻找的一个照片文件。
	 */
	private  File findRandomPhotoFile()
	{
		File result=null;

		File filesDir=getFilesDir();

		Log.d(TAG, "findRandomPhotoFile, files dir: "+ filesDir); //Debug.

		if (filesDir==null) //该目录不存在。
		{

		} //if (filesDir==null) //该目录不存在。
		else //该目录存在。
		{
			result=new File(filesDir.getAbsolutePath()+"/packageItemLaunchCoolDownMap.off"); //指定文件名。
//            R

			if (result.exists()) //文件存在。
			{

			} //if (result.exists()) //文件存在。
			else //文件不存在。
			{
				try
				{
					boolean createResult=result.createNewFile(); //创建文件。

					Log.d(TAG, "findRandomPhotoFile, create file result: " + createResult); //Debug.

				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			} //else //文件不存在。
		} //else //该目录存在。

		return result;
	} //private  File findRandomPhotoFile()

} //public class SimoApp extends Application

