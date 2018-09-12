package com.onezao.zao.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onezao.zao.mobilesafe.utils.ConstantValue;

public class AppLockOpenHelper extends SQLiteOpenHelper {
    public AppLockOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库中表的方法
        sqLiteDatabase.execSQL(ConstantValue.DATABASE_CREATE_TABLE_APPLOCK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
