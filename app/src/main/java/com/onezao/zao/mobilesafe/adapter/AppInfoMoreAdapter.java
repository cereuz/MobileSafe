package com.onezao.zao.mobilesafe.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.bean.AppInfo;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;

import java.util.List;


public class AppInfoMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AppInfo> mAppInfoList;
    List<AppInfo> mSystemList;
    List<AppInfo> mCustomerList;
    AppInfo appInfo;
    private ItemImageViewInterface imageViewInterface;

    //一。定义三种常量  表示三种条目类型
    public static final int TYPE_APP_INFO = 0;
    public static final int TYPE_ISTITLE = 1;

    static class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView tv_appinfo_name;
        TextView tv_appinfo_packagename;
        TextView tv_appinfo_issdcard;
        ImageView iv_appinfo_icon;

        public NormalViewHolder(View view) {
            super(view);
            tv_appinfo_name = (TextView) view.findViewById(R.id.tv_appinfo_name);
            tv_appinfo_packagename = (TextView) view.findViewById(R.id.tv_appinfo_packagename);
            tv_appinfo_issdcard = (TextView) view.findViewById(R.id.tv_appinfo_issdcard);
            iv_appinfo_icon = (ImageView)view.findViewById(R.id.iv_appinfo_icon);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView tv_appinfo_more_title;

        public TitleViewHolder(View view) {
            super(view);
            tv_appinfo_more_title = (TextView) view.findViewById(R.id.tv_appinfo_more_title);
        }
    }

    public AppInfoMoreAdapter(List<AppInfo> mAppInfoList,List<AppInfo> mSystemList,List<AppInfo> mCustomerList) {
        this.mAppInfoList = mAppInfoList;
        this.mSystemList = mSystemList;
        this.mCustomerList = mCustomerList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //二创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        if (viewType == TYPE_APP_INFO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info,parent,false);
            NormalViewHolder holder = new NormalViewHolder(view);
            return holder;
        } else if (viewType == TYPE_ISTITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appinfo_istitle,parent,false);
            TitleViewHolder holder = new TitleViewHolder(view);
            return holder;
            }
        return null;
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

/*    }
    @Override
    public void onBindViewHolder(AppInfoMoreAdapter.ViewHolder holder, final int position) {*/

/*        //
        *//**
         * 让屏幕的条目占满下半部分，recyclerView高度不能自适应（item满屏）
         *//*
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;*/

        //按照自己定义的ViewHolder来设置显示内容
        if(holder instanceof TitleViewHolder){
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

                if(position == 0){
                        titleViewHolder.tv_appinfo_more_title.setText("用户应用" + "( " + mCustomerList.size() + " 个)");
                } else if(position == mCustomerList.size() + 1) {
                        titleViewHolder.tv_appinfo_more_title.setText("系统应用" + "( " + mSystemList.size() + " 个)");
                }
          }

        if(holder instanceof NormalViewHolder){
               NormalViewHolder normalViewHolder = (NormalViewHolder)holder;
               //图文条目
               if(position < mCustomerList.size() + 1 && position > 0){
                appInfo = mCustomerList.get(position - 1);
                setView(normalViewHolder);
               }  else if( position > mCustomerList.size()) {
                appInfo = mSystemList.get(position - mCustomerList.size() - 2);
                setView(normalViewHolder);
              }



            //条目点击事件增加的,图片的点击事件
            normalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,position);
                }
            });

            //条目的长按点击事件
            normalViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
            });

            //条目item中的ImageView 的点击事件
            normalViewHolder.iv_appinfo_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageViewInterface !=null) {
//                  接口实例化后的而对象，调用重写后的方法
                        imageViewInterface.onclick(v,position);
                    }

                }
            });
        }
    }

    private void setView(NormalViewHolder normalViewHolder) {
        normalViewHolder.iv_appinfo_icon.setImageDrawable(appInfo.getIcon());
        normalViewHolder.tv_appinfo_name.setText(appInfo.getName());
        normalViewHolder.tv_appinfo_packagename.setText(appInfo.getPackageName());

        boolean isSdcard = appInfo.isSdcard();
        if(isSdcard) {
            normalViewHolder.tv_appinfo_issdcard.setText("SD卡应用");
        }  else {
            normalViewHolder.tv_appinfo_issdcard.setText("手机应用");
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
//        return mAppInfoList.size();
        //增加了两个 头标题
        return mSystemList.size() + mCustomerList.size() + 2;
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

    //根据条件返回条目的类型
    @Override
    public int getItemViewType(int position) {
        //区分是头标题文本，还是图片文本混合显示
        if (position == 0 || position == mCustomerList.size() + 1) {
            return TYPE_ISTITLE;
        } else {
            return TYPE_APP_INFO;
        }
    }
}
