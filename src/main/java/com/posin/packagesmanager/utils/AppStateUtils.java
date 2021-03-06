package com.posin.packagesmanager.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.posin.packagesmanager.utils.shell.SuShell;

/**
 * FileName: AppStateUtils
 * Author: Greetty
 * Time: 2018/10/12 13:57
 * Desc: TODO
 */
public class AppStateUtils {

    private static final String TAG = "AppStateUtils";

    public static void setPackageEnable(Context context, String packageName, String className,
                                        boolean enabled) throws Exception {

        final String packageSourceDir = context.getApplicationInfo().sourceDir;
        int packageState = getPackageState(context, packageName, className);

        Log.e(TAG, "isVisible(packageState): " + isVisible(packageState));
        Log.e(TAG, "enabled: " + enabled);

        if (isVisible(packageState) != enabled) {
            SuShell.exec("export CLASSPATH=" + packageSourceDir, null, 0);

            final String cmd = "app_process /system/bin com.posin.packagesmanager.utils.shell.pm " +
                    packageName + " " + className + " " + (enabled ? "enable" : "disable");
            SuShell.exec(cmd, null, 2000);
        }
    }

    /**
     * 获取应用状态
     *
     * @param context context
     * @param packageName 应用包名
     * @param className 应用类名
     * @return state
     */
    public static int getPackageState(Context context, String packageName, String className) {
        return context.getPackageManager().getComponentEnabledSetting(
                new ComponentName(packageName, className));
    }

    /**
     * 获取应用状态
     *
     * @param state int应用状态
     * @return stateStr
     */
    public static String getStateString(int state) {
        switch (state) {
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
                return "default";
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
                return "disabled";
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED:
                return "disabled_until_used";
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER:
                return "disabled_user";
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
                return "enabled";
            default:
                return "unknow";
        }
    }

    /**
     * 应用图标是否可见
     *
     * @param state state
     * @return boolean
     */
    public static boolean isVisible(int state) {
        return state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                || state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }


}
