package com.stupidbeauty.comgooglewidevinesoftwaredrmremover.app;

import android.content.Context;
import android.os.AsyncTask;
// import com.stupidbeauty.ftpserver.lib.FtpServer;
import java.net.BindException;
import android.os.Environment;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import java.io.File;

/**
 * 一些基本信息的定义。
 * @author root
 *
 */
class LanImeBaseDef
{
	//Exception log file name
	public static final String EXCEPTION_FILE = "hxlauncher_exception_log.txt"; //!<例外的日志文件名，这个日志文件不删除。

// 	public static final String LOG_BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"com.stupidbeauty.hxlauncher"+File.separator+"Log"; //!<日志目录的路径。
	
	/**
	* get 日志目录的路径。
	*/
	public static String getLogBaseDir()
	{
//     String treePath=Environment.getExternalStorageDirectory().getAbsolutePath();
    
    Context context = HxLauncherApplication.getAppContext();

    String treePath=context.getFilesDir().getAbsolutePath();
    String result= treePath + File.separator+"com.stupidbeauty.hxlauncher"+File.separator+"Log"; 
    
    return result;
	} // public static String getLogBaseDir()
}
