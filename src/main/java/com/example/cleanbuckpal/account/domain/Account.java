package com.example.cleanbuckpal.account.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    @Getter private final AccountId id;

    @Getter private final Money baselineBalance;

    @Getter private final ActivityWindow activityWindow;

    public static Account withId(AccountId accountId, Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(accountId,baselineBalance,activityWindow);
    }

    public Optional<AccountId> getId() {
        return Optional.ofNullable(this.id);
    }

    public Money calculateBalance(){
        return Money.add(this.baselineBalance,
                this.activityWindow.calculateBalance(this.id));
    }

    public boolean deposit(Money money,AccountId sourceAccountId){
        Activity deposit = new Activity(this.id,
                sourceAccountId,
                this.id,
                LocalDateTime.now(),
                money);

        this.activityWindow.addActivity(deposit);
        return true;
    }

    public boolean withdraw(Money money,AccountId targetAccountId){
        if(!mayWithdraw(money)){
            return false;
        }

        Activity withdraw = new Activity(this.id,
                this.id,
                targetAccountId,
                LocalDateTime.now(),
                money);

        this.activityWindow.addActivity(withdraw);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return Money.add(this.calculateBalance(),money.negate()).isPositiveOrZero();
    }

    @Value
    public static class AccountId {
        private Long value;
    }
}
