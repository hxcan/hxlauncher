package com.stupidbeauty.hxlauncher;

import com.stupidbeauty.shutdownat2100.helper.ShutDownAt2100Manager;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.protobuf.InvalidProtocolBufferException;
import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.stupidbeauty.grebe.DownloadRequestor;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
// import com.stupidbeauty.hxlauncher.asynctask.TranslateRequestSendTask;
import com.stupidbeauty.hxlauncher.bean.HxShortcutInfo;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import org.apache.commons.collections4.MultiMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UnLinkActivity extends Activity
{
  private BuiltinShortcutsManager builtinShortcutsManager=new BuiltinShortcutsManager(); //!<内置快捷方式管理器
  private ShutDownAt2100Manager shutDownAt2100Manager=null; //!< Shut down at 2100 manager.

  @BindView(R.id.hitApplicationIcon) ImageView hitApplicationIcon; //!<命中的应用的图标。

  private boolean mscIsInitialized=false; //!<讯飞语音识别是否已经初始化。
  private HashMap<String, Long> packageItemLastLaunchTimestampMap=new HashMap<>(); //!<包名加类名的字符串与最后一次启动时间戳之间的映射。
  private HashMap<String, ShortcutInfo> shortcutTitleInfoMap; //!<快捷方式的标题与快捷方式对象本身的映射。
  private HashMap<String, ShortcutInfo> shortcutIdInfoMap; //!<快捷方式的编号与快捷方式对象本身的映射
  private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
  private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。


  PowerManager.WakeLock wakeLock=null; //!<游戏辅助唤醒锁。
  private boolean activityHasBeenResumed=false; //!<活动是否处于被继续的状态，即正常的运行状态。
  private boolean sentVoiceAssociationData=false; //!<是否已经成功发送语音指令关联应用程序数据。

  private static final int PERMISSIONS_REQUEST = 1; //!<权限请求标识

  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; //!<录音权限。
  private static final String PERMISSION_FINE_LOCATIN = Manifest.permission.ACCESS_FINE_LOCATION; //!<位置权限

    private final DownloadRequestor downloadRequestor = new DownloadRequestor();

    private boolean foundActivity=false; //!<是否命中了活动。

    private MultiMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
//    private HashMap<String, PackageItemInfo> voicePackageNameMap; //!<语音识别结果与包条目信息之间的映射关系。本设备独有的
    private HashMap<String, HxShortcutInfo> voiceShortcutIdMap=new HashMap<>(); //!<语音识别结果与快捷方式编号之间的映射关系．

    private MultiMap<String, PackageItemInfo> voicePackageNameMapBultin; //!<内置的，语音识别结果与包条目信息之间的映射关系。
//    private HashMap<String, PackageItemInfo> voicePackageNameMapBultin; //!<内置的，语音识别结果与包条目信息之间的映射关系。
    private HashMap<String, HxShortcutInfo> voiceShortcutMapBultin; //!<内置的，语音识别结果与快捷方式编号之间的映射关系．


    private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。

    @BindView(R.id.wallpaper) ImageView wallpaper; //!<墙纸视图。

    @BindView(R.id.progressBar) ProgressBar progressBar; //!<进度条。

    private String voiceRecognizeResultString; //!<语音识别结果。

    @BindView(R.id.statustextView) TextView statustextView; //!<用来显示状态的文字标签。

    private String recordSoundFilePath; //!<录音文件路径．

    private int recognizeCounter=0; //!<识别计数器．

    private boolean voiceEndDetected=false; //!<是否已经探测到用户声音结束。

    private int mPageNumber = 1;//{1, 1, 1};

    private final int MSG_LOAD_MORE = 2;
    private boolean mIsLastPage = true;

    /**
     * Report that the operation has failed.
     * @param string 服务器回复的结果说明文字。
     */
    protected void reportOperationFail(String string)
    {
        Toast.makeText(HxLauncherApplication.getAppContext(), string, Toast.LENGTH_LONG).show();   //做一个提示，Failed adding address ,please retry.
    } //protected void reportOperationFail()

    @BindView(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。
    // @BindView(R.id.articleListmy_recycler_view) RecyclerView mRecyclerView; //!<回收视图。
    private static final String TAG="LauncherActivity"; //!<输出调试信息时使用的标记。
    private final String categoryName="default"; //!<要显示的分类的名字。
    final Map<String,PackageCountObject> packageNameCountObjectMap=new HashMap<>(); //软件包名字与计数对象之间的映射。
    final List<PackageCountObject> packageCountObjectList=new ArrayList<>(); //!<计数对象列表。

    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.w(TAG, "onCreate, 323, timestamp: " + System.currentTimeMillis()); //Debug.

        super.onCreate(savedInstanceState);

        Log.d(TAG,"onCreate, wake lock: "+wakeLock+ ", this: " + this); //Debug.
        Log.w(TAG, "onCreate, 328, timestamp: " + System.currentTimeMillis()); //Debug.


//        askShowSystemWallpaper(); //要求显示系统的墙纸在本活动后面。

        requestWindowFeature(Window.FEATURE_NO_TITLE); //不显示标题栏。

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Log.w(TAG, "onCreate, 338, timestamp: " + System.currentTimeMillis()); //Debug.
        }

        setContentView(R.layout.unlink_activity); //设置界面布局

        Log.w(TAG, "onCreate, 343, timestamp: " + System.currentTimeMillis()); //Debug.
        ButterKnife.bind(this); //视图注入。




        showSystemWallpaper(); //显示系统壁纸。
        Log.w(TAG, "onCreate, 306, timestamp: " + System.currentTimeMillis() + ", sending broadcast"); //Debug.





        sendBroadcastUnlink(); //发送广播，断开语音识别链接

        shutDownAt2100Manager=new ShutDownAt2100Manager(this);

        finish(); //关闭
    } //protected void onCreate(Bundle savedInstanceState)

    /**
     * 显示系统壁纸。
     */
    private void showSystemWallpaper()
    {
        try {
            WallpaperManager wallpaperManager=(WallpaperManager) (getSystemService(Context.WALLPAPER_SERVICE)); //获取壁纸管理器。

            Drawable wallpaperDrawable=wallpaperManager.getDrawable(); //获取绘图对象。
//        wallpaperDrawable.

            wallpaper.setImageDrawable(wallpaperDrawable); //显示绘图对象。

        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
    } //private void showSystemWallpaper()


    /**
     * 显示系统墙纸。
     */
    private void loadSystemWallpaper()
    {
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(this); //获取墙纸管理器。

        Drawable wallpaperDrawable=wallpaperManager.getDrawable(); //获取绘图对象。

        wallpaper.setImageDrawable(wallpaperDrawable); //显示墙纸。


    } //private void loadSystemWallpaper()

    /**
     * 发送广播，断开链接。
     */
    private void sendBroadcastUnlink()
    {
        Intent intent4Wrk = new Intent(); //创建意图对象。晚安诸位。
        Bundle extras=new Bundle(); //创建数据包。

        intent4Wrk.putExtras(extras); //设置附加数据。

        intent4Wrk.setAction(Constants.Operation.UnlinkVoiceCommand);// action与接收器相同

        Context appContext= HxLauncherApplication.getAppContext(); //获取应用程序上下文。
        LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(appContext); //Get the local broadcast manger instance.

        lclBrdcstMngr.sendBroadcast(intent4Wrk); //发送广播。


    } //private void saveVoicePackageNameMap()


    private void showStaggeredGridLayoutManager() {
        int columnsPerRow= getResources().getInteger(  R.integer.columnsPerRow); //每行的列数。

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(columnsPerRow,StaggeredGridLayoutManager.VERTICAL); //布局管理器。
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * 活动被暂停。
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        activityHasBeenResumed=false; //不是处于正常运行状态。
    } //protected void onPause()

    /**
     * 判断要不要释放唤醒锁。
     * @param uid 当前为其进行输入的包名。
     */
    private void assesReleaseWakeLock(String uid)
    {
        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

        Set<String> wakeLockPackageNameSet=application.getWakeLockPackageNameSetData().getWakeLockPackageNameSet(); //获取唤醒锁应用包名集合数据。

        if (wakeLockPackageNameSet.contains(uid)) //在唤醒锁名单中。
        {
        } //if (wakeLockPackageNameSet.contains(packageName)) //在唤醒锁名单中。
        else //不在唤醒锁名单中。应当释放。
        {
            HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象。
            hxLauncherApplication.releaseWakeLock(); //释放唤醒锁。

        } //else //不在唤醒锁名单中。应当释放。
    } //private void assesReleaseWakeLock(String uid)

    /**
     * 获取唤醒锁。
     */
    private void acquireWakeLock()
    {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "HxLauncher::WakeLockForGames");

        Log.d(TAG,"acquireWakeLock, wake lock: "+wakeLock + ", this: " + this); //Debug.

        wakeLock.acquire();
    } //private void acquireWakeLock()

}
