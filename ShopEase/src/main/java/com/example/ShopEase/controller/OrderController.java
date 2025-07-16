package com.example.ShopEase.controller;

import com.example.ShopEase.dto.OrderItemRequest;
import com.example.ShopEase.service.JwtService;
import com.example.ShopEase.model.Order;

import com.example.ShopEase.model.OrderItem;
import com.example.ShopEase.service.OrderService;
import lombok.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JwtService jwtService;

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody Order order, HttpServletRequest request) {
        int tokenUserId = jwtService.extractUserIdFromToken(request);
        order.setUserId(tokenUserId); // bind token userId to order
        orderService.placeOrder(order);
        return ResponseEntity.ok("Order placed successfully");
    }

    @PostMapping("/add-item")
    public ResponseEntity<String> addItem(@RequestBody OrderItemRequest request, HttpServletRequest httpReq) {
        int tokenUserId = jwtService.extractUserIdFromToken(httpReq);

        if (!orderService.isOrderOwnedByUser(request.getOrderId(), tokenUserId)) {
            return ResponseEntity.status(403).body("You can't add items to another user's order.");
        }

        orderService.addOrderItem(request); // Service handles mapping
        return ResponseEntity.ok("Item added successfully");
    }




    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getUserOrderHistory(@PathVariable int userId, HttpServletRequest request) {
        String role = jwtService.extractRoleFromToken(request);
        int tokenUserId = jwtService.extractUserIdFromToken(request);

        // ✅ Allow if token matches the pathUserId OR role is ADMIN
        if (tokenUserId != userId && !"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Access Denied: You can only view your own order history.");
        }

        return ResponseEntity.ok(orderService.getOrderHistoryByUser(userId));
    }




    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable int orderId, HttpServletRequest request) {
        int tokenUserId = jwtService.extractUserIdFromToken(request);

        //  Correct usage — check if the order belongs to this user
        if (!orderService.isOrderOwnedByUser(orderId, tokenUserId)) {
            return ResponseEntity.status(403).body("You can only delete your own order.");
        }

        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }

}


