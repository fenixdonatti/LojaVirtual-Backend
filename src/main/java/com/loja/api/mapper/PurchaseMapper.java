package com.loja.api.mapper;

import java.util.List;
import org.springframework.stereotype.Component;

import com.loja.api.dto.purchase.PurchaseRequest;
import com.loja.api.dto.purchase.PurchaseResponse;
import com.loja.api.dto.product.ProductDto;
import com.loja.api.dto.user.UserResponse;
import com.loja.api.entity.Purchase;
import com.loja.api.entity.Product;
import com.loja.api.entity.User;

@Component
public class PurchaseMapper {

    public Purchase toEntity(PurchaseRequest request, User user, List<Product> products) {
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setProducts(products);
        purchase.setTotal(request.total());
        return purchase;
    }

    public PurchaseResponse toResponse(Purchase purchase) {
        UserResponse userResponse = null;
        if (purchase.getUser() != null) {
            User user = purchase.getUser();
            userResponse = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
            );
        }

        List<ProductDto> productDtos = null;
        if (purchase.getProducts() != null) {
            productDtos = purchase.getProducts().stream()
                .map(product -> new ProductDto(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCents(),
                    product.getQtyStock()
                ))
                .toList();
        }

        return new PurchaseResponse(
            purchase.getId(),
            userResponse,
            productDtos,
            purchase.getTotal()
        );
    }
}
