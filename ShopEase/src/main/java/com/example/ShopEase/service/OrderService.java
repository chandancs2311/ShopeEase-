package com.example.ShopEase.service;

import com.example.ShopEase.dto.OrderHistoryResponse;
import com.example.ShopEase.model.Order;
import com.example.ShopEase.model.OrderItem;
import com.example.ShopEase.model.User;
import com.example.ShopEase.repository.OrderItemRepository;
import com.example.ShopEase.repository.OrderRepository;
import com.example.ShopEase.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    // 🟢 Place Order
    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }

    // 🟢 Add Item to Order
    public OrderItem addOrderItem(OrderItem item) {
        item.setTotalPrice(item.getPrice() * item.getQuantity());
        return orderItemRepository.save(item);
    }

    public List<OrderItem> getOrderItems(int orderId, int userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getUserId() != userId) {
            throw new AccessDeniedException("You are not allowed to view this order");
        }

        return orderItemRepository.findByOrder(order); // ✅ FIXED!
    }


    // ✅ Full History with Items (only for logged-in user)
    public List<OrderHistoryResponse> getOrderHistoryByUser(int userId) {
        // Optional: Add extra check if needed: like whether the user exists
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderHistoryResponse> responseList = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrder(order);

            OrderHistoryResponse response = new OrderHistoryResponse();
            response.setOrderId(order.getOrderId());
            response.setTotalAmount(order.getTotalAmount());
            response.setStatus(order.getStatus());
            response.setOrderDate(order.getOrderDate());
            response.setShippingAddress(order.getShippingAddress());
            response.setItems(items);

            responseList.add(response);
        }

        return responseList;
    }

    // 🟢 Admin-only: View All Orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 🟢 Admin/User: Delete an order
    public void deleteOrder(int orderId) {
        orderRepository.deleteById(orderId);
    }

    public boolean isOrderOwnedByUser(int orderId, int userId) {
        return orderRepository.findById(orderId)
                .map(order -> order.getUserId() == userId)
                .orElse(false);
    }

}
