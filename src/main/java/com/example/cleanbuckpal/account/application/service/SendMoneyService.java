package com.example.cleanbuckpal.account.application.service;

import com.example.cleanbuckpal.account.application.port.in.SendMoneyCommand;
import com.example.cleanbuckpal.account.application.port.in.SendMoneyUseCase;
import com.example.cleanbuckpal.account.application.port.out.AccountLock;
import com.example.cleanbuckpal.account.application.port.out.LoadAccountPort;
import com.example.cleanbuckpal.account.application.port.out.UpdateAccountStatePort;
import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        Account sourceAccount = loadAccountPort.loadAccount(
                command.getSourceAccountId(),
                baselineDate
        );

        Account targetAccount = loadAccountPort.loadAccount(
                command.getTargetAccountId(),
                baselineDate
        );

        AccountId sourceAccountId = sourceAccount.getId()
                .orElseThrow(() -> new IllegalArgumentException("expected source account ID not to be empty"));

        AccountId targetAccountId = targetAccount.getId()
                .orElseThrow(() -> new IllegalArgumentException("expected source account ID not to be empty"));

        accountLock.lockAccount(sourceAccountId);
        if(!sourceAccount.withdraw(command.getMoney(),targetAccountId)){
            accountLock.releaseAccount(sourceAccountId);
            return false;
        }

        accountLock.lockAccount(targetAccountId);
        if(!targetAccount.deposit(command.getMoney(),sourceAccountId)){
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);
        return true;
    }
}
