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

public class YllHomeMeizu extends Thread{

    public static String[] search_text = {"温州","wuxue","武汉","南京","wuxi","上海","杭州" };
    public static String[] login_phone = {"13636127440"," ","136","136363636363636","13282380039","18395997650","17017001700" };
    public static String[] login_password = {"123456"," ","654321"," ","123","17017001700" };
    /**
     * 启动APP ， 初始化 driver对象
     */
    static AndroidDriver driver = TestUtils.initDevice(YLLConstantValue.DEVICE_NAME_MEIZU,ConstantValue.UDID_MEIZU,YLLConstantValue.AUTOMATION_NAME_APPIUM,
            YLLConstantValue.PLATFORM_NAME_ANDROID, YLLConstantValue.PLATFORM_VERSION_711,
            YLLConstantValue.APP_PACKAGE_YLL, YLLConstantValue.APP_ACTIVITY_YLL,
            YLLConstantValue.NO_RESET_FALSE,YLLConstantValue.URL_PORT_4723);

    public static String getCurThread(){
        return Thread.currentThread().getName();
    }


    @BeforeTest(groups = "test1")
    public static void before(){
        TestUtils.excuteAdbShell(ConstantValue.APPIUM_INPUT);
        Lo.debug("测试开始之前，主动切换成Appium输入法");

        /**
         * 刷新首页数据
         */
        TestUtils.swipeToDown(driver,ConstantValue.SWIPE_DURING,1);
        TestUtils.testSleep(ConstantValue.THREE_SECOND);

    }

    @AfterTest(groups = "test1")
    public static void after(){
        /**
         * 切换成系统输入法
         */
        AppiumUtil.typeWriting(ConstantValue.DEVICE);  //切换手机系统 自己的输入法
        Lo.debug("测试结束，主动切换成系统输入法");
    }

