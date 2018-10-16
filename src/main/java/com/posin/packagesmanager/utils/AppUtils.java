package com.posin.packagesmanager.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.posin.packagesmanager.base.PackagesConfig;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.utils.shell.SuShell;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: AppUtils
 * Author: Greetty
 * Time: 2018/10/12 11:19
 * Desc: TODO
 */
public class AppUtils {

    private static final String TAG = "AppUtils";


    /**
     * 获取系统所以已安装的应用包
     *
     * @param context
     * @return
     */
    public List<PackageInfo> getAllPackages(Context context) throws Exception {
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledPackages(0);
    }


    /**
     * 获取所有显示在桌面上的APP
     *
     * @param context
     * @return
     */

    public List<AppInfo> getAllApp(Context context) throws Exception {
        List<AppInfo> listAppInfo = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> listPackages = pm.getInstalledPackages(0);

        //加载所有显示在桌面的应用
        for (int i = 0; i < listPackages.size(); i++) {
            PackageInfo pi = listPackages.get(i);
            List<ResolveInfo> listResolveInfo = AppUtils.findActivitiesForPackage(pm, pi.packageName);
            for (ResolveInfo resolveInfo : listResolveInfo) {

                Drawable appIcon = pi.applicationInfo.loadIcon(pm);
                String className = resolveInfo.activityInfo.name;
                String appName = pi.applicationInfo.loadLabel(pm).toString();
                String packageName = pi.packageName;
                int uid = pi.applicationInfo.uid;
                int packageState = AppStateUtils.getPackageState(context, packageName, className);

                AppInfo appInfo = new AppInfo(appIcon, appName, packageName, className,
                        uid, true, packageState);
                // 是否在黑名单
                if (isInBlackList(className))
                    continue;
                // 过滤重复的包
                if (listAppInfo.contains(appInfo))
                    continue;

                listAppInfo.add(appInfo);
            }
        }

        //获取所有被隐藏图标的APP
        List<ComponentName> listDisablePackages = getPackageDisabled();
        if (listDisablePackages != null) {
            for (ComponentName cn : listDisablePackages) {

                PackageInfo pi = getPackageInfo(listPackages, cn.getPackageName());
                if (pi != null) {

                    Drawable appIcon = pi.applicationInfo.loadIcon(pm);
                    String className = cn.getClassName();
                    String appName = pi.applicationInfo.loadLabel(pm).toString();
                    String packageName = pi.packageName;
                    int uid = pi.applicationInfo.uid;
                    int packageState = AppStateUtils.getPackageState(context, packageName, className);

                    AppInfo appInfo = new AppInfo(appIcon, appName, packageName, className, uid,
                            false, packageState);
                    // 是否在黑名单
                    if (isInBlackList(className))
                        continue;
                    // 过滤重复的包
                    if (listAppInfo.contains(appInfo))
                        continue;
                    listAppInfo.add(appInfo);
                }

            }
        }
        return listAppInfo;
    }


    /**
     * 获取所有显示在桌面上的APP
     *
     * @param context
     * @return
     */
    public List<AppInfo> getAllShowApp(Context context, List<PackageInfo> listPackages) throws Exception {
        List<AppInfo> listAppInfo = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> listPackages = pm.getInstalledPackages(0);

        //加载所有显示在桌面的应用
        for (int i = 0; i < listPackages.size(); i++) {
            PackageInfo pi = listPackages.get(i);
            List<ResolveInfo> listResolveInfo = AppUtils.findActivitiesForPackage(pm, pi.packageName);
            for (ResolveInfo resolveInfo : listResolveInfo) {

                Drawable appIcon = pi.applicationInfo.loadIcon(pm);
                String className = resolveInfo.activityInfo.name;
                String appName = pi.applicationInfo.loadLabel(pm).toString();
                String packageName = pi.packageName;
                int uid = pi.applicationInfo.uid;
                int packageState = AppStateUtils.getPackageState(context, packageName, className);

                AppInfo appInfo = new AppInfo(appIcon, appName, packageName,
                        className, uid, true, packageState);
                // 是否在黑名单
                if (isInBlackList(className))
                    continue;
                // 过滤重复的包
                boolean isRepeat = false;
                for (int j = 0; j < listAppInfo.size(); j++) {
                    if (listAppInfo.get(j).getClassName().equals(className)) {
                        if (!listAppInfo.get(j).isShowOnUserMode()) {
                            listAppInfo.get(j).setShowOnUserMode(true);
                        }
                        isRepeat = true;
                    }
                }

                if (isRepeat)
                    continue;
                listAppInfo.add(appInfo);
            }
        }
        return listAppInfo;
    }

