package com.stupidbeauty.hxlauncher.asynctask;

import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.commons.collections4.SetValuedMap;
import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.stupidbeauty.farmingbookapp.PreferenceManagerUtil;
import com.stupidbeauty.hxlauncher.AndroidApplicationMessage;
import com.stupidbeauty.hxlauncher.BuildConfig;
import com.stupidbeauty.hxlauncher.VoiceCommandHitDataMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import java.util.Set;
import java.util.HashSet;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQPassword;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQUserName;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.TRANSLATE_REQUEST_QUEUE_NAME;
import static com.stupidbeauty.hxlauncher.HxLauncherIconType.PbActivityIconType;
import static com.stupidbeauty.hxlauncher.HxLauncherIconType.PbShortcutIconType;

/**
 * @author Hxcan
 * @since Mar 13, 2014
 */
public final class VoicePackageNameMapSaveTask extends AsyncTask<Object, Void, Boolean>
{

	private static final String TAG="VoicePackageNameMapSaveTask"; //!< 输出调试信息时使用的标记。

		@Override
		protected Boolean doInBackground(Object... params)
        {
            //参数顺序：
//            private MultiMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
//            voiceRecognizeResultString, packageName, activityName, recordSoundFilePath, iconType, iconTitle

            Boolean result=false; //结果，是否成功。

            SetValuedMap<String, PackageItemInfo> voicePackageNameMap=(SetValuedMap<String, PackageItemInfo>)(params[0]); //获取映射对象

            VoicePackageMapMessageProtos.VoicePackageMapMessage.Builder translateRequestMessage= VoicePackageMapMessageProtos.VoicePackageMapMessage.newBuilder(); // 创建一个消息对象。

            for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) // 一个个地保存。
            {
              Collection<PackageItemInfo> coll = (Collection<PackageItemInfo>) voicePackageNameMap.get(currentVoiceRecognizeResult);
//            PackageItemInfo currentPackageItemInformation=voicePackageNameMap.get(currentVoiceRecognizeResult); //

              VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage.Builder translateRequestMessageBuilder= VoicePackageMapItemMessageProtos.VoicePackageMapItemMessage.newBuilder();

                translateRequestMessageBuilder.setVoiceRecognizeResult(currentVoiceRecognizeResult); //设置各个字段。
//            .setPackageName(currentPackageName).setActivityName(currentActivityName)
//            translateRequestMessageBuilder.setIconType(PbActivityIconType); //设置图标类型．

                Set<String> displaynameSet=new HashSet<>(); // 显示名字集合。

                for(PackageItemInfo currentPackageInformation: coll) //一个个包地添加
                {
                    String currentPackageName=currentPackageInformation.packageName; //获取包名。
                    String currentActivityName=currentPackageInformation.name; //获取活动名。

                    String displayname=currentPackageName + "/" + currentActivityName; // 构造显示名字。
                    
                    if (displaynameSet.contains(displayname)) // 已在集合中。
                    {
                    } //if (displaynameSet.contains(displayname)) // 已在集合中。
                    else // 不在集合中。
                    {
                      AndroidApplicationMessage.Builder currentApplicationInformation=AndroidApplicationMessage.newBuilder();

                    currentApplicationInformation.setPackageName(currentPackageName);
                    currentApplicationInformation.setActivityName(currentActivityName);
                    currentApplicationInformation.setIconType(PbActivityIconType);

                    translateRequestMessageBuilder.addApplicationInformation(currentApplicationInformation); //添加一个映射目标。
                    
                    displaynameSet.add(displayname); // 加入集合中。
                  } // else // 不在集合中。
                } //for(PackageItemInfo currentPackageInformation: coll) //一个个包地添加

                translateRequestMessage.addMap(translateRequestMessageBuilder); // 添加映射关系。
            } //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

            boolean addPhotoFile=false; //Whether to add photo file

            if (addPhotoFile) //Should add photo file
            {
                //放入一张照片：
                //随机选择一张照片并复制：
                File photoFileDcim=findRandomPhotoFileDcim(); // 随机寻找一个照片文件。

                try // 尝试构造请求对象，并且捕获可能的异常。
                {
                    ByteString photoByteArray=null; //照片的字节数组。

                    if (photoFileDcim!=null) //找到了照片文件。
                    {
                      byte[] photoBytes= FileUtils.readFileToByteArray(photoFileDcim); // 将照片文件内容全部读取。
                        photoByteArray=ByteString.copyFrom(photoBytes); //构造照片的字节字符串。
                    } // if (photoFile!=null) //找到了照片文件。

                    long eventTimeStamp=System.currentTimeMillis(); //获取时间戳。

//            String photoFileName=photoFile.getName(); //获取文件名。

                    translateRequestMessage.setPictureFileContent(photoByteArray); //设置照片文件内容。
                } //try //尝试构造请求对象，并且捕获可能的异常。
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            } // if (addPhotoFile) //Should add photo file
            else  //Should not add photo file
            {
//            translateRequestMessage.clearPictureFileContent()
            } //else  //Should not add photo file

