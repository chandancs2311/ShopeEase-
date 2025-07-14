package com.example.ShopEase.repository;

import com.example.ShopEase.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    //  This is the method that was missing
    List<Product> findByCategoryId(int id);
}
