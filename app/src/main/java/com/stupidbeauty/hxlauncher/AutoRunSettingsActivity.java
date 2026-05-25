package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoRunSettingsActivity extends Activity implements LocalServerListLoadListener,InstalledPackageLoadTaskInterface
{
    private Map<String, Boolean> packageNameAutoRunMap=new HashMap<>(); //!<应用程序包名与是否要自动启动的映射。
    private static final String TAG="AutoRunSettingsActivity"; //!<输出调试信息时使用的标记。

    @BindView(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。

    private AutoRunApplicationInformationAdapter mAdapter; //!<适配器。

    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.auto_run_settings_activity); //设置界面内容。

      ButterKnife.bind(this); //视图注入。

      bindAdapter(); //绑定适配器。

      loadVoicePackageNameMap(); //载入语音识别结果与包名之间的映射。

      reqGameData(); //开始获取数据。

      initializeBus(); //初始化总线。
    } //protected void onCreate(Bundle savedInstanceState)

    @Override
    /**
     * 活动重新处于活跃状态。
     */
    protected void onResume()
    {
        Log.d(TAG, "onResume, starting voice recgonize.");
        super.onResume(); //超类继续工作。

        HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。
        hxLauncherApplication.releaseWakeLock(); //释放唤醒锁。

//        if (wakeLock!=null) //唤醒锁存在。
//        {
//            wakeLock.release();
//            wakeLock=null; //置空，避免重复释放。
//        } //if (wakeLock!=null) //唤醒锁存在。

        Log.d(TAG, "onResume, starting voice recgonize.");

        Log.d(TAG, "onResume, starting voice recgonize.");

        Log.d(TAG, "onResume, starting voice recgonize.");

        boolean isLoggable=Log.isLoggable("onResume", Log.DEBUG);

        System.out.println(TAG+", onResume, is loggable: "+ isLoggable); //Debug.

        Log.i(TAG, "onResume, starting voice recgonize.");

        Log.d(TAG, "onResume, starting voice recgonize.");

        loadVoicePackageNameMap(); //载入语音识别结果与包名之间的映射。

    } //protected void onResume()


    /**
     * 报告结果，翻译请求的发送结果。
     * @param result 是否发送成功。
     */
    public void processApplicationInfoLoadResult(List<PackageInfo> result)
    {
        showInstalledPackages(result);
    } //public void processApplicationInfoLoadResult(Boolean result)



    @Override
    public void onLoadPackageInfoList()
    {
        reqGameDataFromApplication(); //重新获取一次本地服务器列表。
    }

    /**
     * 显示软件包列表。
     * @param packageInfoList 软件包列表。
     */
    void showInstalledPackages(List<PackageInfo>  packageInfoList)
    {
        String Result = ""; // 结果。

        String maskFileName=""; //获取掩码图片文件名。

        ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();

        PackageManager packageManager=getPackageManager(); //获取软件包管理器。
        for (PackageInfo packageInfo:packageInfoList) //一个个地遍历。
        {
            String packageName=packageInfo.packageName; //获取软件包名字。

            Intent launchIntent= packageManager.getLaunchIntentForPackage(packageName); //获取当前软件包的启动意图。

            if (launchIntent!=null) //有启动意图。
            {
//                checkAndCorrectClassNameInLauncherIntent(launchIntent); //检查并更正启动意图中的类名。


                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
                Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));

                for (ResolveInfo temp : appList) {


                    if (temp.activityInfo.packageName.equals(packageName)) //本个应用。
                    {

                        Intent activitylaunchIntent=new Intent(launchIntent);
                        activitylaunchIntent.setClassName(packageName, temp.activityInfo.name); //设置类名。


                        ApplicationInfo applicationInfo=packageInfo.applicationInfo; //获取应用程序信息。

                        CharSequence applicationLabel=packageManager.getApplicationLabel(applicationInfo); //获取应用程序的文字。
                        CharSequence activityapplicationLabel=temp.activityInfo.loadLabel(packageManager); //获取文字标记。

                        ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。
                        currentApplication.setApplicationLabel(activityapplicationLabel); //设置应用程序文字。
                        currentApplication.setLaunchIntent(activitylaunchIntent); //设置启动意图。
                        currentApplication.setPackageName(packageName); //设置包名。

                        boolean autoRun=false;

                        if (packageNameAutoRunMap.containsKey(packageName)) //包含这个键。
                        {
                            autoRun=packageNameAutoRunMap.get(packageName);
                        } //if (packageNameAutoRunMap.containsKey(packageName)) //包含这个键。


                        currentApplication.setAutoRun(autoRun); //是否自动运行。

                        Log.i(TAG, "showInstalledPackages, package and activity name = " + temp.activityInfo.packageName + "    " + temp.activityInfo.name+", auto run: " + autoRun); //Debug.

                        articleInfoArrayList.add(currentApplication); //添加应用。

                    } //if (temp.activityInfo.packageName.equals(packageName)) //本个应用。


                }



            } //else //有启动意图。
        } //for (PackageInfo packageInfo:packageInfoList) //一个个地遍历。

        //传递给适配器：
        mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
        mAdapter.notifyDataSetChanged(); //通知数据变更。
    } //void showInstalledPackages(List<PackageInfo>  packageInfoList)


    /**
     * 初始化总线。
     */
    private void initializeBus()
    {
//        bus=new Bus(); //初始化总线。

//        mAdapter.setBus(bus); //设置总线。

//        bus.register(this); //注册总线。
    } //private void initializeBus()




    /**
     * 绑定适配器。
     */
    private void bindAdapter()
    {
        int columnsPerRow= getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(columnsPerRow,StaggeredGridLayoutManager.VERTICAL); //布局管理器。
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new AutoRunApplicationInformationAdapter(this); //应用程序信息适配器。
        mRecyclerView.setAdapter(mAdapter);
    } //private void bindAdapter()

    /**
     * 初始化数据
     */
    private void reqGameData()
    {
      reqGameDataFromApplication(); //从应用程序对象处请求获取数据。

      registerLocalServerListCallbackToApplication(); //向应用程序注册本地服务器列表获取完毕的回调。
    } //private void reqGameData()

    /**
     * 向应用程序注册本地服务器列表获取完毕的回调。
     */
    private void registerLocalServerListCallbackToApplication()
    {
        HxLauncherApplication baseApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。

        baseApplication.addLocalServerListLoadListener(this); //将本对象加入到回调列表中。
    } //private void registerLocalServerListCallbackToApplication()


    /**
     * 从应用程序对象处请求获取数据。
     */
    private void reqGameDataFromApplication()
    {
        HxLauncherApplication baseApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
        ApplicationListData applicationListData =baseApplication.getApplicationListData(); //获取本地服务器列表数据对象。

        if (applicationListData !=null) //存在对象。
        {
            List<PackageInfo> courtList= applicationListData.getPackageInfoList(); //获取本地服务器列表。

            if (courtList!=null) //对象存在。
            {
                showInstalledPackages(courtList); //显示已安装的应用程序列表。
            } //if (courtList!=null) //对象存在。
        } //if (applicationListData!=null) //存在对象。
    } //private void reqGameDataFromApplication()

    /**
     * 启动应用。
     * @param launchIntent 启动用的意图。
     */
    public void launchApplication(Intent launchIntent)
    {

        try //尝试启动活动，并且捕获可能的异常。
        {
            checkAndAquireWakeLock(launchIntent); //检查并获取唤醒锁。

            startActivity(launchIntent); //启动活动。
        } //try //尝试启动活动，并且捕获可能的异常。
        catch (ActivityNotFoundException exception)
        {
            exception.printStackTrace(); //报告错误。
        } //catch (ActivityNotFoundException exception)
    } //private void launchApplication(Intent launchIntent)

    /**
     * 检查并获取唤醒锁。
     * @param launchIntent 启动意图。
     */
    private void checkAndAquireWakeLock(Intent launchIntent)
    {
        String packageName=launchIntent.getPackage(); //获取包名。



        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

        Set<String> wakeLockPackageNameSet=application.getWakeLockPackageNameSetData().getWakeLockPackageNameSet(); //获取唤醒锁应用包名集合数据。

        Log.d(TAG,"checkAndAquireWakeLock, package name: "+packageName); //Debug.


        if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
        {
            HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。

            hxLauncherApplication.acquireWakeLock(); //获取唤醒锁。

//            acquireWakeLock(); //获取唤醒锁。
        } //if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
    } //private void checkAndAquireWakeLock(Intent launchIntent)

    /**
     * 切换自动启动。
     * @param packageName 要切换自动启动的包名。
     */
    public void toggleAutoRun(String packageName, boolean autoRun)
    {
        Log.i(TAG,"toggleAutoRun, package name: "+packageName+ ", auto run: " + autoRun); //Debug.

        packageNameAutoRunMap.put(packageName, autoRun); //设置到映射中。

        savePackageNameAutoRunMap(); //保存映射。
    } //public void toggleAutoRun(String packageName)

    /**
     * 保存映射。包名与自动启动与否之间的映射。
     */
    private void savePackageNameAutoRunMap()
    {
        PackageAutoRunMapMessageProtos.PackageAutoRunMapMessage.Builder translateRequestMessage= PackageAutoRunMapMessageProtos.PackageAutoRunMapMessage.newBuilder(); //创建一个消息对象。

            for(String currentVoiceRecognizeResult: packageNameAutoRunMap.keySet()) //一个个地保存。
            {
                Boolean currentPackageName=packageNameAutoRunMap.get(currentVoiceRecognizeResult); //获取包名。

                Log.i(TAG,"savePackageNameAutoRunMap, package name: "+currentVoiceRecognizeResult+ ", auto run: " + currentPackageName); //Debug.


                PackageAutoRunMapItemMessageProtos.PackageAutoRunMapItemMessage.Builder translateRequestMessageBuilder= PackageAutoRunMapItemMessageProtos.PackageAutoRunMapItemMessage.newBuilder();

                translateRequestMessageBuilder.setAutoRun(currentPackageName).setPackageName(currentVoiceRecognizeResult); //设置字段。

                translateRequestMessage.addMap(translateRequestMessageBuilder); //添加映射关系。
            } //for(String currentVoiceRecognizeResult: voicePackageNameMap.keySet()) //一个个地保存。

            byte[] serializedContent=translateRequestMessage.build().toByteArray(); //序列化成字节数组。

            File photoFile=findRandomPhotoFile(); //随机寻找一个照片文件。

            try
            {
                FileUtils.writeByteArrayToFile(photoFile, serializedContent); //写入内容。
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    } //private void savePackageNameAutoRunMap()

    /**
     * 载入语音识别结果与包名之间的映射。
     */
    private void loadVoicePackageNameMap()
    {
        File photoFile=findRandomPhotoFile(); //随机寻找一个照片文件。

        packageNameAutoRunMap=new HashMap<>(); //创建映射。

        if (photoFile!=null) //不是空指针。
        {
            if (photoFile.exists()) //文件存在。
            {
                try
                {
                    byte[] photoBytes= FileUtils.readFileToByteArray(photoFile); //将照片文件内容全部读取。

                    PackageAutoRunMapMessageProtos.PackageAutoRunMapMessage translateRequestMessage=PackageAutoRunMapMessageProtos.PackageAutoRunMapMessage.parseFrom(photoBytes); //创建一个消息对象。

                    List<PackageAutoRunMapItemMessageProtos.PackageAutoRunMapItemMessage> relationships=translateRequestMessage.getMapList(); //获取关系列表。

                    for(PackageAutoRunMapItemMessageProtos.PackageAutoRunMapItemMessage currentRelationship: relationships) //一个个地加入映射中。
                    {
                        Log.i(TAG, "loadVoicePackageNameMap, package name: "+ currentRelationship.getPackageName() + ", auto run: " + currentRelationship.getAutoRun()); //Debug.


                        packageNameAutoRunMap.put(currentRelationship.getPackageName(), currentRelationship.getAutoRun()); //加入映射。
                    } //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。


                }
                catch (IOException e)
                {
                    e.printStackTrace();
                } //catch (IOException e)

            } //if (photoFile.exists()) //文件存在。

        } //if (photoFile!=null) //不是空指针。

    } //private void loadVoicePackageNameMap()




    /**
     * 随机寻找一个照片文件。
     * @return 随机寻找的一个照片文件。
     */
    private  File findRandomPhotoFile()
    {
        File result=null;

        File filesDir=getFilesDir();

        Log.d(TAG, "findRandomPhotoFile, files dir: "+ filesDir); //Debug.

        if (filesDir==null) //该目录不存在。
        {

        } //if (filesDir==null) //该目录不存在。
        else //该目录存在。
        {
            result=new File(filesDir.getAbsolutePath()+"/packageAutoRunMap.otz"); //指定文件名。
//            R

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



}



