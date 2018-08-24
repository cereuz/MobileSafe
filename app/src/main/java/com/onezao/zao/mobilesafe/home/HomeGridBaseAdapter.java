package com.onezao.zao.mobilesafe.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;

import java.util.List;

public class HomeGridBaseAdapter extends RecyclerView.Adapter<HomeGridBaseAdapter.GridViewHolder>{

private List<Book> mBookList;

static class GridViewHolder extends RecyclerView.ViewHolder{
    ImageView bookImage;
    TextView bookname;

    public GridViewHolder(View view) {
        super(view);
        bookImage = (ImageView) view.findViewById(R.id.book_iamge);
        bookname = (TextView) view.findViewById(R.id.book_name);
    }
}

public HomeGridBaseAdapter(List<Book> mBookList) {
    this.mBookList = mBookList;
}

//加载item 的布局  创建ViewHolder实例
@Override
public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book,parent,false);
    GridViewHolder holder = new GridViewHolder(view);


    return holder;
}

    @Override
    public void onBindViewHolder(@NonNull HomeGridBaseAdapter.GridViewHolder gridViewHolder, final int position) {
        Book book = mBookList.get(position);
        gridViewHolder.bookname.setText(book.getName());
        gridViewHolder.bookImage.setImageResource(book.getImageId());

        //条目点击事件增加的
        gridViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
        gridViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
            return mBookList.size();
        }


    //条目点击事件
    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
