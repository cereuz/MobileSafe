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
/*//        Cursor cursor = db.rawQuery("select bn_phone,bn_mode,bn_time from blacknumber order by _bn_id desc limit ?,20",new String[]{index + ""});
        Cursor cursor= db.query("blacknumber", null, null,
                null,null, null, null, "10,20");
//        Cursor cursor = db.query(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME,new String[]{"bn_phone","bn_mode","bn_time"},null,null,null,null,"_bn_id desc");*/
        Cursor cursor = db.rawQuery("select bn_phone,bn_mode,bn_time from blacknumber order by _bn_id desc limit ?,20",new String[]{index + ""});
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
     * 分批查询数据，每次查询20个
     *
     * @param index
     *            查询的起始坐标
     * @return 返回保存全部黑名单信息的List
     */
    public List<BlackNumberInfo> queryLimit(int index) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 创建list保存所有黑名单信息
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        // 创建数据，每个方法单独创建数据库，以便每次用完数据库后关闭
        SQLiteDatabase db = bnsOpenHelper.getWritableDatabase();
        // 查询数据库
        Cursor query = db.rawQuery(
                "select bn_phone,bn_mode,bn_time from blacknumber order by _bn_id desc limit 20 offset ?",
                new String[] { index + "" });
        while (query.moveToNext()) {
            // 创建blacknumberinfo对象保存每个黑名单的信息
            BlackNumberInfo info = new BlackNumberInfo();
            // 保存号码
            info.setPhone(query.getString(0));
            // 保存拦截模式
            info.setMode(query.getString(1));
            // 保存保存时间
            info.setTime(query.getString(2));
            // 将单个黑名单信息保存进List集合
            list.add(info);
        }
        db.close();
        return list;
    }


    /**
     * 查询黑名单总数
     * @return 黑名单的总数
     */
    public int queryTotal() {
        // 创建数据，每个方法单独创建数据库，以便每次用完数据库后关闭
        SQLiteDatabase db = bnsOpenHelper.getWritableDatabase();
        // 查询数据库
        Cursor query = db.rawQuery("select count(*) from blacknumber", null);
        int count = 0;
        if (query.moveToNext()) {
            //得到黑名单总数
            count = query.getInt(0);
        }
        //关闭数据库
        db.close();
        return count;
    }


    /**
     * 查询该黑名单号码是否存在
     *
     * @param number
     *            黑马单号码
     * @return true为存在，false为不存在
     */
    public boolean queryNumber(String number) {
        // 创建数据，每个方法单独创建数据库，以便每次用完数据库后关闭
        SQLiteDatabase db = bnsOpenHelper.getWritableDatabase();
        // 查询数据库
        Cursor query = db.query(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME, new String[] { "bn_phone" }, "bn_phone=?",
                new String[] { number }, null, null, null);
        // 如果查询到就是存在
        boolean isExist = query.moveToNext();
        // 关闭数据库
        db.close();
        // 返回结果
        return isExist;
    }

    /**
     * 查询拦截模式
     *
     * @param number
     *            黑马单号码
     * @return 返回对应的拦截模式，Null为没有
     */
    public int queryMode(String number) {
        // 创建数据，每个方法单独创建数据库，以便每次用完数据库后关闭
        SQLiteDatabase db = bnsOpenHelper.getWritableDatabase();
        // 查询数据库
        Cursor query = db.query(ConstantValue.DATABASE_BLACKNUMBER_TABLE_NAME, new String[] { "bn_mode" }, "bn_phone=?",
                new String[] { number }, null, null, null);
        int mode = 0;
        if (query.moveToNext()) {
            // 得到拦截模式
            mode = query.getInt(0);
        }
        // 关闭数据库
        db.close();
        // 返回结果
        return mode;
    }
}
