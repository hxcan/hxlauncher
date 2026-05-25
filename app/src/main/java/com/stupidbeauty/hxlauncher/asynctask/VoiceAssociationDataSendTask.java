package com.stupidbeauty.hxlauncher.asynctask;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import com.stupidbeauty.hxlauncher.VoiceCommandHitDataMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQPassword;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQUserName;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.TRANSLATE_REQUEST_QUEUE_NAME;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.VoiceAssociationDataQueueName;

public class VoiceAssociationDataSendTask extends AsyncTask<Void, Void, Boolean>
{
    public void setLauncherActivity(LauncherActivity launcherActivity) {
        this.launcherActivity = launcherActivity;
    }

    private LauncherActivity launcherActivity=null; //!<启动活动。
    private static final String TAG="VoiceAssociationDataSen"; //!<输出调试信息时使用的标记。

    @Override
    protected Boolean doInBackground(Void... params)
    {
        Boolean result=false; //结果，是否成功。

        //使用protobuf将各个字段序列化成字节数组，然后使用rabbitmq发送到服务器。

        File photoFile=findRandomPhotoFile(); //随机寻找一个照片文件。

        byte[] photoBytes=null; //数据内容。

        try //尝试构造请求对象，并且捕获可能的异常。
        {
            ByteString photoByteArray=null; //照片的字节数组。

            if (photoFile!=null) //找到了照片文件。
            {
                photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。
            } //if (photoFile!=null) //找到了照片文件。
        } //try //尝试构造请求对象，并且捕获可能的异常。
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try //使用RabbitMQ发送，并捕获可能的异常。
        {
            byte[] array=photoBytes; //序列化成字节数组。

            //使用RabbitMQ来发送：
            ConnectionFactory factory=new ConnectionFactory(); //创建连接工厂。
            factory.setHost("stupidbeauty.com"); //设置主机。
            factory.setUsername(RabbitMQUserName); //设置用户名。
            factory.setPassword(RabbitMQPassword); //设置密码。
            Connection connection=factory.newConnection(); //创建连接。
            Channel channel=connection.createChannel(); //创建频道。

            Boolean durableTrue=true; //持久的。

            Boolean exclusiveFalse=false; //非排他性的。
            Boolean autoDeleteFalse=false; //不要自动删除。

            channel.queueDeclare(VoiceAssociationDataQueueName, durableTrue, exclusiveFalse, autoDeleteFalse, null); //声明队列。

            String exchange=""; //交换机。
            String routingKey=VoiceAssociationDataQueueName; //路由键。锓

            byte[] byteArrayBody=array; //消息体。

            channel.basicPublish(exchange,routingKey, MessageProperties.PERSISTENT_BASIC,byteArrayBody);

            String contentString=new String(byteArrayBody, StandardCharsets.UTF_8); //转换成字符串。

//            Log.d(TAG,"doInBackground,published translate request, content: "+ contentString); //Debug.

            result=true; //成功。
        } //try //使用RabbitMQ发送，并捕获可能的异常。
        catch ( Exception e) {
            e.printStackTrace();

            Log.d(TAG,"Exception:"+e.getMessage()); //报告错误。
        }

        return result;
    }


    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    private  File findRandomPhotoFile()
    {
        File result=null;

        File filesDir=launcherActivity.getFilesDir();

        Log.d(TAG, "findRandomPhotoFile, files dir: "+ filesDir); //Debug.

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

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
    @Override
    protected void onPostExecute(Boolean result)
    {
        launcherActivity.setSendVoiceAssociationDataResult(result); //设置结果，发送语音关联数据的结果。

    } //protected void onPostExecute(Boolean result)

}




