package com.example.ShopEase.controller;

import com.example.ShopEase.model.Cart;
import com.example.ShopEase.model.CartItem;
import com.example.ShopEase.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    @PostMapping("/add")
    public CartItem addItem(@RequestBody CartItem item) {
        return cartService.addItem(item);
    }

    @GetMapping("/items/{cartId}")
    public List<CartItem> getCartItems(@PathVariable int cartId) {
        return cartService.getItems(cartId);
    }
}
