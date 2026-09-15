package com.loja.api.dto.purchase;

import java.util.UUID;

public record PurchaseItemResponse(
    UUID productId,
    String productName,
    int quantity,
    long unitPriceCents,
    long subtotalCents
) {}