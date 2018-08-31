package com.onezao.zao.demotest;


import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;


public class AutoTestAppium {

    public static void main(String[] args){

        //  CalculatorTest();
        // 	KedouNew();
        MiCalculatorTest();
    }

    private static void KedouNew() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("appPackage", "com.gcs.mcj.chat");
        capabilities.setCapability("appActivity", "com.gcs.mcj.chat.activity.MainActivity");

        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        driver.findElement(By.id("com.gcs.mcj.chat:id/tab_4")).click();
//        driver.findElement(By.name("5")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        String result = driver.findElement(By.id("com.android.calculator2:id/formula")).getText();
        String result = driver.findElement(By.id("com.gcs.mcj.chat:id/uid")).getText();
        System.out.println(result);

        driver.quit();
    }

    private static void CalculatorTest() throws MalformedURLException,InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "5.1");  //5.1
        capabilities.setCapability("appPackage", "com.android.calculator2");
        capabilities.setCapability("appActivity", ".Calculator");
        capabilities.setCapability("noReset", true);

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        driver.findElement(By.name("1")).click();
        driver.findElement(By.name("5")).click();
        driver.findElement(By.name("9")).click();
        driver.findElement(By.id("com.android.calculator2:id/del")).click();
        driver.findElement(By.name("2")).click();
        driver.findElement(By.id("com.android.calculator2:id/plus")).click();
        driver.findElement(By.name("6")).click();
        driver.findElement(By.id("com.android.calculator2:id/equal")).click();
        Thread.sleep(2000);

//        String result = driver.findElement(By.id("com.android.calculator2:id/formula")).getText();
        String result = driver.findElement(By.className("android.widget.EditText")).getText();
        System.out.println(result);

        driver.closeApp();

        driver.removeApp("io.appium.unlock");
        driver.removeApp("io.appium.settings");

        driver.quit();
    }

    private static void MiCalculatorTest(){
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", "Android Emulator");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", "7.1.1");
            capabilities.setCapability("appPackage", "com.miui.calculator");
            capabilities.setCapability("appActivity", ".cal.CalculatorActivity");
            capabilities.setCapability("noReset", true);

            AndroidDriver driver = null;
            try {
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            driver.findElement(By.name("1")).click();
            driver.findElement(By.name("5")).click();
            driver.findElement(By.name("9")).click();
            driver.findElement(By.id("com.miui.calculator:id/btn_del_s")).click();
            driver.findElement(By.name("2")).click();
            driver.findElement(By.id("com.miui.calculator:id/btn_plus_s")).click();
            driver.findElement(By.name("6")).click();
            driver.findElement(By.id("com.miui.calculator:id/btn_equal_s")).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//        String result = driver.findElement(By.id("com.android.calculator2:id/formula")).getText();
            String result = driver.findElement(By.className("android.widget.TextView")).getText();
            System.out.println(result);

            driver.closeApp();

            driver.removeApp("io.appium.unlock");
            driver.removeApp("io.appium.settings");

            driver.quit();
        }
    }


}