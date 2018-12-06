package com.zao.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class AppiumUtil {

    public static void before(AppiumDriver<WebElement> driver) {
       //找到元素立即执行，找不到一直等待 15 秒，只要执行一次
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        try {
            Thread.sleep(ConstantValue.ONE_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void click(AppiumDriver<WebElement> driver,By by) {
        try {
            before(driver);
            driver.findElement(by).click();
            Lo.info(by.toString() + " = 控件点击成功" );
        } catch (NoSuchElementException e){
            Lo.error(by.toString() + " = 控件不存在或无法获取到" );
        /* driver.findElement(by.id(by));
        driver.findElement(By.name(by));
        driver.findElement(By.className(by));
        driver.findElement(By.xpath(by));*/
        }
    }

    public static Boolean clickB(AppiumDriver<WebElement> driver,By by) {
        try {
            before(driver);
            driver.findElement(by).click();
            Lo.info(by.toString() + " = 控件点击成功" );
            return true;
        } catch (NoSuchElementException e){
            Lo.debug(by.toString() + " = 控件不存在或无法获取到" );
            return false;
        /* driver.findElement(by.id(by));
        driver.findElement(By.name(by));
        driver.findElement(By.className(by));
        driver.findElement(By.xpath(by));*/
        }
    }



    /**
     * 点击相同控件中的指定序号的控件
     * @param driver
     * @param by
     * @param index
     */
    public static void click(AppiumDriver<WebElement> driver,By by, int index) {
        WebElement targetEle = null;
        int size = -1;
        try {
            before(driver);
            List<WebElement> byList = driver.findElements(by);
            size = byList.size();
            targetEle = byList.get(index);
            targetEle.click();
            Lo.info(by.toString() + " 正在点击列表控件，当前点击序号为 ：" + index + " 总共：" + size);
        } catch (IndexOutOfBoundsException ioobe){
                Lo.error( by.toString() + " 当前点击序号为：" + index + " = 控件不存在或无法获取到。"  + " 总共：" + size);
                /* driver.findElement(by.id(by));
                driver.findElement(By.name(by));
                driver.findElement(By.className(by));
                driver.findElement(By.xpath(by));*/
        } catch (NoSuchElementException e){
               Lo.error(by.toString() + " = 当前点击序号为：" + index + " = 控件不存在或无法获取到.。"  + " 总共：" + size);
        }
    }


    /**
     * 点击相同控件中的指定序号的控件
     * @param driver
     * @param by
     * @param index
     */
    public static Boolean clickB(AppiumDriver<WebElement> driver,By by, int index) {
        WebElement targetEle = null;
        int size = -1;
        try {
            before(driver);
            List<WebElement> byList = driver.findElements(by);
            size = byList.size();
            targetEle = byList.get(index);
            targetEle.click();
            Lo.info(by.toString() + " 正在点击列表控件，当前点击序号为 ：" + index + " 总共：" + size);
            return true;
        } catch (IndexOutOfBoundsException ioobe){
            Lo.debug( by.toString() + " 当前点击序号为：" + index + " = 控件不存在或无法获取到。"  + " 总共：" + size);
            return false;
/*                 driver.findElement(by.id(by));
                driver.findElement(By.name(by));
                driver.findElement(By.className(by));
                driver.findElement(By.xpath(by));*/
        } catch (NoSuchElementException e){
            Lo.debug(by.toString() + " = 当前点击序号为：" + index + " = 控件不存在或无法获取到.。"  + " 总共：" + size);
            return false;
        }
    }

    public static void sendKeys(AppiumDriver<WebElement> driver,By by, String string) {
        try {
            before(driver);
            driver.findElement(by).sendKeys(string);
        } catch (NoSuchElementException e){
            Lo.error(by.toString() + " = 控件不存在或无法获取到" );
        /* driver.findElement(by.id(by));
        driver.findElement(By.name(by));
        driver.findElement(By.className(by));
        driver.findElement(By.xpath(by));*/
        }
    }

    public static void sendKeys(AppiumDriver<WebElement> driver,By by, int index, String string) {
        List<WebElement> lis = driver.findElements(by);
        WebElement targetEle = lis.get(index);
        targetEle.sendKeys(string);
    }

    public static String getText(AppiumDriver<WebElement> driver,By by) {
        try {
            before(driver);
            String string=driver.findElement(by).getText();
            return string;
        } catch (NoSuchElementException e){
            Lo.error(by.toString() + " = 控件不存在或无法获取到" );
            /* driver.findElement(by.id(by));
            driver.findElement(By.name(by));
            driver.findElement(By.className(by));
            driver.findElement(By.xpath(by));*/
            return null;
        }
    }

    public static String getText(AppiumDriver<WebElement> driver,By by,int index) {
        List<WebElement> lis = driver.findElements(by);
        WebElement targetEle = lis.get(index);
        String string=targetEle.getText();
        return string;
    }


    /***
     * 通过class输入文本
     * @param driver
     */
    public static void sendKeysClass(AppiumDriver<WebElement> driver,String classname,int index, String para) {
        List<WebElement> lis = driver.findElementsByClassName(classname);
        WebElement targetEle = lis.get(index);
        targetEle.sendKeys(para);
    }
    /***
     * 通过class点击元素
     * @param driver
     */
    public static void clickClass(AppiumDriver<WebElement> driver,String classname,int index) {
        List<WebElement> lis = driver.findElementsByClassName(classname);
        WebElement targetEle = lis.get(index);
        targetEle.click();
    }
    /***
     * 通过class获取文本内容
     * @param driver
     */
    public static String getTextClass(AppiumDriver<WebElement> driver,String classname,int index) {
        List<WebElement> lis = driver.findElementsByClassName(classname);
        WebElement targetEle = lis.get(index);
        return targetEle.getText();

    }
    /***
     * 获取id文本内容
     * @param driver
     */
    public static String getTextId(AppiumDriver<WebElement> driver,String id){
        String string=driver.findElementById(id).getText();
        return string;
    }

    /***
     * 获取元素的数量，返回int
     * @param driver
     */
    public static int getTotal(AppiumDriver<WebElement> driver,By by){
        try {
            before(driver);
            List<WebElement> lis = driver.findElements(by);
            int number=lis.size();
            return number;
            } catch (NoSuchElementException e){
                Lo.error(by.toString() + " = 控件不存在或无法获取到" );
                /* driver.findElement(by.id(by));
                driver.findElement(By.name(by));
                driver.findElement(By.className(by));
                driver.findElement(By.xpath(by));*/
                return -1;
            }
    }

    /***
     * 断言
     */
    public static boolean flag = true;
    public static void verifyEquals(Object actual, Object expected){
        try {
            Assert.assertEquals(actual,expected);
        }catch(Error e){
            flag = false;
        }
    }
    public static void verifyEquals(Object actual, Object expected,String message){
        try {
            Assert.assertEquals(actual,expected,message);
            System.out.println("检查: "+actual+"正确");
        }catch(Error e){
            System.out.println("检查: "+actual+"失败"+";"+"实际为: "+expected);
            flag = false;
        }
    }
    public static void verifyExist(AndroidDriver<?> driver, String Id){
        try{
            Assert.assertNotNull(driver.findElementById(Id));
        }
        catch(Exception e){
            flag = false;
        }
    }
    //具体调用
    public static void AssertEquals(String actual, String expected){
        flag = true;
        verifyEquals(expected,actual,"比较2个数是否相等");
        Assert.assertTrue(flag);
    }
    public void AssertEqualsExist(AndroidDriver<?> driver,String Id){
        flag = true;
        verifyExist(driver,Id);
        Assert.assertTrue(flag);
    }
    /***
     * 断言、不存在某元素返回正确
     * @param driver
     */
    public static void AssertNoExist(AppiumDriver<WebElement> driver,By by){
        try{
            driver.findElement(by);
            Assert.assertTrue(false);

        }catch(Exception e){

            Assert.assertTrue(true);
        };
    }
    public static boolean NoExist(AppiumDriver<WebElement> driver,By by){
        try{
            driver.findElement(by);
            return false;

        }catch(Exception e){

            return true;
        }
    }

    public static boolean isExist(AppiumDriver<WebElement> driver,By by){
        try{
            before(driver);
            driver.findElement(by);
            Lo.info(by.toString() + " = 控件存在且已获取到" );
            return true;
        }catch(Exception e){
            Lo.error(by.toString() + " = 控件不存在或无法获取到" );
            return false;
        }
    }

    public static boolean isExistIndex(AppiumDriver<WebElement> driver,By by, int index){
        WebElement targetEle = null;
        int size = -1;
        try {
                before(driver);
                List<WebElement> byList = driver.findElements(by);
                size = byList.size();
                targetEle = byList.get(index);
                if(targetEle != null) {
                    Lo.info(targetEle.toString() + " 要查找的控件存在，当前点击序号为 ：" + index + " 总共：" + size);
                } else {
                    Lo.info(targetEle.toString() + " 要查找的控件不存在，当前点击序号为 ：" + index + " 总共：" + size);
                }
                return true;
             } catch (IndexOutOfBoundsException ioobe){
                Lo.error(by.toString() + " 当前点击序号为：" + index + " = 控件不存在或无法获取到。" + " 总共：" + size);
                return false;
             } catch(Exception e){
                Lo.error(by.toString() + " = 控件不存在或无法获取到" );
                return false;
        }
    }

    /***
     * 断言、存在某元素返回正确
     * @param driver
     */
    public static void AssertExist(AppiumDriver<WebElement> driver,By by){
        try{
            driver.findElement(by);
            Assert.assertTrue(true);

        }catch(Exception e){

            Assert.assertTrue(false);
        };
    }
    /***
     * 断言、存在某些中文
     * @param txt
     */
    public static void Exist(AppiumDriver<WebElement> driver,String txt){
        String txt1=txt;
        try{
            driver.findElement(By.xpath("//android.widget.TextView[@text='"+txt1+"'"+"]"));

            Assert.assertTrue(true);
            System.out.println("存在:"+txt1+"检查成功");
        }catch(Exception e){
            Assert.assertTrue(false);
            System.out.println("不存在:"+txt1+"检查失败");
        };
    }
    public static void Existdesc(AppiumDriver<WebElement> driver,String txt){
        String txt1=txt;
        try{

            driver.findElementByXPath("//android.view.View[contains(@content-desc,'"+txt1+"')]");
            Assert.assertTrue(true);
            System.out.println("存在:"+txt1+"检查成功");
        }catch(Exception e){

            Assert.assertTrue(false);
            System.out.println("不存在:"+txt1+"检查失败");
        };
    }

    /***
     * 清除文本内容
     * @param driver
     */
    public static void textClear(AppiumDriver<WebElement> driver,WebElement element){
        //选中输入框
        element.click();
        //将光标移到最后
        // driver.presskeycode();
        // driver.sendKeyEvent(123);
        //获取字符串长度
        String txt = element.getText();
        for(int i=0;i<txt.length();i++){
        // driver.sendKeyEvent(67);//一个个的删除
        }
    }

    /***
     * 截图到\Appium_Java\Demo目录下
     * @param driver
     */
    public static void snapShot(AppiumDriver<WebElement> driver,TakesScreenshot drivername, String filename) {

        String currentPath = System.getProperty("user.dir");
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);

        try {
            System.out.println("save snapshot path is:" + currentPath + "/"
                    + filename);
            FileUtils
                    .copyFile(scrFile, new File(currentPath + "\\" + filename));
        } catch (IOException e) {
            System.out.println("Can't save screenshot");
            e.printStackTrace();
        } finally {
            System.out.println("screen shot finished, it's in " + currentPath
                    + " folder");
        }
    }
    /***
     * 切换WEB页面查找元素
     * @param driver
     */
    public static void switchtoWeb(AppiumDriver<WebElement> driver){
        try {
            Set<String> contextNames = driver.getContextHandles();
            for (String contextName : contextNames) {
                System.out.println(contextName);
                if(contextName.contains("WEBVIEW")||contextName.contains("webview")){
                    System.out.println(contextName);
                    driver.context(contextName);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***
     * 重写点击、输入、获取文本、find
     * @param driver
     */
    public static WebElement element(AppiumDriver<WebElement> driver,By by) {
        WebElement element = driver.findElement(by);
        return element;
    }
    public static WebElement elements(AppiumDriver<WebElement> driver,By by, int index) {
        List<WebElement> lis = driver.findElements(by);
        WebElement element = lis.get(index);
        return element;
    }


    /**
     * 当前 手机输入法 的切换
     * "1"   魅族
     */
    public static void typeWriting(int device){
        switch (device){
            case 1 :
                TestUtils.excuteAdbShell(ConstantValue.MEIZU_INPUT);
                break;
            case 2 :
                TestUtils.excuteAdbShell(ConstantValue.HUAWEI_INPUT);
        }
    }
}
