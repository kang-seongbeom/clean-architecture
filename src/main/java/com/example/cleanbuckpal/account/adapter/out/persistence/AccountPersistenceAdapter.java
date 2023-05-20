package com.example.cleanbuckpal.account.adapter.out.persistence;

import com.example.cleanbuckpal.account.application.port.out.LoadAccountPort;
import com.example.cleanbuckpal.account.application.port.out.UpdateAccountStatePort;
import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Activity;
import javax.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final SpringDataAccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final AccountMapper accountMapper;
    @Override
    public Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate) {
        AccountJpaEntity account = accountRepository
                .findById(accountId.getValue())
                .orElseThrow(EntityNotFoundException::new);

        List<ActivityJpaEntity> activities = activityRepository
                .findByOwnerSince(accountId.getValue(),
                        baselineDate);

        Long withdrawalBalance=orZero(activityRepository.getWithdrawalBalanceUntil(accountId.getValue(),baselineDate));
        Long depositBalance = orZero(activityRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate));
        return accountMapper.mapToDomainEntity(
                account,
                activities,
                withdrawalBalance,
                depositBalance
        );
    }

    private Long orZero(Long value) {
        return value==null?0:value;
    }

    @Override
    public void updateActivities(Account account) {
        for(Activity activity:account.getActivityWindow().getActivities()){
            if(activity.getId()==null){
                activityRepository.save(accountMapper.mapToJpaEntity(activity));
            }
        }
    }
}
