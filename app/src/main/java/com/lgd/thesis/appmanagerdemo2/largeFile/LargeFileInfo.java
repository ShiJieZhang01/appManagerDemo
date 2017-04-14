package com.lgd.thesis.appmanagerdemo2.largeFile;

import java.io.Serializable;

/**
 * Created by admin on 17/4/14.
 */

public class LargeFileInfo implements Serializable{
    private String large_file_name;
    private long large_file_size;

    public LargeFileInfo() {
    }

    public LargeFileInfo(String large_file_name, long large_file_size) {
        this.large_file_name = large_file_name;
        this.large_file_size = large_file_size;
    }

    public String getLarge_file_name() {
        return large_file_name;
    }

    public long getLarge_file_size() {
        return large_file_size;
    }

    public void setLarge_file_name(String large_file_name) {
        this.large_file_name = large_file_name;
    }

    public void setLarge_file_size(long large_file_size) {
        this.large_file_size = large_file_size;
    }
}
