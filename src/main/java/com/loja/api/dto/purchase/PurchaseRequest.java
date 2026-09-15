package com.loja.api.dto.purchase;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequest(
    @NotNull UUID userId,
    @NotEmpty @Valid List<PurchaseItemRequest> items
) {}