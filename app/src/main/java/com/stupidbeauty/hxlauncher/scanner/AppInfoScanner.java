// com.stupidbeauty.hxlauncher.scanner.AppInfoScanner.java
package com.stupidbeauty.hxlauncher.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程安全的应用信息扫描器。
 *
 * 仅负责从 PackageManager 获取可启动应用，并转换为 ArticleInfo 列表。
 * 不涉及 UI、Adapter 或 position 映射。
 */
public class AppInfoScanner {

    private final Context context;
    private volatile List<ArticleInfo> cachedAppList;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public AppInfoScanner(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 扫描所有 launcher 应用，返回不可变列表。
     * 首次调用执行实际扫描，后续返回缓存。
     */
    public List<ArticleInfo> scanLauncherApps() {
        if (cachedAppList == null) {
            lock.writeLock().lock();
            try {
                if (cachedAppList == null) {
                    cachedAppList = Collections.unmodifiableList(doScan());
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return cachedAppList;
    }

    /**
     * 强制重新扫描（例如监听到 PACKAGE_ADDED/REMOVED 广播后调用）。
     */
    public void refresh() {
        lock.writeLock().lock();
        try {
            cachedAppList = Collections.unmodifiableList(doScan());
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<ArticleInfo> doScan() {
        List<ArticleInfo> list = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : resolveInfos) {
            ArticleInfo app = new ArticleInfo();
            app.setApplicationLabel(info.loadLabel(pm));
            app.setPackageName(info.activityInfo.packageName);
            app.setActivityName(info.activityInfo.name);

            Intent launchIntent = new Intent(mainIntent);
            launchIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            app.setLaunchIntent(launchIntent);

            list.add(app);
        }

        return list;
    }
}
