package com.sparta.itsminesingle.domain.product.repository;

import com.sparta.itsminesingle.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface CustomProductRepository {

    Optional<Product> findActiveProductById(@Param("productId") Long productId);

}
