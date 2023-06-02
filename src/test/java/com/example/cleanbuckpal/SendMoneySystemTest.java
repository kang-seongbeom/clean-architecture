package com.example.cleanbuckpal;

import com.example.cleanbuckpal.account.application.port.out.LoadAccountPort;
import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static com.example.cleanbuckpal.account.domain.Account.*;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SendMoneySystemTest {

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    @Sql("SendMoneySystemTest.sql")
    void sendMoney(){
        Money initSourceBalance = sourceAccount().calculateBalance();
        Money initTargetBalance = targetAccount().calculateBalance();

        ResponseEntity response = whenSendMoney(sourceAccountId(), targetAccountId(), transferredAmount());

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(sourceAccount().calculateBalance()).isEqualTo(initSourceBalance.minus(transferredAmount()));
        then(targetAccount().calculateBalance()).isEqualTo(initTargetBalance.plus(transferredAmount()));
    }

    private ResponseEntity whenSendMoney(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, httpHeaders);
        return restTemplate.exchange(
                "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
                HttpMethod.POST,
                request,
                Object.class,
                sourceAccountId.getValue(),
                targetAccountId.getValue(),
                money.getAmount().longValue());
    }

    private Money transferredAmount() {
        return Money.of(500L);
    }

    private Account targetAccount() {
        return loadAccount(targetAccountId());
    }

    private Account sourceAccount() {
       return loadAccount(sourceAccountId());
    }

    private Account loadAccount(AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
    }

    private AccountId targetAccountId() {
        return new AccountId(2L);
    }

    private AccountId sourceAccountId() {
        return new AccountId(1L);
    }
}
