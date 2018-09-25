package com.onezao.zao.mobilesafe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.db.domain.BNAppInfo;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;

import java.util.List;


public class BlackNumberAdapter extends RecyclerView.Adapter<BlackNumberAdapter.ViewHolder>{

    private List<BNAppInfo> mBlackNumberInfoList;
    private ItemImageViewInterface imageViewInterface;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_bn_phone;
        TextView tv_bn_mode;
        TextView tv_bn_time;
        ImageView iv_bn_delete;

        public ViewHolder(View view) {
            super(view);
            tv_bn_phone = (TextView) view.findViewById(R.id.tv_appinfo_name);
            tv_bn_mode = (TextView) view.findViewById(R.id.tv_appinfo_packagename);
            tv_bn_time = (TextView) view.findViewById(R.id.tv_appinfo_issdcard);
            iv_bn_delete = (ImageView)view.findViewById(R.id.iv_appinfo_icon);
        }
    }

    public BlackNumberAdapter(List<BNAppInfo> mBlackNumberInfoList) {
        this.mBlackNumberInfoList = mBlackNumberInfoList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public BlackNumberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black_number,parent,false);
        BlackNumberAdapter.ViewHolder holder = new BlackNumberAdapter.ViewHolder(view);
        return holder;
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(BlackNumberAdapter.ViewHolder holder, final int position) {
        BNAppInfo blackNumberInfo = mBlackNumberInfoList.get(position);
        holder.tv_bn_phone.setText(blackNumberInfo.getPhone());
        holder.tv_bn_time.setText(blackNumberInfo.getTime());
        holder.iv_bn_delete.setImageResource(R.mipmap.ic_launcher);
        //模式需要显示文字   拦截模式（1：短信   2：电话   3：拦截所有（短信+电话））
        switch (Integer.parseInt(blackNumberInfo.getMode())){
            case 1 :
              holder.tv_bn_mode.setText("短信");
                break;
            case 2 :
              holder.tv_bn_mode.setText("电话");
                break;
            case 3 :
              holder.tv_bn_mode.setText("所有");
                break;
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
        holder.iv_bn_delete.setOnClickListener(new View.OnClickListener() {
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
        return mBlackNumberInfoList.size();
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
        public void onclick( View view,int position);
    }

}
