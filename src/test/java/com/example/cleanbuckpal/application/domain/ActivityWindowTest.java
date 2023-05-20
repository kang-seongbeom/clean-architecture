package com.example.cleanbuckpal.application.domain;


import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.ActivityWindow;
import com.example.cleanbuckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cleanbuckpal.application.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

public class ActivityWindowTest {

    @DisplayName("금액 계산")
    @Test
    void calculateBalance() {
        // given
        AccountId account1 = new AccountId(1L);
        AccountId account2 = new AccountId(2L);

        ActivityWindow window = new ActivityWindow(
                defaultActivity()
                        .withSourceAccount(account1)
                        .withTargetAccount(account2)
                        .build(),
                defaultActivity()
                        .withSourceAccount(account1)
                        .withTargetAccount(account2)
                        .withMoney(Money.of(1))
                        .build(),
                defaultActivity()
                        .withSourceAccount(account2)
                        .withTargetAccount(account1)
                        .withMoney(Money.of(500))
                        .build()
        );

        // when, then
        assertThat(window.calculateBalance(account1)).isEqualTo(Money.of(-500));
        assertThat(window.calculateBalance(account2)).isEqualTo(Money.of(500));
    }
}
