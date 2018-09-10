package com.onezao.zao.mobilesafe.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.adapter.AppInfoMoreAdapter;
import com.onezao.zao.mobilesafe.bean.AppInfo;
import com.onezao.zao.mobilesafe.engine.AppInfoProvider;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AppManagerMoreActivity extends AppCompatActivity implements View.OnClickListener{

        TextView tv_appinfo_title;
        TextView tv_disk_inuse;
        TextView tv_sd_inuse;
        TextView tv_appinfo_second_title;
        RecyclerView rv_appinfo;

        List<AppInfo> mAppInfoList;
        List<AppInfo> mCustomerList;
        List<AppInfo> mSystemList;
        AppInfoMoreAdapter mAdapter;
        AppInfo mAppInfo;

        TextView tv_share;
        TextView tv_uninstall;
        TextView tv_start;
        TextView tv_setting;
        PopupWindow popupWindow;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //初始化recycleView的显示
            initAPPRV(mAppInfoList,mSystemList,mCustomerList);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager_view);

        //初始化界面
        initUI();
        //初始化磁盘可用和Sd卡可用空间
        initInUseSpace();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showT(this,"onResume");
        //初始化手机应用管理的数据
        initAppData();
    }

    /**
     * 初始化应用的数据
     */
    private void initAppData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                //分割系统应用和用户应用
                mCustomerList = new ArrayList<AppInfo>();
                mSystemList = new ArrayList<AppInfo>();
                for(AppInfo appInfo : mAppInfoList){
                    if(!appInfo.isSystem()){
                        mCustomerList.add(appInfo);
                    } else {
                        mSystemList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化RecycleView的内容以及显示  mCustomerList  mSystemList
     */
    private void initAPPRV(final List<AppInfo> mAppInfoList, final List<AppInfo> mSystemList, final List<AppInfo> mCustomerList) {

        //设置RecyclerView管理器
        rv_appinfo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new AppInfoMoreAdapter(mAppInfoList,mSystemList,mCustomerList);
        //设置添加或删除item时的动画，这里使用默认动画
        rv_appinfo.setItemAnimator(new DefaultItemAnimator());

        /**
         * 打印集合的数据
         */
/*        for(AppInfo info : mAppInfoList){
            Log.i("Zao","Name = " + info.getName() + "  PackageName = " + info.getPackageName() + " Icon = " + info.getIcon() + "isSDcard : " + info.isSdcard() + "isSystem : " + info.isSystem());
        }*/

        //设置条目点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //这里的数据，需要与Adapter对应起来。
                if(position == 0 || position == mCustomerList.size() + 1){
                    return;
                } else if(position > 0 && position < mCustomerList.size()) {
                    //返回用户应用对应的list
                    mAppInfo = mCustomerList.get(position - 1);



                    String text = "\nName : " + mAppInfo.getName() + "\nPackageName : " + mAppInfo.getPackageName() + "\nIcon : " + mAppInfo.getIcon();
//                    Toast.makeText(getApplicationContext(), "点击了条目 : " + position + text, Toast.LENGTH_SHORT).show();

                } else if(position > mCustomerList.size() + 1) {
                    //返回系统应用条目对应的list
                    mAppInfo = mSystemList.get(position - mCustomerList.size() - 2);

                    String text = "\nName : " + mAppInfo.getName() + "\nPackageName : " + mAppInfo.getPackageName() + "\nIcon : " + mAppInfo.getIcon();
//                    Toast.makeText(getApplicationContext(), "点击了条目 : " + position + text, Toast.LENGTH_SHORT).show();
                }

                //弹出窗体
                showPopupWindow(view);
/*                //处理条目的数据
                initItemData(position);*/
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        //设置ImageView的点击事件
        mAdapter.setOnItemImageViewClickListener(new AppInfoMoreAdapter.ItemImageViewInterface(){
            @Override
            public void onclick(View view, int position) {
                Toast.makeText(getApplicationContext(),"点击了小图标 : " + position ,Toast.LENGTH_SHORT).show();
/*                //从数据库中删除数据
                mAppInfoList.delete(mAppInfoList.get(position).getPhone());
                //从集合中删除数据
                mAppInfoList.remove(position);
                //4.通知适配器，数据改变了。
                mAdapter.notifyDataSetChanged();*/
            }
        });

        //监控滚动状态，并对应修改数据
        rv_appinfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //dx为RecyclerView沿着X轴(横向)滑动时偏移量.
                //正数为正向滑动(向右)偏移量,负数为反向滑动(向左)偏移量

                //dy为RecyclerView沿着y轴(纵向)滑动时偏移量.
                //正数为正向滑动(向下)偏移量,负数为反向滑动(向上)偏移量

                Log.e("tag", "onScrolled: ---------- dx:"+dx+" dy:"+dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                //firstVisibleItem 为RecyclerView 可见的第一item的position
                int lastVisibleItem = ((LinearLayoutManager) manager).
                        findLastVisibleItemPosition();

                //lastVisibleItem 为RecyclerView 可见的最后一个item的position
                int firstVisibleItem = ((LinearLayoutManager) manager).
                        findFirstVisibleItemPosition();

                //visibleItemCount 为RecyclerView 当前可见item的数量
                int visibleItemCount = manager.getChildCount();

                //totalItemCount 为RecyclerView 的所有item的总数量
                int totalItemCount = manager.getItemCount();

                Log.e("test", "onScrolled: ------- firstVisibleItem:" +firstVisibleItem
                        +" lastVisibleItem: " +lastVisibleItem
                        +" visibleItemCount: "+visibleItemCount
                        +" totalItemCount: " +totalItemCount);

                   // 在滚动过程中调用方法
                if(firstVisibleItem >= mCustomerList.size() + 1){
                   //滚动到了系统应用
                   tv_appinfo_second_title.setText("系统应用" + "( " + mSystemList.size() + " 个)");
                }  else {
                   //滚动到了用户应用
                   tv_appinfo_second_title.setText("用户应用" + "( " + mCustomerList.size() + " 个)");

                }

            }
        });

        //设置适配器
        rv_appinfo.setAdapter(mAdapter);
    }

    /**
     * 显示弹出框
     */
    private void showPopupWindow(View view) {
         View popupView = View.inflate(this,R.layout.popupwindow_appmanager_view,null);
        tv_share = (TextView)popupView.findViewById(R.id.tv_share);
        tv_uninstall = (TextView)popupView.findViewById(R.id.tv_uninstall);
        tv_start = (TextView)popupView.findViewById(R.id.tv_start);
        tv_setting = (TextView)popupView.findViewById(R.id.tv_setting);

        tv_share.setOnClickListener(this);
        tv_uninstall.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_setting.setOnClickListener(this);

        //透明动画（透明 -- > 不透明）
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(ConstantValue.ONE_SECOND);
        alphaAnimation.setFillAfter(true);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0,1,
                0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        scaleAnimation.setDuration(ConstantValue.ONE_SECOND);
        scaleAnimation.setFillAfter(true);
        //动画集合set
        AnimationSet animationSet = new AnimationSet(true);
        //添加两个动画
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        //1.创建窗体对象，指定宽高
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        //2.设置一个透明背景图片，如果不设值，返回键会不响应
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        //3.指定窗体位置
        popupWindow.showAsDropDown(view,300,-view.getHeight() - 30);
        //执行动画集合set
        popupView.startAnimation(animationSet);
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        tv_appinfo_title = (TextView)findViewById(R.id.tv_appinfo_title);
        tv_appinfo_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.showT(getApplicationContext(),"长按 !!!");
                startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
                return false;
            }
        });


        tv_disk_inuse = (TextView)findViewById(R.id.tv_disk_inuse);
        tv_sd_inuse = (TextView)findViewById(R.id.tv_sd_inuse);
        tv_appinfo_second_title = (TextView)findViewById(R.id.tv_appinfo_second_title);
        rv_appinfo = (RecyclerView) findViewById(R.id.rv_appinfo);
    }

    /**
     * 初始化
     */
    private void initInUseSpace() {
         // 调用方法的到结果设置显示内存和sd信息
        // 显示内存大小
        tv_disk_inuse.setText("内存可用:"
                + Formatter.formatFileSize(this, Environment.getDataDirectory()
                .getFreeSpace()));
        // 显示sd卡大小
        tv_sd_inuse.setText("SD卡可用:"
                + Formatter.formatFileSize(this, Environment
                .getExternalStorageDirectory().getFreeSpace()));
    }

    @Override
    public void onClick(View view) {
                 Intent intent;
         switch (view.getId()){
             case R.id.tv_uninstall :
                 if(mAppInfo == null || mAppInfo.isSystem()){
                     ToastUtil.showT(this,"该应用不可以卸载");
                 } else {
                     if(Intent.ACTION_DELETE != null && Intent.CATEGORY_DEFAULT != null) {
                         // 发送意图给包安装器
                         intent = new Intent(Intent.ACTION_DELETE);
                         intent.addCategory(Intent.CATEGORY_DEFAULT);
                         intent.setData(Uri.parse("package:"
                                 + mAppInfo.getPackageName()));
                         startActivity(intent);
                     }
                 }
                 break;

             case R.id.tv_share :
                 ToastUtil.showT(this,"分享");
                 //发送信息发送意图给短信app
                 intent = new Intent(Intent.ACTION_SEND);
                 intent.addCategory(Intent.CATEGORY_DEFAULT);
                 intent.setType("text/plain");
                 //信息内容
                 intent.putExtra(Intent.EXTRA_TEXT, "好消息，好消息["
                         + mAppInfo.getName()
                         + "]非常好用，你值得拥有，下载地址:http://www.zao.com/app/"
                         + mAppInfo.getPackageName());
                 startActivity(intent);

                 break;

             case R.id.tv_start :
                 ToastUtil.showT(this,"启动");
                 // 使用包管理器传入包名得到要启动的应用的意图
                 intent = getPackageManager().getLaunchIntentForPackage(mAppInfo.getPackageName());
                 if (intent!=null) {
                     //如果找到意图就启动
                     startActivity(intent);
                 }else {
                     //没有则提示用户
                     Toast.makeText(getApplicationContext(), "该应用没有启动界面", 0).show();
                 }

                 break;

             case R.id.tv_setting :
                 ToastUtil.showT(this,"设置");
                 /**
                  * 给设置设置监听
                  *
                  */
                 //发送安装信息意图给设置app
                 intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                 intent.addCategory(Intent.CATEGORY_DEFAULT);
                 //包名
                 intent.setData(Uri.parse("package:"+mAppInfo.getPackageName()));
                 startActivity(intent);
                 break;
         }
         if(popupWindow != null){
             popupWindow.dismiss();
         }
    }
}
