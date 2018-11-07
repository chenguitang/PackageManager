package com.posin.packagesmanager.utils.shell;

import android.content.ComponentName;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.ServiceManager;
import android.util.Log;

import java.lang.reflect.Method;

public class pm {

    private static final String TAG = "minipos.pm";

    private static Class<?> ClassIPackageManager = null;


    public static void main(String[] args) {

        Log.d(TAG, "执行了Main方法: ");

        if (args == null || args.length != 3) {
            printUsage();
            System.exit(1);
            return;
        }

        final String pkgName = args[0];
        final String pkgCls = args[1];
        int newState = 0;

        Log.d(TAG, "package name: " + pkgName);
        Log.d(TAG, "package class: " + pkgCls);


        if ("enable".equals(args[2])) {
            //newState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
            newState = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        } else if ("disable".equals(args[2])) {
            newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            //newState = 3; //PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER;
        } else {
            printUsage();
            System.exit(1);
            return;
        }

        Log.d(TAG, args[2] + " pkg:" + pkgName + " class:" + pkgCls);

        try {

            ClassIPackageManager = Class.forName("android.content.pm.IPackageManager");

//			Object pm = getService("package");
            IPackageManager pm = IPackageManager.Stub.asInterface(
                    ServiceManager.getService("package"));

            ComponentName cn = new ComponentName(pkgName, pkgCls);

            System.out.println(args[2] + " package=" + pkgName + ", class=" + pkgCls);

            invokeAndroidKK(pm, cn, newState, PackageManager.DONT_KILL_APP);

            System.exit(0);

            return;

        } catch (Throwable e) {

            e.printStackTrace();

        }

        System.exit(1);
    }

    private static void printUsage() {
        System.out.println("usage : com.minipos.utils.pm pakcageName className enable|disable");
    }

    private static void invokeAndroidKK(Object pm, ComponentName cn, int newState, int flag) throws Throwable {
        Method m = null;

        m = ClassIPackageManager.getMethod("setComponentEnabledSetting",
                new Class[]{ComponentName.class, int.class, int.class, int.class});

        try {
            m.invoke(pm,
                    new Object[]{cn, newState, flag, (int) 0});
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

//	private static void invokeAndroidICS(Object pm, ComponentName cn, int newState, int flag) throws Throwable {
//		Method m = null;
//		
//		m = ClassIPackageManager.getMethod("setComponentEnabledSetting", 
//				new Class[] { ComponentName.class, int.class, int.class } );
//
//		try {
//			m.invoke(pm, 
//					new Object[] { cn, newState, flag } );
//		} catch(Throwable e) {
//			e.printStackTrace();
//			System.exit(1);;
//		}
//	}
}
