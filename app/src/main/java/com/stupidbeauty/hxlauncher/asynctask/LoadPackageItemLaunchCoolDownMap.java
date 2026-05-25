package com.stupidbeauty.hxlauncher.asynctask;

import com.stupidbeauty.hxlauncher.PackageItemLaunchCoolDownMapItemMessageProtos;
import android.view.View;
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import java.util.HashMap;
import java.io.IOException;
import java.util.List;
import com.stupidbeauty.hxlauncher.PackageItemLaunchCoolDownMapMessageProtos;
import org.apache.commons.io.FileUtils;
import java.io.File;

public class LoadPackageItemLaunchCoolDownMap extends AsyncTask<Object, Void, Object>
{
	private HashMap<String, Integer> packageItemLaunchCoolDownMap=new HashMap<>(); //!<映射。包条目信息字符串与实际冷却时间秒数之间的映射。

	private static final String TAG="BuildInternationalizationDataPackageNameMapTask"; //!<输出调试信息时使用的标记。

	private HxLauncherApplication voicePackageNameMap=null; //!< 启动活动。
	
		/**
	 * 随机寻找一个照片文件。
	 * @return 随机寻找的一个照片文件。
	 */
	@SuppressWarnings("StatementWithEmptyBody")
	private  File findRandomPhotoFile()
	{
		File result=null;

		File filesDir=voicePackageNameMap.getFilesDir();

		if (filesDir==null) //该目录不存在。
		{
		} //if (filesDir==null) //该目录不存在。
		else //该目录存在。
		{
			result=new File(filesDir.getAbsolutePath()+"/packageItemLaunchCoolDownMap.off"); //指定文件名。

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
	private void loadPackageItemLaunchCoolDownMap()
	{
		File photoFile=findRandomPhotoFile(); //随机寻找一个照片文件。

		packageItemLaunchCoolDownMap=new HashMap<>(); //创建映射。

		if (photoFile!=null) //不是空指针。
		{
			if (photoFile.exists()) //文件存在。
			{
				try
				{
					byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。

					PackageItemLaunchCoolDownMapMessageProtos.PackageItemLaunchCoolDownMapMessage translateRequestMessage=PackageItemLaunchCoolDownMapMessageProtos.PackageItemLaunchCoolDownMapMessage.parseFrom(photoBytes); //创建一个消息对象。

					List<PackageItemLaunchCoolDownMapItemMessageProtos.PackageItemLaunchCoolDownMapItemMessage> relationships=translateRequestMessage.getMapList(); //获取关系列表。

					for(PackageItemLaunchCoolDownMapItemMessageProtos.PackageItemLaunchCoolDownMapItemMessage currentRelationship: relationships) //一个个地加入映射中。
					{
						packageItemLaunchCoolDownMap.put(currentRelationship.getPackageItemName(), currentRelationship.getLaunchCoolDown()); //加入映射。
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
            voicePackageNameMap=(HxLauncherApplication)(params[0]); //获取映射对象
            
            
//             buildInternationalizationDataPackageNameMap(); // 构造映射。

            loadPackageItemLaunchCoolDownMap(); // 载入冷却时间映射。

            boolean addPhotoFile=false; //Whether to add photo file

            return packageItemLaunchCoolDownMap;
		}

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Object result)
        {
            voicePackageNameMap.setPackageItemLaunchCoolDownMap(packageItemLaunchCoolDownMap);
		} //protected void onPostExecute(Boolean result)
	}
