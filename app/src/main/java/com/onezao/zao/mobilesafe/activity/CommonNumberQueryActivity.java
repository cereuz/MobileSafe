package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.engine.CommonNumberDao;

import java.util.List;

public class CommonNumberQueryActivity extends AppCompatActivity{

       ExpandableListView elv_common_number;
       List<CommonNumberDao.Group> mGroupList;
        MyExpandableAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonnumberquery_view);

        initUI();
        //初始化数据
        initData();
    }

    /**
     * 给可扩展listView准备数据，并且填充
     */
    private void initData() {
        CommonNumberDao cnDao = new CommonNumberDao();
        mGroupList = cnDao.getGroup();
        mAdapter = new MyExpandableAdapter();
        elv_common_number.setAdapter(mAdapter);


        elv_common_number.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
/*              //只使用一个TextView的情况下可以使用这样的操作！
                // 将对应位置view转换成textview
                TextView view = (TextView) v;
                // 拿到内容，取里面的电话号码
                String phone = view.getText().toString().split("\n")[1].trim();*/
                String phone = mAdapter.getChild(groupPosition,childPosition).number;
                // 发送拨号器意图
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                // 开始意图
                startActivity(intent);
                return true;
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        elv_common_number = (ExpandableListView)findViewById(R.id.elv_common_number);
    }


    class MyExpandableAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return mGroupList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mGroupList.get(i).childList.size();
        }

        @Override
        public CommonNumberDao.Group getGroup(int i) {
            return mGroupList.get(i);
        }

        @Override
        public CommonNumberDao.Child getChild(int i, int i1) {
            return mGroupList.get(i).childList.get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText("             " + getGroup(i).name);
            textView.setTextColor(Color.RED);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            textView.setHeight(80);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(),R.layout.item_commonnumber_elv_child,null);
            TextView tv_common_number_name = (TextView)view.findViewById(R.id.tv_common_number_name);
            TextView tv_common_number_number = (TextView)view.findViewById(R.id.tv_common_number_number);
            ImageView iv_common_number_icon = (ImageView)view.findViewById(R.id.iv_common_number_icon);

            tv_common_number_name.setText(getChild(i,i1).name);
            tv_common_number_number.setText(getChild(i,i1).number);
            iv_common_number_icon.setImageResource(R.mipmap.ic_launcher);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
