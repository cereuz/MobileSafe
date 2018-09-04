package com.onezao.zao.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {
    /**
     * @param context 上下文对象
     * @param serviceName  判断是否运行的服务
     * @return  true 运行，false 没有运行
     */
    public static boolean isRunning(Context context,String serviceName){
       //1.获取ActivityManager管理者对象，可以去获取当前手机正在运行的所有服务
        ActivityManager mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       //2.获取手机正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = mAM.getRunningServices(200);
        if (runningServiceInfoList.size() <= 0) {
            return false;
        }
        for(ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfoList){
            //4.获取每一个正在运行服务的名称
            if(serviceName.equals(runningServiceInfo.service.getClassName())){
                 return  true;
            }
        }
        return false;
    }
}
