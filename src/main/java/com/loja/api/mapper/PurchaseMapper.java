package com.loja.api.mapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.loja.api.dto.purchase.PurchaseItemResponse;
import com.loja.api.dto.purchase.PurchaseRequest;
import com.loja.api.dto.purchase.PurchaseResponse;
import com.loja.api.entity.Product;
import com.loja.api.entity.Purchase;
import com.loja.api.entity.PurchaseItem;
import com.loja.api.entity.User;
import com.loja.api.enums.PurchaseStatus;

@Component
public class PurchaseMapper {

	public Purchase toEntity(PurchaseRequest request, User user, List<Product> products) {
		Map<UUID, Product> productsById = products.stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		Purchase purchase = new Purchase();
		purchase.setUser(user);
		purchase.setStatus(PurchaseStatus.PENDING);

		long totalCents = 0;
		for (var itemRequest : request.items()) {
			Product product = productsById.get(itemRequest.productId());
			if (product == null) {
				throw new IllegalArgumentException("Product not found: " + itemRequest.productId());
			}

			long subtotalCents = Math.multiplyExact(
				(long) product.getCents(),
				itemRequest.quantity()
			);

			PurchaseItem item = new PurchaseItem();
			item.setPurchase(purchase);
			item.setProduct(product);
			item.setQuantity(itemRequest.quantity());
			item.setUnitPriceCents(product.getCents());
			item.setSubtotalCents(subtotalCents);

			purchase.getItems().add(item);
			totalCents = Math.addExact(totalCents, subtotalCents);
		}

		purchase.setTotalCents(totalCents);
		return purchase;
	}

	public PurchaseResponse toResponse(Purchase purchase) {
		List<PurchaseItemResponse> items = purchase.getItems().stream()
			.map(item -> new PurchaseItemResponse(
				item.getProduct().getId(),
				item.getProduct().getName(),
				item.getQuantity(),
				item.getUnitPriceCents(),
				item.getSubtotalCents()
			))
			.toList();

		return new PurchaseResponse(
			purchase.getId(),
			purchase.getUser().getId(),
			items,
			purchase.getTotalCents(),
			purchase.getStatus()
		);
	}
}
