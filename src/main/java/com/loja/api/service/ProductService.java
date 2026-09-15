package com.loja.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.loja.api.dto.product.ProductDto;
import com.loja.api.entity.Product;
import com.loja.api.mapper.ProductMapper;
import com.loja.api.repository.ProductRepository;

@Service 
public class ProductService {
    
    ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductDto getProductById(UUID id) {
        return productRepository.findById(id)
            .map(productMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDto createProduct(ProductDto productRequest) {
        Product product = productMapper.toEntity(productRequest);
        ProductDto productResponse = productMapper.toResponse(productRepository.save(product));
        return productResponse;
    }

    public ProductDto updateProduct(UUID id, ProductDto productRequest) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Product updatedProduct = productMapper.toEntity(productRequest);
        updatedProduct.setId(existingProduct.getId());
        productRepository.save(updatedProduct);

        return productMapper.toResponse(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        productRepository.delete(existingProduct);
    }

    public List<ProductDto> getProductsByIds(List<UUID> productIds) {
        return productRepository.findAllById(productIds).stream()
            .map(productMapper::toResponse)
            .toList();
    }
}
