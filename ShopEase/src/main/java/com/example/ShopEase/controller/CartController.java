package com.example.ShopEase.controller;

import com.example.ShopEase.dto.CartItemRequest;
import com.example.ShopEase.dto.CartItemResponse;
import com.example.ShopEase.model.Cart;
import com.example.ShopEase.model.CartItem;
import com.example.ShopEase.service.CartService;
import com.example.ShopEase.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtService jwtService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createCart(@PathVariable int userId, HttpServletRequest request) {
        if (!jwtService.isTokenUserMatchingRequest(request, userId)) {
            return ResponseEntity.status(403).body("Access Denied: You can only create your own cart.");
        }
        cartService.createCart(userId);
        return ResponseEntity.ok("Cart created successfully");
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItemRequest request) {
        CartItem addedItem = cartService.addItem(request);
        return ResponseEntity.ok(addedItem);
    }

    @GetMapping("/items/{userId}/{cartId}")
    public ResponseEntity<?> getCartItemsByUserAndCartId(
            @PathVariable int userId,
            @PathVariable int cartId,
            HttpServletRequest request) {

        String role = jwtService.extractRoleFromToken(request);
        int tokenUserId = jwtService.extractUserIdFromToken(request);

        //  Only allow if the requester is the user or is admin
        if (tokenUserId != userId && !"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Access Denied: You cannot view another user's cart.");
        }

        return ResponseEntity.ok(cartService.getCartItemsByUserAndCartId(userId, cartId));
    }




    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestParam int userId, @RequestParam int productId, HttpServletRequest request) {
        if (!jwtService.isTokenUserMatchingRequest(request, userId)) {
            return ResponseEntity.status(403).body("Access Denied: You can only remove from your own cart.");
        }
        cartService.removeItem(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }
}
