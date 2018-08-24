package com.onezao.zao.mobilesafe.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.activity.AToolActivity;
import com.onezao.zao.mobilesafe.activity.DeviceAdminActivity;
import com.onezao.zao.mobilesafe.activity.SettingActivity;
import com.onezao.zao.mobilesafe.activity.SetupOverActivity;
import com.onezao.zao.mobilesafe.activity.WebwithAndroidActivity;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "ZAO";
    private List<Book> mlsit = new ArrayList<Book>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();
//        initView();
        initView2(4,false,true);
    }

    //初始化布局，类似GridView
    private void initView2(int spanCount,Boolean isversion,Boolean orientation) {
        //初始化RecyclerView
        RecyclerView recyslerview = (RecyclerView) findViewById(R.id.home_recycler_view);
        //创建GridLayoutManager 对象 这里使用 GridLayoutManager 是GridLayout布局的意思
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);

        //B 通过布局管理器，可以控制条目排列顺序  true：反向显示  false：正常显示(默认)
        gridLayoutManager.setReverseLayout(isversion);
        //C 设置RecyclerView显示的方向，是水平还是垂直！！ GridLayoutManager.VERTICAL(默认) false
        gridLayoutManager.setOrientation(orientation ? GridLayoutManager.VERTICAL: LinearLayoutManager.HORIZONTAL);

        //设置RecyclerView 布局
        recyslerview.setLayoutManager(gridLayoutManager);
        //设置Adapter
        HomeGridBaseAdapter adapter = new HomeGridBaseAdapter(mlsit);

        //条目点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showT(HomeActivity.this,"onItemClick ：" + position);
                toNext(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                ToastUtil.showT(HomeActivity.this,"onItemLongClick ：" + position);
            }
        });

        Log.i(TAG,mlsit.toString());
        recyslerview.setAdapter(adapter);
    }

    //点击条目，跳转事件
    private void toNext(int position) {
        switch (position){
            case  0 :
                //开启密码对话框
                showDialog();
                break;
            case  7 :
                //跳转到高级工具功能列表界面
                startActivity(new Intent(getApplicationContext(),AToolActivity.class));
                break;
            case  8 :
                Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                //传递数据到打开的Activity
                intent.putExtra("item",position + " . 蜗牛");
                startActivity(intent);
                break;
            case  9 :
                //进入Android调用Web的功能界面
                Intent intent9 = new Intent(getApplicationContext(),WebwithAndroidActivity.class);
                startActivity(intent9);
                break;
            case  10 :
                //设备管理
                Intent intent3 = new Intent(HomeActivity.this, DeviceAdminActivity.class);
                startActivity(intent3);
                break;
        }

    }

    //开启 功能  密码对话框
    private void showDialog() {
        //判断本地是否有存储密码 （SP   字符串）
        String sp_moble_safe_pwd  = SpUtils.getString(this, ConstantValue.UPDATE_FILE,ConstantValue.MOBLE_SAFE_PWD,"");
        if(TextUtils.isEmpty(sp_moble_safe_pwd)){
        //1. 初始设置密码对话框
            showSetPwdDialog();
        }  else  {
        //2. 确认密码对话框
            showConfirmPwdDialog();
        }
    }

    /**
     * 第一次进入，设置密码对话框
     */
    private void showSetPwdDialog() {
        //因为需要自己定义对话框的展示样式，所以需要调用dialog.setView(view);
        // view 是由自己编写的xml转换成的view对象   xml  -->  view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this,R.layout.dialog_set_pwd,null);
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                //提交保存密码
                submitPWD(view,dialog);
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框 ， 提交，保存密码功能
     * @param view
     */
    private void submitPWD(View view,AlertDialog dialog) {
        EditText et_set_pwd = (EditText)view.findViewById(R.id.et_set_pwd);
        EditText et_confirm_pwd = (EditText)view.findViewById(R.id.et_confirm_pwd);
        String s_set_pwd = et_set_pwd.getText().toString();
        String s_confirm_pwd = et_confirm_pwd.getText().toString();

        if(!TextUtils.isEmpty(s_set_pwd) && !TextUtils.isEmpty(s_confirm_pwd)){
            if(s_confirm_pwd.equals(s_set_pwd)){
               //进入应用手机防盗模块
               Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
               startActivity(intent);
               //跳转到新页面之后，隐藏对话框
                dialog.dismiss();
               //保存密码到本地
               SpUtils.putString(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.MOBLE_SAFE_PWD,s_confirm_pwd);

            }  else {
                ToastUtil.showT(getApplicationContext(),ConstantValue.NOTICE_PWD_SAME);
            }
        }  else {
            //提示用户密码输入有为空的情况
            ToastUtil.showT(getApplicationContext(),ConstantValue.NOTICE_PWD);
        }
    }

    /**
     *  确认密码对话框
     */
    private void showConfirmPwdDialog() {
        //因为需要自己定义对话框的展示样式，所以需要调用dialog.setView(view);
        // view 是由自己编写的xml转换成的view对象   xml  -->  view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this,R.layout.dialog_confirm_pwd,null);
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                //提交保存密码
                confirmPWD(view,dialog);
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 确认密码对象框的 提交保存密码逻辑
     * @param view
     */
    private void confirmPWD(View view,AlertDialog dialog) {
        EditText et_confirm_pwd = (EditText)view.findViewById(R.id.et_confirm_pwd);
        String s_confirm_pwd = et_confirm_pwd.getText().toString();

        if(!TextUtils.isEmpty(s_confirm_pwd)){
            String s_file_pwd = SpUtils.getString(getApplicationContext(),ConstantValue.UPDATE_FILE,ConstantValue.MOBLE_SAFE_PWD,"");
            if(s_confirm_pwd.equals(s_file_pwd)){
                //进入应用手机防盗模块
                Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
                startActivity(intent);

                //跳转到新页面之后，隐藏对话框
                dialog.dismiss();
            }  else {
                ToastUtil.showT(getApplicationContext(),ConstantValue.NOTICE_PWD_SAME);
            }
        }  else {
            //提示用户密码输入有为空的情况
            ToastUtil.showT(getApplicationContext(),ConstantValue.NOTICE_PWD);

        }
    }

    //初始化布局, 类似ListView
    private void initView() {
        //初始化RecyclerView
        RecyclerView recyslerview = (RecyclerView) findViewById(R.id.home_recycler_view);
        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        recyslerview.setLayoutManager(layoutmanager);
        //设置Adapter
        HomeBaseAdapter adapter = new HomeBaseAdapter(mlsit);
        Log.i(TAG,mlsit.toString());
        recyslerview.setAdapter(adapter);
    }

    //初始化数据
    private void initData() {
        String[] dataB = {"手机防盗","通讯卫士","软件管理","设备管理","手机防盗","手机防盗","手机防盗","高级工具","设置中心","WEB交互","管理权限","手机防盗","手机防盗","手机防盗","手机防盗","手机防盗",};
        for (int i = 0; i < 16; i++) {
            Book book01 = new Book(dataB[i],R.mipmap.ic_launcher);
            mlsit.add(book01);
        }
    }

}

