package com.example.cleanbuckpal.account.application.service;

import com.example.cleanbuckpal.account.application.port.in.SendMoneyCommand;
import com.example.cleanbuckpal.account.application.port.out.AccountLock;
import com.example.cleanbuckpal.account.application.port.out.LoadAccountPort;
import com.example.cleanbuckpal.account.application.port.out.UpdateAccountStatePort;
import com.example.cleanbuckpal.account.application.service.SendMoneyService;
import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

public class SendMoneyServiceTest {

    private final LoadAccountPort loadAccountPort =
            Mockito.mock(LoadAccountPort.class);
    private final AccountLock accountLock =
            Mockito.mock(AccountLock.class);
    private final UpdateAccountStatePort updateAccountStatePort =
            Mockito.mock(UpdateAccountStatePort.class);
    private final SendMoneyService sendMoneyService =
            new SendMoneyService(
                    loadAccountPort,
                    accountLock,
                    updateAccountStatePort
            );

    @DisplayName("전송 성공")
    @Test
    void transactionSucceeds() {
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        givenWithdrawalWillSucceed(sourceAccount);
        givenDepositWillSucceed(targetAccount);

        Money money = Money.of(500L);

        SendMoneyCommand command = new SendMoneyCommand(
                sourceAccount.getId().get(),
                targetAccount.getId().get(),
                money
        );

        boolean success = sendMoneyService.sendMoney(command);

        assertThat(success).isTrue();

        AccountId sourceAccountId = sourceAccount.getId().get();
        AccountId targetAccountId = targetAccount.getId().get();

        then(accountLock).should().lockAccount(eq(sourceAccountId));
        then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
        then(accountLock).should().releaseAccount(eq(sourceAccountId));

        then(accountLock).should().lockAccount(eq(targetAccountId));
        then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
        then(accountLock).should().releaseAccount(eq(targetAccountId));

        thenAccountHaveBeenUpdated(sourceAccountId, targetAccountId);
    }

    @DisplayName("전송 실패")
    @Test
    void givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased(){
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        givenWithdrawalWillFail(sourceAccount);
        givenDepositWillSucceed(targetAccount);

        Money money = Money.of(500L);

        SendMoneyCommand command = new SendMoneyCommand(
                sourceAccount.getId().get(),
                targetAccount.getId().get(),
                money
        );

        boolean success = sendMoneyService.sendMoney(command);

        assertThat(success).isFalse();

        AccountId sourceAccountId = sourceAccount.getId().get();
        then(accountLock).should().lockAccount(eq(sourceAccountId));
        then(accountLock).should().releaseAccount(sourceAccountId);
        then(accountLock).should(times(0)).lockAccount(targetAccount.getId().get());
    }

    private void givenWithdrawalWillFail(Account sourceAccount) {
        given(sourceAccount.withdraw(any(Money.class), any(AccountId.class))).willReturn(false);
    }

    private void thenAccountHaveBeenUpdated(AccountId... accountIds) {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        then(updateAccountStatePort).should(times(accountIds.length))
                .updateActivities(accountCaptor.capture());

        List<AccountId> updatedAccountIds = accountCaptor.getAllValues().stream()
                .map(Account::getId)
                .map(Optional::get)
                .collect(Collectors.toList());

        for(AccountId accountId : accountIds){
            assertThat(updatedAccountIds).contains(accountId);
        }
    }

    private void givenDepositWillSucceed(Account targetAccount) {
        given(targetAccount.deposit(any(Money.class), any(AccountId.class))).willReturn(true);
    }

    private void givenWithdrawalWillSucceed(Account sourceAccount) {
        given(sourceAccount.withdraw(any(Money.class), any(AccountId.class))).willReturn(true);
    }

    private Account givenTargetAccount() {
        return givenAnAccountWithId(new AccountId(42L));
    }

    private Account givenSourceAccount() {
        return givenAnAccountWithId(new AccountId(41L));
    }

    private Account givenAnAccountWithId(AccountId accountId) {
        Account account = Mockito.mock(Account.class);
        given(account.getId()).willReturn(Optional.of(accountId));
        given(loadAccountPort.loadAccount(eq(account.getId().get()),
                any(LocalDateTime.class))
        ).willReturn(account);

        return account;
    }
}
