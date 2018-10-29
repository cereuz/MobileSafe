package com.onezao.zao.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.activity.RocketBackgroundActivity;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

public class RocketService extends Service{

    WindowManager mWM;
    int mScreenWidth;
    int mScreenHeight;
    View mRocketView;

    WindowManager.LayoutParams params;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            params.y = (Integer)msg.obj;
            //告知窗体更新火箭view的所在位置
            mWM.updateViewLayout(mRocketView,params);
        }
    };

    //自定义Toast
    WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

    @Override
    public void onCreate() {

        //获取屏幕的宽高
        mWM = (WindowManager)getSystemService(WINDOW_SERVICE);
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        mScreenHeight = mWM.getDefaultDisplay().getHeight();

        //开启火箭
        showRocket();
        super.onCreate();
    }

    //开启火箭
    private void showRocket() {
        //界面布局的管理者对象
        params = mParams;

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                     | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   //默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
/*        //在响铃的时候显示Toast，和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;*/
        /**
         * 兼容8.0
         */
        if (Build.VERSION.SDK_INT > 25) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.setTitle("Toast");
        //指定Toast的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT + Gravity.TOP;

        //定义吐司所在的布局，并且将其转换成view对象，添加至窗体（权限）
        mRocketView = View.inflate(getApplicationContext(), R.layout.service_rocket_view,null);
        //执行动画
        ImageView iv_rocket = (ImageView)mRocketView.findViewById(R.id.iv_rocket);
        AnimationDrawable animationDrawable = (AnimationDrawable)iv_rocket.getBackground();
        animationDrawable.start();

        mWM.addView(mRocketView,params);

        //火箭升空,小火箭的拖拽事件
        mRocketView.setOnTouchListener(new View.OnTouchListener() {
                int startX;
                int startY;
                //对不同的事件做不同的处理
                @Override
                public boolean onTouch (View view, MotionEvent motionEvent){
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i(ConstantValue.TAG, "ACTION_DOWN" + ZaoUtils.getSystemTimeHello());
                            startX = (int) motionEvent.getRawX();
                            startY = (int) motionEvent.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            Log.i(ConstantValue.TAG, "ACTION_MOVE" + ZaoUtils.getSystemTimeHello());
                            int moveX = (int) motionEvent.getRawX();
                            int moveY = (int) motionEvent.getRawY();

                            int disX = moveX - startX;
                            int disY = moveY - startY;

                            params.x = params.x + disX;
                            params.y = params.y + disY;

                            //容错处理
                            //左侧不能超出界面
                            if (params.x < 0) {
                                params.x = 0;
                            }
                            //上侧不能超出界面
                            if (params.y < 0) {
                                params.y = 0;
                            }
                            if (params.x > mScreenWidth - mRocketView.getWidth()) {
                                params.x = mScreenWidth - mRocketView.getWidth();
                            }
                            if (params.y > mScreenHeight - mRocketView.getHeight() - 32) {
                                params.y = mScreenHeight - mRocketView.getHeight() - 32;
                            }

                            //告知窗体，吐司需要按照手势的移动，去做位置的更新。
                            mWM.updateViewLayout(mRocketView, params);

                            //3.展示坐标修改之后，重置一次起始坐标
                            startX = (int) motionEvent.getRawX();
                            startY = (int) motionEvent.getRawY();
                            break;

                        case MotionEvent.ACTION_UP:
                            Log.i(ConstantValue.TAG, "ACTION_UP" + ZaoUtils.getSystemTimeHello());
                            //火箭拖拽到指定区域的时候放手（抬起）才可以被发射
                            if(params.x > 100 && params.x < 600 && params.y >  360){
                                //发射火箭
                                sendRocket();
                                //产生尾气的Activity
                                Intent intent = new Intent(getApplicationContext(),RocketBackgroundActivity.class);
                                //开启火箭后，关闭了唯一的Activity对应的任务栈，所以在此次需要告知新开启的Activity开辟一个新的任务栈
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            break;
                    }
                    //在当前情况(只有touch事件，没有点击事件)下，返回false 不响应事件，返回true才会响应事件
                    //既要响应拖拽事件，又要响应点击事件，则此结果需要返回false  [onTouchListener和onClickListener同时调用了的时候]
                    return true;
                }
        });

    }

    /**
     * 在指定区域内抬起，就发射火箭
     */
    private void sendRocket() {
        //在向上的移动过程中，一直去减少y轴的大小，直到减少为 0 为止。
        //在主线程中不能去睡眠，可能会导致主线程阻塞
        new Thread(){
            @Override
            public void run() {
              for(int i = 0 ; i < 11 ; i++){
                 int height = 350 - i*35;
                  try {
                      Thread.sleep(50);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  Message msg = Message.obtain();
                  msg.obj = height;
                  mHandler.sendMessage(msg);
              }
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if(mWM != null && mRocketView != null){
            mWM.removeView(mRocketView);
        }
        super.onDestroy();
    }
}
