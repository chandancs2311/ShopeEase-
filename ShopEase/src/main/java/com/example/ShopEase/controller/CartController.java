package com.example.ShopEase.controller;

import com.example.ShopEase.model.Cart;
import com.example.ShopEase.model.CartItem;
import com.example.ShopEase.service.CartService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Data
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<String> createCart(@RequestBody Map<String, Integer> body) {
        int userId = body.get("userId");
        cartService.createCart(userId);
        return ResponseEntity.ok("Cart created successfully");
    }


    @PostMapping("/add")
    public CartItem addItem(@RequestBody CartItem item) {
        return cartService.addItem(item);
    }

    @GetMapping("/items/{cartId}")
    public List<CartItem> getCartItems(@PathVariable int cartId) {
        return cartService.getItems(cartId);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItemFromCart(@RequestParam int userId, @RequestParam int productId) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }
}
