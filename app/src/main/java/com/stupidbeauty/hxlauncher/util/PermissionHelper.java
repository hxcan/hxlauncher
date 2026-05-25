// com.stupidbeauty.hxlauncher.util.PermissionHelper.java
package com.stupidbeauty.hxlauncher.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * 忠实封装 LauncherActivity 原有权限逻辑，不做行为变更。
 * 包括：同时检查/请求 4 个权限（含 INSTALL_PACKAGE，尽管它不能被 requestPermissions 申请）
 */
public class PermissionHelper {

    public static final String PERMISSION_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String PERMISSION_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String PERMISSION_INSTALL_PACKAGE = "android.permission.REQUEST_INSTALL_PACKAGES";

    private static final int PERMISSIONS_REQUEST = 1001;

    private final Activity activity;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 检查是否拥有全部所需权限（行为与原 hasPermission() 完全一致）
     */
    public boolean hasPermission() {
        boolean result = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            permissions.add(PERMISSION_STORAGE);
            permissions.add(PERMISSION_RECORD_AUDIO);
            permissions.add(PERMISSION_FINE_LOCATION);
            permissions.add(PERMISSION_INSTALL_PACKAGE);

            for (String permission : permissions) {
                result = (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
                if (!result) {
                    break;
                }
            }
        } else {
            result = true;
        }

        return result;
    }

    /**
     * 请求权限（行为与原 requestPermission() 完全一致）
     */
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean shouldShowRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_RECORD_AUDIO) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_INSTALL_PACKAGE);

            if (shouldShowRationale) {
                Toast.makeText(activity, "Camera AND storage permission are required for this demo", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(
                activity,
                new String[]{PERMISSION_STORAGE, PERMISSION_RECORD_AUDIO, PERMISSION_FINE_LOCATION, PERMISSION_INSTALL_PACKAGE},
                PERMISSIONS_REQUEST
            );
        }
    }

    /**
     * 入口方法（行为与原 checkPermission() 完全一致）
     */
    public void checkPermission() {
        if (hasPermission()) {
            // do nothing, just like original
        } else {
            requestPermission();
        }
    }

    /**
     * 用于转发 onRequestPermissionsResult（保持原逻辑不变）
     */
    public boolean handlePermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 原 LauncherActivity 中没有在 onRequestPermissionsResult 里做额外处理，
        // 所以这里只需识别是否是我们的请求即可，无需回调。
        return requestCode == PERMISSIONS_REQUEST;
    }
}
