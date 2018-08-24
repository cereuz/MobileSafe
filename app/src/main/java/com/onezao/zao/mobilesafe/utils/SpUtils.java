package com.onezao.zao.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private static SharedPreferences sp;

    /**
     * 写  boolean 值 进入 SharedPreferences
     * @param context  上下文环境
     * @param file    存储在手机本地的文件名称
     * @param key    存储节点名称
     * @param value  存储节点的值  boolean
     */
    public static void putBoolean(Context context,String file,String key,boolean value){
        //（存储节点文件名称，读写方式）
        if(sp == null){
            sp = context.getSharedPreferences(file,Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).apply();
//        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 读  从 SharedPreferences 中读取 boolean 值
     * @param context  上下文环境
     * @param file  读取的本地文件名称
     * @param key   存储节点名称
     * @param defValue  存储节点的值， 如果没有则取默认值
     * @return  默认值或者此节点读取到的结果
     */
    //
    public static boolean getBoolean(Context context,String file,String key,boolean defValue){
        //（存储节点文件名称，读写方式）
        if(sp == null){
            sp = context.getSharedPreferences(file,Context.MODE_PRIVATE);
        }
       return sp.getBoolean(key,defValue);
//        sp.edit().putBoolean(key,value).commit();
    }


    /**
     * 写  String 值 进入 SharedPreferences
     * @param context  上下文环境
     * @param file    存储在手机本地的文件名称
     * @param key    存储节点名称
     * @param value  存储节点的值  boolean
     */
    public static void putString(Context context,String file,String key,String value){
        //（存储节点文件名称，读写方式）
        if(sp == null){
            sp = context.getSharedPreferences(file,Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).apply();
//        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 读  从 SharedPreferences 中读取 String 值
     * @param context  上下文环境
     * @param file  读取的本地文件名称
     * @param key   存储节点名称
     * @param defValue  存储节点的值， 如果没有则取默认值
     * @return  默认值或者此节点读取到的结果
     */
    //
    public static String getString(Context context,String file,String key,String defValue){
        //（存储节点文件名称，读写方式）
        if(sp == null){
            sp = context.getSharedPreferences(file,Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
//        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 删除节点的方法
      * @param context
     * @param file
     * @param key
     */
    public static void remove(Context context, String file, String key) {
        //（存储节点文件名称，读写方式）
        if(sp == null){
            sp = context.getSharedPreferences(file,Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).apply();
    }
}
