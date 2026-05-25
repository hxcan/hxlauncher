package com.stupidbeauty.hxlauncher.asynctask;

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

public class ReqGameDataTask extends AsyncTask<Object, Void, Object>
{
    private ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();
    private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。
    private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。

    private RecyclerView mRecyclerView; //!<回收视图。
    private ApplicationInformationAdapter mAdapter; //!<适配器。

	private static final String TAG="ReqGameDataTask"; //!<输出调试信息时使用的标记。
	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
	    /**
     * 初始化数据
     */
    private void reqGameData()
    {
        Log.w(TAG, "reqGameData, 727, timestamp: " + System.currentTimeMillis()); //Debug.

        solveLauncherIntents(); //解析启动意图。

        Log.w(TAG, "reqGameData, 731, timestamp: " + System.currentTimeMillis()); //Debug.
    } //private void reqGameData()

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

    } //private void solveLauncherIntents()

    private void showStaggeredGridLayoutManager() {
        int columnsPerRow= launcherActivity.getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(columnsPerRow,StaggeredGridLayoutManager.VERTICAL); //布局管理器。
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

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
        Log.w(TAG, "doInBackground, start: " + System.currentTimeMillis()); //Debug.

        //参数顺序：

        Boolean result=false; //结果，是否成功。

        launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
        mAdapter=(ApplicationInformationAdapter)(params[1]); // 获取回收视图对象。

        Log.w(TAG, "doInBackground, mAdapter: " + mAdapter); //Debug.

        solveLauncherIntents(); // 解析启动意图列表。
            
        Log.w(TAG, "doInBackground, finish: " + System.currentTimeMillis()); //Debug.

        return mAdapter;
    }

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
    @Override
    protected void onPostExecute(Object result)
    {
        launcherActivity.setPackageNamePositionMap(packageNamePositionMap);
        launcherActivity.setPackageNameItemNamePositionMap(packageNameItemNamePositionMap);
        
        launcherActivity.setArticleInfoArrayList(articleInfoArrayList); // 设置应用程序信息列表。

    } //protected void onPostExecute(Boolean result)
}
