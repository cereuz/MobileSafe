package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.adapter.AppInfoMoreAdapter;
import com.onezao.zao.mobilesafe.bean.AppInfo;
import com.onezao.zao.mobilesafe.engine.AppInfoProvider;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;
import com.onezao.zao.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AppManagerMoreActivity extends AppCompatActivity{

        TextView tv_appinfo_title;
        TextView tv_disk_inuse;
        TextView tv_sd_inuse;
        RecyclerView rv_appinfo;

        List<AppInfo> mAppInfoList;
        List<AppInfo> mCustomerList;
        List<AppInfo> mSystemList;
        AppInfoMoreAdapter mAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //初始化recycleView的显示
            initAPPRV(mAppInfoList,mSystemList,mCustomerList);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager_view);

        //初始化界面
        initUI();
        //初始化磁盘可用和Sd卡可用空间
        initInUseSpace();
        //初始化手机应用管理的数据
        initAppData();
    }

    /**
     * 初始化应用的数据
     */
    private void initAppData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                //分割系统应用和用户应用
                mCustomerList = new ArrayList<AppInfo>();
                mSystemList = new ArrayList<AppInfo>();
                for(AppInfo appInfo : mAppInfoList){
                    if(!appInfo.isSystem()){
                        mCustomerList.add(appInfo);
                    } else {
                        mSystemList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化RecycleView的内容以及显示  mCustomerList  mSystemList
     */
    private void initAPPRV(final List<AppInfo> mAppInfoList,List<AppInfo> mSystemList,List<AppInfo> mCustomerList) {

        //设置RecyclerView管理器
        rv_appinfo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new AppInfoMoreAdapter(mAppInfoList,mSystemList,mCustomerList);
        //设置添加或删除item时的动画，这里使用默认动画
        rv_appinfo.setItemAnimator(new DefaultItemAnimator());

        /**
         * 打印集合的数据
         */
/*        for(AppInfo info : mAppInfoList){
            Log.i("Zao","Name = " + info.getName() + "  PackageName = " + info.getPackageName() + " Icon = " + info.getIcon() + "isSDcard : " + info.isSdcard() + "isSystem : " + info.isSystem());
        }*/

        //设置条目点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"点击条目 : " + position ,Toast.LENGTH_SHORT).show();
/*                //处理条目的数据
                initItemData(position);*/
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        //设置ImageView的点击事件
        mAdapter.setOnItemImageViewClickListener(new AppInfoMoreAdapter.ItemImageViewInterface(){
            @Override
            public void onclick(View view, int position) {
                Toast.makeText(getApplicationContext(),"删除 : " + position ,Toast.LENGTH_SHORT).show();
/*                //从数据库中删除数据
                mAppInfoList.delete(mAppInfoList.get(position).getPhone());
                //从集合中删除数据
                mAppInfoList.remove(position);
                //4.通知适配器，数据改变了。
                mAdapter.notifyDataSetChanged();*/
            }
        });

        //设置适配器
        rv_appinfo.setAdapter(mAdapter);
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        tv_appinfo_title = (TextView)findViewById(R.id.tv_appinfo_title);
        tv_appinfo_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.showT(getApplicationContext(),"长按 !!!");
                startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
                return false;
            }
        });


        tv_disk_inuse = (TextView)findViewById(R.id.tv_disk_inuse);
        tv_sd_inuse = (TextView)findViewById(R.id.tv_sd_inuse);
        rv_appinfo = (RecyclerView) findViewById(R.id.rv_appinfo);
    }

    /**
     * 初始化
     */
    private void initInUseSpace() {
         // 调用方法的到结果设置显示内存和sd信息
        // 显示内存大小
        tv_disk_inuse.setText("内存可用:"
                + Formatter.formatFileSize(this, Environment.getDataDirectory()
                .getFreeSpace()));
        // 显示sd卡大小
        tv_sd_inuse.setText("SD卡可用:"
                + Formatter.formatFileSize(this, Environment
                .getExternalStorageDirectory().getFreeSpace()));
    }
}
