package com.example.cleanbuckpal.account.adapter.out.persistence;

import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.Activity;
import com.example.cleanbuckpal.account.domain.Activity.ActivityId;
import com.example.cleanbuckpal.account.domain.ActivityWindow;
import com.example.cleanbuckpal.account.domain.Money;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class AccountMapper {

    public Account mapToDomainEntity(AccountJpaEntity account, List<ActivityJpaEntity> activities, Long withdrawalBalance, Long depositBalance) {
        Money baselineBalance=Money.subtract(Money.of(depositBalance),Money.of(withdrawalBalance));
        return Account.withId(
                new AccountId(account.getId()),
                baselineBalance,
                mapToActivityWindow(activities)
        );
    }

    private ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activities) {
        ArrayList<Activity> mappedActivities = new ArrayList<>();

        for(ActivityJpaEntity activity:activities){
            mappedActivities.add(new Activity(
                    new ActivityId(activity.getId()),
                    new AccountId(activity.getOwnerAccountId()),
                    new AccountId(activity.getSourceAccountId()),
                    new AccountId(activity.getTargetAccountId()),
                    activity.getTimestamp(),
                    Money.of(activity.getMoney())
            ));
        }

        return new ActivityWindow(mappedActivities);
    }

    public ActivityJpaEntity mapToJpaEntity(Activity activity) {
        return new ActivityJpaEntity(
                activity.getId()==null?null:activity.getId().getValue(),
                activity.getOwnerAccountId().getValue(),
                activity.getSourceAccountId().getValue(),
                activity.getTargetAccountId().getValue(),
                activity.getTimestamp(),
                activity.getMoney().getAmount().longValue()
        );
    }
}
