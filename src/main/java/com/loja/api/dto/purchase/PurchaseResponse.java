package com.loja.api.dto.purchase;

import java.util.List;
import java.util.UUID;

import com.loja.api.dto.product.ProductDto;
import com.loja.api.dto.user.UserResponse;

public record PurchaseResponse(
    UUID id,
    UserResponse user,
    List<ProductDto> products,
    int total
) {}
