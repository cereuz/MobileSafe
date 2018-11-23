package com.zao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class Log {
    /**
     * 日志工具类，使用静态方法打印日志  无需每个类中定义日志对象
     * Logback对每个Logger对象做了缓存，每次调用LoggerFactory.getLogger(String name)时如果已存在则从缓存中获取不会生成新的对象;
     * 同时也不会有对象的创建与销毁造成的性能损失
     * @author zsc
     * @datetime 2017年12月12日 上午11:43:51
     */
    // LoggerFactory.getLogger()方法的参数使用的是当前类的class
    private static final Logger logger = LoggerFactory.getLogger(getClassName());

    static {
        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure("src/resource/logback.xml");
        } catch (JoranException e) {
            e.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
    }



        public static void error(String msg) {
            logger.error(msg);
        }

        public static void error(String msg, Object... obj) {
            logger.error(msg, obj);
        }

        public static void warn(String msg) {
            logger.warn(msg);
        }

        public static void warn(String msg, Object... obj) {
            logger.warn(msg, obj);
        }

        public static void info(String msg) {
            logger.info(msg);
        }

        public static void info(String msg, Object... obj) {
            logger.info(msg, obj);
        }

        public static void debug(String msg) {
            logger.debug(msg);
        }

        public static void debug(String msg, Object... obj) {
            logger.debug(msg, obj);
        }

        public static void trace(String msg) {
            logger.trace(msg);
        }

        public static void trace(String msg, Object... obj) {
            logger.trace(msg, obj);
        }

        // 获取调用 error,info,debug静态类的类名
        private static String getClassName() {
            return new SecurityManager() {
                public String getClassName() {
                    return getClassContext()[3].getName();
                }
            }.getClassName();
        }
  }
