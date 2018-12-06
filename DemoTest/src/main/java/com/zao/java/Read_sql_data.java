package com.zao.java;

import org.testng.log4testng.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * 获取数据库数据存入二维数组
 *
 * @return Object[][]
 * @author xzee
 */
public class Read_sql_data {
    private static Logger log = Logger.getLogger(Read_sql_data.class);

    @SuppressWarnings({ "unchecked", "rawtypes"})
    public static Object[][] Get_DB_TestCase_data(MysqlConn t_conn,String sql) throws Exception {
        ArrayList<String> arrkey = new ArrayList<String>();
        ResultSet rs = t_conn.queryx(sql);
        ResultSetMetaData data = rs.getMetaData();
        // 总列数
        int columns=data.getColumnCount();
        // 获得首行的列名，作为hashmap的key值
        for (int c = 1; c <= columns; c++) {
            String columnName = data.getColumnName(c);
            arrkey.add(columnName);
        }
        int rowTotalNum = 1;
        while (rs.next()) {
            //获取总行数
            rowTotalNum = rs.getRow() + 1;
        }
        HashMap<String, String>[][] map = new HashMap[rowTotalNum - 1][1];
        // 对数组中所有元素hashmap进行初始化
        if (rowTotalNum > 1) {
            for (int i = 0; i < rowTotalNum - 1; i++) {
                map[i][0] = new HashMap();
            }
            // 特别重要，否则取到的全是0。因为执行上面的while(rs.next())后，ResultSet对象的下标已指到0，所以rs需要重新赋值。
            // API：当生成ResultSet对象的Statement对象关闭、重新执行或用来从多个结果的序列获取下一个结果时，ResultSet对象将自动关闭。
            rs = t_conn.queryx(sql);
            // 输出二维数组
            // 遍历所有的单元格的值添加到hashmap中
            for (int r = 1; rs.next(); r++) {
                for (int c = 1; c <= columns; c++) {
                    String rowValue = rs.getString(c);
                    map[r - 1][0].put(arrkey.get(c - 1), rowValue);
                }
            }
        } else {
            log.error("数据库中没有数据！");
        }
        return map;
    }
}
