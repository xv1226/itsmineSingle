package com.sparta.itsminesingle.domain.product.repository;

import static com.sparta.itsminesingle.domain.product.entity.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsminesingle.domain.product.entity.Product;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Cacheable("productById")
    @Override
    public Optional<Product> findActiveProductById(Long productId) {
        Product foundProduct = jpaQueryFactory
                .selectFrom(product)
                .where(product.id.eq(productId)
                        .and(product.deletedAt.isNull()))
                .fetchOne();
        return Optional.ofNullable(foundProduct);
    }

}
