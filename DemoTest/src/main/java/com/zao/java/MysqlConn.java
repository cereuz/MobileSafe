package com.zao.java;

import java.sql.*;
//import org.apache.log4j.Logger;
import org.testng.log4testng.Logger;

public class MysqlConn {
    private static Logger log = Logger.getLogger(MysqlConn.class);
    private static MysqlConn op;
    private Connection conn;
    private String driver;
    private String url;
    private String userName;
    private String userPassword;

    private MysqlConn() {
    }

    private MysqlConn(String driver, String url, String userName, String userPassword) throws Exception {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.userPassword = userPassword;
        try {
            // 加载mysql驱动器
            Class.forName(driver);
            // 建立数据库连接
            conn = DriverManager.getConnection(url, userName, userPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动器失败");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("注册驱动器失败");
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public void getConnection() throws Exception {
        if (conn != null) {
            Class.forName(this.driver);
            conn = DriverManager.getConnection(this.url, this.userName, this.userPassword);
        }
    }

    // 静态方法，返回mysqlconn实例
    public static MysqlConn instance() {
        if (op == null) {
            op = new MysqlConn();
        }
        return op;
    }

    // 静态方法，返回mysqlconn实例
    public static MysqlConn instance(String driver, String url, String userName, String userPassword) throws Exception {
        if (op == null) {
            op = new MysqlConn(driver, url, userName, userPassword);
        }
        return op;
    }

    // 访问数据库，执行插入、删除、更新操作
    public String execute_Update(String sql) throws Exception {
        getConnection();// 初始化数据库连接
        Statement stmt = this.conn.createStatement();// 创建Statement对象
        if (stmt.executeUpdate(sql) != 1) {
            return "failure";
        }
        return "success";
    }

    // 访问数据库，执行查询操作
    public ResultSet queryx(String sql) throws Exception {
        getConnection();
        Statement stmt = this.conn.createStatement();
        return stmt.executeQuery(sql);
    }

    // 关闭数据库链接
    public void closeDB() {
        try {
            conn.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }
}
