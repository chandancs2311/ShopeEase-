package com.example.ShopEase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private double price;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;
}

