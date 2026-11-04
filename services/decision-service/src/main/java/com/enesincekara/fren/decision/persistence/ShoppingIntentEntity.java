package com.enesincekara.fren.decision.persistence;

import com.enesincekara.fren.decision.domain.Money;
import com.enesincekara.fren.decision.domain.ShoppingDecision;
import com.enesincekara.fren.decision.domain.ShoppingIntent;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

@Entity
@Table(name = "shopping_intents")
public class ShoppingIntentEntity {

    @Id
    private UUID id;

    @Column(
            name = "product_name",
            nullable = false,
            columnDefinition = "text"
    )
    private String productName;

    @Column(nullable = false,columnDefinition = "numeric")
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, columnDefinition = "text")
    private String reason;

    @Column(name = "cooldown_hours", nullable = false)
    private int cooldownHours;


    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(name = "review_available_at", nullable = false)
    private Instant reviewAvailableAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ShoppingDecision decision;

    protected ShoppingIntentEntity() {}

    public static ShoppingIntentEntity fromDomain(ShoppingIntent intent) {
        ShoppingIntentEntity entity = new ShoppingIntentEntity();

        entity.id=intent.id();
        entity.productName=intent.productName();
        entity.amount=intent.price().amount();
        entity.currency=intent.price().currency().getCurrencyCode();
        entity.reason=intent.reason();
        entity.cooldownHours=intent.cooldownHours();
        entity.createdAt=intent.createdAt();
        entity.reviewAvailableAt=intent.reviewAvailableAt();
        entity.decision=intent.decision();
        return entity;

    }

    public ShoppingIntent toDomain() {
        Money price = new Money(amount, Currency.getInstance(currency));
        return ShoppingIntent.restore(
                id,
                productName,
                price,
                reason,
                cooldownHours,
                createdAt,
                reviewAvailableAt,
                decision
        );
    }
}
