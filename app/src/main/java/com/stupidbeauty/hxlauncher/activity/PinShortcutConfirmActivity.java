package com.stupidbeauty.hxlauncher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.stupidbeauty.hxlauncher.Constants;
import com.stupidbeauty.hxlauncher.R;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;
import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinShortcutConfirmActivity extends Activity
{
    @BindView(R.id.rightTextoperationMethodactTitletextView2) TextView rightTextoperationMethodactTitletextView2; //!<文字视图。

    @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!<图标。

    private static final String TAG="PinShortcutConfirmActiv"; //!<输出调试信息时使用的标记。

    @OnClick(R.id.okbutton)
    public void confirmOk()
    {
        finish(); //关闭。
    } //public void confirmOk()

    @Override
    /**
     * 活动被创建。
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.w(TAG, "onCreate, 323, timestamp: " + System.currentTimeMillis()); //Debug.

        super.onCreate(savedInstanceState);

        Log.w(TAG, "onCreate, 328, timestamp: " + System.currentTimeMillis()); //Debug.


//        askShowSystemWallpaper(); //要求显示系统的墙纸在本活动后面。

        Log.w(TAG, "onCreate, 333, timestamp: " + System.currentTimeMillis()); //Debug.
        requestWindowFeature(Window.FEATURE_NO_TITLE); //不显示标题栏。

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Log.w(TAG, "onCreate, 338, timestamp: " + System.currentTimeMillis()); //Debug.
        }

        setContentView(R.layout.pin_shortcut_confirm_activity); //显示内容．

        Log.w(TAG, "onCreate, 343, timestamp: " + System.currentTimeMillis()); //Debug.
        ButterKnife.bind(this); //视图注入。

        Intent intent=getIntent(); //获取意图。

        if (intent!=null) //意图存在。
        {
            processIntent(intent); //处理意图。
        } //if (intent!=null) //意图存在。

        Log.w(TAG, "onCreate, 353, timestamp: " + System.currentTimeMillis()); //Debug.

        Log.w(TAG, "onCreate, 358, timestamp: " + System.currentTimeMillis()); //Debug.

        Log.w(TAG, "onCreate, 361, timestamp: " + System.currentTimeMillis()); //Debug.

        Log.w(TAG, "onCreate, 363, timestamp: " + System.currentTimeMillis()); //Debug.

        Log.w(TAG, "onCreate, 368, timestamp: " + System.currentTimeMillis()); //Debug.
        Log.w(TAG, "onCreate, 373, timestamp: " + System.currentTimeMillis()); //Debug.

        Log.w(TAG, "onCreate, 378, timestamp: " + System.currentTimeMillis()); //Debug.
//        loadSystemWallpaper(); //显示系统墙纸。
//        assessAutoRun(); //考虑要不要执行自动启动动作。

        Log.w(TAG, "onCreate, 383, timestamp: " + System.currentTimeMillis()); //Debug.
    } //protected void onCreate(Bundle savedInstanceState)

    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.d(TAG, "onNewIntent, intent: " + intent); //Debug.
        super.onNewIntent(intent);

        processIntent(intent);
    }

    private void processIntent(Intent intent)
    {
        String action=intent.getAction(); //获取动作。

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后都有钉住快捷方式的广播。
        {
            if (LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT.equals(action)) //钉住快捷方式。
            {
                LauncherApps launcherApps=(LauncherApps) (getSystemService(Context.LAUNCHER_APPS_SERVICE)); //获取启动器应用对象。

                LauncherApps.PinItemRequest pinItemRequest=launcherApps.getPinItemRequest(intent); //获取钉住请求对象。

                pinShortcut(pinItemRequest); //钉住快捷方式。
            } //if (LauncherApps.ACTION_CONFIRM_PIN_SHORTCUT.equals(action)) //钉住快捷方式。
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后都有钉住快捷方式的广播。
    }

    /**
     * 钉住快捷方式。
     * @param pinItemRequest 钉住请求对象。
     */
    private void pinShortcut(LauncherApps.PinItemRequest pinItemRequest)
    {
        Log.d(TAG, "pinShortcut."); //Debug.
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式的请求对象。
        {
            if (pinItemRequest.isValid()) //仍然有效
            {
                ShortcutInfo shortcutInfo=pinItemRequest.getShortcutInfo(); //获取快捷方式对象。

                showShortcutInfo(shortcutInfo); //显示与该个快捷方式相关的信息。图标和文字。

                HxLauncherApplication hxLauncherApplication=HxLauncherApplication.getInstance(); //获取应用程序对象．

                RuntimeInformationStore runtimeInformationStore=hxLauncherApplication.getRuntimeInformationStore(); //获取运行时信息存储对象．

                runtimeInformationStore.setShortcut(shortcutInfo); //记录快捷方式．

                broadcastPinShortcut(); //广播，钉住快捷方式．

//            mAdapter.addShortcut(shortcutInfo); //向适配器添加快捷方式。

//            mAdapter.notifyDataSetChanged(); //通知数据变更。

                pinItemRequest.accept(); //接受。

            } //if (pinItemRequest.isValid()) //仍然有效

        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式的请求对象。

    } //private void pinShortcut(LauncherApps.PinItemRequest pinItemRequest)

    /**
     * 显示与该个快捷方式相关的信息。图标和文字。
     * @param shortcutInfo 快捷方式信息。
     */
    private void showShortcutInfo(ShortcutInfo shortcutInfo)
    {
        Log.d(TAG, "showShortcutInfo, text: "); //Debug.

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式请求。
        {
            LauncherApps launcherApps=(LauncherApps)(getSystemService(Context.LAUNCHER_APPS_SERVICE));
//        int shortcutInfoIndex=position-articleInfoArrayList.size(); //获取快捷方式的下标。

//        ShortcutInfo shortcutInfo=shortcutInfoListManager.getShortcut(shortcutInfoIndex); //获取对应位置的快捷方式。

            String articleTitle=shortcutInfo.getShortLabel().toString(); //获取文章标题。

            Drawable applicationIcon=launcherApps.getShortcutIconDrawable(shortcutInfo, 0); //获取图标。

            applicationIconrightimageView2.setImageDrawable(applicationIcon); //显示图标。
            rightTextoperationMethodactTitletextView2.setText(articleTitle); //显示文字。

            Log.d(TAG, "showShortcutInfo, text: " + articleTitle); //Debug.
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式请求。
    } //private void showShortcutInfo(ShortcutInfo shortcutInfo)

    /**
     * 广播，钉住快捷方式．
     */
    private void broadcastPinShortcut()
    {
        Intent intent4Wrk = new Intent(); //创建意图对象。晚安诸位。
        Bundle extras=new Bundle(); //创建数据包。
//        extras.putString("remoteIp", remoteIp); //加入文本内容。
//		extras.put

//        byte[] messageContent=bb.getAllByteArray(); //获取全部数据。

//        extras.putByteArray("messageContent", messageContent); //加入消息内容。

        intent4Wrk.putExtras(extras); //设置附加数据。

        intent4Wrk.setAction(Constants.Operation.PinShortcut);// action与接收器相同

        Context appContext= HxLauncherApplication.getAppContext(); //获取应用程序上下文。
        LocalBroadcastManager lclBrdcstMngr=LocalBroadcastManager.getInstance(appContext); //Get the local broadcast manger instance.

        lclBrdcstMngr.sendBroadcast(intent4Wrk); //发送广播。

    } //private void broadcastPinShortcut()
}

