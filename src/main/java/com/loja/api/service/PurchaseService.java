package com.loja.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loja.api.dto.purchase.PurchaseRequest;
import com.loja.api.dto.purchase.PurchaseResponse;
import com.loja.api.entity.Product;
import com.loja.api.entity.Purchase;
import com.loja.api.entity.User;
import com.loja.api.enums.PurchaseStatus;
import com.loja.api.mapper.PurchaseMapper;
import com.loja.api.repository.ProductRepository;
import com.loja.api.repository.PurchaseRepository;
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

	@Transactional(readOnly = true)
	public PurchaseResponse getPurchaseById(UUID id) {
		return purchaseRepository.findById(id)
			.map(purchaseMapper::toResponse)
			.orElseThrow(() -> new RuntimeException("Purchase not found"));
	}

	@Transactional
	public PurchaseResponse createPurchase(PurchaseRequest purchaseRequest) {
		User user = findUser(purchaseRequest.userId());
		List<Product> products = findProducts(purchaseRequest);
		Map<UUID, Product> productsById = productsById(products);

		decreaseStock(purchaseRequest, productsById);

		Purchase purchase = purchaseMapper.toEntity(purchaseRequest, user, products);
		Purchase savedPurchase = purchaseRepository.save(purchase);
		return purchaseMapper.toResponse(savedPurchase);
	}

	@Transactional
	public PurchaseResponse updatePurchase(UUID id, PurchaseRequest purchaseRequest) {
		Purchase existingPurchase = findPurchase(id);
		ensurePending(existingPurchase);

		restoreStock(existingPurchase);

		User user = findUser(purchaseRequest.userId());
		List<Product> products = findProducts(purchaseRequest);
		decreaseStock(purchaseRequest, productsById(products));

		Purchase updatedPurchase = purchaseMapper.toEntity(purchaseRequest, user, products);
		existingPurchase.setUser(updatedPurchase.getUser());
		existingPurchase.setItems(updatedPurchase.getItems());
		existingPurchase.getItems().forEach(item -> item.setPurchase(existingPurchase));
		existingPurchase.setTotalCents(updatedPurchase.getTotalCents());
		existingPurchase.setStatus(updatedPurchase.getStatus());

		Purchase savedPurchase = purchaseRepository.save(existingPurchase);
		return purchaseMapper.toResponse(savedPurchase);
	}

	@Transactional
	public void deletePurchase(UUID id) {
		Purchase purchase = findPurchase(id);
		ensurePending(purchase);
		restoreStock(purchase);
		purchaseRepository.delete(purchase);
	}

	private User findUser(UUID id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));
	}

	private Purchase findPurchase(UUID id) {
		return purchaseRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Purchase not found"));
	}

	private List<Product> findProducts(PurchaseRequest request) {
		List<UUID> productIds = request.items().stream()
			.map(item -> item.productId())
			.toList();

		List<Product> products = productRepository.findAllById(productIds);
		Map<UUID, Product> productsById = productsById(products);

		if (productIds.stream().anyMatch(productId -> !productsById.containsKey(productId))) {
			throw new RuntimeException("One or more products not found");
		}

		return products;
	}

	private Map<UUID, Product> productsById(List<Product> products) {
		return products.stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));
	}

	private void decreaseStock(PurchaseRequest request, Map<UUID, Product> productsById) {
		for (var item : request.items()) {
			Product product = productsById.get(item.productId());
			if (product.getQtyStock() < item.quantity()) {
				throw new RuntimeException("Insufficient stock for product: " + product.getId());
			}
			product.setQtyStock(product.getQtyStock() - item.quantity());
		}
		productRepository.saveAll(productsById.values());
	}

	private void restoreStock(Purchase purchase) {
		purchase.getItems().forEach(item -> {
			Product product = item.getProduct();
			product.setQtyStock(product.getQtyStock() + item.getQuantity());
		});
		productRepository.saveAll(
			purchase.getItems().stream()
				.map(item -> item.getProduct())
				.toList()
		);
	}

	private void ensurePending(Purchase purchase) {
		if (purchase.getStatus() != PurchaseStatus.PENDING) {
			throw new RuntimeException("Only pending purchases can be changed");
		}
	}
}