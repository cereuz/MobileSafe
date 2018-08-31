package com.onezao.zao.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;

public class SettingClickView extends RelativeLayout {
    private static final String TAG = "ZAO";
    TextView tv_title;
    TextView tv_des;

    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml  --  view   将设置界面的一个条目转换成view对象，直接添加到了当前SettingItemView对应的View中
        View.inflate(context, R.layout.setting_click_view,this);
        //自定义组合控件中的子控件，如果需要操作，就需要在这里处理
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_des = (TextView)findViewById(R.id.tv_des);
    }

    /**
     * 设置标题内容
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * 设置描述内容
     * @param des
     */
    public void setDes(String des){
        tv_des.setText(des);
    }
}
