package com.onezao.zao.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onezao.zao.mobilesafe.db.AppLockOpenHelper;
import com.onezao.zao.mobilesafe.db.domain.AppLockInfo;
import com.onezao.zao.mobilesafe.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

public class AppLockDao {

       AppLockOpenHelper appLockOpenHelper;
       SQLiteDatabase db;

    //AppLockDao 单例模式
    //1.私有化构造方法
    private AppLockDao(Context context){
        //创建数据库以及其表结构
        appLockOpenHelper = new AppLockOpenHelper(context, ConstantValue.DADABASE_APPLOCK,1);
    };
    //2.声明一个当前类的对象
    private static AppLockDao appLockDao = null;
    //3.提供一个方法，如果当前类的对象为空，创建一个新的
    public static AppLockDao getInstance(Context context){
        if(appLockDao == null){
            appLockDao = new AppLockDao(context);
        }
        return appLockDao;
    }


    /**
     * 数据库的插入操作
     * @param packagename
     * @param time
     */
    public void insert(String packagename,String time){
        db = appLockOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("packagename",packagename);
        contentValues.put("time",time);

        db.insert(ConstantValue.DATABASE_APPLOCK_TABLE_NAME,null,contentValues);
        db.close();
    }

    /**
     * 删除数据库数据
     * @param packagename
     */
    public void delete(String packagename){
        db = appLockOpenHelper.getWritableDatabase();

        db.delete(ConstantValue.DATABASE_APPLOCK_TABLE_NAME,"packagename = ?",new String[]{packagename});
        db.close();
    }

    /**
     * 查询所有存储到数据库中的数据，并存储到一个集合上
     * @return
     */
    public List<AppLockInfo> findAll(){
        db = appLockOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(ConstantValue.DATABASE_APPLOCK_TABLE_NAME,new String[]{"packagename","time"},null,null,null,null,null);
        List<AppLockInfo>  appLockInfoList = new ArrayList<AppLockInfo>();
        while (cursor.moveToNext()){
            AppLockInfo appLockInfo = new AppLockInfo();
            appLockInfo.setPackagename(cursor.getString(0));
            appLockInfo.setTime(cursor.getString(1));
            appLockInfoList.add(appLockInfo);
        }
        cursor.close();
        db.close();
        return appLockInfoList;
    }

    /**
     * 查询所有存储到数据库中的数据，并存储到一个集合上
     * @return
     */
    public List<String> findAll(String packageName){
        db = appLockOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(ConstantValue.DATABASE_APPLOCK_TABLE_NAME,new String[]{packageName},null,null,null,null,null);
        List<String>  appLockInfoList = new ArrayList<String>();
        while (cursor.moveToNext()){
            appLockInfoList.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return appLockInfoList;
    }
}
