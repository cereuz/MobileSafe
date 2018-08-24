package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.home.HomeActivity;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ToastUtil;

public class SetupThreeActivity extends BaseSetupActivity {

    EditText et_phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_three);

        initUI();
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(this,SetupTwoActivity.class);
        startActivity(intent);

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    public void showNextPage() {
        String phone_number = et_phone_number.getText().toString().trim();
        if(!TextUtils.isEmpty(phone_number)) {
            Intent intent = new Intent(this, SetupFourActivity.class);
            startActivity(intent);

            //关闭当前页面
            finish();
            //如果显示是输入的，需要重新保存一下
            SpUtils.putString(this,ConstantValue.UPDATE_FILE,ConstantValue.CONTACT_PHONE,phone_number);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        } else {
            ToastUtil.showT(this,"电话号码不能为空！");
        }
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        et_phone_number = (EditText)findViewById(R.id.et_phone_number);
        //联系人回显
        String sp_phone = SpUtils.getString(this,ConstantValue.UPDATE_FILE,ConstantValue.CONTACT_PHONE,"");
        et_phone_number.setText(sp_phone);

        Button bt_select_number = (Button)findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    /**
     * 返回当前页面的时候，接收结果的方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null) {
            //1.获取返回带过来的数据
            String phone = data.getStringExtra("phone");
            //2.将特殊字符过滤，(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);
            //3.存储联系人到SP中
            SpUtils.putString(this, ConstantValue.UPDATE_FILE,ConstantValue.CONTACT_PHONE,phone);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

/*    *//**
     * 跳转到下一页
     * @param view
     *//*
    public void prePage(View view) {
        Intent intent = new Intent(this,SetupTwoActivity.class);
        startActivity(intent);

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    *//**
     * 跳转到下一页
     * @param view
     *//*
    public void nextPage(View view) {
        String phone_number = et_phone_number.getText().toString().trim();
        if(!TextUtils.isEmpty(phone_number)) {
            Intent intent = new Intent(this, SetupFourActivity.class);
            startActivity(intent);

            //关闭当前页面
            finish();
            //如果显示是输入的，需要重新保存一下
            SpUtils.putString(this,ConstantValue.UPDATE_FILE,ConstantValue.CONTACT_PHONE,phone_number);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        } else {
            ToastUtil.showT(this,"电话号码不能为空！");
        }
    }*/
}
