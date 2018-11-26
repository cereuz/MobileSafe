package com.onezao.zao.mobilesafe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.onezao.zao.mobilesafe.home.HomeActivity;
import com.onezao.zao.mobilesafe.utils.ConstantValue;
import com.onezao.zao.mobilesafe.utils.DownloadUtil;
import com.onezao.zao.mobilesafe.utils.LogZ;
import com.onezao.zao.mobilesafe.utils.SpUtils;
import com.onezao.zao.mobilesafe.utils.StreamUtil;
import com.onezao.zao.mobilesafe.utils.ToastUtil;
import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 1、在A里面设置一个静态的变量instance,初始化为this

 public static A instance = null；

 void onCreate(){
 instance = this;

 }

 2、在需要关闭页面的地方调用A.instance.finish();
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "ZAO";

    TextView tv_splash;
    TextView textViewHello;
    long mVersionCode;
    String mVersionDes;
    String downloadUrl;
    ProgressBar progressBar;
    ConstraintLayout cl_layout;

    //handler处理事件
    private static final int UPDAT_VERSION = 100;
    private static final int ENTER_HOME = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;

    private static final int DOWNLOAD_FILE_SUCCESS = 201;
    private static final int DOWNLOAD_FILE_FAILED = 202;

    //动态申请权限
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    final String positive = "获取权限" ;
    final String negative = "退出";

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    //设置一个静态的变量instance,
    public static SplashActivity instance = null;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDAT_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面，Activity跳转
                    enterHome();
                    break;
                 case URL_ERROR:
                    //网络链接错误
                     ToastUtil.showT(SplashActivity.this,"URL异常");
                     //取消对话框，进入应用程序主界面，Activity跳转
                     enterHome();
                    break;
                 case IO_ERROR:
                    //文件读写错误
                     ToastUtil.showT(SplashActivity.this,"IO读取异常");
                     //取消对话框，进入应用程序主界面，Activity跳转
                     enterHome();
                    break;
                 case JSON_ERROR:
                    //JSON数据有误
                    ToastUtil.showT(SplashActivity.this,"JSO数据解析异常");
                     //取消对话框，进入应用程序主界面，Activity跳转
                     enterHome();
                    break;
                case DOWNLOAD_FILE_SUCCESS:
                    //JSON数据有误
                    ToastUtil.showT(SplashActivity.this,"下载成功");
                    break;
                case DOWNLOAD_FILE_FAILED:
                    //JSON数据有误
                    ToastUtil.showT(SplashActivity.this,"下载失败");
                    //进入应用程序主界面，Activity跳转
                    enterHome();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 点击桌面图标，重新进入，不会重启
         */
        if (!isTaskRoot()) {
            finish();
            return;
        }

