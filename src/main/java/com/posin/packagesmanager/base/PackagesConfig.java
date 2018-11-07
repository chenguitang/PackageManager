package com.posin.packagesmanager.base;

/**
 * FileName: PackagesConfig
 * Author: Greetty
 * Time: 2018/10/12 11:52
 * Desc: TODO
 */
public class PackagesConfig {


    public static final String[] BLACK_LIST = {
            //应用自身
            "com.posin.packagesmanager.ui.activity.MainActivity",
            "com.minipos.packagesmanager.ui.activity.MainActivity",
            //system disabled component
            "com.android.inputmethod.latin.setup.SetupActivity",
            "com.android.stk.StkLauncherActivity",
            "com.android.provision.DefaultActivity",
            "com.android.settings.CryptKeeper",
            "com.android.camera.DisableCameraReceiver",
            "com.android.camera.CameraLauncher",
            //美团收银
            "com.xiaomi.mipush.sdk.MessageHandleService",
            "com.xiaomi.mipush.sdk.PushMessageHandler",
            "com.xiaomi.push.service.receivers.NetworkStatusReceiver",
            "com.huawei.android.pushagent.PushEventReceiver",
            "com.xiaomi.push.service.receivers.PingReceiver",
            "com.xiaomi.push.service.XMPushService",
            "com.dianping.base.push.pushservice.MZPushService",
            "com.huawei.android.pushagent.PushService",
            "com.dianping.base.push.pushservice.MZPushReceiver",
            "com.dianping.base.push.pushservice.HWPushMessageReceiver",
            "com.huawei.android.pushagent.PushBootReceiver",

    };

    /**
     * 隐藏应用数据保存地址
     */
    public static final String Disable_APP_CONFIG_FILE = "/data/posin/disableApp.json";


    public static boolean current_isUseModel = true;

    public static void setUserModel(boolean isUserModel) {
        current_isUseModel = isUserModel;
    }

    public static boolean getUserModel() {
        return current_isUseModel;
    }
}
