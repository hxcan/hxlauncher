package com.stupidbeauty.hxlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Bundle;
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
        
        // 处理安装确认回调（某些情况下系统可能直接调用 onCreate 而不是 onNewIntent）
        if (PACKAGE_INSTALLED_ACTION.equals(getIntent().getAction())) {
            handleInstallCallback(getIntent());
            finish();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FileLogger.d(TAG, CodePosition.newInstance().toString() + ", onNewIntent: " + intent);
        
        if (PACKAGE_INSTALLED_ACTION.equals(intent.getAction())) {
            handleInstallCallback(intent);
        }
    }
    
    private void handleInstallCallback(Intent intent) {
        Bundle extras = intent.getExtras();
        FileLogger.d(TAG, "handleInstallCallback: extras=" + extras);
        
        if (extras != null) {
            int status = extras.getInt(PackageInstaller.EXTRA_STATUS);
            String message = extras.getString(PackageInstaller.EXTRA_STATUS_MESSAGE);
            String packageName = extras.getString(PackageInstaller.EXTRA_PACKAGE_NAME);
            
            FileLogger.d(TAG, "handleInstallCallback: status=" + status + ", message=" + message + ", packageName=" + packageName);
            
            switch (status) {
                case PackageInstaller.STATUS_PENDING_USER_ACTION:
                    FileLogger.d(TAG, "handleInstallCallback: STATUS_PENDING_USER_ACTION - starting confirm dialog");
                    Intent confirmIntent = extras.getParcelable(Intent.EXTRA_INTENT);
                    FileLogger.d(TAG, "handleInstallCallback: confirmIntent=" + confirmIntent);
                    if (confirmIntent != null) {
                        try {
                            startActivity(confirmIntent);
                            FileLogger.d(TAG, "handleInstallCallback: confirmIntent started successfully");
                        } catch (Exception e) {
                            FileLogger.e(TAG, "handleInstallCallback: failed to start confirmIntent: " + e.getMessage(), e);
                            Toast.makeText(this, "Failed to show install dialog: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        FileLogger.e(TAG, "handleInstallCallback: confirmIntent is null!");
                        Toast.makeText(this, "Install confirmation intent is null", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                    
                case PackageInstaller.STATUS_SUCCESS:
                    FileLogger.d(TAG, "handleInstallCallback: STATUS_SUCCESS - " + packageName);
                    Toast.makeText(this, "Install succeeded!", Toast.LENGTH_SHORT).show();
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
                    FileLogger.e(TAG, "handleInstallCallback: STATUS_FAILURE_* - " + status + ", " + message);
                    
                    // 检测是否是 MIUI 系统的自动拒绝
                    if (status == PackageInstaller.STATUS_FAILURE_ABORTED && 
                        message != null && 
                        message.contains("User rejected permissions") &&
                        isMIUI()) {
                        
                        FileLogger.w(TAG, "handleInstallCallback: MIUI system auto-rejected, offering fallback install method");
                        showFallbackInstallDialog(packageName);
                    } else {
                        Toast.makeText(this, "Install failed: " + message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                    
                default:
                    FileLogger.w(TAG, "handleInstallCallback: Unrecognized status - " + status);
                    Toast.makeText(this, "Unrecognized status: " + status, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
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
            return prop != null && !prop.isEmpty();
        } catch (Exception e) {
            FileLogger.e(TAG, "isMIUI: failed to check MIUI version", e);
            return false;
        }
    }
    
    /**
     * 显示降级安装对话框
     */
    private void showFallbackInstallDialog(String packageName) {
        new AlertDialog.Builder(this)
            .setTitle("MIUI 系统限制提示")
            .setMessage("检测到 MIUI/HyperOS 系统自动拒绝了安装请求。\n\n" +
                       "这可能是因为 MIUI 的安全策略限制了自动安装。\n\n" +
                       "您可以尝试使用传统方式安装此应用：" +
                       "\n• 点击\"使用传统方式安装\"按钮" +
                       "\n• 系统将打开文件管理器，请选择\"安装\"" +
                       "\n\n是否现在使用传统方式安装？")
            .setPositiveButton("使用传统方式安装", (dialog, which) -> {
                installViaTraditionalIntent();
            })
            .setNegativeButton("稍后重试", (dialog, which) -> {
                // 尝试再次使用 PackageInstaller（可能用户已手动关闭限制）
                Toast.makeText(this, "请手动前往开发者选项关闭\"MIUI 优化\"后重试", Toast.LENGTH_LONG).show();
                finish();
            })
            .setCancelable(false)
            .show();
    }
    
    /**
     * 使用传统的 Intent.ACTION_VIEW 方式安装 APK
     */
    private void installViaTraditionalIntent() {
        FileLogger.d(TAG, "installViaTraditionalIntent: starting traditional install method");
        
        // 从缓存中获取 APK 路径
        String apkPath = getApkPathFromCache();
        
        if (apkPath == null || apkPath.isEmpty()) {
            FileLogger.e(TAG, "installViaTraditionalIntent: APK path not found in cache");
            Toast.makeText(this, "未找到 APK 文件，请重新下载后安装", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        File apkFile = new File(apkPath);
        
        if (!apkFile.exists()) {
            FileLogger.e(TAG, "installViaTraditionalIntent: APK file does not exist: " + apkPath);
            Toast.makeText(this, "APK 文件不存在，请重新下载", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri;
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                // Android 7.0+ 使用 FileProvider
                String authority = getPackageName() + ".provider";
                apkUri = androidx.core.content.FileProvider.getUriForFile(this, authority, apkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                // Android 7.0 以下直接使用 file:// URI
                apkUri = Uri.fromFile(apkFile);
            }
            
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            FileLogger.d(TAG, "installViaTraditionalIntent: starting traditional install intent, path=" + apkPath);
            startActivity(intent);
            
            Toast.makeText(this, "已启动传统安装方式，请在文件管理器中点击\"安装\"", Toast.LENGTH_LONG).show();
            finish();
            
        } catch (Exception e) {
            FileLogger.e(TAG, "installViaTraditionalIntent: failed to start traditional install: " + e.getMessage(), e);
            Toast.makeText(this, "启动传统安装失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    /**
     * 从缓存中获取 APK 路径
     */
    private String getApkPathFromCache() {
        // 尝试从 HxLauncherApplication 获取最近下载的 APK 路径
        try {
            com.stupidbeauty.hxlauncher.application.HxLauncherApplication app = 
                com.stupidbeauty.hxlauncher.application.HxLauncherApplication.getInstance();
            
            java.util.HashMap<String, String> apkFilePathMap = app.getApkFilePathMap();
            
            if (apkFilePathMap != null && !apkFilePathMap.isEmpty()) {
                // 返回最后一个添加的 APK 路径
                for (String path : apkFilePathMap.values()) {
                    if (path != null && !path.isEmpty()) {
                        FileLogger.d(TAG, "getApkPathFromCache: found APK path: " + path);
                        return path;
                    }
                }
            }
        } catch (Exception e) {
            FileLogger.e(TAG, "getApkPathFromCache: failed to get APK path from cache", e);
        }
        
        // 如果从映射中没找到，尝试常见的缓存目录
        String[] possiblePaths = {
            getExternalCacheDir() + "/com.stupidbeauty.blindbox.apk",
            getCacheDir() + "/com.stupidbeauty.blindbox.apk",
            getExternalFilesDir(null) + "/com.stupidbeauty.blindbox.apk"
        };
        
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                FileLogger.d(TAG, "getApkPathFromCache: found APK in common path: " + path);
                return path;
            }
        }
        
        FileLogger.w(TAG, "getApkPathFromCache: APK path not found");
        return null;
    }
}