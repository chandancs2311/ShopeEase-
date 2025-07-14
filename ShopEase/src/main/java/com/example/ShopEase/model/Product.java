package com.example.ShopEase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // Add other fields like description, price, imageUrl, etc.
    private String description;
    private double price;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;
}
