package com.example.ShopEase.dto;

import lombok.*;

@Data
public class CartItemRequest {
    private int cartId;
    private int productId;
    private int quantity;
}
