package com.example.ShopEase.dto;



import lombok.*;
import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private int id;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
}
