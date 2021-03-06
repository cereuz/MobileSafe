package com.onezao.zao.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.R;
import com.onezao.zao.mobilesafe.adapter.BlackNumberAdapter;
import com.onezao.zao.mobilesafe.db.dao.BlackNumberDao;
import com.onezao.zao.mobilesafe.db.domain.BNAppInfo;
import com.onezao.zao.mobilesafe.home.OnItemClickListener;
import com.onezao.zao.mobilesafe.utils.ToastUtil;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackNumberPagingActivity extends AppCompatActivity {

    List<BNAppInfo> mBlackNumberInfoList = new ArrayList<BNAppInfo>();
    BlackNumberDao mDao;
    BlackNumberAdapter mAdapter;
    RecyclerView mRecyclerView;
    private int mode = 1;
    private int index = 0;// 每次查询的起始位置
    private int total = 0;// //数据库中黑名单总数
    private boolean mIsLoad = false;// 加载状态
    private int firstIndex = 0;  //首次查询数据的起始序号
    int mCount;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //如果没有初始化，就做第一次初始化操作，不然就只需要更新就可以了
                    if(mAdapter == null){
                      initRV(mBlackNumberInfoList);
                    }  else {
                      mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    //4.通知适配器，数据改变了。
                    mAdapter.notifyDataSetChanged();
                    Log.i("Zao", "自动添加数据成功！" + ZaoUtils.getSystemTimeMore(1));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);

        initUI();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //获取操作黑名单数据库的对象
                mDao = BlackNumberDao.getInstance(getApplicationContext());
                //2.查询获取到所有数据   //传入参数表示查询部分数据，开始做分页查询
                mBlackNumberInfoList = mDao.find(firstIndex);
                //2.1 获取数据库的数据条目数
                mCount = mDao.queryTotal();
                //3.通过消息机制，通知主线程可以使用获取到的数据
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化其他控件事件
     */
    private void initUI() {
        //通过findViewById拿到RecyclerView实例
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_blacknumber);

        Button btn_blacknumber_add = (Button) findViewById(R.id.btn_blacknumber_add);
        TextView tv_blacknumber = (TextView) findViewById(R.id.tv_blacknumber);
        /**
         * 设置添加按钮的点击事件
         */
        btn_blacknumber_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        /**
         * 设置titel的点击事件
         */
        tv_blacknumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(), "自动添加数据", Toast.LENGTH_SHORT).show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //批量添加数据
                        addData();
                    }
                }.start();
                return false;
            }
        });

        /**
         * 监听RecycleView的滚动状态
         */
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            //滚动过程中，状态发生改变的时候调用该方法
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /**
                 * 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                 */
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();

