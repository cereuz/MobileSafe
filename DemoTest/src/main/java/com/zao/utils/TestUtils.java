package com.zao.utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class TestUtils {

    public static boolean isExist = true;
    public static AndroidDriver driver;
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
    public static AndroidDriver initDevice(String deviceName, String automationName, String platformName, String platformVersion, String appPackage, String appActivity, boolean noReset,String urlPort) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);
//        capabilities.setCapability("platformVersion", "5.1");
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("unicodeKeyboard", "True"); //Appium版本1.3.3以上，解决无法输入中文问题  使用unicode编码
        capabilities.setCapability("resetKeyboard", "True"); //在关闭后重置设备的默认输入法
        capabilities.setCapability("noReset", noReset); //不需要再次安装

        Log.debug("startAPPunLogin");

//        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL(urlPort), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.info("init and connect device , start APP. SUCCESS");

        TestUtils.testSleep(ConstantValue.TEN_SECOND);
        TestUtils.checkPermission(driver);
        Log.info("check Permissions And Allow it , SUCCESS");
        return driver;
    }

    /**
     * 测试间隔的时候睡眠时间。单位：毫秒。
     * @param time
     */
    public static void testSleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上滑
     *
     * @param driver
     * @param during
     * @param num
     */
    public static void swipeToUp(AppiumDriver<WebElement> driver,int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        for (int i = 0; i < num; i++) {
            driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, during);
            testSleep(3 * 1000);
        }
    }

    /**
     * 下拉
     *
     * @param driver
     * @param during
     * @param num
     */
    public static void swipeToDown(AppiumDriver<WebElement> driver,int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        System.out.println(width);
        System.out.println(height);
        for (int i = 0; i < num; i++) {
            driver.swipe(width / 2, height / 4, width / 2, height * 3 / 4, during);
            testSleep(3 * 1000);
        }
    }

    /**
     * 向左滑动
     *
     * @param driver
     * @param during
     * @param num
     */
    public static void swipeToLeft(AppiumDriver<WebElement> driver,int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        for (int i = 0; i < num; i++) {
            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, during);
            testSleep(3 * 1000);
        }
    }

    /**
     * 向右滑动
     *
     * @param driver
     * @param during
     * @param num
     */
    public static void swipeToRight(AppiumDriver<WebElement> driver, int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        for (int i = 0; i < num; i++) {
            driver.swipe(width / 4, height / 2, width * 3 / 4, height / 2, during);
            testSleep(3 * 1000);
        }
    }


    /**
     * 检测获取系统权限的弹框，并点击确认
     * @param driver
     */
    public static void checkPermission(AndroidDriver driver) {
        try{
            while (isExist){
                WebElement elementPermission = driver.findElementById("com.android.packageinstaller:id/permission_allow_button");
                if (elementPermission.isDisplayed()){
                    elementPermission.click();
                    TestUtils.testSleep(ConstantValue.TWO_SECOND);
                } else {
                    isExist = false;
                }
            }
        } catch (Exception e){
        }
    }


    /**
     * 执行adb命令
     * @param s 要执行的命令
     */
    public static void excuteAdbShell(String s) {
        Runtime runtime=Runtime.getRuntime();
        try{
            runtime.exec(s);
        }catch(Exception e){
            Log.debug("执行命令:"+s+"出错");
        }
    }
}
