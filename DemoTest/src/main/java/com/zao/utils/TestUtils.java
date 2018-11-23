package com.zao.utils;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class TestUtils {

    public static boolean isExist = true;

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

}
