package com.stupidbeauty.hxlauncher;

import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.stupidbeauty.hxlauncher.asynctask.BuildInternationalizationDataPackageNameMapTask;
import com.stupidbeauty.hxlauncher.activity.ApplicationInformationActivity;
import com.stupidbeauty.hxlauncher.manager.NotificationDataManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stupidbeauty.msclearnfootball.AnswerAvailableEvent;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.VoicePackageMapJsonItem;
import com.stupidbeauty.hxlauncher.bean.VoicePackageUrlMapData;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.victoriafresh.VFile;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ActivityIconType;
import static com.stupidbeauty.hxlauncher.datastore.LauncherIconType.ShortcutIconType;

/**
 * 应用程序信息适配器。
 */
public class ApplicationInformationAdapter extends RecyclerView.Adapter<ApplicationInformationAdapter.ClipViewHolder>
{
    private  HashMap<String, String > packageNameApplicationNameMap; //!<包名与应用程序名的映射

    private HashMap<String, String> packageNameUrlMap; //!<包名与下载地址之间的映射关系。

    private HashMap<String, String> voicePackageUrlMap; //!<语音识别结果与包名之间的映射关系。

    private VoicePackageUrlMapData voicePackageUrlMapData; //!<语音识别结果与软件包下载地址之间的映射。

    private HashSet<String> iconNoCachePackageNameSet=new HashSet<>(); //!<不应当缓存其图标的软件包名集合
    private boolean builtinShortcutsVisible=false; //!<内置快捷方式是否可见。
    private HashMap<String, Integer> packageNameItemNamePositionMap=new HashMap<>(); //!<包名加类名的字符串与图标位置之间的映射。
    private HashMap<String, Integer> packageNamePositionMap=new HashMap<>(); //!<包名字符串与图标位置之间的映射。
    private static final String TAG="ApplicationInformationA"; //!<输出调试信息时使用的标记。
    private ShortcutInfoListManager shortcutInfoListManager; //!<快捷方式列表管理器。
//    private ArrayList<ShortcutInfo>

    private final LauncherActivity context; //!<上下文。
    
    /**
    * Clear shortcuts.
    */
    public void clearShortcuts()
    {
      shortcutInfoListManager.clearShortcuts(); // clear shortcuts.
    } // public void clearShortcuts()

    /**
     * 添加快捷方式。
     * @param shortcutInfo 要添加的快捷方式。
     */
    public void addShortcut(ShortcutInfo shortcutInfo)
    {
        Log.d(TAG, "addShortcut, this: " + this); //Debug.
        shortcutInfoListManager.addShortcut(shortcutInfo); //添加到列表中。
    } //private void addShortcut(ShortcutInfo shortcutInfo)

    /**
     * 设置文件信息列表。
     * @param articleInfoArrayList 要设置的文件信息列表。
     */
    public void setArticleInfoArrayList(ArrayList<ArticleInfo> articleInfoArrayList)
    {
        this.articleInfoArrayList = articleInfoArrayList; //记录列表。

        updateItemPositionMap(); //整体更新条目的位置映射
    } //public void setArticleInfoArrayList(ArrayList<ArticleInfo> articleInfoArrayList)

    /**
     * 切换内置快捷方式的可见性。
     * @param uid 是否可见。
     */
    public void toggleBuiltinShortcutsVisible(boolean uid)
    {
        builtinShortcutsVisible=uid; //记录。

        updateItemPositionMap(); //整体更新条目的位置映射
    } //public void toggleBuiltinShortcutsVisible(boolean uid)

    private ArrayList<ArticleInfo> articleInfoArrayList; //!<文章信息列表。
    private ArrayList<ArticleInfo> builtinShortcuts; //!<内置快捷方式列表。

    /**
     * 设置内置快捷方式列表
     * @param builtinShortcuts 内置快捷方式列表
     */
    public void setBuiltinShortcuts(ArrayList<ArticleInfo> builtinShortcuts)
    {

        this.builtinShortcuts = builtinShortcuts;

        updateItemPositionMap(); //整体更新条目的位置映射

    } //public void setBuiltinShortcuts(ArrayList<ArticleInfo> builtinShortcuts)

    public static class ClipViewHolder extends RecyclerView.ViewHolder
    {
      public LauncherIconType iconType; //!<图标类型。

