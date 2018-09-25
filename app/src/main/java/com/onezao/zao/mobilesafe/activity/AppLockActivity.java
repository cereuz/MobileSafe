package com.onezao.zao.mobilesafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.bean.AppInfo;
import com.onezao.zao.mobilesafe.db.dao.AppLockDao;
import com.onezao.zao.mobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends AppCompatActivity{

        TextView tv_applock_unlock,tv_applock_locked,tv_applock_number;
        AppLockDao mDao;
        List<AppInfo>  mAppInfoList;
        List<String>  mAppLockList;
        List<AppInfo>  mLockedList;
        List<AppInfo>  mUnLockList;

        private Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //6.接收到消息，填充已加锁和未加锁的数据适配器
            }
        };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock_view);

        initUI();
        initData();
    }

    /**
     * 初始化数据
     * 区分未加锁和已加锁应用的集合
     */
    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
        //1.回去手机中所有的应用
        mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
        //2.获取数据库中已加锁应用包名的集合
        mDao = AppLockDao.getInstance(getApplicationContext());
        mAppLockList = mDao.findAll("packagename");
        //3.区分已加锁应用和未加锁应用
        mLockedList = new ArrayList<AppInfo>();
        mUnLockList = new ArrayList<AppInfo>();
        for (AppInfo appInfo : mAppInfoList){
            //4.如果循环到的应用包名，在数据库中，则说明是已加锁应用
            if(mAppLockList.contains(appInfo.packageName)){
                mLockedList.add(appInfo);
            }  else {
                mUnLockList.add(appInfo);
            }
        }
        //5.告诉主线程，可以使用维护好的数据了
           mHandler.sendEmptyMessage(0);
          }
        }.start();

    }

    /**
     * 初始化界面
     */
    private void initUI() {
        tv_applock_unlock = (TextView)findViewById(R.id.tv_applock_unlock);
        tv_applock_locked = (TextView)findViewById(R.id.tv_applock_locked);
        tv_applock_number = (TextView)findViewById(R.id.tv_applock_number);
    }
}
