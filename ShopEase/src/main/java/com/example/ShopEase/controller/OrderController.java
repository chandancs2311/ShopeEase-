package com.example.ShopEase.controller;

import com.example.ShopEase.model.Order;
import com.example.ShopEase.model.OrderItem;
import com.example.ShopEase.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        orderService.placeOrder(order);
        return ResponseEntity.ok("placed order successfully");
    }

    @PostMapping("/add-item")
    public ResponseEntity<String> addItem(@RequestBody OrderItem item) {
         orderService.addOrderItem(item);
         return ResponseEntity.ok("added item");
    }

    @GetMapping("/items/{orderId}")
    public List<OrderItem> getItems(@PathVariable int orderId) {
        return orderService.getOrderItems(orderId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }

}
