package com.posin.packagesmanager.bean;

import java.util.List;

/**
 * FileName: PackagesMessage
 * Author: Greetty
 * Time: 2018/10/13 16:54
 * Desc: TODO
 */
public class PackagesMessage {


    /**
     * model : user
     * disabled : [{"packageName":"com.xunjoy.lewaimai.pos","className":"com.xunjoy.lewaimai.pos.MainActivity"},{"packageName":"com.posin.updtae","className":"com.posin.update.ui.activity.MainActivity"},{"packageName":"com.deeyi.yddx5","className":"com.deeyi.yddx5.LaunchActivity"}]
     */

    private String model;
    private List<DisabledBean> disabled;

    public PackagesMessage(String model, List<DisabledBean> disabled) {
        this.model = model;
        this.disabled = disabled;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<DisabledBean> getDisabled() {
        return disabled;
    }

    public void setDisabled(List<DisabledBean> disabled) {
        this.disabled = disabled;
    }

    public static class DisabledBean {
        /**
         * packageName : com.xunjoy.lewaimai.pos
         * className : com.xunjoy.lewaimai.pos.MainActivity
         */

        private String packageName;
        private String className;

        public DisabledBean(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
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

        @Override
        public String toString() {
            return "DisabledBean{" +
                    "packageName='" + packageName + '\'' +
                    ", className='" + className + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PackagesMessage{" +
                "model='" + model + '\'' +
                ", disabled=" + disabled +
                '}';
    }
}
