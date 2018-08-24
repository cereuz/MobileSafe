package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ToastUtil;

public class SetupFourActivity extends BaseSetupActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_four);

        initUI();
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(this,SetupThreeActivity.class);
        startActivity(intent);

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    public void showNextPage() {
        {
            boolean open_security = SpUtils.getBoolean(this, ConstantValue.UPDATE_FILE, ConstantValue.OPEN_SECURITY, false);
            if (open_security) {

                Intent intent = new Intent(this, SetupOverActivity.class);
                startActivity(intent);

                finish();
                SpUtils.putBoolean(this, ConstantValue.UPDATE_FILE, ConstantValue.SETUP_OVER, open_security);
                //开启平移动画
                overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
            }  else  {
                ToastUtil.showT(this,"请开启防盗保护");
            }
        }
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        final CheckBox checkBox = (CheckBox)findViewById(R.id.cb_box);
        //1. 是否选中状态的回显
        boolean open_security = SpUtils.getBoolean(this,ConstantValue.UPDATE_FILE,ConstantValue.OPEN_SECURITY,false);
        //2.0  根据状态，修好checkbox选择框的显示
        checkBox.setChecked(open_security);
        //2.1 根据状态，修改checkbox后续的文字显示
        if(open_security){
            checkBox.setText("安全设置已开启");
        } else {
            checkBox.setText("安全设置已关闭");
        }

        //3.点击过程中，checkbox的状态的切换，以及切换之后状态的存储
//        checkBox.setChecked(!open_security);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                checkBox.setChecked(!open_security);
               SpUtils.putBoolean(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.OPEN_SECURITY,b);
               //5. 根据开启关闭状态，去修改显示的文字
                if(b){
                    checkBox.setText("安全设置已开启");
                } else {
                    checkBox.setText("安全设置已关闭");
                }
            }
        });
    }

/*    *//**
     * 跳转到下一页
     * @param view
     *//*
    public void prePage(View view) {
        Intent intent = new Intent(this,SetupThreeActivity.class);
        startActivity(intent);

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    *//**
     * 跳转到下一页
     * @param view
     *//*
    public void nextPage(View view) {
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.UPDATE_FILE, ConstantValue.OPEN_SECURITY, false);
        if (open_security) {

            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);

            finish();
            SpUtils.putBoolean(this, ConstantValue.UPDATE_FILE, ConstantValue.SETUP_OVER, open_security);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }  else  {
            ToastUtil.showT(this,"请开启防盗保护");
        }
    }*/
}
