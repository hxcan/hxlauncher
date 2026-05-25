package com.stupidbeauty.hxlauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.BuiltinShortcut;
import com.stupidbeauty.hxlauncher.bean.BuiltinShortcutsContainer;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import com.stupidbeauty.victoriafresh.VFile;

import java.util.ArrayList;
import java.util.List;

public class BuiltinShortcutsManager
{

    /**
     * 检查这个包是否已经安装。
     * @param packageName 包名。
     * @return 是否已经安装。
     */
    private boolean checkPackageAvailable(String packageName)
    {
        boolean result=false ; //结果。

        Context context=HxLauncherApplication.getAppContext();

        PackageManager packageManager=context.getPackageManager(); //获取软件包管理器。


        try
        {
          PackageInfo packageInfo=packageManager.getPackageInfo(packageName,0); //获取对应的软件包信息。

          result=true; //存在。
        }
        catch (PackageManager.NameNotFoundException e) //未找到该软件包。
        {
          result=false; //不存在。
        } //catch (PackageManager.NameNotFoundException e) //未找到该软件包。

        return result;
    } //private boolean checkPackageAvailable(String packageName)

    /**
     * 载入内置快捷方式列表。
     */
    public ArrayList<ArticleInfo>  loadBuiltinShortcuts()
    {
      ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();

      String qrcFileName="builtinShortcuts.json"; //文件名。
      String fullQrcFileName=":/BuiltinShortcuts/"+qrcFileName; //构造完整的qrc文件名。

      Context context= HxLauncherApplication.getAppContext();

      VFile qrcHtmlFile=new VFile(context, fullQrcFileName); //qrc网页文件。

      String photoBytes= qrcHtmlFile.getFileTextContent(); //将照片文件内容全部读取。

      //解析JSON：
      Gson gson=new Gson(); //创建gson对象。

      BuiltinShortcutsContainer voiceRecognizeResult=gson.fromJson(photoBytes, BuiltinShortcutsContainer.class); //解析成结果对象。

      List<BuiltinShortcut> builtinShortcuts=voiceRecognizeResult.getBuiltinShortcuts(); //获取内置快捷方式列表。

      Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
      mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

      PackageManager packageManager=context.getPackageManager(); //获取包管理器。

      //一个个地处理：
      for(BuiltinShortcut currentRelationship: builtinShortcuts) //一个个地加入映射中。
      {
        String packageName=currentRelationship.getPackageName(); //获取包名。

            boolean packageAvailable=checkPackageAvailable(packageName); //检查这个包是否已经安装。

            if (packageAvailable) //包已经安装。
            {

                List<String> activities=currentRelationship.getActivities(); //获取活动列表。

                for(String activity: activities) //一个个地添加。
                {
                    Intent activitylaunchIntent=new Intent(mainIntent);
                    activitylaunchIntent.setClassName(packageName, activity); //设置类名。

                    ComponentName cm = new ComponentName(packageName, activity); //构造组件名字对象。

                    CharSequence activityapplicationLabel=""; //活动标题。

                    try {
                        ActivityInfo activityInfo=packageManager.getActivityInfo(cm, 0); //获取活动信息。

//                            activityTitle=activityInfo.loadLabel(packageManager).toString(); //获取标题。

                        activityapplicationLabel=activityInfo.loadLabel(packageManager); //获取文字标记。

                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
//                        e.printStackTrace();
                    } //catch (PackageManager.NameNotFoundException e)



                    ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。

                    currentApplication.setApplicationLabel(activityapplicationLabel); //设置应用程序文字。
                    currentApplication.setLaunchIntent(activitylaunchIntent); //设置启动意图。
                    currentApplication.setPackageName(packageName); //设置包名。
                    currentApplication.setActivityName(activity); //设置活动名字。

                    articleInfoArrayList.add(currentApplication); //添加应用。

                } //for(String activity: activities) //一个个地添加。
            } //if (packageAvailable) //包已经安装。

//                        voicePackageNameMap.put(currentRelationship.getVoiceRecognizeResult(), currentRelationship.getPackageName()); //加入映射。

//                HxShortcutInfo currentPackageItemInfo=new HxShortcutInfo(); //当前的包条目信息对象。
//
//                currentPackageItemInfo.packageName=currentRelationship.getPackageName();
//                currentPackageItemInfo.shortcutId=currentRelationship.getShortcutId(); //记录快捷方式编号。
//
//                voiceShortcutMapBultin.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfo); //加入映射。



//                PackageItemInfo currentPackageItemInfo=new PackageItemInfo(); //当前的包条目信息对象。
//
//                currentPackageItemInfo.packageName=currentRelationship.getPackageName();
//                currentPackageItemInfo.name=currentRelationship.getActivityName(); //记录活动名字。
//
//                voicePackageNameMapBultin.put(currentRelationship.getVoiceRecognizeResult(), currentPackageItemInfo); //加入映射。
//

        } //for(TranslateRequestMessageProtos.TranslateRequestMessage currentRelationship: relationships) //一个个地加入映射中。

        return (articleInfoArrayList); //设置文章信息列表。
    } //private void loadBuiltinShortcuts()

}

