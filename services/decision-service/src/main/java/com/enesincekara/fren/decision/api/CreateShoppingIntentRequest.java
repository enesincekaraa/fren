package com.enesincekara.fren.decision.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateShoppingIntentRequest(
        @NotBlank
        String productName,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal amount,

        @NotBlank
        String currency,

        @NotBlank
        String reason,

        @NotNull
        Integer cooldownHours
) {
}
