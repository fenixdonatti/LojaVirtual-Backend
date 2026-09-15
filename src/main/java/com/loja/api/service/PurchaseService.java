package com.loja.api.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.loja.api.dto.purchase.PurchaseRequest;
import com.loja.api.dto.purchase.PurchaseResponse;
import com.loja.api.entity.Purchase;
import com.loja.api.entity.Product;
import com.loja.api.entity.User;
import com.loja.api.mapper.PurchaseMapper;
import com.loja.api.repository.PurchaseRepository;
import com.loja.api.repository.ProductRepository;
import com.loja.api.repository.UserRepository;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper,
                          UserRepository userRepository, ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public PurchaseResponse getPurchaseById(UUID id) {
        return purchaseRepository.findById(id)
            .map(purchaseMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Purchase not found"));
    }

    public PurchaseResponse createPurchase(PurchaseRequest purchaseRequest) {
        User user = userRepository.findById(purchaseRequest.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> products = productRepository.findAllById(purchaseRequest.productIds())
            .stream()
            .filter(product -> purchaseRequest.productIds().contains(product.getId()))
            .collect(Collectors.toList());

        if (products.size() != purchaseRequest.productIds().size()) {
            throw new RuntimeException("One or more products not found");
        }

        Purchase purchase = purchaseMapper.toEntity(purchaseRequest, user, products);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toResponse(savedPurchase);
    }

    public PurchaseResponse updatePurchase(UUID id, PurchaseRequest purchaseRequest) {
        Purchase existingPurchase = purchaseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Purchase not found"));

        User user = userRepository.findById(purchaseRequest.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> products = productRepository.findAllById(purchaseRequest.productIds())
            .stream()
            .filter(product -> purchaseRequest.productIds().contains(product.getId()))
            .collect(Collectors.toList());

        if (products.size() != purchaseRequest.productIds().size()) {
            throw new RuntimeException("One or more products not found");
        }

        Purchase updatedPurchase = purchaseMapper.toEntity(purchaseRequest, user, products);
        updatedPurchase.setId(existingPurchase.getId());
        purchaseRepository.save(updatedPurchase);

        return purchaseMapper.toResponse(updatedPurchase);
    }

    public void deletePurchase(UUID id) {
        Purchase existingPurchase = purchaseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Purchase not found"));

        purchaseRepository.delete(existingPurchase);
    }
}