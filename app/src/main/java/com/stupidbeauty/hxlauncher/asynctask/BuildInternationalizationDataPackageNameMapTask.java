package com.stupidbeauty.hxlauncher.asynctask;

import android.view.View;
import android.os.AsyncTask;
import com.stupidbeauty.hxlauncher.LauncherActivity;
import java.util.HashMap;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import java.util.List;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;

public class BuildInternationalizationDataPackageNameMapTask extends AsyncTask<Object, Void, Object>
{
  private HashMap<String, String> internationalizationDataPackageNameMap=new HashMap<>(); //映射。应用程序的国际化名字与包名之间的映射。

	private static final String TAG="BuildInternationalizationDataPackageNameMapTask"; //!<输出调试信息时使用的标记。

	private LauncherActivity voicePackageNameMap=null; //!< 启动活动。
	
  /**
    * 构造映射，应用的国际化名字与包名之间的映射。
    */
  private void buildInternationalizationDataPackageNameMap()
  {
    HxLauncherApplication application=HxLauncherApplication.getInstance(); //获取应用程序对象。

    ApplicationNameInternationalizationData internationalizationData=application.getApplicationNameInternationalizationData(); //获取国际化数据对象。

    List<ApplicationNamePair> applicationNames = internationalizationData.getApplicationNames(); //获取数据列表。

    for (ApplicationNamePair applicationNamePair: applicationNames) //一个个地加入映射。
    {
      internationalizationDataPackageNameMap.put(applicationNamePair.getReadableApplicationName(), applicationNamePair.getPackageName()); //加入映射。
    } //for (ApplicationNamePair applicationNamePair: applicationNames) //一个个地加入映射。
  } //private void buildInternationalizationDataPackageNameMap()

  @Override
  protected Object doInBackground(Object... params)
  {
    //参数顺序：

    Boolean result=false; //结果，是否成功。

    voicePackageNameMap=(LauncherActivity)(params[0]); //获取映射对象
            
    buildInternationalizationDataPackageNameMap(); // 构造映射。

    boolean addPhotoFile=false; //Whether to add photo file

    return internationalizationDataPackageNameMap;
  }

  /**
    * 报告结果。
    * @param result 结果。是否成功。
    */
  @Override
  protected void onPostExecute(Object result)
  {
    voicePackageNameMap.setInternationalizationDataPackageNameMap(internationalizationDataPackageNameMap);
  } //protected void onPostExecute(Boolean result)
}
