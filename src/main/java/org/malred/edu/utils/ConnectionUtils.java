package org.malred.edu.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {
    // 工厂类来生成单例,这里不需要了
    // 保证当前类是单例,否则无法保证每次使用该类,其threadlocal都同一个
//    private ConnectionUtils() {
//    }
//
//    private static ConnectionUtils connectionUtils = new ConnectionUtils();
//
//    public static ConnectionUtils geInstance() {
//        return connectionUtils;
//    }

    /**
     * 总结而言：ThreadLocal是一个将在多线程中为每一个线程创建单独的变量副本的类;
     * 当使用ThreadLocal来维护变量时, ThreadLocal会为每个线程创建单独的变量副本,
     * 避免因多线程操作共享变量而导致的数据不一致的情况
     * ------
     * 著作权归@pdai所有
     * 原文链接：https://pdai.tech/md/java/thread/java-thread-x-threadlocal.html
     */
    // 存储当前线程的连接
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /**
     * 从当前线程获取连接
     */
    public static Connection getCurrentThreadConn() throws SQLException {
        // 判断当前线程是否已经绑定连接,如果没有,就1,从连接池获取,2,并绑定到当前线程
        Connection connection = threadLocal.get();
        if (connection == null) {
            // 从连接池拿连接
            connection = DruidUtils.getInstance().getConnection();
            // 绑定到当前线程
            threadLocal.set(connection);
        }
        return connection;
    }
}
