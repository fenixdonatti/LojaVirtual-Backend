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

import com.loja.api.dto.product.ProductDto;
import com.loja.api.service.ProductService;

@RestController 
@RequestMapping ("/product")
public class ProductController {
    
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    private final ProductService productService;

    @GetMapping ("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        try {
            ProductDto productDto = productService.getProductById(id);
            return ResponseEntity.ok(productDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(ProductDto productRequest) {
        try {
            ProductDto productResponse = productService.createProduct(productRequest);
            return ResponseEntity.ok(productResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, ProductDto productRequest) {
        try {
            ProductDto productResponse = productService.updateProduct(id, productRequest);
            return ResponseEntity.ok(productResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
