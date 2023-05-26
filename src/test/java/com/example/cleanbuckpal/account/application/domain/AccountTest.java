package com.example.cleanbuckpal.account.application.domain;

import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.ActivityWindow;
import com.example.cleanbuckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cleanbuckpal.common.AccountTestData.defaultAccount;
import static com.example.cleanbuckpal.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @DisplayName("고객의 현재 금액 계산")
    @Test
    void calculateBalance(){
        // given
        AccountId accountId = new AccountId(1L);
        Account account = defaultAccount()
                .withId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L))
                                .build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L))
                                .build()
                )).build();

        // when
        Money balance = account.calculateBalance();

        //then
        assertThat(balance).isEqualTo(Money.of(1555L));
    }

    @DisplayName("송금 성공")
    @Test
    void withdrawalSucceeds(){
        AccountId accountId = new AccountId(1L);
        Account account = defaultAccount()
                .withId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L))
                                .build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L))
                                .build()
                )).build();

        boolean isSuccess = account.withdraw(Money.of(555L), new AccountId(99L));

        assertThat(isSuccess).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
    }

    @DisplayName("송금 실패")
    @Test
    void withdrawalFailure(){
        AccountId accountId = new AccountId(1L);
        Account account = defaultAccount()
                .withId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L))
                                .build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L))
                                .build()
                )).build();

        boolean isSuccess = account.withdraw(Money.of(1556L), new AccountId(99L));

        assertThat(isSuccess).isFalse();
        assertThat(account.getActivityWindow().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L));
    }

    @DisplayName("입금 성공")
    @Test
    void depositSuccess(){
        AccountId accountId = new AccountId(1L);
        Account account = defaultAccount()
                .withId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L))
                                .build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L))
                                .build()
                )).build();

        boolean isSuccess = account.deposit(Money.of(445L), new AccountId(99L));

        assertThat(isSuccess).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L));
    }
}
