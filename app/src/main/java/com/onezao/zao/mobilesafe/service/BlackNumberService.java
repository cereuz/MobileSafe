package com.onezao.zao.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.onezao.zao.mobilesafe.db.dao.BlackNumberDao;
import com.onezao.zao.mobilesafe.utils.ConstantValue;

import java.lang.reflect.Method;

public class BlackNumberService extends Service{

    private SMSAbort receiver;// 短信拦截
    private BlackNumberDao mDao;// 数据库dao
    private TelephonyManager tm;// 电话服务
    private MyPhoneStateListener listener;// 电话状态监听

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建数据库dao
        mDao = BlackNumberDao.getInstance(this);
        // 服务创建时注册广播接收者
        // 创建广播接收器
        receiver = new SMSAbort();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(receiver, filter);

        // 得到电话服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 设置电话状态监听
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        Log.i(ConstantValue.TAG,"开启了服务。。。");
    }

    /**
     * 电话状态监听
     *
     * @author Administrator
     *
     */
    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:// 来电铃声状态
                    if (mDao.queryNumber(incomingNumber)) {
                        // 查询拦截模式
                        int mode = mDao.queryMode(incomingNumber);
                        if (2 == mode || 3 == mode) {
                            // 如果模式为电话或者全部拦截，则拦截此电话
                            abortCall();
                            //因为生成未接日志是异步的所以立即删除日志可能不成功
                            // 注册内容观察者删除对应的未接电话记录
                            Uri uri = Uri.parse("content://call_log/calls");
                            getContentResolver().registerContentObserver(uri, true,new MyContentObserver(new Handler(),incomingNumber) );
                        }
                    }
                    break;
            }
        }
    }
    /**
     * 内容观察者，观察未接电话
     */
    class MyContentObserver extends ContentObserver {
        String incomingNumber;
        public MyContentObserver(Handler handler,String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            super.onChange(selfChange);
            //调用删除未接电话的方法
            deleteRecord(incomingNumber);
            //删除后自动解除注册
            getContentResolver().unregisterContentObserver(this);
        }


    }
    /**
     * 短信拦截
     *
     * @author Administrator
     *
     */
    class SMSAbort extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 从Intent解析出短信
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
                // 得到发送短信的号码
                String phone = msg.getOriginatingAddress();
                // 将号码查询数据库

                if (mDao.queryNumber(phone)) {
                    // 查询拦截模式
                    int mode = mDao.queryMode(phone);
                    if (1 == mode || 3 == mode) {
                        // 如果模式为短信或者全部拦截，则拦截此短信
                        Log.i(ConstantValue.TAG,msg.getMessageBody());
                        abortBroadcast();
                    }
                }
            }

        }

    }

    /**
     * 电话拦截
     */
    public void abortCall() {
        //挂断电话
        try {
            //得到字节码
            Class clazz = BlackNumberService.class.getClassLoader().loadClass("android.os.ServiceManager");
            //从字节码里拿到方法
            Method method = clazz.getMethod("getService", String.class);
            //调用方法,拿到iBinder
            IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            //拷贝aidl，拿到Telephone代理对象
            ITelephony itel = ITelephony.Stub.asInterface(ibinder);
            //调用endcall方法
            itel.endCall();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 使用能容提供者删除未接电话
     * @param incomingNumber 要删除的未接电话
     */
    public void deleteRecord(String incomingNumber) {
        // 删除未接电话记录
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        resolver.delete(uri, "number=?", new String[]{incomingNumber});

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 服务销毁时注销广播接收者
        unregisterReceiver(receiver);
        receiver = null;
        // 服务销毁时注销监听
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        tm = null;
        listener = null;

        Log.i(ConstantValue.TAG,"关闭了服务。。。");
    }
}
