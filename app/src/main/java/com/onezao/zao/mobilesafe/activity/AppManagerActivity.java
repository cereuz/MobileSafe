package com.onezao.zao.mobilesafe.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;

public class AppManagerActivity extends AppCompatActivity{

        TextView tv_disk_inuse;
        TextView tv_sd_inuse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager_view);

        //初始化界面
        initUI();
        //初始化磁盘可用和Sd卡可用空间
        initInUseSpace();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        tv_disk_inuse = (TextView)findViewById(R.id.tv_disk_inuse);
        tv_sd_inuse = (TextView)findViewById(R.id.tv_sd_inuse);
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
