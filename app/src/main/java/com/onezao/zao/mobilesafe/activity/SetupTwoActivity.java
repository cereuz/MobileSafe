package com.onezao.zao.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ToastUtil;
import com.onezao.zao.mobilesafe.view.SettingItemView;

public class SetupTwoActivity extends BaseSetupActivity {

    SettingItemView siv_sim_bond;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_two);

        //初始化界面
        initUI();
    }

    /**
     * 跳转到上一页
     */
    @Override
    public void showPrePage() {
        Intent intent = new Intent(this,SetupOverActivity.class);
        startActivity(intent);
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    /**
     * 跳转到下一页
     */
    @Override
    public void showNextPage() {
        boolean siv_isCheck = siv_sim_bond.isCheck();
        if(siv_isCheck){
            Intent intent = new Intent(this,SetupThreeActivity.class);
            startActivity(intent);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }  else {
            ToastUtil.showT(this,"请绑定手机卡！");
        }
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        siv_sim_bond = (SettingItemView)findViewById(R.id.siv_sim_bond);
        //1. 回显。（读取已有的绑定状态，用作显示，SP中是否存储了SIM卡的序列号）
        String sim_num = SpUtils.getString(this, ConstantValue.UPDATE_FILE,ConstantValue.SIM_NUMBER,"");
      //2. 判断序列卡号是否为空
        if(TextUtils.isEmpty(sim_num)){
            siv_sim_bond.setCheck(false);
        }  else {
            siv_sim_bond.setCheck(true);
        }

        siv_sim_bond.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"HardwareIds", "MissingPermission"})
            @Override
            public void onClick(View view) {
                //3. 获取原有状态，
                Boolean isCheck = siv_sim_bond.isCheck();
                //4. 将原有状态进行取反，状态设置给当前条目，存储（序列卡号）
                siv_sim_bond.setCheck(!isCheck);
                if(!isCheck){
                //5. 存储（序列卡号）
                    //6.1  获取SIM卡序列号 TelephoneManager
                    TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    //6.2 获取SIM卡的序列号
                    String sim_serial_num = manager.getSimSerialNumber();
                    //6.3 存储序列卡号到SP中
                    SpUtils.putString(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.SIM_NUMBER,sim_serial_num);
                } else {
                  //7. 将存储序列卡号的节点，从SP中删除
                    SpUtils.remove(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.SIM_NUMBER);
                }
                Log.i(ConstantValue.TAG,SpUtils.getString(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.SIM_NUMBER,"没有找到！"));
            }
        });
    }

/*    *//**
     * 跳转到下一页
     * @param view
     *//*
    public void prePage(View view) {
        Intent intent = new Intent(this,SetupOverActivity.class);
        startActivity(intent);
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    *//**
     * 跳转到下一页
     * @param view
     *//*
    public void nextPage(View view) {
        boolean siv_isCheck = siv_sim_bond.isCheck();
        if(siv_isCheck){
        Intent intent = new Intent(this,SetupThreeActivity.class);
        startActivity(intent);
        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }  else {
            ToastUtil.showT(this,"请绑定手机卡！");
        }
    }*/
}