/*        //去除当前Activity的头部.  去除当前Activity，一个一个的去除 。 Eclipse上可以使用
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_splash);

        //初始化定义的instance为this
        instance = this;

        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化 系统权限
        initPermission();
        //初始化动画效果
        initAnimation();
        //初始化数据库
        initDB();

        initInput();
    }

    /**
     * 打印手机系统的输入法
     */
    private void initInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> methodList = imm.getInputMethodList();
        for (InputMethodInfo info : methodList){
            LogZ.e(info.getId() + " ---- " + info.getPackageName() + " ---- " + info.toString() + "\n");
        }
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        //1.归属地的数据库的拷贝过程
        initAddressDB("address.db");
        //2.初始化常用号码数据库的拷贝过程
        initAddressDB("commonnum.db");
    }

    /**
     * 拷贝数据库值到file文件夹下
     * @param dbName 数据库名称
     */

    private void initAddressDB(String dbName) {
        //1.在file文件夹下创建同名dbName数据库文件过程
        File files = getFilesDir();
        File file = new File(files,dbName);
/*        File files = Environment.getExternalStorageDirectory();
        File file  = new File(files,"/ame/" + dbName);*/
         if(file.exists()){
             return;
         }
         //2.读取第三方资产目录下的文件
          FileOutputStream fos = null;
          InputStream stream = null;
        try {
            stream = getAssets().open(dbName);
            //3.将读取的内容写入到指定文件夹的文件中去
            fos = new FileOutputStream(file);
            //4.每次读取内容的大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = stream.read(bs)) != -1){
                fos.write(bs,0,temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
             if(fos != null && stream != null){
                 try {
                     stream.close();
                     fos.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        }
    }

    //实现启动页淡入淡出效果
    private void initAnimation() {
        AlphaAnimation  aa = new AlphaAnimation(0,1);
        aa.setDuration(4000);
        cl_layout.startAnimation(aa);
    }

    /**
     *  初始化UI界面
     */
    private void initUI() {
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        textViewHello = (TextView) findViewById(R.id.textViewHello);
        //进度条
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        //整体布局
        cl_layout = (ConstraintLayout) findViewById(R.id.cl_layout);
    }

    /**
     *  初始化数据
     */
    private void initData() {
        //1.应用版本名称 , 版本编号 , 手机系统当前时间日期
        String versionName = getVersionName();

        mVersionCode = getVersionCode();
        String today = ZaoUtils.getSystemTimeHello();
        //设置时间，日期
        textViewHello.setText(today);
        //设置版本名称
        tv_splash.setText(versionName);

        //判断本地存储的是否自动更新状态，
    if(SpUtils.getBoolean(this, ConstantValue.UPDATE_FILE,ConstantValue.OPEN_UPDATE,false)){
        //获取服务器版本号（客户端发请求，服务端响应，（JSON,XML））
        checkVersion();
        }  else {
             //如果没有保存  SP ，则什么也不做。
//           mHandler.sendEmptyMessageDelayed(ENTER_HOME,ConstantValue.SIX_SECOND);
        }
    }


    /**Empty
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框，是依赖于Activity存在的。  this不可以少！！！
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        //设置标题和描述内容
        builder.setTitle(ZaoUtils.DIALOG_TITLE);
        builder.setMessage(mVersionDes);
        //确定按钮，立即更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载APK文件
                downloadAPK(downloadUrl);
            }
        });
        //消极按钮，稍后再说
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消对话框，进入应用程序主界面，Activity跳转
                enterHome();
            }
        });

        //点击取消按钮 监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //取消对话框，进入应用程序主界面，Activity跳转
                enterHome();
                //取消dialog
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    /**
     * 下载文件
     * @param url  下载地址
     */
    private void downloadAPK(String url) {
        DownloadUtil.get().download(url, "ame", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final File file) {
                //子线程不能修改UI ，需要在主线程Toast
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                ToastUtil.showT(SplashActivity.this, "下载完成");
                    //安装apk
                    installAPK(file);
                    }
                });
                Log.i(TAG,"下载完成" + ZaoUtils.getSystemTime());
            }
            @Override
            public void onDownloading(int progress) {
                progressBar.setProgress(progress);
            }
            @Override
            public void onDownloadFailed() {
//                ToastUtil.showT(SplashActivity.this, "下载失败");
                Log.i(TAG,"下载失败");
            }
        });
    }

    // 下载APK文件之后，安装文件
    private void installAPK(File file) {
        // 兼容7.0    Android 7.0 系统共享文件需要通过 FileProvider 添加临时权限，否则系统会抛出 FileUriExposedException .
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),"com.onezao.zao.mobilesafe.fileprovider",file);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
            //调用安装APP的Activity
            startActivityForResult(intent, 0);
        } else {
            // 版本 N以下的，这样处理
            Log.e("OpenFile", file.getName());
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
        /*        intent.setData(Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");*/
            //上下两种 二选一
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            startActivityForResult(intent, 0);
        }
    }

    //开启一个Activity之后，返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //进入应用程序主界面，Activity跳转
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    //跳转到应用主界面 HomeActivity
    private void enterHome() {
        Intent intent =  new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    /**
     *  通过请求网络，获取版本号，检测是否有版本更新。
     */
    private void checkVersion() {
        //联网必须要在子线程处理，不然会有线程阻塞。这里注意要获取网络权限
        new Thread(){
            @Override
            public void run() {

                //睡眠几秒钟，使SplashActivity显示出来
                try {
                    Thread.sleep(ConstantValue.ONE_SECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //将子线程的数据发送到主线程，通过handler处理
//                Message message = new Message();
                Message message = Message.obtain();

                //发送请求，获取数据，参数为请求json的链接地址
                try {
                    //1.封装 url 地址
                  URL url = new URL(ZaoUtils.CHECK_VERSION_JSON_URL);
                  //2 . 开启一个连接
                    HttpURLConnection connection =(HttpURLConnection)url.openConnection();
                    //3. 设置常见请求头参数（请求头）
                       //请求超时
                    connection.setConnectTimeout(ZaoUtils.CHECKVERSIONCONTIME);
                       //连接超时
                    connection.setReadTimeout(ZaoUtils.CHECKVERSIONCONTIME);
                       //请求方式，默认是get
//                    connection.setRequestMethod("POST");
                    //4. 获取请求成功响应码，并对应做处理
                    if(connection.getResponseCode() == 200){
                        //5. 以流的形式，将数据读取下来
                        InputStream is = connection.getInputStream();
                        //6. 将流转换成字符串 （工具类封装）
                        String json = StreamUtil.stream2String(is);
                          //打印json数据
                        Log.i(TAG,json);
                        //7.解析JSON数据
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");

                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        downloadUrl = jsonObject.getString("downloadUrl");
                        //打印解析的json内数据
                        Log.i(TAG,versionName);
                        Log.i(TAG,mVersionDes);
                        Log.i(TAG,versionCode);
                        Log.i(TAG,downloadUrl);

                        //8.比对json的版本号和本地APK的版本号。（如果网络版本号更大，提示用户更新。）
                        if(mVersionCode < Integer.parseInt(versionCode)){
                            // 如果本地版本号小于线上版本，弹出对话框，提示用户是否下载  。  UI ，消息机制
                            message.what = UPDAT_VERSION;
                        } else {
                            //如果不小于，则直接进入主界面
                            message.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = JSON_ERROR;
                } finally {
                    //
                    mHandler.sendMessage(message);
                }
            }
        }.start();

/*        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });*/
    }

    //获取版本名称
    public String getVersionName() {
        //1.包管理者对象 PackageManager
        PackageManager pm = getPackageManager();
        //2. 从包的管理者对象中，获取指定包名的基本信息（版本名称，版本号）,flag传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
//            return packageInfo.versionName + "\n" + packageInfo.packageName;
            return packageInfo.versionName ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

    //获取版本号
    public int getVersionCode() {
        //1.包管理者对象 PackageManager
        PackageManager pm = getPackageManager();
        //2. 从包的管理者对象中，获取指定包名的基本信息（版本名称，版本号）,flag传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
//            return packageInfo.versionName + "\n" + packageInfo.packageName;
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  0;
    }

    //点击imageView检查更新,  主动监测是否更新
    public void imageView(View view) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
        //获取服务器版本号（客户端发请求，服务端响应，（JSON,XML））
            checkVersion();
        } else {
            ToastUtil.showT(this,"请等待1秒钟后再次点击。");
        }
    }


    //高版本手机授予安全权限
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            return;
        }
    }

    public void checkPermission() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[] {
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.PROCESS_OUTGOING_CALLS,

                        Manifest.permission.VIBRATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
/*                        Manifest.permission.FLASHLIGHT,
                        Manifest.permission.READ_HISTORY_BOOKMARKS,*/
//                        Manifest.permission.CAMERA
                }
        );
        // 如果这3个权限全都拥有, 则直接执行读取短信代码
        if (isAllGranted) {
/*            getData();
            simpleAdapter.notifyDataSetChanged();*/
            toast("所有权限已经授权！");
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.PROCESS_OUTGOING_CALLS,

                        Manifest.permission.VIBRATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        Manifest.permission.CAMERA
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }
    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                toast("检查权限");
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行读取短信代码
/*                getData();
                simpleAdapter.notifyDataSetChanged();*/
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
//                openAppDetails();
                toast("需要授权！");
                myPermissionDialog();
            }
        }
    }
    public void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }

    //点击按钮，弹出一个普通对话框
    public void myPermissionDialog(){
        // 通过builder 构建器来构造
        final AlertDialog.Builder   builder =  new AlertDialog.Builder(this);
        builder.setTitle("警告zz");
        builder.setMessage("世界上最遥远的距离是没有网络！");
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("点击了确定按钮");
                Toast.makeText(SplashActivity.this, positive, Toast.LENGTH_SHORT).show();
                initPermission();
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("点击了取消按钮");
                Toast.makeText(SplashActivity.this, negative, Toast.LENGTH_SHORT).show();
                //关闭Activity
                SplashActivity.this.finish();
            }
        });
        builder.setCancelable(false);  //设置获取权限框不可以取消。
        //最后一步，一定要记得和Toast一样，show出来。
        builder.show();
    }
}
