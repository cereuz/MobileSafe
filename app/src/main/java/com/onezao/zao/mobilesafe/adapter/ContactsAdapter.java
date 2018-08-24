package com.onezao.zao.mobilesafe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.bean.Contacts;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;

import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    private List<Contacts> mContactList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_id;
        TextView tv_name;
        TextView tv_phone;
        ImageView iv_pic;

        public ViewHolder(View view) {
            super(view);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            iv_pic = (ImageView)view.findViewById(R.id.iv_pic);
        }
    }

    public ContactsAdapter(List<Contacts> mContactList) {
        this.mContactList = mContactList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact,parent,false);
        ContactsAdapter.ViewHolder holder = new ContactsAdapter.ViewHolder(view);
        return holder;
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, final int position) {
        Contacts  contact = mContactList.get(position);
        holder.tv_id.setText(contact.getId());
        holder.tv_name.setText(contact.getName());
        holder.tv_phone.setText(contact.getPhone());
        holder.iv_pic.setImageResource(contact.getImageId());

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
    }
    //返回子项个数
    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    //声明条目点击事件的接口
    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
