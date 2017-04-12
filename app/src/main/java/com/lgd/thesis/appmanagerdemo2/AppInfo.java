package com.lgd.thesis.appmanagerdemo2;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 17/4/11.
 */

public class AppInfo {
    private String appLabel;    //应用程序标签
    private Drawable appIcon ;  //应用程序图像
//    private Intent intent ;     //启动应用程序的Intent ，一般是Action为Main和Category为Lancher的Activity
    private String app_size ;    //应用程序所对应的大小
    private String app_version_name;  // version name
    private String app_Package_Name;

    public AppInfo(){}

    public AppInfo(String appLabel, Drawable appIcon, String app_size, String app_version_name, String app_Package_Name) {
        this.appLabel = appLabel;
        this.appIcon = appIcon;
        this.app_size = app_size;
        this.app_version_name = app_version_name;
        this.app_Package_Name = app_Package_Name;
    }

    public String getApp_Package_Name() {
        return app_Package_Name;
    }

    public void setApp_Package_Name(String app_Package_Name) {
        this.app_Package_Name = app_Package_Name;
    }

    public String getAppLabel() {
        return appLabel;
    }
    public void setAppLabel(String appName) {
        this.appLabel = appName;
    }
    public Drawable getAppIcon() {
        return appIcon;
    }
    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }


    public String getApp_size() {
        return app_size;
    }

    public String getApp_version_name() {
        return app_version_name;
    }

    public void setApp_size(String app_size) {
        this.app_size = app_size;
    }

    public void setApp_version_name(String app_version_name) {
        this.app_version_name = app_version_name;
    }
}
