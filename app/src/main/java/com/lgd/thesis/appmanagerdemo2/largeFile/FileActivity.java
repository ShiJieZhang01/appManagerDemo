package com.lgd.thesis.appmanagerdemo2.largeFile;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.lgd.thesis.appmanagerdemo2.R;
import com.lgd.thesis.appmanagerdemo2.databinding.ActivityFileBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FileActivity extends Activity {

    private static final String TAG = "fileActivity zsj tag";
    private static final int  REQUEST_CODE_SOME_FEATURES_PERMISSIONS=1;

    ActivityFileBinding mBinding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_file);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCallPhonePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            List<String> permissions = new ArrayList<String>();
            if (hasCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
//                AbSharedUtil.putString(this, "storage", "true");
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }else {//小于6.0
//            AbSharedUtil.putString(this,"storage", "true");
        }
        findLargeFiles();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.e("TTT","Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.e("TTT","Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void findLargeFiles(){
        ContentResolver resolver = getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] protection = new String[]{"_data","_size"};
        Cursor cursor = resolver.query(uri,protection,null,null,null);
        while(cursor.moveToNext()){
            String dataName = cursor.getString(cursor.getColumnIndex("_data"));
            long fileSize = cursor.getLong(cursor.getColumnIndex("_size"));
            if(fileSize > 1024*1024*10){
                Log.d(TAG, "findLargeFiles: -----------");
                Log.d(TAG, "findLargeFiles: "+dataName);
                Log.d(TAG, "findLargeFiles: "+fileSize);
                Log.d(TAG, "findLargeFiles: -----------");
            }


        }
    }
    private Observable<List<LargeFileInfo>> getLargeFiles(){

        Observable<List<LargeFileInfo>> observable = Observable.create(new Observable.OnSubscribe<List<LargeFileInfo>>() {
            @Override
            public void call(Subscriber<? super List<LargeFileInfo>> subscriber) {
                List<LargeFileInfo> largeFileInfoList = queryLargeFiles();
                Log.d(TAG, "call: largefileinfolist"+largeFileInfoList.size());
                subscriber.onNext(largeFileInfoList);
                subscriber.onError(new Throwable("on Error to query large file"));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
    private List<LargeFileInfo> queryLargeFiles(){
        List<LargeFileInfo>  large_files_list = new ArrayList<>();


        String path="/storage/emulated/0/";
        File all_files=new File(path);
        ArrayList<File> listfile = toListFile(all_files.listFiles());
        Log.d(TAG, "queryLargeFiles: listfile.size"+listfile.size());
        LargeFileInfo large_file_info;
        for (File large_file : listfile) {
            large_file_info = new LargeFileInfo(large_file.getName(),large_file.length());
            large_files_list.add(large_file_info);
        }
        return large_files_list;
    }

    private ArrayList<File> toListFile(File[] listFiles) {
        ArrayList<File> list_file = new ArrayList<>();
        for (int i = 0; i < listFiles.length; i++) {
            if(listFiles[i].length() > 1024*1024*10){
                if(listFiles[i].isDirectory()){
                    toListFile(listFiles[i].listFiles());
                }
                if(listFiles[i].isFile()){
                    list_file.add(listFiles[i]);
                }
            }

        }
        return list_file;
    }
}
