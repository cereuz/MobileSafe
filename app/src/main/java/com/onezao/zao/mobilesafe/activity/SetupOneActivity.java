package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.onezao.zao.mobilesafe.R;

public class SetupOneActivity extends AppCompatActivity {

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //密码输入成功，四个导航界面没有设置完成-----> 跳转到导航界面第一个
        setContentView(R.layout.activity_setup_one);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
                //监听手势的移动
                if(motionEvent.getX() - motionEvent1.getX() >  0){
                    //由右向左，移动到下一页
                    Intent intent = new Intent(getApplicationContext(),SetupTwoActivity.class);
                    startActivity(intent);
                    //关闭当前页面
                    finish();
                    //开启平移动画
                    overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

                }  else if(motionEvent.getX() - motionEvent1.getX() < 0){
                    //由左向右，移动到上一页
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2. 通过手势处理类，接收多种类型的事件，用作处理
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 跳转到下一页
     * @param view
     */
    public void nextPage(View view) {
        Intent intent = new Intent(this,SetupTwoActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

}
