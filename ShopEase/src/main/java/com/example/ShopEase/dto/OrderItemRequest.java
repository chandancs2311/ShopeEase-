package com.example.ShopEase.dto;
import lombok.*;
@Data
public class OrderItemRequest {
    private int orderId;
    private int productId;
    private int quantity;
}

