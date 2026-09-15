package com.loja.api.dto.purchase;

import java.util.List;
import java.util.UUID;

public record PurchaseRequest(
    UUID userId,
    List<UUID> productIds,
    int total
) {}
