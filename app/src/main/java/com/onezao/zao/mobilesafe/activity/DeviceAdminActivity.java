package com.onezao.zao.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.receiver.DeviceAdmin;

public class DeviceAdminActivity extends AppCompatActivity implements View.OnClickListener{

    Button device_btn_active;
    Button device_btn_lock;
    Button device_btn_wipedata;
    Button device_btn_uninstall;

    /**
     * 设备管理员
     */
    private DevicePolicyManager mDPM;
    /**
     * 四大组件名的封装类
     */
    private ComponentName mConmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_admin);

        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        device_btn_active = (Button)findViewById(R.id.device_btn_active);
        device_btn_active.setOnClickListener(this);
        device_btn_lock = (Button)findViewById(R.id.device_btn_lock);
        device_btn_lock.setOnClickListener(this);
        device_btn_wipedata = (Button)findViewById(R.id.device_btn_wipedata);
        device_btn_wipedata.setOnClickListener(this);
        device_btn_uninstall = (Button)findViewById(R.id.device_btn_uninstall);
        device_btn_uninstall.setOnClickListener(this);

        // 1, 获取设备管理员
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 2, 申请权限
        mConmp = new ComponentName(this, DeviceAdmin.class);
        //根据是否获取权限，初始化按钮的显示内容
        initText();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case  R.id.device_btn_active :
                clearAndOpenDeviceAdmin();
            break;

            case  R.id.device_btn_lock :
                LockScreenAndNo();
            break;

            case  R.id.device_btn_wipedata :
                wipeDeviceData();
            break;

            case  R.id.device_btn_uninstall :
                unInstallAPK();
            break;
        }
    }

    /**
     * 卸载设备的应用
     */
    private void unInstallAPK() {
        // 判断是否获得管理员权限啊
        boolean active = mDPM.isAdminActive(mConmp);
        if (active){
            //取消激活
            clearDeviceAdmin();
            device_btn_active.setText("一键激活");
        }  else  {
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);

            device_btn_active.setText("一键激活");
        }


    }

    //清楚手机数据
    private void wipeDeviceData() {
        // 判断是否获得管理员权限啊
        boolean active = mDPM.isAdminActive(mConmp);
        if (active){
            mDPM.wipeData(0);
            mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
            device_btn_active.setText("一键取消激活");
        }  else  {
            //开启
            openDeviceAdmin();
            device_btn_active.setText("一键激活");
        }
    }

    //取消设备管理员权限
    private void clearAndOpenDeviceAdmin() {
        initTextAndAdmin();
    }

    /**
     *  根据是否获取权限，初始化按钮的显示内容
     */
    private void initTextAndAdmin() {
        // 判断是否获得管理员权限啊
        boolean active = mDPM.isAdminActive(mConmp);
        if (active){
            clearDeviceAdmin();
            device_btn_active.setText("一键取消激活");
        }  else  {
            //开启
            openDeviceAdmin();
            device_btn_active.setText("一键激活");
        }
    }

    /**
     *  根据是否获取权限，初始化按钮的显示内容
     */
    private void initText() {
        // 判断是否获得管理员权限啊
        boolean active = mDPM.isAdminActive(mConmp);
        if (active){
            device_btn_active.setText("一键取消激活");
        }  else  {
            //开启
            device_btn_active.setText("一键激活");
        }
    }


    //开启设备管理员权限。一键锁屏
    private void LockScreenAndNo() {
        // 判断是否获得管理员权限啊
        boolean active = mDPM.isAdminActive(mConmp);

        if (active) {
            // 已经获取管理员权限可以锁屏
            System.out.println("--已经获取管理员权限--");
            mDPM.lockNow();
            // 解锁时要输入123才能解锁
            mDPM.resetPassword("123456", 0);
        } else {
            //开启
            openDeviceAdmin();
        }

  }

        //1.清除管理员权限
    private void clearDeviceAdmin() {
        DevicePolicyManager dc;//设备管理
        dc= (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName mdeviceAdminSample=new ComponentName(this,DeviceAdmin.class);
        dc.removeActiveAdmin(mdeviceAdminSample);
    }

    //开启管理员权限
    private void openDeviceAdmin() {
        // 没有管理员权限---启动系统activity让用户激活管理员权限
        Intent intent = new Intent(
                DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mConmp);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "只有激活了管理员权限才能锁屏,清理缓存");
//            startActivityForResult(intent, 0);
        startActivity(intent);
    }
}
