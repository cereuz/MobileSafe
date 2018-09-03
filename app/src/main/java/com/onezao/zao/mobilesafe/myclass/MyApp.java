package com.onezao.zao.mobilesafe.myclass;

import android.app.Application;
import android.util.Log;

import com.onezao.zao.mobilesafe.utils.AppFrontBackHelper;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ToastUtil;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register(MyApp.this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                Log.i(ConstantValue.TAG,"前台。。。");
                ToastUtil.showT(getApplicationContext(),"你的应用已经在前台了。");
            }

            @Override
            public void onBack() {
                //应用切到后台处理
                Log.i(ConstantValue.TAG,"后台。。。");
                ToastUtil.showT(getApplicationContext(),"你的应用已经切换到后台了。");

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.i(ConstantValue.TAG,"正在复制文件。。。");
                        ZaoUtils.copyDBtoSD(getApplicationContext());
                    }
                }.start();

            }
        });
    }

}