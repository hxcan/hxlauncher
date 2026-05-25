package com.stupidbeauty.hxlauncher.asynctask;

import com.upokecenter.cbor.CBORException;
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import com.stupidbeauty.hxlauncher.bean.VoicePackageUrlMapData;
import com.stupidbeauty.hxlauncher.bean.WakeLockPackageNameSetData;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.hxlauncher.encryption.CryptoHandler;
import com.stupidbeauty.hxlauncher.encryption.MessageEncryptor;
import com.upokecenter.cbor.CBORObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import android.annotation.SuppressLint;
import com.stupidbeauty.hxlauncher.bean.VoicePackageUrlMapData;
import java.util.Collection;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.asynctask.VoiceShortcutAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import com.stupidbeauty.victoriafresh.VFile;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoadApplicationLockSetTask extends AsyncTask<Object, Void, Object>
{
  private static final String TAG="LoadVoicePackageUrlMapTask"; //!< 输出调试信息时使用的标记。
  private VoicePackageUrlMapData voicePackageUrlMapData; //!<语音识别结果与软件包下载地址之间的映射。

  private HashMap<String, String> voicePackageUrlMap; //!<语音识别结果与包名之间的映射关系。
  private HashMap<String, String> packageNameUrlMap; //!<包名与下载地址之间的映射关系。
  private HashMap<String, String> packageNameInformationUrlMap; //!<包名与信息页面地址之间的映射关系。
  private HashMap<String, String> packageNameVersionNameMap; //!< 包名与可用版本号之间的映射关系。
  private  HashMap<String, String > packageNameApplicationNameMap; //!<包名与应用程序名的映射
  private HxLauncherApplication launcherActivity=null; //!< 启动活动。
  private HashSet<String> applicationLockSet=null; //!< 应用锁集合。


  @Override
  protected Object doInBackground(Object... params)
  {
    Boolean result=false; //结果，是否成功。

    launcherActivity=(HxLauncherApplication)(params[0]); //获取映射对象
          
//     loadVoicePackageUrlMapCbor(); // 载入语音识别结果与下载网址之间的映射。使用CBOR。陈欣。
    loadApplicationLockSet(); // 载入应用锁集合。
          
    boolean addPhotoFile=false; //Whether to add photo file

    return applicationLockSet;
  } //protected Object doInBackground(Object... params)
  
  /**
  * 寻找映射文件。包条目信息与别名之间的映射。
  * @return 映射文件。包条目信息与别名之间的映射。
  */
  private File findPackageItemAliasMapFile()
  {
    File result=null;

    File filesDir=launcherActivity.getFilesDir();

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
          result.createNewFile(); //创建文件。
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
  * 载入应用锁集合。
  */
  private void loadApplicationLockSet() 
  {
    applicationLockSet=new HashSet<>(); // 创建对象。

    File photoFile=findPackageItemAliasMapFile(); //寻找映射文件。
    
    try // 读取文件内容，并捕获异常。
    {
      byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。

      CBORObject videoStreamMessage= CBORObject.DecodeFromBytes(photoBytes); //解析消息。
        
      //         陈欣
    
      Collection<CBORObject> subFilesList=videoStreamMessage.getValues();

      voicePackageUrlMap=new HashMap<>(); //创建映射。
      packageNameUrlMap=new HashMap<>(); //创建映射
      packageNameInformationUrlMap=new HashMap<>(); // 创建映射。
      packageNameVersionNameMap=new HashMap<>(); // 创建映射。陈欣
      packageNameApplicationNameMap=new HashMap<>(); //创建映射

      for (CBORObject currentSubFile: subFilesList) //一个个子文件地比较其文件名。
      {
        String voiceCommand=currentSubFile.AsString();
                
        applicationLockSet.add(voiceCommand); // 加入集合。
      } //for (FileMessageContainer.FileMessage currentSubFile:videoStreamMessage.getSubFilesList()) //一个个子文件地比较其
    } //try // 读取文件内容，并捕获异常。
    catch (IOException e)
    {
      e.printStackTrace();
    } //catch (IOException e)
    catch (CBORException e) // 解码异常。
    {
      e.printStackTrace();
    } //catch (IOException e)
  } // private void loadApplicationLockSet()
  
  /**
  * 报告结果。
  * @param result 结果。是否成功。
  */
  @Override
  protected void onPostExecute(Object result)
  {
    launcherActivity.setApplicationLockSet(applicationLockSet);
  } //protected void onPostExecute(Boolean result)
  
  /**
  *  载入语音识别结果与下载网址之间的映射。使用CBOR。陈欣。
  */
  private void  loadVoicePackageUrlMapCbor()
  {
    String qrcFileName="voicePackageUrlMap.cbor.cx"; //文件名。
    String fullQrcFileName=":/VoicePackageUrlMapInternationalization/"+qrcFileName; //构造完整的qrc文件名。

    VFile qrcHtmlFile=new VFile(launcherActivity, fullQrcFileName); //qrc网页文件。

    byte[] photoBytes= qrcHtmlFile.getFileContent(); //将照片文件内容全部读取。
        
    CBORObject videoStreamMessage= CBORObject.DecodeFromBytes(photoBytes); //解析消息。
        
    //         陈欣

    Collection<CBORObject> subFilesList=videoStreamMessage.get("voicePackageMapJsonItemList").getValues();
        
    voicePackageUrlMap=new HashMap<>(); //创建映射。
    packageNameUrlMap=new HashMap<>(); //创建映射
    packageNameInformationUrlMap=new HashMap<>(); // 创建映射。
    packageNameVersionNameMap=new HashMap<>(); // 创建映射。陈欣
    packageNameApplicationNameMap=new HashMap<>(); //创建映射

    for (CBORObject currentSubFile: subFilesList) //一个个子文件地比较其文件名。
    {
      String voiceCommand=currentSubFile.get("voiceCommand").AsString();
      String packageUrl=currentSubFile.get("packageUrl").AsString();
      String packageName=currentSubFile.get("packageName").AsString();
      String informationUrl=currentSubFile.get("informationUrl").AsString(); // 获取信息页面地址。
            
      CBORObject versionNameObject=currentSubFile.get("versionName");
            
      if (versionNameObject!=null)
      {
        String versionName=versionNameObject.AsString();

        packageNameVersionNameMap.put(packageName, versionName); // 加入映射。
      } //versionNameObject
                
      voicePackageUrlMap.put(voiceCommand, packageUrl); //加入映射。
      packageNameUrlMap.put(packageName, packageUrl); //加入映射。
      packageNameApplicationNameMap.put( packageName, voiceCommand); //加入映射，包名与应用程序名的映射
      packageNameInformationUrlMap.put(packageName, informationUrl); // 加入映射，包名与信息页面地址的映射。
    } //for (FileMessageContainer.FileMessage currentSubFile:videoStreamMessage.getSubFilesList()) //一个个子文件地比较其
  } //private void  loadVoicePackageUrlMapCbor()
}
