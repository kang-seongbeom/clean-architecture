package com.example.cleanbuckpal.account.domain;

import com.example.cleanbuckpal.account.domain.Account.AccountId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityWindow {

    private List<Activity> activities;

    public ActivityWindow(List<Activity> activities) {
        this.activities=activities;
    }

    public ActivityWindow(Activity... activities) {
        this.activities = new ArrayList<>(List.of(activities));
    }

    public Money calculateBalance(AccountId accountId) {
        Money depositBalance = activities.stream()
                .filter(a -> a.getTargetAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        Money withdrawBalance = activities.stream()
                .filter(a -> a.getSourceAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        return Money.add(depositBalance,withdrawBalance.negate());
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(this.activities);
    }
}
