package com.stupidbeauty.hxlauncher.rpc;

import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.lanime.network.volley.MapUtils;
import com.stupidbeauty.victoriafresh.VFile;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class DownloadCallback implements Callback
{
    Uri uri = null; //!< 请求的网址。
    private String packageName=null; //!< 包名。
    private SpeechRecognizer speechRecognizer=null; //!<语音识别器实例

    private Context context; //!<用于获取系统资源的上下文对象
    private static final String TAG="DownloadCallback"; //!< 输出调试信息时使用的标记
    private DownloadListener recognizerListener; //!< 下载监听器。陈欣
    
    public void setContext(Context context) 
    {
        this.context = context;
    }

    /**
    * 记录安装包路径。
    */
    private void  rememberApkFile(String apkFilePath) 
    {
        if (packageName!=null)
        {
            HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //获取应用程序对象。

            HashMap<String, String> apkFilePathMap=baseApplication.getApkFilePathMap(); // 获取 APK 安装包路径映射。
            
            Log.d(TAG, "rememberApkFile, package name: " + packageName + ", apk file path: " + apkFilePath); // Debug.
            
            apkFilePathMap.put(packageName, apkFilePath); // 加入映射中。
            
            baseApplication.saveApkFilePathMap(); // 保存映射。
        } //if (packageName!=null)
    } //private void  rememberApkFile(String apkFilePath)
    
    /**
    * 设置包名。
    */
    public void setPackageName(String packageName)
    {
        this.packageName=packageName;
    } //DownloadCallback
    
    public void setUri(Uri uri)
    {
        this.uri=uri;
    }

    public void setRecognizerListener(DownloadListener recognizerListener) 
    {
        this.recognizerListener = recognizerListener;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e)
    {
        Log.d(TAG, "33, onFailure, exception: "); //Debug.
        e.printStackTrace();

        String exceptionClassName = e.getClass().getName();

        Log.d(TAG, "40, onFailure, class name: " + exceptionClassName);

        int errorCode= ErrorCode.ERROR_UNKNOWN; //错误码

        if (exceptionClassName.equals("java.net.SocketTimeoutException")) //网络超时
        {
            errorCode=ErrorCode.ERROR_NETWORK_TIMEOUT; //错误码
        } //if (exceptionClassName.equals("java.net.SocketTimeoutException")) //网络超时
        else if (exceptionClassName.equals("java.net.ConnectException")) //网络连接失败
        {
            errorCode=ErrorCode.ERROR_NET_CONNECTSOCK; //网络连接失败
        } //else if (exceptionClassName.equals("java.net.ConnectException")) //网络连接失败
        else if (exceptionClassName.equals("java.net.UnknownServiceException")) //明文HTTP
        {
            errorCode=ErrorCode.ERROR_CLEARTEXT_HTTP_NOT_PERMITTED; //明文HTTP
        } //else if (exceptionClassName.equals("java.net.UnknownServiceException")) //明文HTTP
        else if (exceptionClassName.equals("java.net.UnknownHostException")) //网络不通
        {
            errorCode=ErrorCode.ERROR_NETWORK_UNREACHABLE; //未连接到网络
        } //else if (exceptionClassName.equals("java.net.UnknownServiceException")) //明文HTTP

        reportError(errorCode); // 报告错误。陈欣
    } //public void onFailure(@NotNull Call call, @NotNull IOException e)
    
    /**
    * 报告错误。陈欣
    */
    private void reportError(int errorCode)
    {
      Handler uiHandler = new Handler(Looper.getMainLooper());

      final int finalErrorCode = errorCode;

        Runnable runnable= new Runnable()
        {
            /**
             * 具体执行的代码
             */
            public void run()
            {
                SpeechError speechError=new SpeechError(finalErrorCode); //构造错误对象

                recognizerListener.onError(speechError); //报告错误
            } //public void run()
        };

        uiHandler.post(runnable);
    } //private void reportError(int errorCode)

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException 
    {
        boolean isSuccessful=response.isSuccessful(); // 获取结果，是否成功。
        
        if (isSuccessful) // 是请求成功。
        {
        InputStream byteStream= response.body().byteStream(); // 获取字节输入流。
        
        String fileName=uri.getLastPathSegment(); // 获取文件名。陈欣
            
        File downloadFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        String wholePath =downloadFolder.getPath()+ File.separator  + fileName;

        File apkFile=new File( wholePath);

        String apkFilePath=wholePath; // 文件路径。
        
        FileUtils.copyInputStreamToFile(byteStream, apkFile); // 写入到存储空间。

        Request request = call.request();
        
        HttpUrl url= request.url();

        Handler uiHandler = new Handler(Looper.getMainLooper());

        Runnable runnable= new Runnable()
        {
            /**
             * 具体执行的代码
             */
            public void run()
            {
                reportResponse(apkFilePath, uri); //报告回复结果
            } //public void run()
        };

        uiHandler.post(runnable);
        
        rememberApkFile(apkFilePath); // 记录安装包路径。
        } //if (isSuccessful) // 是请求成功。
        else // 请求失败
        {
            int errorCode=ErrorCode.ERROR_DOWNLOAD_FAILURE; //错误码

            reportError(errorCode); // 报告错误。陈欣
        } //else // 请求失败
        
    }

    /**
     * 报告回复结果
     * @param responseContent 回复内容
     */
//     private void reportResponse(byte[] responseContent, Uri uri)
    private void reportResponse(String apkFilePath, Uri uri)
    {
        DownloadResult resultObject=new DownloadResult(apkFilePath); //构造结果对象
        
        resultObject.setUri(uri);

        recognizerListener.onResult(resultObject, true); //报告结果
    } //private void reportResponse(String responseContent)
}
