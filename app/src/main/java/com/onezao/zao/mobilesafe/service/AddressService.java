package com.onezao.zao.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.engine.AddressDao;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

/**
 * 电话号码归属地的方格，可以跟随手势去做移动
 *     1.在设置界面添加一个可点击条目，点击此条目弹出activity(半透明)
 *     2.双击居中的view和描述文字处在不同的竖直（上下）区域。
 *     3.限制view的可拖拽范围
 *     4.view双击居中
 */

/**
 * 是否开启归属地状态显示的判断条件
 *     服务正在运行==来电归属地开启
 *     服务关闭==来电归属地关闭
 */

/**
 * 在应用层监听通话状态只有三种，从TelephonyManager.java中注释可知这三种状态含义如下：
 * CALL_STATE_IDLE 空闲态(没有通话活动)
 * CALL_STATE_RINGING 包括响铃、第三方来电等待
 * CALL_STATE_OFFHOOK 包括dialing拨号中、active接通、hold挂起等
 *
 * * // 精确的九大通话状态
     public class PreciseCallState implements Parcelable {
     public static final int PRECISE_CALL_STATE_IDLE =           0; //通话空闲
     public static final int PRECISE_CALL_STATE_ACTIVE =         1; //正在通话(活动中)
     public static final int PRECISE_CALL_STATE_HOLDING =        2; //通话挂起(例如我和多个人通话,其中一个通话在活动,而其它通话就会进入挂起状态)
     public static final int PRECISE_CALL_STATE_DIALING =        3; //拨号开始
     public static final int PRECISE_CALL_STATE_ALERTING =       4; //正在呼出(提醒对方接电话)
     public static final int PRECISE_CALL_STATE_INCOMING =       5; //对方来电
     public static final int PRECISE_CALL_STATE_WAITING =        6; //第三方来电等待(例如我正在和某人通话,而其他人打入时就会就进入等待状态)
     public static final int PRECISE_CALL_STATE_DISCONNECTED =   7; //挂断完成
     public static final int PRECISE_CALL_STATE_DISCONNECTING =  8; //正在挂断
 }
 */

public class AddressService extends Service{

        //电话管理者对象
        TelephonyManager mTM;
        //电话状态监听者
        MyPhoneStateListener myPhoneStateListener;
        //自定义Toast
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        View mViewToast;
         //窗体对象
        WindowManager mWM;
        int mScreenWidth;
        int mScreenHeight;
        //查询出来的号码归属地
        String mAddress;
        //自定义Toast的TextView
        TextView tv_toast;
        int[]  mStyleIds;
        //去电广播接收者
        InnerOutCallReceiver mInnerOutCallReceiver;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_toast.setText(mAddress);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    /**
     * 初始化界面
     */
    private void initData() {
        //第一次开启服务之后，就需要去管理Toast的显示
        //电话状态的监听，(服务开启的时候，需要去做监听，关闭的时候电话状态就不需要监听)
        // 1.电话管理者对象
        mTM = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //2. 监听电话状态
        myPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        //3. 获取窗体对象
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        mScreenHeight = mWM.getDefaultDisplay().getHeight();

        //去电显示电话归属地
        showOutgoingAddress();
    }

    /**
     * 拨打电话的时候，显示号码归属地
     */
    private void showOutgoingAddress() {
        //监听拨出电话的广播过滤事件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        //创建广播接收者
        mInnerOutCallReceiver = new InnerOutCallReceiver();
        //动态注册广播接收者
        registerReceiver(mInnerOutCallReceiver,intentFilter);
    }

