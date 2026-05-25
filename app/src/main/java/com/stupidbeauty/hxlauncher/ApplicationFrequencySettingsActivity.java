package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

//import com.squareup.otto.Bus;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.qtdocchinese.ArticleInfo;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationFrequencySettingsActivity extends Activity implements LocalServerListLoadListener,InstalledPackageLoadTaskInterface
{
    private Map<String, Boolean> packageNameAutoRunMap=new HashMap<>(); //!<应用程序包名与是否要自动启动的映射。
    private static final String TAG="AutoRunSettingsActivity"; //!<输出调试信息时使用的标记。

    @BindView(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。

    private ApplicationFrequencyApplicationInformationAdapter mAdapter; //!<适配器。

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
    } //protected void onCreate(Bundle savedInstanceState)

    @Override
    /**
     * 活动重新处于活跃状态。
     */
    protected void onResume()
    {
      super.onResume(); //超类继续工作。

      HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。
      hxLauncherApplication.releaseWakeLock(); //释放唤醒锁。

      boolean isLoggable=Log.isLoggable("onResume", Log.DEBUG);

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
        ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager=getPackageManager(); //获取软件包管理器。

        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);

        int position=0; //条目的位置。

        //按照活动的标题来匹配：
        for (ResolveInfo temp : appList) //遍历解析结果，并一个个加入到列表中。
        {
            Intent activitylaunchIntent=new Intent(mainIntent);
            activitylaunchIntent.setClassName(temp.activityInfo.packageName, temp.activityInfo.name); //设置类名。
//            temp.activityInfo.

            CharSequence activityapplicationLabel=temp.activityInfo.loadLabel(packageManager); //获取文字标记。

            ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。

            currentApplication.setApplicationLabel(activityapplicationLabel); //设置应用程序文字。
            currentApplication.setLaunchIntent(activitylaunchIntent); //设置启动意图。
            currentApplication.setPackageName(temp.activityInfo.packageName); //设置包名。
            currentApplication.setActivityName(temp.activityInfo.name); //设置活动名字。

            articleInfoArrayList.add(currentApplication); //添加应用。

//            packageNameItemNamePositionMap.put(temp.activityInfo.packageName+"/"+temp.activityInfo.name, position); //记录映射。
//            packageNamePositionMap.put(temp.activityInfo.packageName, position); //记录映射。

            position++; //计数。
        } //for (ResolveInfo temp : appList) //遍历解析结果，并一个个加入到列表中。

        mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
        mAdapter.notifyDataSetChanged(); //通知数据变更。


//        String Result = ""; // 结果。
//
//        String maskFileName=""; //获取掩码图片文件名。
//
//        ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();
//
//        PackageManager packageManager=getPackageManager(); //获取软件包管理器。
//        for (PackageInfo packageInfo:packageInfoList) //一个个地遍历。
//        {
//            String packageName=packageInfo.packageName; //获取软件包名字。
//
//            Intent launchIntent= packageManager.getLaunchIntentForPackage(packageName); //获取当前软件包的启动意图。
//
//            if (launchIntent!=null) //有启动意图。
//            {
////                checkAndCorrectClassNameInLauncherIntent(launchIntent); //检查并更正启动意图中的类名。
//
//
//                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//                List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
//                Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
//
//                for (ResolveInfo temp : appList) {
//
//
//                    if (temp.activityInfo.packageName.equals(packageName)) //本个应用。
//                    {
//
//                        Intent activitylaunchIntent=new Intent(launchIntent);
//                        activitylaunchIntent.setClassName(packageName, temp.activityInfo.name); //设置类名。
//
//
//                        ApplicationInfo applicationInfo=packageInfo.applicationInfo; //获取应用程序信息。
//
//                        CharSequence applicationLabel=packageManager.getApplicationLabel(applicationInfo); //获取应用程序的文字。
//                        CharSequence activityapplicationLabel=temp.activityInfo.loadLabel(packageManager); //获取文字标记。
//
//                        ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。
//                        currentApplication.setApplicationLabel(activityapplicationLabel); //设置应用程序文字。
//                        currentApplication.setLaunchIntent(activitylaunchIntent); //设置启动意图。
//                        currentApplication.setPackageName(packageName); //设置包名。
//                        currentApplication.setActivityName(temp.activityInfo.name); //设置活动类名。
//
//                        boolean autoRun=false;
//
//                        if (packageNameAutoRunMap.containsKey(packageName)) //包含这个键。
//                        {
//                            autoRun=packageNameAutoRunMap.get(packageName);
//                        } //if (packageNameAutoRunMap.containsKey(packageName)) //包含这个键。
//
//
//                        currentApplication.setAutoRun(autoRun); //是否自动运行。
//
//                        Log.i(TAG, "showInstalledPackages, package and activity name = " + temp.activityInfo.packageName + "    " + temp.activityInfo.name+", auto run: " + autoRun); //Debug.
//
//                        articleInfoArrayList.add(currentApplication); //添加应用。
//
//                    } //if (temp.activityInfo.packageName.equals(packageName)) //本个应用。
//
//
//                }
//
//
//
//            } //else //有启动意图。
//        } //for (PackageInfo packageInfo:packageInfoList) //一个个地遍历。
//
//        //传递给适配器：
//        mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
//        mAdapter.notifyDataSetChanged(); //通知数据变更。
    } //void showInstalledPackages(List<PackageInfo>  packageInfoList)

    /**
     * 绑定适配器。
     */
    private void bindAdapter()
    {
      int columnsPerRow= getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this); //布局管理器。
      mRecyclerView.setLayoutManager(mLayoutManager);

      mAdapter=new ApplicationFrequencyApplicationInformationAdapter(this); //应用程序信息适配器。
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
     * 更新启动冷却时间数据。
     * @param packageName 包名。
     * @param activityName 活动名。
     * @param seekBarValue 冷却时间在寻找条中的索引。
     */
    public void updateLaunchCoolDownDuration(String packageName, String activityName, int seekBarValue)
    {
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      HashMap<Integer, Integer> seekBarValueCoolDownTimeMap=hxLauncherApplication.getSeekBarValueCoolDownTimeMap(); //获取映射。
      int calculatedCoolDownTime=seekBarValueCoolDownTimeMap.get(seekBarValue); //计算出实际的冷却时间，秒数。

      HashMap<String, Integer> packageItemLaunchCoolDownMap=hxLauncherApplication.getPackageItemLaunchCoolDownMap(); //获取映射。包条目信息字符串与实际冷却时间秒数之间的映射。

      packageItemLaunchCoolDownMap.put(packageName+"/"+activityName, calculatedCoolDownTime); //写入到映射中。

      hxLauncherApplication.savePackageItemLaunchCoolDownMap(); //保存映射。
    } //public void updateLaunchCoolDownDuration(String packageName, String activityName, int seekBarValue)

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



