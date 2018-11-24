package com.zao.yll;

import com.zao.utils.ConstantValue;
import com.zao.utils.Log;
import com.zao.utils.TestUtils;

import org.openqa.selenium.By;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class YllHome {

    public static String[] search_text = {"温州","wuxue","武汉","南京","wuxi","上海","杭州"};

    /**
     * 启动APP ， 初始化 driver对象
     */
    static AndroidDriver driver = TestUtils.initDevice(YLLConstantValue.DEVICE_NAME_MEIZU,YLLConstantValue.AUTOMATION_NAME,
            YLLConstantValue.PLATFORM_NAME_ANDROID, YLLConstantValue.PLATFORM_VERSION_711,
            YLLConstantValue.APP_PACKAGE_YLL, YLLConstantValue.APP_ACTIVITY_YLL,
            YLLConstantValue.NO_RESET_TRUE,YLLConstantValue.URL_PORT_4723);

    @Test
    public static void initSearch(){
        TestUtils.swipeToDown(driver, 3 * 1000, 1);
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        for(int i = 0; i < 1 ; i++) {
            driver.findElement(By.id(YLLConstantValue.home_tv_search)).click();
            driver.findElement(By.id(YLLConstantValue.id_back)).click();
        }
        driver.findElement(By.id(YLLConstantValue.home_tv_search)).click();
        TestUtils.testSleep(ConstantValue.ONE_SECOND);
        /**
         * 在首页的输入框，输入内容，搜索，然后返回
         */
        for (int i = 0; i < search_text.length; i++ ){
            inputMsg(search_text[i]);
        }

        /**
         * 点击“清除历史记录”
         */
        driver.findElement(By.id(YLLConstantValue.search_tv_clear)).click();
    }

    @AfterTest
    public static void after(){
/*        driver.removeApp("io.appium.android.ime");
        Log.debug("REMOVE  io.appium.android.ime");*/
    }

    /**
     * 在首页的输入框，输入内容，搜索，然后返回
     */
    private static void inputMsg(String message) {
        TestUtils.testSleep(ConstantValue.ONE_SECOND);
        driver.findElement(By.id(YLLConstantValue.search_search_content)).click();
        TestUtils.testSleep(ConstantValue.ONE_SECOND);
        driver.findElement(By.id(YLLConstantValue.search_search_content)).sendKeys(message);
        Log.debug(message);
        TestUtils.excuteAdbShell("adb shell ime set com.android.inputmethod.pinyin/.PinyinIME");
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        //点击右下角的搜索，即ENTER键  ===输入拼音的时候，输入法会先确认内容，再进行搜索！！==
        driver.pressKeyCode(AndroidKeyCode.ENTER);
        driver.pressKeyCode(AndroidKeyCode.ENTER);
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        TestUtils.excuteAdbShell("adb shell ime set io.appium.android.ime/.UnicodeIME");
        driver.findElement(By.id(YLLConstantValue.id_back)).click();
    }
}
