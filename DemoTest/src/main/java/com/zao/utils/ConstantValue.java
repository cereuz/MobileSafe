package com.zao.utils;

public class ConstantValue {
    public static long ONE_SECOND = 1000;
    public static long TWO_SECOND = 2000;
    public static long THREE_SECOND = 3000;
    public static long FOUR_SECOND = 3000;
    public static long SIX_SECOND = 6000;
    public static long TEN_SECOND = 10 * 1000;
    public static long TWENTY_SECOND = 20 * 1000;

    public static int SWIPE_DURING = 3 * 1000;
    public static int SWIPE_SLEEP = 1 * 1000;
    /**
     * 机型区分
     *  1 ：魅族
     */
    public static int DEVICE = 1;

    public static String UDID_HUAWEI = "8RYBB18307505264";
    public static String UDID_MEIZU = "80SQBDQF2237K";

    /**
     * 输入法区分
     */
//    public static String GOOGLE_INPUT = "adb shell ime set com.android.inputmethod.pinyin/.PinyinIME";
    public static String GOOGLE_INPUT = "adb shell ime set com.android.inputmethod.latin/.LatinIME";
    public static String APPIUM_INPUT = "adb shell ime set io.appium.android.ime/.UnicodeIME";
    public static String HUAWEI_INPUT = "adb shell ime set com.baidu.input_huawei/.ImeService";
    public static String MEIZU_INPUT = "adb shell ime set com.meizu.flyme.input/com.meizu.input.MzInputService";

    public static String id_permission_message = "com.android.packageinstaller:id/permission_message";
    public static String id_permission_allow_button = "com.android.packageinstaller:id/permission_allow_button";

}
