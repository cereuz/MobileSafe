package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;

public class SetupOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //根据导航界面是否设置完成，跳转到不同界面
        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.UPDATE_FILE, ConstantValue.SETUP_OVER,false);
        if(setup_over){
        //密码输入成功，并且四个导航界面设置完成-----> 停留在设置完成功能列表界面
        setContentView(R.layout.activity_setup_over);

        initUI();
        }  else {
            Intent intent = new Intent(this,SetupOneActivity.class);
            startActivity(intent);

/*        //密码输入成功，四个导航界面没有设置完成-----> 跳转到导航界面第一个
        setContentView(R.layout.activity_setup_one);
*//*        //开启了一个新的界面之后，关闭功能列表页面
        finish();*/
        }
    }

    //初始化界面
    private void initUI() {
       TextView tv_setup_safe_number = (TextView)findViewById(R.id.tv_setup_safe_number);
       //设置联系人号码
        String phone = SpUtils.getString(this,ConstantValue.UPDATE_FILE,ConstantValue.CONTACT_PHONE,"");
        tv_setup_safe_number.setText(phone);

       TextView tv_reset_setup = (TextView)findViewById(R.id.tv_reset_setup);
       tv_reset_setup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),SetupOneActivity.class);
               startActivity(intent);
           }
       });
    }
}
