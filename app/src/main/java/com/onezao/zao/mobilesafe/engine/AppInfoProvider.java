package com.onezao.zao.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.onezao.zao.mobilesafe.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppInfoProvider {
    /**
     * 获取安装在手机上应用相关信息
     * @param context
     * @return
     */
    public static List<AppInfo> getAppInfoList(Context context){
        //1.获取包管理者对象
        //2.固定做法：获取安装在手机上的所有应用信息，则传递参数 0
        //3.循环遍历上述集合，获取每一个安装在手机上的应用相关信息（包名，名称，路径，系统，图标）
        //4.获取包名
        //5.获取应用名称，图标 ： 这些是存储在manifest.xml文件的application节点上的
        //6.判断是否为系统应用，（系统应用，用户应用）
        //7.判断是否安装在SD卡上，（安装在磁盘上，SD卡上）
        List<AppInfo>  listApp = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo: installedPackages) {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = packageInfo.packageName;
            appInfo.name = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.icon = packageInfo.applicationInfo.loadIcon(pm);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
                //系统应用
                appInfo.isSystem = true;
            }  else  {
                appInfo.isSystem = false;
            }
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                //系统应用
                appInfo.isSdcard = true;
            }  else  {
                appInfo.isSdcard = false;
            }
            listApp.add(appInfo);
        }
        return listApp;
    }
}
