package com.onezao.zao.mobilesafe.activity;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SDcardUtil;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import java.io.File;

public class FileAndSpaceActivity extends AppCompatActivity {

        Button btn_search;
        TextView tv_disk;
        TextView tv_room;
        TextView tv_system;

        String dataTotal;
        String dataUsable;
        String dataFree;

        String sdcardTotal;
        String sdcardUsable;
        String sdcardFree;

        String systemTotal;
        String systemUsable;
        String systemFree;

        String text;
        String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileandspace_view);

        initUI();
        initData();
    }

    /**
     * 初始化数据
     *   Environment.getDataDirectory() = /data
         Environment.getDownloadCacheDirectory() = /cache
         Environment.getExternalStorageDirectory() = /mnt/sdcard
         Environment.getRootDirectory() = /system
         context.getCacheDir() = /data/data/com.mt.mtpp/cache
         context.getExternalCacheDir() = /mnt/sdcard/Android/data/com.mt.mtpp/cache
         context.getFilesDir() = /data/data/com.mt.mtpp/files
     */
    private void initData() {
        //    /data
        dataTotal = Formatter.formatFileSize(this, Environment.getDataDirectory().getTotalSpace());
        dataUsable = Formatter.formatFileSize(this, Environment.getDataDirectory().getUsableSpace());
        dataFree = Formatter.formatFileSize(this, Environment.getDataDirectory().getFreeSpace());

        //     /mnt/sdcard
        sdcardTotal = Formatter.formatFileSize(this, Environment.getExternalStorageDirectory().getTotalSpace());
        sdcardUsable = Formatter.formatFileSize(this, Environment.getExternalStorageDirectory().getUsableSpace());
        sdcardFree = Formatter.formatFileSize(this, Environment.getExternalStorageDirectory().getFreeSpace());

        //     /system
        systemTotal = Formatter.formatFileSize(this, Environment.getRootDirectory().getTotalSpace());
        systemUsable = Formatter.formatFileSize(this, Environment.getRootDirectory().getUsableSpace());
        systemFree = Formatter.formatFileSize(this, Environment.getRootDirectory().getFreeSpace());


        //获取外部存储空间
        String pathOut = Environment.getExternalStorageDirectory().getPath();
        String absolutePathOut = Environment.getExternalStorageDirectory().getAbsolutePath();
        long totalSpaceOut = Environment.getExternalStorageDirectory().getTotalSpace();
        long freeSpaceOut  = Environment.getExternalStorageDirectory().getFreeSpace();

        //获取内部存储空间
        String pathIn = Environment.getRootDirectory().getPath();
        String absolutePathIn = Environment.getRootDirectory().getAbsolutePath();
        long totalSpaceIn = Environment.getRootDirectory().getTotalSpace();
        long freeSpaceIn = Environment.getRootDirectory().getFreeSpace();
        //格式化空间大小
        String totalFileSizeOut = Formatter.formatFileSize(getApplicationContext(), totalSpaceOut);
        String freeFileSizeOut = Formatter.formatFileSize(getApplicationContext(), freeSpaceOut);
        String totalFileSizeIn = Formatter.formatFileSize(getApplicationContext(), totalSpaceIn);
        String freeFileSizeIn = Formatter.formatFileSize(getApplicationContext(), freeSpaceIn);

        //获取DataDirectory
        long totalDataDirectory = Environment.getDataDirectory().getTotalSpace();
        String totalSizeDataDirectory = Formatter.formatFileSize(getApplicationContext(), totalDataDirectory);

        long freeDataDirectory = Environment.getDataDirectory().getFreeSpace();
        String freeSizeDataDirectory = Formatter.formatFileSize(getApplicationContext(), freeDataDirectory);

        long totalDownloadCacheDirectory = Environment.getDownloadCacheDirectory().getTotalSpace();
        String totalFileDownloadCacheDirectory = Formatter.formatFileSize(getApplicationContext(), totalDownloadCacheDirectory);
        long freeDownloadCacheDirectory = Environment.getDownloadCacheDirectory().getFreeSpace();
        String freeFileDownloadCacheDirectory = Formatter.formatFileSize(getApplicationContext(), freeDownloadCacheDirectory);
        long totalDIRECTORYMUSIC = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getTotalSpace();
        String totalFileDIRECTORYMUSIC = Formatter.formatFileSize(getApplicationContext(), totalDIRECTORYMUSIC);
        long freeDIRECTORY_MUSIC = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getFreeSpace();
        String freeFileDIRECTORY_MUSIC = Formatter.formatFileSize(getApplicationContext(), freeDIRECTORY_MUSIC);

        String textOut = "pathOut："+pathOut + "\n"+"absolutePathOut："+absolutePathOut +"\n"+"totalFileSizeOut："+totalFileSizeOut+"\n"+"freeFileSizeOut："+freeFileSizeOut;
        String textIn  ="pathIn："+ pathIn + "\n"+ "absolutePathIn："+absolutePathIn+ "\n"+"totalFileSizeIn："+totalFileSizeIn+ "\n"+"freeFileSizeIn："+freeFileSizeIn;
        String textOther = "totalSizeDataDirectory："+totalSizeDataDirectory+"\n"+"freeSizeDataDirectory："+freeSizeDataDirectory+"\n"+"totalFileDownloadCacheDirectory："+totalFileDownloadCacheDirectory+"\n"+"freeFileDownloadCacheDirectory："+freeFileDownloadCacheDirectory+"\n"+"totalFileDIRECTORYMUSIC："+totalFileDIRECTORYMUSIC+"\n"+"freeFileDIRECTORY_MUSIC："+freeFileDIRECTORY_MUSIC;

        text = textOut + "\n" + textIn + "\n" + textOther;

        //设置数据显示在控件上
        setMyText();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        btn_search = (Button)findViewById(R.id.btn_search);
        tv_disk = (TextView)findViewById(R.id.tv_disk);
        tv_room = (TextView)findViewById(R.id.tv_room);
        tv_system = (TextView)findViewById(R.id.tv_system);
    }


    public void search(View view){
        setMyText();
        ZaoUtils.walk(new File(path));
    }

    private void setMyText() {
        tv_disk.setText("/data : \n" + "dataTotal : " + dataTotal + "\nUsable : " + dataUsable + "\nFree :　" + dataFree );
        tv_room.setText("/mnt/sdcard : \n" + "sdcardTotal : " + sdcardTotal + "\nUsable : " + sdcardUsable + "\nFree :　" + sdcardFree +
                "\n\n/system : \n" + "systemTotal : " + systemTotal + "\nUsable : " + systemUsable + "\nFree :　" + systemFree );
        tv_system.setText(text);

        //
        /**
         * 获取第二个内存卡，如果没有第二个，则返回第一个。
         * 第一个
         * I/ZAO: 路径：/storage/emulated/0
           内存大小Byte：1.50 GB
         */
        path = SDcardUtil.getSecondaryStoragePath(this);
        String pathSize = ZaoUtils.getPathSize(this,path);
        Log.i(ConstantValue.TAG,"路径：" + path);
        Log.i(ConstantValue.TAG,"内存大小Byte：" + pathSize);
    }
}
