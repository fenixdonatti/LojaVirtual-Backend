package com.loja.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loja.api.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    
}
