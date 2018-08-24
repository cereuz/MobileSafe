package com.onezao.zao.mobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends AppCompatActivity {

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
                //监听手势的移动
                if(motionEvent.getX() - motionEvent1.getX() >  0){
                    //由右向左，移动到下一页
                    showNextPage();

                }  else if(motionEvent.getX() - motionEvent1.getX() < 0){
                    //由左向右，移动到上一页
                    showPrePage();
                }
                return false;
            }
        });
    }

    //上一页的抽象方法，由具体实现的子类决定具体跳转到哪个页面
    public abstract void showPrePage();
    //下一页的抽象方法，由具体实现的子类决定具体跳转到哪个页面
    public abstract void showNextPage();

    //跳转到上一页
    public void prePage(View view){
        showPrePage();
    }

    //跳转到下一页
    public void nextPage(View view){
        showNextPage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2. 通过手势处理类，接收多种类型的事件，用作处理
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
