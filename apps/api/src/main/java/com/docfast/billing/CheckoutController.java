package com.docfast.billing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    public record CheckoutRequest(
            @NotNull @Email String email,
            @NotNull Purchase.Plan plan) {
    }

    @PostMapping
    public CheckoutService.CheckoutResponse createCheckout(@Valid @RequestBody CheckoutRequest request) {
        log.info("POST /api/checkout - email: {}, plan: {}", request.email, request.plan);
        return service.createCheckout(request.email, request.plan);
    }
}
