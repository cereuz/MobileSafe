package com.onezao.zao.mobilesafe.db.dao;

import android.content.Context;

import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

public class TestCopyFile {
    /**
     * 将数据库文件复制到内存卡
     */
    public void copyAtoB(Context context){
//        String pathDB = ZaoUtils.pathSD + "/ame/body.jpg";
//        String pathDB = context.getFilesDir().getAbsolutePath() + "/database/" +ConstantValue.DADABASE_MOBILESAFE;
        String pathDB = context.getDatabasePath(ConstantValue.DADABASE_MOBILESAFE).getAbsolutePath();
        ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame/mobilesafe0831.db");
    }

}
