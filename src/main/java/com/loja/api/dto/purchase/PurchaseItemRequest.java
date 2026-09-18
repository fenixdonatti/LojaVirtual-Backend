package com.loja.api.dto.purchase;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseItemRequest(
    @NotNull UUID productId,
    @Positive int quantity
) {}
