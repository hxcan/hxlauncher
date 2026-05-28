package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.stupidbeauty.hxlauncher.utils.FileLogger;
import com.stupidbeauty.codeposition.CodePosition;
import java.io.File;

/**
 * 专门用于处理 PackageInstaller 安装流程回调的 Activity
 * 解决 MIUI 阻止从 LauncherActivity.onNewIntent() 中间接启动安装确认对话框的问题
 */
public class InstallConfirmActivity extends Activity {
    
    private static final String TAG = "InstallConfirmActivity";
    private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";
    
    private String pendingApkPath = null; // 待安装的 APK 路径（用于降级安装）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: InstallConfirmActivity created");
        
        // 处理安装确认回调（某些情况下系统可能直接调用 onCreate 而不是 onNewIntent）
        if (PACKAGE_INSTALLED_ACTION.equals(getIntent().getAction())) {
            Log.d(TAG, "onCreate: Detected PACKAGE_INSTALLED_ACTION, calling handleInstallCallback");
            handleInstallCallback(getIntent());
            // 不在这里 finish()，等待用户操作后再 finish
        } else {
            Log.w(TAG, "onCreate: Unexpected action: " + getIntent().getAction());
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // 更新 intent
        Log.d(TAG, CodePosition.newInstance().toString() + ", onNewIntent: " + intent);
        
        if (PACKAGE_INSTALLED_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "onNewIntent: Detected PACKAGE_INSTALLED_ACTION, calling handleInstallCallback");
            handleInstallCallback(intent);
            // 不在这里 finish()，等待用户操作后再 finish
        } else {
            Log.w(TAG, "onNewIntent: Unexpected action: " + intent.getAction());
            finish();
        }
    }
    
    private void handleInstallCallback(Intent intent) {
        Log.d(TAG, "handleInstallCallback: Starting...");
        
        Bundle extras = intent.getExtras();
        Log.d(TAG, "handleInstallCallback: extras=" + extras);
        
        if (extras == null) {
            Log.e(TAG, "handleInstallCallback: extras is null!");
            Toast.makeText(this, "安装回调错误：缺少额外信息", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        int status = extras.getInt(PackageInstaller.EXTRA_STATUS);
        String message = extras.getString(PackageInstaller.EXTRA_STATUS_MESSAGE);
        String packageName = extras.getString(PackageInstaller.EXTRA_PACKAGE_NAME);
        
        Log.d(TAG, "handleInstallCallback: status=" + status + ", message=" + message + ", packageName=" + packageName);
        
        switch (status) {
            case PackageInstaller.STATUS_PENDING_USER_ACTION:
                Log.d(TAG, "handleInstallCallback: STATUS_PENDING_USER_ACTION - starting confirm dialog");
                Intent confirmIntent = extras.getParcelable(Intent.EXTRA_INTENT);
                Log.d(TAG, "handleInstallCallback: confirmIntent=" + confirmIntent);
                
                if (confirmIntent != null) {
                    try {
                        Log.d(TAG, "handleInstallCallback: Attempting to start confirmIntent...");
                        startActivity(confirmIntent);
                        Log.d(TAG, "handleInstallCallback: confirmIntent started successfully");
                        // 不在这里 finish()，让用户完成安装操作
                    } catch (Exception e) {
                        Log.e(TAG, "handleInstallCallback: Failed to start confirmIntent: " + e.getMessage(), e);
                        Log.e(TAG, "handleInstallCallback: Exception stack trace: " + Log.getStackTraceString(e));
                        Toast.makeText(this, "无法显示安装确认对话框：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        // 尝试降级方案
                        showFallbackInstallDialog(packageName);
                    }
                } else {
                    Log.e(TAG, "handleInstallCallback: confirmIntent is null!");
                    Toast.makeText(this, "安装确认意图为空", Toast.LENGTH_LONG).show();
                    // 尝试降级方案
                    showFallbackInstallDialog(packageName);
                }
                break;
                
            case PackageInstaller.STATUS_SUCCESS:
                Log.d(TAG, "handleInstallCallback: STATUS_SUCCESS - " + packageName);
                Toast.makeText(this, "安装成功！", Toast.LENGTH_SHORT).show();
                
                if (packageName != null) {
                    try {
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                        if (launchIntent != null) {
                            Log.d(TAG, "handleInstallCallback: Launching installed app: " + packageName);
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchIntent);
                        } else {
                            Log.w(TAG, "handleInstallCallback: No launch intent found for: " + packageName);
                            Toast.makeText(this, "安装成功，但找不到启动图标", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "handleInstallCallback: Failed to launch app: " + e.getMessage(), e);
                        Toast.makeText(this, "安装成功，但启动失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "handleInstallCallback: STATUS_FAILURE_* - " + status + ", " + message);
                
                // 检测是否是 MIUI 系统的自动拒绝
                if (status == PackageInstaller.STATUS_FAILURE_ABORTED && 
                    message != null && 
                    message.contains("User rejected permissions") &&
                    isMIUI()) {
                    
                    Log.w(TAG, "handleInstallCallback: MIUI system auto-rejected, offering fallback install method");
                    showFallbackInstallDialog(packageName);
                } else {
                    String errorMsg = "安装失败：" + message;
                    Log.e(TAG, "handleInstallCallback: " + errorMsg);
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
                
            default:
                Log.w(TAG, "handleInstallCallback: Unrecognized status - " + status);
                Toast.makeText(this, "未知的安装状态：" + status, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
    
    /**
     * 检测是否为 MIUI/HyperOS 系统
     */
    private boolean isMIUI() {
        try {
            // 使用反射访问 SystemProperties 类（隐藏 API）
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method method = clazz.getMethod("get", String.class, String.class);
            String prop = (String) method.invoke(null, "ro.miui.ui.version.name", "");
            boolean isMiui = prop != null && !prop.isEmpty();
            Log.d(TAG, "isMIUI: ro.miui.ui.version.name=" + prop + ", isMiui=" + isMiui);
            return isMiui;
        } catch (Exception e) {
            Log.e(TAG, "isMIUI: failed to check MIUI version", e);
            return false;
        }
    }
    
    /**
     * 显示降级安装对话框
     */
    private void showFallbackInstallDialog(String packageName) {
        Log.d(TAG, "showFallbackInstallDialog: Starting... packageName=" + packageName);
        
        try {
            Log.d(TAG, "showFallbackInstallDialog: Creating AlertDialog...");
            
            new AlertDialog.Builder(this)
                .setTitle("MIUI 系统限制提示")
                .setMessage("检测到 MIUI/HyperOS 系统自动拒绝了安装请求。\n\n" +
                           "这可能是因为 MIUI 的安全策略限制了自动安装。\n\n" +
                           "您可以尝试使用传统方式安装此应用：" +
                           "\n• 点击\"使用传统方式安装\"按钮" +
                           "\n• 系统将打开文件管理器，请选择 APK 文件" +
                           "\n• 点击\"安装\"按钮完成安装" +
                           "\n\n是否现在使用传统方式安装？")
                .setPositiveButton("使用传统方式安装", (dialog, which) -> {
                    Log.d(TAG, "showFallbackInstallDialog: User clicked 'Use traditional method'");
                    installViaTraditionalIntent();
                })
                .setNegativeButton("稍后重试", (dialog, which) -> {
                    Log.d(TAG, "showFallbackInstallDialog: User clicked 'Try later'");
                    Toast.makeText(this, "请手动前往开发者选项关闭\"MIUI 优化\"后重试", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setCancelable(false)
                .setOnDismissListener(dialog -> {
                    Log.d(TAG, "showFallbackInstallDialog: Dialog dismissed");
                })
                .show();
            
            Log.d(TAG, "showFallbackInstallDialog: AlertDialog shown successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "showFallbackInstallDialog: Failed to show dialog: " + e.getMessage(), e);
            Log.e(TAG, "showFallbackInstallDialog: Exception stack trace: " + Log.getStackTraceString(e));
            
            // 后备方案：直接显示 Toast 并尝试传统安装
            Log.w(TAG, "showFallbackInstallDialog: Falling back to direct traditional install");
            Toast.makeText(this, "对话框创建失败，尝试直接打开文件管理器...", Toast.LENGTH_LONG).show();
            
            // 延迟 500ms 后尝试传统安装，确保 Toast 能显示出来
            new android.os.Handler(getMainLooper()).postDelayed(() -> {
                Log.d(TAG, "showFallbackInstallDialog: Delayed task - calling installViaTraditionalIntent");
                installViaTraditionalIntent();
            }, 500);
        }
    }
    
    /**
     * 使用传统的 Intent.ACTION_VIEW 方式安装 APK
     */
    private void installViaTraditionalIntent() {
        Log.d(TAG, "installViaTraditionalIntent: Starting traditional install method...");
        
        // 从缓存中获取 APK 路径
        String apkPath = getApkPathFromCache();
        Log.d(TAG, "installViaTraditionalIntent: APK path from cache: " + apkPath);
        
        if (apkPath == null || apkPath.isEmpty()) {
            Log.e(TAG, "installViaTraditionalIntent: APK path not found in cache");
            Toast.makeText(this, "未找到 APK 文件，请重新下载后安装", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        File apkFile = new File(apkPath);
        Log.d(TAG, "installViaTraditionalIntent: APK file exists: " + apkFile.exists() + ", path: " + apkPath);
        
        if (!apkFile.exists()) {
            Log.e(TAG, "installViaTraditionalIntent: APK file does not exist: " + apkPath);
            Toast.makeText(this, "APK 文件不存在，请重新下载", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        try {
            Log.d(TAG, "installViaTraditionalIntent: Creating Intent.ACTION_VIEW intent...");
            
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri;
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                // Android 7.0+ 使用 FileProvider
                String authority = getPackageName() + ".provider";
                Log.d(TAG, "installViaTraditionalIntent: Using FileProvider with authority: " + authority);
                apkUri = androidx.core.content.FileProvider.getUriForFile(this, authority, apkFile);
                
                // 添加明确的权限授予标志，确保安装程序可以访问 FileProvider
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                
                Log.d(TAG, "installViaTraditionalIntent: Intent flags: " + intent.getFlags());
                Log.d(TAG, "installViaTraditionalIntent: Intent data: " + intent.getData());
                Log.d(TAG, "installViaTraditionalIntent: Intent type: " + intent.getType());
                
            } else {
                // Android 7.0 以下直接使用 file:// URI
                Log.d(TAG, "installViaTraditionalIntent: Using file:// URI (Android < 7.0)");
                apkUri = Uri.fromFile(apkFile);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            
            Log.d(TAG, "installViaTraditionalIntent: Starting activity with intent: " + intent);
            
            startActivity(intent);
            
            Log.d(TAG, "installViaTraditionalIntent: Activity started successfully");
            Toast.makeText(this, "已打开文件管理器，请选择 APK 文件并点击\"安装\"", Toast.LENGTH_LONG).show();
            
            // 不在这里 finish()，让用户完成安装操作
            // finish();
            
        } catch (Exception e) {
            Log.e(TAG, "installViaTraditionalIntent: Failed to start traditional install: " + e.getMessage(), e);
            Log.e(TAG, "installViaTraditionalIntent: Exception stack trace: " + Log.getStackTraceString(e));
            Toast.makeText(this, "启动传统安装失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    /**
     * 从缓存中获取 APK 路径
     */
    private String getApkPathFromCache() {
        Log.d(TAG, "getApkPathFromCache: Starting...");
        
        // 尝试从 HxLauncherApplication 获取最近下载的 APK 路径
        try {
            com.stupidbeauty.hxlauncher.application.HxLauncherApplication app = 
                com.stupidbeauty.hxlauncher.application.HxLauncherApplication.getInstance();
            
            java.util.HashMap<String, String> apkFilePathMap = app.getApkFilePathMap();
            Log.d(TAG, "getApkPathFromCache: apkFilePathMap size: " + (apkFilePathMap != null ? apkFilePathMap.size() : "null"));
            
            if (apkFilePathMap != null && !apkFilePathMap.isEmpty()) {
                // 返回最后一个添加的 APK 路径
                for (String path : apkFilePathMap.values()) {
                    if (path != null && !path.isEmpty()) {
                        Log.d(TAG, "getApkPathFromCache: Found APK path: " + path);
                        return path;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getApkPathFromCache: Failed to get APK path from HxLauncherApplication", e);
        }
        
        // 如果从映射中没找到，尝试常见的缓存目录
        Log.d(TAG, "getApkPathFromCache: Trying common cache directories...");
        
        String[] possiblePaths = {
            getExternalCacheDir() + "/com.stupidbeauty.blindbox.apk",
            getCacheDir() + "/com.stupidbeauty.blindbox.apk",
            getExternalFilesDir(null) + "/com.stupidbeauty.blindbox.apk"
        };
        
        for (String path : possiblePaths) {
            File file = new File(path);
            Log.d(TAG, "getApkPathFromCache: Checking path: " + path + ", exists: " + file.exists());
            
            if (file.exists()) {
                Log.d(TAG, "getApkPathFromCache: Found APK in common path: " + path);
                return path;
            }
        }
        
        Log.w(TAG, "getApkPathFromCache: APK path not found in any location");
        return null;
    }
}