    class  InnerOutCallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到此广播后，需要显示自定义的吐司，显示拨出号码的归属地
            //获取拨出电话号码的字符串
            String phone = getResultData();
            showToast(phone);
        }
    }

    class  MyPhoneStateListener extends PhoneStateListener{
        //3.手动重写，电话状态发生改变的时候会触发的方法
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE :
                    //电话空闲状态 。 没有任何活动（移除吐司）
                    Log.i(ConstantValue.TAG,"挂断电话，空闲了。。。。。。。");
//                    showToast(phoneNumber + "--挂断电话，空闲了。。。。。。。");
                    //挂断电话的时候窗体需要移除吐司
                    if(mWM != null && mViewToast != null){
                        mWM.removeView(mViewToast);
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK :
                    //电话摘机状态 。至少有个电话活动，该活动或是拨打（dialing），或是通话
                    Log.i(ConstantValue.TAG,"电话摘机状态。。。。。。。");
//                    showToast(phoneNumber + "--电话摘机状态。。。。。。。");
                    break;

                case TelephonyManager.CALL_STATE_RINGING :
                    //电话响铃状态。展示吐司
                    Log.i(ConstantValue.TAG,"响铃了。。。。。。。");
                    showToast(phoneNumber);
                    break;
            }
            super.onCallStateChanged(state, phoneNumber);
        }
    }


    /**
     * 弹出吐司
     * @param phoneNumber
     */
    private void showToast(String phoneNumber) {
//        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();

        //界面布局的管理者对象
        final WindowManager.LayoutParams params = mParams;

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                     | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   //默认能够被触摸
                     | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示Toast，和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        //指定Toast的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT + Gravity.TOP;

        //吐司显示效果（吐司布局文件），xml --> view (吐司)，将吐司挂到windowManager窗体上
        mViewToast = View.inflate(getApplicationContext(), R.layout.toast_view,null);
        //初始化显示文字的控件
        tv_toast = (TextView)mViewToast.findViewById(R.id.tv_toast);

        mViewToast.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;
            //对不同的事件做不同的处理
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN :
                            Log.i(ConstantValue.TAG,"ACTION_DOWN" + ZaoUtils.getSystemTimeHello());
                            startX = (int)motionEvent.getRawX();
                            startY = (int)motionEvent.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE :
                            Log.i(ConstantValue.TAG,"ACTION_MOVE" + ZaoUtils.getSystemTimeHello());
                            int moveX = (int)motionEvent.getRawX();
                            int moveY = (int)motionEvent.getRawY();

                            int disX = moveX - startX;
                            int disY = moveY - startY;

                            params.x = params.x + disX;
                            params.y = params.y + disY;

                            //容错处理
                            //左侧不能超出界面
                            if(params.x < 0){
                                params.x = 0;
                            }
                            //上侧不能超出界面
                            if(params.y < 0){
                                params.y = 0;
                            }
                            if(params.x > mScreenWidth - mViewToast.getWidth()){
                                 params.x = mScreenWidth - mViewToast.getWidth();
                            }
                            if(params.y > mScreenHeight - mViewToast.getHeight() - 32){
                                 params.y = mScreenHeight - mViewToast.getHeight() - 32;
                            }

                            //告知窗体，吐司需要按照手势的移动，去做位置的更新。
                            mWM.updateViewLayout(mViewToast,params);

                            //3.展示坐标修改之后，重置一次起始坐标
                            startX = (int)motionEvent.getRawX();
                            startY = (int)motionEvent.getRawY();
                            break;

                        case MotionEvent.ACTION_UP :
                            Log.i(ConstantValue.TAG,"ACTION_UP" + ZaoUtils.getSystemTimeHello());
                            //4.存储移动到的位置的坐标值
                            SpUtils.putInt(getApplicationContext(), ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_X,params.x);
                            SpUtils.putInt(getApplicationContext(), ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_Y,params.y);
                            break;
                    }
                    //在当前情况(只有touch事件，没有点击事件)下，返回false 不响应事件，返回true才会响应事件
                    //既要响应拖拽事件，又要响应点击事件，则此结果需要返回false  [onTouchListener和onClickListener同时调用了的时候]
                    return true;
                }
        });

        //读取SP中存储的吐司位置的 x ，y坐标。
        params.x = SpUtils.getInt(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_X,0);
        params.y = SpUtils.getInt(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_Y,0);


        //从SP中获取色值文字的索引，匹配图片，用作显示。
        mStyleIds = new int[]{
               R.color.colorDecent,
               R.color.colorOrange,
                R.color.colorBlue,
                R.color.colorGrey,
                R.color.colorGreen,
                R.color.colorRed  };
      int mToastStyleIndex = SpUtils.getInt(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.TOAST_STYLE,2);
      tv_toast.setBackgroundColor(getResources().getColor(mStyleIds[mToastStyleIndex]));//自定义的颜色，需要转化一下。
//      tv_toast.setBackgroundColor(Color.parseColor("#FF6100"));  //自定义的颜色，需要转化一下。

        //在窗体上挂载一个View（权限）
        mWM.addView(mViewToast,params);

        //获取到了来电号码之后，需要做来电号码归属地查询
        query(phoneNumber);
    }

    /**
     * 查询电话号码归属地属于耗时操作，需要开启子线程
     * @param phoneNumber
     */
    private void query(final String phoneNumber) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mAddress = AddressDao.getAddress(phoneNumber);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //取消对电话状态的监听 (开启服务的时候监听电话的对象)
        if(mTM != null && myPhoneStateListener != null){
              mTM.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        //去电广播接收者的注销过程
        if(mInnerOutCallReceiver != null){
            unregisterReceiver(mInnerOutCallReceiver);
        }
        super.onDestroy();
    }
}
