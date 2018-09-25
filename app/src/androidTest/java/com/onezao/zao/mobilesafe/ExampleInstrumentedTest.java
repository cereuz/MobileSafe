package com.onezao.zao.mobilesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.onezao.zao.mobilesafe.db.dao.AppLockDao;
import com.onezao.zao.mobilesafe.db.dao.BlackNumberDao;
import com.onezao.zao.mobilesafe.db.domain.AppLockInfo;
import com.onezao.zao.mobilesafe.db.domain.BNAppInfo;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void useAppContext() {
        // Context of the app under test.

        assertEquals("com.onezao.zao.mobilesafe", appContext.getPackageName());

    }


    /**
     * AppLock的插入测试
     */
    @Test
    public void testAppLockInsert(){
        AppLockDao dao = AppLockDao.getInstance(appContext);
        for(int i = 0 ; i < 1000 ; i++){
            if(i < 10){
                dao.insert("1328238000" + i,ZaoUtils.getSystemTimeMore(1));
                ZaoUtils.sleep(20);
            }  else if(i < 100){
                dao.insert("132823800" + i,ZaoUtils.getSystemTimeMore(1));
                ZaoUtils.sleep(20);
            }  else if (i < 1000){
                dao.insert("13282380" + i,ZaoUtils.getSystemTimeMore(1));
                ZaoUtils.sleep(10);
            }
        }

        /**
         *  String pathDB = appContext.getDatabasePath(ConstantValue.DADABASE_APPLOCK).getAbsolutePath();
         *  ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame/msAppLock.db");
         *  ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame" + File.separator +"msAppLock" + ".db");
         */
        //直接复制到SD卡
        copyFile(ConstantValue.DADABASE_APPLOCK,"msAppLock");
    }

    public void copyFile(String from,String to){
        //直接复制到SD卡
        String pathDB = appContext.getDatabasePath(from).getAbsolutePath();
        ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame" + File.separator + to + ".db");
    }

    /**
     * AppLock的数据库数据的删除操作
     */
    @Test
    public void testAppLockDelete(){
        AppLockDao dao = AppLockDao.getInstance(appContext);
        dao.delete("13282380009");

        //直接复制到SD卡
        String pathDB = appContext.getDatabasePath(ConstantValue.DADABASE_APPLOCK).getAbsolutePath();
        ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame/msAppLock.db");
    }

    /**
     * AppLock的数据库数据的查询操作
     */
    @Test
    public void testAppLockFindAll(){
        AppLockDao dao = AppLockDao.getInstance(appContext);
        List<AppLockInfo>  list = dao.findAll();
        for(AppLockInfo info : list){
            Log.i("Zao","PackageName = " + info.getPackagename() + " time = " + info.getTime() );
        }
    }


    /**
     * AppLock的数据库数据的查询操作
     */
    @Test
    public void testAppLockFindAll2(){
        AppLockDao dao = AppLockDao.getInstance(appContext);
        List<String>  list = dao.findAll("packagename");
        for(String packageName : list){
            Log.i("Zao","PackageName = " + packageName);
        }
    }

    @Test
    public void testCopy(){
        //数据库文件地址
        //1. 指定访问数据库的路径
//        String path = "data/data/com.onezao.zao.mobilesafe/files/address.db";

//        ZaoUtils.copyFile(ZaoUtils.pathSD + "/ame/mobilesafe.db",ZaoUtils.pathSD + "/ame/xopy.db");
        //黑名单的数据库
        String pathDB = appContext.getDatabasePath(ConstantValue.DADABASE_BLACKNUMBER).getAbsolutePath();
        ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame/mobilesafe0831.db");
    }

    @Test
    public void testInsert(){
        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        for(int i = 0 ; i < 1000 ; i++){
            if(i < 10){
               dao.insert("1328238000" + i, String.valueOf(new Random().nextInt(3) + 1),ZaoUtils.getSystemTimeMore(1));
               ZaoUtils.sleep(20);
            }  else if(i < 100){
               dao.insert("132823800" + i, String.valueOf(new Random().nextInt(3) + 1),ZaoUtils.getSystemTimeMore(1));
               ZaoUtils.sleep(20);
            }  else if (i < 1000){
                dao.insert("13282380" + i, String.valueOf(new Random().nextInt(3) + 1),ZaoUtils.getSystemTimeMore(1));
                ZaoUtils.sleep(10);
            }
        }
//        dao.insert("13636120000","2","sunedo@qq.com");
        //直接复制到SD卡
        testCopy();
    }

    @Test
    public void testDelete(){
        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        dao.delete("13282380004");

        //直接复制到SD卡
        testCopy();
    }

    @Test
    public void TestUpdate(){
        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        dao.update("1688","12306",1 + "",ZaoUtils.getSystemTimeMore(1));

        //直接复制到SD卡
        testCopy();
    }

    @Test
    public void testFindAll(){
        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        List<BNAppInfo>  list = dao.findAll();
        for(BNAppInfo info : list){
           Log.i("Zao","Phone = " + info.getPhone() + "  mode = " + info.getMode() + " time = " + info.getTime() );
        }
    }

    @Test
    public void testFind(){
        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        List<BNAppInfo>  list = dao.find(10);
        for(BNAppInfo info : list){
            Log.i("Zao","Phone = " + info.getPhone() + "  mode = " + info.getMode() + " time = " + info.getTime() );
        }
    }
}
