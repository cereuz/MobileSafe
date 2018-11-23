package com.zao;

import com.zao.utils.ConstantValue;
import com.zao.utils.Log;
import com.zao.utils.TestUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;

public class TestYLL {
    public static String  apkPath = "http://app-global.pgyer.com/7874cd6757061e3a9602d5a3f277397d.apk?attname=app-debug.apk&sign=71d52cc0d8175069a0916c1f5cc4f1cb&t=5bac5fa8\n";

    @Test
    public static void testYLL(){
        /**
         * 未登录状态下启动APP
         */
        startAPPunLogin();
    }

    /**
     * 安装APP
     */
    public static void installAPP(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "meizu-pro-6s");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "7.1.1");
        capabilities.setCapability("app", apkPath); //不需要再次安装

        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        TestUtils.testSleep(ConstantValue.SIX_SECOND);
        TestUtils.checkPermission(driver);

    }

    /**
     * 未登录状态下
     */
    @Test
    public static void startAPPunLogin(){

        AndroidDriver driver = initDevice(YLLConstantValue.DEVICE_NAME_MEIZU,YLLConstantValue.AUTOMATION_NAME,
                                          YLLConstantValue.PLATFORM_NAME_ANDROID, YLLConstantValue.PLATFORM_VERSION,
                                          YLLConstantValue.APP_PACKAGE_YLL, YLLConstantValue.APP_ACTIVITY_YLL,
                                          YLLConstantValue.NO_RESET_TRUE);

        TestUtils.swipeToDown(driver, 3 * 1000, 1);
        TestUtils.testSleep(ConstantValue.TWO_SECOND);

        driver.findElement(By.id(YLLConstantValue.home_iv_ticket)).click();
        TestUtils.testSleep(ConstantValue.TWO_SECOND);

        for(int i = 0 ; i < 3; i++) {
            TestUtils.swipeToUp(driver, 3 * 1000, 3);
            TestUtils.swipeToDown(driver, 3 * 1000, 3);
        }
        TestUtils.testSleep(ConstantValue.TWO_SECOND);

        /**
         * 点击返回按钮，进入主页。
         */
        driver.findElement(By.id(YLLConstantValue.id_index_iv_back)).click();
        TestUtils.testSleep(ConstantValue.TWO_SECOND);

        /**
         * 点击底部的几个按钮
         */
        for(int i = 0; i< 3; i++) {
            driver.findElement(By.id(YLLConstantValue.id_index_homepage)).click();
            driver.findElement(By.id(YLLConstantValue.id_index_found)).click();
            /**
             * 因为是未登录状态，这里跳转到登录界面，所以需要点击右上角按钮，返回一下。
             */
            driver.findElement(By.id(YLLConstantValue.id_index_local)).click();
            driver.findElement(By.id(YLLConstantValue.id_index_local_rightImg)).click();
            TestUtils.testSleep(ConstantValue.TWO_SECOND);

            driver.findElement(By.id(YLLConstantValue.id_index_profit)).click();
        }
        //退出应用
//        driver.quit();
    }

    /**
     * 初始化驱动连接设备，启动APP， 检查权限
     * @param deviceName
     * @param automationName
     * @param platformName
     * @param platformVersion
     * @param appPackage
     * @param appActivity
     * @param noReset
     * @return
     */
    private static AndroidDriver initDevice(String deviceName,String automationName,String platformName,String platformVersion,String appPackage,String appActivity,boolean noReset) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);
//        capabilities.setCapability("platformVersion", "5.1");
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("noReset", noReset); //不需要再次安装

        Log.debug("startAPPunLogin");

        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.info("初始化驱动连接设备，启动APP， 成功");

        TestUtils.testSleep(ConstantValue.TEN_SECOND);
        TestUtils.checkPermission(driver);
        Log.info("检查APP权限并同意， 成功");
        return driver;
    }
}
