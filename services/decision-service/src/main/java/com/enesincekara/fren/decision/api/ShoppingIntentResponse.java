package com.enesincekara.fren.decision.api;

import com.enesincekara.fren.decision.domain.ShoppingIntent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ShoppingIntentResponse(

        UUID id,
        String productName,
        BigDecimal amount,
        String currency,
        String reason,
        int cooldownHours,
        Instant createdAt,
        Instant reviewAvailableAt
) {

    public static ShoppingIntentResponse from(ShoppingIntent intent) {
        return new ShoppingIntentResponse(
                intent.id(),
                intent.productName(),
                intent.price().amount(),
                intent.price().currency().getCurrencyCode(),
                intent.reason(),
                intent.cooldownHours(),
                intent.createdAt(),
                intent.reviewAvailableAt()
        );
    }
}