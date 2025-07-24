package com.example.ShopEase.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@Data
public class OrderHistoryResponse {
    private int orderId;
    private double totalAmount;
    private String status;
    private Date orderDate;
    private String shippingAddress;
    private List<OrderItemDTO> items; // ✅ Use DTO here
}
