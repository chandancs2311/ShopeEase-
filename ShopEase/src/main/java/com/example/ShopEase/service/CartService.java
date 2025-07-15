package com.example.ShopEase.service;
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
import org.springframework.stereotype.Service;
import java.lang.ExceptionInInitializerError;

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



    public CartItem addItem(CartItem item) {
        //  Load actual product from DB
        Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        item.setProduct(product); // Now full product with name, price, etc.
        item.setPrice(product.getPrice()); // Ensures accurate pricing
        item.setTotalPrice(product.getPrice() * item.getQuantity());

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


    public List<CartItem> getItems(int cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}
