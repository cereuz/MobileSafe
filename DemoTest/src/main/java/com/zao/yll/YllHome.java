package com.zao.yll;

import com.zao.utils.AppiumUtil;
import com.zao.utils.ConstantValue;
import com.zao.utils.Lo;
import com.zao.utils.TestUtils;

import org.openqa.selenium.By;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
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

    @BeforeTest
    public static void before(){
        TestUtils.excuteAdbShell(ConstantValue.APPIUM_INPUT);
        Lo.debug("测试开始之前，主动切换成Appium输入法");

//        driver.to
    }

    @AfterTest
    public static void after(){
        /**
         * 切换成系统输入法
         */
        AppiumUtil.typeWriting(ConstantValue.DEVICE);  //切换手机系统 自己的输入法
        Lo.debug("测试结束，主动切换成系统输入法");
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
                Lo.debug("当前城市是："  + home_tv_city + "  当前的天气是：" + home_weather_tv_weather);
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
        TestUtils.swipeToDown(driver,ConstantValue.SWIPE_DURING,1);
        TestUtils.testSleep(ConstantValue.THREE_SECOND);

        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,1);
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(3));
        swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));

        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,1);
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_layout_collection));
        if(AppiumUtil.clickB(driver, By.id(YLLConstantValue.home_tv_ticket_title), TestUtils.random(4))){
            swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));
        }

        AppiumUtil.click(driver,By.id(YLLConstantValue.home_layout_default));
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(4));
        swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));

        AppiumUtil.click(driver,By.id(YLLConstantValue.home_layout_distance));
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(4));
        swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));

        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,3);
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(6));
        swipeItemPage(By.xpath(YLLConstantValue.xpath_hotel_back));


/*
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
*/


    }

    /**
     * 景点条目的详情界面的操作。操作结束之后返回。
     */
    private static void swipeItemPage(By by) {
        for (int j = 0; j < 1; j++) {
            TestUtils.swipeToUp(driver, ConstantValue.SWIPE_DURING, 3);
            TestUtils.swipeToDown(driver, ConstantValue.SWIPE_DURING, 3);
        }
        AppiumUtil.click(driver, by);
    }

    /**
     *  景点门票  列表界面的操作
     */
    @Test
    public static void initTicket(){
        /**
         * 搜索框
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_title_cd_ticket));
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_ticket_et_search));
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
        /**
         * 右上角的定位城市 。
         * 1.先获取总的个数，然后随机点击其中一个
         * 2.必须先进入对应界面，才能获取到控件的总共个数。不然会全部都是 0 ！！！
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_ticket_city_click));
        int size = AppiumUtil.getTotal(driver,By.id(YLLConstantValue.location_dw_gridview_tv));
        AppiumUtil.click(driver,By.id(YLLConstantValue.location_dw_gridview_tv),TestUtils.random(size));
        /**
         * 三个列表
         */
        for (int i = 0; i < 1; i++) {
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_ticket_tv_collection));
            clickListItem();
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_ticket_tv_distance));
            clickListItem();
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_ticket_tv_moren));
            clickListItem();
        }

        /**
         * 点击返回到 主界面
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
    }

    /**
     *  酒店 搜索，列表界面的操作
     */
    @Test
    public static void initHotel(){
        AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_hotel));

        AppiumUtil.sendKeys(driver,By.xpath(YLLConstantValue.xpath_hotel_key_words),search_text[TestUtils.random(search_text.length)]);
        AppiumUtil.click(driver, By.xpath(YLLConstantValue.xpath_hotel_start_search));

        AppiumUtil.click(driver, By.id(YLLConstantValue.id_back));  //返回酒店搜索页面
        AppiumUtil.click(driver, By.xpath(YLLConstantValue.xpath_hotel_back));  //返回首页
    }

    /**
         * 1.上下滑动列表
         * 2.点击列表条目 ， 返回
         */
    private static void clickListItem() {
        for(int i = 0 ; i < 1; i++) {
            TestUtils.swipeToUp(driver, ConstantValue.SWIPE_DURING, 3);
            TestUtils.swipeToDown(driver, ConstantValue.SWIPE_DURING, 2);
        }

        int size2 = AppiumUtil.getTotal(driver,By.id(YLLConstantValue.home_ticket_tv_item_ticket_title));
        for(int i = 0 ; i < size2; i++) {
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_ticket_tv_item_ticket_title),TestUtils.random(size2));
            for(int j = 0 ; j < 1; j++) {
                TestUtils.swipeToUp(driver, ConstantValue.SWIPE_DURING, 3);
                TestUtils.swipeToDown(driver, ConstantValue.SWIPE_DURING, 3);
            }
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_iv_back));
        }
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
        AppiumUtil.sendKeys(driver,By.id(YLLConstantValue.search_search_content),message);
        Lo.info(message);
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
