package com.onezao.zao.mobilesafe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.adapter.ContactsAdapter;
import com.onezao.zao.mobilesafe.bean.Contacts;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    List<Contacts> mContactList = new ArrayList<Contacts>();
    ContactsAdapter mAdapter;

    //消息机制
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //8. 填充数据适配器
            initUI();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initData();
//        initUI();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //如果手机联系人较多，就是耗时操作，所以建议放在子线程处理
        new Thread(){
            @Override
            public void run() {
         //1. 获取内容解析器对象
        ContentResolver contentResolver = getContentResolver();
        //2. 查询系统联系人数据库的过程(读取联系人权限)
        Cursor cursor =contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"},
                null,null,null);
        //清空集合数据
                mContactList.clear();
        //3. 循环游标，直到没有数据为止
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    Log.i(ConstantValue.TAG, "id = " + id);
                    if(id == null){
                    Log.i(ConstantValue.TAG, "这是一个空值 id = " + id);
                    }  else {
                        //4. 根据用户唯一性id值，查询data表和mimetype表生成的视图，获取data以及mimetype字段
                        Cursor indexCursor = contentResolver.query(
                                Uri.parse("content://com.android.contacts/data"),
                                new String[]{"data1", "mimetype"},
                                "raw_contact_id = ?",
                                new String[]{id},
                                null);
                        if (indexCursor == null) {
                            Log.i(ConstantValue.TAG, "内容为空！ " + id);
                        } else {
                            //5. 再次循环游标，获取每一个联系人的电话号码以及姓名，数据类型
                            Contacts contacts = new Contacts();
                            while (indexCursor.moveToNext()) {
                                String data = indexCursor.getString(0);
                                String mimetype = indexCursor.getString(1);
                                Log.i(ConstantValue.TAG, "data = " + data);
                                Log.i(ConstantValue.TAG, "mimetype = " + mimetype);


                                //区分类型 填充数据
                                contacts.setId(id);
                                contacts.setImageId(R.mipmap.ic_launcher);
                                if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
                                    //数据非空判断
                                    if(!TextUtils.isEmpty(data)){
                                        contacts.setPhone(data);
                                    }
                                } else if(mimetype.equals("vnd.android.cursor.item/name")){
                                    if(!TextUtils.isEmpty(data)){
                                        contacts.setName(data);
                                    }
                                }
                            }
                            //循环结束，关闭内部游标
                            indexCursor.close();
                            mContactList.add(contacts);
                        }
                    }
                }
                //循环结束，关闭游标
                cursor.close();
                //7. 消息机制
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化界面
     */
    private void initUI() {

/*        for(int i = 0; i < 100; i++){
        Contacts contacts = new Contacts();
            contacts.setId("ID : " + i);
            contacts.setName("Tom--" + i);
            contacts.setPhone("1366666999" + i);
            contacts.setImageId(R.mipmap.ic_launcher);
            mContactList.add(contacts);
        }
        for(int i = 0; i < 100; i++) {
            Log.i(ConstantValue.TAG, mContactList.get(i).name);
        }*/

        //通过findViewById拿到RecyclerView实例
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.rv_contact);
        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new ContactsAdapter(mContactList);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置条目点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //处理条目的数据
                initItemData(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initItemData(int position) {
        String id = mContactList.get(position).getId();
        String name = mContactList.get(position).getName();
        String phone = mContactList.get(position).getPhone();
        ToastUtil.showT(getApplicationContext(),"id = " + id + "\nname = " + name + "\nphone = " + phone);

        //3. 获取信息给第三个导航界面使用
        Intent intent = new Intent();
        intent.putExtra("phone",phone);
        setResult(0,intent);

        //关闭当前页面
        finish();
    }
}
