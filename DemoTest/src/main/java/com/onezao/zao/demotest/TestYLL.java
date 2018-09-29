package com.onezao.zao.demotest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;

public class TestYLL {
    public static boolean isExist = true;
    public static String  apkPath = "http://app-global.pgyer.com/7874cd6757061e3a9602d5a3f277397d.apk?attname=app-debug.apk&sign=71d52cc0d8175069a0916c1f5cc4f1cb&t=5bac5fa8\n";

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
        checkPermission(driver);

    }

    /**
     * 未登录状态下
     */
    public static void startAPPunLogin(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "meizu-pro-6s");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("platformName", "Android");
//        capabilities.setCapability("platformVersion", "7.1.1");
        capabilities.setCapability("platformVersion", "5.1");
        capabilities.setCapability("appPackage", "com.zjjyy.yilvyou");
        capabilities.setCapability("appActivity", "com.zjjyy.yilvyou.mylibrary.activity.WelcomeActivity");
        capabilities.setCapability("noReset", true); //不需要再次安装

        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        TestUtils.testSleep(ConstantValue.TEN_SECOND);
        checkPermission(driver);
        TestUtils.swipeToDown(driver, 3 * 1000, 1);
        TestUtils.testSleep(ConstantValue.TWO_SECOND);

        driver.findElement(By.id("com.zjjyy.yilvyou:id/iv_ticket")).click();
        TestUtils.testSleep(ConstantValue.TWO_SECOND);

       for(int i = 0 ; i < 3; i++) {
           TestUtils.swipeToUp(driver, 3 * 1000, 1);
           TestUtils.swipeToDown(driver, 3 * 1000, 1);
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
     * 检测获取系统权限的弹框，并点击确认
     * @param driver
     */
    private static void checkPermission(AndroidDriver driver) {
        try{
            while (isExist){
                WebElement elementPermission = driver.findElementById("com.android.packageinstaller:id/permission_allow_button");
                if (elementPermission.isDisplayed()){
                    elementPermission.click();
                    TestUtils.testSleep(ConstantValue.TWO_SECOND);
                } else {
                    isExist = false;
                    System.out.println("已经没有弹框了" + ZaoUtils.getSystemTimeMore(2));
                }
            }
        } catch (Exception e){
        }
    }
}
