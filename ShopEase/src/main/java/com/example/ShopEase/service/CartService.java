package com.example.ShopEase.service;
import com.example.ShopEase.dto.CartItemResponse;
import com.example.ShopEase.repository.ProductRepository;

import com.example.ShopEase.model.Cart;
import com.example.ShopEase.model.CartItem;
import com.example.ShopEase.model.Product;
import com.example.ShopEase.model.User;
import com.example.ShopEase.repository.CartItemRepository;
import com.example.ShopEase.repository.CartRepository;
import com.example.ShopEase.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.lang.ExceptionInInitializerError;
import com.example.ShopEase.dto.CartItemRequest;

import java.math.BigDecimal;
import java.util.List;
@Data
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    public Cart createCart(int userId) {
        //  Step 1: Load the actual User from the DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Step 2: Create and assign user to cart
        Cart cart = new Cart();
        cart.setUser(user);

        //  Step 3: Save the cart
        return cartRepository.save(cart);
    }



    public CartItem addItem(CartItemRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setPrice(product.getPrice());
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        return cartItemRepository.save(item);
    }





    public void removeItem(int userId, int productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cart.getItems();

        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            items.remove(itemToRemove);
            cartItemRepository.delete(itemToRemove);
            cartRepository.save(cart); // Optional: to update total price, etc.
        } else {
            throw new RuntimeException("Product not found in cart");
        }
    }


    public List<CartItemResponse> getCartItemsByUserAndCartId(int userId, int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getUser().getId() != userId) {
            throw new AccessDeniedException("Cart does not belong to this user");
        }

        return cart.getItems().stream().map(item -> {
            CartItemResponse dto = new CartItemResponse();
            dto.setId(item.getId());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setTotalPrice(item.getTotalPrice());
            return dto;
        }).toList();
    }

}