            byte[] serializedContent=translateRequestMessage.build().toByteArray(); //序列化成字节数组。

            File photoFile=findVoicePackageMapFile(); //寻找语音识别与软件包映射文件。

            try
            {
                FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            updateVoicePackageNameMapVersion(); // 更新语音命中应用映射数据文件的版本

            return result;
		}

    /**
     * 更新语音命中应用映射数据文件的版本
     */
    private void updateVoicePackageNameMapVersion()
    {
      PreferenceManagerUtil.setVoicePackageNameMapVersion(BuildConfig.VERSION_CODE); // 设置语音命中数据文件的版本号
    } //private void updateVoicePackageNameMapVersion()


    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    private  File findRandomPhotoFileDcim()
    {
        File result=null;

        String photoDirectoryPath= com.stupidbeauty.hxlauncher.Constants.DirPath.DCIM_SD_CARD_PATH; //照片目录路径。

        File photoDirecotry= new File(photoDirectoryPath); //照片目录。

        Log.d(TAG,"findRandomPhotoFile,photo directory:"+photoDirectoryPath); //Debug.

        IOFileFilter fileFilter= TrueFileFilter.INSTANCE; // 文件过滤器。

        IOFileFilter dirFilter= TrueFileFilter.INSTANCE; //文件过滤器。

        try //尝试列出文件，并且捕获可能的异常。
        {
          Collection<File> photoFiles= FileUtils.listFiles(photoDirecotry, fileFilter, dirFilter); // 列出全部文件。

            Random random=new Random(); //随机数生成器。

//            LogHelper.d(TAG,"findRandomPhotoFile,photo amount:"+photoFiles.size()); //Debug.

            if (photoFiles.size()>0) // 有照片文件。
            {
                int randomIndex=random.nextInt(photoFiles.size()); //随机选择一个文件。

                result=(File)((photoFiles.toArray())[randomIndex]); //选择指定的文件。

            } // if (photoFiles.size()>0) //有照片文件。


        } //try //尝试列出文件，并且捕获可能的异常。
        catch (IllegalArgumentException illegalArgumentException) //参数不符合要求。
        {
            illegalArgumentException.printStackTrace(); //输出调用栈。
        } //catch (IllegalArgumentException illegalArgumentException) //参数不符合要求。


        return result;
    } //private  File findRandomPhotoFile()

    /**
     * 寻找语音识别与软件包映射文件。
     * @return 语音识别与软件包映射文件。
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private  File findVoicePackageMapFile()
    {
        File result=null;

        Context context= HxLauncherApplication.getAppContext(); //获取上下文

        File filesDir=context.getFilesDir();

        Log.d(TAG, "1459, findRandomPhotoFile, files dir: "+ filesDir); //Debug.

        if (filesDir==null) //该目录不存在。
        {

        } //if (filesDir==null) //该目录不存在。
        else // 该目录存在。
        {
            result=new File(filesDir.getAbsolutePath()+"/voicePackageNameMap.proto"); //指定文件名。

            Log.d(TAG, "1469, findRandomPhotoFile, files exists: "+ result.exists() + ", size: " + result.length()); //Debug.

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
            } // else //文件不存在。
        } //else //该目录存在。

        return result;
    } //private  File findRandomPhotoFile()


    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    private  File findRandomPhotoFile()
    {
        File result=null;

        String photoDirectoryPath= com.stupidbeauty.hxlauncher.Constants.DirPath.DCIM_SD_CARD_PATH; //照片目录路径。

        File photoDirecotry= new File(photoDirectoryPath); //照片目录。

        IOFileFilter fileFilter= TrueFileFilter.INSTANCE; // 文件过滤器。

        IOFileFilter dirFilter= TrueFileFilter.INSTANCE; // 文件过滤器。

        try //尝试列出文件，并且捕获可能的异常。
        {
          Collection<File> photoFiles= FileUtils.listFiles(photoDirecotry, fileFilter, dirFilter); // 列出全部文件。

          Random random=new Random(); // 随机数生成器。

            Log.d(TAG,"findRandomPhotoFile,photo amount:"+photoFiles.size()); //Debug.

            if (photoFiles.size()>0) //有照片文件。
            {
              int randomIndex=random.nextInt(photoFiles.size()); // 随机选择一个文件。

                result=(File)((photoFiles.toArray())[randomIndex]); //选择指定的文件。

            } //if (photoFiles.size()>0) //有照片文件。


        } //try //尝试列出文件，并且捕获可能的异常。
        catch (IllegalArgumentException illegalArgumentException) //参数不符合要求。
        {
            illegalArgumentException.printStackTrace(); //输出调用栈。
        } //catch (IllegalArgumentException illegalArgumentException) //参数不符合要求。

        return result;
    } //private  File findRandomPhotoFile()

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Boolean result)
        {
		} //protected void onPostExecute(Boolean result)
	}



