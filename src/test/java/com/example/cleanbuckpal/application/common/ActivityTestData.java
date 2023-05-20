package com.example.cleanbuckpal.application.common;

import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.Activity;
import com.example.cleanbuckpal.account.domain.Money;

import java.time.LocalDateTime;

public class ActivityTestData {

    public static ActivityBuilder defaultActivity(){
        return new ActivityBuilder()
                .withOwnerAccount(new AccountId(42L))
                .withSourceAccount(new AccountId(42L))
                .withTargetAccount(new AccountId(41L))
                .withTimestamp(LocalDateTime.now())
                .withMoney(Money.of(999L));
    }

    public static class ActivityBuilder{

        private Activity.ActivityId id;

        private AccountId ownerAccountId;

        private AccountId sourceAccountId;

        private AccountId targetAccountId;

        private LocalDateTime timestamp;

        private Money money;

        public ActivityBuilder withId(Activity.ActivityId id) {
            this.id = id;
            return this;
        }

        public ActivityBuilder withOwnerAccount(AccountId ownerAccountId) {
            this.ownerAccountId = ownerAccountId;
            return this;
        }

        public ActivityBuilder withSourceAccount(AccountId sourceAccountId) {
            this.sourceAccountId = sourceAccountId;
            return this;
        }

        public ActivityBuilder withTargetAccount(AccountId targetAccountId) {
            this.targetAccountId = targetAccountId;
            return this;
        }

        public ActivityBuilder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ActivityBuilder withMoney(Money money) {
            this.money = money;
            return this;
        }

        public Activity build(){
            return new Activity(
                    this.id,
                    this.ownerAccountId,
                    this.sourceAccountId,
                    this.targetAccountId,
                    this.timestamp,
                    this.money);
        }
    }


}
