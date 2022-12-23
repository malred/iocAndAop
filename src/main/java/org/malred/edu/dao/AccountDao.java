package org.malred.edu.dao;

import org.malred.edu.pojo.Account;

public interface AccountDao {
    Account queryAccountByCardNo(String cardNo) throws Exception;

    int updateAccountByCardNo(Account account) throws Exception;
}
