package com.stupidbeauty.hxlauncher.asynctask;

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
import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.android.volley.RequestQueue;
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
// import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.AnimationDrawable;
import org.apache.commons.collections4.SetValuedMap;
// import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import com.andexert.library.RippleView;
import com.stupidbeauty.hxlauncher.AndroidApplicationMessage;
import com.stupidbeauty.hxlauncher.VoicePackageMapItemMessageProtos;
// import com.stupidbeauty.hxlauncher.VoicePackageMapMessageProtos;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.stupidbeauty.hxlauncher.ApplicationInformationAdapter;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.io.File;
import java.io.IOException;

public class LoadShortcutsTask extends AsyncTask<Object, Void, Object>
{
    List<ShortcutInfo> shortcutInfos=null; //查询钉住的快捷方式。

    private ArrayList<ArticleInfo> articleInfoArrayList = new ArrayList<>();
    private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。
    private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
    private RecyclerView mRecyclerView; //!<回收视图。
//     private ApplicationInformationAdapter mAdapter; //!<适配器。

	private static final String TAG="LoadShortcutsTask"; //!<输出调试信息时使用的标记。
	private LauncherActivity launcherActivity=null; //!< 启动活动。
	
    /**
     * 载入快捷方式列表。
     */
    private void loadShortcuts()
    {
      if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N_MR1) //25之后才有快捷方式。
      {
        LauncherApps launcherApps=(LauncherApps) (launcherActivity.getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。

        LauncherApps.ShortcutQuery shortcutQuery=new LauncherApps.ShortcutQuery();

        int queryFlags=FLAG_MATCH_DYNAMIC | FLAG_MATCH_MANIFEST | FLAG_MATCH_PINNED; //查询标志位。

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P) //25之后才有快捷方式。
        {
          queryFlags=queryFlags | FLAG_MATCH_PINNED_BY_ANY_LAUNCHER; //访问其它桌面设置的快捷方式
        } //if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P) //25之后才有快捷方式。

        shortcutQuery.setQueryFlags(queryFlags); //设置标志位，匹配任何启动器钉住的快捷方式。

        try 
        {
          shortcutInfos=launcherApps.getShortcuts(shortcutQuery, Process.myUserHandle()); //查询钉住的快捷方式。

          Log.d(TAG, "loadShortcuts, short cut list size: " + shortcutInfos.size()); //Debug.

                 if (shortcutInfos != null && !shortcutInfos.isEmpty()) {
                for (ShortcutInfo shortcutInfo : shortcutInfos) {
                    printShortcutInfo(shortcutInfo);
                }
            }
            
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            } //catch (SecurityException e)
        } //if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N_MR1) //25之后才有快捷方式。
    } //private void loadShortcuts()

        private static void printShortcutInfo(ShortcutInfo shortcutInfo) {
              // Log.d(TAG, CodePosition.newInstance().toString() + ", events: "  + usageEvents + ", has next event?:" + usageEvents.hasNextEvent()); //Debug.

        Log.i(TAG, CodePosition.newInstance().toString() + ", Shortcut ID: " + shortcutInfo.getId());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Short Label: " + shortcutInfo.getShortLabel());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Long Label: " + shortcutInfo.getLongLabel());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Disabled Message: " + shortcutInfo.getDisabledMessage());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Package: " + shortcutInfo.getPackage());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Activity: " + shortcutInfo.getActivity());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Last Changed Timestamp: " + shortcutInfo.getLastChangedTimestamp());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Rank: " + shortcutInfo.getRank());
        Log.i(TAG, CodePosition.newInstance().toString() + ", Categories: " + (shortcutInfo.getCategories() != null ? shortcutInfo.getCategories().toString() : "None"));
        Log.i(TAG, CodePosition.newInstance().toString() + ", Intent: " + (shortcutInfo.getIntent() != null ? shortcutInfo.getIntent().toString() : "None"));
        }
    
    private void showStaggeredGridLayoutManager() {
        int columnsPerRow= launcherActivity.getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(columnsPerRow,StaggeredGridLayoutManager.VERTICAL); //布局管理器。
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * 寻找语音识别与软件包映射文件。
     * @return 语音识别与软件包映射文件。
     */
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

      launcherActivity=(LauncherActivity)(params[0]); // 获取启动活动对象
            
      loadShortcuts(); // 载入快捷方式。
            
      return launcherActivity;
    }

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Object result)
    {
      launcherActivity.setShortcutInfos(shortcutInfos); // 设置快捷方式列表。

      launcherActivity.buildShortcutTitleInfoMap(shortcutInfos); //构造映射，快捷方式的标题与快捷方式对象之间的映射。
		} //protected void onPostExecute(Boolean result)
	}
