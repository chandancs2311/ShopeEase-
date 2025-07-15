package com.example.ShopEase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   private int userId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //  Add this field

    private double price;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
