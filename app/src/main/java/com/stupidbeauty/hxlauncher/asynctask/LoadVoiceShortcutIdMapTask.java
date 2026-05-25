// Generated code from Butter Knife. Do not modify!
package com.stupidbeauty.hxlauncher.asynctask;

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

public class LoadVoiceShortcutIdMapTask extends AsyncTask<Object, Void, Object>
{
//     private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。
//     private SetValuedMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的

    private HashMap<String, HxShortcutInfo> voiceShortcutIdMap=new HashMap<>(); //!<语音识别结果与快捷方式编号之间的映射关系．

	private static final String TAG="BuildInternationalizationDataPackageNameMapTask"; //!<输出调试信息时使用的标记。

	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
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
     * 载入语音识别结果与快捷方式之间的映射。
     */
    private void loadVoiceShortcutIdMap()
    {
        File photoFile=findVoiceShortcutIdMapFile(); //寻找数据文件。

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
                        HxShortcutInfo currentPackageItemInfo=new HxShortcutInfo(); //当前的包条目信息对象。

                        currentPackageItemInfo.packageName=currentRelationship.getPackageName();
                        currentPackageItemInfo.shortcutId=currentRelationship.getShortcutId(); //记录快捷方式编号。

                        voiceShortcutIdMap.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfo); //加入映射。


                    } //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                } //catch (IOException e)
            } //if (photoFile.exists()) //文件存在。
        } //if (photoFile!=null) //不是空指针。

    } //private void loadVoiceShortcutIdMap()

	    /**
     * 寻找语音识别与软件包映射文件。
     * @return 语音识别与软件包映射文件。
     */
    @SuppressWarnings("StatementWithEmptyBody")
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
            //参数顺序：
//            private MultiMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
//            voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, iconType, iconTitle

            Boolean result=false; //结果，是否成功。

//            String subject=(String)(params[0]); //获取识别结果文字内容。
//             SetValuedMap<String, PackageItemInfo> voicePackageNameMap=(SetValuedMap<String, PackageItemInfo>)(params[0]); //获取映射对象
            launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
            
//             loadVoicePackageNameMap(); // 载入映射。
            
            loadVoiceShortcutIdMap(); // 载入映射。
            
//             buildInternationalizationDataPackageNameMap(); // 构造映射。

            boolean addPhotoFile=false; //Whether to add photo file

            return voiceShortcutIdMap;
		}

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Object result)
        {
            launcherActivity.setVoiceShortcutIdMap(voiceShortcutIdMap);
		} //protected void onPostExecute(Boolean result)
	}
