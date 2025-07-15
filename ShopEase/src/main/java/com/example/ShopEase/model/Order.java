package com.example.ShopEase.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int userId;
    private Date orderDate = new Date();
    private String shippingAddress;
    private String status;
    private double totalAmount;
}

