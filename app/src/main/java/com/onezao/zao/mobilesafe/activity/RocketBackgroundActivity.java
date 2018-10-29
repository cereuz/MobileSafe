package com.onezao.zao.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.onezao.zao.mobilesafe.R;

public class RocketBackgroundActivity extends Activity{

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
             //收到消息之后结束当前动画背景Activity
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocketbackground);

        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        ImageView iv_rocket_bg_top = (ImageView)findViewById(R.id.iv_rocket_bg_top);
        ImageView iv_rocket_bg_bottom = (ImageView)findViewById(R.id.iv_rocket_bg_bottom);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        iv_rocket_bg_top.startAnimation(alphaAnimation);
        iv_rocket_bg_bottom.startAnimation(alphaAnimation);

        mHandler.sendEmptyMessageDelayed(0,1000);
    }
}
