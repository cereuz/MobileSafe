package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.SplashActivity;
import com.onezao.zao.mobilesafe.service.RocketService;

public class RocketActivity extends AppCompatActivity implements View.OnClickListener{

       Button btn_start_rocket;
       Button btn_stop_roket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_view);

        //初始化操作
        initUI();
        //初始化权限, 6.0以上申请系统权限，需要用户手动给出
        initPermission();
    }

    //初始化界面
    private void initUI() {
        btn_start_rocket = (Button)findViewById(R.id.btn_start_rocket);
        btn_stop_roket = (Button)findViewById(R.id.btn_stop_roket);
        btn_start_rocket.setOnClickListener(this);
        btn_stop_roket.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_rocket :
                //开启火箭的服务
                startService(new Intent(getApplicationContext(),RocketService.class));
                finish();
                SplashActivity.instance.finish();
                break;

            case R.id.btn_stop_roket :
                stopService(new Intent(getApplicationContext(),RocketService.class));
                finish();
                SplashActivity.instance.finish();
                break;
        }
    }

    /**
     * 初始化权限
     */
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (! Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }
    }

    /**
     * 处理权限申请之后的功能
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(this,"需要允许自定义Toast需要的权限！！", Toast.LENGTH_SHORT);
                    initPermission();
                }
            }
        }
    }
}