    @Test(groups = "test1", priority = 1)
    public static void initSearch(){
        TestUtils.swipeToDown(driver, 3 * 1000, 1);
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        for(int i = 0; i < 1 ; i++) {
            AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_search));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_back_click));
        }
        for (int m = 0; m < 1; m++) {
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_tv_search));
        }
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
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back_click));
    }

    /**
     * 定位城市
     */
    @Test(groups = "test2")
    public static void initLocation(){
        /**
         * 直接返回
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_city));
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));

        /**
         * 测试点击已开通城市
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_city));
        int size = AppiumUtil.getTotal(driver,By.id(YLLConstantValue.location_dw_gridview_tv));
        AppiumUtil.click(driver,By.id(YLLConstantValue.location_dw_gridview_tv));
        for(int i = 0; i < size; i++){
            AppiumUtil.click(driver,By.id(YLLConstantValue.home_tv_city));
            AppiumUtil.click(driver,By.id(YLLConstantValue.location_dw_gridview_tv),i);

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
    @Test(groups = "test1", priority = 2)
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
    @Test(priority = 1)
    public static void main(){
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_iv_msg));
        if(!AppiumUtil.clickB(driver,By.id(YLLConstantValue.id_index_local_rightImg))){
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_profile));
            Lo.debug("当前登录的用户为：" + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_user_name))
                            + "； 手机号："   + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_user_phone_num))
                            + "； 余额："    + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_balence))
                            + "； 用户返现："    + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_return_money))
                            + "； 用户优惠券："    + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_discount_coupon)));
            AppiumUtil.click(driver,By.id(YLLConstantValue.profile_ib_setting));
            AppiumUtil.click(driver,By.id(YLLConstantValue.profile_LogOff));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_homepage));
            TestUtils.swipeToDown(driver,ConstantValue.SWIPE_DURING,2);
        }

        for (int i=0; i < 2; i++) {
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_ticket));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_iv_back));
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_hotel));
            AppiumUtil.click(driver,By.xpath(YLLConstantValue.xpath_meizu_hotel_back_1));
            AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_voucher));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_local_rightImg));
        }


//        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,1);
        TestUtils.swipeToUp(driver, ConstantValue.SWIPE_DURING, 1, 3, 1);
        if(AppiumUtil.clickB(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(2))) {
            swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));
        }

        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,1);
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_layout_collection));
        if(AppiumUtil.clickB(driver, By.id(YLLConstantValue.home_tv_ticket_title), TestUtils.random(4))){
            swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));
        }

        AppiumUtil.click(driver,By.id(YLLConstantValue.home_layout_default));
        if(AppiumUtil.clickB(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(4))){
            swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));
        }

        AppiumUtil.click(driver,By.id(YLLConstantValue.home_layout_distance));
        if (AppiumUtil.clickB(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(4))){
            swipeItemPage(By.id(YLLConstantValue.id_index_iv_back));
        }

        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,3);
        if(AppiumUtil.clickB(driver,By.id(YLLConstantValue.home_tv_ticket_title),TestUtils.random(6))) {
            TestUtils.testSleep(ConstantValue.THREE_SECOND);
            swipeItemPage(By.xpath(YLLConstantValue.xpath_hotel_back_9));
        }

        TestUtils.swipeToDown(driver,ConstantValue.SWIPE_DURING,5);//滑动界面，返回到首页最上边

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
    @Test(priority = 4)
    public static void initTicket(){
        /**
         * 搜索框
         */
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_title_cd_ticket));
        TestUtils.testSleep(ConstantValue.TWO_SECOND);
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_ticket_et_search));
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_back_click));
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
        AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_iv_back));
    }

    /**
     *  酒店 搜索，列表界面的操作
     */
    @Test(priority = 5)
    public static void initHotel(){
        AppiumUtil.click(driver, By.id(YLLConstantValue.home_title_cd_hotel));

        AppiumUtil.sendKeys(driver,By.xpath(YLLConstantValue.xpath_meizu_hotel_key_words),search_text[TestUtils.random(search_text.length)]);
        AppiumUtil.click(driver, By.xpath(YLLConstantValue.xpath_meizu_hotel_start_search));

        AppiumUtil.click(driver, By.id(YLLConstantValue.id_back));  //返回酒店搜索页面
        AppiumUtil.click(driver, By.xpath(YLLConstantValue.xpath_meizu_hotel_back_1));  //返回首页
    }

    /**
     *  登录注册
     */
    @Test(priority = 6)
    public static void initLogin() {
          AppiumUtil.click(driver, By.id(YLLConstantValue.id_index_local));
          int phone_lenth = login_phone.length;
          int password_lenth = login_password.length;
        for(int i = 0; i < phone_lenth * 2; i ++) {
            String phone = login_phone[TestUtils.random(phone_lenth)];
            String password = login_password[TestUtils.random(password_lenth)];
              AppiumUtil.sendKeys(driver, By.id(YLLConstantValue.login_edit_phone), phone);
              AppiumUtil.sendKeys(driver, By.id(YLLConstantValue.login_edit_password), password);
              AppiumUtil.click(driver, By.id(YLLConstantValue.login_btn_login));
              Lo.info("【登录注册】手机号码 ：" + phone + " 密码 ：" + password);
              TestUtils.testSleep(ConstantValue.TWO_SECOND);

              initLogout();
              AppiumUtil.click(driver, By.id(YLLConstantValue.id_index_local));
          }

        AppiumUtil.click(driver, By.id(YLLConstantValue.id_index_local_rightImg));
        TestUtils.swipeToUp(driver,ConstantValue.SWIPE_DURING,2);
    }

    /**
     * 退出登录
     */
    @Test(priority = 1)
    public static void initLogout() {
        AppiumUtil.click(driver,By.id(YLLConstantValue.home_iv_msg));
        if(!AppiumUtil.clickB(driver,By.id(YLLConstantValue.id_index_local_rightImg))){
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_back));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_profile));
            Lo.debug("当前登录的用户为：" + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_user_name))
                    + "； 手机号："   + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_user_phone_num))
                    + "； 余额："    + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_balence))
                    + "； 用户返现："    + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_return_money))
                    + "； 用户优惠券："    + AppiumUtil.getText(driver,By.id(YLLConstantValue.profile_tv_discount_coupon)));
            AppiumUtil.click(driver,By.id(YLLConstantValue.profile_ib_setting));
            AppiumUtil.click(driver,By.id(YLLConstantValue.profile_LogOff));
            AppiumUtil.click(driver,By.id(YLLConstantValue.id_index_homepage));
            TestUtils.swipeToDown(driver,ConstantValue.SWIPE_DURING,2);
        }
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
