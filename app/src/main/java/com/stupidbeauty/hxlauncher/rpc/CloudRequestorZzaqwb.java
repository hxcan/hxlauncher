package com.stupidbeauty.hxlauncher.rpc;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.util.concurrent.TimeUnit;

import okhttp3.tls.Certificates;
import okhttp3.tls.HandshakeCertificates;
import com.iflytek.cloud.SpeechRecognizer;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.lanime.network.volley.MapUtils;
import com.stupidbeauty.victoriafresh.VFile;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 内部接口。
 */
public class CloudRequestorZzaqwb
{
    private String appId=null; //!<应用程序编号。

    public void setAppId(String appId) {
        this.appId = appId;
    }

    private SpeechRecognizer speechRecognizer=null; //!<语音识别器实例

    public void setSpeechRecognizer(SpeechRecognizer speechRecognizer) 
    {
        this.speechRecognizer = speechRecognizer;
    }

    private String sessionId="sid173626"; //!<会话编号
    private String userId="uid173634"; //!<用户编号
    private String equipmentNo="eid173641"; //!<设备编号
    private String rawAudioPath=null; //!<原始录音文件路径。

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setRawAudioPath(String rawAudioPath) {
        this.rawAudioPath = rawAudioPath;
    }

    private static final String TAG="CloudRequestorZzaqwb"; //!<输出调试信息时使用的标记
    private String backendUrl="http://frp.wise4ai.com:7996"; //!<后端请求地址

    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public void setContext(Context context) 
    {
        this.context = context;
    }

    public void setRecognizerListener(RecognizerListener recognizerListener) 
    {
        this.recognizerListener = recognizerListener;

        asrRequestCallback.setRecognizerListener(recognizerListener); //设置识别结果监听器
    }

    private RecognizerListener recognizerListener; //!<识别结果监听器
    private OkHttpClient client = null; //!<HTTP客户端
    private DownloadCallback downloadCallback=new DownloadCallback(); //!< 下载请求回调对象。

    private AsrRequestCallback asrRequestCallback=new AsrRequestCallback(); //!<语音识别请求回调对象
    private Context context=null; //!<用于获取资源的上下文对象