      /**
      * 显示应用信息
      */
      @OnLongClick({R.id.homeNewsLayout,R.id.launchRipple})
      public boolean  showApplicationInformation()
      {
        launcherActivity.showApplicationInformation(itemView, packageName, activityName); //显示应用程序信息活动

        return true;
      } //public void showApplicationInformation()

      @OnClick({R.id.homeNewsLayout,R.id.launchRipple})
      /**
      * 启动应用。
      */
      public void launchApplication()
      {
        launcherActivity.animateHeart(itemView.getX(), itemView.getY()); //显示点赞爱心动画

        if (iconType==ActivityIconType) //是活动。
        {
          launchAsActivity();
        } //if (iconType==ActivityIconType) //是活动。
        else //是快捷方式。
        {
          launchAsShortcut(); //以快捷方式的形式启动。
        } //else //是快捷方式。
      } //public void launchApplication()

      /**
      * 以快捷方式的形式启动。
      */
      private void launchAsShortcut()
      {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25之后才有快捷方式编号。
        {
          Log.d(TAG, "launchAsShortcut, package name: " + packageName + ", shortcut id: "+ shortcutInfo.getId()); //Debug.

                AnswerAvailableEvent answerAvailableEvent = new AnswerAvailableEvent(packageName, activityName); //创建事件消息对象．
                answerAvailableEvent.setIconType(iconType); //设置图标类型．
                answerAvailableEvent.setShortcutId(shortcutInfo.getId()); //设置快捷方式编号．

//                bus.post(answerAvailableEvent); //发布消息。

                launcherActivity.answerAvailable(answerAvailableEvent); //通知，点击了图标

                launcherActivity.launchShortcut(shortcutInfo); //启动快捷方式。

            } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25之后才有快捷方式编号。

        } //private void launchAsShortcut()

        /**
         * 以活动的方式启动．
         */
        private void launchAsActivity()
        {
//            Log.d(TAG, "185, launchAsActivity, PAKCAGE name: " + packageName + ", activityy name: " + activityName + ", bus: " + bus); //Debug.

            AnswerAvailableEvent answerAvailableEvent = new AnswerAvailableEvent(packageName, activityName); //创建事件消息对象．
            answerAvailableEvent.setIconType(iconType); //设置图标类型．

            launcherActivity.answerAvailable(answerAvailableEvent); //通知，点击了图标

//            bus.post(answerAvailableEvent); //发布消息。

            try //尝试启动活动，并且捕获可能的异常。
            {
                launcherActivity.launchApplication(launchIntent); //启动活动。
            }
            catch (ActivityNotFoundException exception)
            {
                exception.printStackTrace(); //报告错误。
            } //catch (ActivityNotFoundException exception)
        } //private void launchAsActivity()

        public String packageName; //!<应用包名。
        public String activityName; //!<具体的活动名字。
//        public Bus bus; //!<消息总线。
        public int clipId=0; //!<本视频对应的剪辑编号。
        public Intent launchIntent; //!<用于启动应用程序的意图。
        public ShortcutInfo shortcutInfo; //!<用于启动快捷方式的快捷方式信息对象。

        /**
         * 将文字加入队列中。
         * @param buttongetText 文字。
         */
        private void enqueueText(String buttongetText)
        {

            interactiveText=interactiveText+buttongetText; //加入到文字后面。
        } //private void enqueueText(String buttongetText)

        private String interactiveText=""; //!<手工校正的交互过程中的文字字符串。


        private void startCallTimer() {
            applyInteractiveText(); //应用交互过程中产生的文字。
        }

        /**
         * 应用交互过程中产生的文字。
         */
        private void applyInteractiveText()
        {
            mTextView.setText(interactiveText); //修改文字。

            interactiveText=""; //交互过程中累积的文字清空。
        } //private void applyInteractiveText()

        private final LauncherActivity launcherActivity; //!<上下文。

        @BindView(R.id.rightTextoperationMethodactTitletextView2) TextView mTextView; //!<文字视图。
        @BindView(R.id.badgeNumbertView2) TextView badgeNumbertView2; //!< Badge number.
        @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!<图标。
        @BindView(R.id.voiceAssistantLayout) RelativeLayout voiceAssistantLayout; //!< The layout of notificatin badge.

        public ClipViewHolder(View v, LauncherActivity context1)
        {
          super(v);

          ButterKnife.bind(this,v); //视图注入。

          launcherActivity =context1; //记录上下文。
        } //public ClipViewHolder(TextView v)
    }

