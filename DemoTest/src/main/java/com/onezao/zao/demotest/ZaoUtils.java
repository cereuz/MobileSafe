package com.onezao.zao.demotest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 蜗牛
 *  pathSD  :   获取SD卡路径
 */
public class ZaoUtils {
    private static final Logger LOGGER = LogManager.getLogger(ZaoUtils.class);
    public static String ONEZAO = "onezao";

    //复制文件
    public static void copyFile(String path1,String path2) {
        // TODO Auto-generated method stub
        //封装数据源
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(path1));
            //封装目的地
            FileOutputStream fos = new FileOutputStream(new File(path2));

            int by = 0;
            while((by = fis.read()) != -1){
                fos.write(by);
            }
            //释放资源(先关谁都行)
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取文件
    public static String readFile(String  path){
        File  file =  new  File(path);
        try {
            BufferedReader bf = new  BufferedReader(new FileReader(file));
            String  content  =  "";
            StringBuilder  sb =  new  StringBuilder();
            while(content != null){
                content = bf.readLine();
                if(content == null){
                    break;
                }
                sb.append(content.trim());
            }
            bf.close();
            return sb.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  null;
        }
    }

    //获取系统时间。
    public static String getSystemTimeMore(int i) {
        SimpleDateFormat mFormat = null;
        Date date = new Date(System.currentTimeMillis());//获取当前时间
        switch (i) {
            case 1:
                mFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒SS毫秒 EEEE");
                //2018年07月24日 15时16分27秒 星期二
                LOGGER.info(mFormat.format(date));
                break;

            case 2:
                mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEE", Locale.ENGLISH);
                LOGGER.info(mFormat.format(date));
                break;
            case 3:
                mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                LOGGER.info(mFormat.format(date));
                break;
            case 4:
                mFormat = new SimpleDateFormat("yyyy/MM/dd");
                LOGGER.info(mFormat.format(date));
                break;
            case 5:
                mFormat = new SimpleDateFormat("HH-mm-ss");
                LOGGER.info(mFormat.format(date));
                break;
            case 6:
                mFormat = new SimpleDateFormat("EEEE");
                LOGGER.info(mFormat.format(date));
                break;
            case 7:
                mFormat = new SimpleDateFormat("E");
                LOGGER.info(mFormat.format(date));
                break;

            case 8:
                mFormat = new SimpleDateFormat("yyyy年MM月dd日 ，EEEE ");
                break;

            case 9:
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM dd, yyyy HH:mm:ss",
                        Locale.ENGLISH);
                break;
        }

        //	SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日 HH:mm:ss ");
//        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日 , EEEE ");
        String str = mFormat.format(date);
        return str;
    }
}

