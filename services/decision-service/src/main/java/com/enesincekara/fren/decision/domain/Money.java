package com.enesincekara.fren.decision.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("amount is required");
        }
        if (currency == null) {
            throw new IllegalArgumentException("currency is required");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");

        }
        int fractionDigits = currency.getDefaultFractionDigits();

        if (fractionDigits < 0) {
            throw new IllegalArgumentException(
                    "currency must define fraction digits"
            );
        }

        try {
            amount = amount.setScale(fractionDigits, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                    "amount has unsupported fractional precision",
                    exception
            );
        }
    }
}
