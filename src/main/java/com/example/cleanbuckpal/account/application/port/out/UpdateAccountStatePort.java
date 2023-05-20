package com.example.cleanbuckpal.account.application.port.out;

import com.example.cleanbuckpal.account.domain.Account;

public interface UpdateAccountStatePort {
    void updateActivities(Account account);
}
