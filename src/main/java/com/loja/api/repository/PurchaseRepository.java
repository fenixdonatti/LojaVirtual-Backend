package com.loja.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loja.api.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

}