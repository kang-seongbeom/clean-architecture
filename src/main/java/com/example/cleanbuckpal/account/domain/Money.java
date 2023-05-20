package com.example.cleanbuckpal.account.domain;

import lombok.NonNull;
import lombok.Value;

import java.math.BigInteger;

@Value
public class Money {

    public static Money ZERO = Money.of(0L);
    @NonNull
    private final BigInteger amount;

    public static Money of(long value){
        return new Money(BigInteger.valueOf(value));
    }

    public static Money add(Money a,Money b) {
        return new Money(a.amount.add(b.amount));
    }

    public static Money subtract(Money a, Money b) {
        return new Money(a.amount.subtract(b.amount));
    }

    public Money negate() {
        return new Money(this.amount.negate());
    }

    public boolean isPositiveOrZero() {
        return this.amount.compareTo(BigInteger.ZERO)>=0;
    }
}
