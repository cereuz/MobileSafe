package com.zao.yll;

import com.zao.utils.AppiumUtil;
import com.zao.utils.ConstantValue;
import com.zao.utils.Log;
import com.zao.utils.TestUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.TestNGUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

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

    @BeforeTest
    public static void before(){
        TestUtils.excuteAdbShell(ConstantValue.APPIUM_INPUT);
        Log.debug("测试开始之前，主动切换成Appium输入法");
    }

    @Test
    public static void initSearch(){
        TestUtils.swipeToDown(driver, 3 * 1000, 1);
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        for(int i = 0; i < 1 ; i++) {
            AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_search));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
        }
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_search));
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
        AppiumUtil.click(driver,By.id(YLLConstantValue.search_tv_clear));
        /**
         * 返回首页
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
    }

    /**
     * 定位城市
     */
    @Test
    public static void initLocation(){
        /**
         * 直接返回
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_city));
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
        AppiumUtil.click(driver,By.id(YLLConstantValue.location_confirm_button));

        /**
         * 测试点击已开通城市
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_city));
        int size = AppiumUtil.getTotal(driver,By.id(YLLConstantValue.location_dw_gridview_tv));
        AppiumUtil.click(driver,By.id(YLLConstantValue.location_dw_gridview_tv));
        AppiumUtil.click(driver,By.id(YLLConstantValue.location_confirm_button));
        for(int i = 0; i < size; i++){
            AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_city));
            AppiumUtil.click(driver,By.id(YLLConstantValue.location_dw_gridview_tv),i);
            AppiumUtil.click(driver,By.id(YLLConstantValue.location_confirm_button));

            String home_tv_city = AppiumUtil.getText(driver,By.id(YLLConstantValue.home_tv_city));
            String home_weather_tv_weather = AppiumUtil.getText(driver,By.id(YLLConstantValue.home_weather_tv_weather));
            if(home_tv_city !=null || home_tv_city!= null){
                Log.debug("当前城市是："  + home_tv_city + "  当前的天气是：" + home_weather_tv_weather);
            }
        }

    }

    /**
     * 轮播图
     */
    @Test
    public static void initBanner(){
        int size = AppiumUtil.getTotal(driver,By.id(YLLConstantValue.home_banner_bannerViewPager));
        for(int i = 0; i < size ; i++) {
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_banner_bannerViewPager),i);
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
        }
    }

    /**
     *  未登录状态下
     *  0.右上角的消息按钮， 1.景点门票 ，2.酒店预订  3.领券中心
     */
    @Test
    public static void main(){
        for (int i=0; i < 2; i++) {
            AppiumUtil.click(driver,By.id(YLLConstantValue.home_iv_msg));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_local_rightImg));
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_ticket));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_iv_back));
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_hotel));
            AppiumUtil.click(driver,By.xpath(YLLConstantValue.xpath_hotel_back));
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_voucher));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_local_rightImg));
        }
    }

    /**
     *  景点门票  列表界面的操作
     */
    @Test
    public static void ticket(){
        /**
         * 搜索框
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_title_cd_ticket));
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_ticket_et_search));
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
        /**
         * 右上角的定位城市
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_ticket_city_click));
        AppiumUtil.click(driver,By.id(YLLConstantValue.location_dw_gridview_tv),TestUtils.randomTen());


    }

    @AfterTest
    public static void after(){
        /**
         * 切换成系统输入法
         */
        AppiumUtil.typeWriting(ConstantValue.DEVICE);  //切换手机系统 自己的输入法
        Log.debug("测试结束，主动切换成系统输入法");
    }

    /**
     * 在首页的输入框，输入内容，搜索，然后返回
     * 1. 输入内容的时候，需要先切换成Appium输入法，在@BeforeTest就切换了，2.按确认键的时候切换成系统输入法
     */
    private static void inputMsg(String message) {
        TestUtils.testSleep(ConstantValue.ONE_SECOND);
        AppiumUtil.click(driver,By.id(YLLConstantValue.search_search_content));
        TestUtils.testSleep(ConstantValue.ONE_SECOND);
        AppiumUtil.click(driver,By.id(YLLConstantValue.search_search_content));
//        driver.findElement(By.id(YLLConstantValue.search_search_content)).sendKeys(message);
        AppiumUtil.sendKeys(driver,By.id(YLLConstantValue.search_search_content),message);
        Log.debug(message);
        AppiumUtil.typeWriting(ConstantValue.DEVICE);  //切换手机系统 自己的输入法 . 1对应魅族
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        //点击右下角的搜索，即ENTER键  ===输入拼音的时候，输入法会先确认内容，再进行搜索！！==
        for (int i = 0; i < 2; i++){
            driver.pressKeyCode(AndroidKeyCode.ENTER);
        }
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        TestUtils.excuteAdbShell(ConstantValue.APPIUM_INPUT);
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
    }

}
