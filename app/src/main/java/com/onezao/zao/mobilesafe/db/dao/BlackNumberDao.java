package com.onezao.zao.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onezao.zao.mobilesafe.db.BlackNumberSqliteOpenHelper;
import com.onezao.zao.mobilesafe.db.domain.BlackNumberInfo;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import java.util.ArrayList;
import java.util.List;

public class BlackNumberDao {

       BlackNumberSqliteOpenHelper bnsOpenHelper;
       SQLiteDatabase db;

    //BlackNumberDao 单例模式
    //1.私有化构造方法
    private BlackNumberDao(Context context){
        //创建数据库以及其表结构
        bnsOpenHelper = new BlackNumberSqliteOpenHelper(context, ConstantValue.DADABASE_MOBILESAFE,1);
    };
    //2.声明一个当前类的对象
    private static BlackNumberDao blackNumberDao = null;
    //3.提供一个方法，如果当前类的对象为空，创建一个新的
    public static BlackNumberDao getInstance(Context context){
        if(blackNumberDao == null){
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }

    /**
     * 添加一个条目
     * @param phone  拦截的电话号码
     * @param mode   拦截类型（1：短信   2：电话   3：拦截所有（短信+电话））
     * @param time   拦截的时间
     */
    public void insert(String phone,String mode,String time){
        //1.开启数据库，准备做写入操作
        db = bnsOpenHelper.getWritableDatabase();
        //2.插入数据
        ContentValues values = new ContentValues();
        values.put("bn_phone",phone);
        values.put("bn_mode",mode);
        values.put("bn_time",time);
        db.insert(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME,null,values);
        db.close();
    }

    /**
     * 从数据库中删除一条数据
     * @param phone  删除对应电话号码的数据
     */
    public void delete(String phone){
       db = bnsOpenHelper.getWritableDatabase();
       db.delete(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME,"bn_phone = ?",new String[]{phone});
       db.close();
    }

    /**

     */
    /**
     *
     * 根据电话号码去更新拦截模式
     * @param oldPhone  更新拦截模式的电话号码
     * @param newPhone  更新拦截模式的电话号码
     * @param mode   更新拦截模式（1：短信   2：电话   3：拦截所有（短信+电话））
     */
    public void update(String oldPhone,String newPhone,String mode,String time){
        db = bnsOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("bn_phone",newPhone);
        values.put("bn_mode",mode);
        values.put("bn_time",time);
        db.update(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME,values,"bn_phone = ? ",new String[]{oldPhone});

        db.close();
    }

    /**
     * ANDROID_HOME
     *  查询所有存储的数据，并返回
     * @return   返回list集合  List<BlackNumberInfo>
     */
    public List<BlackNumberInfo> findAll(){
        db = bnsOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME,new String[]{"bn_phone","bn_mode","bn_time"},null,null,null,null,"_bn_id desc");
            //创建存储查询到的数据的集合
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()){
            //创建每一条数据的对象
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setPhone(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setTime(cursor.getString(2));
            list.add(blackNumberInfo);
        }
             cursor.close();
             db.close();

            return list;
    }


    /**
     * ANDROID_HOME
     *  查询所有存储的数据，并返回
     * @return   返回list集合  List<BlackNumberInfo>
     */
    public List<BlackNumberInfo> find(int index){
        db = bnsOpenHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("select bn_phone,bn_mode,bn_time from blacknumber order by _bn_id desc limit ?,20",new String[]{index + ""});
        Cursor cursor= db.query("blacknumber", null, null,
                null,null, null, null, "10,20");
//        Cursor cursor = db.query(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME,new String[]{"bn_phone","bn_mode","bn_time"},null,null,null,null,"_bn_id desc");
        //创建存储查询到的数据的集合
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()){
            //创建每一条数据的对象
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setPhone(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setTime(cursor.getString(2));
            list.add(blackNumberInfo);
        }
        cursor.close();
        db.close();

        return list;
    }
}
