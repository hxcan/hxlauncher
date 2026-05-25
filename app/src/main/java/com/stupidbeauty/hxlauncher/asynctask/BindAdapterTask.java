package com.stupidbeauty.hxlauncher.asynctask;

import com.stupidbeauty.hxlauncher.adapter.FlipAnimationAdapter;
import com.stupidbeauty.hxlauncher.asynctask.VoicePackageNameMapSaveTask;
import com.stupidbeauty.hxlauncher.bean.VoiceCommandHitDataObject;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.stupidbeauty.hxlauncher.R;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
// import java.io.File;
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
// import org.apache.commons.collections4.SetValuedMap;
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

public class BindAdapterTask extends AsyncTask<Object, Void, Object>
{
  private FlipAnimationAdapter recyclerViewsetAdapter=null; //!< 翻转动画适配器。
  private RecyclerView.LayoutManager mLayoutManager = null; //!< 布局管理器。
  private RecyclerView mRecyclerView; //!<回收视图。
  private ApplicationInformationAdapter mAdapter; //!<适配器。

  private static final String TAG="BindAdapterTask"; //!<输出调试信息时使用的标记。

  private LauncherActivity launcherActivity=null; //!< 启动活动。
	
  /**
  * 绑定适配器。
  */
  private void bindAdapter()
  {
    showStaggeredGridLayoutManager(); //显示瀑布布局

    mAdapter=new ApplicationInformationAdapter(launcherActivity); //应用程序信息适配器。

    recyclerViewsetAdapter=new FlipAnimationAdapter(mAdapter); // 翻转动画适配器。

    recyclerViewsetAdapter.setFirstOnly(false);

    recyclerViewsetAdapter.setDuration(600);
  } //private void bindAdapter()

  private void showStaggeredGridLayoutManager() 
  {
    int columnsPerRow= launcherActivity.getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

    mLayoutManager = new StaggeredGridLayoutManager(columnsPerRow,StaggeredGridLayoutManager.VERTICAL); // 布局管理器。
  }

  @Override
  protected Object doInBackground(Object... params)
  {
    Log.w(TAG, "doInBackground, start: " + System.currentTimeMillis()); //Debug.

    //参数顺序：

    Boolean result=false; //结果，是否成功。

    launcherActivity=(LauncherActivity)(params[0]); //获取映射对象
    mRecyclerView=(RecyclerView)(params[1]); // 获取回收视图对象。
            
    bindAdapter(); // 绑定适配器。
        
    Log.d(TAG, "doInBackground, mAdapter: "+ mAdapter); // Debug.
            
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
    Log.d(TAG, "onPostExecute, mAdapter: "+ mAdapter); // Debug.

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(recyclerViewsetAdapter);

    launcherActivity.setApplicationInformationAdapter(mAdapter);
  } //protected void onPostExecute(Boolean result)
}
