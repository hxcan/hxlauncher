package com.stupidbeauty.hxlauncher.asynctask;

import java.util.Locale;
import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import com.google.gson.Gson;
import com.stupidbeauty.hxlauncher.Constants;
import com.stupidbeauty.hxlauncher.PackageItemAliasMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.PackageItemAliasMapMessageProtos;
import com.stupidbeauty.hxlauncher.PackageItemLaunchCoolDownMapItemMessageProtos;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.asynctask.VoiceAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.asynctask.VoiceShortcutAssociationDataSendTask;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.ApplicationNamePair;
import com.stupidbeauty.victoriafresh.VFile;
import com.stupidbeauty.hxlauncher.rpc.CloudRequestorZzaqwb;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
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
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.hxlauncher.bean.VoicePackageMapJsonItem;
import com.stupidbeauty.hxlauncher.bean.VoicePackageUrlMapData;
import com.stupidbeauty.hxlauncher.bean.WakeLockPackageNameSetData;
import com.stupidbeauty.hxlauncher.datastore.RuntimeInformationStore;

public class LoadApplicationNameInternationalFileTask extends AsyncTask<Object, Void, Object>
{
	private static final String TAG="LoadApplicationNameInternationalFileTask"; //!<输出调试信息时使用的标记。
	private ApplicationNameInternationalizationData applicationNameInternationalizationData ; //!<应用程序可读名字国际化数据。

	private HxLauncherApplication launcherActivity=null; //!< 启动活动。
	
    /**
	 * 载入应用程序名字的国际化信息文件。
	 */
	private void loadApplicationNameInternationalFile()
	{
		Locale locale=Locale.getDefault(); //获取默认语系。

		String androidLocaleName=locale.toString(); //获取语系名字。
		String normalizedLocaleName="zh_CN"; //规一化的语系名字。

		if (androidLocaleName.startsWith("zh_CN")) //简体中文。
		{
			normalizedLocaleName="zh_CN"; //规一化的语系名字。
		} //简体中文。
		else //英语。
		{
			normalizedLocaleName="en_US"; //规一化的语系名字。
		} //else //英语。

		String qrcFileName=normalizedLocaleName+ "/applicationNames.json"; //文件名。
		String fullQrcFileName=":/ApplicationNameInternationalization/"+qrcFileName; //构造完整的qrc文件名。

		VFile qrcHtmlFile=new VFile(launcherActivity, fullQrcFileName); //qrc网页文件。

		String fileContent=qrcHtmlFile.getFileTextContent(); //获取文件的完整内容。

		Gson gson=new Gson();

		applicationNameInternationalizationData = gson.fromJson(fileContent, ApplicationNameInternationalizationData.class); //解析。
	} //private void loadApplicationNameInternationalFile()

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

        launcherActivity=(HxLauncherApplication)(params[0]); //获取映射对象
            
        loadApplicationNameInternationalFile(); // 载入国际化名字映射。
            
        boolean addPhotoFile=false; //Whether to add photo file

        return applicationNameInternationalizationData;
    } //protected Object doInBackground(Object... params)

    /**
     * 报告结果。
     * @param result 结果。是否成功。
     */
		@Override
		protected void onPostExecute(Object result)
        {
            launcherActivity.setApplicationNameInternationalizationData(applicationNameInternationalizationData);
		} //protected void onPostExecute(Boolean result)
	}