    /**
     * 构造函数
     * @param context1 启动活动对象
     */
    public ApplicationInformationAdapter(LauncherActivity context1)
    {
        context=context1; //记录上下文。

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式请求对象。
        {
            shortcutInfoListManager=new ShortcutInfoListManager(); //创建管理器。
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式请求对象。

        loadBuiltinVoiceShortcutMap(); //载入不可缓存的包名集合
    } //public ApplicationInformationAdapter(LauncherActivity context1)

    /**
     * 载入内置的语音识别结果与快捷方式信息之间的映射。
     */
    private void loadBuiltinVoiceShortcutMap()
    {
        Locale locale=Locale.getDefault(); //获取默认语系。

        String androidLocaleName=locale.toString(); //获取语系名字。

        String qrcFileName="voicePackageUrlMap.json"; //文件名。
        String fullQrcFileName=":/IconNoCachePackageNameList/"+qrcFileName; //构造完整的qrc文件名。

        VFile qrcHtmlFile=new VFile(context, fullQrcFileName); //qrc网页文件。

        String fileContent=qrcHtmlFile.getFileTextContent(); //获取文件的完整内容。

        Gson gson=new Gson();

        voicePackageUrlMapData = gson.fromJson(fileContent, VoicePackageUrlMapData.class); //解析。

        voicePackageUrlMap=new HashMap<>(); //创建映射。
        packageNameUrlMap=new HashMap<>(); //创建映射
        packageNameApplicationNameMap=new HashMap<>(); //创建映射

        if (voicePackageUrlMapData!=null) //解析得到的映射数据不为空。
        {
            for(VoicePackageMapJsonItem currentItem: voicePackageUrlMapData.getVoicePackageMapJsonItemList()) //一个个地添加。
            {
                iconNoCachePackageNameSet.add(currentItem.getPackageName()); //加入包名。陈欣
            } //for(VoicePackageMapJsonItem currentItem: voicePackageUrlMapData.getVoicePackageMapJsonItemList()) //一个个地添加。
        } //if (voicePackageUrlMapData!=null) //解析得到的映射数据不为空。
    } //private void loadBuiltinVoiceShortcutMap()

    /**
     * 创建视图占位器。
     * @param parent 亲代视图。
     * @param viewType 视图类型。
     * @return 创建的占位器。
     */
    public ClipViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
      View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.application_info_row,parent,false);

      ClipViewHolder clipViewHolder=new ClipViewHolder(v,context);

