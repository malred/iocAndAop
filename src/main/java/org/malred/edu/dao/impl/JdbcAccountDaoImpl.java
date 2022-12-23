package org.malred.edu.dao.impl;

import org.malred.edu.dao.AccountDao;
import org.malred.edu.pojo.Account;
import org.malred.edu.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcAccountDaoImpl implements AccountDao {
    private ConnectionUtils connectionUtils;

    // set方法,让工厂类可以依赖注入
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        //从连接池获取连接
        // Connection con = DruidUtils.getInstance().getConnection();
//        Connection con = ConnectionUtils.geInstance().getCurrentThreadConn();
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "select * from account where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = new Account();
        while (resultSet.next()) {
            account.setCardNo(resultSet.getString("cardNo"));
            account.setName(resultSet.getString("name"));
            account.setMoney(resultSet.getInt("money"));
        }
        resultSet.close();
        preparedStatement.close();
        // 不能关闭该连接,否则下次getCurrentThreadConn时会重新获取连接(因为上传的连接被销毁,判断为未绑定)
//        con.close();
        return account;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {
        //从连接池获取连接
        // 改造为从当前线程中获取绑定的connect链接
//        Connection con = DruidUtils.getInstance().getConnection();
//        Connection con = ConnectionUtils.geInstance().getCurrentThreadConn();
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "update account set money=? where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1, (int) account.getMoney());
        preparedStatement.setString(2, account.getCardNo());
        int i = preparedStatement.executeUpdate();
        preparedStatement.close();
//        con.close(); // 不能关闭连接,因为当前线程已经绑定,关了就没有绑定了
        return i;
    }
}
