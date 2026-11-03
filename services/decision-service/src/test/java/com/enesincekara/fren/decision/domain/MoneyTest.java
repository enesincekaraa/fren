package com.enesincekara.fren.decision.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void createsMoneyWithValidAmountAndCurrency(){
        Money money = new Money(
                new BigDecimal("3499.90"),
                Currency.getInstance("TRY")
        );
        assertEquals(new BigDecimal("3499.90"), money.amount());
        assertEquals(Currency.getInstance("TRY"), money.currency());
    }
    @Test
    void treatsEquivalentAmountsAsEqual() {
        Money first = new Money(
                new BigDecimal("10"),
                Currency.getInstance("TRY")
        );

        Money second = new Money(
                new BigDecimal("10.00"),
                Currency.getInstance("TRY")
        );

        assertEquals(first, second);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.01", "-100"})
    void rejectsZeroOrNegativeAmount(String amount) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(
                        new BigDecimal(amount),
                        Currency.getInstance("TRY")
                )
        );
    }

    @Test
    void rejectsMissingAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(null, Currency.getInstance("TRY"))
        );
    }

    @Test
    void rejectsMissingCurrency() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(new BigDecimal("10.00"), null)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "TRY, 10.001",
            "JPY, 10.1",
            "KWD, 10.0001"
    })
    void rejectsAmountThatRequiresRounding(
            String currencyCode,
            String amount
    ) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(
                        new BigDecimal(amount),
                        Currency.getInstance(currencyCode)
                )
        );
    }


    @Test
    void acceptsThreeFractionDigitsWhenCurrencySupportsThem() {
        Money money = new Money(
                new BigDecimal("10.125"),
                Currency.getInstance("KWD")
        );

        assertEquals(new BigDecimal("10.125"), money.amount());
    }

    @Test
    void rejectsCurrencyWithoutDefinedFractionDigits() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(
                        new BigDecimal("10"),
                        Currency.getInstance("XXX")
                )
        );
    }

}