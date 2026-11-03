package com.enesincekara.fren.decision.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class ShoppingIntent {

    private final UUID id;
    private final String productName;
    private final Money price;
    private final String reason;
    private final int cooldownHours;
    private final Instant createdAt;
    private final Instant reviewAvailableAt;

    private ShoppingIntent(
            UUID id,
            String productName,
            Money price,
            String reason,
            int cooldownHours,
            Instant createdAt,
            Instant reviewAvailableAt
    ) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.reason = reason;
        this.cooldownHours = cooldownHours;
        this.createdAt = createdAt;
        this.reviewAvailableAt = reviewAvailableAt;
    }

    public static ShoppingIntent create(
            String productName,
            Money price,
            String reason,
            int cooldownHours,
            Clock clock
    ) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "product name is required"
            );
        }

        if (price == null) {
            throw new IllegalArgumentException(
                    "price is required"
            );
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "reason is required"
            );
        }

        if (cooldownHours != 24 && cooldownHours != 72) {
            throw new IllegalArgumentException(
                    "cooldown must be 24 or 72 hours"
            );
        }

        if (clock == null) {
            throw new IllegalArgumentException(
                    "clock is required"
            );
        }

        Instant createdAt = Instant.now(clock);

        Instant reviewAvailableAt = createdAt.plus(
                Duration.ofHours(cooldownHours)
        );

        return new ShoppingIntent(
                UUID.randomUUID(),
                productName.strip(),
                price,
                reason.strip(),
                cooldownHours,
                createdAt,
                reviewAvailableAt
        );
    }

    public boolean canBeReviewed(Clock clock) {
        if (clock == null) {
            throw new IllegalArgumentException(
                    "clock is required"
            );
        }

        Instant now = Instant.now(clock);

        return !now.isBefore(reviewAvailableAt);
    }

    public UUID id() {
        return id;
    }

    public String productName() {
        return productName;
    }

    public Money price() {
        return price;
    }

    public String reason() {
        return reason;
    }

    public int cooldownHours() {
        return cooldownHours;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant reviewAvailableAt() {
        return reviewAvailableAt;
    }
}