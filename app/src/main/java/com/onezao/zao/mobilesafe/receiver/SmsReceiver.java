package com.onezao.zao.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;

public class SmsReceiver extends BroadcastReceiver {

    /**
     * 设备管理员
     */
    private DevicePolicyManager mDPM;
    /**
     * 四大组件名的封装类
     */
    private ComponentName mConmp;

    @Override
    public void onReceive(Context context, Intent intent) {


        // 1, 获取设备管理员
        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 2, 申请权限
        mConmp = new ComponentName(context, DeviceAdmin.class);


        //1.判断是否开启了防盗保护
        boolean open_security = SpUtils.getBoolean(context, ConstantValue.UPDATE_FILE, ConstantValue.OPEN_SECURITY, false);
        if (open_security) {
            Log.i(ConstantValue.TAG, "广播，短信收到了！");
            //2. 获取短信内容
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //3. 循环遍历短信过程
            for (Object object : objects) {
                //4. 获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                //5. 获取短信对象的基本信息
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                //6. 判断是否包含播放音乐的关键字
                if (messageBody.contains("本月")) {
                    //7.播放音乐（准备音乐，播放音乐）
//                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.chysx);
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.jcldxt);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } else if (messageBody.contains("*#location#*")) {

                    //7.播放音乐（准备音乐，播放音乐）
//                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.chysx);
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.jcldxt);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                } else if (messageBody.contains("*#lockscreen#*")) {

                    LockScreenAndNo(context);

                } else if (messageBody.contains("*#wipedata#*")) {

                    wipeDeviceData(context);

                }
            }
        }
    }

    //开启管理员权限
    private void openDeviceAdmin(Context context) {
        // 没有管理员权限---启动系统activity让用户激活管理员权限
        Intent intent = new Intent(
                DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mConmp);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "只有激活了管理员权限才能锁屏,清理缓存");
//            startActivityForResult(intent, 0);
        context.startActivity(intent);
    }

    //清楚手机数据
    private void wipeDeviceData(Context context) {
        // 判断是否获得管理员权限啊
        boolean active = mDPM.isAdminActive(mConmp);
        if (active) {
            mDPM.wipeData(0);
            mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        } else {
            //开启
            openDeviceAdmin(context);
        }
    }

    //开启设备管理员权限。一键锁屏
    private void LockScreenAndNo(Context context) {
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
            openDeviceAdmin(context);
        }
    }
}
