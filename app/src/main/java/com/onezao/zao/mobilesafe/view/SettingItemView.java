package com.onezao.zao.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onezao.zao.mobilesafe.R;

public class SettingItemView extends RelativeLayout {
    private static final String TAG = "ZAO";
    private static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.onezao.zao.mobilesafe";
    TextView tv_title;
    TextView tv_des;
    CheckBox cb_box;

    String destitle;
    String deson;
    String desoff;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml  --  view   将设置界面的一个条目转换成view对象，直接添加到了当前SettingItemView对应的View中
        View.inflate(context, R.layout.setting_item_view,this);
        //自定义组合控件中的子控件，如果需要操作，就需要在这里处理
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_des = (TextView)findViewById(R.id.tv_des);
        cb_box = (CheckBox)findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作，写在此处，AttributeSet attrs对象中获取
        initAttrs(attrs);
         //设置标题
        tv_title.setText(destitle);

    }

    /**
     *
     * @param attrs  构造方法中维护好的属性集合
     *               返回属性集合中自定义属性的属性值
     */
    private void initAttrs(AttributeSet attrs) {
        destitle = attrs.getAttributeValue(NAME_SPACE,"destitle");
        deson = attrs.getAttributeValue(NAME_SPACE,"deson");
        desoff = attrs.getAttributeValue(NAME_SPACE,"desoff");


        //打Log调试，
        Log.i(TAG,"attrs.getAttributeCount() = " + attrs.getAttributeCount());
        //获取属性名称以及属性值
        for(int i=0; i < attrs.getAttributeCount() ; i++){
            Log.i(TAG,"name = " + attrs.getAttributeName(i));
            Log.i(TAG,"value = " + attrs.getAttributeValue(i));
        }

    }

    /**
     *  checkBox
     * @return  返回当前SetttingItemView是否选中状态，
     *    true 开启（CheckBox返回true）    false关闭（CheckBox返回true）
     */
    public boolean isCheck(){
        //由CheckBox的选中结果，决定当前条目是否开启
         return cb_box.isChecked();
    }

    /**
     *
     * @param isCheck  是否作为开启的变量，由点击过程中去做传递
     */
    public void setCheck(boolean isCheck){
        //当前条目在选择的过程中，cb_box选中状态也在跟随（isCheck）变化
        cb_box.setChecked(isCheck);
        if(isCheck){
            //开启
//            tv_des.setText("自动更新已开启");
            tv_des.setText(deson);
        }  else  {
            //关闭
//            tv_des.setText("自动更新已关闭");
            tv_des.setText(desoff);
        }
    }
}
