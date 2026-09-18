package com.loja.api.dto.auth;

public record LoginResponse(
    String token,
    long expiresIn
) {}
