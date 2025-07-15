package com.example.ShopEase.service;

import com.example.ShopEase.model.Product;
import com.example.ShopEase.repository.ProductRepository;
import com.example.ShopEase.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    // New method to get products by category ID
    public List<Product> getByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
