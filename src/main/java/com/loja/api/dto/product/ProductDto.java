package com.loja.api.dto.product;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ProductDto(
    @NotBlank 
    UUID id,

    @NotBlank 
    String name,

    String description,
    
    @NotBlank 
    int cents,

    @NotBlank
    int qtyStock
) {}
