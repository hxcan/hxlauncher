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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class ApplicationAliasApplicationInformationAdapter extends RecyclerView.Adapter<ApplicationAliasApplicationInformationAdapter.ClipViewHolder>
{
    private static final String TAG="ApplicationAliasApplicationInformationAdapter"; //!< The tag used for debug code.

    private ArrayList<ArticleInfo> articleInfoArrayList; //!<文章信息列表。

    private final ApplicationAliasSettingsActivity context; //!<上下文。

    public static class ClipViewHolder extends RecyclerView.ViewHolder
    {
        public String activityName; //!<具体的活动名字。

        public String packageName; //!<应用包名。

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

        /**
         * 记住针对某个活动设置的别名。
         */
        @OnTextChanged(R.id.aliaseditText)
        public void rememberAliasForActivity(CharSequence aliasContent)
        {
//            context.updateLaunchCoolDownDuration();
            context.updateAliasForActivity(packageName, activityName, aliasContent.toString()); //更新别名。
        } //public void rememberAliasForActivity()

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

        private final ApplicationAliasSettingsActivity context; //!<上下文。

        @BindView(R.id.aliaseditText) EditText aliaseditText; //!<别名输入框

        @BindView(R.id.rightTextoperationMethodactTitletextView2) TextView mTextView; //!<文字视图。

        @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!<图标。

        public ClipViewHolder(View v, final ApplicationAliasSettingsActivity context1)
        {
            super(v);

            ButterKnife.bind(this,v); //视图注入。

            context=context1; //记录上下文。
        } //public ClipViewHolder(TextView v)
    }

    public ApplicationAliasApplicationInformationAdapter(ApplicationAliasSettingsActivity context1)
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
    public void onBindViewHolder(ApplicationAliasApplicationInformationAdapter.ClipViewHolder holder, int position)
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

        checkAndShowAlias(articleInfo, holder); //检查并显示别名。

    } //public void onBindViewHolder(ClipViewHolder holder,int position)

    /**
     * 检查并显示别名。
     * @param articleInfo 应用程序活动信息对象。
     * @param holder 视图容器。
     */
    private void checkAndShowAlias(ArticleInfo articleInfo, ApplicationAliasApplicationInformationAdapter.ClipViewHolder holder)
    {
        HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

        HashMap<String, String> packageItemLaunchCoolDownMap=application.getPackageItemAliasMap(); //获取映射。包条目信息字符串与别名字符串之间的映射。

        String alias=packageItemLaunchCoolDownMap.get(holder.packageName+"/"+holder.activityName); //获取映射的别名。



        if (alias!=null) //数据存在。
        {
            holder.aliaseditText.setText(alias); //显示别名。
        } //if (internationalizationData!=null) //数据存在。
        else //数据不存在
        {
            holder.aliaseditText.setText(R.string.empty); //显示别名。

        } //else //数据不存在
    } //private void checkAndShowAlias(ArticleInfo articleInfo, ClipViewHolder holder)

    /**
     * 检查并显示国际化名字。
     * @param articleInfo 应用程序信息对象。
     * @param holder 视图占位对象。
     */
    private void checkAndShowInternationalizationName(ArticleInfo articleInfo,  ApplicationAliasApplicationInformationAdapter.ClipViewHolder holder)
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
    public ApplicationAliasApplicationInformationAdapter.ClipViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.application_alias_application_info_row, parent,false); //从界面文件中创建出视图。

        ApplicationAliasApplicationInformationAdapter.ClipViewHolder clipViewHolder=new ApplicationAliasApplicationInformationAdapter.ClipViewHolder(v,context);
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





