package com.loja.api.mapper;

import org.springframework.stereotype.Component;

import com.loja.api.dto.product.ProductDto;
import com.loja.api.entity.Product;

@Component 
public class ProductMapper {
    
    public Product toEntity(ProductDto request) {
        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCents(request.cents());
        product.setQtyStock(request.qtyStock());

        return product;
    }

    public ProductDto toResponse(Product product) {
        return new ProductDto(
            product.getId(),
            product.getName(), 
            product.getDescription(),
            product.getCents(),
            product.getQtyStock()
        );
    }
}
