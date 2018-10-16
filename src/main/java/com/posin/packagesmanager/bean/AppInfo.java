package com.posin.packagesmanager.bean;


import android.graphics.drawable.Drawable;

/**
 * FileName: AppInfo
 * Author: Greetty
 * Time: 2018/10/12 9:56
 * Desc: TODO
 */
public class AppInfo {

    private Drawable appIcon;
    //APP名称
    private String appName;
    //应用包名
    private String packageName;
    //应用类名
    private String className;
    //应用对应的UId
    private int appUId;
    //在用户模式下是否隐藏应用
    private boolean ShowOnUserMode;
    //应用状态
    private int mState;

//    public AppInfo() {
//    }


    public AppInfo(Drawable appIcon, String appName, String packageName, String className,
                   int appUId, boolean showOnUserMode, int mState) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.packageName = packageName;
        this.className = className;
        this.appUId = appUId;
        ShowOnUserMode = showOnUserMode;
        this.mState = mState;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAppUId() {
        return appUId;
    }

    public void setAppUId(int appUId) {
        this.appUId = appUId;
    }

    public boolean isShowOnUserMode() {
        return ShowOnUserMode;
    }

    public void setShowOnUserMode(boolean showOnUserMode) {
        ShowOnUserMode = showOnUserMode;
    }

    public int getmState() {
        return mState;
    }

    public void setmState(int mState) {
        this.mState = mState;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appIcon=" + appIcon +
                ", appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", appUId=" + appUId +
                ", ShowOnUserMode=" + ShowOnUserMode +
                ", mState=" + mState +
                '}';
    }
}
