package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ToastUtil;
import com.onezao.zao.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "ZAO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Activity间传递数据
        initIntentData();
        //初始化界面
        initUpdate();

    }

    //处理自定义的自动更新控件
    private void initUpdate() {
     final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
     //获取本地已有的开关状态，用作显示  ,  默认是false
       boolean update_check_sp = SpUtils.getBoolean(this, ConstantValue.UPDATE_FILE,ConstantValue.OPEN_UPDATE,false);
     //是否选中，根据上一次存储的结果去做决定
     siv_update.setCheck(update_check_sp);

       siv_update.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             //获取点击之前，当前控件的选中状态
             boolean siv_check = siv_update.isCheck();

             //如果之前是选中的，点击之后，变成未选中
             //如果之前是未选中的，点击之后，变成选中
             //简单取反就可以实现
             siv_update.setCheck(!siv_check);

             //将取反后的状态，存储到相应SP中
             SpUtils.putBoolean(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.OPEN_UPDATE,!siv_check);
         }
     });
    }

    /**
     * 传递从上一个Activity携带的数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        String cool = intent.getStringExtra("item");
        Log.i(TAG,cool);
        ToastUtil.showT(this,cool);
    }
}
