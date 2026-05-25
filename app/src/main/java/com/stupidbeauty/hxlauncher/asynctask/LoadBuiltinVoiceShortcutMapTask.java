package com.stupidbeauty.hxlauncher.asynctask;

import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.comgooglewidevinesoftwaredrmremover.app.LanImeUncaughtExceptionHandler;
import com.stupidbeauty.grebe.DownloadRequestor;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.victoriafresh.VFile;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import com.stupidbeauty.hxlauncher.BuildConfig;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
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
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import android.content.pm.PackageItemInfo;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;

public class LoadBuiltinVoiceShortcutMapTask extends AsyncTask<Object, Void, Object>
{
    private HashMap<String, HxShortcutInfo> voiceShortcutIdMapBuiltin=new HashMap<>(); //!<语音识别结果与快捷方式编号之间的映射关系．

	private static final String TAG="LoadBuiltinVoiceShortcutMapTask"; //!<输出调试信息时使用的标记。

	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
    /**
     * 载入内置的语音识别结果与快捷方式信息之间的映射。
     */
    private void loadBuiltinVoiceShortcutMap()
    {
      int historyVoicePackageNampVersion=PreferenceManagerUtil.getVoiceShortCutMapVersion(); //获取历史数据的版本号

      if (historyVoicePackageNampVersion < BuildConfig.VERSION_CODE) //历史数据版本号较低
      {
        try 
        {
          String qrcFileName = "voiceShortMap.ost"; //文件名。
          String fullQrcFileName = ":/VoicePackageItemMap/" + qrcFileName; //构造完整的qrc文件名。

          VFile qrcHtmlFile = new VFile(launcherActivity, fullQrcFileName); //qrc网页文件。

          byte[] photoBytes = qrcHtmlFile.getFileContent(); //将照片文件内容全部读取。

          VoicePackageMapMessageProtos.VoicePackageMapMessage translateRequestMessage = VoicePackageMapMessageProtos.VoicePackageMapMessage.parseFrom(photoBytes); //创建一个消息对象。

          List<VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage> relationships = translateRequestMessage.getMapList(); //获取关系列表。

          for (VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage currentRelationship : relationships) //一个个地加入映射中。
          {
            HxShortcutInfo currentPackageItemInfo = new HxShortcutInfo(); //当前的包条目信息对象。

                    currentPackageItemInfo.packageName = currentRelationship.getPackageName();
                    currentPackageItemInfo.shortcutId = currentRelationship.getShortcutId(); //记录快捷方式编号。

                    voiceShortcutIdMapBuiltin.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfo); //加入映射。
                } //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。
            } catch (IOException e) {
                e.printStackTrace();
            } //catch (IOException e)
        }
    } //private void loadBuiltinVoiceShortcutMap()

	
	    /**
     * 寻找数据记录文件。
     * @return 数据记录文件．语音识别结果与快捷方式之间的映射文件．
     */
    private File findVoiceShortcutIdMapFile()
    {
        File result=null;

        File filesDir= launcherActivity.getFilesDir();

        if (filesDir==null) //该目录不存在。
        {

        } //if (filesDir==null) //该目录不存在。
        else //该目录存在。
        {
            result=new File(filesDir.getAbsolutePath()+"/voiceShortcutIdMap.etn"); //指定文件名。

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
    } //private File findVoiceShortcutIdMapFile()

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

    @Override
    protected Object doInBackground(Object... params)
    {
      launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
            
      loadBuiltinVoiceShortcutMap(); // 载入映射。
            
      return voiceShortcutIdMapBuiltin;
    }

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
    @Override
    protected void onPostExecute(Object result)
    {
      launcherActivity.setVoiceShortcutIdMapBuiltin(voiceShortcutIdMapBuiltin);
    } //protected void onPostExecute(Boolean result)
  }
