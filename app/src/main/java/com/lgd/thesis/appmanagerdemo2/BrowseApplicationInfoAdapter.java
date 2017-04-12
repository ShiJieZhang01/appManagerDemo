package com.lgd.thesis.appmanagerdemo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 17/4/11.
 */

public class BrowseApplicationInfoAdapter extends BaseAdapter {
    private List<AppInfo> mlistAppInfo = null;

    LayoutInflater infater = null;

    public BrowseApplicationInfoAdapter(Context context,  List<AppInfo> apps) {
        infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlistAppInfo = apps ;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("size" + mlistAppInfo.size());
        return mlistAppInfo.size();
    }

    public void addList(AppInfo appInfo){
        if(appInfo != null){
            mlistAppInfo.add(appInfo);
        }
        notifyDataSetChanged();

    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlistAppInfo.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertview, ViewGroup arg2) {
        System.out.println("getView at " + position);
        View view = null;
        ViewHolder holder = null;
        if (convertview == null || convertview.getTag() == null) {
            view = infater.inflate(R.layout.browse_app_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = convertview ;
            holder = (ViewHolder) convertview.getTag() ;
        }
        AppInfo appInfo = (AppInfo) getItem(position);
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.tvAppLabel.setText(appInfo.getAppLabel());
        holder.tv_App_Size.setText(appInfo.getApp_size());
        holder.tv_App_Version_Name.setText(appInfo.getApp_version_name());
        return view;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView tvAppLabel;
        TextView tv_App_Size;
        TextView tv_App_Version_Name;

        public ViewHolder(View view) {
            this.appIcon = (ImageView) view.findViewById(R.id.imgApp);
            this.tvAppLabel = (TextView) view.findViewById(R.id.tv_App_Label);
            this.tv_App_Size = (TextView) view.findViewById(R.id.tv_App_Size);
            this.tv_App_Version_Name = (TextView) view.findViewById(R.id.tv_App_version_name);
        }
    }
}
