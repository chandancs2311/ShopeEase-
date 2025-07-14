package com.example.ShopEase.controller;

import com.example.ShopEase.model.Order;
import com.example.ShopEase.model.OrderItem;
import com.example.ShopEase.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    @PostMapping("/add-item")
    public OrderItem addItem(@RequestBody OrderItem item) {
        return orderService.addOrderItem(item);
    }

    @GetMapping("/items/{orderId}")
    public List<OrderItem> getItems(@PathVariable int orderId) {
        return orderService.getOrderItems(orderId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
