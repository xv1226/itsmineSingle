package com.sparta.itsminesingle.domain.product.repository;

import static com.sparta.itsminesingle.global.response.ResponseExceptionEnum.CATEGORY_NOT_FOUND;
import static com.sparta.itsminesingle.global.response.ResponseExceptionEnum.PRODUCT_NOT_FOUND;

import com.sparta.itsminesingle.domain.category.entity.Category;
import com.sparta.itsminesingle.domain.category.repository.CategoryRepository;
import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.user.repository.UserRepository;
import com.sparta.itsminesingle.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAdapter {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public Product getProduct(Long productId) {
        return productRepository.findActiveProductById(productId)
                .orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND));
    }

    public Category findCategoryByCategoryName(String categoryName) {
        return categoryRepository.findCategoryByCategoryName(
                        categoryName)
                .orElseThrow(() -> new DataNotFoundException(CATEGORY_NOT_FOUND));
    }
}
