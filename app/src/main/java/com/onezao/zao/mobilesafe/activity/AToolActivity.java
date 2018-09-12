package com.onezao.zao.mobilesafe.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.engine.SmsBackUp;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;


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
             //归属地查询
             case R.id.tv_atool_location :
                 startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
                 break;

             //短信备份
             case R.id.tv_atool_sms_backup :
                 showProgressDialog();
                 break;

             //常用号码查询
             case R.id.tv_atool_ofenuse :
                 startActivity(new Intent(getApplicationContext(),CommonNumberQueryActivity.class));
                 break;
             case R.id.tv_atool_applock :

                 break;
         }
    }

    /**
     * 带进度条的对话框
     */
    private void showProgressDialog() {
          //1.创建一个带进度条的对话框
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("短信备份");
        //2.指定进度条的样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //2.2设置进度条不可以被取消
        progressDialog.setCancelable(false);
        //3.展示进度条
        progressDialog.show();
        //4.调用备份短信方法，备份短信
        new Thread(){
            @Override
            public void run() {
                super.run();
                String filePath = ZaoUtils.pathSD + "/ame/sms0306.xml";
                SmsBackUp.backupSms(getApplicationContext(),filePath,progressDialog);
                progressDialog.dismiss();

                //打开其他应用，发邮件
                emailFile(filePath);
            }
        }.start();

    }

    /**
     *  跳转到另外一个应用的发邮件Activity
     */
    private void emailFile(String path) {
        try{
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            //参数是包名，类全限定名，注意直接用类名不行
            ComponentName cn=new ComponentName("com.onezao.zao.zaov",
                    "com.onezao.zao.javamail.JavaMailSetupActivity");
            intent.setComponent(cn);
            intent.putExtra("path",path);
            startActivity(intent);
        }catch (Exception e){
            Log.i(ConstantValue.TAG,"跳转其他应用产生异常。。");
        }
    }
}
