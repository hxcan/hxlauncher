package com.stupidbeauty.hxlauncher.asynctask;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import static android.content.Intent.ACTION_PACKAGE_REPLACED;
import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_PACKAGE_NAME;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
import static com.stupidbeauty.hxlauncher.Constants.Actions.LegacyInstallShortcut;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.InputtingForPackage;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.PackageNameOfInputting;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.os.PowerManager;
import static android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
import static com.stupidbeauty.hxlauncher.Constants.Actions.LegacyInstallShortcut;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.InputtingForPackage;
import static com.stupidbeauty.hxlauncher.Constants.LanImeAction.PackageNameOfInputting;
import static com.stupidbeauty.hxlauncher.Constants.Numbers.IgnoreVoiceResultLength;
import android.os.Process;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import com.stupidbeauty.hxlauncher.adapter.FlipAnimationAdapter;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.stupidbeauty.hxlauncher.R;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static android.content.Intent.ACTION_PACKAGE_CHANGED;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import android.view.View;
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import org.apache.commons.collections4.SetValuedMap;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import com.andexert.library.RippleView;
import com.stupidbeauty.hxlauncher.AndroidApplicationMessage;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.stupidbeauty.hxlauncher.ApplicationInformationAdapter;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.stupidbeauty.hxlauncher.BuiltinShortcutsManager;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class BuildActivityLabelPackageItemInfoMapTask extends AsyncTask<Object, Void, Object>
{
    List<ShortcutInfo> shortcutInfos=null; //查询钉住的快捷方式。
    private BuiltinShortcutsManager builtinShortcutsManager=new BuiltinShortcutsManager(); //!<内置快捷方式管理器
    private MultiMap<String, PackageItemInfo> activityLabelPackageItemInfoMap=new MultiValueMap<>(); //!<活动的标签，与活动本身信息之间的映射。

    private ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();
    private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。
    private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。

    private RecyclerView mRecyclerView; //!<回收视图。

	private static final String TAG="BuildActivityLabelPackageItemInfoMapTask"; //!<输出调试信息时使用的标记。

	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
	    /**
     * 构造映射，活动的标签，与活动的包条目信息之间的映射。用于语音识别之后快速命中活动。
     */
    private void buildActivityLabelPackageItemInfoMap()
    {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager= launcherActivity.getPackageManager(); //获取软件包管理器。

        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);

        //按照活动的标题来匹配：
        for (ResolveInfo temp : appList) //遍历解析结果，并一个个加入到列表中。
        {
            CharSequence activityapplicationLabel=temp.activityInfo.loadLabel(packageManager); //获取文字标记。
            String activityapplicationLabelString=activityapplicationLabel.toString(); //转换成字符串。

            PackageItemInfo currentPackageItemInfo=new PackageItemInfo(); //当前的包条目信息对象。

            currentPackageItemInfo.packageName=temp.activityInfo.packageName;
            currentPackageItemInfo.name=temp.activityInfo.name; //记录活动名字。

            activityLabelPackageItemInfoMap.put(activityapplicationLabelString, currentPackageItemInfo); //加入映射。
        } //for (ResolveInfo temp : appList) //遍历解析结果，并一个个加入到列表中。
    } //private void buildActivityLabelPackageItemInfoMap()

    /**
     * 载入内置快捷方式列表。
     */
    private void loadBuiltinShortcuts()
    {
//         ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();

        articleInfoArrayList=builtinShortcutsManager.loadBuiltinShortcuts(); //载入内置的快捷方式列表。
    } //private void loadBuiltinShortcuts()

    /**
     * 解析启动意图。
     */
    private void solveLauncherIntents()
    {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager= launcherActivity.getPackageManager(); //获取软件包管理器。

        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);

        int position=0; //条目的位置。

        //按照活动的标题来匹配：
        for (ResolveInfo temp : appList) //遍历解析结果，并一个个加入到列表中。
        {
            Intent activitylaunchIntent=new Intent(mainIntent);
            activitylaunchIntent.setClassName(temp.activityInfo.packageName, temp.activityInfo.name); //设置类名。

            CharSequence activityapplicationLabel=temp.activityInfo.loadLabel(packageManager); //获取文字标记。

            ArticleInfo currentApplication=new ArticleInfo(); //创建应用程序信息对象。

            currentApplication.setApplicationLabel(activityapplicationLabel); //设置应用程序文字。
            currentApplication.setLaunchIntent(activitylaunchIntent); //设置启动意图。
            currentApplication.setPackageName(temp.activityInfo.packageName); //设置包名。
            currentApplication.setActivityName(temp.activityInfo.name); //设置活动名字。

            articleInfoArrayList.add(currentApplication); //添加应用。

            packageNameItemNamePositionMap.put(temp.activityInfo.packageName+"/"+temp.activityInfo.name, position); //记录映射。
            packageNamePositionMap.put(temp.activityInfo.packageName, position); //记录映射。

            position++; //计数。
        } //for (ResolveInfo temp : appList) //遍历解析结果，并一个个加入到列表中。

//         mAdapter.setArticleInfoArrayList(articleInfoArrayList); //设置文章信息列表。
//         mAdapter.notifyDataSetChanged(); //通知数据变更。
    } //private void solveLauncherIntents()

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
        Boolean result=false; //结果，是否成功。

        launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
            
//         loadBuiltinShortcuts(); // 载入内置快捷方式。

        buildActivityLabelPackageItemInfoMap(); // 构造映射。
            
        return launcherActivity;
    }

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
    @Override
    protected void onPostExecute(Object result)
    {
//         Log.d(TAG, "onPostExecute, articleInfoArrayList size: " + articleInfoArrayList.size()); // Debug.
        launcherActivity.setActivityLabelPackageItemInfoMap(activityLabelPackageItemInfoMap); // 设置内置快捷方式列表。
    } //protected void onPostExecute(Boolean result)
}
