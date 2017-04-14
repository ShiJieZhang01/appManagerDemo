package com.lgd.thesis.appmanagerdemo2;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 17/4/11.
 */

public class AppInfo {
    private String appLabel;    //应用程序标签
    private Drawable appIcon ;  //应用程序图像
    private String app_size_formate ;    //应用程序所对应的大小
    private String app_version_name;  // version name
    private String app_Package_Name;
    private long last_update_time;
    private long app_size;
    private String app_source_dir;

    public AppInfo(){}

    public AppInfo(String appLabel, Drawable appIcon, String app_size_formate, String app_version_name,
                   String app_Package_Name, long last_update_time, long app_size, String app_source_dir) {
        this.appLabel = appLabel;
        this.appIcon = appIcon;
        this.app_size_formate = app_size_formate;
        this.app_version_name = app_version_name;
        this.app_Package_Name = app_Package_Name;
        this.last_update_time = last_update_time;
        this.app_size = app_size;
        this.app_source_dir = app_source_dir;
    }

    public String getApp_source_dir() {
        return app_source_dir;
    }

    public void setApp_source_dir(String app_source_dir) {
        this.app_source_dir = app_source_dir;
    }

    public String getApp_size_formate() {
        return app_size_formate;
    }

    public long getLast_update_time() {
        return last_update_time;
    }

    public long getApp_size() {
        return app_size;
    }

    public void setApp_size_formate(String app_size_formate) {
        this.app_size_formate = app_size_formate;
    }

    public void setLast_update_time(long last_update_time) {
        this.last_update_time = last_update_time;
    }

    public void setApp_size(long app_size) {
        this.app_size = app_size;
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



    public String getApp_version_name() {
        return app_version_name;
    }

    public void setApp_version_name(String app_version_name) {
        this.app_version_name = app_version_name;
    }
}
