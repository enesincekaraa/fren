package com.enesincekara.fren.decision.application;

import com.enesincekara.fren.decision.domain.Money;
import com.enesincekara.fren.decision.domain.ShoppingIntent;
import com.enesincekara.fren.decision.persistence.ShoppingIntentEntity;
import com.enesincekara.fren.decision.persistence.ShoppingIntentJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingIntentService {


    private final Clock clock;
    private final ShoppingIntentJpaRepository repository;
    public ShoppingIntentService(Clock clock, ShoppingIntentJpaRepository repository) {
        this.clock = clock;
        this.repository = repository;
    }

    @Transactional
    public ShoppingIntent create(
            String productName,
            BigDecimal amount,
            String currencyCode,
            String reason,
            int cooldownHours
    ){
        Money price = new Money(amount, Currency.getInstance(currencyCode));

        ShoppingIntent intent = ShoppingIntent.create(
                productName,
                price,
                reason,
                cooldownHours,
                clock
        );

        ShoppingIntentEntity entity =ShoppingIntentEntity.fromDomain(intent);
        repository.save(entity);
        return intent;
    }

    @Transactional(readOnly = true)
    public Optional<ShoppingIntent> findById(UUID id) {
        return repository.findById(id).map(ShoppingIntentEntity::toDomain);
    }
}
