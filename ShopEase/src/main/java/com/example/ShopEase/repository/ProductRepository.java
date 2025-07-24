package com.example.ShopEase.repository;

import com.example.ShopEase.model.Product;
import com.example.ShopEase.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory_Id(int categoryId);  // ✅ FIXED!
}


