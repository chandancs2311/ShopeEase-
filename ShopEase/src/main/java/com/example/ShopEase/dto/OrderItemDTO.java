package com.example.ShopEase.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private double totalItemPrice;
}
