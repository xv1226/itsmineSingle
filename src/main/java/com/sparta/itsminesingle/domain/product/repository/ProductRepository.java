package com.sparta.itsminesingle.domain.product.repository;

import com.sparta.itsminesingle.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
