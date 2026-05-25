package com.stupidbeauty.hxlauncher;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationFrequencyApplicationInformationAdapter extends RecyclerView.Adapter<ApplicationFrequencyApplicationInformationAdapter.ClipViewHolder>
{
    private static final String TAG="AutoRunApplicationInfor"; //!<输出调试信息时使用的标记。

    private ArrayList<ArticleInfo> articleInfoArrayList; //!<文章信息列表。

//    private Bus bus; //!<总线 。

    private final ApplicationFrequencySettingsActivity context; //!<上下文。

    public static class ClipViewHolder extends RecyclerView.ViewHolder
    {
        public String activityName; //!<具体的活动名字。

        public String packageName; //!<应用包名。
//        public Bus bus; //!<消息总线。
        public int clipId=0; //!<本视频对应的剪辑编号。
        public Intent launchIntent; //!<用于启动应用程序的意图。

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



        private final ApplicationFrequencySettingsActivity context; //!<上下文。

        @BindView(R.id.durationSeekBar) TickSeekBar durationSeekBar; //!<间隔时长寻找条。

        @BindView(R.id.rightTextoperationMethodactTitletextView2) TextView mTextView; //!<文字视图。

        @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!<图标。

        public ClipViewHolder(View v, final ApplicationFrequencySettingsActivity context1)
        {
          super(v);

          ButterKnife.bind(this,v); //视图注入。

          context=context1; //记录上下文。

          durationSeekBar.setOnSeekChangeListener
          (
            new OnSeekChangeListener() 
            {
              @Override
              public void onSeeking(SeekParams seekParams) 
              {

              }

              @Override
              public void onStartTrackingTouch(TickSeekBar seekBar) 
              {

              }

              @Override
              public void onStopTrackingTouch(TickSeekBar seekBar) 
              {
                int seekBarValue=seekBar.getProgress(); //获取当前的值。

                context.updateLaunchCoolDownDuration(packageName, activityName, seekBarValue); //更新启动冷却时间数据。
              } // public void onStopTrackingTouch(TickSeekBar seekBar) 
            });
        } //public ClipViewHolder(TextView v)
    }

    public ApplicationFrequencyApplicationInformationAdapter(ApplicationFrequencySettingsActivity context1)
    {
        context=context1; //记录上下文。

    }

//    public void setBus(Bus bus) {
//        this.bus = bus;
//    }

    /**
     * 设置文件信息列表。
     * @param articleInfoArrayList 要设置的文件信息列表。
     */
    public void setArticleInfoArrayList(ArrayList<ArticleInfo> articleInfoArrayList)
    {
        this.articleInfoArrayList = articleInfoArrayList; //记录列表。
    } //public void setArticleInfoArrayList(ArrayList<ArticleInfo> articleInfoArrayList)


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

        return result;

    } //public int getItemCount()

    /**
     * 绑定视图占位器。
     * @param holder 占位器。
     * @param position 位置。
     */
    public void onBindViewHolder(ApplicationFrequencyApplicationInformationAdapter.ClipViewHolder holder, int position)
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

      boolean autoRun=articleInfo.isAutoRun(); //是否自动启动。

      Log.d(TAG, "onBindViewHolder, package name: "+articleInfo.getPackageName()+", application name: "+ articleTitle+", class name:"+ applicationIntent.getComponent().getClassName()); //Debug.

      checkAndShowInternationalizationName(articleInfo, holder); //检查并显示国际化名字。
      
      HxLauncherApplication hxLauncherApplication= HxLauncherApplication.getInstance(); //获取应用程序对象。
      HashMap<Integer, Integer> seekBarValueCoolDownTimeMap = hxLauncherApplication.getSeekBarValueCoolDownTimeMap(); //获取映射。
      HashMap<Integer, Integer> coolDownTimeSeekBarValueMap = hxLauncherApplication.getCoolDownTimeSeekBarValueMap(); // Get the map of cool down time to seek bar value.

      HashMap<String, Integer> packageItemLaunchCoolDownMap=hxLauncherApplication.getPackageItemLaunchCoolDownMap(); //获取映射。包条目信息字符串与实际冷却时间秒数之间的映射。

      if (packageItemLaunchCoolDownMap!=null) // 映射存在。
      {
        if (packageItemLaunchCoolDownMap.containsKey( holder.packageName + "/" + holder.activityName)) //有这个键。
        {
          // Chen xin. set the seek bar value according to cool down time.
          
          int launchCoolDownTime=packageItemLaunchCoolDownMap.get(holder.packageName + "/" + holder.activityName); //获取冷却时间。

          // int seekBarValue = seekBarValueCoolDownTimeMap.key(launchCoolDownTime); // Get the seek bar value.
          int seekBarValue = coolDownTimeSeekBarValueMap.get(launchCoolDownTime); // Get the seek bar value.
          
          holder.durationSeekBar.setProgress(seekBarValue); // Choose the value.
        } // if (packageItemLaunchCoolDownMap.containsKey(packageName+"/"+activityName)) //有这个键。
      } // if (packageItemLaunchCoolDownMap!=null) // 映射存在。
    } //public void onBindViewHolder(ClipViewHolder holder,int position)

    /**
     * 检查并显示国际化名字。
     * @param articleInfo 应用程序信息对象。
     * @param holder 视图占位对象。
     */
    private void checkAndShowInternationalizationName(ArticleInfo articleInfo,  ApplicationFrequencyApplicationInformationAdapter.ClipViewHolder holder)
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
     * 创建视图占位器。
     * @param parent 亲代视图。
     * @param viewType 视图类型。
     * @return 创建的占位器。
     */
    public ApplicationFrequencyApplicationInformationAdapter.ClipViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.application_frequency_application_info_row, parent,false); //从界面文件中创建出视图。

        ApplicationFrequencyApplicationInformationAdapter.ClipViewHolder clipViewHolder=new ApplicationFrequencyApplicationInformationAdapter.ClipViewHolder(v,context);
//        clipViewHolder.bus=bus;

        return clipViewHolder;
    } //public ClipViewHolder onCreateViewHolder(ViewGroup parent,int viewType)

    /**
     * 获取图标。
     * @param articleInfo 应用程序信息。
     * @return 应用程序的启动图标。
     */
    private Drawable getApplicationIcon(ArticleInfo articleInfo)
    {
        Drawable result; //结果。

        String packageName=articleInfo.getPackageName(); //获取应用程序包名。

        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

        HashMap<String,Drawable> launchIconMap=application.getLaunchIconMap(); //获取启动图标缓存。

        result=launchIconMap.get(packageName); //获取缓存绘图对象。

        if (result==null) //未缓存。
        {
            PackageManager packageManager=context.getPackageManager(); //获取软件包管理器。

            try
            {
                PackageInfo packageInfo=packageManager.getPackageInfo(packageName,0); //获取应用程序信息。

                ApplicationInfo applicationInfo=packageInfo.applicationInfo; //获取应用程序信息。

                result=packageManager.getApplicationIcon(applicationInfo); //获取图标。

                launchIconMap.put(packageName,result); //加入缓存。
            }
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
    } //private Drawable getApplicationIcon(ArticleInfo articleInfo)


}





