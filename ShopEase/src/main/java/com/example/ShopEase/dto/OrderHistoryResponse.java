package com.example.ShopEase.dto;

import com.example.ShopEase.model.OrderItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderHistoryResponse {
    private int orderId;
    private double totalAmount;
    private String status;
    private Date orderDate;  // ✅ corrected
    private String shippingAddress;
    private List<OrderItem> items;
}