    /**
     * 获取所有被隐藏图标的APP
     *
     * @param context
     * @return
     */
    public List<AppInfo> getAllDisableApp(Context context, List<PackageInfo> allPackageInfo) throws Exception {

        List<AppInfo> listAllDisableApps = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ComponentName> listDisablePackages = getPackageDisabled();
        if (listDisablePackages != null) {
            for (ComponentName cn : listDisablePackages) {

                PackageInfo pi = getPackageInfo(allPackageInfo, cn.getPackageName());
                if (pi != null) {

                    Drawable appIcon = pi.applicationInfo.loadIcon(pm);
                    String className = cn.getClassName();
                    String appName = pi.applicationInfo.loadLabel(pm).toString();
                    String packageName = pi.packageName;
                    int uid = pi.applicationInfo.uid;
                    int packageState = AppStateUtils.getPackageState(context, packageName, className);

                    AppInfo appInfo = new AppInfo(appIcon, appName, packageName, className,
                            uid, false, packageState);
                    // 是否在黑名单
                    if (isInBlackList(className))
                        continue;
                    // 过滤重复的包
                    boolean isRepeat = false;
                    for (int j = 0; j < listAllDisableApps.size(); j++) {
                        if (listAllDisableApps.get(j).getClassName().equals(className)) {
                            if (!listAllDisableApps.get(j).isShowOnUserMode()) {
                                listAllDisableApps.get(j).setShowOnUserMode(true);
                            }
                            isRepeat = true;
                        }
                    }
                    if (isRepeat)
                        continue;

                    listAllDisableApps.add(appInfo);
                }

            }
        }
        return listAllDisableApps;
    }


    private PackageInfo getPackageInfo(List<PackageInfo> lst, String pkg) throws Exception {
        for (PackageInfo pi : lst) {
            if (pi.packageName.equals(pkg))
                return pi;
        }
        return null;
    }

    /**
     * Query the package manager for MAIN/LAUNCHER activities in the supplied package.
     * 查询包管理器中提供的包中的主/启动程序活动。
     */
    public static List<ResolveInfo> findActivitiesForPackage(PackageManager packageManager, String packageName) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }

    /**
     * 读取系统配置文件，查找隐藏APP
     *
     * @return
     */
    private List<ComponentName> getPackageDisabled() throws Exception {
        try {
            StringBuilder sb = new StringBuilder();
            SuShell.exec("cat /data/system/users/0/package-restrictions.xml", sb, 10000);

            if (sb.length() == 0) {
                Log.d(TAG, "******* erro : null package restrictions");
                return null;
            }

            int index = sb.indexOf("<?xml version='1.0'");
            if (index > 0)
                sb.delete(0, index);
            index = sb.indexOf("</package-restrictions>");
            if (index > 0)
                sb.delete(index + "</package-restrictions>".length() + 1, sb.length());

            final ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());
            final List<ComponentName> result = XMLUtils.readXML(bais);

            bais.close();

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断该应用是否在黑名单中
     *
     * @param className
     * @return
     */
    private static boolean isInBlackList(String className) {
        for (String s : PackagesConfig.BLACK_LIST) {
            if (s.equals(className))
                return true;
        }
        return false;
    }


}
