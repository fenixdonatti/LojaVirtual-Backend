package com.loja.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loja.api.dto.purchase.PurchaseRequest;
import com.loja.api.dto.purchase.PurchaseResponse;
import com.loja.api.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    private final PurchaseService purchaseService;

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable UUID id) {
        try {
            PurchaseResponse purchaseResponse = purchaseService.getPurchaseById(id);
            return ResponseEntity.ok(purchaseResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(PurchaseRequest purchaseRequest) {
        try {
            PurchaseResponse purchaseResponse = purchaseService.createPurchase(purchaseRequest);
            return ResponseEntity.ok(purchaseResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponse> updatePurchase(@PathVariable UUID id, PurchaseRequest purchaseRequest) {
        try {
            PurchaseResponse purchaseResponse = purchaseService.updatePurchase(id, purchaseRequest);
            return ResponseEntity.ok(purchaseResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable UUID id) {
        try {
            purchaseService.deletePurchase(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}