//                RecyclerView.SCROLL_STATE_IDLE; //飞速滚动
//                RecyclerView.SCROLL_STATE_DRAGGING;//拖拽状态
//                RecyclerView.SCROLL_STATE_SETTLING;  //设置数据状态
                    /**
                     * 条件一：滚动到停止状态
                     * 条件二：最后一个条目可见（最后一个条目的索引值 >= 数据适配器中集合的总条目数 - 1）
                     */
                if(newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition >= mBlackNumberInfoList.size() -1
                        && !mIsLoad){
                    /**
                     * 1.mIsLoad防止重复加载而设置的变量
                     * 2.如果当前正在加载mIsLoad就会为true，本次加载完毕后，再将mIsLoad改为false
                     * 3. 下一次加载需要去做执行的时候，会判断上述mIsLoad变量是否为false，
                     * 4. mIsLoad 如果为true，就需要等待上一次加载完成，将其值改为false之后再去加载
                     */
                    //如果条目总数大于集合大小的时候，才可以继续加载更多
               if(mCount > mBlackNumberInfoList.size()){
                     //加载下一页数据
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //获取操作黑名单数据库的对象
                            mDao = BlackNumberDao.getInstance(getApplicationContext());
                            //2.查询获取到所有数据   //传入参数表示查询部分数据，开始做分页查询
                            List<BNAppInfo> mData = mDao.find(mBlackNumberInfoList.size() + firstIndex);
                            //3.添加下一页数据的过程
                            mBlackNumberInfoList.addAll(mData);
                            //3.通过消息机制，通知主线程可以使用获取到的数据
                            mHandler.sendEmptyMessage(0);
                        }
                    }.start();

                    }  else {
                   ToastUtil.showT(getApplicationContext(),"没有更多数据了！");
               }
                    }
                }
            }
            //滚动过程中，调用该方法
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 弹出添加黑名单的对话框
     */
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);


        final EditText et_add_phone = (EditText) view.findViewById(R.id.et_add_phone);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);

        Button btn_add_cancel = (Button) view.findViewById(R.id.btn_add_cancel);
        Button btn_add_commit = (Button) view.findViewById(R.id.btn_add_commit);

        /**
         *   监听RadioGroup中的RadioButton的选中切换过程
         */

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_add_sms:
                        //拦截短信
                        mode = 1;
                        break;

                    case R.id.rb_add_phone:
                        //拦截电话
                        mode = 2;
                        break;

                    case R.id.rb_add_all:
                        //拦截所有
                        mode = 3;
                        break;
                }
            }
        });

        /**
         * 监听弹框的确认按钮的点击事件
         */
        btn_add_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.获取输入框中的号码
                String phone = et_add_phone.getText().toString().trim();
                String time = ZaoUtils.getSystemTimeMore(1);
                if (!TextUtils.isEmpty(phone)) {
                    //2.向数据库中插入当前写的电话号码
                    mDao.insert(phone, mode + "", time);
                    //3.直接往集合中添加新添加的对象
                    addInfoToList(phone, mode, time);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入电话号码", Toast.LENGTH_SHORT).show();
                }

                //4.通知适配器，数据改变了。
                mAdapter.notifyDataSetChanged();
                //5.隐藏对话框
                dialog.dismiss();
            }
        });

        /**
         * 监听弹框的取消按钮的点击事件
         */
        // 给取消键设置监听
        btn_add_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 取消则隐藏
                dialog.dismiss();
            }
        });

        dialog.setView(view, 0, 0, 0, 0);
        //显示弹框
        dialog.show();
    }

    /**
     * 3.直接往集合中添加新添加的对象，默认添加到第一个
     *
     * @param phone 对象的手机号码
     * @param mode  对象的模式
     */
    private void addInfoToList(String phone, int mode, String time) {
        BNAppInfo info = new BNAppInfo();
        info.setPhone(phone);
        info.setMode(mode + "");
        info.setTime(time);
        mBlackNumberInfoList.add(0, info);
    }

    /**
     * 直接往集合中添加新添加的对象,带序号的
     *
     * @param index
     * @param phone
     * @param mode
     * @param time
     */
    private void addInfoToList(int index, String phone, int mode, String time) {
        BNAppInfo info = new BNAppInfo();
        info.setPhone(phone);
        info.setMode(mode + "");
        info.setTime(time);
        mBlackNumberInfoList.add(index, info);
    }

    /**
     * 自动添加数据，并更新
     */
    private void addData() {
        BlackNumberDao dao = BlackNumberDao.getInstance(this);

        for (int i = 0; i < 100; i++) {
            String phone;
            int mode;
            String time;
            if (i < 10) {
                phone = "1328238000" + i;
                mode = new Random().nextInt(3) + 1;
                time = ZaoUtils.getSystemTimeMore(1);
                //往数据库插入数据
                dao.insert(phone, String.valueOf(mode), time);
                //3.直接往集合中添加新添加的对象
                addInfoToList(phone, mode, time);
                ZaoUtils.sleep(20);
            } else if (i < 100) {
                phone = "132823800" + i;
                mode = new Random().nextInt(3) + 1;
                time = ZaoUtils.getSystemTimeMore(1);
                //往数据库插入数据
                dao.insert(phone, String.valueOf(mode), time);
                //3.直接往集合中添加新添加的对象
                addInfoToList(phone, mode, time);
                //睡眠，
                ZaoUtils.sleep(20);
            } else if (i < 1000) {
                phone = "13282380" + i;
                mode = new Random().nextInt(3) + 1;
                time = ZaoUtils.getSystemTimeMore(1);
                //往数据库插入数据
                dao.insert(phone, String.valueOf(mode), time);
                //3.直接往集合中添加新添加的对象
                addInfoToList(phone, mode, time);
                ZaoUtils.sleep(10);
            }
        }


        mHandler.sendEmptyMessage(1);

        /**
         * 复制数据库文件到SD卡
         */
        ZaoUtils.copyDBtoSD(getApplicationContext());
    }

    //初始化界面显示
    private void initRV(final List<BNAppInfo> mBlackNumberInfoList) {

        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new BlackNumberAdapter(mBlackNumberInfoList);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * 打印集合的数据
         */
        for (BNAppInfo info : mBlackNumberInfoList) {
            Log.i("Zao", "Phone = " + info.getPhone() + "  mode = " + info.getMode() + " time = " + info.getTime());
        }

        //设置条目点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //处理条目的数据
                initItemData(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        //设置ImageView的点击事件
        mAdapter.setOnItemImageViewClickListener(new BlackNumberAdapter.ItemImageViewInterface() {
            @Override
            public void onclick(View view, int position) {
                Toast.makeText(getApplicationContext(), "删除 : " + position, Toast.LENGTH_SHORT).show();
                //从数据库中删除数据
                mDao.delete(mBlackNumberInfoList.get(position).getPhone());
                //从集合中删除数据
                mBlackNumberInfoList.remove(position);
                //4.通知适配器，数据改变了。
                mAdapter.notifyDataSetChanged();
            }
        });

        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initItemData(int position) {
        String phone = mBlackNumberInfoList.get(position).getPhone();
        String mode = mBlackNumberInfoList.get(position).getMode();
        String time = mBlackNumberInfoList.get(position).getTime();
        ToastUtil.showT(getApplicationContext(), "phone = " + phone + "\nmode = " + mode + "\ntime = " + time);

        modifyItem(position, phone, mode);

        //3. 获取信息给第三个导航界面使用
        Intent intent = new Intent();
        intent.putExtra("phone", phone);
        setResult(0, intent);

/*        //关闭当前页面
        finish();*/
    }

    /**
     * 修改条目的数据
     *
     * @param position
     * @param phone
     * @param mode
     */
    private void modifyItem(final int position, final String phone, String mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);


        final EditText et_add_phone = (EditText) view.findViewById(R.id.et_add_phone);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);

        Button btn_add_cancel = (Button) view.findViewById(R.id.btn_add_cancel);
        Button btn_add_commit = (Button) view.findViewById(R.id.btn_add_commit);

        RadioButton rb_add_sms = (RadioButton) view.findViewById(R.id.rb_add_sms);
        RadioButton rb_add_phone = (RadioButton) view.findViewById(R.id.rb_add_phone);
        RadioButton rb_add_all = (RadioButton) view.findViewById(R.id.rb_add_all);

        et_add_phone.setText(phone);
        rb_add_sms.setChecked(false);
        rb_add_phone.setChecked(false);
        rb_add_all.setChecked(false);
        int Nmode = Integer.parseInt(mode);
        switch (Nmode) {
            case 1:
                rb_add_sms.setChecked(true);
                break;
            case 2:
                rb_add_phone.setChecked(true);
                break;
            case 3:
                rb_add_all.setChecked(true);
                break;
        }

        /**
         *   监听RadioGroup中的RadioButton的选中切换过程
         */

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_add_sms:
                        //拦截短信
                        BlackNumberPagingActivity.this.mode = 1;
                        break;

                    case R.id.rb_add_phone:
                        //拦截电话
                        BlackNumberPagingActivity.this.mode = 2;
                        break;

                    case R.id.rb_add_all:
                        //拦截所有
                        BlackNumberPagingActivity.this.mode = 3;
                        break;
                }
            }
        });

        /**
         * 监听弹框的确认按钮的点击事件
         */
        btn_add_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.获取输入框中的号码
                String Nphone = et_add_phone.getText().toString().trim();
                String time = ZaoUtils.getSystemTimeMore(1);
                if (!TextUtils.isEmpty(Nphone)) {
                    //2.向数据库中插入当前写的电话号码
                    mDao.update(phone, Nphone, BlackNumberPagingActivity.this.mode + "", time);
                    //先移除集合中的数据
                    mBlackNumberInfoList.remove(position);
                    //3.直接往集合中添加新添加的对象，按照点击的位置来添加
                    addInfoToList(position, Nphone, BlackNumberPagingActivity.this.mode, time);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入电话号码", Toast.LENGTH_SHORT).show();
                }

                //4.通知适配器，数据改变了。
                mAdapter.notifyDataSetChanged();
                //5.隐藏对话框
                dialog.dismiss();
            }
        });

        /**
         * 监听弹框的取消按钮的点击事件
         */
        // 给取消键设置监听
        btn_add_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 取消则隐藏
                dialog.dismiss();
            }
        });

        dialog.setView(view, 0, 0, 0, 0);
        //显示弹框
        dialog.show();
    }
}