    public CloudRequestorZzaqwb()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) //21及以上
        {
//            client = new OkHttpClient(); //!<HTTP客户端

//                    + "BAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTAwMTE5\n"
//                    + "MDAwMDAwWhcNMzgwMTE4MjM1OTU5WjCBhTELMAkGA1UEBhMCR0IxGzAZBgNVBAgT\n"
//                    + "EkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UEBxMHU2FsZm9yZDEaMBgGA1UEChMR\n"
//                    + "Q09NT0RPIENBIExpbWl0ZWQxKzApBgNVBAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNh\n"
//                    + "dGlvbiBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCR\n"
//                    + "6FSS0gpWsawNJN3Fz0RndJkrN6N9I3AAcbxT38T6KhKPS38QVr2fcHK3YX/JSw8X\n"
//                    + "pz3jsARh7v8Rl8f0hj4K+j5c+ZPmNHrZFGvnnLOFoIJ6dq9xkNfs/Q36nGz637CC\n"
//                    + "9BR++b7Epi9Pf5l/tfxnQ3K9DADWietrLNPtj5gcFKt+5eNu/Nio5JIk2kNrYrhV\n"
//                    + "/erBvGy2i/MOjZrkm2xpmfh4SDBF1a3hDTxFYPwyllEnvGfDyi62a+pGx8cgoLEf\n"
//                    + "Zd5ICLqkTqnyg0Y3hOvozIFIQ2dOciqbXL1MGyiKXCJ7tKuY2e7gUYPDCUZObT6Z\n"
//                    + "+pUX2nwzV0E8jVHtC7ZcryxjGt9XyD+86V3Em69FmeKjWiS0uqlWPc9vqv9JWL7w\n"
//                    + "qP/0uK3pN/u6uPQLOvnoQ0IeidiEyxPx2bvhiWC4jChWrBQdnArncevPDt09qZah\n"
//                    + "SL0896+1DSJMwBGB7FY79tOi4lu3sgQiUpWAk2nojkxl8ZEDLXB0AuqLZxUpaVIC\n"
//                    + "u9ffUGpVRr+goyhhf3DQw6KqLCGqR84onAZFdr+CGCe01a60y1Dma/RMhnEw6abf\n"
//                    + "Fobg2P9A3fvQQoh/ozM6LlweQRGBY84YcWsr7KaKtzFcOmpH4MN5WdYgGq/yapiq\n"
//                    + "crxXStJLnbsQ/LBMQeXtHT1eKJ2czL+zUdqnR+WEUwIDAQABo0IwQDAdBgNVHQ4E\n"
//                    + "FgQUu69+Aj36pvE8hI6t7jiY7NkyMtQwDgYDVR0PAQH/BAQDAgEGMA8GA1UdEwEB\n"
//                    + "/wQFMAMBAf8wDQYJKoZIhvcNAQEMBQADggIBAArx1UaEt65Ru2yyTUEUAJNMnMvl\n"
//                    + "wFTPoCWOAvn9sKIN9SCYPBMtrFaisNZ+EZLpLrqeLppysb0ZRGxhNaKatBYSaVqM\n"
//                    + "4dc+pBroLwP0rmEdEBsqpIt6xf4FpuHA1sj+nq6PK7o9mfjYcwlYRm6mnPTXJ9OV\n"
//                    + "2jeDchzTc+CiR5kDOF3VSXkAKRzH7JsgHAckaVd4sjn8OoSgtZx8jb8uk2Intzna\n"
//                    + "FxiuvTwJaP+EmzzV1gsD41eeFPfR60/IvYcjt7ZJQ3mFXLrrkguhxuhoqEwWsRqZ\n"
//                    + "CuhTLJK7oQkYdQxlqHvLI7cawiiFwxv/0Cti76R7CZGYZ4wUAc1oBmpjIXUDgIiK\n"
//                    + "boHGhfKppC3n9KUkEEeDys30jXlYsQab5xoq2Z0B15R97QNKyvDb6KkBPvVWmcke\n"
//                    + "jkk9u+UJueBPSZI9FoJAzMxZxuY67RIuaTxslbH9qh17f4a+Hg4yRvv7E491f0yL\n"
//                    + "S0Zj/gA0QHDBw7mh3aZw4gSzQbzpgJHqZJx64SIDqZxubw5lT2yHh17zbqD5daWb\n"
//                    + "QOhTsiedSrnAdyGN/4fy3ryM7xfft0kL0fJuMAsaDk527RH89elWsn2/x20Kk4yl\n"
//                    + "0MC2Hb46TpSi125sC8KKfPog88Tk5c0NqMuRkrF8hey1FGlmDoLnzc7ILaZRfyHB\n"
//                    + "NVOFBkpdn627G190\n"
//                    + "---
//
//                    --END CERTIFICATE-----\n");

            String qrcFileName="server.cert"; //文件名。

            String fullQrcFileName=":/Certificate/106.12.166.173/"+qrcFileName; //构造完整的qrc文件名。

            context= HxLauncherApplication.getAppContext(); //获取应用程序上下文。

            VFile qrcHtmlFile=new VFile(context, fullQrcFileName); //qrc网页文件。


            String fileContent=qrcHtmlFile.getFileTextContent(); //获取文件的完整内容。

            String pemContent=fileContent; //获取读取的内容

            X509Certificate o106RsaCertificationAuthority = Certificates.decodeCertificatePem(pemContent); //读取证书


            HandshakeCertificates certificates = new HandshakeCertificates.Builder()
//                    .addTrustedCertificate(letsEncryptCertificateAuthority)
//                    .addTrustedCertificate(entrustRootCertificateAuthority)
//                    .addTrustedCertificate(comodoRsaCertificationAuthority)
                    .addTrustedCertificate(o106RsaCertificationAuthority)
                    // Uncomment if standard certificates are also required.
                    //.addPlatformTrustedCertificates()
                    .build();

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager())
        .connectTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
                    .build();
        } //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) //21及以上
    }

    /**
     * 开始下载安装包。陈欣
     * @param uri 安装包的网址
     */
    public void startDownload(Uri uri, DownloadListener downloadListener, String packageName)
    {
        DownloadCallback downloadCallback=new DownloadCallback(); //!< 下载请求回调对象。
        downloadCallback.setRecognizerListener(downloadListener); // 设置下载结果监听器。
        downloadCallback.setPackageName(packageName); // 设置包名。
        downloadCallback.setContext(context); // 设置上下文。
    
        Map<String,String> map = new HashMap<String, String>();

        String gsonUrl = uri.toString(); //The gson request string.最新版本号。

        Request request = new Request.Builder().url(gsonUrl).build();

        if (client!=null) //客户端对象存在
        {
            downloadCallback.setUri(uri); // 记录网址。
            client.newCall(request).enqueue(downloadCallback); // 将请求放置到队列中。
        } //if (client!=null) //客户端对象存在
        else //客户端对象不存在
        {
            int errorCode= ErrorCode.ERROR_NOT_SUPPORT_OK_HTTP; //错误码

            final int finalErrorCode = errorCode;

            SpeechError speechError=new SpeechError(finalErrorCode); //构造错误对象

            recognizerListener.onError(speechError); //报告错误
        } //else //客户端对象不存在
    } //public void startDownload(Uri uri)

    /**
     * 发送请求
     * @param audioContent 语音识别结果
     */
    public void sendAudioSpeechRecognitionRequest(String audioContent)
    {
      Map<String,String> map = new HashMap<String, String>();

      map.put("question", audioContent); //Add the parameter , 输入类型是文字.

      String gsonUrl = "https://"+"106.12.166.173:14094"+"/question?"+ MapUtils.toUrlGetString(map); //The gson request string.最新版本号。

      Request request = new Request.Builder()
        .url(gsonUrl)
        .build();
        
      if (client!=null) //客户端对象存在
      {
        client.newCall(request).enqueue(asrRequestCallback);

        Log.d(TAG, "123, sendAudioSpeechRecognitionRequest, REQUEST queued"); //Debug.
      } //if (client!=null) //客户端对象存在
      else //客户端对象不存在
      {
        int errorCode= ErrorCode.ERROR_NOT_SUPPORT_OK_HTTP; //错误码

        final int finalErrorCode = errorCode;

        SpeechError speechError=new SpeechError(finalErrorCode); //构造错误对象

        recognizerListener.onError(speechError); //报告错误
      } //else //客户端对象不存在
    } //public void sendAudioSpeechRecognitionRequest(byte[] audioContent)
}
