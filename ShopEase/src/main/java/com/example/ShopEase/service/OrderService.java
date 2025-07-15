package com.example.ShopEase.service;

import com.example.ShopEase.model.Order;
import com.example.ShopEase.model.OrderItem;
import com.example.ShopEase.repository.OrderItemRepository;
import com.example.ShopEase.repository.OrderRepository;
import com.example.ShopEase.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.ShopEase.dto.OrderHistoryResponse;
import java.util.ArrayList;

import java.util.List;
@Data
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }

    public OrderItem addOrderItem(OrderItem item) {
        item.setTotalPrice(item.getPrice() * item.getQuantity());
        return orderItemRepository.save(item);
    }

    public List<OrderItem> getOrderItems(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderItemRepository.findByOrder(order);
    }
    public List<OrderHistoryResponse> getOrderHistoryByUser(int userId) {
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


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(int orderId) {
        orderRepository.deleteById(orderId);
    }
}
