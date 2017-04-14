package com.lgd.thesis.appmanagerdemo2;

import android.app.Application;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 17/4/13.
 */

public class AppManagerInfo extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidProcesses.setLoggingEnabled(true);
        Picasso.setSingletonInstance(new Picasso.Builder(this)
                .addRequestHandler(new AppIconRequestHandler(this))
                .build());
    }
}
