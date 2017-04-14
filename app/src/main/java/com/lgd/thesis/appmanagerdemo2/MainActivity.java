package com.lgd.thesis.appmanagerdemo2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.AndroidProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.lgd.thesis.appmanagerdemo2.databinding.ActivityMainBinding;
import com.lgd.thesis.appmanagerdemo2.largeFile.LargeFileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "appinfo zsj";
    private PackageManager pm;
    List<AppInfo> appinfos_listItem;
    private BrowseApplicationInfoAdapter browseAppAdapter;
    ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        appinfos_listItem = new ArrayList<>();
        preloadInstallPackage().subscribe(new Subscriber<List<AppInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                Log.d(TAG, "onError: throwable"+e.toString());
            }

            @Override
            public void onNext(List<AppInfo> appInfos) {
                Log.d(TAG, "onNext: appinfo" + appInfos.size());

                    appinfos_listItem.clear();
                    appinfos_listItem.addAll(appInfos);
                    browseAppAdapter.notifyDataSetChanged();
            }
        });

        browseAppAdapter = new BrowseApplicationInfoAdapter(MainActivity.this, appinfos_listItem);
        mBinding.listviewApp.setAdapter(browseAppAdapter);
        mBinding.listviewApp.setOnItemClickListener(this);

     

    }



    private Observable<List<AppInfo>> preloadInstallPackage() {
        Observable<List<AppInfo>> observable = Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                List<AppInfo> appInfoList = queryAppInfo();
                if (appInfoList != null && appInfoList.size() > 0) {

                    Collections.sort(appInfoList, new Comparator<AppInfo>() {
                        @Override
                        public int compare(AppInfo o1, AppInfo o2) {
                            return (int) (o2.getApp_size() - o1.getApp_size());
                        }
                    });
                    subscriber.onNext(appInfoList);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("empty list error"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    public List<AppInfo> queryAppInfo()  {
        final List<AppInfo> mlistAppInfo = new ArrayList<>();
        pm = this.getPackageManager(); // 获得PackageManager对象
        List<PackageInfo> listAppcations = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();
            for (final PackageInfo reInfo : listAppcations) {
                if((reInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    final String appLabel = (String) reInfo.applicationInfo.loadLabel(pm); // 获得应用程序的Label
                    Log.d(TAG, "queryAppInfo: "+appLabel+"-----"+reInfo.applicationInfo.sourceDir);
                    final Drawable icon = reInfo.applicationInfo.loadIcon(pm); // 获得应用程序图标
                    final String version_name = reInfo.versionName+"."+reInfo.versionCode;
                    final long last_update_time = reInfo.lastUpdateTime;
                    try {
                        Method getPackageSizeInfo = getPackageManager().getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
                        getPackageSizeInfo.invoke(getPackageManager(),reInfo.packageName, new IPackageStatsObserver.Stub(){
                            @Override
                            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                if(succeeded && pStats.codeSize > 0){
                                    mlistAppInfo.add(new AppInfo(appLabel,icon,formateFileSize(pStats.codeSize),
                                            version_name,reInfo.packageName,last_update_time,pStats.codeSize,reInfo.applicationInfo.sourceDir));
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return mlistAppInfo;
    }

    private String formateFileSize(long size){
        return Formatter.formatFileSize(MainActivity.this, size);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String app_package_name = appinfos_listItem.get(position).getApp_Package_Name();
        Log.d(TAG, "onItemClick: "+app_package_name);

//        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view_appManager_dialog = inflater.inflate(R.layout.dialog_app_manager_info,null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("a");
//        builder.create().show();

        //卸载
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + app_package_name));
//
//        startActivity(intent);

        //打开
//        Intent luanch_Intent = getPackageManager().getLaunchIntentForPackage(app_package_name);
//        startActivity(luanch_Intent);

        Log.d(TAG, "onItemClick: Envirnment"+Environment.getExternalStorageDirectory());
        //back up  安装
//        String fileName = appinfos_listItem.get(position).getApp_source_dir();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
//        startActivity(intent);
        
        // 复制文件

//        String old_source_path = appinfos_listItem.get(position).getApp_source_dir();
//        String new_source_path = "/cleaner"+appinfos_listItem.get(position).getApp_source_dir();
//
//        File old_source_dir = new File(old_source_path);
//        File new_source_dir = new File(new_source_path);
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(old_source_path);
//            FileOutputStream fos = new FileOutputStream(new_source_path);
//            int len = 0;
//            byte[] buf = new byte[1024];
//            while ((len = fis.read(buf)) != -1) {
//                fos.write(buf, 0, len);
//            }
//            fos.close();
//            fis.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
        File file = null;
        File file_dir = new File("cleaner");
        file_dir.mkdir();
        try {
            file = new File("cleaner/a.word");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
