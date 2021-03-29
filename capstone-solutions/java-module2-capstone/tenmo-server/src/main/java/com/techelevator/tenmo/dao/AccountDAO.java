package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

    Account getAccountByUserId(Long userId);

	void updateBalance(Account account);
}

