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
            // system disabled component
            "com.android.inputmethod.latin.setup.SetupActivity",
            "com.android.stk.StkLauncherActivity",
            "com.android.provision.DefaultActivity",
            "com.android.settings.CryptKeeper",
    };

}
