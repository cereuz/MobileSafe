package com.onezao.zao.mobilesafe.activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.engine.AddressDao;

public class QueryAddressActivity extends AppCompatActivity{

    EditText et_phone;
    TextView tv_address;
    String mAddress;

   private Handler  mHandler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
            //4.控件使用查询结果，修改数据
           tv_address.setText(mAddress);
       }
   };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address_view);

        initUI();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
       //5.实时查询（监听输入框文本变化）
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = et_phone.getText().toString().trim();
                //2.查询是耗时操作，开启子线程
                query(phone);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_address = (TextView)findViewById(R.id.tv_address);
    }

    public void queryPhone(View view) {
        String phone = et_phone.getText().toString().trim();
        if(!TextUtils.isEmpty(phone)){
        //2.查询是耗时操作，开启子线程
        query(phone);
        }  else  {
            //2.2输入框为空的时候，点击查询，会产生抖动效果。
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_phone.startAnimation(shake);
            //2.3 输入框为空的时候，点击查询，会产生抖动效果
            Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            long[] patter = {2000, 5000, 2000, 5000};
            vibrator.vibrate(patter, 3);
            //2.3.1 取消震动
//            vibrator.cancel();
        }

    }



    /**
     *  查询是耗时操作，开启子线程
     * @param phone
     */
    private void query(final String phone) {
         new Thread( ) {
             @Override
             public void run() {
                 //测试查询引擎类是否成功
                 mAddress = AddressDao.getAddress(phone);
                 //3.子线程，需要使用消息机制，告知主线程查询结束，可以去使用查询结果
                 mHandler.sendEmptyMessage(0);
             }
         }.start();
    }
}
