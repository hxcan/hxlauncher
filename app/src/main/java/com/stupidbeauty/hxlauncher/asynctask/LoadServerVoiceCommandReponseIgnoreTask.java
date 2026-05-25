package com.stupidbeauty.hxlauncher.asynctask;

import android.view.View;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.bean.HxShortcutInfo;
import com.stupidbeauty.hxlauncher.callback.LauncherAppsCallback;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import com.stupidbeauty.hxlauncher.datastore.VoiceCommandSourceType;
import android.view.View;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import android.content.pm.PackageItemInfo;

import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LoadServerVoiceCommandReponseIgnoreTask extends AsyncTask<Object, Void, Object>
{
    private HashMap<String, String> serverVoiceCommandResponseIgnoreMap=new HashMap<>(); //!<服务器的回复中，要忽略掉的关系映射

	private static final String TAG="LoadServerVoiceCommandReponseIgnoreTask"; //!<输出调试信息时使用的标记。

	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
	    /**
     * 寻找数据文件。要忽略的服务器回复映射
     * @return 寻找到的数据文件
     */
    private File findServerVoiceCommandReponseIgnoreMapFile()
    {
        File result=null;

        File filesDir= launcherActivity.getFilesDir();

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
     * 载入映射，对于服务器的回复中，要忽略掉的识别结果映射关系
     */
    private void loadServerVoiceCommandReponseIgnore()
    {
      File photoFile=findServerVoiceCommandReponseIgnoreMapFile(); //寻找数据文件。

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
              //                        voicePackageNameMap.put(currentRelationship.getVoiceRecognizeResult(), currentRelationship.getPackageName()); //加入映射。

              //                        HxShortcutInfo currentPackageItemInfo=new HxShortcutInfo(); //当前的包条目信息对象。

                        String currentPackageItemInfopackageName=currentRelationship.getPackageName();
//                        currentPackageItemInfo.shortcutId=currentRelationship.getShortcutId(); //记录快捷方式编号。

                        serverVoiceCommandResponseIgnoreMap.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfopackageName); //加入映射。


                    } //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                } //catch (IOException e)
            } //if (photoFile.exists()) //文件存在。
        } //if (photoFile!=null) //不是空指针。


    } //private void loadServerVoiceCommandReponseIgnore()

    @Override
    protected Object doInBackground(Object... params)
    {
      Boolean result=false; //结果，是否成功。

      launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
        
      loadServerVoiceCommandReponseIgnore(); // 载入映射
        
      boolean addPhotoFile=false; //Whether to add photo file

      return serverVoiceCommandResponseIgnoreMap;
    }

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
    @Override
    protected void onPostExecute(Object result)
    {
      launcherActivity.setServerVoiceCommandResponseIgnoreMap(serverVoiceCommandResponseIgnoreMap);
    } //protected void onPostExecute(Boolean result)
  }
