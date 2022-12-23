package org.malred.edu.utils;

import java.sql.SQLException;

/**
 * 管理事务的类(开启/提交/回滚)
 */
public class TransactionManager {

    private ConnectionUtils connectionUtils;

    // set方法,让工厂类可以依赖注入
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }
    // 工厂类来实例化单例
    // 也是单例
//    private static TransactionManager transactionManager = new TransactionManager();
//
//    public static TransactionManager getInstance() {
//        return transactionManager;
//    }

    // 开启手动事务控制
    public void beginTransaction() throws SQLException {
//        ConnectionUtils.geInstance().getCurrentThreadConn().setAutoCommit(false);
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }


    // 提交事务
    public void commit() throws SQLException {
//        ConnectionUtils.getCurrentThreadConn().commit();
        connectionUtils.getCurrentThreadConn().commit();
    }


    // 回滚事务
    public void rollback() throws SQLException {
//        ConnectionUtils.getCurrentThreadConn().rollback();
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