      return clipViewHolder;
    } //public ClipViewHolder onCreateViewHolder(ViewGroup parent,int viewType)

    /**
     * 获取活动条目的位置。
     * @param packageItemInfopackageName 包名。
     * @param packageItemInfoname 活动类名。
     * @return 图标的位置编号。
     */
    public int getItemPosition(String packageItemInfopackageName, String packageItemInfoname)
    {
      Log.d(TAG, "getItemPosition, package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname); //Debug.
      Log.d(TAG, "getItemPosition, packageNameItemNamePositionMap: " + packageNameItemNamePositionMap); //Debug.
      int result=0; //结果。

      if (packageItemInfoname!=null) // 有类名。
      {
        result = getItemPositionByPackageAndComponentName(packageItemInfopackageName, packageItemInfoname); // Get position by package and component name.
        
        Log.d(TAG, "getItemPosition, package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname + ", result: " + result); //Debug.
      } //if (packageItemInfoname!=null) //有类名。
      else //没有类名。
      {
        result = getItemPositionByPacakgeName(packageItemInfopackageName); // Get from thee pacakge name map.
        
        Log.d(TAG, "getItemPosition, package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname + ", result: " + result); //Debug.
      } //else //没有类名。

      return result;
    } //public int getItemPosition(String packageItemInfopackageName, String packageItemInfoname)
    
    /**
    * Get position by package and component name.
    */
    private int getItemPositionByPackageAndComponentName(String packageItemInfopackageName, String packageItemInfoname) 
    {
      // Log.d(TAG, CodePosition.newInstance().toString() + ", events: "  + usageEvents + ", has next event?:" + usageEvents.hasNextEvent()); //Debug.

      Log.d(TAG, CodePosition.newInstance().toString() + ", package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname); //Debug.
      int result=0; //结果。
      Integer positionObject = packageNameItemNamePositionMap.get(packageItemInfopackageName + "/" + packageItemInfoname);

      if (positionObject!=null) //有结果
      {
      Log.d(TAG, CodePosition.newInstance().toString() + ", package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname); //Debug.
        result=positionObject; //从映射中查找。
      } //if (positionObject!=null) //有结果
      else // No result from package name and component name.
      {
      Log.d(TAG, CodePosition.newInstance().toString() + ", package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname); //Debug.
        result = getItemPositionByPacakgeName(packageItemInfopackageName); // Get from thee pacakge name map.
      } // else // No result from package name and component name.
        
      Log.d(TAG, CodePosition.newInstance().toString() + ", package name: " + packageItemInfopackageName + ", activity name: " + packageItemInfoname); //Debug.
      return result;
    } // private int getItemPositionByPackageAndComponentName(String packageItemInfopackageName, String packageItemInfoname)
    
    /**
    * Get from thee pacakge name map.
    */
    private int getItemPositionByPacakgeName(String packageItemInfopackageName)
    {
      int result=0; //结果。
        Integer positionInteger=packageNamePositionMap.get(packageItemInfopackageName); //从映射中查找。

        if (positionInteger!=null) //有条目。
        {
          result=positionInteger; //从映射中查找。
        } //if (positionInteger!=null) //有条目。
      return result;
    } // private int getItemPositionByPacakgeName(String packageItemInfopackageName)

    /**
     * 绑定视图占位器。
     * @param holder 占位器。
     * @param position 位置。
     */
    public void onBindViewHolder(ClipViewHolder holder, int position)
    {
      int formalShortcutAmount=0; //正式快捷方式个数。

      if (shortcutInfoListManager!=null) //有正式快捷方式。
      {
        formalShortcutAmount=shortcutInfoListManager.getShortcutAmount();
      } //if (shortcutInfoListManager!=null) //有正式快捷方式。

      if (position<articleInfoArrayList.size()) //活动。
      {
        bindViewHolderActivity(holder, position); //针对活动，绑定界面元素。
      } //if (position<articleInfoArrayList.size()) //活动。
      else if (position< (articleInfoArrayList.size() + formalShortcutAmount)) //快捷方式。
      {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式的接口。
        {
          LauncherApps launcherApps=(LauncherApps)(context.getSystemService(Context.LAUNCHER_APPS_SERVICE));
          int shortcutInfoIndex=position-articleInfoArrayList.size(); //获取快捷方式的下标。

          ShortcutInfo shortcutInfo=shortcutInfoListManager.getShortcut(shortcutInfoIndex); //获取对应位置的快捷方式。

          String articleTitle=shortcutInfo.getShortLabel().toString(); //获取文章标题。

          Drawable applicationIcon=launcherApps.getShortcutIconDrawable(shortcutInfo, 0); //获取图标。

          holder.mTextView.setText(articleTitle); //设置新的文字内容。
        holder.voiceAssistantLayout.setVisibility(View.INVISIBLE);

          Glide.with(context).load("").placeholder(applicationIcon).into(holder.applicationIconrightimageView2); //显示图标。

          holder.packageName=shortcutInfo.getPackage(); //获取包名。
          holder.activityName=shortcutInfo.getActivity().getClassName(); //获取活动名字。
          holder.iconType=ShortcutIconType; //类型是快捷方式。
          holder.shortcutInfo=shortcutInfo; //记录快捷方式。
        } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //26之后才有钉住快捷方式的接口。
      } //else //快捷方式。
      else //内置快捷方式。
      {
        bindViewHolderActivityBuiltinShortcuts(holder, position); //针对内置快捷方式的活动，绑定界面元素。
      } //else //内置快捷方式。

    } //public void onBindViewHolder(ClipViewHolder holder,int position)

    /**
     * 针对内置快捷方式的活动，绑定界面元素。
     * @param holder 视图容器。
     * @param wholePosition 整体的位置。
     */
    private void bindViewHolderActivityBuiltinShortcuts(ClipViewHolder holder, int wholePosition)
    {
        int formalShortcutAmount=0;

        if (shortcutInfoListManager!=null)
        {
            formalShortcutAmount=shortcutInfoListManager.getShortcutAmount();

        } //if (shortcutInfoListManager!=null)


        int position=wholePosition-articleInfoArrayList.size()-formalShortcutAmount; //计算出在内置快捷方式列表中的位置。
        ArticleInfo articleInfo=builtinShortcuts.get(position); //获取对应位置的文章。

        String articleTitle=articleInfo.getApplicationLabel().toString(); //获取文章标题。

        Drawable applicationIcon=getApplicationIcon(articleInfo); //获取图标。
        Intent applicationIntent=articleInfo.getLaunchIntent(); //获取启动意图。

        holder.mTextView.setText(articleTitle); //设置新的文字内容。

        Glide.with(context).load("").placeholder(applicationIcon).into(holder.applicationIconrightimageView2); //显示图标。

        holder.launchIntent=applicationIntent; //设置启动意图。
        holder.packageName=articleInfo.getPackageName(); //获取包名。
        holder.activityName=articleInfo.getActivityName(); //获取活动名字。
        holder.voiceAssistantLayout.setVisibility(View.INVISIBLE);



        holder.iconType=ActivityIconType; //类型是活动。

//        packageNameItemNamePositionMap.put(holder.packageName+"/"+holder.activityName, position); //从映射中查找。



        checkAndShowInternationalizationName(articleInfo, holder); //检查并显示国际化名字。

        checkAndShowAlias(articleInfo, holder); //检查并显示别名。
    } //private void bindViewHolderActivityBuiltinShortcuts(ClipViewHolder holder, int wholePosition)

    /**
     * 整体更新条目的位置映射
     */
    public void updateItemPositionMap()
    {
      int position=0; //记录的位置。

      if (articleInfoArrayList!=null) //指针有效。
      {
        for(ArticleInfo articleInfo: articleInfoArrayList) //一个个地绑定
        {
          String packageName =  articleInfo.getPackageName(); // Get the packag ename.
          packageNameItemNamePositionMap.put(packageName+"/"+articleInfo.getActivityName(), position); //从映射中查找。

          if (packageNamePositionMap.containsKey(packageName)) // Already has this one.
          {
          } // if (packageNamePositionMap.contains(articleInfo.getPackageName())) // Already has this one.
          else // Add it.
          {
            packageNamePositionMap.put(packageName, position); //记录映射。
          } // else // Add it.
          
          position++;
        } //for(ArticleInfo articleInfo: articleInfoArrayList) //一个个地绑定
      } //if (articleInfoArrayList!=null) //指针有效。

      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25之后才有快捷方式编号。
      {
        if (shortcutInfoListManager!=null) //快捷方式管理器有效。
        {
          int shortcutAmount=shortcutInfoListManager.getShortcutAmount(); //获取快捷方式个数。
          int shortcutCounter=0;

          for(shortcutCounter=0; shortcutCounter< shortcutAmount; shortcutCounter++) //一个个地映射
          {
            ShortcutInfo shortcutInfo=shortcutInfoListManager.getShortcut(shortcutCounter);

            packageNameItemNamePositionMap.put(shortcutInfo.getPackage()+"/"+shortcutInfo.getId(), position); //从映射中查找。


            position++;
          } //for(shortcutCounter=0; shortcutCounter< shortcutAmount; shortcutCounter++) //一个个地映射
        } //if (shortcutInfoListManager!=null) //快捷方式管理器有效。
      } //if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) //25之后才有快捷方式编号。

      if (builtinShortcuts!=null) //有内置快捷方式。
      {
        if (builtinShortcutsVisible) //内置快捷方式可见。
        {
          for(ArticleInfo articleInfo: builtinShortcuts) //一个个地绑定
          {
            packageNameItemNamePositionMap.put(articleInfo.getPackageName()+"/"+articleInfo.getActivityName(), position); //从映射中查找。

            position++;
          } //for(ArticleInfo articleInfo: articleInfoArrayList) //一个个地绑定
        } //if (builtinShortcutsVisible) //内置快捷方式可见。
      } //if (builtinShortcuts!=null) //有内置快捷方式。
    } //public void updateItemPositionMap()

    /**
     * 针对活动，绑定界面元素。
     * @param holder 视图容器。
     * @param position 位置。
     */
    private void bindViewHolderActivity(ClipViewHolder holder, int position)
    {
      ArticleInfo articleInfo=articleInfoArrayList.get(position); //获取对应位置的文章。

      String articleTitle=articleInfo.getApplicationLabel().toString(); //获取文章标题。

      Drawable applicationIcon=getApplicationIcon(articleInfo); //获取图标。
      Intent applicationIntent=articleInfo.getLaunchIntent(); //获取启动意图。

      holder.mTextView.setText(articleTitle); //设置新的文字内容。

      Glide.with(context).load("").placeholder(applicationIcon).into(holder.applicationIconrightimageView2); //显示图标。

      holder.launchIntent=applicationIntent; //设置启动意图。
      holder.packageName=articleInfo.getPackageName(); //获取包名。
      holder.activityName=articleInfo.getActivityName(); //获取活动名字。
      holder.iconType=ActivityIconType; //类型是活动。


      checkAndShowInternationalizationName(articleInfo, holder); //检查并显示国际化名字。

      checkAndShowAlias(articleInfo, holder); //检查并显示别名。
        
      checkAndShowBadgeNumber(articleInfo, holder); // Check and show badget number
    } //private void bindViewHolderActivity(ClipViewHolder holder, int position)
    
    /**
    * Check and show badget number
    */
    private void checkAndShowBadgeNumber(ArticleInfo articleInfo, ClipViewHolder holder)
    {
      HxLauncherApplication baseApplication = HxLauncherApplication.getInstance(); //获取应用程序对象。
      NotificationDataManager notificationDataManager = baseApplication.getNotificationDataManager(); // Get the notificaiton data manager.
      
      int badgetNumber = notificationDataManager.getNotificationAmount( holder.packageName ); // Get the notification amount.

      if (badgetNumber > 0) // There is at least one notification
      {
        holder.voiceAssistantLayout.setVisibility(View.VISIBLE);
        
        holder.badgeNumbertView2.setText("" + badgetNumber); // Show the number.

      } // if (badgetNumber > 0) // There is at least one notification
      else // No notifications.
      {
        holder.voiceAssistantLayout.setVisibility(View.INVISIBLE);
      } // else // No notifications.
    } // private void checkAndShowBadgeNumber(ArticleInfo articleInfo, ClipViewHolder holder)

    /**
     * 检查并显示别名。
     * @param articleInfo 应用程序活动信息对象。
     * @param holder 视图容器。
     */
    private void checkAndShowAlias(ArticleInfo articleInfo, ClipViewHolder holder)
    {
        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

        HashMap<String, String> packageItemLaunchCoolDownMap=application.getPackageItemAliasMap(); //获取映射。包条目信息字符串与别名字符串之间的映射。

        if (packageItemLaunchCoolDownMap!=null)
        {
        String alias=packageItemLaunchCoolDownMap.get(holder.packageName+"/"+holder.activityName); //获取映射的别名。

        if (alias!=null) //数据存在。
        {
            if (alias.isEmpty()) //内容是空的
            {
            } //if (alias.isEmpty()) //内容是空的
            else //内容不是空的
            {
                holder.mTextView.setText(alias); //显示别名。
            } //else //内容不是空的
        } //if (internationalizationData!=null) //数据存在。
        } //if (packageItemLaunchCoolDownMap!=null)
    } //private void checkAndShowAlias(ArticleInfo articleInfo, ClipViewHolder holder)

    /**
     * 检查并显示国际化名字。
     * @param articleInfo 应用程序信息对象。
     * @param holder 视图占位对象。
     */
    private void checkAndShowInternationalizationName(ArticleInfo articleInfo,  ClipViewHolder holder)
    {
      HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

      ApplicationNameInternationalizationData internationalizationData=application.getApplicationNameInternationalizationData(); //获取国际化数据对象。

      if (internationalizationData!=null) //数据存在。
      {
        String internationalizationName=internationalizationData.getInternationalizationName(holder.packageName); //获取国际化名字。

        if (internationalizationName!=null) //有国际化名字。
        {
          holder.mTextView.setText(internationalizationName); //显示国际化名字。
        } //if (internationalizationName!=null) //有国际化名字。
      } //if (internationalizationData!=null) //数据存在。
    } //private void checkAndShowInternationalizationName(ArticleInfo articleInfo,  ClipViewHolder holder)

    /**
     * 获取由系统提供的应用程序图标。
     * @param articleInfo 应用程序信息。
     * @return 由系统提供的应用程序图标。
     */
    private Drawable getSystemProvidedApplicationIcon(ArticleInfo articleInfo)
    {
        Drawable result; //结果。

        String packageName=articleInfo.getPackageName(); //获取应用程序包名。

        String activityName=articleInfo.getActivityName(); //获取活动名字


        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

        HashMap<String,Drawable> launchIconMap=application.getLaunchIconMap(); //获取启动图标缓存。

        result=launchIconMap.get(packageName + "/" + activityName); //获取缓存绘图对象。

        if (result==null) //未缓存。
        {
            PackageManager packageManager=context.getPackageManager(); //获取软件包管理器。

            try //读取图标内容
            {
                PackageInfo packageInfo=packageManager.getPackageInfo(packageName,0); //获取应用程序信息。

                ApplicationInfo applicationInfo=packageInfo.applicationInfo; //获取应用程序信息。

//                result=packageManager.getApplicationIcon(applicationInfo); //获取图标。


                ComponentName componentName=new ComponentName(packageName, activityName); //创建部件名字对象

                result=packageManager.getActivityIcon(componentName); //获取活动的图标

                if (iconNoCachePackageNameSet.contains(packageName)) //不应当缓存
                {
                } //if (iconNoCachePackageNameSet.contains(packageName)) //不应当缓存
                else //可以缓存
                {
                    launchIconMap.put(packageName + "/" + activityName, result); //加入缓存。
                } //else //可以缓存
            } //try //读取图标内容
            catch (PackageManager.NameNotFoundException e) //未找到该应用程序包。
            {
                e.printStackTrace(); //报告错误。
            } //catch (PackageManager.NameNotFoundException e) //未找到该应用程序包。
            catch (OutOfMemoryError outOfMemoryError)
            {
                outOfMemoryError.printStackTrace(); //报告错误。
            } //catch (OutOfMemoryError outOfMemoryError)
        } //if (result==null) //未缓存。

        return result;
    } //private Drawable getSystemProvidedApplicationIcon(ArticleInfo articleInfo)

    /**
     * 获取图标。
     * @param articleInfo 应用程序信息。
     * @return 应用程序的启动图标。
     */
    private Drawable getApplicationIcon(ArticleInfo articleInfo)
    {
      Drawable result; //结果。

      result=getBuiltinApplicationIcon(articleInfo); //尝试载入内置的应用程序图标。

      if (result==null) //没有内置的应用程序图标。
      {
        result=getSystemProvidedApplicationIcon(articleInfo); //获取由系统提供的应用程序图标。
      } //if (result==null) //没有内置的应用程序图标。

      return result;
    } //private Drawable getApplicationIcon(ArticleInfo articleInfo)

    /**
     * 尝试载入内置的应用程序图标。
     * @param articleInfo 应用程序信息。
     * @return 内置的应用程序图标。
     */
    private  Drawable getBuiltinApplicationIcon(ArticleInfo articleInfo)
    {
      Drawable result=null; //结果。

      String packageName=articleInfo.getPackageName(); //获取包名。
      String qrcFileName=packageName; //文件名。
      String fullQrcFileName=":/ApplicationIcon/"+qrcFileName; //构造完整的qrc文件名。

      VFile qrcHtmlFile=new VFile(context, fullQrcFileName); //qrc网页文件。

      if (qrcHtmlFile.exists()) //文件存在。
      {
          byte[] photoBytes= qrcHtmlFile.getFileContent(); //将照片文件内容全部读取。

          ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(photoBytes);


          result= Drawable.createFromStream(byteArrayInputStream, "JPEG"); //从文件中解码。
      } //if (qrcHtmlFile.exists()) //文件存在。

      return result;
    } //private  Drawable getBuiltinApplicationIcon(ArticleInfo articleInfo)

    /**
     * 获取条目个数。
     * @return 条目个数。
     */
    public int getItemCount()
    {
        int result=0; //个数。

        if (articleInfoArrayList!=null) //指针有效。
        {
            result=articleInfoArrayList.size();

        } //if (articleInfoArrayList!=null) //指针有效。

        if (shortcutInfoListManager!=null) //快捷方式管理器有效。
        {
            int shortcutAmount=shortcutInfoListManager.getShortcutAmount(); //获取快捷方式个数。

            result=result+shortcutAmount; //加上快捷方式个数。
        } //if (shortcutInfoListManager!=null) //快捷方式管理器有效。

        if (builtinShortcuts!=null) //有内置快捷方式。
        {
            if (builtinShortcutsVisible) //内置快捷方式可见。
            {
                result=result+builtinShortcuts.size(); //加上内置快捷方式的数量。

            } //if (builtinShortcutsVisible) //内置快捷方式可见。
        } //if (builtinShortcuts!=null) //有内置快捷方式。

        return result;
    } //public int getItemCount()
} //public class SettingTabAboutActivity extends Activity implements OnClickListener
