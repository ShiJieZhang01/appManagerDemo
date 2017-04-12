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

import com.lgd.thesis.appmanagerdemo2.databinding.ActivityMainBinding;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
                    final Drawable icon = reInfo.applicationInfo.loadIcon(pm); // 获得应用程序图标
                    final String version_name = reInfo.versionName+"."+reInfo.versionCode;
                    try {
                        Method getPackageSizeInfo = getPackageManager().getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
                        getPackageSizeInfo.invoke(getPackageManager(),reInfo.packageName, new IPackageStatsObserver.Stub(){
                            @Override
                            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                if(succeeded && pStats.codeSize > 0){
                                    mlistAppInfo.add(new AppInfo(appLabel,icon,formateFileSize(pStats.codeSize),version_name,reInfo.packageName));
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
        Intent luanch_Intent = getPackageManager().getLaunchIntentForPackage(app_package_name);
        startActivity(luanch_Intent);
    }
}
