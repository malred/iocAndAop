package org.malred.edu.service.impl;

import org.malred.edu.dao.AccountDao;
import org.malred.edu.pojo.Account;
import org.malred.edu.service.TransferService;
import org.malred.edu.utils.TransactionManager;

public class TransferServiceImpl implements TransferService {
    //    private AccountDao accountDao = new JdbcAccountDaoImpl();
//    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");
    // 更好的形式,让框架来注入属性
    private AccountDao accountDao;

    // 让框架可以反射设置值
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    private TransactionManager transactionManager;

    // 让框架可以反射设置值
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
//        try {
        // 开启事务(关闭事务的自动提交,jdbc默认自动提交)
//            ConnectionUtils.geInstance().getCurrentThreadConn().setAutoCommit(false);
//            TransactionManager.getInstance().beginTransaction();
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);
        from.setMoney(from.getMoney() - money);
        to.setMoney(to.getMoney() + money);
        accountDao.updateAccountByCardNo(from);
        int c = 1 / 0;//人为异常
        accountDao.updateAccountByCardNo(to);
        // 提交事务
//            ConnectionUtils.geInstance().getCurrentThreadConn().commit();
//        TransactionManager.getInstance().commit();
        transactionManager.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 回滚事务
////            ConnectionUtils.geInstance().getCurrentThreadConn().rollback();
//            TransactionManager.getInstance().rollback();
//            throw e; // 抛出异常,让servlet捕获
//        }
    }
}