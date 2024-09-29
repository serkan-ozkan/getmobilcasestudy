package com.mobilgetstudy.repository;

import com.mobilgetstudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
