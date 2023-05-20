package com.example.cleanbuckpal.application.common;

import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.ActivityWindow;
import com.example.cleanbuckpal.account.domain.Money;

public class AccountTestData {

    public static AccountBuilder defaultAccount() {
        return new AccountBuilder()
                .withId(new AccountId(42L))
                .withBaselineBalance(Money.of(999L))
                .withActivityWindow(new ActivityWindow(
                        ActivityTestData.defaultActivity().build(),
                        ActivityTestData.defaultActivity().build()
                ));
    }

    public static class AccountBuilder {
        private AccountId id;
        private Money baselineBalance;
        private ActivityWindow activityWindow;

        public AccountBuilder withId(AccountId id) {
            this.id = id;
            return this;
        }

        public AccountBuilder withBaselineBalance(Money baselineBalance) {
            this.baselineBalance = baselineBalance;
            return this;
        }

        public AccountBuilder withActivityWindow(ActivityWindow activityWindow) {
            this.activityWindow = activityWindow;
            return this;
        }

        public Account build(){
            return Account.withId(this.id, this.baselineBalance, this.activityWindow);
        }
    }
}
