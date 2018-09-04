package com.onezao.zao.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.service.AddressService;
import com.onezao.zao.mobilesafe.service.BlackNumberService;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ServiceUtil;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ToastUtil;
import com.onezao.zao.mobilesafe.view.SettingClickView;
import com.onezao.zao.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "ZAO";
    String[] mToastStyleDes;
    int des_num;
    SettingClickView scv_toast_style;
    SettingClickView scv_toast_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Activity间传递数据
        initIntentData();
        //初始化自动更新界面
        initUpdate();
        //初始化电话归属地功能
        initAddress();
        //初始化权限, 6.0以上申请系统权限，需要用户手动给出
        initPermission();
        //初始化自定义Toast的样式
        initToastStyle();
        //初始化自定义Toast的位置
        initToastLocation();
        //初始化黑名单服务的开启和关闭
        initBlackNumber();
    }

    /**
     * 初始化黑名单服务的开启和关闭
     */
    private void initBlackNumber() {
            final SettingItemView siv_blacknumber = (SettingItemView)findViewById(R.id.siv_blacknumber);
            //对服务是否开启的状态做显示,绑定服务是否开启状态并对应显示
            boolean isRunning = ServiceUtil.isRunning(this,ConstantValue.BLACKNUMBER_SERVICE_CLASS);
            siv_blacknumber.setCheck(isRunning);
            //点击过程中，状态（是否开启显示电话归属地）的切换过程
            siv_blacknumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //返回点击前的状态
                    boolean isCheck = siv_blacknumber.isCheck();
                    siv_blacknumber.setCheck(!isCheck);
                    if(!isCheck){
                        //如果设置之后，是选中的状态，则开启服务
                        startService(new Intent(getApplicationContext(),BlackNumberService.class));
                    } else {
                        //如果设置之后，是取消的状态，则停止服务
                        stopService(new Intent(getApplicationContext(),BlackNumberService.class));
                    }
                }
            });
        }


    /**
     * 初始化自定义的Toast的显示位置
     */
    private void initToastLocation() {
        scv_toast_location = (SettingClickView)findViewById(R.id.scv_toast_location);
        scv_toast_location.setTitle("归属地提示框的位置");
        scv_toast_location.setDes("设置归属地提示框的位置");
        scv_toast_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
            }
        });
    }

    /**
     * 初始化Toast的样式
     */
    private void initToastStyle() {
        scv_toast_style = (SettingClickView)findViewById(R.id.scv_toast_style);
        scv_toast_style.setTitle("设置电话归属地显示样式");
        //1. 创建文描述所在的String数组
        mToastStyleDes = new String[]{"透明","橙色","蓝色","灰色","绿色","红色"};
        //2. SP 获取吐司样式的索引值(int)，用于获取描述文字
        des_num = SpUtils.getInt(this, ConstantValue.UPDATE_FILE,ConstantValue.TOAST_STYLE,2);
        //3.通过索引，获取字符串数组中的字，显示给描述内容控件。
        scv_toast_style.setDes(mToastStyleDes[des_num]);
        //4.监听点击事件，弹出对话框
        scv_toast_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //5.显示吐司样式的对话框
                showToastStyleDialog();
            }
        });
    }

    /**
     * 弹出对话框，选择吐司的显示样式
     */
    private void showToastStyleDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setIcon(R.mipmap.ic_launcher);
      builder.setTitle("选择吐司的样式");
      //选择单个条目事件监听
        /**
         * 1：String类型的数字描述文字数组，
         * 2：弹出对话框的时候选中条目索引值，
         * 3：点击某一个条目后触发的点击事件）
         */
        //2. SP 获取吐司样式的索引值(int)，用于获取描述文字 ，因为是在当前页面的操作弹出对话框，SP存储值可能会更改，为了保证数据是最新数据，重新获取一下。
        des_num = SpUtils.getInt(this, ConstantValue.UPDATE_FILE,ConstantValue.TOAST_STYLE,2);
      builder.setSingleChoiceItems(mToastStyleDes, des_num, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {  // i  是选中的索引值
              //1.记录选中的索引值，2.关闭对话框，3.显示选择数字对应的文字
              SpUtils.putInt(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.TOAST_STYLE,i);
              dialogInterface.dismiss();
              scv_toast_style.setDes(mToastStyleDes[i]);
          }
      });
      //消极按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
            }
        });
        //最后一步，一定要记得：显示出来！！！
        builder.show();
    }

    /**
     * 初始化权限，M之后，系统权限需要弹出让用户主动点击同意
     */
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (! Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }
    }

    /**
     * 处理权限申请之后的功能
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(this,"需要允许自定义Toast需要的权限！！", Toast.LENGTH_SHORT);
                    initPermission();
                }
            }
        }
    }

    /**
     * 初始化电话归属地功能
     */
    private void initAddress() {
        final SettingItemView siv_address = (SettingItemView)findViewById(R.id.siv_address);
        //对服务是否开启的状态做显示,绑定服务是否开启状态并对应显示
        boolean isRunning = ServiceUtil.isRunning(this,ConstantValue.ADDRESS_SERVICE_CLASS);
        siv_address.setCheck(isRunning);
        //点击过程中，状态（是否开启显示电话归属地）的切换过程
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回点击前的状态
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
                if(!isCheck){
                //如果设置之后，是选中的状态，则开启服务
                    startService(new Intent(getApplicationContext(),AddressService.class));
                } else {
                    //如果设置之后，是取消的状态，则停止服务
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
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
