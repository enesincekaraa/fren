package com.enesincekara.fren.decision.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingIntentTest {

    private final Clock creationClock = Clock.fixed(
            Instant.parse("2026-11-03T10:00:00Z"),
            ZoneOffset.UTC
    );

    private final Money price = new Money(
            new BigDecimal("3499.90"),
            Currency.getInstance("TRY")
    );

    @Test
    void createsShoppingIntentAndCalculatesReviewTime() {
        ShoppingIntent intent = ShoppingIntent.create(
                "  Kablosuz kulaklık  ",
                price,
                "  Mevcut kulaklığımın pili çabuk bitiyor  ",
                72,
                creationClock
        );

        assertNotNull(intent.id());

        assertEquals(
                "Kablosuz kulaklık",
                intent.productName()
        );

        assertEquals(
                "Mevcut kulaklığımın pili çabuk bitiyor",
                intent.reason()
        );

        assertEquals(price, intent.price());

        assertEquals(
                Instant.parse("2026-11-03T10:00:00Z"),
                intent.createdAt()
        );

        assertEquals(
                Instant.parse("2026-11-06T10:00:00Z"),
                intent.reviewAvailableAt()
        );
    }

    @Test
    void cannotBeReviewedBeforeCooldownEnds() {
        ShoppingIntent intent = createIntentWith72HourCooldown();

        Clock beforeDeadline = Clock.fixed(
                Instant.parse("2026-11-06T09:59:59Z"),
                ZoneOffset.UTC
        );

        assertFalse(intent.canBeReviewed(beforeDeadline));
    }

    @Test
    void canBeReviewedExactlyWhenCooldownEnds() {
        ShoppingIntent intent = createIntentWith72HourCooldown();

        Clock atDeadline = Clock.fixed(
                Instant.parse("2026-11-06T10:00:00Z"),
                ZoneOffset.UTC
        );

        assertTrue(intent.canBeReviewed(atDeadline));
    }

    @Test
    void canBeReviewedAfterCooldownEnds() {
        ShoppingIntent intent = createIntentWith72HourCooldown();

        Clock afterDeadline = Clock.fixed(
                Instant.parse("2026-11-06T10:00:01Z"),
                ZoneOffset.UTC
        );

        assertTrue(intent.canBeReviewed(afterDeadline));
    }

    @Test
    void calculatesReviewTimeFor24HourCooldown() {
        ShoppingIntent intent = ShoppingIntent.create(
                "Kitap",
                price,
                "Okumak istiyorum",
                24,
                creationClock
        );

        assertEquals(
                Instant.parse("2026-11-04T10:00:00Z"),
                intent.reviewAvailableAt()
        );
    }

    @Test
    void rejectsBlankProductName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ShoppingIntent.create(
                        "   ",
                        price,
                        "Bir gerekçe",
                        24,
                        creationClock
                )
        );
    }

    @Test
    void rejectsMissingPrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ShoppingIntent.create(
                        "Kulaklık",
                        null,
                        "Bir gerekçe",
                        24,
                        creationClock
                )
        );
    }

    @Test
    void rejectsBlankReason() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ShoppingIntent.create(
                        "Kulaklık",
                        price,
                        " ",
                        24,
                        creationClock
                )
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 12, 48, 96})
    void rejectsUnsupportedCooldown(int cooldownHours) {
        assertThrows(
                IllegalArgumentException.class,
                () -> ShoppingIntent.create(
                        "Kulaklık",
                        price,
                        "Bir gerekçe",
                        cooldownHours,
                        creationClock
                )
        );
    }

    private ShoppingIntent createIntentWith72HourCooldown() {
        return ShoppingIntent.create(
                "Kablosuz kulaklık",
                price,
                "Mevcut kulaklığımın pili çabuk bitiyor",
                72,
                creationClock
        );
    }
}