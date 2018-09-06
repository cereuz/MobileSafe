package com.onezao.zao.mobilesafe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.bean.AppInfo;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;

import java.util.List;


public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder>{

    private List<AppInfo> mAppInfoList;
    private ItemImageViewInterface imageViewInterface;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_appinfo_name;
        TextView tv_appinfo_packagename;
        TextView tv_appinfo_issdcard;
        ImageView iv_appinfo_icon;

        public ViewHolder(View view) {
            super(view);
            tv_appinfo_name = (TextView) view.findViewById(R.id.tv_appinfo_name);
            tv_appinfo_packagename = (TextView) view.findViewById(R.id.tv_appinfo_packagename);
            tv_appinfo_issdcard = (TextView) view.findViewById(R.id.tv_appinfo_issdcard);
            iv_appinfo_icon = (ImageView)view.findViewById(R.id.iv_appinfo_icon);
        }
    }

    public AppInfoAdapter(List<AppInfo> mAppInfoList) {
        this.mAppInfoList = mAppInfoList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public AppInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info,parent,false);
        AppInfoAdapter.ViewHolder holder = new AppInfoAdapter.ViewHolder(view);
        return holder;
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(AppInfoAdapter.ViewHolder holder, final int position) {

/*        //
        *//**
         * 让屏幕的条目占满下半部分，recyclerView高度不能自适应（item满屏）
         *//*
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;*/


        AppInfo appInfo = mAppInfoList.get(position);
        holder.iv_appinfo_icon.setImageDrawable(appInfo.getIcon());
        holder.tv_appinfo_name.setText(appInfo.getName());
        holder.tv_appinfo_packagename.setText(appInfo.getPackageName());

        boolean isSdcard = appInfo.isSdcard();
        if(isSdcard) {
            holder.tv_appinfo_issdcard.setText("SD卡应用");
        }  else {
            holder.tv_appinfo_issdcard.setText("手机应用");
        }

        //条目点击事件增加的
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onItemLongClick(v, position);
                return true;
            }
        });

        //条目item中的ImageView 的点击事件
        holder.iv_appinfo_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewInterface !=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    imageViewInterface.onclick(v,position);
                }

            }
        });
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }

    /**
     * 声明条目点击事件的接口
     */
    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     *按钮点击事件需要的方法
     */
    public void setOnItemImageViewClickListener(ItemImageViewInterface itemImageViewInterface){
        this.imageViewInterface = itemImageViewInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ItemImageViewInterface {
        public void onclick(View view, int position);
    }

}
