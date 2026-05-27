package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.widget.Toast;
import com.stupidbeauty.hxlauncher.utils.FileLogger;
import com.stupidbeauty.codeposition.CodePosition;

/**
 * 专门用于处理 PackageInstaller 安装流程回调的 Activity
 * 解决 MIUI 阻止从 LauncherActivity.onNewIntent() 中间接启动安装确认对话框的问题
 */
public class InstallConfirmActivity extends Activity {
    
    private static final String TAG = "InstallConfirmActivity";
    private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不设置布局，这是一个透明的回调处理 Activity
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FileLogger.d(TAG, CodePosition.newInstance().toString() + ", onNewIntent: " + intent);
        
        if (PACKAGE_INSTALLED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            FileLogger.d(TAG, "onNewIntent: extras=" + extras);
            
            if (extras != null) {
                int status = extras.getInt(PackageInstaller.EXTRA_STATUS);
                String message = extras.getString(PackageInstaller.EXTRA_STATUS_MESSAGE);
                String packageName = extras.getString(PackageInstaller.EXTRA_PACKAGE_NAME);
                
                FileLogger.d(TAG, "onNewIntent: status=" + status + ", message=" + message + ", packageName=" + packageName);
                
                switch (status) {
                    case PackageInstaller.STATUS_PENDING_USER_ACTION:
                        FileLogger.d(TAG, "onNewIntent: STATUS_PENDING_USER_ACTION - starting confirm dialog");
                        Intent confirmIntent = extras.getParcelable(Intent.EXTRA_INTENT);
                        FileLogger.d(TAG, "onNewIntent: confirmIntent=" + confirmIntent);
                        if (confirmIntent != null) {
                            try {
                                startActivity(confirmIntent);
                                FileLogger.d(TAG, "onNewIntent: confirmIntent started successfully");
                            } catch (Exception e) {
                                FileLogger.e(TAG, "onNewIntent: failed to start confirmIntent: " + e.getMessage(), e);
                                Toast.makeText(this, "Failed to show install dialog: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            FileLogger.e(TAG, "onNewIntent: confirmIntent is null!");
                            Toast.makeText(this, "Install confirmation intent is null", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                        
                    case PackageInstaller.STATUS_SUCCESS:
                        FileLogger.d(TAG, "onNewIntent: STATUS_SUCCESS - " + packageName);
                        Toast.makeText(this, "Install succeeded!", Toast.LENGTH_SHORT).show();
                        // 启动已安装的应用
                        if (packageName != null) {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                            if (launchIntent != null) {
                                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(launchIntent);
                            }
                        }
                        finish();
                        break;
                        
                    case PackageInstaller.STATUS_FAILURE_ABORTED:
                    case PackageInstaller.STATUS_FAILURE_BLOCKED:
                    case PackageInstaller.STATUS_FAILURE_CONFLICT:
                    case PackageInstaller.STATUS_FAILURE_INCOMPATIBLE:
                    case PackageInstaller.STATUS_FAILURE_INVALID:
                    case PackageInstaller.STATUS_FAILURE_STORAGE:
                        FileLogger.e(TAG, "onNewIntent: STATUS_FAILURE_* - " + status + ", " + message);
                        Toast.makeText(this, "Install failed: " + message, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                        
                    default:
                        FileLogger.w(TAG, "onNewIntent: Unrecognized status - " + status);
                        Toast.makeText(this, "Unrecognized status: " + status, Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        }
    }
}