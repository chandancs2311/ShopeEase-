package com.example.ShopEase.service;

import com.example.ShopEase.model.Order;
import com.example.ShopEase.model.OrderItem;
import com.example.ShopEase.repository.OrderItemRepository;
import com.example.ShopEase.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }

    public OrderItem addOrderItem(OrderItem item) {
        item.setTotalPrice(item.getPrice() * item.getQuantity());
        return orderItemRepository.save(item);
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
