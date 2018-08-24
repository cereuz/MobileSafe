package com.onezao.zao.mobilesafe.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(ConstantValue.TAG,"重启手机，已成功收到重启广播！！！");
        //1.获取手机开机后手机的sim卡序列号
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"HardwareIds", "MissingPermission"})
        String simSerialNumber = telephonyManager.getSimSerialNumber() + "06"; //设置序列号不相同的情况。
        //2.SP中存储的序列卡号
        String sim_number = SpUtils.getString(context,ConstantValue.UPDATE_FILE,ConstantValue.SIM_NUMBER,"");
        //3.比对不一致
        if(!simSerialNumber.equals(sim_number)){
           //4.发送短信给选择联系人号码
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("13282380039",null,"sim changed !!!",null,null);
        }
    }
}
