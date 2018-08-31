package com.onezao.zao.mobilesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.onezao.zao.mobilesafe.db.dao.BlackNumberDao;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test
    public void TestInsert(){
        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        String time = ZaoUtils.getSystemTimeMore(1);
        for(int i = 0 ; i < 100 ; i++){
            if(i < 10){
               dao.insert("1328238000" + i, String.valueOf(new Random().nextInt(3) + 1),time);
               ZaoUtils.sleep(100);
            }  else {
               dao.insert("132823800" + i, String.valueOf(new Random().nextInt(3) + 1),time);
               ZaoUtils.sleep(100);
            }
        }
//        dao.insert("13636120000","2","sunedo@qq.com");
        //直接复制到SD卡
        TestCopy();
    }

    @Test
    public void TestCopy(){
        //数据库文件地址
        //1. 指定访问数据库的路径
//        String path = "data/data/com.onezao.zao.mobilesafe/files/address.db";

//        ZaoUtils.copyFile(ZaoUtils.pathSD + "/ame/mobilesafe.db",ZaoUtils.pathSD + "/ame/xopy.db");
        //黑名单的数据库
        String pathDB = appContext.getDatabasePath(ConstantValue.DADABASE_MOBILESAFE).getAbsolutePath();
        ZaoUtils.copyFile(pathDB,ZaoUtils.pathSD + "/ame/mobilesafe0831.db");
    }
}
