package com.onezao.zao.mobilesafe.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import com.onezao.zao.mobilesafe.utils.ZaoUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SmsBackUp {
    private static int index = 0;

    /**
     * 备份短信
     * @param context  上下文
     * @param path     文件存储路径
     * @param progressDialog  进度条的显示
     */
    public static void backupSms(Context context, String path, ProgressDialog progressDialog) {
            Cursor cursor = null;
            FileOutputStream fos = null;
           //1.获取备份短信写入的文件
           File file = new File(path);
           //2.获取内容解析器，获取短信数据库中数据
        cursor = context.getContentResolver().query(Uri.parse("content://sms/")
                 ,new String[]{"address","date","type","body"},null,null,null);
        try {
          //3.文件相应的输出流
            fos = new FileOutputStream(file);
            //4.序列号数据库中读取的数据，放到xml文件中
            XmlSerializer newSerializer = Xml.newSerializer();
            //5.给此xml做相应设置
            newSerializer.setOutput(fos, "utf-8");
            //DTD(Xml规范)
            newSerializer.startDocument("utf-8",true);
            newSerializer.startTag(null,"smss");

            //6.备份短信总数的指定
            progressDialog.setMax(cursor.getCount());

            //7.读取数据库中的每一行的数据，写入xml中
            while (cursor.moveToNext()){
                newSerializer.startTag(null,"sms");

                newSerializer.startTag(null,"address");
                if(cursor.getString(0) == null){
                    newSerializer.text("未知地址");
                } else {
                    newSerializer.text(cursor.getString(0));
                }
                newSerializer.endTag(null,"address");

                newSerializer.startTag(null,"date");
                newSerializer.text(ZaoUtils.getDateToString(cursor.getString(1)));
                newSerializer.endTag(null,"date");

                newSerializer.startTag(null,"type");
                String type = cursor.getString(2);
                switch (type){
                    case "1" :
                        newSerializer.text("收信");
                        break;
                    case "2" :
                        newSerializer.text("发信");
                        break;
                    case "3" :
                      newSerializer.text("草稿");
                        break;
                }
                newSerializer.endTag(null,"type");

                newSerializer.startTag(null,"body");
                newSerializer.text(cursor.getString(3));
                newSerializer.endTag(null,"body");

                newSerializer.endTag(null,"sms");

                //8.每读取一条短信，就让进度条叠加
                index++;
                try {
               //短信比较少的时候，睡眠一下，可以看的更清晰
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.setProgress(index);
            }
            newSerializer.endTag(null,"smss");
            newSerializer.endDocument();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(cursor != null && fos != null){
                cursor.close();
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
