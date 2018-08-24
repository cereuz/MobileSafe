package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;

public class AToolActivity  extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool_view);

        //电话号码归属地查询方法
        initPhoneLocation();
    }

    /**
     * 查询电话号码归属地
     */
    private void initPhoneLocation() {
        TextView  tv_atool_location;
        TextView  tv_atool_sms_backup;
        TextView  tv_atool_ofenuse;
        TextView  tv_atool_applock;
        tv_atool_location = (TextView)findViewById(R.id.tv_atool_location);
        tv_atool_sms_backup = (TextView)findViewById(R.id.tv_atool_sms_backup);
        tv_atool_ofenuse = (TextView)findViewById(R.id.tv_atool_ofenuse);
        tv_atool_applock = (TextView)findViewById(R.id.tv_atool_applock);
        tv_atool_location.setOnClickListener(this);
        tv_atool_sms_backup.setOnClickListener(this);
        tv_atool_ofenuse.setOnClickListener(this);
        tv_atool_applock.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.tv_atool_location :
                 startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
                 break;

             case R.id.tv_atool_sms_backup :
                 break;
             case R.id.tv_atool_ofenuse :
                 break;
             case R.id.tv_atool_applock :
                 break;
         }
    }
}
