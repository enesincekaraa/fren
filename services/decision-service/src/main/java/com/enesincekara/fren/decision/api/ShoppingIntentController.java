package com.enesincekara.fren.decision.api;


import com.enesincekara.fren.decision.application.ShoppingIntentService;
import com.enesincekara.fren.decision.domain.ShoppingIntent;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-intents")
public class ShoppingIntentController {

    private final ShoppingIntentService service;
    public ShoppingIntentController(ShoppingIntentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ShoppingIntentResponse> create(
            @Valid @RequestBody CreateShoppingIntentRequest request
    ) {
        ShoppingIntent intent = service.create(
                request.productName(),
                request.amount(),
                request.currency(),
                request.reason(),
                request.cooldownHours()
        );
        ShoppingIntentResponse response = ShoppingIntentResponse.from(intent);
        URI location = URI.create(
                "/api/v1/shopping-intents/" + intent.id()
        );

        return ResponseEntity.created(location).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ShoppingIntentResponse> getById(
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity.of(
                service.findById(id)
                        .map(ShoppingIntentResponse::from)
        );
    }

}
