package com.example.ShopEase.service;

import com.example.ShopEase.model.Cart;
import com.example.ShopEase.model.CartItem;
import com.example.ShopEase.repository.CartItemRepository;
import com.example.ShopEase.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.lang.ExceptionInInitializerError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public CartItem addItem(CartItem item) {
        item.setTotalPrice(item.getPrice() * item.getQuantity());
        return cartItemRepository.save(item);
    }

    public List<CartItem> getItems(int cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}
