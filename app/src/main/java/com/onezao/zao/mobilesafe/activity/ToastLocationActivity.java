package com.onezao.zao.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;

public class ToastLocationActivity extends Activity implements View.OnClickListener{

        ImageView iv_drag;
        Button  btn_bottom;
        Button  btn_top;

        WindowManager mWM;
        int mScreenWidth;
        int mScreenHeight;

        long[]  mHits = new long[3];

      @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);

        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        iv_drag = (ImageView)findViewById(R.id.iv_drag);
        btn_bottom = (Button)findViewById(R.id.btn_bottom);
        btn_top = (Button)findViewById(R.id.btn_top);

        iv_drag.setOnClickListener(this);
        btn_bottom.setOnClickListener(this);
        btn_top.setOnClickListener(this);

        //获取屏幕的宽高
        mWM = (WindowManager)getSystemService(WINDOW_SERVICE);
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        mScreenHeight = mWM.getDefaultDisplay().getHeight();

        //获取存储的坐标值
        int location_x = SpUtils.getInt(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_X,0);
        int location_y = SpUtils.getInt(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_Y,0);

        //1.2 上下按钮的显示和隐藏处理
        setButtonVisibles(location_y);

        //左上角的坐标作用在iv_drag上
        //ImageView在相对布局中，所以其所在位置的规则需要由相对布局提供

        //指定宽高为WRAP_CONTENT
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将左上角坐标作用到iv_drag相对规则参数上
        layoutParams.leftMargin = location_x;
        layoutParams.topMargin = location_y;
        //将以上设置规则作用到iv_drag上
        iv_drag.setLayoutParams(layoutParams);

        //监听某一个控件的拖拽过程（按下，移动，拽起）
        iv_drag.setOnTouchListener(new View.OnTouchListener() {
                    int startX;
                    int startY;
            //对不同的事件做不同的处理
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN :
                            startX = (int)motionEvent.getRawX();
                            startY = (int)motionEvent.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE :
                            int moveX = (int)motionEvent.getRawX();
                            int moveY = (int)motionEvent.getRawY();

                            int disX = moveX - startX;
                            int disY = moveY - startY;

                            //1.当前控件所在屏幕的（左，上）角的位置  。  安卓的坐标是以左上角为准。
                            int left = iv_drag.getLeft() + disX; //左侧坐标
                            int top = iv_drag.getTop() + disY; //顶部坐标
                            int right = iv_drag.getRight() + disX; //右侧坐标
                            int bottom = iv_drag.getBottom() + disY; //底部坐标

                            //1.1容错处理（iv_drag不能拖拽出手机屏幕，上下左右都需要处理）
                            //左边缘不能超出屏幕
                            if(left < 0 ){
                                break;
                            }
                            //右边边缘不能超出屏幕
                            if(right > mScreenWidth){
                                break;
//                            return true;
                            }
                            //上边边缘不能超出屏幕可显示区域
                            if(top < 0){
                                break;
//                            return  true;
                            }
                            //下边缘不能大于底部边缘显示最大值（屏幕宽度-顶部状态栏宽度）
                            if(bottom > mScreenHeight - 32){
                                break;
//                            return true;
                            }

                            //1.2 上下按钮的显示和隐藏处理
                            setButtonVisibles(top);


                            //2.告知移动的控件，按计算出来的坐标去做展示
                            iv_drag.layout(left,top,right,bottom);

                            //3.展示坐标修改之后，重置一次起始坐标
                            startX = (int)motionEvent.getRawX();
                            startY = (int)motionEvent.getRawY();
                            break;


                        case MotionEvent.ACTION_UP :
                            //4.存储移动到的位置的坐标值
                            SpUtils.putInt(getApplicationContext(), ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_X,startX);
                            SpUtils.putInt(getApplicationContext(), ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_Y,startY);
                            break;
                    }
                    //在当前情况(只有touch事件，没有点击事件)下，返回false 不响应事件，返回true才会响应事件
                    //既要响应拖拽事件，又要响应点击事件，则此结果需要返回false  [onTouchListener和onClickListener同时调用了的时候]
                    return false;
                }
            }
        });
    }

    private void setButtonVisibles(int top) {
        //1.2 上下按钮的显示和异常处理
        if(top > mScreenHeight / 2){
           btn_bottom.setVisibility(View.INVISIBLE);
           btn_top.setVisibility(View.VISIBLE);
        } else {
            btn_bottom.setVisibility(View.VISIBLE);
            btn_top.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_top :
                moreClick();
                break;


            case R.id.iv_drag :
                moreClick();
                break;

            case R.id.btn_bottom :
                moreClick();
                break;
        }
    }

    /**
     * 多击点击事件
     *     参数说明
     *     1.原数组（要被拷贝的数组）
     *     2.原数组的拷贝起始位置索引值
     *     3.目标数组（原数组的数据--拷贝--》目标数组）
     *     4.目标数组接受值的起始索引位置
     *     5.拷贝的长度
     */
    private void moreClick() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if(mHits[mHits.length - 1] - mHits[0] < 500){
            //响应了一个三击事件
            Toast.makeText(getApplicationContext(),"你是IceAnt！",0).show();
            //设置控件居中
            setImageCenter();
        }
    }

    /**
     * 三击控件之后，控件居中
     */
    private void setImageCenter() {
        //计算居中时候上下左右的坐标值
        int left = mScreenWidth / 2 - iv_drag.getWidth() / 2;
        int right = mScreenWidth / 2 + iv_drag.getWidth() / 2;
        int top = mScreenHeight / 2 - iv_drag.getWidth() / 2;
        int bottom = mScreenHeight / 2 + iv_drag.getWidth() / 2;

        iv_drag.layout(left,top,right,bottom);
        //4.存储移动到的位置的坐标值
        SpUtils.putInt(getApplicationContext(), ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_X,iv_drag.getLeft());
        SpUtils.putInt(getApplicationContext(), ConstantValue.UPDATE_FILE,ConstantValue.TOAST_LOCATION_Y,iv_drag.getTop());
    }
}
