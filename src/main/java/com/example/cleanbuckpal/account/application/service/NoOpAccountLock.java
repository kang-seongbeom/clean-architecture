package com.example.cleanbuckpal.account.application.service;

import com.example.cleanbuckpal.account.application.port.out.AccountLock;
import com.example.cleanbuckpal.account.domain.Account;
import org.springframework.stereotype.Component;


@Component
public class NoOpAccountLock  implements AccountLock {
    @Override
    public void lockAccount(Account.AccountId accountId) {

    }

    @Override
    public void releaseAccount(Account.AccountId accountId) {

    }
}
