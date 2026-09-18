package com.loja.api.dto.purchase;

import java.util.List;
import java.util.UUID;

import com.loja.api.enums.PurchaseStatus;

public record PurchaseResponse(
    UUID id,
    UUID userId,
    List<PurchaseItemResponse> items,
    long totalCents,
    PurchaseStatus status
) {}
