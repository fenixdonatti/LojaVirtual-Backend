package com.loja.api.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "products")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class Product {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank 
    private String name;

    @NotBlank 
    private int cents;

    private String description;

    @NotBlank
    @Column (name = "qty_stock")
    private int qtyStock;
}
