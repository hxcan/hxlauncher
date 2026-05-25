package com.stupidbeauty.hxlauncher.asynctask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import android.view.View;
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import com.andexert.library.RippleView;
import com.stupidbeauty.hxlauncher.AndroidApplicationMessage;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;

public class LoadVoicePackageNameMapTask extends AsyncTask<Object, Void, Object>
{
    private SetValuedMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的

	private static final String TAG="LoadVoicePackageNameMapTask"; //!<输出调试信息时使用的标记。

	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
    /**
     * 寻找语音识别与软件包映射文件。
     * @return 语音识别与软件包映射文件。
     */
    private  File findVoicePackageMapFile()
    {
      File result=null;

      File filesDir= launcherActivity.getFilesDir();

      if (filesDir==null) //该目录不存在。
      {
      } //if (filesDir==null) //该目录不存在。
      else //该目录存在。
      {
        result=new File(filesDir.getAbsolutePath()+"/voicePackageNameMap.proto"); //指定文件名。

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
    } //private  File findRandomPhotoFile()

	    /**
     * 载入语音识别结果与包名之间的映射。
     */
    private void loadVoicePackageNameMap()
    {
      File photoFile= findVoicePackageMapFile(); //随机寻找一个照片文件。

      voicePackageNameMap=new HashSetValuedHashMap<>(); //创建映射。

      if (photoFile!=null) //不是空指针。
      {
        if (photoFile.exists()) //文件存在。
        {
          try
          {
            byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。

            VoicePackageMapMessageProtos.VoicePackageMapMessage translateRequestMessage=VoicePackageMapMessageProtos.VoicePackageMapMessage.parseFrom(photoBytes); //创建一个消息对象。

            List<VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage> relationships=translateRequestMessage.getMapList(); //获取关系列表。

            for(VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage currentRelationship: relationships) //一个个地加入映射中。
            {
              PackageItemInfo currentPackageItemInfo=new PackageItemInfo(); //当前的包条目信息对象。

              currentPackageItemInfo.packageName=currentRelationship.getPackageName();
              currentPackageItemInfo.name=currentRelationship.getActivityName(); //记录活动名字。

              voicePackageNameMap.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfo); //加入映射。

              List<AndroidApplicationMessage> applicationMessages=currentRelationship.getApplicationInformationList(); //获取多映射目标列表

              for(AndroidApplicationMessage currentApplicationMessage: applicationMessages) //一个个地加入映射目标
              {
                PackageItemInfo currentPackageItemInfoA=new PackageItemInfo(); //当前的包条目信息对象。

                currentPackageItemInfoA.packageName=currentApplicationMessage.getPackageName();
                currentPackageItemInfoA.name=currentApplicationMessage.getActivityName(); //记录活动名字。

                voicePackageNameMap.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfoA); //加入映射。
              } //for(AndroidApplicationMessage currentApplicationMessage: applicationMessages) //一个个地加入映射目标
            } //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。
          }
          catch (IOException e)
          {
            e.printStackTrace();
          } //catch (IOException e)
        } //if (photoFile.exists()) //文件存在。
      } //if (photoFile!=null) //不是空指针。
    } //private void loadVoicePackageNameMap()

    @Override
    protected Object doInBackground(Object... params)
    {
      //参数顺序：
      //            private MultiMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
      //            voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, iconType, iconTitle

      Boolean result=false; //结果，是否成功。

      //            String subject=(String)(params[0]); //获取识别结果文字内容。
      //             SetValuedMap<String, PackageItemInfo> voicePackageNameMap=(SetValuedMap<String, PackageItemInfo>)(params[0]); //获取映射对象
      launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
        
      loadVoicePackageNameMap(); // 载入映射。
        
      //             buildInternationalizationDataPackageNameMap(); // 构造映射。

      boolean addPhotoFile=false; //Whether to add photo file

      return voicePackageNameMap;
    }

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Object result)
        {
            launcherActivity.setVoicePackageNameMap(voicePackageNameMap);
		} //protected void onPostExecute(Boolean result)
	}
